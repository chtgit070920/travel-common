package com.travel.common.mybatis.interceptor;

import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import com.travel.common.mybatis.Page;
import com.travel.common.utils.ReflectionUtil;

/**
 * 数据库分页插件，只拦截查询语句.
 */
@Intercepts({@Signature(type = Executor.class, method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class PaginationInterceptor extends BaseInterceptor {

    private static final long serialVersionUID = 1L;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

    	   //
           final MappedStatement oldMappedStatement = (MappedStatement) invocation.getArgs()[0];
        
//          拦截需要分页的SQL
//          if (mappedStatement.getId().matches(_SQL_PATTERN)) {
//          if (StringUtils.indexOfIgnoreCase(mappedStatement.getId(), _SQL_PATTERN) != -1) {
            Object oldParameter = invocation.getArgs()[1];
            
            //BoundSql--确定sql
            BoundSql oldBoundSql = oldMappedStatement.getBoundSql(oldParameter);
            Object oldParameter2 = oldBoundSql.getParameterObject();

            //获取分页参数对象
            Page<Object> page =oldParameter2 != null?page = convertParameter(oldParameter2):null;

            //如果设置了分页对象，则进行分页
            if (page != null) {

            	if (StringUtils.isBlank(oldBoundSql.getSql())){
                    return null;
                }
                String originalSql = oldBoundSql.getSql().trim();
            	
                //设置总记录数
                page.setTotal(SQLHelper.getCount(originalSql, null, oldMappedStatement, oldParameter2, oldBoundSql, log));

                //分页查询 本地化对象 修改数据库注意修改实现
                String pageSql = SQLHelper.generatePageSql(originalSql, page, DIALECT);
//              if (log.isDebugEnabled()) {
//                 log.debug("PAGE SQL:" + StringUtils.replace(pageSql, "\n", ""));
//              }
                
                //args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
                invocation.getArgs()[2] = new RowBounds(RowBounds.NO_ROW_OFFSET, RowBounds.NO_ROW_LIMIT);
                
                BoundSql newBoundSql = new BoundSql(oldMappedStatement.getConfiguration(), pageSql, oldBoundSql.getParameterMappings(), oldBoundSql.getParameterObject());
                
                //解决MyBatis 分页foreach 参数失效 start
                if (ReflectionUtil.getFieldValue(oldBoundSql, "metaParameters") != null) {
                    MetaObject mo = (MetaObject) ReflectionUtil.getFieldValue(oldBoundSql, "metaParameters");
                    ReflectionUtil.setFieldValue(newBoundSql, "metaParameters", mo);
                }
                //解决MyBatis 分页foreach 参数失效 end
                
                MappedStatement newMappedStatement = copyFromMappedStatement(oldMappedStatement, new BoundSqlSqlSource(newBoundSql));

                invocation.getArgs()[0] = newMappedStatement;
            }
//        }
        return invocation.proceed();
    }


    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        super.initProperties(properties);
    }

    private MappedStatement copyFromMappedStatement(MappedStatement ms,
                                                    SqlSource newSqlSource) {
        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(),
                ms.getId(), newSqlSource, ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null) {
            for (String keyProperty : ms.getKeyProperties()) {
                builder.keyProperty(keyProperty);
            }
        }
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(ms.getResultMaps());
        builder.cache(ms.getCache());
        return builder.build();
    }

    public static class BoundSqlSqlSource implements SqlSource {
        BoundSql boundSql;

        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }

        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }
    }
}

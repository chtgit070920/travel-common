package com.travel.common.interceptor;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NamedThreadLocal;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.travel.common.utils.DateUtil;

/**
 * @description:记录请求日志
 */
public class LogInterceptor implements HandlerInterceptor{
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private static final ThreadLocal<Long> startTimeThreadLocal =new NamedThreadLocal<Long>("ThreadLocal StartTime");
			
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		if (logger.isDebugEnabled()){
			long beginTime = System.currentTimeMillis();//1、开始时间  
	        
			startTimeThreadLocal.set(beginTime);		//线程绑定变量（该数据只有当前请求的线程可见）  
	        
			logger.debug("-------------------------------request start-------------------------------");
	        logger.debug("URI: {} 计时: {} ",request.getRequestURI(), new SimpleDateFormat("HH:mm:ss.SSS").format(beginTime));
	        	
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
		String viewname=null;
		if (modelAndView != null){
			viewname=modelAndView.getViewName();
		}
		if(StringUtils.isNotBlank(viewname)){
			logger.info(" ViewName: "+viewname) ;
		}else{
			logger.info(" ViewName: 无！") ;
		}
		
		
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
		    //logger.warn(response.getClass().getName());
		   // logger.warn(response.toString());
			if (logger.isDebugEnabled()){
				long beginTime = startTimeThreadLocal.get();//得到线程绑定的局部变量（开始时间）  
				long endTime = System.currentTimeMillis(); 	//2、结束时间  
		        logger.debug("URI: {} 计时：{}  耗时：{} 最大内存: {}m  已分配内存: {}m  已分配内存中的剩余空间: {}m  最大可用内存: {}m",
		        		request.getRequestURI(),
		        		new SimpleDateFormat("HH:mm:ss.SSS").format(endTime), 
		        		
		        		DateUtil.formatDateTime(endTime - beginTime),
						Runtime.getRuntime().maxMemory()/1024/1024, 
						Runtime.getRuntime().totalMemory()/1024/1024, 
						Runtime.getRuntime().freeMemory()/1024/1024, 
						(Runtime.getRuntime().maxMemory()-Runtime.getRuntime().totalMemory()+Runtime.getRuntime().freeMemory())/1024/1024
				);
		        logger.debug("-------------------------------request end-------------------------------");
			}
		
	}

}

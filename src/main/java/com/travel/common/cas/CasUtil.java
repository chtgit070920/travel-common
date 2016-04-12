package com.travel.common.cas;

import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.validation.Assertion;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Created by smart on 16-1-20.
 */
public class CasUtil {


    private  static final String CAS_SERVER_USERID_KEY="id";
    private  static final String CAS_SERVER_USERNAME_KEY="name";

    //获取断言
    public static Assertion getAssertion(final ServletRequest servletRequest){

        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpSession session = request.getSession(false);
        final Assertion assertion = (Assertion) (session == null ? request
                .getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION) : session
                .getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION));
        return assertion;
    }

    //获取principal
    public static AttributePrincipal getPrincipal(final ServletRequest servletRequest){
        Assertion assertion =getAssertion(servletRequest);
        if(null!=assertion) return assertion.getPrincipal();
        return null;
    }

    //获取所有返回数据
    public static  Map<String, Object> getAttribute(final ServletRequest servletRequest){
        AttributePrincipal attributePrincipal =getPrincipal(servletRequest);
        if(null!=attributePrincipal) return attributePrincipal.getAttributes();
        return null;
    }


    //获取用户id
    public static String getUserId(final ServletRequest servletRequest){
        Map<String, Object> attributes=getAttribute(servletRequest);
        if(null!=attributes)return (String)attributes.get(CAS_SERVER_USERID_KEY);
        else return null;
    }

    //获取用户name
    public static String getUserName(final ServletRequest servletRequest){
        Map<String, Object> attributes=getAttribute(servletRequest);
        if(null!=attributes) return (String)attributes.get(CAS_SERVER_USERNAME_KEY);
        else return null;
    }

}

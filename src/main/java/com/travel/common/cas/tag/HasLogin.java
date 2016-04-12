package com.travel.common.cas.tag;

import com.travel.common.cas.CasUtil;
import org.jasig.cas.client.validation.Assertion;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Created by smart on 16-1-20.
 */
public class HasLogin implements Tag {


    private PageContext pageContext;

    @Override
    public void setPageContext(PageContext pc) {
        this.pageContext = pc;
    }

    @Override
    public void setParent(Tag t) {}
    @Override
    public Tag getParent() {
        return null;
    }

    @Override
    public int doStartTag() throws JspException {
        HttpServletRequest request =(HttpServletRequest) pageContext.getRequest();
        Assertion assertion=CasUtil.getAssertion(request);
        if(assertion!=null){
            return TagSupport.EVAL_BODY_INCLUDE;
        }else{
            return TagSupport.SKIP_BODY;
        }

    }

    @Override
    public int doEndTag() throws JspException {
        return 0;
    }

    @Override
    public void release() {

    }
}

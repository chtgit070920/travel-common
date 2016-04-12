package com.travel.common.cas.tag;

import com.travel.common.cas.CasUtil;
import com.travel.common.utils.ExceptionUtil;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import java.io.IOException;

/**
 * Created by smart on 16-1-20.
 */
public class UsernameTag implements Tag {

    private PageContext pageContext;

    @Override
    public void setPageContext(PageContext pc) {
        this.pageContext = pc;
    }

    @Override
    public void setParent(Tag t) {

    }

    @Override
    public Tag getParent() {
        return null;
    }

    @Override
    public int doStartTag() throws JspException {
        HttpServletRequest request =(HttpServletRequest) pageContext.getRequest();
        String username= CasUtil.getUserName(request);
        if (StringUtils.isEmpty(username)) username=StringUtils.EMPTY;
        JspWriter out = pageContext.getOut();
        try {
            out.write(username);
        } catch (IOException e) {
            throw ExceptionUtil.unchecked(e);
        }
        return 0;
    }

    @Override
    public int doEndTag() throws JspException {
        return 0;
    }

    @Override
    public void release() {

    }
}

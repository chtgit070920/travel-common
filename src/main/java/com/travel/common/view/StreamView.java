package com.travel.common.view;

import java.io.OutputStream;
import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.view.AbstractView;

import com.travel.common.utils.MIMEUtil;

import eu.medsea.mimeutil.MimeType;

/**
 * @description  
 *
 * @date 2014-11-21 上午10:22:39
 *
 * @author 崔红涛
 *
 */
public class StreamView extends AbstractView {
	
	public final static String STREAM_VIEW_NAME="stream";
	public final static String STREAM_DATA_KEY="stream";
	
	private final static Logger logger = Logger.getLogger(StreamView.class);

	private final static String DEFAULT_CONTENT_TYPE="application/octet-stream";
	
	protected void renderMergedOutputModel(Map<String, Object> model,HttpServletRequest request, HttpServletResponse response)throws Exception {
			
		byte[]data=(byte[])model.get(STREAM_DATA_KEY);
		
		if(data==null)data=new byte[]{};
		
		String contentType=decideContentType(data);
		
		//super.setContentType(contentType);
		
		response.setContentType(contentType);
		 
		response.setHeader("Cache-Control","max-age=3600"); //缓存一小时
			 
	    OutputStream os = response.getOutputStream();
	     
		os.write(data);
		os.flush();
	    os.close();
		 
	}
	
	
	private String decideContentType(byte[]data){
		
		if(null==data||0==data.length) {
			logger.warn("stream view will use default_content_type:"+DEFAULT_CONTENT_TYPE);
			return DEFAULT_CONTENT_TYPE;
		}
		 
		Collection<MimeType> mimeTypes = MIMEUtil.getMimeTypes(data);  
		
		MimeType bestMimeType=null;
		for(MimeType mimeType:mimeTypes){
			 bestMimeType=mimeType;
			 break;
		}
		String contentType="";
		if(null!=bestMimeType)
			 contentType=bestMimeType.toString();
			 
		if(!"".equals(contentType)){
			 contentType=contentType+";charset=UTF-8";
		}
		
		logger.info("stream view will use decide_content_type:"+contentType);
		return contentType;
	}
	
	public static void main(String args[]){
	}

}

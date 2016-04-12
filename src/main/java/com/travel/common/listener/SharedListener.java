package com.travel.common.listener;

import javax.servlet.ServletContext;

import com.travel.common.utils.FileUtil;
import com.travel.common.utils.JarUtil;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.IOException;


public class SharedListener extends org.springframework.web.context.ContextLoaderListener {
	private final String sharedOutDir="shared";//相对于webApp目录
	private final String inJarDir="META-INF"+File.separator+"shared";//共享文件在jar中的目录

	@Override
	public WebApplicationContext initWebApplicationContext(ServletContext servletContext) {
		
		StringBuilder sb = new StringBuilder();
		sb.append("\r\n======================================================================\r\n");
		sb.append("\r\n===========================shared jsp  file copy start================\r\n");
		sb.append("\r\n======================================================================\r\n");
		System.out.println(sb.toString());

		String jarPah= JarUtil.getJarPath(SharedListener.class);
		String jarName=new File(jarPah).getName();
		String jarNameNoDot=jarName.replaceAll("\\.","");
		String tempDir= FileUtil.getTempDirectoryPath()+ File.separator+jarNameNoDot;
		String unJarDir="unjar";
		try {
			//拷贝jar
			FileUtil.copyFileToDirectory(jarPah,tempDir);

			//解压jar
			JarUtil.unJarByJarFile(new File(tempDir+File.separator+jarName),new File(tempDir+File.separator+unJarDir));

			//拷贝至当前项目目标目录下
			String webRootDir=servletContext.getRealPath("/");
			String distinctDir=webRootDir+File.separator+sharedOutDir;
			for(File file:new File(tempDir+File.separator+unJarDir+File.separator+inJarDir).listFiles()){
				if(file.isFile())
					FileUtil.copyFileToDirectory(file,new File(distinctDir));
				else
					FileUtil.copyDirectoryToDirectory(file,new File(distinctDir));
			}

		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			FileUtil.deleteFile(tempDir);
		}


		sb.setLength(0);
		sb.append("\r\n======================================================================\r\n");
		sb.append("\r\n===========================shared jsp  file copy end==================\r\n");
		sb.append("\r\n======================================================================\r\n");
		System.out.println(sb.toString());
		
		return super.initWebApplicationContext(servletContext);
	}
}

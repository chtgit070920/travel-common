package com.travel.common.utils;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.slf4j.LoggerFactory;

import java.applet.Applet;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;

/**
 * @description: 文件工具类
 */
public class FileUtil {

	// 创建目录：new File("/tmp/one/two/three").mkdirs();执行后，会建立tmp/one/two/three四级目录
	// 创建目录：new File("/tmp/one/two/three").mkdir();执行后则不会建立任何目录，因为找不到/tmp/one/two目录
	// 创建文件：newFile("/tmp/one/two/three/aa").createNewFile();执行后，若/tmp/one/two/three/目录不存在，则文件会创建失败，不过我们一般不会显式的创建文件。
	// 判定文件或目录是否存在：FIle.exists();它可以判定文件，也可以判定目录。


    protected static final org.slf4j.Logger logger = LoggerFactory.getLogger(FileUtil.class);
	private static final int BUFFER = 1024*8;//8k
	public static final String LEFT_FLASH="/";



	public static String toLeftSlash(String path){
		if(null==path) return path;
		return path.replaceAll("\\\\", LEFT_FLASH);
	}
	public static String getSmpleFileName(String fileName){
		File file=new File(fileName);
		return file.getName();
	}



	/*************************************************************************************/
	/***********************************系统属性*****************************************/
	/*************************************************************************************/
	public static String getTempDirectoryPath() {
		return System.getProperty("java.io.tmpdir");
	}
	public static File getTempDirectory() {
		return new File(getTempDirectoryPath());
	}
	public static String getUserDirectoryPath() {
		return System.getProperty("user.home");
	}
	public static File getUserDirectory() {
		return new File(getUserDirectoryPath());
	}


	/*************************************************************************************/
	/***********************************读取和写入*****************************************/
	/*************************************************************************************/
	//把string写入到文件中
	public static void  writeStringToFile(String file,String data) throws IOException{
		File f=new File(file);
		writeStringToFile(f,data);
	}
	//把string写入到文件中
	public static void  writeStringToFile(File file,String data) throws IOException{
		FileUtils.writeStringToFile(file,data);
	}
	//从文件中提取string
	public static String readFileToString(String file) throws IOException{
		File f=new File(file);
		return readFileToString(f);
	}
	//从文件中提取string
	public static String readFileToString(File file) throws IOException{
		return FileUtils.readFileToString(file);
	}

	//从文件中提取字节
	public static byte[] getBytesFromFile(String file) throws IOException {
		File f=new File(file);
		return getBytesFromFile(f);
	}
	//从文件中提取字节
	public static byte[] getBytesFromFile(File file) throws IOException {
		return FileUtils.readFileToByteArray(file);
	}

	/*************************************************************************************/
	/***********************************复制功能*******************************************/
	/*************************************************************************************/
	//复制文件到文件
	public static void copyFileToFile(String source,String desc)throws IOException{
		File s=new File(source);
		File d=new File(desc);
		copyFileToFile(s,d);
	}
	//复制文件到文件
	public static void copyFileToFile(File source,File desc)throws IOException{
		FileUtils.copyFile(source,desc);
	}
	//复制文件到目录
	public static void copyFileToDirectory(String file,String directory) throws IOException {
		File f=new File(file);
		File d=new File(directory);
		copyFileToDirectory(f,d);
	}
	//复制文件到目录
	public static void copyFileToDirectory(File file,File directory) throws IOException {
		FileUtils.copyFileToDirectory(file,directory);
	}
	//复制目录到目录
	public static void copyDirectoryToDirectory(String source,String desc) throws IOException {
		File f=new File(source);
		File d=new File(desc);
		copyDirectoryToDirectory(f,d);
	}
	//复制目录到目录
	public static void copyDirectoryToDirectory(File source,File desc) throws IOException {
		FileUtils.copyDirectoryToDirectory(source,desc);
	}


	/*************************************************************************************/
	/***********************************移动功能*******************************************/
	/*************************************************************************************/
	public static void moveFileToDirectory(String file,String directory) throws IOException {
		File f=new File(file);
		File d=new File(directory);
		moveFileToDirectory(f,d);
	}
	public static void moveFileToDirectory(File file,File directory) throws IOException {
		FileUtils.moveFileToDirectory(file,directory,true);
	}
	public static void moveDirectoryToDirectory(String source,String desc) throws IOException {
		File s=new File(source);
		File d=new File(desc);
		moveDirectoryToDirectory(s,d);
	}
	public static void moveDirectoryToDirectory(File source,File desc) throws IOException {
		FileUtils.moveDirectoryToDirectory(source,desc,true);
	}

	/*************************************************************************************/
	/***********************************删除功能*******************************************/
	/*************************************************************************************/
	public static void deleteFile(String file){
		File f=new File(file);
		deleteFile(f);
	}
	public static void deleteFile(File file){
		FileUtils.deleteQuietly(file);
	}
	public static void deleteFile(String ...files){
		for(String file:files){
			deleteFile(file);
		}
	}
	public static void deleteFile(File ...files){
		for(File file:files){
			deleteFile(file);
		}
	}


	/*************************************************************************************/
	/***********************************包含功能*******************************************/
	/*************************************************************************************/
	public static boolean directoryContains(final String directory, final String child) throws IOException {
		File d=new File(directory);
		File c=new File(child);
		return FileUtils.directoryContains(d,c);
	}
	public static boolean directoryContains(final File directory, final File child) throws IOException {
		return FileUtils.directoryContains(directory,child);
	}


	/*************************************************************************************/
	/***********************************前后缀********************************************/
	/*************************************************************************************/
	public static String getFilePrefix(String fileName) {
		if(StringUtils.isBlank(fileName)) return null;
		int index = fileName.lastIndexOf(".");
		if(-1==index)return fileName;
		return fileName.substring(0, index);
	}
	public static String getFileSuffix(String fileName) {
		if(StringUtils.isBlank(fileName)) return null;
		int index = fileName.lastIndexOf(".");
		if(-1==index)return "";
		return fileName.substring(index + 1);
	}




    /*************************************************************************************/
    /***********************************zip功能*******************************************/
    /*************************************************************************************/
	public static void zip(String sourcePath, String zipPath) {
        /**
         * @Description 文件或文件夹压缩
         * @param sourcePath 原路径:如果到目录级，则压缩该目录，如果到文件级，则压缩该文件
         * @param zipPath  目标路径 ：存储路径+压缩后文件名
         */
		File file = new File(sourcePath);
		File zipFile = new File(zipPath);
		if (!file.exists())
			throw new RuntimeException(sourcePath + " not exists！");
		if (!zipFile.exists()) {
			File zipdir = new File(zipPath.substring(0,zipPath.lastIndexOf(LEFT_FLASH)));
			zipdir.mkdirs();
		}
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(zipFile);
			CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream,new CRC32());
			ZipOutputStream out = new ZipOutputStream(cos);
			compress(file, out, "");
			out.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
	private static void compress(File file, ZipOutputStream out, String basedir) {/** 压缩 */
		if (file.isDirectory()) {
			compressDirectory(file, out, basedir);
		} else {
			compressFile(file, out, basedir);
		}
	}
	private static void compressDirectory(File dir, ZipOutputStream out,String basedir) {/** 压缩一个目录 */
		if (!dir.exists())
			return;
		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++) {
			compress(files[i], out, basedir + dir.getName() + LEFT_FLASH);
		}
	}
	private static void compressFile(File file, ZipOutputStream out,String basedir) {/** 压缩一个文件 */
		if (!file.exists()) {
			return;
		}
		try {
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			ZipEntry entry = new ZipEntry(basedir + file.getName());
			out.putNextEntry(entry);
			int count;
			byte data[] = new byte[BUFFER];
			while ((count = bis.read(data, 0, BUFFER)) != -1) {
				out.write(data, 0, count);
			}
			bis.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	public static void  unzip(String zipName, String toPath) {
        /**
         *  为了解决sun jar包解压缩时的中文乱码问题（这里的中文乱码是指被压缩的文件名出现中文乱码），
         *  我们使用apache公司的ant.jar包来解压缩！
         *
         *  1. 说明假设a.zip下有两个文件夹：aaa和bbb,而且aaa文件下有
         * 	   shang.txt和xue.txt,而bbb文件夹下有tang.txt，那么zip条目有
         *     多少个？
         *     回答： 5个！文件夹两个，文件三个！
         *  2. 假设文件夹a下有b.zip,那么b.zip解压后所得文件位置在哪里？在a文件夹下！
         *     而不是在a文件夹下的b文件下！（这个尤为重要）
         *
         *  3. 解决了中文乱码问题
         *
         *  4. 只能解压zip，而rar  java无法解压！
         */
        try {
				// 创建zip文件
				org.apache.tools.zip.ZipFile zf= new org.apache.tools.zip.ZipFile(zipName);
				// zipEntry指解压后的条目
				ZipEntry ze = null;
				Enumeration e = zf.getEntries();
				while (e.hasMoreElements()) {
					ze = (ZipEntry) e.nextElement();
					// 条目-->文件
					File zfile = new File(toPath + LEFT_FLASH+ ze.getName());

					//文件是路径文件
					if (ze.isDirectory()) {// 该条目是否是路径
						if (!zfile.exists())// 是路径，那么zfile（此时它是路径文件）路径是否已经存在？
							zfile.mkdirs();// 不存在 -->创建
					}else{// 否则该条目是非路径文件
						File fpath = zfile.getParentFile();// 获得解压后该文件的父文件
						if (!fpath.exists())// 父文件（它一定是路径）路径是否存在
							fpath.mkdirs();// 不存在，创建
						//创建输入流
						InputStream in = zf.getInputStream(ze);
						// 创建输出流
						FileOutputStream out = new FileOutputStream(zfile);
						byte ch[] = new byte[256];
						int j;
						while ((j = in.read(ch)) != -1)out.write(ch, 0, j);
						out.close();
						in.close();
						//文件如果还是zip，则解压
						if (zfile.getAbsolutePath().endsWith(".zip")) {
							unzip( zfile.getAbsolutePath(),zfile.getParent());
							//这里需要进行删除该子zip文件的操作吗？不需要，务必注意，不懂？
							//那什么是迭代操作？恩 ，就是这个意思!!!
							//deleteFile(zfile);//,删除的是子zip文件
						}
					}
				}
				zf.close();
				deleteFile(new File(zipName));//删除传进来的zip文件
				//deleteFile(new File(zipName));
		} catch (Exception e) {
			ExceptionUtil.unchecked(e);
		}
	}
	



	
	//本方法是获得src下所有被编译后文件的的路径！即获得src下被编译后的某文件所在路径
	//如果是propertie文件而且和本类在同一包下，你可以直接获得properties的输入流，使用下面的方法
	//InputStream in=FileOperation.class.getResourceAsStream("hotLable.properties");
	//另外 必须注意：resouceName的名字也是有要求的：
	//分两种情况:
	//如果该文件在src根下，则resourceName名字可以是“struts.xml”，还可以是“log4j.xml”,还可以是“a.txt”或者“b.java”。
	//如果该文件不在src根下，而是在包下，则resouceName,需是“包名/文件名”
	//其实如果是包下的文件，并且同该方法所在的类处于同一包下，则resouceName是可以简写为文件名，
	//首先提出一个问题：src下的文件被编译之后位于那里？WebRoot下的文件被编译之后又是位于那里？
	//假定：项目名字是OA,src文件夹被放置在“F:\workspace1\OA”下
	//细心的同志们还会发现：
	//类FileOperation.java 路径是“F:\workspace1\OA\src\com\ utils\FileOperation.java”;
	//但它被编译之后的文件FileOperation.class所在路径是
	//"F:\workspace1\OA\WebRoot\WEB-INF\classes\com\ utils\FileOperation.class" 
	//同理位于src下的配置文件，例如xxx.properties或者xxx.hbm.xml或者applicationContext-xxx.xml等
	//被编译之后的路径同上
	//而位于F:\workspace1\OA\WebRoot下的js、images、jsp等文件夹以及这些文件夹下的所有子文件夹及文件..
	//的位置与编译之前完全相同。
	//此时我们再看一个问题，当执行代码时需要获得某配置文件的路径
	//那么该路径究竟是编译前还是编译后的？
	//回答：编译后的.
	public static String getCompiledDirOfsrc(String resourceName) {
		if (null == resourceName || "".equals(resourceName))
			throw new NullPointerException(resourceName+" not exist!");

		Class clazz = FileUtil.class;
		java.net.URL url;
		
		//.class和配置文件的url获取方法不同！
		if("java".equals(getFileSuffix(resourceName))){//如果以.java为后缀!则将其该为.class后缀！
			resourceName=getFilePrefix(resourceName)+".class";
		}
		url = clazz.getResource(resourceName);//从包下获取资源
		if(null==url)//从根下获取资源
			url=clazz.getClassLoader().getResource(resourceName);
		if(null==url){
			System.out.println("文件不存在;\n或者你resourceName不规范:\n" +
					"  如果是src根下的资源请直接给出这个资源的名字，例如struts.xml;\n" +
					"  如果是包下的资源，请给出包名+文件名，例如com/split/hotLable.properties;");
			return null;
		}
		//System.out.println(url);//  file:/F:/workspace1/OA/WebRoot/WEB-INF/classes/com/utils/FileOperation.class
		String absolute = url.getPath();
		//System.out.println(absolute);//  /F:/workspace1/OA/WebRoot/WEB-INF/classes/com/utils/FileOperation.class
		return absolute;// 去掉开头的“/”
	}
	
	//获取web根目录所在的磁盘路径
    //FileUtil必须被置于WEB-INF/classes/..才有效。
    public static String getWebRootDir(){
        String curResouce=FileUtil.class.getName().replaceAll("\\.","/")+".class";
        String path=getCompiledDirOfsrc(curResouce);
        int index=path.indexOf("/WEB-INF/classes/");
        if(index==-1)index=path.indexOf("/target/classes");
        return path.substring(0,index);
    }

	
	//读取properties文件，并将其key-value放置于map中！
	//注意propertiesName的名字问题：
	//分两种情况:
	//如果该文件在src根下，则resourceName名字可以是“struts.xml”，还可以是“log4j.xml”,还可以是“a.txt”或者“b.java”。
	//如果该文件不在src根下，而是在包下，则resouceName,需是“包名/文件名”或者“/包名/文件名”
	//其实如果是包下的文件，并且同该方法所在的类处于同一包下，则resouceName是可以简写为文件名，
	public static Map readProperties(String propertiesName){
		Map map=new HashMap();
		Properties properties =new Properties();
		String path=getCompiledDirOfsrc(propertiesName);
        logger.debug(propertiesName+" realpath is "+path);
		System.out.println(path);
		try {
			InputStream in=new FileInputStream(path);//读取.properties文件
			properties.load(in);//从流中读取键值对，放到properties中！
		} catch (IOException e) {
            throw ExceptionUtil.unchecked(e);
		}
		//获得properties中所有的键，枚举类型
		Enumeration enu=properties.keys();
		while(enu.hasMoreElements()){
			String key=enu.nextElement().toString();//获得一个key
			String value=properties.getProperty(key);//获得 该key 对应的  value
			try {
                //将 key 和 value 以utf-8的方式进行编码转换
				key=new String(key.getBytes("ISO8859-1"),"UTF-8");
				value=new String(value.getBytes("ISO8859-1"),"UTF-8");
			} catch (UnsupportedEncodingException e) {
				throw ExceptionUtil.unchecked(e);
			}
			map.put(key,value);//放入map
		}
		return map;
	}
	
	// 获得文件夹下所有符合某要求的所有文件,例如，文件的名字后缀是“.avi”等
	public static File[] getAnyFile(String dir, String req) {
		// dir = "F:/workspace1/dom4j/WebRoot/WEB-INF/classes";
		// req = "com";
		File file = new File(dir);// 文件夹对象，从该文件夹下找出符合要求的文件。
		File array[];// 用于 存放符合要求的文件.
		File temp[] = file.listFiles();// 获取dir下的所有文件（路径文件和非路径文件）
		File dic[] =null;// 用于存放 dir下符合要求的路径文件下的非路径文件！
		File nonDic[];// 用于存放 dir下符合要求的非路径文件！
		
		ArrayList list=new ArrayList();
		for (int i = 0; i < temp.length; i++) {// 如果是目录则。。
			File d[]=null;
			if (temp[i].isDirectory()) {// 如果是目录则。。
				d=getAnyFile(temp[i].getAbsolutePath(), req);
				File f=null;
				for(int j=0;j<d.length;j++){
					f=d[j];
					list.add(f);
					
				}
			}
		}
		//System.out.println(list);
		dic=new File[list.size()];
		for(int i=0;i<list.size();i++){
			dic[i]=(File)list.get(i);
		}
		
		// 不是目录，那么就是有后缀的文件，那么过滤这些文件
		nonDic = file.listFiles(new DirFilter(req));
		array = new File[dic.length + nonDic.length];
		System.arraycopy(dic, 0, array, 0, dic.length);
		System.arraycopy(nonDic, 0, array, dic.length, nonDic.length);
		return array;
	}
	// 文件过滤类
	static class DirFilter implements FilenameFilter {
		String str;
		DirFilter(String str) {
			this.str = str;
		}
		public boolean accept(File dir, String name) {
			if (null == str || "".equals(str))// 如果没有任何规格要求那么返回true
				return true;
			String f = new File(name).getName();// 否则...
			return f.indexOf(str) != -1;
		}
	}


    /*************************************************************************************/
    /***********************************图片压缩*******************************************/
    /*************************************************************************************/
	private final static Integer IMAGE_COMPRESSION_DEFAULT_WIDTH=150;//图片压缩后默认宽度
	private final static Integer IMAGE_COMPRESSION_DEFAULT_HEIGHT=150;//图片压缩后默认高度
	public static boolean compressionImage(String sourcePath,String targetPath,boolean proportion,Double rate,Integer outputWidth,Integer outputHeight) {
        /**
         * @Description 图片压缩
         * 如果按等比例压缩，需要设置压缩比例
         * 如果固定宽高压缩，需要设置outputWidth和outputHeight
         * 如果不设置，则会按默认的150*150
         * @param sourcePath 原路径
         * @param targetPath	目标路径
         * @param proportion	是否等比例压缩
         * @param rate	压缩比例
         * @param outputWidth 压缩后宽度
         * @param outputHeight 压缩后高度
         * @return
         *
         */
        File file = new File(targetPath);  
        FileOutputStream tempout = null;  
        try {  
            tempout = new FileOutputStream(file);  
        } catch (Exception ex) {  
            System.out.println(ex.toString());  
        }  
        Image img = null;  
        Toolkit tk = Toolkit.getDefaultToolkit();  
        Applet app = new Applet();  
        MediaTracker mt = new MediaTracker(app);  
        try {  
            img = tk.getImage(sourcePath);  
            mt.addImage(img, 0);  
            mt.waitForID(0);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        if (img.getWidth(null) == -1) {  
            logger.error(" can't read,retry!" + "<BR>");
            return false;
        } else {  
            int new_w;  
            int new_h;  
            if (proportion) { // 判断是否是等比缩放.  
                // 为等比缩放计算输出的图片宽度及高度  
            	double rate1 = (double) outputWidth /((double) img.getWidth(null)) ;
                double rate2 =  (double) outputHeight/((double) img.getHeight(null));
                
                rate=rate1==0?rate2:(rate1 >= rate2 ? (rate2==0?rate1 :rate2): rate1);
                
                new_w = (int) (((double) img.getWidth(null)) * rate);  
                new_h = (int) (((double) img.getHeight(null)) * rate);  
            } else {  
            	outputWidth = outputWidth==null?IMAGE_COMPRESSION_DEFAULT_WIDTH:outputWidth;
        		outputHeight = outputHeight==null?IMAGE_COMPRESSION_DEFAULT_HEIGHT:outputHeight;
                new_w = outputWidth; // 输出的图片宽度  
                new_h = outputHeight; // 输出的图片高度  
            }  
            BufferedImage buffImg = new BufferedImage(new_w, new_h,  
                    BufferedImage.TYPE_INT_RGB);  
            Graphics g = buffImg.createGraphics();  
            g.setColor(Color.white);  
            g.fillRect(0, 0, new_w, new_h);  
            g.drawImage(img, 0, 0, new_w, new_h, null);  
            g.dispose();  
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(tempout);  
            try {  
                encoder.encode(buffImg);  
                tempout.close();  
            } catch (IOException ex) {  
                System.out.println(ex.toString());  
            }  
        }  
        return true;  
    }

	
	public static void main(String args[]) throws IOException{

		//class clazz=org.apache.commons.io.FileUtils.class;

        System.getProperty("user.dir");
        System.out.println(getWebRootDir());


        String curResouce=FileUtil.class.getName().replaceAll("\\.","/")+".class";
        System.out.println(curResouce);
        String path=getCompiledDirOfsrc(curResouce);
        System.out.println(path);
//		String path = new File(FileUtil.class.getResource("/META-INF/notice.txt").getFile()).getAbsolutePath();
//		System.out.println(path);


//		File src=new File("/home/smart/Documents/workspace-eclipse-4.4/jyl-backstage/logs");
//		File desc=new File("/home/smart/Documents/workspace-eclipse-4.4/jyl-backstage/logs1");
//
//		FileUtils.copyDirectory(src,desc);

		//String path=getPathOfsrcOfCompile("FileUtil.class");
		//System.out.println(path);
		//
		//
		//F:\\svnSpace\\lyl\\doc\\99_temp\\accessory\\5_105.html
		//System.out.println(getFileContent(new File("F:\\svnSpace\\lyl\\doc\\99_temp\\accessory\\5_105.html")));
		
		//System.out.println(createHtmlFile(null,null));
		
		//Collection<MimeType> mimeTypes = MimeUtil.getMimeTypes(new File("C:/Users/Edward\\Desktop\\01_spring3.0-bean注解.htm"));  
		//System.out.println(mimeTypes);
		
		//System.out.println(createUploadFileName("aaa.html",""));
		
		//File file=new File("C:/Users/Edward\\Desktop\\a\\");
		
		//File parent=file.getParentFile();
		//parent.mkdirs();
		//file.createNewFile();
		
		//System.out.println(file.getParentFile());
		
		//
		//System.out.println(file.getName());
//		System.out.println(getSmpleFileName("C:/Users/Edward/Desktop/a/a"));
//		
//		File file=new File("C:/Users/Edward/Desktop/a");
//		
//		//if(!file.exists()) file.createNewFile();;
//		
//		System.out.println(toLeftSlash("f:\\lyl\\upload\\a.jpg"));
//		System.out.println(toLeftSlash("f:/lyl/upload/a.jpg"));
		//System.out.println(FileUtil.compressionImage("d:/377090271896782.jpg","d:/2000.jpg",true,1.2,1350,0));
	}
	
	
	
	
	
}

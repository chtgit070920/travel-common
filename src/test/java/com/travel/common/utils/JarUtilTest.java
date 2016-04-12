package com.travel.common.utils;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.util.Log4jConfigurer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by smart on 16-1-21.
 */
@ContextConfiguration(locations = { "classpath:com/config/springConfig.xml" })
public class JarUtilTest {


    /*测试结论:
     * 1.JarInputStream的getNextJarEntry()和jarOutputStream的putNextJarEntry()中没有包括"META-INF/MANIFEST.MF"这一项,因此复制和解压都要注意
     * 2.JarFile的entries()方法包含了全部Entry,也包括"META-INF/MANIFEST.MF",没有"META-INF/"这一项,因此在解压的时候要先检测父文件存不存在
     * 4.复制jar文件有3中方法, A是直接用BufferedInputStream和BufferedOutputStream复制,
     *                      B是用JarInputStream的getNextJarEntry()和jarOutputStream的putNextJarEntry()
     *                      C是用JarFile的entries()方法,遍寻JarEntry的InputStream,以此写出
     * 5.解压jar的话推荐使用JarFile,当前实例方法只支持解压jar文件
     * 6.在复制的时候,src文件只可以是jar文件,但des文件可以是带zip或rar后缀的文件
     */

    private static File targetJarFile;
    private static File testDir;
    private static File unJarDir;
    private static File copyJarDir;
    @BeforeClass
    public static void init(){


        try {
            Log4jConfigurer.initLogging("classpath:log4j.properties");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String user_temp=System.getProperty("java.io.tmpdir");
        String test_temp=user_temp+ File.separator+"jarTest";
        testDir=new File(test_temp);if(!testDir.exists())testDir.mkdir();
        unJarDir=new File(testDir,"unJar"); if(!unJarDir.exists())unJarDir.mkdir();
        copyJarDir=new File(testDir,"copyJar"); if(!copyJarDir.exists())copyJarDir.mkdir();

        String java_home=System.getProperty("java.home");
        File[] files=FileUtil.getAnyFile(java_home,"jar");
        File any=files[0];
        targetJarFile=new File(testDir,any.getName());

        try {
            FileUtil.copyFileToDirectory(any,testDir);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @AfterClass
    public static void destroy(){
        try {
            FileUtils.cleanDirectory(testDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @After
    public  void cleanDir(){
        try {
            FileUtils.cleanDirectory(unJarDir);
            FileUtils.cleanDirectory(copyJarDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


//    @Test
//    public void testJavaHome(){
//        System.out.println(System.getProperty("java.home"));
//    }

//    @Test
//    public void testTempDic(){
//        System.out.println(FileUtil.getTempDirectoryPath());
//    }

    @Test
    public void testGetJarDir(){
        String path = FileUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        System.out.println(path);
    }

//    @Test  //--可能有些情况有问题
//    public void testCopyJar(){
////        File src = new File("/tmp/jar/travel-common-0.0.1.jar");
////        File des = new File("/tmp/jar/travel-common-0.0.1.copy.jar");
//        //实验表明只运行复制和解压jar文件
////      File src = new File("C:/rtf.zip");
////      File des = new File("C:/testCopy.zip");
//        try {
//            JarUtil.copyJar(targetJarFile,new File(copyJarDir,targetJarFile.getName()));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

//    @Test //--可能有些情况有问题
//    public void testCopyJarByJarFile(){
////        File src = new File("/tmp/jar/travel-common-0.0.1.jar");
////        File des = new File("/tmp/jar/travel-common-0.0.1.copy2.jar");
//        try {
//            JarUtil.copyJarByJarFile(targetJarFile,new File(copyJarDir,targetJarFile.getName()));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }



    @Test  //建议使用此方法
    public void testCopyJarByCommonFile(){
//        File src = new File("/tmp/jar/travel-common-0.0.1.jar");
//        File des = new File("/tmp/jar/travel-common-0.0.1.copy3.jar");
        try {
            FileUtil.copyFileToFile(targetJarFile,new File(copyJarDir,targetJarFile.getName()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testUnJar(){
//        File src = new File("/tmp/jar/travel-common-0.0.1.jar");
//        File src = new File("C:/b.rar");    //不支持rar解压
//        String desFile = "unjar";
//        File desDir = new File(src.getParent()+File.separator+desFile);
        try {
            JarUtil. unJar(targetJarFile,new File(unJarDir,targetJarFile.getName()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUnJarByJarFile(){
//        File src = new File("/tmp/jar/travel-common-0.0.1.jar");
//        String desFile = "unjar2";
//        File desDir = new File(src.getParent()+File.separator+desFile);
        try {
            JarUtil.unJarByJarFile(targetJarFile,new File(unJarDir,targetJarFile.getName()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}

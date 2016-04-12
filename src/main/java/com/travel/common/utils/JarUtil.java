package com.travel.common.utils;

import org.apache.commons.io.FileUtils;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Enumeration;
import java.util.jar.*;
import java.util.zip.ZipEntry;

/**
 * Created by smart on 16-1-20.
 */
public class JarUtil {


    protected static final org.slf4j.Logger log = LoggerFactory.getLogger(JarUtil.class);

    //获得class所在jar文件所在磁盘位置
    public static String getJarPath(Class clazz){
        String path = clazz.getProtectionDomain().getCodeSource().getLocation().getPath();
        return path;
    }

    //解压jar文件by JarFile
    public static void unJarByJarFile(File src , File desDir) throws  IOException{
        log.debug("{} will be unjar to dir {}",src.getAbsolutePath(),desDir.getAbsolutePath());
        JarFile jarFile = new JarFile(src);
        Enumeration<JarEntry> jarEntrys = jarFile.entries();
        if(!desDir.exists())desDir.mkdirs(); //建立用户指定存放的目录
        byte[] bytes = new byte[1024];

        while(jarEntrys.hasMoreElements()){
            ZipEntry entryTemp = jarEntrys.nextElement();
            File desTemp = new File(desDir.getAbsoluteFile() + File.separator + entryTemp.getName());

            if(entryTemp.isDirectory()){    //jar条目是空目录
                if(!desTemp.exists())desTemp.mkdirs();
                log.debug("unjar-dir : " + entryTemp.getName());
            }else{    //jar条目是文件
                //因为manifest的Entry是"META-INF/MANIFEST.MF",写出会报"FileNotFoundException"
                File desTempParent = desTemp.getParentFile();
                if(!desTempParent.exists())desTempParent.mkdirs();

                BufferedInputStream in = new BufferedInputStream(jarFile.getInputStream(entryTemp));
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(desTemp));

                int len = in.read(bytes, 0, bytes.length);
                while(len != -1){
                    out.write(bytes, 0, len);
                    len = in.read(bytes, 0, bytes.length);
                }

                in.close();
                out.flush();
                out.close();

                log.debug("unjar-file: " + entryTemp.getName());
            }
        }

        jarFile.close();
    }

    //解压jar---不推荐
    public static void unJar(File src , File desDir) throws IOException{
        log.debug("{} will be unjar to dir {}",src.getAbsolutePath(),desDir.getAbsolutePath());

        JarInputStream jarIn = new JarInputStream(new BufferedInputStream(new FileInputStream(src)));
        if(!desDir.exists())desDir.mkdirs();
        byte[] bytes = new byte[1024];

        while(true){
            ZipEntry entry = jarIn.getNextJarEntry();
            if(entry == null)break;

            File desTemp = new File(desDir.getAbsoluteFile() + File.separator + entry.getName());

            if(entry.isDirectory()){    //jar条目是空目录
                if(!desTemp.exists())desTemp.mkdirs();
                log.debug("unjar-dir : " + entry.getName());
            }else{    //jar条目是文件
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(desTemp));
                int len = jarIn.read(bytes, 0, bytes.length);
                while(len != -1){
                    out.write(bytes, 0, len);
                    len = jarIn.read(bytes, 0, bytes.length);
                }

                out.flush();
                out.close();
                log.debug("unjar-file: " + entry.getName());
            }
            jarIn.closeEntry();
        }

        //解压Manifest文件
        Manifest manifest = jarIn.getManifest();
        if(manifest != null){
            File manifestFile = new File(desDir.getAbsoluteFile()+File.separator+JarFile.MANIFEST_NAME);
            if(!manifestFile.getParentFile().exists())manifestFile.getParentFile().mkdirs();
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(manifestFile));
            manifest.write(out);
            out.close();
        }

        //关闭JarInputStream
        jarIn.close();
    }


    //复制jar---不建议使用，建议使用FileUtil.copyFileToFile
    private static void copyJar(File src , File des) throws IOException{
        JarInputStream jarIn = new JarInputStream(new BufferedInputStream(new FileInputStream(src)));
        Manifest manifest = jarIn.getManifest();
        JarOutputStream jarOut = null;
        if(manifest == null){
            jarOut = new JarOutputStream(new BufferedOutputStream(new FileOutputStream(des)));
        }else{
            jarOut = new JarOutputStream(new BufferedOutputStream(new FileOutputStream(des)),manifest);
        }

        byte[] bytes = new byte[1024];
        while(true){
            //重点
            ZipEntry entry = jarIn.getNextJarEntry();
            if(entry == null)break;
            jarOut.putNextEntry(entry);

            int len = jarIn.read(bytes, 0, bytes.length);
            while(len != -1){
                jarOut.write(bytes, 0, len);
                len = jarIn.read(bytes, 0, bytes.length);
            }
            log.info("Copyed: " + entry.getName());
        }
        jarIn.close();
        jarOut.finish();
        jarOut.close();
    }
    //复制jar by JarFile---不建议使用，建议使用FileUtil.copyFileToFile
    private static void copyJarByJarFile(File src , File des) throws IOException{
        //重点
        JarFile jarFile = new JarFile(src);
        Enumeration<JarEntry> jarEntrys = jarFile.entries();
        JarOutputStream jarOut = new JarOutputStream(new BufferedOutputStream(new FileOutputStream(des)));
        byte[] bytes = new byte[1024];

        while(jarEntrys.hasMoreElements()){
            JarEntry entryTemp = jarEntrys.nextElement();
            jarOut.putNextEntry(entryTemp);
            BufferedInputStream in = new BufferedInputStream(jarFile.getInputStream(entryTemp));
            int len = in.read(bytes, 0, bytes.length);
            while(len != -1){
                jarOut.write(bytes, 0, len);
                len = in.read(bytes, 0, bytes.length);
            }
            in.close();
            jarOut.closeEntry();
            log.info("Copyed: " + entryTemp.getName());
        }

        jarOut.finish();
        jarOut.close();
        jarFile.close();
    }



}

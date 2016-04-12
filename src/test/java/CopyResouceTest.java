import com.travel.common.utils.FileUtil;
import com.travel.common.utils.JarUtil;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * Created by smart on 16-1-21.
 */
public class CopyResouceTest {

    private final String distinctRelativeDir="public";//相对于webApp目录
    //private final String tempRelativeDir="";//相对于用户临时目录
    //private final String tempRelativeUnJarDir="unjar";//相对于用户临时目录+tempRelativeDir
    private final String resouceJarDir="META-INF"+File.separator+"maven";

    @Test
    public  void test1(){

        StringBuilder sb = new StringBuilder();
        sb.append("\r\n======================================================================\r\n");
        sb.append("\r\n===========================共用jsp拷贝开始=================================\r\n");
        sb.append("\r\n======================================================================\r\n");
        System.out.println(sb.toString());

        String jarPah= JarUtil.getJarPath(FileUtils.class);
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
            String webappPath="/tmp/webapp/";
            String distinctDir=webappPath+distinctRelativeDir;
            for(File file:new File(tempDir+File.separator+unJarDir+File.separator+resouceJarDir).listFiles()){
                if(file.isFile())
                    FileUtil.copyFileToDirectory(file,new File(distinctDir));
                else
                    FileUtil.copyDirectoryToDirectory(file,new File(distinctDir));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }





        sb.setLength(0);
        sb.append("\r\n======================================================================\r\n");
        sb.append("\r\n===========================共用jsp拷贝结束=================================\r\n");
        sb.append("\r\n======================================================================\r\n");
        System.out.println(sb.toString());


    }
}

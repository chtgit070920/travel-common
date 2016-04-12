package com.travel.common.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;



/**
 * Created by smart on 16-1-19.
 */
public class HttpClientUtil {

    public  static String req(String gateway,Map<String,String> params){

        CloseableHttpClient httpclient=null;
        try {
            String now=DateUtil.date_string(new Date(),"yyyy-MM-dd HH:mm:ss");

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            for(Map.Entry<String, String> entry:params.entrySet()){
                nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            nameValuePairs.add(new BasicNameValuePair("timestamp",now));

            httpclient = HttpClients.createDefault();
            HttpPost httppost = new HttpPost(gateway);
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"utf-8"));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            String ret = EntityUtils.toString(entity);
            //logger.info(ret);
            return ret;
        }catch(Exception e){
            throw ExceptionUtil.unchecked(e);
        }finally{
            try {httpclient.close();} catch (IOException e) {e.printStackTrace();}
        }
    }
}

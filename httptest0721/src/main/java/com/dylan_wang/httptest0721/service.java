package com.dylan_wang.httptest0721;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class service {

    public static String get_save(String name, String phone) {
        /**
         * 出现乱码原因有2个 提交参数没有对中文编码，解决方法：使用URLEncoder.encode(xx,xx)对要提交的汉字转码
         * tomatCAT服务器默认采用iso859-1编码，解决方法：把PHP页面保存为UTF-8格式
         */
        String path = "http://192.168.0.117/testxml/web.php";
        Map<String, String> params = new HashMap<String, String>();
        try {
            params.put("name", URLEncoder.encode(name, "UTF-8"));
            params.put("phone", phone);
            return sendgetrequest(path, params);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "提交失败";
    }

    private static String sendgetrequest(String path, Map<String, String> params)
            throws Exception {

        // path="http://192.168.0.117/testxml/web.php?name=xx&phone=xx";
        StringBuilder url = new StringBuilder(path);
        url.append("?");
        for (Map.Entry<String, String> entry : params.entrySet()) {
            url.append(entry.getKey()).append("=");
            url.append(entry.getValue()).append("&");
        }
        url.deleteCharAt(url.length() - 1);
        URL url1 = new URL(url.toString());
        HttpURLConnection conn = (HttpURLConnection) url1.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == 200) {
            InputStream instream = conn.getInputStream();
            ByteArrayOutputStream outstream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            while (instream.read(buffer) != -1) {
                outstream.write(buffer);
            }
            instream.close();
            return outstream.toString().trim();

        }
        return "连接失败";
    }

    public static String post_save(String name, String phone) {
        /**
         * 出现乱码原因有2个 提交参数没有对中文编码，解决方法：使用URLEncoder.encode(xx,xx)对要提交的汉字转码
         * tomatCAT服务器默认采用iso859-1编码，解决方法：把PHP页面保存为UTF-8格式
         */
        String path = "http://192.168.0.117/testxml/web.php";
        Map<String, String> params = new HashMap<String, String>();
        try {
            params.put("name", URLEncoder.encode(name, "UTF-8"));
            params.put("phone", phone);
            return sendpostrequest(path, params);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "提交失败";
    }

    private static String sendpostrequest(String path,
                                          Map<String, String> params) throws Exception {
        // name=xx&phone=xx
        StringBuilder data = new StringBuilder();
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                data.append(entry.getKey()).append("=");
                data.append(entry.getValue()).append("&");
            }
            data.deleteCharAt(data.length() - 1);
        }
        byte[] entiy = data.toString().getBytes(); // 生成实体数据
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(5000);
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);// 允许对外输出数据
        conn.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(entiy.length));
        OutputStream outstream = conn.getOutputStream();
        outstream.write(entiy);
        if (conn.getResponseCode() == 200) {
            InputStream instream = conn.getInputStream();
            ByteArrayOutputStream byteoutstream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            while (instream.read(buffer) != -1) {
                byteoutstream.write(buffer);
            }
            instream.close();
            return byteoutstream.toString().trim();
        }
        return "提交失败";
    }

    public static String httpclient_postsave(String name, String phone) {
        /**
         * 出现乱码原因有2个 提交参数没有对中文编码，解决方法：使用URLEncoder.encode(xx,xx)对要提交的汉字转码
         * tomatCAT服务器默认采用iso859-1编码，解决方法：把PHP页面保存为UTF-8格式
         */
        String path = "http://192.168.0.117/testxml/web.php";
        Map<String, String> params = new HashMap<String, String>();
        try {
            params.put("name", name);
            params.put("phone", phone);
            return sendhttpclient_postrequest(path, params);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "提交失败";
    }

    private static String sendhttpclient_postrequest(String path,Map<String, String> params) {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        try {
            UrlEncodedFormEntity entity=new  UrlEncodedFormEntity(pairs,"UTF-8");
            HttpPost httpost=new HttpPost(path);
            httpost.setEntity(entity);
            HttpClient client=new DefaultHttpClient();
            HttpResponse response= client.execute(httpost);
            if(response.getStatusLine().getStatusCode()==200){

                return EntityUtils.toString(response.getEntity(), "utf-8");
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }


    public static String httpclient_getsave(String name, String phone) {
        /**
         * 出现乱码原因有2个 提交参数没有对中文编码，解决方法：使用URLEncoder.encode(xx,xx)对要提交的汉字转码
         * tomatCAT服务器默认采用iso859-1编码，解决方法：把PHP页面保存为UTF-8格式
         */
        String path = "http://192.168.0.117/testxml/web.php";
        Map<String, String> params = new HashMap<String, String>();
        try {
            params.put("name", name);
            params.put("phone", phone);
            return sendhttpclient_getrequest(path, params);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "提交失败";
    }

    private static String sendhttpclient_getrequest(String path,Map<String, String> map_params) {
        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        for (Map.Entry<String, String> entry : map_params.entrySet()) {
            params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        String param= URLEncodedUtils.format(params, "UTF-8");
        HttpGet getmethod=new HttpGet(path+"?"+param);
        HttpClient httpclient=new DefaultHttpClient();
        try {
            HttpResponse response=httpclient.execute(getmethod);
            if(response.getStatusLine().getStatusCode()==200){
                return EntityUtils.toString(response.getEntity(), "utf-8");
            }
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

}

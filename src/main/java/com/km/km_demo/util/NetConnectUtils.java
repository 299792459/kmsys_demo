package com.km.km_demo.util;




import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetConnectUtils {

    String baseURL="http://127.0.0.1:8090";
    BufferedReader reader = null;
    String result = null;
    HttpURLConnection conn;
    OutputStream dataout;


    public myResult urlGet(String api) throws IOException {

        //打开连接
        try {
            conn = (HttpURLConnection) new URL(baseURL + api).openConnection();
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(10000);
            conn.setRequestMethod("GET");// 默认GET请求
            conn.connect();// 建立TCP连接
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                result = TextConvertUtils.inputstreamToString(conn.getInputStream());
            }
        }catch (Exception e){
                e.printStackTrace();
        } finally {
            //关闭网络连接
            conn.disconnect();
        }
        //将结果转成json格式


        //返回信息
        myResult mr=new myResult();
        mr.setStatecode(1);
        mr.setMessage("ok");
        mr.setContent(result);
        return mr;
    }



    public myResult urlPost(String api,String param) throws IOException {

        //打开连接
        try {
            conn = (HttpURLConnection) new URL(baseURL + api).openConnection();
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(10000);

            conn.setDoOutput(true);// 设置是否向connection输出，因为这个是post请求，参数要放在http正文内，因此需要设为true,默认情况下是false
            conn.setDoInput(true); // 设置是否从connection读入，默认情况下是true;
            conn.setRequestMethod("POST");// 设置请求方式为post,默认GET请求
            conn.setUseCaches(false);// post请求不能使用缓存设为false
            conn.setInstanceFollowRedirects(true);// 设置该HttpURLConnection实例是否自动执行重定向
            conn.setRequestProperty("connection", "Keep-Alive");// 连接复用
            conn.setRequestProperty("charset", "utf-8");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.connect();// 建立TCP连接,getOutputStream会隐含的进行connect,所以此处可以不要

            dataout = new DataOutputStream(conn.getOutputStream());// 创建输入输出流,用于往连接里面输出携带的参数

            String Nparam=param;
            dataout.write(Nparam.getBytes());
            dataout.flush();
            dataout.close();

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                //reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));// 发送http请求
                // 读取流
                result = TextConvertUtils.inputstreamToString(conn.getInputStream());
                System.out.println(result);
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            //关闭网络连接
            conn.disconnect();
        }
        //将结果转成json格式
        JSONObject jsonresult = JSONObject.parseObject(result);

        //返回信息
        myResult mr=new myResult();
        mr.setStatecode(1);
        mr.setMessage("ok");
        mr.setContent(jsonresult);
        return mr;
    }





}

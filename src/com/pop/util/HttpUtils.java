package com.pop.util;



import android.text.TextUtils;
import android.util.Log;


import com.alibaba.fastjson.JSONObject;

import org.xutils.http.cookie.DbCookieStore;

import java.io.*;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xugang on 15/7/13.
 */
public class HttpUtils {

    // cookie manager
    private static final CookieManager COOKIE_MANAGER =
            new CookieManager(DbCookieStore.INSTANCE, CookiePolicy.ACCEPT_ALL);
    /**
     * Post 请求超时时间和读取数据的超时时间均为2000ms。
     *
     * @param urlPath       post请求地址
     * @param jsonObject json请求
     * @return String json字符串，成功：code=1001，否者为其他值
     * @throws Exception 链接超市异常、参数url错误格式异常
     */
    public static String doPost(String urlPath, JSONObject jsonObject, String who, String ip) throws Exception {

        if (null == urlPath || null == jsonObject) { // 避免null引起的空指针异常
            return "";
        }
        String jsonParam = jsonObject.toJSONString();
        URL localURL = new URL(urlPath);
        URLConnection connection = localURL.openConnection();
        HttpURLConnection httpURLConnection = (HttpURLConnection) connection;

        httpURLConnection.setDoOutput(true);
        if (!StringUtil.isEmpty(who)) {
            httpURLConnection.setRequestProperty("who", who);
        }
        if (!StringUtil.isEmpty(ip)) {
            httpURLConnection.setRequestProperty("clientIP", ip);
        }
        Map<String, List<String>> singleMap =
                COOKIE_MANAGER.get(localURL.toURI(), new HashMap<String, List<String>>(0));
        List<String> cookies = singleMap.get("Cookie");
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
        httpURLConnection.setRequestProperty("Content-Type", "application/json");
        httpURLConnection.setRequestProperty("Content-Length", String.valueOf(jsonParam.length()));
        if (cookies != null) {
            httpURLConnection.setRequestProperty("Cookie", TextUtils.join(";", cookies));
        }
        httpURLConnection.setConnectTimeout(18000);
        httpURLConnection.setReadTimeout(18000);

        OutputStream outputStream = null;
        OutputStreamWriter outputStreamWriter = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuilder resultBuffer = new StringBuilder();
        String tempLine = null;

        try {
            outputStream = httpURLConnection.getOutputStream();
            outputStreamWriter = new OutputStreamWriter(outputStream);
            outputStreamWriter.write(jsonParam);
            outputStreamWriter.flush();

            if (httpURLConnection.getResponseCode() >= 300) {
                throw new Exception("HTTP Request is not success, Response code is " + httpURLConnection.getResponseCode());
            }

            inputStream = httpURLConnection.getInputStream(); // 真正的发送请求到服务端
            inputStreamReader = new InputStreamReader(inputStream);
            reader = new BufferedReader(inputStreamReader);

            while ((tempLine = reader.readLine()) != null) {
                resultBuffer.append(tempLine);
            }

        } finally {

            if (outputStreamWriter != null) {
                outputStreamWriter.close();
            }

            if (outputStream != null) {
                outputStream.close();
            }

            if (reader != null) {
                reader.close();
            }

            if (inputStreamReader != null) {
                inputStreamReader.close();
            }

            if (inputStream != null) {
                inputStream.close();
            }
        }
        return resultBuffer.toString();
    }

    public static String doPost(String url, JSONObject jsonObject) throws Exception {

        return doPost(url, jsonObject, "", "");
    }

    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url   发送请求的URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param, String who, String ip) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url;
            if (!"".equals(param)) {
                urlNameString = urlNameString + "?" + param;
            }
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            if (!StringUtil.isEmpty(who)) {
                connection.setRequestProperty("who", who);
            }
            if (!StringUtil.isEmpty(ip)) {
                connection.setRequestProperty("clientIP", ip);
            }
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();

            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            Log.e("httpError","发送GET请求出现异常！", e);
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                Log.e("httpError","fail to close inputStream.", e2);
            }
        }
        return result;
    }

    public static String sendGet(String url, Map<String, Object> params) throws Exception {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            sb.append(entry.getKey())
                    .append("=")
                    .append(entry.getValue())
                    .append("&");
        }

        // no matter for the last '&' character

        return sendGet(url, sb.toString(), "", "");
    }



}
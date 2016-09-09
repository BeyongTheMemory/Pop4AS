package com.pop.util;

/**
 * Created by xugang on 16/8/24.
 */
public class UrlUtil {
    public static final String ip = "http://192.168.1.112:81/";//枫华府第
   // public static final String ip = "http://192.168.1.103:81/";//滨兴小区
   // public static final String ip = "http://30.143.139.121:81/";//阿里

    public static String getQiNiuToken(String type) {
        return ip + "pop-control/qiniu/getToken?type=" + type;
    }

    public static String getUploadHead(String url) {
        return ip + "pop-control/user/uploadHead?url=" + url;
    }


    public static String getLogin() {
        return ip + "pop-control/user/login";
    }

    public static String getRegist(){
        return ip + "pop-control/user/regist";
    }

    public static String getTest() {
        return ip + "pop-control/user/test";
    }
}

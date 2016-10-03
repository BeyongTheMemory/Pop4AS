package com.pop.util;

/**
 * Created by xugang on 16/8/24.
 */
public class UrlUtil {
    //public static final String ip = "http://192.168.1.112:81/";//枫华府第
    // public static final String ip = "http://192.168.1.103:81/";//滨兴小区
    // public static final String ip = "http://30.143.139.121:81/";//阿里
    // public static final String ip = "http://192.168.31.151:81/";//倾城
   // public static final String ip = "http://172.17.4.241:81/";//黄龙guest
    public static final String ip ="http://192.168.1.130:81/";//家

    public static String getQiNiuToken(String type) {
        return ip + "pop-control/qiniu/getToken?type=" + type;
    }

    public static String getUploadHead(String url) {
        return ip + "pop-control/user/uploadHead?url=" + url;
    }

    public static String getUpdateUser() {
        return ip + "pop-control/user/updateUser";
    }


    public static String getLogin() {
        return ip + "pop-control/user/login";
    }

    public static String getRegist() {
        return ip + "pop-control/user/regist";
    }

    public static String getUpdatePwd() {
        return ip + "pop-control/user/updatePwd";
    }

    public static String getPop() {
        return ip + "pop-control/pop/getPop";
    }

    public static String getNewPop() {
        return ip + "pop-control/pop/newPop";
    }

    public static String getTest() {
        return ip + "pop-control/user/test";
    }
}

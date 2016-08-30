package com.pop.util;

/**
 * Created by xugang on 16/8/24.
 */
public class UrlUtil {
    public static final String ip = "http://192.168.1.112:81/";
    public static String getQiNiuToken(String type){
        return ip+"pop-control/qiniu/getToken?type="+type;
    }

        public static String getLogin(){
        return ip+"pop-control/user/login";
    }
}

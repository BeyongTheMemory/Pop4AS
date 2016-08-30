package com.pop.util;

import java.io.File;

/**
 * Created by xugang on 16/8/24.
 */
public class FileUtil {
    public static final String basicPath = "/sdcard/pop/";
    public static void initFile(){
        File temp = new File(basicPath);//自已项目 文件夹
        if (!temp.exists()) {
            temp.mkdir();
        }
    }

}

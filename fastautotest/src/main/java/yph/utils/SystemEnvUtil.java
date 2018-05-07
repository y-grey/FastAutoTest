package yph.utils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by _yph on 2018/5/7 0007.
 */
public class SystemEnvUtil {

    private static String[] adbFiles = {"adb.exe","AdbWinApi.dll","AdbWinUsbApi.dll"};

    public static String getCopyAdb(){
        String oriAdb = null;
        String destAdb = "adb/";
        for(int i = 0;i < adbFiles.length;i++){
            if(!new File(destAdb+adbFiles[i]).exists())
                break;
            else if(i == adbFiles.length-1)
                return destAdb + adbFiles[0];
        }

        String pathString = System.getenv("Path");
        String[] arr = pathString.split(";");
        for(String s : arr){
            if(s.contains("\\platform-tools")){
                oriAdb = s;
                copyAdb(oriAdb+"/" , destAdb);
            }
        }
        if(oriAdb == null){
            throw new IllegalStateException("未配置adb环境");
        }
        return destAdb + adbFiles[0];
    }

    private static void copyAdb(String oriAdb , String destAdb){
        try {
            for(String adbFile : adbFiles) {
                FileUtils.copyFile(new File(oriAdb + adbFile), new File(destAdb + adbFile));
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("copy adb失败，请手动copy " +
                    Arrays.asList(adbFiles).toString() + " 至项目根目录adb文件夹下");
        }
    }
}

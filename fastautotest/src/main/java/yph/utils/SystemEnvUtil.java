package yph.utils;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.Arrays;

/**
 * Created by _yph on 2018/5/7 0007.
 */
public class SystemEnvUtil {

    private static String[] adbFiles = {"adb.exe", "AdbWinApi.dll", "AdbWinUsbApi.dll"};

    public static String getResCopyAdb() {
        try {
            for (String adbFile : adbFiles) {
                fileCopy("/adb/"+adbFile, "/adb/"+adbFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("copy adb失败，请手动copy " +
                    Arrays.asList(adbFiles).toString() + " 至项目根目录adb文件夹下");
        }
        return "/adb/" + adbFiles[0];
    }

    public static String getCopyAdb() {
        String destAdb = "adb/";
        for (int i = 0; i < adbFiles.length; i++) {
            if (!new File(destAdb + adbFiles[i]).exists())
                break;
            else if (i == adbFiles.length - 1)
                return destAdb + adbFiles[0];
        }

        String oriAdb = getSystemAdb();
        if (oriAdb == null) {
            throw new IllegalStateException("not set adb environment");
        } else {
            copyAdb(oriAdb + "/", destAdb);
        }
        return destAdb + adbFiles[0];
    }

    private static String getSystemAdb() {
        String pathString = System.getenv("Path");
        String[] arr = pathString.split(";");
        for (String s : arr) {
            if (s.contains("\\platform-tools")) {
                return s;
            }
        }
        String androidHome = System.getenv("ANDROID_HOME");
        if (androidHome != null) {
            return androidHome + "/platform-tools";
        }
        return null;
    }

    private static void copyAdb(String oriAdb, String destAdb) {
        try {
            for (String adbFile : adbFiles) {
                FileUtils.copyFile(new File(oriAdb + adbFile), new File(destAdb + adbFile));
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("copy adb fail，请手动copy " +
                    Arrays.asList(adbFiles).toString() + " 至项目根目录adb文件夹下");
        }
    }

    public static void fileCopy(String srcFilePath, String destFilePath) throws Exception {
        File destFile = new File(destFilePath);
        FileUtils.forceMkdirParent(destFile);
        BufferedInputStream fis = new BufferedInputStream(ClassLoader.getSystemResourceAsStream(srcFilePath));
        FileOutputStream fos = new FileOutputStream(destFile);
        byte[] buf = new byte[1024];
        int c;
        while ((c = fis.read(buf)) != -1) {
            fos.write(buf, 0, c);
        }
        fis.close();
        fos.close();
    }
}

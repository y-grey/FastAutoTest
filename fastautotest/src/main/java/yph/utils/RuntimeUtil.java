package yph.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class RuntimeUtil {

    private static String filter[] = new String[]{"List of devices attached","offline","adb server version",
            "daemon not running", "adb server is out of date", "daemon started successfully","not found","Failed to"};

    public static List<String> getDevices(String adb) {
        List<String> devices = new ArrayList<>();
        List<String> results = RuntimeUtil.exec(adb + " devices");
        if (results.size() > 0) {
            for (int i = 0; i < results.size(); i++) {
                String deviceName = results.get(i);
                deviceName = deviceName.substring(0, deviceName.indexOf("\t"));
                devices.add(deviceName);
            }
        } else {
            new Throwable("Can't find devices").printStackTrace();
        }
        return devices;
    }

    public static String getPlatformVersion(String adb,String deviceName) {
        List<String> results = RuntimeUtil.exec(adb + " -s " + deviceName + " shell getprop ro.build.version.release");
        return results.get(0);
    }

    public static void installApk(String adb,String deviceName,String apkPath) {
        RuntimeUtil.exec(adb + " -s " + deviceName + " install "+apkPath);
    }

    public static int getVersionCode(String adb,String deviceName,String pkgName) {
        List<String> results = RuntimeUtil.exec(adb + " -s " + deviceName + " shell dumpsys package "+pkgName+" | findstr versionCode");
        String versionCode = "0";
        if (results.size() > 0) {
            versionCode = results.get(0);
            versionCode = versionCode.substring(versionCode.indexOf("versionCode="),versionCode.indexOf(" targetSdk"));
        }
        return Integer.valueOf(versionCode);
    }

    public static String getLaunchActivity(String adb,String deviceName) {
        List<String> results = RuntimeUtil.exec(adb + " -s " + deviceName + " shell getprop ro.build.version.release");
        return results.get(0);
    }

    public static boolean isProcessRunning(String keyMsg) {
        boolean running;
        List<String> results = RuntimeUtil.exec("cmd.exe /c netstat -ano|findstr " + keyMsg);
        if (results.isEmpty()) {
            running = false;
        } else {
            running = true;
        }
        return running;
    }

    public static void killProcess(String keyMsg) {
        List<String> results = RuntimeUtil.exec("cmd.exe /c netstat -ano|findstr " + keyMsg);
        if (results.isEmpty()) {
            new Throwable("Can't find process " + keyMsg).printStackTrace();
        } else {
            RuntimeUtil.exec("cmd.exe /c taskkill /pid " + results.get(0).substring(results.get(0).lastIndexOf(" ")));
        }
    }

    public static List<String> exec(String cmd) {
        List<String> list = new ArrayList<>();
        try {
            System.out.println("exec " + cmd);
            Process process = Runtime.getRuntime().exec(cmd);
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                if (filter(line))
                    list.add(line);
            }
            process.waitFor();
            inputStream.close();
            reader.close();
            process.destroy();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("出错：" + e.getMessage());
        }
        return list;
    }

    private static boolean filter(String line) {
        if (line.trim().equals("")) return false;
        boolean b = true;
        for (String filt : filter) {
            if (line.contains(filt)) {
                b = false;
                break;
            }
        }
        return b;
    }


}

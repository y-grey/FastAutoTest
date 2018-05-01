package yph.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by _yph on 2018/3/15 0015.
 */

public class CmdUtil {

    private CmdUtil() {
    }

    public static CmdUtil get() {
        return Singleton.instance;
    }

    private static class Singleton {
        static CmdUtil instance = new CmdUtil();
    }

    private String adb;
    private String pkgName;

    public void init(String adb, String pkgName) {
        this.adb = adb;
        this.pkgName = pkgName;
    }

    public List<String> getDevices() {
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

    public String getPlatformVersion(String deviceName) {
        List<String> results = RuntimeUtil.exec(adb + " -s " + deviceName + " shell getprop ro.build.version.release");
        return results.get(0);
    }

    public void installApk(String deviceName, String apkPath) {
        RuntimeUtil.exec(adb + " -s " + deviceName + " install " + apkPath);
    }

    public int getVersionCode(String deviceName) {
        List<String> results = RuntimeUtil.exec(adb + " -s " + deviceName + " shell dumpsys package " + pkgName + " | findstr versionCode");
        String versionCode = "0";
        if (results.size() > 0) {
            versionCode = results.get(0);
            versionCode = versionCode.substring(versionCode.indexOf("versionCode="), versionCode.indexOf(" targetSdk"));
        }
        return Integer.valueOf(versionCode);
    }

    public String getLaunchActivity(String deviceName) {
        List<String> results = RuntimeUtil.exec(adb + " -s " + deviceName + " shell getprop ro.build.version.release");
        return results.get(0);
    }

    public void getCpu(String deviceName) {
        RuntimeUtil.execAsync(adb + " -s " + deviceName + " shell top -n 1 -s  cpu|grep " + pkgName,
                new RuntimeUtil.AsyncInvoke() {
                    @Override
                    public void invoke(String cpu) {
                        if(!cpu.contains(pkgName+":")) {
                            System.out.println("cpu " + cpu);
                            cpu = cpu.substring(cpu.lastIndexOf("%") - 2, cpu.lastIndexOf("%") + 1);
                            System.out.println("cpu " + cpu);
                            getMem(deviceName);
                        }
                    }
                });
    }
    public void getMem(String deviceName) {
        List<String> results = RuntimeUtil.exec(adb + " -s " + deviceName + " shell dumpsys meminfo " + pkgName+"|grep TOTAL:");
        if (results.size() > 0) {
            String mem = results.get(0);
            mem = mem.substring(mem.indexOf(":")+1, mem.lastIndexOf("TOTAL")).trim();
            System.out.println("men " + mem);
        }
    }
    public boolean isProcessRunning(String keyMsg) {
        boolean running;
        List<String> results = RuntimeUtil.exec("cmd.exe /c netstat -ano|findstr " + keyMsg);
        if (results.isEmpty()) {
            running = false;
        } else {
            running = true;
        }
        return running;
    }

    public void killProcessIfExist(String keyMsg) {
        List<String> results = RuntimeUtil.exec("cmd.exe /c netstat -ano|findstr " + keyMsg);
        if (!results.isEmpty()) {
            RuntimeUtil.exec("cmd.exe /c taskkill /f /pid " + results.get(0).substring(results.get(0).lastIndexOf(" ")));
        }
    }
}

package yph.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import yph.bean.Device;

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

    private final String adb = "fastautotest/adb/adb";
    private String pkgName;

    public void init(String pkgName) {
        this.pkgName = pkgName;
    }

    public List<String> getDevices() {
        List<String> devices = new ArrayList<>();
        List<String> results = RuntimeUtil.exec(adb + " devices", "Get Devices");
        System.out.println(results);
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
        List<String> results = RuntimeUtil.exec(adb + " -s " + deviceName + " shell getprop ro.build.version.release", "Get " + deviceName + " Platform Version");
        System.out.println(results);
        return results.get(0);
    }

    public void installApk(String deviceName, String apkPath) {
        RuntimeUtil.exec(adb + " -s " + deviceName + " install " + apkPath, "Install App");
    }

    public int getVersionCode(String deviceName) {
        List<String> results = RuntimeUtil.exec(adb + " -s " + deviceName + " shell dumpsys package " + pkgName + " | findstr versionCode", "");
        String versionCode = "0";
        if (results.size() > 0) {
            versionCode = results.get(0);
            versionCode = versionCode.substring(versionCode.indexOf("versionCode="), versionCode.indexOf(" targetSdk"));
        }
        return Integer.valueOf(versionCode);
    }

    public Timer startPerforMonitor(String deviceName) {
        return RuntimeUtil.execAsync(adb + " -s " + deviceName + " shell top -n 1 -s  cpu|grep " + pkgName,
                new RuntimeUtil.AsyncInvoke() {
                    @Override
                    public void invoke(String cpu) {
                        if (!cpu.contains(pkgName + ":")) {
                            cpu = cpu.substring(cpu.lastIndexOf("%") - 2, cpu.lastIndexOf("%") + 1);
                            getMem(deviceName, new Device(deviceName).setCpu(cpu));
                        }
                    }
                });
    }

    public void getMem(String deviceName, Device device) {
        List<String> results = RuntimeUtil.exec(adb + " -s " + deviceName + " shell dumpsys meminfo " + pkgName + "|grep TOTAL ", "");
        if (results.size() > 0) {
            String mem = results.get(0).replace("TOTAL","");
            String menTem = "";
            char[] chars = mem.toCharArray();
            boolean b = false;
            for(char c : chars){
                if(c == ' '){
                    if(b){
                        break;
                    }
                }else {
                    menTem = menTem + c;
                    b = true;
                }
            }
            device.setMem(menTem);
        }
        System.out.println("["+deviceName + " cpu:" + device.getCpu() + "  men:" + device.getMem()+"]");
    }

    public boolean isProcessRunning(String keyMsg) {
        boolean running;
        List<String> results = RuntimeUtil.exec("cmd.exe /c netstat -ano|findstr " + keyMsg, "");
        if (results.isEmpty()) {
            running = false;
        } else {
            running = true;
        }
        return running;
    }

    public void killProcessIfExist(String keyMsg) {
        List<String> results = RuntimeUtil.exec("cmd.exe /c netstat -ano|findstr " + keyMsg, "");
        if (!results.isEmpty()) {
            RuntimeUtil.exec("cmd.exe /c taskkill /f /pid " + results.get(0).substring(results.get(0).lastIndexOf(" ")), "");
        }
    }
}

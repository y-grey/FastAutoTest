package yph.base;

import yph.bean.Configure;
import yph.bean.TestBean;
import yph.performance.Device;
import yph.utils.CmdUtil;
import yph.utils.RuntimeUtil;

import java.util.List;
import java.util.Timer;

import static yph.performance.Device.deviceList;

/**
 * Created by _yph on 2018/4/1 0001.
 */

public class PerforMonitor {
    private Timer monitorTimer;
    private boolean isInitialStatics = true;
    private long preTraffic;
    private long lastestTraffic;
    private long traffic;
    private String deviceUdid;
    private String pkgName;
    private int uid = -1;
    private int pid = -1;
    private Device device = new Device();

    public PerforMonitor(String deviceName,String deviceUdid, String pkgName) {
        device.setName(deviceName);
        this.deviceUdid = deviceUdid;
        this.pkgName = pkgName;
    }

    public void start(final Thread mainThread) {
        deviceList.add(device);
        monitorTimer = CmdUtil.get().getCpu(deviceUdid, new RuntimeUtil.AsyncInvoke() {
            @Override
            public void invoke(String cpu) {
                if (!cpu.contains(pkgName + ":")) {
                    getPid(cpu);
                    long traffic = getTraffic(deviceUdid, getUid(cpu));
                    cpu = cpu.substring(cpu.lastIndexOf("%") - 2, cpu.lastIndexOf("%")).trim();
                    int mem = CmdUtil.get().getMem(deviceUdid);
                    String stackString = getCurStack(mainThread);

                    device.setCpu(Integer.valueOf(cpu))
                            .setMem(mem)
                            .setTraffic(traffic)
                            .setCurStack(stackString);

                    System.out.println("[" + device.getName() + " cpu:" + cpu + "%  men:" + mem
                            + "MB  traffic:" + traffic + "KB  curStack:" + stackString + "]");
                }
            }
        });
    }

    public void stop() {
        if (monitorTimer != null)
            monitorTimer.cancel();
    }


    private String getCurStack(Thread mainThread) {
        String stackString = "";
        StackTraceElement[] stackTraceElements = mainThread.getStackTrace();
        for (StackTraceElement element : stackTraceElements) {
            for (TestBean testBean : Configure.get().getTestBeans()) {
                for (Class c : testBean.getClasses()) {
                    if (element.toString().contains(c.getName()))
                        stackString = stackString + element.toString();
                }
            }
        }
        return stackString;
    }

    public String getCrashLog() {
        String crashLog = "";
        List<String> results = CmdUtil.get().getCrashLog(deviceUdid, pid);
        for (String s : results){
            if(s.contains("at "))
                crashLog = crashLog + s;
        }
        return crashLog;
    }

    public String getAnrLog() {
        return CmdUtil.get().getAnrLog(deviceUdid, pkgName);
    }

    public int getPid(String cpu) {
        if (pid == -1) {
            try {
                pid = Integer.valueOf(CmdUtil.get().getWordBetweenBlank(cpu));
            } catch (Exception e) {
                pid = -1;
            }
        }
        return pid;
    }

    private int getUid(String cpu) {
        if (uid == -1) {
            try {
                String uidString = cpu.substring(cpu.indexOf("u0_a") + 4);
                uidString = uidString.substring(0, uidString.indexOf(" "));
                uid = Integer.valueOf(uidString) + 10000;
            } catch (Exception e) {
                uid = -1;
            }
        }
        return uid;
    }

    private long getTraffic(String deviceUdid, int uid) {
        if (isInitialStatics) {
            preTraffic = CmdUtil.get().getTraffic(deviceUdid, uid);
            isInitialStatics = false;
            return 0;
        } else {
            lastestTraffic = CmdUtil.get().getTraffic(deviceUdid, uid);
            if (preTraffic == -1)
                traffic = -1;
            else {
                if (lastestTraffic > preTraffic) {
                    traffic += (lastestTraffic - preTraffic + 1023) / 1024;
                }
            }
            preTraffic = lastestTraffic;
            return traffic;
        }
    }
}
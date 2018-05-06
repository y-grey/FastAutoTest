package yph.base;

import yph.bean.Configure;
import yph.bean.TestBean;
import yph.performance.Device;
import yph.utils.CmdUtil;
import yph.utils.RuntimeUtil;

import java.util.Timer;

import static yph.performance.Device.deviceList;

/**
 * Created by _yph on 2018/4/1 0001.
 */

public class PerforMonitor{
    private Timer monitorTimer;
    private boolean isInitialStatics = true;
    private long preTraffic;
    private long lastestTraffic;
    private long traffic;
    private int uid = 0;
    private Device device = new Device();


    public void start(final String deviceName, final String deviceUdid, final String pkgName, final Thread mainThread) {
        device.setName(deviceName);
        deviceList.add(device);
        monitorTimer = CmdUtil.get().getCpu(deviceUdid, new RuntimeUtil.AsyncInvoke() {
            @Override
            public void invoke(String cpu) {
                if (!cpu.contains(pkgName + ":")) {
                    long traffic = getTraffic(deviceUdid, getUid(cpu));
                    cpu = cpu.substring(cpu.lastIndexOf("%") - 2, cpu.lastIndexOf("%")).trim();
                    int mem = CmdUtil.get().getMem(deviceUdid);
                    String stackString = getCurStack(mainThread);

                    device.setCpu(Integer.valueOf(cpu))
                          .setMem(mem)
                          .setTraffic(traffic)
                          .setCurStack(stackString);

//                    System.out.println("[" + device.getName() + " cpu:" + device.getCpuList() + "%  men:" + device.getMemList()
//                            + "MB  traffic:" + device.getTrafficList() + "KB  curStack:" + device.getCurStackList() + "]");
                }
            }
        });
    }

    public void stop() {
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

    private int getUid(String cpu) {
        if (uid == 0) {
            try {
                String uidString = cpu.substring(cpu.indexOf("u0_a") + 4);
                uidString = uidString.substring(0, uidString.indexOf(" "));
                uid = Integer.valueOf(uidString) + 10000;
            } catch (Exception e) {
                uid = 0;
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
            return traffic ;
        }
    }
}
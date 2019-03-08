package yph.performance;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import yph.bean.Configure;
import yph.bean.TestBean;
import yph.utils.CmdUtil;
import yph.utils.CpuSnapshot;
import yph.utils.Log;

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
    public int pid = -1;
    private Device device = new Device();

    private CpuSnapshot mLastSnapShot;
    private int occurCount;


    public PerforMonitor(String deviceName, String deviceUdid, String pkgName) {
        device.setName(deviceName);
        this.deviceUdid = deviceUdid;
        this.pkgName = pkgName;
        initPUid();
    }

    public void start(final Thread mainThread) {
        deviceList.add(device);
        monitorTimer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
//                Log.d("cpu start");
                int cpu = getAppCpuRate();
//                Log.d("cpu end");
                if (cpu == -1) {
                    String line = CmdUtil.get().execPs(deviceUdid);
                    if (!isPsNotExists(line)) {
                        initPUid(line);
                    }
                    return;
                }
//                Log.d("traffic start");
//                long traffic = getTraffic(deviceUdid, uid);
//                Log.d("traffic end");
//                Log.d("mem start");
//                int mem = 0;
                int mem = CmdUtil.get().getMem(deviceUdid);
//                Log.d("mem end");
                String stackString = getCurStack(mainThread);

                device.setCpu(cpu)
                        .setMem(mem)
                        .setTraffic(traffic)
                        .setCurStack(stackString);

                Log.i("[" + device.getName() + " cpu:" + cpu + "%  men:" + mem
                        + "MB  traffic:" + traffic + "KB  curStack:" + stackString + "]");
            }
        };
        monitorTimer.schedule(timerTask, 0, 1000);
    }

    public void stop() {
        if (monitorTimer != null)
            monitorTimer.cancel();
    }

    private int getAppCpuRate() {
        CpuSnapshot endSnapshot = CmdUtil.get().getCpu(deviceUdid, pid);
        if (endSnapshot == null) {
            return -1;
        }
        if (mLastSnapShot == null) {
            mLastSnapShot = endSnapshot;
            return -1;
        }
        float totalTime = (endSnapshot.total - mLastSnapShot.total) * 1.0f;
        if (totalTime <= 0) {
            return -1;
        }

        int appRatio = (int) ((endSnapshot.app - mLastSnapShot.app) / totalTime * 100f);
        if (appRatio > 80) {
            if (occurCount < 4) {
                occurCount++;
            }
            if (occurCount == 2) {
                // TODO: 2019/3/2 0002 dump traceView
            }
        } else {
            occurCount = 0;
        }
        mLastSnapShot = endSnapshot;
        return appRatio;
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
        if (isPsNotExists(CmdUtil.get().execPs(deviceUdid))) {
            List<String> results = CmdUtil.get().getCrashLog(deviceUdid);
            for (String s : results) {
                crashLog = crashLog + s.replace("(AndroidRuntime)", "") + "\n";
            }
        }
        return crashLog;
    }

    public String getAnrLog() {
        return CmdUtil.get().getAnrLog(deviceUdid);
    }

    private boolean isPsNotExists(String line) {
        return line == null || line.equals("");
    }

    private void initPUid() {
        String line = CmdUtil.get().execPs(deviceUdid);
        initPUid(line);
    }

    private void initPUid(String line) {
        try {
            if (line == null || line.equals("")) {
                return;
            }
//            Log.d(line);
            String uidString = line.substring(line.indexOf("u0_a") + 4);
            uidString = uidString.substring(0, uidString.indexOf(" "));
            uid = Integer.valueOf(uidString) + 10000;

            String pidString = line.substring(line.indexOf(" "));
            pidString = CmdUtil.get().getWordBetweenBlank(pidString);
            pid = Integer.valueOf(pidString);

            if (uid != -1 && pid != -1) {
                Log.d("获取p/u id  " + uid + "  " + pid);
            }
            Thread.sleep(1000);
        } catch (Exception e) {
            Log.d("获取p/u id" + e.toString());
        }
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
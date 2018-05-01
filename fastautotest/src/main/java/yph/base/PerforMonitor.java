package yph.base;

import java.util.Timer;

import yph.utils.CmdUtil;

/**
 * Created by _yph on 2018/4/1 0001.
 */

public class PerforMonitor {
    private Timer monitorTimer;

    public void start(String deviceName) {
        monitorTimer = CmdUtil.get().startPerforMonitor(deviceName);
    }

    public void stop() {
        monitorTimer.cancel();
    }
}

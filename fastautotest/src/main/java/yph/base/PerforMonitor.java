package yph.base;

import java.util.Timer;

import yph.bean.Configure;
import yph.bean.Device;
import yph.bean.TestBean;
import yph.utils.CmdUtil;
import yph.utils.RuntimeUtil;

/**
 * Created by _yph on 2018/4/1 0001.
 */

public class PerforMonitor {
    private Timer monitorTimer;

    public void start(final String deviceName,final String deviceUdid, final String pkgName, final Thread mainThread) {
        monitorTimer = CmdUtil.get().getCpu(deviceUdid,new RuntimeUtil.AsyncInvoke() {
            @Override
            public void invoke(String cpu) {
                if (!cpu.contains(pkgName + ":")) {
                    cpu = cpu.substring(cpu.lastIndexOf("%") - 2, cpu.lastIndexOf("%") + 1);
                    String mem = CmdUtil.get().getMem(deviceUdid);
                    String stackString = getCurStack(mainThread);

                    Device device = new Device(deviceName)
                            .setCpu(cpu)
                            .setMem(mem)
                            .setCurStack(stackString);
                    System.out.println("["+ device.getName() + " cpu:" + device.getCpu() + "  men:" + device.getMem()
                            + "  curStack:" + device.getCurStack()+"]");
                }
            }
        });
    }

    public void stop() {
        monitorTimer.cancel();
    }

    private String getCurStack(Thread mainThread){
        String stackString = "";
        StackTraceElement[] stackTraceElements = mainThread.getStackTrace();
        for (StackTraceElement element : stackTraceElements){
            for(TestBean testBean : Configure.get().getTestBeans()) {
                for(Class c : testBean.getClasses()) {
                    if (element.toString().contains(c.getName()))
                        stackString = stackString + element.toString();
                }
            }
        }
        return stackString;
    }
}

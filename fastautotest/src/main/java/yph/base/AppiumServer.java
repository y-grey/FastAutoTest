package yph.base;


import java.io.IOException;
import java.util.HashMap;

import yph.utils.CmdUtil;
import yph.utils.RuntimeUtil;
import yph.utils.SleepUtil;

public class AppiumServer {
    private static HashMap<String, Process> processMap = new HashMap<>();

    static void start(final String nodePath, final String appiumPath, final String port, final String bootstrapPort, final String chromeDriverPort, final String udid) {
        CmdUtil.get().killProcessIfExist("0.0.0.0:" + port);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String cmd = nodePath + " \"" + appiumPath + "\" " + "--session-override " + " -p "
                        + port + " -bp " + bootstrapPort + " --chromedriver-port " + chromeDriverPort + " -U " + udid;
                try {
                    Process process = Runtime.getRuntime().exec(cmd);
                    processMap.put(port, process);
                    RuntimeUtil.exec(process,"AppiumServer("+port+") Starting...");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        SleepUtil.s(10000);
        while (!CmdUtil.get().isProcessRunning("0.0.0.0:" + port)) {
            SleepUtil.s(4000);
        }
        System.out.println("AppiumServer("+port+") Start Successful");
    }

    static void stop(String port) {
        processMap.get(port).destroy();
    }
}

package yph.base;


import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import yph.utils.CmdUtil;
import yph.utils.Log;
import yph.utils.RuntimeUtil;
import yph.utils.SleepUtil;

public class AppiumServer {
    private static HashMap<String, Process> processMap = new HashMap<>();

    static void start(final String nodePath, final String appiumPath, final String port, final String bootstrapPort, final String chromeDriverPort, final String udid) {
        if (appiumPath.equals("")){
            if (!CmdUtil.get().isProcessRunning("0.0.0.0:" + port)) {
               Log.e("Not Set AppiumMainJs Path , Please Set AppiumMainJs Path or Start Appium Manual.");
            }
            return;
        }
        if(!new File(appiumPath).exists()){
            Log.e("AppiumMainJs Path Error");
            return;
        }
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
        Log.i("AppiumServer("+port+") Start Successful");
    }

    static void stop(String port) {
        if(processMap.containsKey(port))
            processMap.get(port).destroy();
    }
}

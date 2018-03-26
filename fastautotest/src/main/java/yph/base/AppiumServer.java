package yph.base;


import yph.utils.RuntimeUtil;
import yph.utils.SleepUtil;

public class AppiumServer {

    static void start(final String nodePath,final  String appiumPath,final  String port,final  String bootstrapPort,final  String chromeDriverPort,final String udid){
        if(!RuntimeUtil.isProcessRunning("0.0.0.0:"+port)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String cmd = nodePath + " \"" + appiumPath + "\" " + "--session-override " + " -p "
                            + port + " -bp " + bootstrapPort + " --chromedriver-port " + chromeDriverPort + " -U " + udid;
                    RuntimeUtil.exec(cmd);
                }
            }).start();
            SleepUtil.s(10000);
            while(!RuntimeUtil.isProcessRunning("0.0.0.0:"+port)){
                SleepUtil.s(4000);
            }
        }
    }

}

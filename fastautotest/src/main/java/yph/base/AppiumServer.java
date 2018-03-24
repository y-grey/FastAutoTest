package yph.base;


import yph.utils.RuntimeUtil;

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
            try {
                Thread.sleep(40000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}

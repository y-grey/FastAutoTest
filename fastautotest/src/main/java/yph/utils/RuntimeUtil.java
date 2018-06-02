package yph.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import yph.constant.Constant;
import yph.filter.AdbFilter;
import yph.filter.Filter;

public class RuntimeUtil {

    public static List<String> exec(String cmd, String log) {
        return exec(cmd, log, AdbFilter.get());
    }

    public static List<String> exec(String cmd, String log, Filter filter) {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String> list = exec(process, log, filter);
        return list;
    }

    public static List<String> exec(Process process, String log) {
        return exec(process, log, AdbFilter.get());
    }

    public static List<String> exec(Process process, String log, Filter filter) {
        if (log != null && !log.equals(""))
            Log.i(log);
        List<String> list = new ArrayList<>();
        try {
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                if (filter.filt(line)) {
                    list.add(line);
                }
            }
            process.waitFor();
            inputStream.close();
            reader.close();
            process.destroy();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("出错：" + e.getMessage());
        }
        return list;
    }


    public static Timer execAsync(final String cmd, final AsyncInvoke syncInvoke, int interval,final Filter filter) {
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    Log.d("cpu start");
//                    L.i("exec " + cmd);
                    Process process = Runtime.getRuntime().exec(cmd);
                    InputStream inputStream = process.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    boolean isGetCpu = false;
                    while ((line = reader.readLine()) != null) {
                        if (filter.filt(line)) {
                            isGetCpu = true;
                            if (syncInvoke != null) syncInvoke.invoke(line);
                        }
                    }
                    if(!isGetCpu && syncInvoke != null){
                        syncInvoke.invoke(Constant.APP_NOT_STARTING);
                    }
                    process.waitFor();
                    inputStream.close();
                    reader.close();
                    process.destroy();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("出错：" + e.getMessage());
                }
            }
        };
        if (interval > 0)
            timer.schedule(timerTask, 0, interval);
        else
            timer.schedule(timerTask, 0);
        return timer;
    }

    public interface AsyncInvoke {
        void invoke(String line);
    }
}

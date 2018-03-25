package yph.utils;

/**
 * Created by _yph on 2018/3/25 0025.
 */

public class SleepUtil {
    public static void s(long ms){
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

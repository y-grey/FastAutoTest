package yph.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {

    private final static boolean isDebug = new File("fastautotest").exists();

    public static void i(Object o) {
        System.out.println(getCurTime() + o);
    }

    public static void d(Object o) {
        if(isDebug)
            System.err.println(getCurTime() + o);
    }

    public static void e(Object o) {
        System.err.println(getCurTime() + o);
    }

    private static String getCurTime() {
        return new SimpleDateFormat("MM/dd HH:mm:ss:SSS  ").format(new Date(System.currentTimeMillis()));
    }
}

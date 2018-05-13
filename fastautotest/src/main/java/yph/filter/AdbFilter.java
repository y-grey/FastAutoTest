package yph.filter;

/**
 * Created by _yph on 2018/5/12 0012.
 */

public class AdbFilter implements Filter {

    private static String filter[] = new String[]{"List of devices attached", "offline", "adb server version",
            "daemon not running", "adb server is out of date", "daemon started successfully", "not found", "Failed to"
            , "No such file or directory"};

    private static AdbFilter adbFilter = new AdbFilter();

    public static Filter get() {
        return adbFilter;
    }

    @Override
    public boolean filt(String line) {
        if (line.trim().equals("")) return false;
        boolean b = true;
        for (String filt : filter) {
            if (line.contains(filt)) {
                b = false;
                break;
            }
        }
        return b;
    }
}

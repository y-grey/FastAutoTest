package yph.filter;

import static yph.base.BaseTest.perforMonitorTl;

/**
 * Created by _yph on 2018/5/12 0012.
 */

public class CrashFilter extends AdbFilter {

    @Override
    public boolean filt(String line) {
        if (super.filt(line)) {
            int pid = perforMonitorTl.get().pid;
            if (line.contains("" + pid)) {
                if (line.contains("at ") || line.contains("Exception:")) {
                    if (line.contains("at android.") || line.contains("at java.") ||
                            line.contains("at com.android.") || line.contains("at dalvik."))
                        return false;
                    return true;
                }
            }
        }
        return false;
    }
}

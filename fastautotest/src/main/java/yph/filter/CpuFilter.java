package yph.filter;

import yph.bean.Configure;

/**
 * Created by _yph on 2018/5/12 0012.
 */

public class CpuFilter extends AdbFilter {
    private static CpuFilter cpuFilter = new CpuFilter();

    public static Filter get() {
        return cpuFilter;
    }
    @Override
    public boolean filt(String line) {
        if (super.filt(line)) {
            if (!line.contains(Configure.get().getAppPackage() + ":")) {
                return true;
            }
        }
        return false;
    }
}

package yph.filter;

import yph.utils.Log;

/**
 * Created by _yph on 2018/5/12 0012.
 */

public class AnrFilter extends AdbFilter {
    boolean isFind = false;
    boolean neverFind = false;

    @Override
    public boolean filt(String line) {
        if (super.filt(line)) {
            if(neverFind)return false;
            if(isFind && line.contains("\"")){
                neverFind = true;
                return false;
            }
            if (!line.contains("at ")) return false;
            if (line.contains("at android.") || line.contains("at java.")
                    || line.contains("at com.android.") || line.contains("at 2")) {
                return false;
            }
            isFind = true;
            Log.e(line);
            return true;
        }
        return false;
    }
}

package yph.helper;

import yph.bean.Configure;
import yph.bean.TestBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by _yph on 2018/5/6 0006.
 */

public class RestartTestHelper {

    private static ThreadLocal<List<Boolean>> restartTestListTl = new ThreadLocal<>();

    private static List<Boolean> getRestartTestList() {
        if (restartTestListTl.get() == null) {
            List<Boolean> restartTestList = new ArrayList<>();
            List<TestBean> testBeans = Configure.get().getTestBeans();
            for (int i = 0 ; i< testBeans.size();i++) {
                if(i == 0)
                    restartTestList.add(true);
                else
                    restartTestList.add(testBeans.get(i).isRestart());
            }
            restartTestListTl.set(restartTestList);
        }
        return restartTestListTl.get();
    }

    public static boolean isCurTestRestart() {
        List<Boolean> restartTestList = getRestartTestList();
        if(restartTestList.isEmpty()) return true;
        boolean isCurTestRestart = restartTestList.get(0);
        restartTestList.remove(0);
        return isCurTestRestart;
    }

    public static boolean isNextTestRestart() {
        List<Boolean> restartTestList = getRestartTestList();
        if(restartTestList.isEmpty()) return true;
        boolean isCurTestRestart = restartTestList.get(0);
        return isCurTestRestart;
    }
}

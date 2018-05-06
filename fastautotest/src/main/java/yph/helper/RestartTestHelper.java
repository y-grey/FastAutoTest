package yph.helper;

import java.util.ArrayList;
import java.util.List;

import yph.bean.Configure;
import yph.bean.TestBean;

/**
 * Created by _yph on 2018/5/6 0006.
 */

public class RestartTestHelper {

    private static ThreadLocal<List<Boolean>> restartTestListTl = new ThreadLocal<>();

    private static List<Boolean> getRestartTestList() {
        if (restartTestListTl.get() == null) {
            List<Boolean> restartTestList = new ArrayList<>();
            List<TestBean> testBeans = Configure.get().getTestBeans();
            for (TestBean testBean : testBeans) {
                restartTestList.add(testBean.isRestart());
            }
            restartTestListTl.set(restartTestList);
        }
        return restartTestListTl.get();
    }

    public static boolean isCurTestRestart() {
        List<Boolean> restartTestList = getRestartTestList();
        if(restartTestList.isEmpty())return true;
        boolean isCurTestRestart = restartTestList.get(0);
        restartTestList.remove(0);
        System.out.println("isCurTestRestart  "+isCurTestRestart);
        return isCurTestRestart;
    }

    public static boolean isNextTestRestart() {
        List<Boolean> restartTestList = getRestartTestList();
        if(restartTestList.isEmpty()){
            System.out.println("isNextTestRestart  true");
            return true;
        }
        boolean isCurTestRestart = restartTestList.get(0);
        System.out.println("isNextTestRestart  "+isCurTestRestart);
        return isCurTestRestart;
    }
}

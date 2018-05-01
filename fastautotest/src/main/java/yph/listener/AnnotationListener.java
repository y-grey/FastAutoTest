package yph.listener;

import org.testng.IAnnotationTransformer;
import org.testng.IRetryAnalyzer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Created by _yph on 2018/4/15 0015.
 */

public class AnnotationListener implements IAnnotationTransformer {

    public AnnotationListener(int maxRetryCount) {
        TestRetryListener.maxRetryCount = maxRetryCount;
    }

    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        IRetryAnalyzer retry = annotation.getRetryAnalyzer();
        if (retry == null) {
            annotation.setRetryAnalyzer(TestRetryListener.class);
        }
    }
}

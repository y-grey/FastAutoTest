package yph.bean;

/**
 * Created by yph on 2018/3/23.
 */

public class TestBean {
    private String name;
    private Class[] classes;

    public String getName() {
        return name;
    }

    public TestBean setName(String name) {
        this.name = name;
        return this;
    }

    public Class[] getClasses() {
        return classes;
    }

    public TestBean setClasses(Class[] classes) {
        this.classes = classes;
        return this;
    }
}

package yph.bean;

/**
 * Created by yph on 2018/3/23.
 */

public class TestBean {
    private String name;
    private Class[] classes;
    private boolean restart = true;

    public TestBean() {
    }

    public TestBean(String name, Class[] classes) {
        this.name = name;
        this.classes = classes;
    }

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

    public boolean isRestart() {
        return restart;
    }

    public TestBean notRestart() {
        restart = false;
        return this;
    }
}

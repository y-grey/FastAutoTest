package yph.bean;

/**
 * Created by _yph on 2018/4/15 0015.
 */

public class Device {
    private String name;
    private String cpu;
    private String mem;

    public Device() {
    }
    
    public Device(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Device setName(String name) {
        this.name = name;
        return this;
    }

    public String getCpu() {
        return cpu;
    }

    public Device setCpu(String cpu) {
        this.cpu = cpu;
        return this;
    }

    public String getMem() {
        return mem;
    }

    public Device setMem(String mem) {
        this.mem = mem;
        return this;
    }
}

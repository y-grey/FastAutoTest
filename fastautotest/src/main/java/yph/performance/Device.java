package yph.performance;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by _yph on 2018/4/15 0015.
 */

public class Device {
    public static List<Device> deviceList = new ArrayList<>();
    private String name;
    private List<Integer> cpuList = new ArrayList<>();
    private List<Integer> memList = new ArrayList<>();
    private List<Long> trafficList = new ArrayList<>();
    private List<String> curStackList = new ArrayList<>();

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

    public List<Integer> getCpuList() {
        return cpuList;
    }

    public Device setCpu(int cpu) {
        cpuList.add(cpu);
        return this;
    }

    public List<Integer> getMemList() {
        return memList;
    }

    public Device setMem(int mem) {
        memList.add(mem);
        return this;
    }

    public List<Long> getTrafficList() {
        return trafficList;
    }

    public Device setTraffic(long traffic) {
        trafficList.add(traffic);
        return this;
    }

    public List<String> getCurStackList() {
        return curStackList;
    }

    public Device setCurStack(String curStack) {
        curStackList.add("'"+curStack+"'");
        return this;
    }
}

package yph.utils;

/**
 * Created by _yph on 2019/3/2 0002.
 */

public class CpuSnapshot {

    public long user = 0;
    public long system = 0;
    public long idle = 0;
    public long ioWait = 0;
    public long total = 0;
    public long app = 0;


    public CpuSnapshot(long user, long system, long idle, long ioWait, long total, long app) {
        this.user = user;
        this.system = system;
        this.idle = idle;
        this.ioWait = ioWait;
        this.total = total;
        this.app = app;
    }


}
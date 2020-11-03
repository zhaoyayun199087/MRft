package mingrifuture.gizlib.code.provider;

/**
 * Created by Administrator on 2017/5/24.
 */
public class AppCmd {
    int cmd;
    Object date;
    public AppCmd(int cmd,Object date){
        this.cmd = cmd;
        this.date = date;
    }

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public Object getDate() {
        return date;
    }

    public void setDate(Object date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "AppCmd{" +
                "cmd=" + cmd +
                ", date=" + date +
                '}';
    }
}

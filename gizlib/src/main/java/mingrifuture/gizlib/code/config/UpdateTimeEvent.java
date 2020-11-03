package mingrifuture.gizlib.code.config;

/**
 * Created by pengl on 2016/1/18.
 */
public class UpdateTimeEvent {
    private String time;

    public UpdateTimeEvent(String time){
        this.time = time;
    }

    public String getTime() {
        return time;
    }
}

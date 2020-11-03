package mingrifuture.gizlib.code.config;

/**
 * Created by pengl on 2016/1/28.
 */
public class ReceiveDataEvent {
    private byte[] data;

    public ReceiveDataEvent(byte[] data){
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }
}

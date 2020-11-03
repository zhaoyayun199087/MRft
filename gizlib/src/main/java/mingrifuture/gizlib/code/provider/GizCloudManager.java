package mingrifuture.gizlib.code.provider;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import mingrifuture.gizlib.code.remote.RemoteControlService;

/**
 * Created by Administrator on 2017/5/23.
 */
public class GizCloudManager {

    private Context context;
    private GizCloudCallback callback;
    private RemoteControlService mRemoteBinder;


    public GizCloudManager(Context c, GizCloudCallback callback){
        this.callback = callback;
        this.context = c;
    }

    public void initGizCloud(){
        Intent homeIntent = new Intent(context, RemoteControlService.class);
        context.getApplicationContext().bindService(homeIntent, HomeConnection,
                Service.BIND_AUTO_CREATE);
    }

    public void sendDateToGizCloud(byte[] date) throws Exception{
        mRemoteBinder.sendDateToGizCloud(date);
    }

    /**
     * bind 远程控制服务时的连接接口
     */
    private ServiceConnection HomeConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mRemoteBinder = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mRemoteBinder = ((RemoteControlService.RemoteControlBinder) service)
                    .getService();
            mRemoteBinder.setCallBack(callback);
        }
    };

    public void onDestroy(){
        if (mRemoteBinder != null)
            context.getApplicationContext().unbindService(HomeConnection);
    }
}

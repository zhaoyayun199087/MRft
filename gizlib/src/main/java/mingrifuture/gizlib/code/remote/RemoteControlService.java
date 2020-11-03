package mingrifuture.gizlib.code.remote;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import mingrifuture.gizlib.code.config.ReceiveDataEvent;
import mingrifuture.gizlib.code.config.WifiStateChangedEvent;
import mingrifuture.gizlib.code.provider.AppCmd;
import mingrifuture.gizlib.code.provider.BizCmdConverter;
import mingrifuture.gizlib.code.provider.BizCmdConverterForMrFuture;
import mingrifuture.gizlib.code.provider.GizCloudCallback;
import mingrifuture.gizlib.code.util.CommonUtils;
import mingrifuture.gizlib.code.util.FileUtils;
import mingrifuture.gizlib.code.util.LogUtils;
import com.wlt.eclipse.paho.client.mqttv3.MqttPersistenceException;

import java.util.Timer;
import java.util.TimerTask;

public class RemoteControlService extends Service {
    /**
     * TCP连接异常，需要重启,指的是serversocket
     **/
    public final static int TCP_EXCEPTION = 103;

    /**
     * TCP关闭
     */
    public final static int TCP_CLOSE = 104;

    /**
     * 检查网络
     */
    public final static int CHECK_NETWORK = 105;

    /**
     * 收到TCP数据包
     */
    public final static int TCP_RECEIVEDATA = 106;

    /**
     * 网络连接是否开始
     */
    public static boolean isStarted = false;

    /**
     * 本地网络的TCP服务端
     */
    private TCPServerSocket mTcpServerSocket;

    /**
     * 连接机智云的服务端，用于服务启动时和机智云进行TCP长连接，用于不同网络App访问的接口
     */
    private WLTServerSocket mWltServerSocket;

    /**
     * 定时检查网络计时器
     */
    private Timer mTimer = new Timer();

    private final IBinder mBinder = new RemoteControlBinder();

    /**
     * 当前app的数据状态字节码
     */
    private byte appStatus[] = null;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                switch (msg.what) {
                    case TCP_EXCEPTION:
                        performNetConnect();
                        break;
                    case CHECK_NETWORK:
                        FileUtils.writeLogToFile("checkNetwork....", new byte[]{});
                        performNetConnect();
                        break;
                    case TCP_RECEIVEDATA:
                        LogUtils.d("tcp receve date");
                        callback.OnChangeDate((AppCmd) msg.obj );
//                        sendDateToGizCloud(BizCmdConverterForMrFuture.deviceMsgToGizConverter());
                        break;
                }
            } catch (Exception e) {
                FileUtils.printExceptionToFile(e, "handle Message failed1:");
            }
        }

    };

    @Override
    public void onCreate() {
        super.onCreate();

        // 注册订阅

        mWltServerSocket = new WLTServerSocket(this, mHandler);

//        IntentFilter filter = new IntentFilter();
//        filter.addAction(UsbDeviceService.ACTION_RECEIVE_USB_DATA);
//        registerReceiver(mUsbDataReceiver, filter);
    }

    /**
     * 绑定服务时，启动tcp,udp连接，计时任务
     */
    @Override
    public IBinder onBind(Intent intent) {
        performNetConnect();

        isStarted = true;
        // 每隔10秒检查一下网络
        mTimer.schedule(new TimeTimerTask(), 10000, 10000);
        return mBinder;
    }

    /**
     * 重新绑定服务时
     */
    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        FileUtils.writeLogToFile("onRebind....", new byte[]{});
        performNetConnect();
        isStarted = true;
    }

    /**
     * 解除绑定服务时
     */
    @Override
    public boolean onUnbind(Intent intent) {
        FileUtils.writeLogToFile("onUnbind....", new byte[]{});
        releaseConnection();
        isStarted = false;
        mTimer.cancel();
        mTimer = null;
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 重启网络连接
     */
    public void reOpen() {
        releaseConnection();

        try {
            performNetConnect();
        } catch (Exception e) {
            FileUtils.printExceptionToFile(e, "reOpen failed:");
        }
    }

    /**
     * 启动网络连接
     */
    private void performNetConnect() {
        try {
            mWltServerSocket.networkIsAble();
            startTcpService();
            startUdpService();
        } catch (Exception e) {
            FileUtils.printExceptionToFile(e, "openNetConnection failed:");
        }

    }

    private void startTcpService() {
        // 启动本地服务端套接字,隔10s后启动连接云服务端的套接字
        if (mTcpServerSocket == null) {
            mTcpServerSocket = new TCPServerSocket(mHandler, this);
            mTcpServerSocket.start();
            return;
        }

        mWltServerSocket.startTcpService();

        if (!mTcpServerSocket.isRun()) {
            mTcpServerSocket = null;
            startTcpService();
        }
    }

    private void startUdpService() throws Exception {
        mWltServerSocket.startUdpService();
    }

    private StringBuffer cmd = new StringBuffer();
    // private String cmdStart = "ff";
    private String cmdEnd = "ef";

    private GizCloudCallback callback;

    public void setCallBack(GizCloudCallback callBack){
        this.callback = callBack;
    }

    public void sendDateToGizCloud(byte[] data) {
            onReceiveUsbData(data);
    };

    /**
     * wifi连接状态的改变
     *
     * @param event
     */
    public void onEventMainThread(WifiStateChangedEvent event) {
        performNetConnect();
    }

    /**
     * 对Usb收到的数据进行处理
     *
     * @param data
     */
    protected void onReceiveUsbData(byte[] data) {
        LogUtils.d("send data to remote 1");
        if (mTcpServerSocket != null && mTcpServerSocket.isRun()) {
            try {
                LogUtils.d("send data to remote 2 " + CommonUtils.decodeBytesToHexString(data));
                mTcpServerSocket.writeBytes(data);
            } catch (Exception e) {
                LogUtils.d("send data to remote -2");

                e.printStackTrace();
                FileUtils.printExceptionToFile(e,
                        "send data to app failed by local network");
            }
        }
        try {
            if (null != mWltServerSocket.getWltMqttClient()) {
                LogUtils.d("send data to remote 3");
                mWltServerSocket.getWltMqttClient().sendMsg2App(data);
            }
        } catch (MqttPersistenceException e) {
            e.printStackTrace();
            LogUtils.d("send data to remote -3");

            FileUtils.printExceptionToFile(e,
                    "send data to app failed by remote");
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.d("send data to remote -4");

            FileUtils.printExceptionToFile(e,
                    "send data to app failed by remote");
        }
        // 防止因为网络未打开，而关闭监听
        if (null == mTcpServerSocket) {
            performNetConnect();
        }
    }

    /**
     * 释放连接
     */
    private void releaseConnection() {
        FileUtils.writeLogToFile("starting stopConnection...", new byte[]{});

        mWltServerSocket.releaseConnection();

        if (mTcpServerSocket != null) {
            mTcpServerSocket.close();
            mTcpServerSocket = null;
        }
    }

    /**
     * 定时检查网络的时间任务
     */
    class TimeTimerTask extends TimerTask {
        @Override
        public void run() {
            mHandler.obtainMessage(CHECK_NETWORK).sendToTarget();
        }
    }

    public class RemoteControlBinder extends Binder {
        public RemoteControlService getService() {
            return RemoteControlService.this;
        }
    }

    public byte[] getCurAppStatus() {
        return appStatus;
    }

    public synchronized void setCurAppStatus(byte[] appStatus) {
        this.appStatus = appStatus;
    }

}

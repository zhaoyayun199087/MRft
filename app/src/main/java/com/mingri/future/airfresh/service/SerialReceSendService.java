package com.mingri.future.airfresh.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.mingri.future.airfresh.R;
import com.mingri.future.airfresh.bean.ReceDataFromMachine;
import com.mingri.future.airfresh.bean.SendDataToMachine;
import com.mingri.future.airfresh.bean.SendDateToGiz;
import com.mingri.future.airfresh.bean.SendUpdateDataToMachine;
import com.mingri.future.airfresh.bean.Task;
import com.mingri.future.airfresh.util.CommonUtils;
import com.mingri.future.airfresh.util.CreateCmdToMachineFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import mingrifuture.gizlib.code.config.Constants;
import mingrifuture.gizlib.code.provider.MachineStatusForMrFrture;
import mingrifuture.gizlib.code.util.FileUtils;
import mingrifuture.gizlib.code.util.LogUtils;
import mingrifuture.gizlib.code.util.SPUtils;

import com.mingri.future.airfresh.util.TaskQueue;
import com.smatek.uart.UartComm;


/**
 * 串口收发服务
 * Created by Cjh on 2017/3/14.
 */
public class SerialReceSendService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private UartComm UC;
    private int uart_fd;
    private Handler mRecvHandler = null;
    private Handler mHander = new Handler();
    private HandlerThread thread;
    private byte[] cmdByte = new byte[1000];
    private int cmdLen = 0;
    private int cmdNum = 0;
    private int mCmdLen;


    private volatile boolean mRunning = true;
    private boolean mNeedSendData = false;
    private static Thread mSendDateThread;
    private static Runnable mSendDateRunnable;
    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        UC = new UartComm();
        //硬件串口端
        uart_fd = UC.uartInit("/dev/ttyS3");
        //频率设置
        UC.setOpt(uart_fd, 9600, 8, 0, 1);
        //开启收发线程
        thread = new HandlerThread("UartRecvThread233");
        thread.start();//创建一个HandlerThread并启动它
        mRecvHandler = new Handler(thread.getLooper());
        mRecvHandler.post(mRecvRunnable);

        mSendDateRunnable = new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                sendDate();
            }
        };
        mSendDateThread = new Thread(mSendDateRunnable);
        mSendDateThread.start();
    }

    private void sendDate() {
        Task t = TaskQueue.getTask();
        while (true) {
            if (t != null && t.getData().length > 0) {
//                FileUtils.writeLogToFile1("write date to usb date is -->"
//                        + CommonUtils.decodeBytesToHexString(t.getData()), new byte[]{});

                write(t.getData());
            }
            MachineStatusForMrFrture.sSendRecvNum++;
            if( MachineStatusForMrFrture.sSendRecvNum > 10 ){
                FileUtils.writeLogToFile1("send 10 times but not receive date", new byte[]{});
                MachineStatusForMrFrture.sSendRecvNum = 0;
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            t = TaskQueue.getTask();
        }
    }

    public void write(int[] data) {
        mNeedSendData = true;
        //设置为0 发送数据
        UC.setRS485WriteRead(0);
        UC.send(uart_fd, data, data.length);
        mNeedSendData = false;
        UC.setRS485WriteRead(1);
    }

    private int iCount = 0;
    Runnable mRecvRunnable = new Runnable() {
        @Override
        public void run() {
            while (mRunning) {
                int size;
                //每次接受数据的最大容量
                int[] buffer = new int[256];
                if (!mNeedSendData) {
                    //set RS485 to send data mode
                    //设置为1 接受数据
                    UC.setRS485WriteRead(1);
                    size = UC.recv(uart_fd, buffer, 255);
                    if (size > 0) {
                        MachineStatusForMrFrture.sSendRecvNum = 0;
                        byte[] msg = new byte[size];
                        for (int i = 0; i < size; i++) {
                            msg[i] = (byte) buffer[i];
                        }
                        try {
                            if( MachineStatusForMrFrture.bUpdating ) {
                                EventBus.getDefault().post(new ReceDataFromMachine(msg));
                                continue;
                            }
                            //接收到的数据
//                        FileUtils.writeLogToFile1("recv serial date ---> " + CommonUtils.decodeBytesToHexString(msg),new byte[]{});
                            System.arraycopy(msg, 0, cmdByte, cmdNum, msg.length);
                            cmdNum += msg.length;
                            if (cmdByte.length >= 9 && CommonUtils.decodeByteToHexString(cmdByte[0]).equals("ff") && CommonUtils.decodeByteToHexString(cmdByte[1]).equals("ff")) {
                                cmdLen = CommonUtils.getIntFromTwoByte(cmdByte[2], cmdByte[3]);
                            }
                            LogUtils.d("recv serial date is ---> " + CommonUtils.decodeBytesToHexString(cmdByte));

                            LogUtils.d("recv serial date is ---> cmdlen cmdNum " + cmdLen + "  " + cmdNum + "  " + msg.length);
                            if( msg.length == 11  ){
                                LogUtils.d("boot mode 1 " + CommonUtils.decodeBytesToHexString(msg) );

                                if( msg[0] == (byte)0xff && msg[1] == (byte)0xaa && msg[2] == (byte)0x00 && msg[3] == (byte)0x07 && msg[4] == (byte)0x87){
                                    LogUtils.d("boot mode 2" );
                                    mHander.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            iCount++;
                                            if( iCount == 3 ){
                                                iCount = 0;
                                                Toast.makeText(SerialReceSendService.this,getString(R.string.boot_tip),Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }

                            if (cmdLen == cmdNum - 4) {
//                          FileUtils.writeLogToFile1("cmd len is " + cmdLen + " num is " + cmdNum + " date is -----" , cmdByte);
                                dispatchDate(cmdByte, cmdNum);
                                cmdNum = 0;
                                cmdLen = 0;
                            }
                            if( cmdLen < (cmdNum - 4) ){
                                cmdLen = 0;
                                cmdNum = 0;
                            }
                        }catch (Exception e){
                            cmdLen = 0;
                            cmdNum = 0;
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    };

    /**
     * 分发串口上传过来的数据，解析数据，并上传至giz云服务器
     * @param date
     */
    private void dispatchDate(byte[] date,int len){
        parseDate(date,len);
    }


    private void parseDate(byte[] mmMessages, int mMessageLen) {
        mCmdLen = CommonUtils.getIntFromTwoByte(mmMessages[2], mmMessages[3]);


        if (mmMessages[0] != Constants.ORDER_HEAD
                && mmMessages[1] != Constants.ORDER_HEAD) {
           LogUtils.d("receve date from machine head error");
            return;
        }
        byte sum = CreateCmdToMachineFactory.getPackageCheckSum(mmMessages, mMessageLen);
        if (mmMessages[mMessageLen - 1] != sum) {
            LogUtils.d("receve date from machine check sum error");
            return;
        }

        // 重启回复
        if (mmMessages[4] == Constants.ORDER_REBOOT_REP) {
            LogUtils.d("receve date from machine reboot rep");
        }
        // 非法消息回复
        else if (mmMessages[4] == Constants.ORDER_ILLEGAL_REP) {
            LogUtils.d("receve date from machine illegal rep");

        } else if (mmMessages[4] == Constants.ORDER_READ_STATUS_REP) {
            // 上位机控制回复
            if (mCmdLen == 5) {
                 LogUtils.d("receve date from machine control rep");
            }
            // 读取设备状态回复
            else {
                LogUtils.d("receve date from machine read status rep");
                parseDeviceStatus(mmMessages);
                updateDateToGiz();
            }
        }
    }

    private boolean bNeedResend;
    private void parseDeviceStatus(byte[] msg) {
        int sn;
        int dateOffset = 9;

        sn = msg[5];
        int date = (msg[dateOffset + 2] & 0x01);
        if (date == 1) {
            MachineStatusForMrFrture.Switch= true;
        } else {
            MachineStatusForMrFrture.Switch= false;
        }
        LogUtils.d("power machine is " + MachineStatusForMrFrture.Switch);
        //离子开关1
        date = (msg[dateOffset + 2] & 0x02);
        if (date == 0x02) {
            MachineStatusForMrFrture.Switch_Plasma1= true;
        } else {
            MachineStatusForMrFrture.Switch_Plasma1= false;
        }
        LogUtils.d("plasma1 " +  MachineStatusForMrFrture.Switch_Plasma1);
        //儿童锁
        date = (msg[dateOffset + 2] & 0x04);
//        if (date == 0x04) {
//            MachineStatusForMrFrture.Child_Security_Lock= true;
//        } else {
//            MachineStatusForMrFrture.Child_Security_Lock= false;
//        }
        //新风阀门开关
        date = (msg[dateOffset + 2] & 0x08);
        if (date == 0x08) {
            MachineStatusForMrFrture.Switch_Valve= true;
        } else {
            MachineStatusForMrFrture.Switch_Valve= false;
        }
        //离子开关2
        date = (msg[dateOffset + 2] & 0x10);
        if (date == 0x10) {
            MachineStatusForMrFrture.Switch_Plasma2= true;
        } else {
            MachineStatusForMrFrture.Switch_Plasma2= false;
        }
        //离子开关3
        date = (msg[dateOffset + 2] & 0x20);
        if (date == 0x20) {
            MachineStatusForMrFrture.Switch_Plasma3= true;
        } else {
            MachineStatusForMrFrture.Switch_Plasma3= false;
        }
        //uvc开关
        date = (msg[dateOffset + 2] & 0x40);
        if (date == 0x40) {
            MachineStatusForMrFrture.Switch_UVC= true;
        } else {
            MachineStatusForMrFrture.Switch_UVC= false;
        }
        LogUtils.d("resend msg cmd 2 " + MachineStatusForMrFrture.Switch_UVC );

        //ptc开关
        date = (msg[dateOffset + 2] & 0x80);
        if (date == 0x80) {
            MachineStatusForMrFrture.Switch_PTC= true;
        } else {
            MachineStatusForMrFrture.Switch_PTC= false;
        }
        //静电集成开关
        date = (msg[dateOffset + 1] & 0x01);
        if (date == 1) {
            MachineStatusForMrFrture.Switch_Electrostatic= true;
        } else {
            MachineStatusForMrFrture.Switch_Electrostatic= false;
        }
        //自定义风速
        MachineStatusForMrFrture.Wind_Velocity= (byte)((msg[dateOffset + 1] >>1) & 0x0f);
        //运行模式
        MachineStatusForMrFrture.Mode= (byte)((msg[dateOffset + 1] >>5) & 0x07);
        //调压仓
        MachineStatusForMrFrture.Surge_tank= (byte)(( (msg[dateOffset ] ) & 0x07  ));
        //初效滤网寿命
        MachineStatusForMrFrture.Filter_Life1= msg[dateOffset + 3];
        //温度
        MachineStatusForMrFrture.Temperature_Quality= msg[dateOffset + 4];
        //中效滤网寿命
        MachineStatusForMrFrture.UVC_Life= msg[dateOffset + 5];
        //活性炭滤网寿命
        MachineStatusForMrFrture.Filter_Life2= msg[dateOffset + 6];
        //高效滤网寿命
        MachineStatusForMrFrture.Filter_Life3= msg[dateOffset + 7];
        //高效滤网寿命
        MachineStatusForMrFrture.Filter_Life4= msg[dateOffset + 8];
        //保留
        MachineStatusForMrFrture.set_byte1= msg[dateOffset + 9];
        //保留
        MachineStatusForMrFrture.humidity= msg[dateOffset + 10];
        //定时开机
//        MachineStatusForMrFrture.Timing_On= CommonUtils.getIntFromTwoByte(msg[dateOffset + 11], msg[dateOffset + 12]);
        //定时关机
//        MachineStatusForMrFrture.Timing_Off= CommonUtils.getIntFromTwoByte(msg[dateOffset + 13], msg[dateOffset + 14]);
        //室内voc
        MachineStatusForMrFrture.VOC_Quality= CommonUtils.getIntFromTwoByte(msg[dateOffset + 15], msg[dateOffset + 16]);
        //保留
        MachineStatusForMrFrture.set_int1= CommonUtils.getIntFromTwoByte(msg[dateOffset + 17], msg[dateOffset + 18]);
        //保留
        MachineStatusForMrFrture.set_int2= CommonUtils.getIntFromTwoByte(msg[dateOffset + 19], msg[dateOffset + 20]);
        MachineStatusForMrFrture.read_byte1=  msg[dateOffset + 21];
        MachineStatusForMrFrture.PM25_Indoor= CommonUtils.getIntFromTwoByte(msg[dateOffset + 22], msg[dateOffset + 23]);
        MachineStatusForMrFrture.HCHO_Quality= CommonUtils.getIntFromTwoByte(msg[dateOffset + 24], msg[dateOffset + 25]);
        MachineStatusForMrFrture.CO2_value= CommonUtils.getIntFromTwoByte(msg[dateOffset + 26], msg[dateOffset + 27]);
        MachineStatusForMrFrture.read_int1= CommonUtils.getIntFromTwoByte(msg[dateOffset + 28], msg[dateOffset + 29]);
        MachineStatusForMrFrture.read_int2= CommonUtils.getIntFromTwoByte(msg[dateOffset + 30], msg[dateOffset + 31]);

        if(MachineStatusForMrFrture.soft_version != MachineStatusForMrFrture.set_int1){
            MachineStatusForMrFrture.soft_version = MachineStatusForMrFrture.set_int1;
            SPUtils.put(this, "soft_version", MachineStatusForMrFrture.soft_version);
        }
        if(MachineStatusForMrFrture.hard_version != MachineStatusForMrFrture.read_int2){
            MachineStatusForMrFrture.hard_version = MachineStatusForMrFrture.read_int2;
            SPUtils.put(this, "hard_version", MachineStatusForMrFrture.hard_version);
        }

        //此两项未确定
        MachineStatusForMrFrture.Alert_Air_Quality = false;
        MachineStatusForMrFrture.Alert_VOC = false;

        if( MachineStatusForMrFrture.UVC_Life < 10 ) {
            MachineStatusForMrFrture.Alert_UVC_Life = true;
        }else{
            MachineStatusForMrFrture.Alert_UVC_Life = false;
        }

        if( MachineStatusForMrFrture.Filter_Life1 < 10  ){
            MachineStatusForMrFrture.Alert_Filter_Life1 = true;
        }else{
            MachineStatusForMrFrture.Alert_Filter_Life1 = false;
        }
        if( MachineStatusForMrFrture.Filter_Life2 < 10  ){
            MachineStatusForMrFrture.Alert_Filter_Life2 = true;
        }else{
            MachineStatusForMrFrture.Alert_Filter_Life2 = false;
        }
        if( MachineStatusForMrFrture.Filter_Life3 < 10  ){
            MachineStatusForMrFrture.Alert_Filter_Life3 = true;
        }else{
            MachineStatusForMrFrture.Alert_Filter_Life3 = false;
        }
        if( MachineStatusForMrFrture.Filter_Life4 < 10  ){
            MachineStatusForMrFrture.Alert_Filter_Life4 = true;
        }else{
            MachineStatusForMrFrture.Alert_Filter_Life4 = false;
        }
        if( MachineStatusForMrFrture.HCHO_Quality > 10 ){
            MachineStatusForMrFrture.Alert_HCHO = true;
        }else{
            MachineStatusForMrFrture.Alert_HCHO = false;
        }
        if( MachineStatusForMrFrture.CO2_value > 1000 ){
            MachineStatusForMrFrture.Alert_CO2 = true;
        }else{
            MachineStatusForMrFrture.Alert_CO2 = false;
        }
        if(MachineStatusForMrFrture.read_int1 > 50 ){
            MachineStatusForMrFrture.Alert_CH4 = true;
        }else{
            MachineStatusForMrFrture.Alert_CH4 = false;
        }

        if( (msg[dateOffset +34] & 0x01) == 0x01 ){
            MachineStatusForMrFrture.Fault_Motor = true;
        }else{
            MachineStatusForMrFrture.Fault_Motor = false;
        }
        if( (msg[dateOffset +34] & 0x02) == 0x02 ){
            MachineStatusForMrFrture.fault_CO2 = true;
        }else{
            MachineStatusForMrFrture.fault_CO2 = false;
        }
        if( (msg[dateOffset +34] & 0x04) == 0x04 ){
            MachineStatusForMrFrture.fault_PM25 = true;
        }else{
            MachineStatusForMrFrture.fault_PM25 = false;
        }
        if( (msg[dateOffset +34] & 0x08) == 0x08 ){
            MachineStatusForMrFrture.fault_VOC = true;
        }else{
            MachineStatusForMrFrture.fault_VOC = false;
        }
        if( (msg[dateOffset +34] & 0x10) == 0x10 ){
            MachineStatusForMrFrture.fault_HCHO = true;
        }else{
            MachineStatusForMrFrture.fault_HCHO = false;
        }
        if( (msg[dateOffset +34] & 0x20) == 0x20 ){
            MachineStatusForMrFrture.fault_CH4 = true;
        }else{
            MachineStatusForMrFrture.fault_CH4 = false;
        }
        if( (msg[dateOffset +34] & 0x40) == 0x40 ){
            MachineStatusForMrFrture.fault_tmp = true;
        }else{
            MachineStatusForMrFrture.fault_tmp = false;
        }
        if( (msg[dateOffset +34] & 0x80) == 0x80 ){
            MachineStatusForMrFrture.fault_humidity = true;
        }else{
            MachineStatusForMrFrture.fault_humidity = false;
        }

         bNeedResend = false;
         reSendMsgIfError(sn);
        if( bNeedResend == false){
            CommonUtils.setOrder(0, 0);
            LogUtils.d("recv date");
            EventBus.getDefault().post(new ReceDataFromMachine(msg));
        }

        MachineStatusForMrFrture.bRecvSerialDate = true;
    }

    private void reSendMsgIfError(int sn) {
        int cmd = CommonUtils.getOrderNo();
        int order = CommonUtils.getOrderObject();
        int sendSn = CommonUtils.getSn();
        if (sn != sendSn) {
            return;
        }

        switch (cmd) {
            case Constants.ANDROID_SEND_POWER:
                if (MachineStatusForMrFrture.Switch && order == 0) {
                    MachineStatusForMrFrture.Switch = false;
                    LogUtils.d("power  3 is " + MachineStatusForMrFrture.Switch);
                    bNeedResend = true;
                    reSendCmd(cmd, sn);
                }
                if (!MachineStatusForMrFrture.Switch && order == 1) {
                    MachineStatusForMrFrture.Switch = true;
                    bNeedResend = true;
                    reSendCmd(cmd, sn);
                }
                break;
            case Constants.ANDROID_SEND_SWITCH_PLASMAL1:
                if (MachineStatusForMrFrture.Switch_Plasma1 && order == 0) {
                    MachineStatusForMrFrture.Switch_Plasma1 = false;
                    bNeedResend = true;
                    reSendCmd(cmd, sn);
                }
                if (!MachineStatusForMrFrture.Switch_Plasma1 && order == 1) {
                    MachineStatusForMrFrture.Switch_Plasma1 = true;
                    bNeedResend = true;
                    reSendCmd(cmd, sn);
                }
//            case Constants.ANDROID_SEND_SWITCH_CHIRLD:
//                if (MachineStatusForMrFrture.Child_Security_Lock && order == 0) {
//                    MachineStatusForMrFrture.Child_Security_Lock = false;
//                    bNeedResend = true;
//                    reSendCmd(cmd, sn);
//                }
//                if (!MachineStatusForMrFrture.Child_Security_Lock && order == 1) {
//                    MachineStatusForMrFrture.Child_Security_Lock = true;
//                    bNeedResend = true;
//                    reSendCmd(cmd, sn);
//                }
            case Constants.ANDROID_SEND_SWITCH_FRESH:
                if (MachineStatusForMrFrture.Switch_Valve && order == 0) {
                    MachineStatusForMrFrture.Switch_Valve = false;
                    bNeedResend = true;
                    reSendCmd(cmd, sn);
                }
                if (!MachineStatusForMrFrture.Switch_Valve && order == 1) {
                    MachineStatusForMrFrture.Switch_Valve = true;
                    bNeedResend = true;
                    reSendCmd(cmd, sn);
                }
                break;

            case Constants.ANDROID_SEND_SWITCH_UVC:
                LogUtils.d("resend msg cmd 3 " + cmd + " order " + order + " value " +  MachineStatusForMrFrture.Switch_UVC );

                if (MachineStatusForMrFrture.Switch_UVC && order == 0) {
                    MachineStatusForMrFrture.Switch_UVC = false;
                    bNeedResend = true;
                    reSendCmd(cmd, sn);
                }
                if (!MachineStatusForMrFrture.Switch_UVC && order == 1) {
                    MachineStatusForMrFrture.Switch_UVC = true;
                    bNeedResend = true;
                    reSendCmd(cmd, sn);
                }
                break;
            case Constants.ANDROID_SEND_SWITCH_PTC:
                if (MachineStatusForMrFrture.Switch_PTC && order == 0) {
                    MachineStatusForMrFrture.Switch_PTC = false;
                    bNeedResend = true;
                    reSendCmd(cmd, sn);
                }
                if (!MachineStatusForMrFrture.Switch_PTC && order == 1) {
                    MachineStatusForMrFrture.Switch_PTC = true;
                    bNeedResend = true;
                    reSendCmd(cmd, sn);
                }
                break;
            case Constants.ANDROID_SEND_SWITCH_ELECTROSTATIC:
                if (MachineStatusForMrFrture.Switch_Electrostatic && order == 0) {
                    MachineStatusForMrFrture.Switch_Electrostatic = false;
                    bNeedResend = true;
                    reSendCmd(cmd, sn);
                }
                if (!MachineStatusForMrFrture.Switch_Electrostatic && order == 1) {
                    MachineStatusForMrFrture.Switch_Electrostatic = true;
                    bNeedResend = true;
                    reSendCmd(cmd, sn);
                }
                break;
            case Constants.ANDROID_SEND_WIND_LEVEL:
                if (MachineStatusForMrFrture.Wind_Velocity != order) {
                    MachineStatusForMrFrture.Wind_Velocity = (byte)order;
                    bNeedResend = true;
                    reSendCmd(cmd, sn);
                }
                break;
            case Constants.ANDROID_SEND_MODE:
                if (MachineStatusForMrFrture.Mode != order) {
                    MachineStatusForMrFrture.Mode = (byte)order;
                    bNeedResend = true;
                    reSendCmd(cmd, sn);
                }
                break;
            case Constants.ANDROID_SEND_SURGE_TANK:
                if (MachineStatusForMrFrture.Surge_tank != order) {
                    MachineStatusForMrFrture.Surge_tank = (byte)order;
                    bNeedResend = true;
                    reSendCmd(cmd, sn);
                }
                break;
            case Constants.ANDROID_SEND_LEFTTIME_CHU:
                if (MachineStatusForMrFrture.Filter_Life1 != order) {
                    MachineStatusForMrFrture.Filter_Life1 = (byte)order;
                    bNeedResend = true;
                    reSendCmd(cmd, sn);
                }
                break;
            case Constants.ANDROID_SEND_UVC:
                if (MachineStatusForMrFrture.UVC_Life != order) {
                    MachineStatusForMrFrture.UVC_Life = (byte)order;
                    bNeedResend = true;
                    reSendCmd(cmd, sn);
                }
                break;
            case Constants.ANDROID_SEND_LEFTTIME_ZHONG:
                if (MachineStatusForMrFrture.Filter_Life2 != order) {
                    MachineStatusForMrFrture.Filter_Life2 = (byte)order;
                    bNeedResend = true;
                    reSendCmd(cmd, sn);
                }
                break;
            case Constants.ANDROID_SEND_LEFTTIME_HUOXING:
                if (MachineStatusForMrFrture.Filter_Life3 != order) {
                    MachineStatusForMrFrture.Filter_Life3 = (byte)order;
                    bNeedResend = true;
                    reSendCmd(cmd, sn);
                }
                break;
            case Constants.ANDROID_SEND_LEFTTIME_GAOXIAO:
                if (MachineStatusForMrFrture.Filter_Life4 != order) {
                    MachineStatusForMrFrture.Filter_Life4 = (byte)order;
                    bNeedResend = true;
                    reSendCmd(cmd, sn);
                }
                break;
        }

    }

    private void reSendCmd(int cmd, int sn) {



        int reSendNum = (int) SPUtils.get(this, "resend_num", 0);
        reSendNum++;
        SPUtils.put(this, "resend_num", reSendNum);

        if (cmd == MachineStatusForMrFrture.lastSendCmd) {
            MachineStatusForMrFrture.reSendNum++;
        } else {
            MachineStatusForMrFrture.reSendNum = 0;
        }

        MachineStatusForMrFrture.lastSendCmd = cmd;

        if (MachineStatusForMrFrture.reSendNum >= 3) {
            CommonUtils.setOrder(0, 0);
            MachineStatusForMrFrture.reSendNum = 0;
            return;
        }

        if (cmd == 0) {
            return;
        }

        LogUtils.d("resend num is " + MachineStatusForMrFrture.reSendNum + " cmd is  " + cmd);
        int d[] =  CreateCmdToMachineFactory.createControlCmd(cmd, sn);
        EventBus.getDefault().post(new SendDataToMachine(d));
    }

    private void updateDateToGiz() {
        EventBus.getDefault().post(new SendDateToGiz());
    }

    @Override
    public void onDestroy() {
        //注销EventBus
        EventBus.getDefault().unregister(this);

        UC.uartDestroy(uart_fd);
        mRunning = false;
        mRecvHandler.removeCallbacks(mRecvRunnable);
        //释放资源
        thread.quit();
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void sendSerialData(SendDataToMachine data) {

        if( data.getSendData() == null ){
            int cmd[] = CreateCmdToMachineFactory.createReadStatusCmd();
            int sn1 = 0;
            if( cmd.length > 5 ){
                sn1 = cmd[5];
            }
            CommonUtils.SetSn(sn1);
            TaskQueue.addTask(new Task(cmd, sn1));
            return;
        }

        if (TaskQueue.getTaskNum() > 3) {
            TaskQueue.finishAllTask();
        }
        int sn = 0;
        if( data.getSendData().length > 5 ){
            sn = data.getSendData()[5];
        }
        TaskQueue.addTask(new Task(data.getSendData(), sn));
        int cmd[] = CreateCmdToMachineFactory.createReadStatusCmd();
        int sn1 = 0;
        if( cmd.length > 5 ){
            sn1 = cmd[5];
        }
        CommonUtils.SetSn(sn1);
        TaskQueue.addTask(new Task(cmd, sn1));

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void sendSerialData(SendUpdateDataToMachine data) {

        if( data.getSendData() == null ){

            return;
        }

        TaskQueue.addTask(new Task(data.getSendData(), 0));
    }
}

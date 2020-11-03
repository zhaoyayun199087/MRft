package com.mingri.future.airfresh.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.mingri.future.airfresh.R;
import com.mingri.future.airfresh.bean.CheckOtaEvent;
import com.mingri.future.airfresh.bean.ReceDataFromMachine;
import com.mingri.future.airfresh.bean.SendInitDateEvent;
import com.mingri.future.airfresh.bean.SendUpdateDataToMachine;
import com.mingri.future.airfresh.util.CommonUtils;
import com.mingri.future.airfresh.util.HttpDownloader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.ButterKnife;
import butterknife.InjectView;
import mingrifuture.gizlib.code.provider.MachineStatusForMrFrture;
import mingrifuture.gizlib.code.util.LogUtils;
import mingrifuture.gizlib.code.util.NetUtils;

/**
 * Created by Administrator on 2017/8/14.
 * <p>
 * 点击  请求设备升级 ---> 写入信息 -----> 开始升级 ----> 查询信息 ----> 跳转App命令 ;
 * 如果设备在升级状态下收到app协议或者错误信息，设备会返回非法信息。
 * 跳转到app模式如果失败，只能重新升级然后再尝试跳转(升级中掉电)。
 */

public class TestUpdataActivity extends Activity {
    @InjectView(R.id.container)
    LinearLayout container;
    @InjectView(R.id.btn_down)
    Button btn_down;
    @InjectView(R.id.btn_one_step)
    Button btnOneStep;
    @InjectView(R.id.sv)
    ScrollView sv;
    TextView textView;
    private Button btnDown;
    int wCRC = 0xffff;
    int wCPoly = 0x1021;
    int sn = 0;
    byte[] buffer = null;
    int[] filelen;
    int[] fileCrc;
    int[] hardWare = new int[2];
    int[] softWare= new int[2];
    int WCRC;
    int Sn = 0;
    int cur_off = 0;
    int cur_length = 200;
    private boolean loop = true;
    public Handler handler = new Handler();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.d("test update activity destroy");
        handler.removeCallbacksAndMessages(null);
        handler = null;
        buffer = null;
        container.removeAllViews();
        container = null;
        EventBus.getDefault().unregister(this);
        MachineStatusForMrFrture.bUpdating = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_updata);
        ButterKnife.inject(this);
        EventBus.getDefault().register(this);
        MachineStatusForMrFrture.bUpdating = true;

        hardWare[0] = ((MachineStatusForMrFrture.hard_version >> 8) & 0xff);
        hardWare[1] =((MachineStatusForMrFrture.hard_version ) & 0xff);
        softWare[0] = ((MachineStatusForMrFrture.soft_version >> 8) & 0xff);
        softWare[1] = ((MachineStatusForMrFrture.soft_version ) & 0xff);
        handler.post(new Runnable() {
            @Override
            public void run() {
                textView = new TextView(TestUpdataActivity.this);
                textView.setText("温馨提示：若您非专业人员，请立刻点击\"退出\"按键来退出当前页面。");
                container.addView(textView);
                btn_down.setEnabled(false);
            }
        });
        Toast.makeText(this,"hard " + MachineStatusForMrFrture.hard_version + " soft " + MachineStatusForMrFrture.soft_version, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        MachineStatusForMrFrture.iCount = 0;
        return super.dispatchTouchEvent(ev);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiverData(ReceDataFromMachine event) {
//        textView.setText("收到数据  " + CommonUtils.decodeBytesToHexString(event.getBytes()));
        Log.e("TAG", "  收到数据 " + CommonUtils.decodeBytesToHexString(event.getBytes()));
        parseReceiverData(event.getBytes());
    }

    private boolean bExit = false;
    private void parseReceiverData(byte[] bytes) {
        Log.e("TAG", "parse receve date: "  + mingrifuture.gizlib.code.util.CommonUtils.decodeBytesToHexString(bytes));
        if (bytes[0] == (byte) 0xff && bytes[1] == (byte) 0xaa && bytes[3] == (byte) 0x04 && bytes[4] == (byte) 0x82 && bytes.length == 8) {
            Log.e("TAG", " 成功交互 " + "序列号:" + CommonUtils.decodeByteToHexString(bytes[5]));
//            if( !bExit ) {
//                handler.sendEmptyMessageDelayed(0, 50);
//            }else{
//                handler.removeMessages(0);
//                handler.removeCallbacksAndMessages(null);
//            }
            if( !bExit )
            sendFileInfo();
        } else if (bytes.length == 30 && bytes[3] == (byte) 0x1a && bytes[4] == (byte) 0x82 && bytes[6] != (byte) 0x00) {
            loop = false;   //停止传输 ,  cur_off = cur_off - cur_length; 复位到上次传输的位置
            Log.e("TAG", " 交互失败  如正在传输升级数据则停止传输" + "序列号:" + CommonUtils.decodeByteToHexString(bytes[5]));
            parseErrorCode(bytes[6]);
            //传文件过程发生错误可以重新传
//            reSendDate(bytes);
        } else if (bytes[3] == (byte) 0x29 && bytes[4] == (byte) 0x84 && bytes.length == 45) {
            loop = false;
            Log.e("TAG", " 查询 信息 " + CommonUtils.decodeBytesToHexString(bytes));
            checkUpdateSuccess(bytes);
        } else if (bytes[4] == (byte) 0x86 && bytes.length == 8) {
            Log.e("TAG", "跳转成功" + CommonUtils.decodeBytesToHexString(bytes));
            textView = new TextView(this);
            textView.setText("跳转成功");
            container.addView(textView);
        } else if (bytes[4] == (byte) 0x86 && bytes.length == 30) {
            Log.e("TAG", "跳转失败 " + CommonUtils.decodeBytesToHexString(bytes));
            parseErrorCode(bytes[6]);
        } else if (bytes[1] == (byte) 0xff && bytes[3] == (byte) 0x05 && bytes[4] == (byte) 0x86 && bytes.length == 9) {
            Log.e("TAG", "请求升级成功 " + CommonUtils.decodeBytesToHexString(bytes));
            textView = new TextView(this);
            textView.setText("请求升级成功,可开始发送升级数据");
            container.addView(textView);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    sv.fullScroll(ScrollView.FOCUS_DOWN);
                }
            }, 80);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    LogUtils.d("send conmand info");
                    sendCommandInfo();
                }
            }, 1000);

        } else if (bytes[1] == (byte) 0xff && bytes[4] == (byte) 0x04 && bytes[8] == (byte) 0x03) {
            Log.e("TAG", "上位机读取设备当前状态 " + CommonUtils.decodeBytesToHexString(bytes));
        }
    }

    private void reSendDate(byte[] bytes) {
        /*
         ffaa001a82b4f400000000f4004544000045446d3e0100000300000000f9
         ffaa001a8201f00000000000004544000045444d3a01000003000000002a
         */
        if( filelen[0] == bytes[15] || filelen[1] == bytes[16]|| filelen[2] == bytes[17]|| filelen[3] == bytes[18] || fileCrc[0] == bytes[19]|| fileCrc[1] == bytes[20]){
            int len = mingrifuture.gizlib.code.util.CommonUtils.getIntFromFourByte(bytes[11],bytes[12],bytes[13],bytes[14]);
            LogUtils.d("len is " + len);
            if( len < buffer.length ){
                cur_off = len;
                handler.sendEmptyMessageDelayed(0,50);
            }
        }
    }

    private void checkUpdateSuccess(byte[] bytes) {
        if (bytes[8] == bytes[12] && bytes[9] == bytes[13] && bytes[10] == bytes[14] && bytes[11] == bytes[15]) {
            textView = new TextView(this);
            textView.setText("升级成功");
            container.addView(textView);
        } else {
            textView = new TextView(this);
            textView.setText("升级失败");
            container.addView(textView);
        }
    }

    private final String path = Environment.getExternalStorageDirectory().getPath() + "/update/";

    public void download(View v) {
        if (sDownloadUrl == null) {
            Toast.makeText(this, "download url is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    wCRC = 0xffff;
                    wCPoly = 0x1021;
                    downLoadFromUrl(sDownloadUrl, "update_" + sSoftWare + ".bin", path, new HttpDownloader.downloadCallback() {
                        @Override
                        public void percent(final int percent) {
                            LogUtils.d("download " + percent);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    textView = new TextView(TestUpdataActivity.this);
                                    textView.setText("完成 " + percent);
                                    container.addView(textView);
                                }
                            });
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    sv.fullScroll(ScrollView.FOCUS_DOWN);
                                }
                            }, 80);
                            if (percent == 100) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            textView = new TextView(TestUpdataActivity.this);
                                            byte date[] = readFileSdcardFile();
                                            textView.setText("文件长度是" + date.length);
                                            filelen = parseFileLen(Integer.toHexString(date.length));
                                            container.addView(textView);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        WCRC = parseCRC(buffer);
                                        cur_off = 0;
                                    }
                                });
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private String sCheckResult = null;
    private String sDownloadUrl = null;
    private String sSoftWare = null;

    public void checkota(View v) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!NetUtils.isWifiConnected(TestUpdataActivity.this)) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            textView = new TextView(TestUpdataActivity.this);
                            textView.setText("设备未连接网络");
                            container.addView(textView);
                            btn_down.setEnabled(false);
                            LogUtils.d("check ota result :wifi not connect");
                        }
                    });

                    return;
                }
                sCheckResult = NetUtils.checkOta(TestUpdataActivity.this);
                LogUtils.d("check ota result " + sCheckResult);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (sCheckResult.contains("download_url")) {
                            textView = new TextView(TestUpdataActivity.this);
                            sSoftWare = sCheckResult.split("&")[0].substring(9);
                            sDownloadUrl = sCheckResult.split("&")[1].substring(13);
                            textView.setText("检测到新版本  " + sSoftWare + "  下载地址：" + sDownloadUrl);
                            container.addView(textView);
                            btn_down.setEnabled(true);
                        } else {
                            textView = new TextView(TestUpdataActivity.this);
                            textView.setText("无新版本更新");
                            container.addView(textView);
                            btn_down.setEnabled(false);
                        }
                    }
                });
            }
        }).start();
    }

    public void click(View v) {
        sendCommandInfo();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                LogUtils.d("send update date");
                info(new View(TestUpdataActivity.this));
            }
        }, 2000);
    }

    public void jump(View v) {
        jumphAppCommand();
    }

    /**
     * 当收到回复后，应当等待600ms再发数据；
     * 若收到的是boot的非法消息通知，则表示已进入boot，不必继续重发
     *
     * @param v
     */
    public void update(View v) {
        bExit = false;
        int[] info = new int[]{0xff, 0xff, 0x00, 0x05, 0x85, Sn, 0, 0, 0};
        info[info.length - 1] = CommonUtils.checkSum(info);
        Sn = (++Sn) % 256;
        EventBus.getDefault().post(new SendUpdateDataToMachine(info));
    }

    public void info(View v) {
        loop = true;
        bExit = false;

        sendFileInfo();//        handler.sendEmptyMessage(0);
    }

    private void sendFileInfo() {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if( buffer == null ){
//            Toast.makeText(this, "请先下载固件", Toast.LENGTH_SHORT).show();
            return;
        }
        LogUtils.d("send file info " + cur_off + "  + buffer len " + buffer.length);
        if( cur_off >= buffer.length ){
            return;
        }
        int yu = buffer.length % cur_length;
        if (cur_off + yu == buffer.length) {
            if (yu != 0) {
                sendCommandContainer(buffer.length - yu, yu);
                cur_off = cur_off + yu;
            }
            Log.e("TAG", " 发送文件结束");
            return;
        }
        sendCommandContainer(cur_off, cur_length);
        cur_off = cur_off + cur_length;
    }

    public void check(View v) {
        sendBufInfo();
    }

    public void EXIT(View v) {
        bExit = true;
        MachineStatusForMrFrture.bUpdating = false;
        jumphAppCommand();
        handler.removeCallbacksAndMessages(null);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                EventBus.getDefault().post(new CheckOtaEvent());
                EventBus.getDefault().post(new SendInitDateEvent());
                finish();
            }
        },1000);
    }

    public void MCU(View v) {
        int[] info = new int[]{0xff, 0xff, 0x00, 0x05, 0x0f, Sn, 0, 0, 0};
        info[info.length - 1] = CommonUtils.checkSum(info);
        Sn = (++Sn) % 256;
        EventBus.getDefault().post(new SendUpdateDataToMachine(info));
    }

    public void curStatus(View v) {
        int[] info = new int[]{0xff, 0xff, 0x00, 0x06, 0x03, Sn, 0, 0, 0x02, 0};
        info[info.length - 1] = CommonUtils.checkSum(info);
        Sn = (++Sn) % 256;
        EventBus.getDefault().post(new SendUpdateDataToMachine(info));
    }

    /**
     * 获取 CRC16 校验
     */
    public int parseCRC(byte[] bytes) {
        int len = bytes.length;
        int off = 0;
        while (len > 0) {
            int wChar = bytes[off];
            wCRC ^= (wChar << 8);
            for (int i = 0; i < 8; i++) {
                if ((wCRC & 0x8000) == 0x8000) {
                    wCRC = ((wCRC << 1) ^ wCPoly);
                } else {
                    wCRC = (wCRC << 1);
                }
            }
            len--;
            off++;
        }
        if (wCRC < 0) wCRC = 65536 + wCRC;
        parseFileCrc(wCRC);
        return wCRC;
    }

    /**
     * 读取文件
     *
     * @return
     * @throws IOException
     */
    public byte[] readFileSdcardFile() throws IOException {
        String fileName = path + "update_" + sSoftWare + ".bin";
//        String fileName = path + "update_00000000"  + ".bin";
        LogUtils.d("read file " + fileName);
        try {
            FileInputStream fin = new FileInputStream(fileName);

            int length = fin.available();
            LogUtils.d("read file len " + length);
            buffer = new byte[length];
            LogUtils.d("read file len1 " + buffer.length);
            fin.read(buffer);
            fin.close();
        } catch (Exception e) {
            LogUtils.d("read file error" );
            e.printStackTrace();
        }
        return buffer;
    }

    /**
     * 写入文件信息
     */
    private void sendCommandInfo() {
        if (filelen == null) {
//            Toast.makeText(this, "请先下载固件", Toast.LENGTH_SHORT).show();
            return;
        }
        sendCommand(21, 0x81, new int[]{0xff, 0xff, 0xff, 0xff, filelen[0], filelen[1], filelen[2], filelen[3], fileCrc[0], fileCrc[1], hardWare[0], hardWare[1], softWare[0], softWare[1], 0, 0, 0, 0, 0});
    }

    /**
     * @param off    写文件的起始位置
     * @param length 写入长度
     */
    private void sendCommandContainer(final int off, int length) {
        Log.e("TAG", "当前 位置" + off + "   长度 " + length);
        handler.post(new Runnable() {
            @Override
            public void run() {
                textView = new TextView(TestUpdataActivity.this);
                textView.setText("升级完成" + ((off *100/ buffer.length) + 1) + "%");
                container.addView(textView);
                btn_down.setEnabled(false);
            }
        });
        if( ((off *100/ buffer.length) + 1) == 100 ){
            handler.post(new Runnable() {
                @Override
                public void run() {
                    textView = new TextView(TestUpdataActivity.this);
                    textView.setText("请稍等片刻，听见开机音乐声表示升级成功，您可以退出升级模式进入app运行模式了");
                    container.addView(textView);
                    btn_down.setEnabled(false);
                    bExit =true;
                }
            });
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sv.fullScroll(ScrollView.FOCUS_DOWN);
            }
        }, 80);

        //length=14 长度为21    length=50 长度为57
        int[] lens = new int[length + 1];
        for (int i = 0; i < length; i++) {
            lens[i] = buffer[off + i];
        }
        lens[length] = 0;
        int[] offs = parseFileLen(Integer.toHexString(off));   //要写入的数据在文件中的起始位置;
        offs = CommonUtils.uniteInts(offs, lens);
        sendCommand(7 + length, 0x81, offs);
    }

    /**
     * @param len     长度1
     * @param cmd     命令码
     * @param payload 内容
     */
    public void sendCommand(int len, int cmd, int[] payload) {
        int[] command = new int[]{0xff, 0xaa, 0x00, len, cmd, Sn};
        command = CommonUtils.uniteInts(command, payload);
        command[command.length - 1] = CommonUtils.checkSum(command);
        Sn = (++Sn) % 256;
        EventBus.getDefault().post(new SendUpdateDataToMachine(command));
    }

    /**
     * 上位机查询命令
     */
    public void sendBufInfo() {
        int[] info = new int[]{0xff, 0xaa, 0x00, 0x03, 0x83, Sn, 0};
        info[info.length - 1] = CommonUtils.checkSum(info);
        Sn = (++Sn) % 256;
        EventBus.getDefault().post(new SendUpdateDataToMachine(info));
    }

    /**
     * 跳转到App命令，结束升级.
     */
    public void jumphAppCommand() {
        cur_off = 0;
        int[] info = new int[]{0xff, 0xaa, 0x00, 0x03, 0x85, Sn, 0};
        info[info.length - 1] = CommonUtils.checkSum(info);
        Sn = (++Sn) % 256;
        EventBus.getDefault().post(new SendUpdateDataToMachine(info));
    }

    private int[] parseFileLen(String hex) {
        int length = hex.length();
        if ((8 - length) > 0) {
            for (int i = 0; i < (8 - length); i++) {
                hex = 0 + hex;
            }
        }
        Log.e("TAG", "   fileLen  " + hex);
        int[] lens = new int[]{Integer.parseInt(hex.substring(0, 2), 16), Integer.parseInt(hex.substring(2, 4), 16), Integer.parseInt(hex.substring(4, 6), 16), Integer.parseInt(hex.substring(6, 8), 16)};
        return lens;
    }

    private void parseFileCrc(int crc) {
        String mcrc = Integer.toHexString(crc);
        if (mcrc.length() < 4) {
            for (int i = 0; i < 4 - mcrc.length(); i++) {
                mcrc = 0 + mcrc;
            }
        } else if (mcrc.length() > 4) {
            mcrc = mcrc.substring(mcrc.length() - 4, mcrc.length());
        }
        Log.e("TAG", "  FileCrc " + mcrc);
        fileCrc = new int[]{Integer.parseInt(mcrc.substring(0, 2), 16), Integer.parseInt(mcrc.substring(2, 4), 16)};

    }

    private void parseErrorCode(byte error) {
        textView = new TextView(this);
        switch (error) {
            case (byte) 0x00:
                Log.e("TAG", "ERRCODE_NULL");
                textView.setText("ERRCODE_NULL");
                break;
            case (byte) 0xff:
                Log.e("TAG", "ERRCODE_PACKET");
                textView.setText("ERRCODE_PACKET");
                break;
            case (byte) 0xfd:
                Log.e("TAG", "ERRCODE_FileNoComplete");
                textView.setText("ERRCODE_FileNoComplete");
                break;
            case (byte) 0xfc:
                Log.e("TAG", "ERRCODE_FileNoWrite");
                textView.setText("ERRCODE_FileNoWrite");
                break;
            case (byte) 0xfb:
                Log.e("TAG", "ERRCODE_FileNoWrite");
                textView.setText("ERRCODE_FileNoWrite");
                break;
            case (byte) 0xfa:
                Log.e("TAG", "ERRCODE_FileNoExe");
                textView.setText("ERRCODE_FileNoExe");
                break;
            case (byte) 0xf9:
                Log.e("TAG", "ERRCODE_FileWriteOutRange");
                textView.setText("ERRCODE_NULL");
                break;
            case (byte) 0xf8:
                Log.e("TAG", "ERRCODE_FileWriteOutLen");
                textView.setText("ERRCODE_FileWriteOutLen");
                break;
            case (byte) 0xf7:
                Log.e("TAG", "ERRCODE_FileWriteOutOff");
                textView.setText("ERRCODE_FileWriteOutOff");
                break;
            case (byte) 0xf6:
                Log.e("TAG", "ERRCODE_FileWriteInOff");
                textView.setText("ERRCODE_FileWriteInOff");
                break;
            case (byte) 0xf5:
                Log.e("TAG", "ERRCODE_FileBroken");
                textView.setText("ERRCODE_FileBroken");
                break;
            case (byte) 0xf4:
                Log.e("TAG", "ERRCODE_FileCRC16");
                textView.setText("ERRCODE_FileCRC16");
                break;
            case (byte) 0xf3:
                Log.e("TAG", "ERRCODE_FlashEraseErr");
                textView.setText("ERRCODE_FlashEraseErr");
                break;
            case (byte) 0xf2:
                Log.e("TAG", "ERRCODE_FlashEraseErr");
                textView.setText("ERRCODE_FlashEraseErr");
                break;
            case (byte) 0xf1:
                Log.e("TAG", "ERRCODE_CoveredByApp");
                textView.setText("ERRCODE_CoveredByApp");
                break;
            case (byte) 0xf0:
                Log.e("TAG", "ERRCODE_hardwarev");
                textView.setText("ERRCODE_hardwarev");
                break;
            case (byte) 0xec:
                Log.e("TAG", "ERRCODE_CmdNoExist");
                textView.setText("ERRCODE_CmdNoExist");
                break;
        }
        textView.append(" 请重新升级 ");
        container.addView(textView);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sv.fullScroll(ScrollView.FOCUS_DOWN);
            }
        }, 80);
    }


    /**
     * 从网络Url中下载文件
     *
     * @param urlStr
     * @param fileName
     * @param savePath
     * @throws IOException
     */
    public void downLoadFromUrl(String urlStr, String fileName, String savePath, HttpDownloader.downloadCallback dc) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //设置超时间为3秒
        conn.setConnectTimeout(5 * 1000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

        int len = conn.getContentLength();
        //得到输入流
        InputStream inputStream = conn.getInputStream();
        //获取自己数组
        byte[] getData = readInputStream(inputStream, dc, len);

        //文件保存位置
        File saveDir = new File(savePath);
        if (!saveDir.exists()) {
            saveDir.mkdir();
        }
        File file = new File(saveDir + File.separator + fileName);
        if (file.exists()) {
            file.delete();
        }
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(getData);
        if (fos != null) {
            fos.close();
        }
        if (inputStream != null) {
            inputStream.close();
        }
        System.out.println("info:" + url + " download success");
    }

    /**
     * 从输入流中获取字节数组
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static byte[] readInputStream(InputStream inputStream, HttpDownloader.downloadCallback dc, int fileLen) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        int sum = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
            sum += len;
            dc.percent((sum * 100) / fileLen);
        }
        bos.close();
        return bos.toByteArray();
    }
}

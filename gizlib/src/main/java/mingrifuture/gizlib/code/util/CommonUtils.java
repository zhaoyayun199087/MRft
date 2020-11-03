package mingrifuture.gizlib.code.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import mingrifuture.gizlib.code.remote.WLTServerSocket;


/**
 * Created by pengl on 2016/1/13.
 */
public class CommonUtils {
    /**
     * 得到设备id
     * @return
     * @throws Exception
     */
    public static String readDid() throws Exception {
        // 读文件
        Properties properties = new Properties();
        try {
            File file = new File(Environment.getExternalStorageDirectory(), WLTServerSocket.mingrifuture_CONFIG);
            if (!file.exists()) {
                file.createNewFile();
                return null;
            } else {
                FileInputStream s = new FileInputStream(file);
                properties.load(s);
            }
        } catch (Exception e) {
            throw e;
        }

        if ((String) properties.get("did") != null) {
            return ((String) properties.get("did"));
        }

        return null;
    }

    /**
     * 获取固件版本号
     */
    public static String  getBinVersion(Context context){
        int iV = (int) SPUtils.get(context,"soft_version", 0);
        String sV = "0000";
        int iV1 = (byte) (iV & 0xff);
        int iV2 = (byte) ((iV >> 8) & 0xff);
        String sV1 = String.format("%02d", iV1);
        String sV2 = String.format("%02d", iV2);
        sV = sV + sV2 + sV1;
        return sV;
    }

    /**
     * 获取固件版本号
     */
    public static String  getBinHardVersion(Context context){
        int iV = (int) SPUtils.get(context,"hard_version", 0);
        String sV = "0000";
        int iV1 = (byte) (iV & 0xff);
        int iV2 = (byte) ((iV >> 8) & 0xff);
        String sV1 = String.format("%02d", iV1);
        String sV2 = String.format("%02d", iV2);
        sV = sV + sV2 + sV1;
        return sV;
    }

    /**
     * 二位数的格式化，若只有一位，前面添个0
     *
     * @param value
     * @return
     */
    public static String formatOct(int value) {
        if (value < 10) {
            return "0" + value;
        }
        return Integer.toString(value);
    }

    /**
     * 将两个16以内的数字合并为高四位低四位
     *
     * @param src0
     * @param src1
     * @return
     */
    public static byte uniteNumber(byte src0, byte src1) {
        byte _b0 = src0;
        _b0 = (byte) (_b0 << 4);
        byte _b1 = src1;
        byte ret = (byte) (_b0 ^ _b1);
        return ret;
    }

    /**
     * 合并多个字节数组
     *
     * @param data
     * @return
     */
    public static byte[] uniteBytes(byte[]... data) {
        int length = 0;
        for (byte[] msg : data) {
            length += msg.length;
        }
        byte[] newData = new byte[length];
        int i = 0;
        for (byte[] msg : data) {
            System.arraycopy(msg, 0, newData, i, msg.length);
            i += msg.length;
        }

        return newData;
    }

    /**
     * 解析十六进制字符至16进制字符串
     */
    public static String decodeByteToHexString(byte src) {
        byte[] des = new byte[2];
        des[1] = (byte) (src & 0x0f);
        des[0] = (byte) ((src & 0xf0) >> 4);
        return Integer.toHexString(des[0]) + Integer.toHexString(des[1]);
    }

    public static String decodeBytesToHexString(byte[] data) {
        String result = new String();
        for (byte dd : data) {
            result = result.concat(decodeByteToHexString(dd));
        }
        return result;
    }

    /**
     * 解析十六进制字符
     *
     * @param src
     * @return
     */
    public static byte[] decodeByte(byte src) {
        byte[] des = new byte[2];
        des[0] = (byte) (src & 0x0f);
        des[1] = (byte) ((src & 0xf0) >> 4);
        return des;
    }

    public static byte[] chatOrders(String c) {
        byte[] m = c.getBytes();
        if (m.length % 2 == 0) {
            byte[] bytes = new byte[m.length / 2];
            for (int i = 0, j = 0; i < m.length; i += 2, j++) {
                bytes[j] = uniteByte(m[i], m[i + 1]);
            }
            return bytes;
        }
        return null;
    }

    // 实现两个十六进制字符合并为一个8位大小字节
    public static byte uniteByte(byte src0, byte src1) {
        byte _b0 = Byte.decode("0x" + new String(new byte[]{src0}))
                .byteValue();
        _b0 = (byte) (_b0 << 4);
        byte _b1 = Byte.decode("0x" + new String(new byte[]{src1}))
                .byteValue();
        byte ret = (byte) (_b0 ^ _b1);
        return ret;
    }

    public static byte reverseByte(byte b)
    {
        byte tmp ,ret = 0;
        for(int i = 0; i < 8; i++){
            tmp =(byte) (( b >> i) & (byte)0x01);
            ret = (byte) (ret | (tmp << (7 - i)));
        }
        return ret;
    }

    public static void releaseImageResource(ImageView circleImageView) {
        if (circleImageView != null && circleImageView.getDrawable() != null) {
            Bitmap oldBitmap = ((BitmapDrawable) circleImageView.getDrawable())
                    .getBitmap();
            circleImageView.setImageDrawable(null);
            if (oldBitmap != null) {
                oldBitmap.recycle();
                oldBitmap = null;
            }
        }
    }




    private byte reverse8(byte c )
    {
        c = (byte) (( c & (byte)0x55 ) << 1 | ( c & (byte)0xAA ) >> 1);
        c = (byte) (( c & (byte)0x33 ) << 2 | ( c & (byte)0xCC ) >> 2);
        c = (byte) (( c & (byte)0x0F ) << 4 | ( c & (byte)0xF0 ) >> 4);
        return c;
    }

    /**
     * 将两字节转换成int
     *
     * @param a 高位
     * @param b 低位
     * @return
     */
    public static int getIntFromTwoByte(byte a, byte b) {

        return (int) ((a << 8 | (b & 0xff)) & 0xffff);
    }

    /**
     * 将四字节转换成int
     *
     * @param a 高位
     * @param b 低位
     * @return
     */
    public static int getIntFromFourByte(byte a, byte b,byte c,byte d) {
        int b0 = a & 0xFF;
        int b1 = b & 0xFF;
        int b2 = c & 0xFF;
        int b3 = d & 0xFF;
        return (b0 << 24) | (b1 << 16) | (b2 << 8) | b3;
    }

    /**
     * 将八字节转换成long
     *
     * @param a 高位
     * @param b 低位
     * @return
     */
    public static long getLongFromEightByte(byte a, byte b,byte c,byte d, byte e, byte f,byte g,byte h) {
        return (long)((long)a << 56) |((long)b << 48) |((long)c << 40) |((long)d << 32) |((long)e << 24) | ((long)f << 16) | ((long)g << 8) | (long)h;
    }

    public static  int lastOpentime;
    public static  int lastCloseTime;

    /**
     * 命令序列号
     */
    private static int mSn;
    /**
     * 命令编号
     */
    private static int mOrderNo;

    /**
     * 命令内容对象
     */
    private static int mOrderObject;

    public static int getOrderNo() {
        return mOrderNo;
    }

    public static int getOrderObject() {
        return mOrderObject;
    }

    public static void setOrder(int orderNo, int orderObject) {
        mOrderNo = orderNo;
        mOrderObject = orderObject;
    }
    public static int getSn() {
        return mSn;
    }

    public static void SetSn(int sn) {
        mSn = sn;
    }

}

package mingrifuture.gizlib.code.remote;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class WLTUtilsEx {
    public static String DID = "xh5yUBzvGWbGt4JHhwueDT";
    public static String product_key = "02ebbd8a73de4a58b9679abe63372ef9";
    public static final String PASS_CODE = "gokit1";
    public static byte[] product_keyByte;
    public static String DEVICEID;
    public static final String CONFIGPREFERENCE = "config";
    public static final String CONFIG_MODE = "mode";
    public static final String CONFIG_CONFIGDATA = "configdata";
    public static final String CONFIG_SENSOR = "sensor";
    public static final String CONFIG_WEATHER = "weather";
    public static final String CONFIG_TIME = "gettime";
    public static final String CONFIG_OPRATION = "opration";
    public static final String CONFIG_PERMISSION = "permission";
    public static final String CONFIG_SYSTEMTIME = "systemtime";
    public static final String CONFIG_LOCATION = "location";
    public static final int NETWORK_PHONE = 1;
    public static final int NO_NETWORK = 0;
    public static final int NETWORK_WIFI = 2;
    public static final String MAC = "ACCF23538B6A";
    public static byte[] ORDER1;
    public static byte[] ORDER2;
    public static byte[] ORDER3;
    public static byte[] ORDER4;
    public static byte[] ORDER5;
    public static byte[] ORDER6;
    public static byte[] ORDER7;
    public static byte[] ORDER8;
    public static byte[] ORDER9;
    public static byte[] ORDER10;
    public static byte[] ORDER11;
    public static byte[] ORDER12;
    public static byte[] ORDER13;
    public static byte[] BIND_ORDER1;
    public static byte[] BIND_ORDER2;
    public static byte[] BIND_ORDER3;
    public static byte[] BIND_ORDER4;
    public static byte[] BIND_ORDER5;
    public static byte[] BIND_ORDER6;
    public static byte[] LOGIN_ORDER1;
    public static byte[] LOGIN_ORDER2;
    public static byte[] LOGIN_ORDER3;
    public static byte[] LOGIN_ORDER4;
    public static byte[] LOGIN_ORDER5_SUCCESS;
    public static byte[] LOGIN_ORDER5_FAILED;
    public static byte[] HEARTBEAT_ORDER1;
    public static byte[] HEARTBEAT_ORDER2;
    public static byte[] HEARTBEAT_ORDER3;
    public static byte[] HEARTBEAT_ORDER4;
    public static byte[] READINFO_ORDER1;
    public static byte[] READINFO_ORDER2;
    public static byte[] READINFO_ORDER3;
    public static byte[] READINFO_ORDER4;
    public static byte[] READINFO_ORDER5;
    public static byte[] READINFO_ORDER6;
    public static byte[] READINFO_ORDER7;
    public static byte[] READINFO_ORDER8;
    public static byte[] READINFO_ORDER9;
    public static byte[] READINFO_ORDER10;
    public static byte[] READINFO_ORDER11;
    public static byte[] READINFO_ORDER12;
    public static byte[] READINFO_ORDER13;
    public static boolean isDebug;
    public static final int UPDATE_TYPE_AUTO = 1;
    public static final int UPDATE_TYPE_FORCE = 2;
    public static final int UPDATE_TYPE_SLIENT = 3;

    public WLTUtilsEx() {
    }

    public static byte[] chatOrders(String c) {
        byte[] m = c.getBytes();
        if(m.length % 2 != 0) {
            return null;
        } else {
            byte[] bytes = new byte[m.length / 2];
            int i = 0;

            for(int j = 0; i < m.length; ++j) {
                bytes[j] = uniteByte(m[i], m[i + 1]);
                i += 2;
            }

            return bytes;
        }
    }

    public static byte uniteByte(byte src0, byte src1) {
        byte _b0 = Byte.decode("0x" + new String(new byte[]{src0})).byteValue();
        _b0 = (byte)(_b0 << 4);
        byte _b1 = Byte.decode("0x" + new String(new byte[]{src1})).byteValue();
        byte ret = (byte)(_b0 ^ _b1);
        return ret;
    }

    public static byte[] uniteBytes(byte[]... data) {
        int length = 0;
        byte[][] newData = data;
        int i = data.length;

        for(int arr$ = 0; arr$ < i; ++arr$) {
            byte[] len$ = newData[arr$];
            length += len$.length;
        }

        byte[] var8 = new byte[length];
        i = 0;
        byte[][] var9 = data;
        int var10 = data.length;

        for(int i$ = 0; i$ < var10; ++i$) {
            byte[] msg = var9[i$];
            System.arraycopy(msg, 0, var8, i, msg.length);
            i += msg.length;
        }

        return var8;
    }

    public static byte[] intToByte(int number) {
        byte[] abyte = new byte[]{(byte)((-16777216 & number) >> 24), (byte)((16711680 & number) >> 16), (byte)(('\uff00' & number) >> 8), (byte)(255 & number)};
        return abyte;
    }

    public static int byteArrayToInt(byte[] bytes) {
        int value = 0;

        for(int i = 0; i < 4; ++i) {
            int shift = (3 - i) * 8;
            value += (bytes[i] & 255) << shift;
        }

        return value;
    }

    public static String formatOct(int value) {
        return value < 10?"0" + value: Integer.toString(value);
    }

    public static byte[] decodeByte(byte src) {
        byte[] des = new byte[]{(byte)(src & 15), (byte)((src & 240) >> 4)};
        return des;
    }

    public static String decodeByteToHexString(byte src) {
        byte[] des = new byte[]{(byte)((src & 240) >> 4), (byte)(src & 15)};
        return Integer.toHexString(des[0]) + Integer.toHexString(des[1]);
    }

    public static String decodeBytesToHexString(byte[] data) {
        String result = new String();
        byte[] arr$ = data;
        int len$ = data.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            byte dd = arr$[i$];
            result = result.concat(decodeByteToHexString(dd));
        }

        return result;
    }

    public static String getbyteMsg(byte[] data) {
        String msg = new String();
        byte[] arr$ = data;
        int len$ = data.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            byte by = arr$[i$];
            byte[] db = decodeByte(by);
            msg.concat(getMsg(db[0], db[1]));
        }

        return msg;
    }

    public static String getMsg(byte one, byte two) {
        byte[] data = new byte[]{one, two};
        String msg = new String(data, 0, data.length);
        return msg;
    }

    public static byte uniteNumber(byte src0, byte src1) {
        byte _b0 = (byte)(src0 << 4);
        byte ret = (byte)(_b0 ^ src1);
        return ret;
    }

    public static String getLocalIpAddress() throws SocketException {
        Enumeration en = NetworkInterface.getNetworkInterfaces();

        while(en.hasMoreElements()) {
            NetworkInterface intf = (NetworkInterface)en.nextElement();
            Enumeration enumIpAddr = intf.getInetAddresses();

            while(enumIpAddr.hasMoreElements()) {
                InetAddress inetAddress = (InetAddress)enumIpAddr.nextElement();
                if(!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                    return inetAddress.getHostAddress().toString();
                }
            }
        }

        return null;
    }

    public static String byteToBit(byte b) {
        return "" + (byte)(b >> 7 & 1) + (byte)(b >> 6 & 1) + (byte)(b >> 5 & 1) + (byte)(b >> 4 & 1) + (byte)(b >> 3 & 1) + (byte)(b >> 2 & 1) + (byte)(b >> 1 & 1) + (byte)(b >> 0 & 1);
    }

    public static String intoIp(int i) {
        return (i & 255) + "." + (i >> 8 & 255) + "." + (i >> 16 & 255) + "." + (i >> 24 & 255);
    }

    public static byte BitToByte(String byteStr) {
        if(null == byteStr) {
            return (byte)0;
        } else {
            int len = byteStr.length();
            if(len != 4 && len != 8) {
                return (byte)0;
            } else {
                int re;
                if(len == 8) {
                    if(byteStr.charAt(0) == 48) {
                        re = Integer.parseInt(byteStr, 2);
                    } else {
                        re = Integer.parseInt(byteStr, 2) - 256;
                    }
                } else {
                    re = Integer.parseInt(byteStr, 2);
                }

                return (byte)re;
            }
        }
    }

    public static void printBytes(byte[] bytes) {
        if(null != bytes && bytes.length > 0) {
            for(int i = 0; i < bytes.length; ++i) {
                if(i == bytes.length - 1) {
                    System.out.print(decodeByteToHexString(bytes[i]));
                } else {
                    System.out.print(decodeByteToHexString(bytes[i]) + "-");
                }
            }

            System.out.println("");
        }

    }

    static {
        product_keyByte = product_key.getBytes();
        DEVICEID = "deviceID";
        ORDER1 = new byte[]{(byte)0, (byte)0, (byte)0, (byte)3};
        ORDER2 = new byte[]{intToByte(intToByte(11)[3] + intToByte(DID.getBytes().length)[3] + intToByte("ACCF23538B6A".getBytes().length)[3] + intToByte(product_key.getBytes().length)[3] + intToByte(product_key.getBytes().length)[3])[3]};
        ORDER3 = new byte[]{(byte)0};
        ORDER4 = new byte[]{(byte)0, (byte)4};
        ORDER5 = new byte[]{(byte)0, intToByte(DID.getBytes().length)[3]};
        ORDER6 = DID.getBytes();
        ORDER7 = new byte[]{(byte)0, intToByte("ACCF23538B6A".getBytes().length)[3]};
        ORDER8 = "ACCF23538B6A".getBytes();
        ORDER9 = new byte[]{(byte)0, intToByte(product_key.getBytes().length)[3]};
        ORDER10 = product_key.getBytes();
        ORDER11 = new byte[]{(byte)0, intToByte(product_key.getBytes().length)[3]};
        ORDER12 = product_key.getBytes();
        ORDER13 = new byte[]{(byte)0, (byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)1,};
        BIND_ORDER1 = new byte[]{(byte)0, (byte)0, (byte)0, (byte)3};
        BIND_ORDER2 = new byte[]{(byte)0x0b};
        BIND_ORDER3 = new byte[]{(byte)0};
        BIND_ORDER4 = new byte[]{(byte)0, (byte)7};
        BIND_ORDER5 = new byte[]{(byte)0, intToByte("gokit1".getBytes().length)[3]};
        BIND_ORDER6 = "gokit1".getBytes();
        LOGIN_ORDER1 = new byte[]{(byte)0, (byte)0, (byte)0, (byte)3};
        LOGIN_ORDER2 = new byte[]{(byte)4};
        LOGIN_ORDER3 = new byte[]{(byte)0};
        LOGIN_ORDER4 = new byte[]{(byte)0, (byte)9};
        LOGIN_ORDER5_SUCCESS = new byte[]{(byte)0};
        LOGIN_ORDER5_FAILED = new byte[]{(byte)1};
        HEARTBEAT_ORDER1 = new byte[]{(byte)0, (byte)0, (byte)0, (byte)3};
        HEARTBEAT_ORDER2 = new byte[]{(byte)3};
        HEARTBEAT_ORDER3 = new byte[]{(byte)0};
        HEARTBEAT_ORDER4 = new byte[]{(byte)0, (byte)0x16};
        READINFO_ORDER1 = new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 3};
        READINFO_ORDER2 = new byte[]{(byte) 0x57};
        READINFO_ORDER3 = new byte[]{(byte) 0};
        READINFO_ORDER4 = new byte[]{(byte) 0, (byte) 0x14};
        READINFO_ORDER5 = "00000000".getBytes();
        READINFO_ORDER6 = "00000000".getBytes();
        READINFO_ORDER7 = "00000000".getBytes();
        READINFO_ORDER8 = "00000000".getBytes();
        READINFO_ORDER9 = "00000000".getBytes();
        READINFO_ORDER10 = "00000000".getBytes();
        READINFO_ORDER11 = new byte[]{(byte) 0, (byte) 0x0};
        READINFO_ORDER12 = new byte[]{(byte) 0, intToByte(product_key.getBytes().length)[3]};
        READINFO_ORDER13 = product_key.getBytes();
        isDebug = true;
    }
}

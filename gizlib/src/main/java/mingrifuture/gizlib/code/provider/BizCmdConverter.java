package mingrifuture.gizlib.code.provider;

import android.os.Handler;
import android.os.Message;

import mingrifuture.gizlib.code.config.Constants;
import mingrifuture.gizlib.code.remote.RemoteControlService;
import mingrifuture.gizlib.code.util.CommonUtils;
import mingrifuture.gizlib.code.util.LogUtils;

/**
 * 数据包通信转换类
 *
 * @author andyz
 */
public class BizCmdConverter {
    private static byte SET_CMD = 0X01;
    private static byte SEARCH_CMD = 0X02;


    /**
     * 组包发送给机智云服务器
     * @return
     */
    public static byte[] deviceMsgConvertEx() {
        // 查询返回/上报 0003 xx 00 00 XX
        // 头 长度 flag 命令字
        try {
            byte date[] = new byte[77];
            StringBuilder sb = new StringBuilder();
            sb.append("000000034800009103");
            System.arraycopy(CommonUtils.chatOrders(sb.toString()), 0, date, 0, 9);
            byte tmp = 0;
            if (MachineStatus.sRes_bit1) {
                tmp = 0x01;
            }
            date[9] = tmp;

            tmp = 0;
            if (MachineStatus.sSys_sw) {
                tmp = (byte) (tmp | (1 << 0));
            }
            if (MachineStatus.sLock_sw) {
                tmp = (byte) (tmp | (1 << 1));
            }
            if (MachineStatus.sHeat_sw) {
                tmp = (byte) (tmp | (1 << 2));
            }
            if (MachineStatus.sWash_sw) {
                tmp = (byte) (tmp | (1 << 3));
            }
            if (MachineStatus.sTemp_sw) {
                tmp = (byte) (tmp | (1 << 4));
            }
            if (MachineStatus.sFloat_sw) {
                tmp = (byte) (tmp | (1 << 5));
            }
            if (MachineStatus.sChirld_lock_sw) {
                tmp = (byte) (tmp | (1 << 6));
            }
            if (MachineStatus.sCold_sw) {
                tmp = (byte) (tmp | (1 << 7));
            }
            date[10] = tmp;
            date[11] = (byte) MachineStatus.sVolume;
            date[12] = (byte) MachineStatus.sRes_byte1;
            date[13] = (byte) MachineStatus.sRes_byte2;
            date[14] = (byte) ((MachineStatus.sLeft_time >> 8) & 0xff);
            date[15] = (byte) (MachineStatus.sLeft_time & 0xff);
            date[16] = (byte) ((MachineStatus.sRes_int1 >> 8) & 0xff);
            date[17] = (byte) (MachineStatus.sRes_int1 & 0xff);
            date[18] = (byte) ((MachineStatus.sRes_int2 >> 8) & 0xff);
            date[19] = (byte) (MachineStatus.sRes_int2 & 0xff);
            date[60] = (byte) MachineStatus.sSoftver;
            date[61] = (byte) MachineStatus.sHardver;
            date[62] = (byte) MachineStatus.sFac_No;
            date[63] = (byte) MachineStatus.sDevtype;
            date[64] = (byte) MachineStatus.sSys_sta;
            date[65] = (byte) MachineStatus.sInfrared;
            date[66] = (byte) MachineStatus.sRes_byte3;
            date[67] = (byte) ((MachineStatus.sStrtime >> 8) & 0xff);
            date[68] = (byte) (MachineStatus.sStrtime & 0xff);
            date[69] = (byte) ((MachineStatus.sIn_tds >> 8) & 0xff);
            date[70] = (byte) (MachineStatus.sIn_tds & 0xff);
            date[71] = (byte) ((MachineStatus.sOut_tds >> 8) & 0xff);
            date[72] = (byte) (MachineStatus.sOut_tds & 0xff);
            date[73] = (byte) ((MachineStatus.sRet_left_time >> 8) & 0xff);
            date[74] = (byte) (MachineStatus.sRet_left_time & 0xff);
            date[75] = (byte) ((MachineStatus.sSRes_int3 >> 8) & 0xff);
            date[76] = (byte) (MachineStatus.sSRes_int3 & 0xff);
            return date;
        } catch (Exception e) {
            e.printStackTrace();
            return new byte[]{};
        }
    }

    /**
     * 解析机智云服务器发送过来的数据
     * @param msg
     * @param handler
     */
/*
    public static void receivedMsgConvert(byte[] msg, Handler handler) {
        LogUtils.d("receive msg from app " + CommonUtils.decodeBytesToHexString(msg));

        try {
            if (msg.length < 63) {
                return;
            }
            if (msg[8] == SET_CMD) {
                int date;
                AppCmd appCmd;
                int cmd = CommonUtils.getIntFromFourByte((byte) 0x00, msg[9], msg[10], msg[11]);
                switch (cmd) {
                    case Constants.APP_SEND_POWER:
                        date = (msg[13] & 0x01);
                        if (date == 1) {
                            MachineStatus.sSys_sw = true;
                        } else {
                            MachineStatus.sSys_sw = false;
                        }
                        appCmd = new AppCmd(cmd, MachineStatus.sSys_sw);
                        break;
                    case Constants.APP_SEND_LOCK:
                        date = ((msg[13] >> 1) & 0x01);
                        if (date == 1) {
                            MachineStatus.sLock_sw = true;
                        } else {
                            MachineStatus.sLock_sw = false;
                        }
                        appCmd = new AppCmd(cmd, MachineStatus.sLock_sw);

                        break;
                    case Constants.APP_SEND_HEAD:
                        date = ((msg[13] >> 2) & 0x01);
                        if (date == 1) {
                            MachineStatus.sHeat_sw = true;
                        } else {
                            MachineStatus.sHeat_sw = false;
                        }
                        appCmd = new AppCmd(cmd, MachineStatus.sHeat_sw);

                        break;
                    case Constants.APP_SEND_RUSH:
                        date = ((msg[13] >> 3) & 0x01);
                        if (date == 1) {
                            MachineStatus.sWash_sw = true;
                        } else {
                            MachineStatus.sWash_sw = false;
                        }
                        appCmd = new AppCmd(cmd, MachineStatus.sWash_sw);

                        break;
                    case Constants.APP_SEND_TMP:
                        date = ((msg[13] >> 4) & 0x01);
                        if (date == 1) {
                            MachineStatus.sTemp_sw = true;
                        } else {
                            MachineStatus.sTemp_sw = false;
                        }
                        appCmd = new AppCmd(cmd, MachineStatus.sTemp_sw);
                        break;
                    case Constants.APP_SEND_FLOAT:
                        date = ((msg[13] >> 5) & 0x01);
                        if (date == 1) {
                            MachineStatus.sFloat_sw = true;
                        } else {
                            MachineStatus.sFloat_sw = false;
                        }
                        appCmd = new AppCmd(cmd, MachineStatus.sFloat_sw);
                        break;
                    case Constants.APP_SEND_CHIRLD_LOCK:
                        date = ((msg[13] >> 6) & 0x01);
                        if (date == 1) {
                            MachineStatus.sChirld_lock_sw = true;
                        } else {
                            MachineStatus.sChirld_lock_sw = false;
                        }
                        appCmd = new AppCmd(cmd, MachineStatus.sChirld_lock_sw);
                        break;
                    case Constants.APP_SEND_COLD:
                        date = ((msg[13] >> 7) & 0x01);
                        if (date == 1) {
                            MachineStatus.sCold_sw = true;
                        } else {
                            MachineStatus.sCold_sw = false;
                        }
                        appCmd = new AppCmd(cmd, MachineStatus.sCold_sw);
                        break;
                    case Constants.APP_SEND_RES_BIT1:
                        date = ((msg[12]) & 0x01);
                        if (date == 1) {
                            MachineStatus.sRes_bit1 = true;
                        } else {
                            MachineStatus.sRes_bit1 = false;
                        }
                        appCmd = new AppCmd(cmd, MachineStatus.sRes_bit1);
                        break;
                    case Constants.APP_SEND_VOLUME:
                        date = msg[14];
                        MachineStatus.sVolume = msg[14];
                        appCmd = new AppCmd(cmd, CommonUtils.getIntFromTwoByte((byte)0x00,msg[14]));
                        break;
                    case Constants.APP_SEND_RES_BYTE1:
                        date = msg[15];
                        MachineStatus.sRes_byte1 = msg[15];
                        appCmd = new AppCmd(cmd, CommonUtils.getIntFromTwoByte((byte)0x00,msg[15]));
                        break;
                    case Constants.APP_SEND_RES_BYTE2:
                        date = msg[16];
                        MachineStatus.sRes_byte2 = msg[16];
                        appCmd = new AppCmd(cmd,CommonUtils.getIntFromTwoByte((byte)0x00,msg[16]));
                        break;
                    case Constants.APP_SEND_LEFT_TIME:
                        date = CommonUtils.getIntFromTwoByte(msg[17], msg[18]);
                        MachineStatus.sLeft_time = date;
                        appCmd = new AppCmd(cmd, MachineStatus.sLeft_time);
                        break;
                    case Constants.APP_SEND_RES_INT1:
                        date = CommonUtils.getIntFromTwoByte(msg[19], msg[20]);
                        MachineStatus.sRes_int1 = date;
                        appCmd = new AppCmd(cmd, MachineStatus.sRes_int1);
                        break;
                    case Constants.APP_SEND_RES_INT2:
                        date = CommonUtils.getIntFromTwoByte(msg[21], msg[22]);
                        MachineStatus.sRes_int2 = date;
                        appCmd = new AppCmd(cmd, MachineStatus.sRes_int2);
                        break;
                    case Constants.APP_SEND_JD:
                        byte[] str = new byte[20];
                        System.arraycopy(msg, 22, str, 0, 20);
                        MachineStatus.sJd = CommonUtils.decodeBytesToHexString(str);
                        appCmd = new AppCmd(cmd, MachineStatus.sJd);
                        break;
                    case Constants.APP_SEND_WD:
                        byte[] str1 = new byte[20];
                        System.arraycopy(msg, 42, str1, 0, 20);
                        MachineStatus.sWd = CommonUtils.decodeBytesToHexString(str1);
                        appCmd = new AppCmd(cmd, MachineStatus.sWd);
                        break;
                    default:
                        appCmd = new AppCmd(0, 0);
                        break;
                }
                Message message = handler.obtainMessage();
                message.obj = appCmd;
                message.what = RemoteControlService.TCP_RECEIVEDATA;
                handler.sendMessage(message);

            } else if (msg[8] == SEARCH_CMD) {
                Message message = handler.obtainMessage();
                message.obj = new AppCmd(Constants.APP_QUEARY,0);
                message.what = RemoteControlService.TCP_RECEIVEDATA;
                handler.sendMessage(message);
                return;
            } else {
                return;
            }
        } catch (Exception e) {
            return;
        }
    }
*/
}

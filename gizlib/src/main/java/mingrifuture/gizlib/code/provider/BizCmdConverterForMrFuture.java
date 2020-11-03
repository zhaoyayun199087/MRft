package mingrifuture.gizlib.code.provider;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import mingrifuture.gizlib.code.config.Constants;
import mingrifuture.gizlib.code.remote.RemoteControlService;
import mingrifuture.gizlib.code.util.CommonUtils;
import mingrifuture.gizlib.code.util.LogUtils;

/**
 * 数据包通信转换类
 *
 * @author andyz
 */
public class BizCmdConverterForMrFuture {
    private static byte SET_CMD = 0X01;
    private static byte SEARCH_CMD = 0X02;


    /**
     * 组包发送给机智云服务器
     *
     * @return
     */
    public static byte[] deviceMsgToGizConverter() {
        // 查询返回/上报 0003 xx 00 00 XX
        // 头 长度 flag 命令字
        try {
            byte date[] = new byte[59];
            for (int i = 0; i < date.length; i++) {
                date[i] = 0;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("000000033600009104");
            System.arraycopy(CommonUtils.chatOrders(sb.toString()), 0, date, 0, 9);
            byte temp = 0;
            date[9] = (byte) ((MachineStatusForMrFrture.Surge_tank) & 0x07);
            if (MachineStatusForMrFrture.Switch_Electrostatic) {
                temp = (byte) (temp | 0x01);
            }
            temp |= (byte) ((MachineStatusForMrFrture.Wind_Velocity << 1) & 0x1e);
            temp |= (byte) ((MachineStatusForMrFrture.Mode << 5) & 0xe0);
            date[10] = temp;
            temp = 0;
            if (MachineStatusForMrFrture.Switch) {
                temp |= 0x01;
            }
            if (MachineStatusForMrFrture.Switch_Plasma1) {
                temp |= 0x02;
            }
            if (MachineStatusForMrFrture.Child_Security_Lock) {
                temp |= 0x04;
            }
            if (MachineStatusForMrFrture.Switch_Valve) {
                temp |= 0x08;
            }
            if (MachineStatusForMrFrture.Switch_Plasma2) {
                temp |= 0x10;
            }
            if (MachineStatusForMrFrture.Switch_Plasma3) {
                temp |= 0x20;
            }
            if (MachineStatusForMrFrture.Switch_UVC) {
                temp |= 0x40;
            }
            if (MachineStatusForMrFrture.Switch_PTC) {
                temp |= 0x80;
            }
            date[11] = temp;
            date[12] = MachineStatusForMrFrture.Filter_Life1;
            date[13] = MachineStatusForMrFrture.Temperature_Quality;
            date[14] = MachineStatusForMrFrture.UVC_Life;
            date[15] = MachineStatusForMrFrture.Filter_Life2;
            date[16] = MachineStatusForMrFrture.Filter_Life3;
            date[17] = MachineStatusForMrFrture.Filter_Life4;
            date[18] = MachineStatusForMrFrture.set_byte1;
            date[19] = MachineStatusForMrFrture.humidity;

            date[20] = MachineStatusForMrFrture.temp_outdoor;
            date[21] = MachineStatusForMrFrture.humidity_outdoor;
            date[22] = MachineStatusForMrFrture.co_outdoor;

            date[24] = (byte) (MachineStatusForMrFrture.Timing_On & 0xff);
            date[23] = (byte) ((MachineStatusForMrFrture.Timing_On >> 8) & 0xff);
            date[26] = (byte) (MachineStatusForMrFrture.Timing_Off & 0xff);
            date[25] = (byte) ((MachineStatusForMrFrture.Timing_Off >> 8) & 0xff);
            int voc = MachineStatusForMrFrture.VOC_Quality / 2;
            date[28] = (byte) (voc & 0xff);
            date[27] = (byte) ((voc >> 8) & 0xff);
            date[30] = (byte) (MachineStatusForMrFrture.set_int1 & 0xff);
            date[29] = (byte) ((MachineStatusForMrFrture.set_int1 >> 8) & 0xff);
            date[32] = (byte) (MachineStatusForMrFrture.set_int2 & 0xff);
            date[31] = (byte) ((MachineStatusForMrFrture.set_int2 >> 8) & 0xff);

            date[34] = (byte) (MachineStatusForMrFrture.aqi_outdoor & 0xff);
            date[33] = (byte) ((MachineStatusForMrFrture.aqi_outdoor >> 8) & 0xff);
            date[36] = (byte) (MachineStatusForMrFrture.pm25_outdoor & 0xff);
            date[35] = (byte) ((MachineStatusForMrFrture.pm25_outdoor >> 8) & 0xff);
            date[38] = (byte) (MachineStatusForMrFrture.pm10_outdoor & 0xff);
            date[37] = (byte) ((MachineStatusForMrFrture.pm10_outdoor >> 8) & 0xff);
            date[40] = (byte) (MachineStatusForMrFrture.so2_outdoor & 0xff);
            date[39] = (byte) ((MachineStatusForMrFrture.so2_outdoor >> 8) & 0xff);
            date[42] = (byte) (MachineStatusForMrFrture.no2_outdoor & 0xff);
            date[41] = (byte) ((MachineStatusForMrFrture.no2_outdoor >> 8) & 0xff);
            date[44] = (byte) (MachineStatusForMrFrture.o3_outdoor & 0xff);
            date[43] = (byte) ((MachineStatusForMrFrture.o3_outdoor >> 8) & 0xff);


            date[45] = MachineStatusForMrFrture.read_byte1;
            date[47] = (byte) (MachineStatusForMrFrture.PM25_Indoor & 0xff);
            date[46] = (byte) ((MachineStatusForMrFrture.PM25_Indoor >> 8) & 0xff);
            date[49] = (byte) (MachineStatusForMrFrture.HCHO_Quality & 0xff);
            date[48] = (byte) ((MachineStatusForMrFrture.HCHO_Quality >> 8) & 0xff);
            date[51] = (byte) (MachineStatusForMrFrture.CO2_value & 0xff);
            date[50] = (byte) ((MachineStatusForMrFrture.CO2_value >> 8) & 0xff);
            date[53] = (byte) (MachineStatusForMrFrture.read_int1 & 0xff);
            date[52] = (byte) ((MachineStatusForMrFrture.read_int1 >> 8) & 0xff);
            date[55] = (byte) (MachineStatusForMrFrture.read_int2 & 0xff);
            date[54] = (byte) ((MachineStatusForMrFrture.read_int2 >> 8) & 0xff);
            temp = 0;
            if (MachineStatusForMrFrture.Alert_Air_Quality) {
                temp |= 0x01;
            }
            if (MachineStatusForMrFrture.Alert_Filter_Life1) {
                temp |= 0x02;
            }
            if (MachineStatusForMrFrture.Alert_Filter_Life2) {
                temp |= 0x04;
            }
            if (MachineStatusForMrFrture.Alert_Filter_Life3) {
                temp |= 0x08;
            }
            if (MachineStatusForMrFrture.Alert_Filter_Life4) {
                temp |= 0x10;
            }
            if (MachineStatusForMrFrture.Alert_UVC_Life) {
                temp |= 0x20;
            }
            if (MachineStatusForMrFrture.Alert_HCHO) {
                temp |= 0x40;
            }
            if (MachineStatusForMrFrture.Alert_CO2) {
                temp |= 0x80;
            }
            date[57] = temp;
            temp = 0;
            if (MachineStatusForMrFrture.Alert_CH4) {
                temp |= 0x01;
            }
            if (MachineStatusForMrFrture.Alert_VOC) {
                temp |= 0x02;
            }
            date[56] = temp;
            temp = 0;
            if (MachineStatusForMrFrture.Fault_Motor) {
                temp |= 0x01;
            }
            if (MachineStatusForMrFrture.fault_CO2) {
                temp |= 0x02;
            }
            if (MachineStatusForMrFrture.fault_PM25) {
                temp |= 0x04;
            }
            if (MachineStatusForMrFrture.fault_VOC) {
                temp |= 0x08;
            }
            if (MachineStatusForMrFrture.fault_HCHO) {
                temp |= 0x10;
            }
            if (MachineStatusForMrFrture.fault_CH4) {
                temp |= 0x20;
            }
            if (MachineStatusForMrFrture.fault_humidity) {
                temp |= 0x40;
            }
            if (MachineStatusForMrFrture.fault_tmp) {
                temp |= 0x80;
            }
            date[58] = temp;
            return date;
        } catch (Exception e) {
            e.printStackTrace();
            return new byte[]{};
        }
    }

    /**
     * 解析机智云服务器发送过来的数据
     *
     * @param msg
     * @param handler
     */
    public static void receivedMsgFromGizConvert(byte[] msg, Handler handler) {
        LogUtils.d("mrfurure receive msg from app " + CommonUtils.decodeBytesToHexString(msg));
        //000000032d000090010000000004000000000000000000000000000000000000000000000000000000000000000000000000
        try {
            if (msg.length < 34) {
                return;
            }
            if (msg[8] == SET_CMD) {

                int date = 0;
                AppCmd appCmd = null;
                long cmd = CommonUtils.getLongFromEightByte((byte) 0, (byte) 0, (byte) 0, msg[9], msg[10], msg[11], msg[12], msg[13]);
                if (cmd == Constants.MR_APP_SEND_POWER) {
                    date = (msg[16] & 0x01);
                    if (date == 1) {
                        MachineStatusForMrFrture.Switch = true;
                    } else {
                        MachineStatusForMrFrture.Switch = false;
                        LogUtils.d("power  1 is " + MachineStatusForMrFrture.Switch);
                    }
                    appCmd = new AppCmd(Constants.ANDROID_SEND_POWER, MachineStatusForMrFrture.Switch);
                }

                //离子开关1
                if (cmd == Constants.MR_APP_SEND_SWITCH_PLASMAL1) {
                    date = (msg[16] & 0x02);
                    if (date == 0x02) {
                        MachineStatusForMrFrture.Switch_Plasma1 = true;
                    } else {
                        MachineStatusForMrFrture.Switch_Plasma1 = false;
                    }
                    appCmd = new AppCmd(Constants.ANDROID_SEND_SWITCH_PLASMAL1, MachineStatusForMrFrture.Switch_Plasma1);

                }
                //儿童锁
                if (cmd == Constants.MR_APP_SEND_SWITCH_CHIRLD) {
                    date = (msg[16] & 0x04);
                    if (date == 0x04) {
                        appCmd = new AppCmd(Constants.ANDROID_SEND_SWITCH_CHIRLD, true);
                    } else {
                        appCmd = new AppCmd(Constants.ANDROID_SEND_SWITCH_CHIRLD, false);
                    }
                }
                //新风阀门开关
                if (cmd == Constants.MR_APP_SEND_SWITCH_FRESH) {
                    date = (msg[16] & 0x08);
                    if (date == 0x08) {
                        MachineStatusForMrFrture.Switch_Valve = true;
                    } else {
                        MachineStatusForMrFrture.Switch_Valve = false;
                    }
                    appCmd = new AppCmd(Constants.ANDROID_SEND_SWITCH_FRESH, MachineStatusForMrFrture.Switch_Valve);
                }
                //离子开关2
                if (cmd == Constants.MR_APP_SEND_SWITCH_PLASMAL2) {
                    LogUtils.d("receive msg from app date is " + msg[14]);
                    date = (msg[16] & 0x10);
                    if (date == 0x10) {
                        MachineStatusForMrFrture.Switch_Plasma2 = true;
                    } else {
                        MachineStatusForMrFrture.Switch_Plasma2 = false;
                    }
                    appCmd = new AppCmd(Constants.ANDROID_SEND_SWITCH_PLASMAL2, MachineStatusForMrFrture.Switch_Plasma2);
                }
                //离子开关3
                if (cmd == Constants.MR_APP_SEND_SWITCH_PLASMAL3) {
                    date = (msg[16] & 0x20);
                    if (date == 0x20) {
                        MachineStatusForMrFrture.Switch_Plasma3 = true;
                    } else {
                        MachineStatusForMrFrture.Switch_Plasma3 = false;
                    }
                    appCmd = new AppCmd(Constants.ANDROID_SEND_SWITCH_PLASMAL3, MachineStatusForMrFrture.Switch_Plasma3);

                }
                //uvc开关
                if (cmd == Constants.MR_APP_SEND_SWITCH_UVC) {
                    date = (msg[16] & 0x40);
                    if (date == 0x40) {
                        MachineStatusForMrFrture.Switch_UVC = true;
                    } else {
                        MachineStatusForMrFrture.Switch_UVC = false;
                    }
                    appCmd = new AppCmd(Constants.ANDROID_SEND_SWITCH_UVC, MachineStatusForMrFrture.Switch_UVC);
                }
                //ptc开关
                if (Math.abs(cmd) == Constants.MR_APP_SEND_SWITCH_PTC) {
                    date = (msg[16] & 0x80);
                    if (date == 0x80) {
                        MachineStatusForMrFrture.Switch_PTC = true;
                    } else {
                        MachineStatusForMrFrture.Switch_PTC = false;
                    }
                    appCmd = new AppCmd(Constants.ANDROID_SEND_SWITCH_PTC, MachineStatusForMrFrture.Switch_PTC);
                }
                //静电集成开关
                if (cmd == Constants.MR_APP_SEND_SWITCH_ELECTROSTATIC) {
                    date = (msg[15] & 0x01);
                    if (date == 1) {
                        MachineStatusForMrFrture.Switch_Electrostatic = true;
                    } else {
                        MachineStatusForMrFrture.Switch_Electrostatic = false;
                    }
                    appCmd = new AppCmd(Constants.ANDROID_SEND_SWITCH_ELECTROSTATIC, MachineStatusForMrFrture.Switch_Electrostatic);
                }
                //自定义风速
                if (cmd == Constants.MR_APP_SEND_WIND_LEVEL) {
                    MachineStatusForMrFrture.Wind_Velocity = (byte) ((msg[15] >> 1) & 0x0f);
                    appCmd = new AppCmd(Constants.ANDROID_SEND_WIND_LEVEL, MachineStatusForMrFrture.Wind_Velocity);
                }
                //运行模式
                if (cmd == Constants.MR_APP_SEND_MODE) {
                    MachineStatusForMrFrture.Mode = (byte) ((msg[15] >> 5) & 0x07);
                    appCmd = new AppCmd(Constants.ANDROID_SEND_MODE, MachineStatusForMrFrture.Mode);
                }
                //调压仓
                if (cmd == Constants.MR_APP_SEND_SURGE_TANK) {
                    MachineStatusForMrFrture.Surge_tank = (byte) (((msg[14]) & 0x07));
                    appCmd = new AppCmd(Constants.ANDROID_SEND_SURGE_TANK, MachineStatusForMrFrture.Surge_tank);
                }
                //初效滤网寿命
                if (cmd == Constants.MR_APP_SEND_LEFTTIME_CHU) {
                    MachineStatusForMrFrture.Filter_Life1 = msg[17];
                    appCmd = new AppCmd(Constants.ANDROID_SEND_LEFTTIME_CHU, MachineStatusForMrFrture.Filter_Life1);
                }
                //温度
                if (cmd == Constants.MR_APP_SEND_TEMP) {
                    MachineStatusForMrFrture.Temperature_Quality = msg[18];
                    appCmd = new AppCmd(Constants.ANDROID_SEND_TEMP, MachineStatusForMrFrture.Temperature_Quality);
                }
                //中效滤网寿命
                if (cmd == Constants.MR_APP_SEND_UVC) {
                    MachineStatusForMrFrture.UVC_Life = msg[19];
                    appCmd = new AppCmd(Constants.ANDROID_SEND_UVC, MachineStatusForMrFrture.UVC_Life);
                }
                //活性炭滤网寿命
                if (Math.abs(cmd) == Constants.MR_APP_SEND_LEFTTIME_ZHONG) {
                    MachineStatusForMrFrture.Filter_Life2 = msg[20];
                    appCmd = new AppCmd(Constants.ANDROID_SEND_LEFTTIME_ZHONG, MachineStatusForMrFrture.Filter_Life2);
                }
                //高效滤网寿命
                if (cmd == Constants.MR_APP_SEND_LEFTTIME_HUOXING) {
                    MachineStatusForMrFrture.Filter_Life3 = msg[21];
                    appCmd = new AppCmd(Constants.ANDROID_SEND_LEFTTIME_HUOXING, MachineStatusForMrFrture.Filter_Life3);
                }
                //高效滤网寿命
                if (cmd == Constants.MR_APP_SEND_LEFTTIME_GAOXIAO) {
                    MachineStatusForMrFrture.Filter_Life4 = msg[22];
                    appCmd = new AppCmd(Constants.ANDROID_SEND_LEFTTIME_GAOXIAO, MachineStatusForMrFrture.Filter_Life4);
                }
                //保留
                if (cmd == Constants.MR_APP_SEND_SET_BYTE1) {
                    MachineStatusForMrFrture.set_byte1 = msg[23];
                    appCmd = new AppCmd(Constants.ANDROID_SEND_SET_BYTE1, CommonUtils.getIntFromTwoByte((byte) 0x00, MachineStatusForMrFrture.set_byte1));
                }
                //保留
                if (cmd == Constants.MR_APP_SEND_SET_HUMIDITY) {
                    MachineStatusForMrFrture.humidity = msg[24];
                    appCmd = new AppCmd(Constants.ANDROID_SEND_SET_HUMIDITY, CommonUtils.getIntFromTwoByte((byte) 0x00, MachineStatusForMrFrture.humidity));
                }
                //室外温度
                if ((cmd & Constants.MR_APP_SEND_OUT_TEMP) == Constants.MR_APP_SEND_OUT_TEMP) {
                    MachineStatusForMrFrture.bOutDateEnable = true;
                    MachineStatusForMrFrture.temp_outdoor = msg[25];
                }
                //室外湿度
                if ((cmd & Constants.MR_APP_SEND_OUT_HUMIDITY) == Constants.MR_APP_SEND_OUT_HUMIDITY) {
                    MachineStatusForMrFrture.bOutDateEnable = true;
                    MachineStatusForMrFrture.humidity_outdoor = msg[26];
                }
                //室外co
                if ((cmd & Constants.MR_APP_SEND_OUT_CO) == Constants.MR_APP_SEND_OUT_CO) {
                    MachineStatusForMrFrture.bOutDateEnable = true;
                    MachineStatusForMrFrture.co_outdoor = msg[27];
                }
                //定时开机
                if (Math.abs(cmd) == Constants.MR_APP_SEND_OPENTIME) {
                    MachineStatusForMrFrture.Timing_On = CommonUtils.getIntFromTwoByte(msg[28], msg[29]);
                    appCmd = new AppCmd(Constants.ANDROID_SEND_OPENTIME, MachineStatusForMrFrture.Timing_On);
                }
                //定时关机
                if (cmd == Constants.MR_APP_SEND_CLOSETIME) {
                    MachineStatusForMrFrture.Timing_Off = CommonUtils.getIntFromTwoByte(msg[30], msg[31]);
                    appCmd = new AppCmd(Constants.ANDROID_SEND_CLOSETIME, MachineStatusForMrFrture.Timing_Off);
                }
                //室内voc
                if (cmd == Constants.MR_APP_SEND_VOC) {
                    MachineStatusForMrFrture.VOC_Quality = CommonUtils.getIntFromTwoByte(msg[32], msg[33]);
                    appCmd = new AppCmd(Constants.ANDROID_SEND_VOC, MachineStatusForMrFrture.VOC_Quality);
                }
                //保留
                if (cmd == Constants.MR_APP_SEND_SETINT1) {
                    MachineStatusForMrFrture.set_int1 = CommonUtils.getIntFromTwoByte(msg[34], msg[35]);
                    appCmd = new AppCmd(Constants.ANDROID_SEND_SETINT1, MachineStatusForMrFrture.set_int1);
                }
                //保留
                if (cmd == Constants.MR_APP_SEND_SETINT2) {
                    MachineStatusForMrFrture.set_int2 = CommonUtils.getIntFromTwoByte(msg[36], msg[37]);
                    appCmd = new AppCmd(Constants.ANDROID_SEND_SETINT2, MachineStatusForMrFrture.set_int2);
                }
                if ((cmd & Constants.MR_APP_SEND_OUT_AQI) == Constants.MR_APP_SEND_OUT_AQI) {
                    MachineStatusForMrFrture.bOutDateEnable = true;
                    MachineStatusForMrFrture.aqi_outdoor = CommonUtils.getIntFromTwoByte(msg[38], msg[39]);
                }
                if ((cmd & Constants.MR_APP_SEND_OUT_PM25) == Constants.MR_APP_SEND_OUT_PM25) {
                    MachineStatusForMrFrture.bOutDateEnable = true;
                    MachineStatusForMrFrture.pm25_outdoor = CommonUtils.getIntFromTwoByte(msg[40], msg[41]);
                }
                if ((cmd & Constants.MR_APP_SEND_OUT_PM10) == Constants.MR_APP_SEND_OUT_PM10) {
                    MachineStatusForMrFrture.bOutDateEnable = true;
                    MachineStatusForMrFrture.pm10_outdoor = CommonUtils.getIntFromTwoByte(msg[42], msg[43]);
                }
                if (Math.abs((cmd & Constants.MR_APP_SEND_OUT_SO2)) == (long) Constants.MR_APP_SEND_OUT_SO2) {
                    MachineStatusForMrFrture.bOutDateEnable = true;
                    MachineStatusForMrFrture.so2_outdoor = CommonUtils.getIntFromTwoByte(msg[44], msg[45]);
                }
                if ((long) (cmd & Constants.MR_APP_SEND_OUT_NO2) == (long) Constants.MR_APP_SEND_OUT_NO2) {
                    MachineStatusForMrFrture.bOutDateEnable = true;
                    MachineStatusForMrFrture.no2_outdoor = CommonUtils.getIntFromTwoByte(msg[46], msg[47]);
                }
                if ((long) (cmd & Constants.MR_APP_SEND_OUT_O3) == (long) Constants.MR_APP_SEND_OUT_O3) {
                    MachineStatusForMrFrture.bOutDateEnable = true;
                    MachineStatusForMrFrture.o3_outdoor = CommonUtils.getIntFromTwoByte(msg[48], msg[49]);
                }
                if (appCmd == null) {
                    appCmd = new AppCmd(0, 0);
                }

//                if( MachineStatusForMrFrture.temp_outdoor > 0 ){
//                    MachineStatusForMrFrture.bOutDateEnable = true;
//                }else {
//                    MachineStatusForMrFrture.bOutDateEnable = false;
//                }

                Message message = handler.obtainMessage();
                message.obj = appCmd;
                message.what = RemoteControlService.TCP_RECEIVEDATA;
                handler.sendMessage(message);

            } else if (msg[8] == SEARCH_CMD) {
//                Message message = handler.obtainMessage();
//                message.obj = new AppCmd(Constants.APP_QUEARY, 0);
//                message.what = RemoteControlService.TCP_RECEIVEDATA;
//                handler.sendMessage(message);
                return;
            } else {
                return;
            }
        } catch (Exception e) {
            return;
        }
    }


}

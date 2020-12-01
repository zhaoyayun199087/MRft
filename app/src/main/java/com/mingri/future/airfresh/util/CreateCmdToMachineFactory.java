package com.mingri.future.airfresh.util;

import mingrifuture.gizlib.code.provider.MachineStatus;
import mingrifuture.gizlib.code.provider.MachineStatusForMrFrture;
import mingrifuture.gizlib.code.util.LogUtils;

/**
 * Created by Administrator on 2017/7/7.
 */
public class CreateCmdToMachineFactory {

    private static  int mSn = 0;
    public static int[] createReadStatusCmd() {
        // TODO Auto-generated method stub
        int[] date = new int[10];
        date[0] = (byte) 0xff;
        date[1] = (byte) 0xff;
        date[3] = 6;
        date[4] = (byte) 0x03;
//        mSn++;
//        if(mSn > 254){
//            mSn = 1;
//        }
        date[5] = (byte) mSn;
        date[8] = (byte) 0x02;
        checksum(date);
        return date;
    }

    public static int[] createErrorInfoCmd( ) {
        // TODO Auto-generated method stub
        int[] date = new int[10];
        date[0] = (byte) 0xff;
        date[1] = (byte) 0xff;
        date[3] = 6;
        date[4] = (byte) 0x11;
        mSn++;
        if(mSn > 254){
            mSn = 1;
        }
        date[5] = (byte) mSn;
        date[8] = (byte) MachineStatusForMrFrture.sFaultCode;
        checksum(date);
        return date;
    }

    public static int[] createPowerOff() {
        // TODO Auto-generated method stub
        int[] date = new int[35];
        date[0] = (byte) 0xff;
        date[1] = (byte) 0xff;
        date[3] = 31;
        date[4] = (byte) 0x03;
        mSn++;
        if(mSn > 254){
            mSn = 1;
        }
        date[5] = (byte) mSn;
        date[8] = (byte) 0x01;

        date[9] = (byte) 0x01;
        date[10] = (byte) 0x00;
        date[11] = (byte) 0x00;
        date[12] = (byte) 0x00;

        date[13] = 0;
        checksum(date);
        return date;
    }

    public static int[] createPowerOn() {
        // TODO Auto-generated method stub
        int[] date = new int[35];
        date[0] = (byte) 0xff;
        date[1] = (byte) 0xff;
        date[3] = 31;
        date[4] = (byte) 0x03;
        mSn++;
        if(mSn > 254){
            mSn = 1;
        }
        date[5] = (byte) mSn;
        date[8] = (byte) 0x01;

        date[9] = (byte) 0x01;
        date[10] = (byte) 0x01;
        date[11] = (byte) 0x01;
        date[12] = (byte) 0x01;

        date[15] = (byte)0x01;
        checksum(date);
        return date;
    }

    public static int[] createRebootCmd() {
        // TODO Auto-generated method stub
        int[] date = new int[9];
        date[0] = (byte) 0xff;
        date[1] = (byte) 0xff;
        date[3] = 5;
        date[4] = (byte) 0x0f;
        mSn++;
        if(mSn > 254){
            mSn = 1;
        }
        date[5] = (byte) mSn;
        checksum(date);
        return date;
    }

    public static int[] createControlCmd(byte[] msg) {
        // TODO Auto-generated method stub
        int []tmp = new int[msg.length];
        for(int i = 0; i< msg.length; i++){
            tmp[i] = msg[i];
        }
        int[] date = new int[35];
        date[0] = (byte) 0xff;
        date[1] = (byte) 0xff;
        date[3] = 31;
        date[4] = (byte) 0x03;
        mSn++;
        if (mSn > 254) {
            mSn = 1;
        }
        date[5] = (byte) mSn;
        date[8] = (byte) 0x01;
        System.arraycopy(tmp,9, date,9,tmp.length - 9);
        checksum(date);
        return date;
    }


    public static int[] createControlCmd(int cmdFlag) {
        // TODO Auto-generated method stub
        int[] date = new int[35];
        date[0] = (byte) 0xff;
        date[1] = (byte) 0xff;
        date[3] = 31;
        date[4] = (byte) 0x03;
        mSn++;
        if(mSn > 254){
            mSn = 1;
        }
        date[5] = (byte) mSn;
        date[8] = (byte) 0x01;

        date[12] = (byte) (cmdFlag & (0xff));
        date[11] = (byte) ((cmdFlag >> 8) & (0xff));
        date[10] = (byte) ((cmdFlag >> 16) & (0xff));
        date[9] = (byte) ((cmdFlag >> 24) & (0xff));

        byte temp = 0;
        LogUtils.d("cmd down surge tank " + MachineStatusForMrFrture.Surge_tank);
        date[13] = (byte) ((MachineStatusForMrFrture.Surge_tank) & 0x07);
        if (MachineStatusForMrFrture.Switch_Electrostatic) {
            temp = (byte) (temp | 0x01);
        }
        temp |= (byte) ((MachineStatusForMrFrture.Wind_Velocity << 1) & 0x1e);
        temp |= (byte) ((MachineStatusForMrFrture.Mode << 5) & 0xe0);
        date[14] = temp;
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
        date[15] = temp;
        date[16] = MachineStatusForMrFrture.Filter_Life1;
        date[17] = MachineStatusForMrFrture.Temperature_Quality;
        date[18] = MachineStatusForMrFrture.UVC_Life;
        date[19] = MachineStatusForMrFrture.Filter_Life2;
        date[20] = MachineStatusForMrFrture.Filter_Life3;
        date[21] = MachineStatusForMrFrture.Filter_Life4;
        date[22] = MachineStatusForMrFrture.set_byte1;
        date[23] = MachineStatusForMrFrture.humidity;
        date[25] = (byte) (MachineStatusForMrFrture.Timing_On & 0xff);
        date[24] = (byte) ((MachineStatusForMrFrture.Timing_On >> 8) & 0xff);
        date[27] = (byte) (MachineStatusForMrFrture.Timing_Off & 0xff);
        date[26] = (byte) ((MachineStatusForMrFrture.Timing_Off >> 8) & 0xff);
        int voc =       Math.round( MachineStatusForMrFrture.VOC_Quality / 200f);
        date[29] = (byte) (voc & 0xff);
        date[28] = (byte) ((voc >> 8) & 0xff);
        date[31] = (byte) (MachineStatusForMrFrture.set_int1 & 0xff);
        date[30] = (byte) ((MachineStatusForMrFrture.set_int1 >> 8) & 0xff);
        date[33] = (byte) (MachineStatusForMrFrture.set_int2 & 0xff);
        date[32] = (byte) ((MachineStatusForMrFrture.set_int2 >> 8) & 0xff);

        checksum(date);
        return date;
    }

    public static int[] createControlCmd(int cmdFlag, int sn) {
        // TODO Auto-generated method stub
        int[] date = new int[35];
        date[0] = (byte) 0xff;
        date[1] = (byte) 0xff;
        date[3] = 31;
        date[4] = (byte) 0x03;

        date[5] = (byte) sn;
        date[8] = (byte) 0x01;

        date[12] = (byte) (cmdFlag & (0xff));
        date[11] = (byte) ((cmdFlag >> 8) & (0xff));
        date[10] = (byte) ((cmdFlag >> 16) & (0xff));
        date[9] = (byte) ((cmdFlag >> 24) & (0xff));

        byte temp = 0;
        date[13] = (byte) ((MachineStatusForMrFrture.Surge_tank) & 0x07);
        if (MachineStatusForMrFrture.Switch_Electrostatic) {
            temp = (byte) (temp | 0x01);
        }
        temp |= (byte) ((MachineStatusForMrFrture.Wind_Velocity << 1) & 0x1e);
        temp |= (byte) ((MachineStatusForMrFrture.Mode << 5) & 0xe0);
        date[14] = temp;
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
        date[15] = temp;
        date[16] = MachineStatusForMrFrture.Filter_Life1;
        date[17] = MachineStatusForMrFrture.Temperature_Quality;
        date[18] = MachineStatusForMrFrture.UVC_Life;
        date[19] = MachineStatusForMrFrture.Filter_Life2;
        date[20] = MachineStatusForMrFrture.Filter_Life3;
        date[21] = MachineStatusForMrFrture.Filter_Life4;
        date[22] = MachineStatusForMrFrture.set_byte1;
        date[23] = MachineStatusForMrFrture.humidity;
        date[25] = (byte) (MachineStatusForMrFrture.Timing_On & 0xff);
        date[24] = (byte) ((MachineStatusForMrFrture.Timing_On >> 8) & 0xff);
        date[27] = (byte) (MachineStatusForMrFrture.Timing_Off & 0xff);
        date[26] = (byte) ((MachineStatusForMrFrture.Timing_Off >> 8) & 0xff);
        date[29] = (byte) (MachineStatusForMrFrture.VOC_Quality & 0xff);
        date[28] = (byte) ((MachineStatusForMrFrture.VOC_Quality >> 8) & 0xff);
        date[31] = (byte) (MachineStatusForMrFrture.set_int1 & 0xff);
        date[30] = (byte) ((MachineStatusForMrFrture.set_int1 >> 8) & 0xff);
        date[33] = (byte) (MachineStatusForMrFrture.set_int2 & 0xff);
        date[32] = (byte) ((MachineStatusForMrFrture.set_int2 >> 8) & 0xff);

        checksum(date);
        return date;
    }

    public static void checksum(int[] date) {
        // TODO Auto-generated method stub
        int len = date.length;
        int sum = 0;
        for (int i = 2; i <= len - 2; i++) {
            sum += (int) date[i];
        }
        date[len - 1] = (byte) (sum & 0xff);
    }
    /**
     * @param date
     * @return
     */
    public static byte getPackageCheckSum(byte[] date, int length) {
        // TODO Auto-generated method stub
        int len = length;
        int sum = 0;
        for (int i = 2; i <= len - 2; i++) {
            sum += (int) date[i];
        }
        return (byte) (sum & 0xff);
    }
}

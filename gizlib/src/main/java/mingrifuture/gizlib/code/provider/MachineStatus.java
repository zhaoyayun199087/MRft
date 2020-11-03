package mingrifuture.gizlib.code.provider;

public class MachineStatus {

    //开关
    public static boolean sSys_sw;
    //锁机
    public static boolean sLock_sw;
    //加热开关
    public static boolean sHeat_sw;
    //冲洗功能
    public static boolean sWash_sw;
    //温控开关
    public static boolean sTemp_sw;
    //浮子开关
    public static boolean sFloat_sw;
    //童锁开关
    public static boolean sChirld_lock_sw;
    //制冷开关
    public static boolean sCold_sw;
    //制冷开关
    public static boolean sRes_bit1;
    //音量大小
    public static byte sVolume;
    //保留位
    public static byte sRes_byte1;
    //保留位
    public static byte sRes_byte2;
    //剩余时间
    public static int sLeft_time;
    //地址信息
    public static int sRes_int1;
    //地址信息2
    public static int sRes_int2;
    //精度
    public static String sJd = "";
    //纬度
    public static String sWd = "";
    //软件版本
    public static int sSoftver;
    //硬件版本
    public static int sHardver;
    //厂家编码
    public static int sFac_No;
    //设备主控编码
    public static int sDevtype;
    //状态信息
    public static int sSys_sta;
    //红外监控状态
    public static int sInfrared;
    //保留位
    public static int sRes_byte3;
    //滤网运行时间
    public static int sStrtime;
    //进水tds
    public static int sIn_tds;
    //出水tds
    public static int sOut_tds;
    //返回剩余时间
    public static int sRet_left_time;
    //保留位
    public static int sSRes_int3;
    /*
    重传次数（此处是发数据失败重发）
     */
    public static int sSendRecvNum = 0;

    /*
    最后发送个命令
     */
    public static int lastSendCmd = 0;
    /*
    从发命令次数（此处是收发数据不一致而重发）
     */
    public static int reSendNum = 0;
}

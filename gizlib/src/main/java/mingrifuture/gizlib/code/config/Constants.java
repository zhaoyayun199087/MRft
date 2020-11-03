package mingrifuture.gizlib.code.config;

/**
 * Created by pengl on 2016/1/6.
 */
public class Constants {
    /**
     * 厂家标识
     */
    public final static int CH2102VendorId = 4292;

    /**
     * 产品标识
     */
    public final static int CH2102ProductId = 60000;
    /**
     * 厂家标识
     */
    public final static int CH340VendorId = 6790;

    /**
     * 产品标识
     */
    public final static int CH340ProductId = 29987;
    /**
     * 厂家标识
     */
    public final static int PL2303VendorId = 1659;

    /**
     * 产品标识
     */
    public final static int PL2303ProductId = 8963;

    /**
     * TCP 监听端口
     **/
    public final static int TCP_PORT = 12416;

    /**
     * 命令信息头部
     */
    public final static byte ORDER_HEAD = -1;

    /**
     * 重启设备回复
     */
    public final static byte ORDER_REBOOT_REP = (byte) 0x10;

    /**
     * 命令信息头部
     */
    public final static byte ORDER_ILLEGAL_REP = (byte) 0x12;

    /**
     * 命令信息头部
     */
    public final static byte ORDER_READ_STATUS_REP = (byte) 0x04;

    /**
     * 命令信息尾部
     */
    public final static byte ORDER_FOOTER = -17;

    /**
     * APP命令信息头部
     */
    public final static byte ORDER_APP_HEAD = -18;

    /**
     * 查询命令的数据
     */
    public final static byte[] ORDER_QUERY_DATA = {-1, 17, 1, 17, 17, -17};

    /**
     * 命令信息设置
     */
    public final static byte ORDER_SETTING = -86;

    public final static String ORDER_POWER_OFF = "ffa1011111ef";

    public final static String ORDER_POWER_ON = "ffa1011010ef";

    public final static String ORDER_SEARCH = "ff11011111ef";

    public final static String ORDER_SET_FAIL = "ffa0010000ef";

    public final static String ORDER_CORRECT = "ff5a011111ef";

    // public final static String ORDER_CORRECT_PM = "ffa502";

    public final static byte ORDER_REPORTE_SMALL = -96;

    /**
     * 聚合天气预报数据接口id
     */
    public final static int WEATHER_DID = 73;

    /**
     * 聚合天气预报数据接口url
     */
    public final static String WEATHER_URL = "http://op.juhe.cn/onebox/weather/query";

    public static boolean IS_UPDATED = false;

    /*
     * 新协议2017-4-12 14:31:54
     */
    //开关机
    public static final int APP_SEND_POWER = 0x000001;
    //锁机
    public static final int APP_SEND_LOCK = 0x000002;
    //加热开关
    public static final int APP_SEND_HEAD = 0x000004;
    //冲洗功能
    public static final int APP_SEND_RUSH = 0x000008;
    //温控开关
    public static final int APP_SEND_TMP = 0x000010;
    //浮子开关
    public static final int APP_SEND_FLOAT = 0x000020;
    //童锁开关
    public static final int APP_SEND_CHIRLD_LOCK = 0x000040;
    //制冷开关
    public static final int APP_SEND_COLD = 0x000080;
    //保留位
    public static final int APP_SEND_RES_BIT1 = 0x000100;
    //音量
    public static final int APP_SEND_VOLUME = 0x000200;
    //保留位
    public static final int APP_SEND_RES_BYTE1 = 0x000400;
    //保留位
    public static final int APP_SEND_RES_BYTE2 = 0x000800;
    //剩余时间
    public static final int APP_SEND_LEFT_TIME = 0x001000;
    //保留位
    public static final int APP_SEND_RES_INT1 = 0x002000;
    //保留位
    public static final int APP_SEND_RES_INT2 = 0x004000;
    //精度
    public static final int APP_SEND_JD = 0x008000;
    //纬度
    public static final int APP_SEND_WD = 0x010000;
    //查询
    public static final int APP_QUEARY = 0xffffff;


    /*
     *   明日蔚蓝项目
     */
    //开关
    public static final int ANDROID_SEND_POWER = 0x00000001;
    //离子开关1
    public static final int ANDROID_SEND_SWITCH_PLASMAL1 = 0x00000002;
    //儿童锁
    public static final int ANDROID_SEND_SWITCH_CHIRLD = 0x00000004;
    //新风阀门开关
    public static final int ANDROID_SEND_SWITCH_FRESH = 0x00000008;
    //离子开关2
    public static final int ANDROID_SEND_SWITCH_PLASMAL2 = 0x00000010;
    //离子开关3
    public static final int ANDROID_SEND_SWITCH_PLASMAL3 = 0x00000020;
    //uvc开关
    public static final int ANDROID_SEND_SWITCH_UVC = 0x00000040;
    //ptc开关
    public static final int ANDROID_SEND_SWITCH_PTC = 0x00000080;
    //静电集成开关
    public static final int ANDROID_SEND_SWITCH_ELECTROSTATIC = 0x00000100;
    //自定义风速
    public static final int ANDROID_SEND_WIND_LEVEL = 0x00000200;
    //运行模式
    public static final int ANDROID_SEND_MODE = 0x00000400;
    //调压仓
    public static final int ANDROID_SEND_SURGE_TANK = 0x00000800;
    //初效滤网寿命
    public static final int ANDROID_SEND_LEFTTIME_CHU = 0x00001000;
    //温度
    public static final int ANDROID_SEND_TEMP = 0x00002000;
    //uvc
    public static final int ANDROID_SEND_UVC = 0x00004000;
    //中效滤网寿命
    public static final int ANDROID_SEND_LEFTTIME_ZHONG = 0x00008000;
    //活性炭滤网寿命
    public static final int ANDROID_SEND_LEFTTIME_HUOXING = 0x00010000;
    //高效滤网寿命
    public static final int ANDROID_SEND_LEFTTIME_GAOXIAO = 0x00020000;
    //保留
    public static final int ANDROID_SEND_SET_BYTE1 = 0x00040000;
    //湿度
    public static final int ANDROID_SEND_SET_HUMIDITY = 0x00080000;
    //定时开机
    public static final int ANDROID_SEND_OPENTIME = 0x00100000;
    //定时关机
    public static final int ANDROID_SEND_CLOSETIME = 0x00200000;
    //室内voc
    public static final int ANDROID_SEND_VOC = 0x00400000;
    //保留
    public static final int ANDROID_SEND_SETINT1 = 0x00800000;
    //保留
    public static final int ANDROID_SEND_SETINT2 = 0x01000000;

    /**
     * 机智云下发过来的指令
     */
    //开关
    public static final long MR_APP_SEND_POWER =                0x0000000000000001L;
    //离子开关1
    public static final long MR_APP_SEND_SWITCH_PLASMAL1 =    0x0000000000000002L;
    //儿童锁
    public static final long MR_APP_SEND_SWITCH_CHIRLD =    0x0000000000000004L;
    //新风阀门开关
    public static final long MR_APP_SEND_SWITCH_FRESH = 0x0000000000000008L;
    //离子开关2
    public static final long MR_APP_SEND_SWITCH_PLASMAL2 = 0x0000000000000010L;
    //离子开关3
    public static final long MR_APP_SEND_SWITCH_PLASMAL3 = 0x0000000000000020L;
    //uvc开关
    public static final long MR_APP_SEND_SWITCH_UVC = 0x0000000000000040L;
    //ptc开关
    public static final long MR_APP_SEND_SWITCH_PTC = 0x0000000000000080L;
    //静电集成开关
    public static final long MR_APP_SEND_SWITCH_ELECTROSTATIC = 0x0000000000000100L;
    //自定义风速
    public static final long MR_APP_SEND_WIND_LEVEL = 0x0000000000000200L;
    //运行模式
    public static final long MR_APP_SEND_MODE = 0x0000000000000400L;
    //调压仓
    public static final long MR_APP_SEND_SURGE_TANK = 0x0000000000000800L;
    //初效滤网寿命
    public static final long MR_APP_SEND_LEFTTIME_CHU = 0x0000000000001000L;
    //温度
    public static final long MR_APP_SEND_TEMP = 0x0000000000002000L;
    //uvc
    public static final long MR_APP_SEND_UVC = 0x0000000000004000L;
    //中效滤网寿命
    public static final long MR_APP_SEND_LEFTTIME_ZHONG = 0x0000000000008000L;
    //活性炭滤网寿命
    public static final long MR_APP_SEND_LEFTTIME_HUOXING = 0x0000000000010000L;
    //高效滤网寿命
    public static final long MR_APP_SEND_LEFTTIME_GAOXIAO = 0x0000000000020000L;
    //保留
    public static final long MR_APP_SEND_SET_BYTE1 = 0x0000000000040000L;
    //湿度
    public static final long MR_APP_SEND_SET_HUMIDITY = 0x0000000000080000L;
    //室外温度
    public static final long MR_APP_SEND_OUT_TEMP = 0x0000000000100000L;
    //室外湿度
    public static final long MR_APP_SEND_OUT_HUMIDITY = 0x0000000000200000L;
    //室外CO
    public static final long MR_APP_SEND_OUT_CO = 0x0000000000400000L;
    //定时开机
    public static final long MR_APP_SEND_OPENTIME =  0x0000000000800000L;
    //定时关机
    public static final long MR_APP_SEND_CLOSETIME = 0x0000000001000000L;
    //室内voc
    public static final long MR_APP_SEND_VOC = 0x0000000002000000L;
    //保留
    public static final long MR_APP_SEND_SETINT1 = 0x0000000004000000L;
    //保留
    public static final long MR_APP_SEND_SETINT2 = 0x0000000008000000L;
    //保留
    public static final long MR_APP_SEND_OUT_AQI = 0x0000000010000000L;
    //保留
    public static final long MR_APP_SEND_OUT_PM25 = 0x0000000020000000L;
    //保留
    public static final long MR_APP_SEND_OUT_PM10 = 0x0000000040000000L;
    //保留
    public static final long MR_APP_SEND_OUT_SO2 =  0x0000000080000000L;
    //保留
    public static final long MR_APP_SEND_OUT_NO2 = 0x0000000100000000L;
    //保留
    public static final long MR_APP_SEND_OUT_O3 =  0x0000000200000000L;
}

package mingrifuture.gizlib.code.provider;

public class MachineStatusForMrFrture {
    public static boolean Switch;  //	开关
    public static byte Wind_Velocity;  //	自定义风速
    public static boolean Switch_Plasma1;  //	离子开关1
    public static boolean Switch_Clock = false;  //	定时开关
    public static boolean Switch_Wifi = true;  //	定时开关
    public static boolean Child_Security_Lock;  //	儿童安全锁
    public static byte Filter_Life1;  //	初效滤网寿命
    public static int PM25_Indoor;  //	室内PM2.5
    public static byte temp_outdoor;  //  室外温度
    public static byte humidity_outdoor;  //  室外湿度
    public static byte co_outdoor;  //  室外co
    public static int aqi_outdoor;  //  室外aqi
    public static int pm25_outdoor;  //  室外PM25
    public static int pm10_outdoor;  //  室外PM10
    public static int so2_outdoor;  //  室外So2
    public static int no2_outdoor;  //  室外no2
    public static int o3_outdoor;  //  室外o3
    public static int HCHO_Quality;  //	室内甲醛
    public static boolean Fault_Motor;  //	电机故障
    public static int Timing_On;  //	定时开机
    public static int Timing_Off;  //	定时关机
    public static boolean Alert_Air_Quality;  //	空气质量警报
    public static boolean Switch_Valve;  //	新风阀门开关
    public static int VOC_Quality;  //	室内VOC
    public static byte Temperature_Quality;  //	温度
    public static int CO2_value;  //	室内CO2
    public static byte UVC_Life;  //	UVC寿命
    public static boolean fault_CO2;  //	CO2故障
    public static boolean fault_PM25;  //	PM25故障
    public static boolean fault_VOC;  //	VOC故障
    public static boolean fault_HCHO;  //	甲醛故障
    public static boolean fault_CH4;  //	甲烷故障
    public static boolean fault_humidity;  //	湿度传感器故障
    public static boolean fault_tmp;  //	温度传感器故障
    public static byte humidity;  //	湿度
    public static byte Filter_Life2;  //	中效滤网寿命
    public static byte Filter_Life3;  //	活性炭滤网寿命
    public static byte Filter_Life4;  //	高效滤网寿命
    public static byte Mode;  //	运行模式
    public static boolean Switch_Plasma2;  //	离子开关2
    public static boolean Switch_Plasma3;  //	离子开关3
    public static byte Surge_tank;  //	调压仓
    public static boolean Switch_UVC;  //	UVC开关
    public static boolean Switch_PTC;  //	PTC辅热开关
    public static boolean Switch_Electrostatic;  //	静电集尘开关
    public static boolean Alert_Filter_Life1;  //	寿命报警-初效滤芯
    public static boolean Alert_Filter_Life2;  //	寿命报警-中效滤芯
    public static boolean Alert_Filter_Life3;  //	寿命报警-活性炭滤芯
    public static boolean Alert_Filter_Life4;  //	寿命报警-高效滤芯
    public static boolean Alert_UVC_Life;  //	寿命报警-UVC杀菌灯
    public static boolean Alert_HCHO;  //	甲醛超标报警
    public static boolean Alert_CO2;  //	二氧化碳超标报警
    public static boolean Alert_CH4;  //	甲烷超标报警
    public static boolean Alert_VOC;  //	VOC超标报警
    public static int set_int1;  //	set_int1
    public static int set_int2;  //	set_int2
    public static byte set_byte1;  //	set_byte1
    public static byte read_byte1;  //	read_byte1
    public static int read_int1;  //	read_int1  改为甲烷
    public static int read_int2;  //	read_int2

    public static int soft_version;
    public static int hard_version;
    //错误码
    public static int sFaultCode;
    //开机时间点
    public static long startTime;
    //设置极速模式时间点
    public static long setSpeedmodeTime;
    //是否开启智能控制（不是智能模式）
    public static boolean bSmartControl = true;
    //是否二氧化碳超标弹窗
    public static boolean bCo2Popup = false;
    //是否甲醛超标弹窗
    public static boolean bHchoPopup = false;
    //是否甲烷才超标弹窗
    public static boolean bCh4Popup = false;
    //是否VOC超标弹窗
    public static boolean bVOCPopup = false;
    //是否已经初效滤网寿命弹窗
    public static boolean bChuDiag = false;
    //是否已经中效滤网寿命弹窗
    public static boolean bZhongDiag = false;
    //是否已经高效滤网寿命弹窗
    public static boolean bGaoDiag = false;
    //是否已经活性炭滤网寿命弹窗
    public static boolean bHuoDiag = false;
    //是否已经uvc寿命弹窗
    public static boolean bUvcDiag = false;
    /*
    重传次数（此处是发数据失败重发）
     */
    public static long sShutDownTime = 0;
    /*
    发送次数，发一次+1，接收到了清零
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
    /*
    是否第一次接收到了串口数据
     */
    public static boolean bRecvSerialDate = false;
    /*
       是否联网获取室外数据
        */
    public static boolean bOutDateEnable = false;
    /*
      是否正在升级
       */
    public static boolean bUpdating = false;
    /*
    自动改变屏幕亮度计数
   */
    public static int iCount ;
    /*
    是否在输入wifi密码对话框，如果是，则不息屏
    */
    public static boolean bShowWifiDialog = false ;

    /*
    准备退出程序标志
    */
    public static boolean isExiting = false;
    /*
    省份
     */
    public static String province;
    /*
    市区
     */
    public static String city;

    /**
     * 室外数据是否需要更新
     */
    public static boolean bUpdateOutDate =false;
}

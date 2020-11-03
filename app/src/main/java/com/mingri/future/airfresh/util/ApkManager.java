package com.mingri.future.airfresh.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;

import mingrifuture.gizlib.code.util.LogUtils;

/**
 * Created by Administrator on 2017/9/20.
 */

public class ApkManager {
    private static final String TAG = "ApkManager";
    private static final String INSTALL_CMD = "install";
    private static final String UNINSTALL_CMD = "uninstall";





    //静默卸载
    //  com.mingri.future.airfresh
    public static boolean uninstallSlient(String PACKAGE_NAME) {
        String cmd = "pm uninstall " + PACKAGE_NAME;
        Process process = null;
        DataOutputStream os = null;
        BufferedReader successResult = null;
        BufferedReader errorResult = null;
        StringBuilder successMsg = null;
        StringBuilder errorMsg = null;
        try {
            //卸载也需要root权限
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.write(cmd.getBytes());
            os.writeBytes("\n");
            os.writeBytes("exit\n");
            os.flush();
            //执行命令
            process.waitFor();
            //获取返回结果
            successMsg = new StringBuilder();
            errorMsg = new StringBuilder();
            successResult = new BufferedReader(new InputStreamReader(process.getInputStream()));
            errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String s;
            while ((s = successResult.readLine()) != null) {
                successMsg.append(s);
            }
            while ((s = errorResult.readLine()) != null) {
                errorMsg.append(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (process != null) {
                    process.destroy();
                }
                if (successResult != null) {
                    successResult.close();
                }
                if (errorResult != null) {
                    errorResult.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //显示结果
        Log.e("cjh", "成功消息：" + successMsg.toString() + "\n" + "错误消息: " + errorMsg.toString());
        if( (successMsg.toString().length()<=1) || "Failure".equals(successMsg.toString()))return false;
        return true;
    }


    //静默安装
    public static  boolean  installSlient() {

        String cmd = "pm install -r "+ Environment.getExternalStorageDirectory().getPath() + "/update/update.apk";
        Process process = null;
        DataOutputStream os = null;
        BufferedReader successResult = null;
        BufferedReader errorResult = null;
        StringBuilder successMsg = null;
        StringBuilder errorMsg = null;
        LogUtils.d("install silence begin");
        try {
            //静默安装需要root权限
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.write(cmd.getBytes());
            os.writeBytes("\n");
            os.writeBytes("exit\n");
            os.flush();
            //执行命令
            LogUtils.d("install silence 1");
            process.waitFor();
            LogUtils.d("install silence 2");
            //获取返回结果
            successMsg = new StringBuilder();
            errorMsg = new StringBuilder();
            successResult = new BufferedReader(new InputStreamReader(process.getInputStream()));
            errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String s;
            while ((s = successResult.readLine())!= null) {
                successMsg.append(s);
            }
            while ((s = errorResult.readLine())!= null) {
                errorMsg.append(s);
            }
            LogUtils.d("install silence 3 " + successMsg.toString() );

        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.d("install silence 4 " + e.toString() );

        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (process != null) {
                    process.destroy();
                }
                if (successResult != null) {
                    successResult.close();
                }
                if (errorResult != null) {
                    errorResult.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //显示结果
        LogUtils.d("install silence：" + successMsg.toString() +"\n" + "错误消息: " + errorMsg.toString());        if(successMsg.toString().length()<=1)return false;
        return true;
    }
    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}

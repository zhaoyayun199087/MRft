package com.mingri.future.airfresh.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mingri.future.airfresh.R;
import com.mingri.future.airfresh.bean.SendDataToMachine;
import com.mingri.future.airfresh.util.ApkManager;
import com.mingri.future.airfresh.util.CommonUtils;
import com.mingri.future.airfresh.util.CreateCmdToMachineFactory;
import com.mingri.future.airfresh.util.UpdateUtils;
import com.mingri.future.airfresh.view.GradientCircleVIew;
import com.mingri.future.airfresh.view.dialog.AlreadyNewVersionDialog;
import com.mingri.future.airfresh.view.dialog.DetectUpdataDialog;
import com.mingri.future.airfresh.view.dialog.DisableWifiDialog;
import com.mingri.future.airfresh.view.dialog.InstallDialog;
import com.mingri.future.airfresh.view.dialog.NewVersionDialog;
import com.pgyersdk.javabean.AppBean;
import com.wlt.apache.http.client.HttpClient;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import butterknife.ButterKnife;
import butterknife.InjectView;
import mingrifuture.gizlib.code.config.Constants;
import mingrifuture.gizlib.code.provider.MachineStatusForMrFrture;
import mingrifuture.gizlib.code.util.LogUtils;
import mingrifuture.gizlib.code.util.NetUtils;
import mingrifuture.gizlib.code.util.Utils;

/**
 * 更新版本
 */
public class UpDataFragment extends BaseFragment {
    @InjectView(R.id.tv_version)
    TextView tvVersion;
    @InjectView(R.id.tv_mac)
    TextView tvMac;
    @InjectView(R.id.ll_update)
    LinearLayout llUpdate;
    @InjectView(R.id.ll_1)
    LinearLayout ll1;
    @InjectView(R.id.tv_percent)
    TextView tvPercent;
    @InjectView(R.id.tv_update)
    TextView tvUpdate;
    @InjectView(R.id.btn_install)
    Button btnInstall;
    @InjectView(R.id.ll_2)
    LinearLayout ll2;
    @InjectView(R.id.pb_progress)
    ProgressBar pbProgress;
    @InjectView(R.id.circle_percent)
    GradientCircleVIew circlePercent;
    private Handler mHandler = new Handler();
    private final String path = Environment.getExternalStorageDirectory().getPath() + "/update/";

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frg_updata, null);
        ButterKnife.inject(this, v);
        tvVersion.setText(getString(R.string.diag_current_version) + Utils.getVersion(getActivity()));
        String mac = NetUtils.getMac(getActivity());
        if (mac != null) {
            mac = mac.replaceAll(":", "");
            if (mac.length() > 4) {
                mac = "accf" + mac.substring(4, mac.length());
            }
            mac = mac.toUpperCase();
            tvMac.setText(getString(R.string.diag_mac) + mac);
        }
        tvUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateUtils.pgyCheckUpdate(getActivity(), false);
            }
        });

        btnInstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setDataAndType(Uri.fromFile(new File(path + "update.apk")),
//                        "application/vnd.android.package-archive");
//                getActivity().startActivity(intent);
                shutDown();
                InstallDialog dialog = new InstallDialog(activity);
                dialog.show();
                //TODO 静默安装
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        boolean success = ApkManager.installSlient();
                    }
                }).start();
            }
        });

        llUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isWifiConnected(getActivity())) {
                    DisableWifiDialog dialog = new DisableWifiDialog(activity);
                    dialog.show();
                    return;
                }

                final DetectUpdataDialog dialog = new DetectUpdataDialog(activity);
                dialog.show();

                UpdateUtils.pgyCheckUpdate(getActivity(), new UpdateUtils.pgyUpdateCallback() {
                    @Override
                    public void onSuccess(AppBean appBean) {
                        LogUtils.d("update sucess " + appBean.getDownloadURL() + " " + appBean.getReleaseNote() + " " + appBean.getVersionCode() + " " + appBean.getVersionName() + "  ");
                        NewVersionDialog nd = new NewVersionDialog(activity, appBean.getVersionName(), appBean.getReleaseNote(), appBean.getDownloadURL(), new NewVersionDialog.updateCallback() {
                            @Override
                            public void update(final String url) {
                                LogUtils.d("begin download " + url);
                                ll1.setVisibility(View.GONE);
                                ll2.setVisibility(View.VISIBLE);
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        downFile(url);
                                    }
                                }).start();
                            }
                        });
                        nd.show();
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailed() {
                        LogUtils.d("update failed 1");
                        dialog.dismiss();
                        AlreadyNewVersionDialog ad = new AlreadyNewVersionDialog(getActivity());
                        ad.show();
                    }
                });
            }
        });
        return v;
    }

    //更新重启
    private void shutDown() {
        MachineStatusForMrFrture.Switch = false;

        LogUtils.d("power  4 is " + MachineStatusForMrFrture.Switch);
        if (MachineStatusForMrFrture.Switch) {
            MachineStatusForMrFrture.startTime = System.currentTimeMillis();
        }
        CommonUtils.setOrder(Constants.ANDROID_SEND_POWER, MachineStatusForMrFrture.Switch ? 1 : 0);

        int[] d = CreateCmdToMachineFactory.createControlCmd(Constants.ANDROID_SEND_POWER);
        EventBus.getDefault().post(new SendDataToMachine(d));
    }

    private void downFile(String url) {
        try {
            downLoadFromUrl(url, "update.apk", path, new downloadCallback() {
                @Override
                public void percent(final int percent) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            tvPercent.setText("" + percent + "%");
                            circlePercent.setPercent(percent);
                        }
                    });
                    if (percent == 100) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                tvUpdate.setVisibility(View.GONE);
                                btnInstall.setVisibility(View.VISIBLE);
                                pbProgress.setVisibility(View.GONE);
                            }
                        });
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            mHandler.sendEmptyMessage(1);
        }
    }

    private boolean isWifiConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetworkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    /**
     * 从网络Url中下载文件
     *
     * @param urlStr
     * @param fileName
     * @param savePath
     * @throws IOException
     */
    public void downLoadFromUrl(String urlStr, String fileName, String savePath, downloadCallback dc) throws Exception {
        LogUtils.d("htts download url " + urlStr);
        if (urlStr.contains("https")) {
            urlStr = urlStr.replaceAll("https", "http");
        }
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //设置超时间为3秒
        conn.setConnectTimeout(5 * 1000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

        int len = conn.getContentLength();
        if (len == -1) {
            throw new Exception("get content lengt error");
        }
        //得到输入流
        InputStream inputStream = conn.getInputStream();

        //文件保存位置
        File saveDir = new File(savePath);
        if (!saveDir.exists()) {
            saveDir.mkdir();
        }
        File file = new File(saveDir + File.separator + fileName);
        if (file.exists()) {
            file.delete();
        }
        OutputStream fos = new FileOutputStream(file);
        readInputStream(fos, inputStream, dc, len);

        System.out.println("info:" + url + " download success");

    }

    public static SSLContext getSLLContext() {
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }}, new SecureRandom());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sslContext;
    }

    private static HostnameVerifier hostnameVerifier = new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    private class MyHostnameVerifier implements HostnameVerifier {

        @Override
        public boolean verify(String hostname, SSLSession session) {
            // TODO Auto-generated method stub
            return true;
        }
    }

    private class MyTrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
            // TODO Auto-generated method stub

        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
            // TODO Auto-generated method stub

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            // TODO Auto-generated method stub
            return null;
        }
    }

    /**
     * 从输入流中获取字节数组
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static void readInputStream(OutputStream sfos, InputStream inputStream, downloadCallback dc, int fileLen) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        int sum = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            sfos.write(buffer, 0, len);
            sum += len;
            LogUtils.d("down file sum is " + sum + " fileLen is  " + fileLen);
            dc.percent((sum * 100) / fileLen);
        }
        if (sfos != null) {
            sfos.close();
        }
        if (inputStream != null) {
            inputStream.close();
        }
        return;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    public interface downloadCallback {
        public void percent(int percent);
    }
}

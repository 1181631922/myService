package cn.edu.sjzc.service.activity;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;
import cn.edu.sjzc.service.R;
import cn.edu.sjzc.service.thread.FileDownloadThread;

public class LoginSuccessActivity extends BaseActivity implements
        OnClickListener {
    private Button download, download_two;
    private ProgressBar progressBar, notificationProgress;
    private NotificationManager manager;
    private TextView notificationTitle, notificationPercent, progresstext;
    private int progress, msize;
    private PendingIntent contentIntent;
    // NotificationManager的滚动提示
    /**
     * NotificationManager的图标，一般不要用彩色，我测试是用系统默认的
     * 还有就是系统带的图片是int类型的，测试Listview显示本地图片时时记得定义int类型 但是如果是网络图片要定义为string类型
     */
    private int icon_download = android.R.drawable.stat_sys_download;
    private String tickerText = "开始下载XXXXXXX";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginsuccess);
        initView();
    }

    private void initView() {
        this.download = (Button) LoginSuccessActivity.this
                .findViewById(R.id.download);
        this.download.setOnClickListener(this);

        this.progresstext = (TextView) LoginSuccessActivity.this
                .findViewById(R.id.progresstext);
        this.progressBar = (ProgressBar) LoginSuccessActivity.this
                .findViewById(R.id.progressBar);
        this.notificationTitle = (TextView) LoginSuccessActivity.this
                .findViewById(R.id.notificationTitle);
        this.notificationPercent = (TextView) LoginSuccessActivity.this
                .findViewById(R.id.notificationPercent);
        this.notificationProgress = (ProgressBar) LoginSuccessActivity.this
                .findViewById(R.id.notificationProgress);

        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Notification的Intent，即点击后转向的Activity
        Intent it_notification = new Intent(this, this.getClass());
        it_notification.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        contentIntent = PendingIntent.getActivity(this, 0, it_notification, 0);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.download:
                doDownload();
                break;

            default:
                break;
        }

    }

    /**
     * 使用Handler更新UI界面信息
     */
    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // 创建Notification
            Notification mNotification = new Notification(icon_download,
                    tickerText, System.currentTimeMillis());
            // 设定Notification出现时的声音，看过好多资料都说不建议自定义
            mNotification.defaults |= Notification.DEFAULT_SOUND;
            // 设置是否震动
            mNotification.defaults |= Notification.DEFAULT_VIBRATE;
            // 指定Flag,Notification.FLAG_AUTO_CANCEL意指点击这个Notification后立刻取消自身
            // 符合一般的Notification的运作规范，左划不消失类似QQ
            // mNotification.flags |= Notification.FLAG_ONGOING_EVENT;
            // 通知被左划后消失
            mNotification.flags |= Notification.FLAG_AUTO_CANCEL;
            // 创建RemoteViews用在Notification
            RemoteViews contentView = new RemoteViews(getPackageName(),
                    R.layout.notification_view_download);
            contentView.setTextViewText(R.id.notificationTitle,
                    "Download:正在下载中...");

            progressBar.setProgress(msg.getData().getInt("size"));
            float temp = (float) progressBar.getProgress()
                    / (float) progressBar.getMax();
            int progress = (int) (temp * 100);
            if (progress == 100) {
                // Activity显示
                Toast.makeText(LoginSuccessActivity.this, "下载完成！",
                        Toast.LENGTH_LONG).show();
                // Notification显示
                mNotification.icon = R.drawable.down_success;
                contentView.setTextViewText(R.id.notificationTitle,
                        "Download:下载完成！");

            }
            // notification显示
            contentView.setTextViewText(R.id.notificationPercent, progress
                    + "%");
            contentView.setProgressBar(R.id.notificationProgress, 100,
                    progress, false);
            mNotification.contentView = contentView;
            mNotification.contentIntent = contentIntent;
            manager.notify(1, mNotification);
            // activity显示
            progresstext.setText("下载进度:" + progress + " %");

        }
    };

    /**
     * 下载准备工作，获取SD卡路径、开启线程
     */
    private void doDownload() {

        // 获取SD卡路径
        String path = Environment.getExternalStorageDirectory()
                + "/amosdownload/";
        File file = new File(path);
        // 如果SD卡目录不存在创建
        if (!file.exists()) {
            file.mkdir();
        }
        // 设置progressBar初始化
        progressBar.setProgress(0);

        // 简单起见，我先把URL和文件名称写死，其实这些都可以通过HttpHeader获取到
        String downloadUrl = "http://gdown.baidu.com/data/wisegame/91319a5a1dfae322/baidu_16785426.apk";
        String fileName = "baidu_16785426.apk";
        int threadNum = 5;
        String filepath = path + fileName;

        downloadTask task = new downloadTask(downloadUrl, threadNum, filepath);
        task.start();
    }

    /**
     * 多线程文件下载
     *
     *
     * @2014-8-7
     */
    class downloadTask extends Thread {
        private String downloadUrl;// 下载链接地址
        private int threadNum;// 开启的线程数
        private String filePath;// 保存文件路径地址
        private int blockSize;// 每一个线程的下载量

        public downloadTask(String downloadUrl, int threadNum, String fileptah) {
            this.downloadUrl = downloadUrl;
            this.threadNum = threadNum;
            this.filePath = fileptah;
        }

        @Override
        public void run() {

            FileDownloadThread[] threads = new FileDownloadThread[threadNum];
            try {
                URL url = new URL(downloadUrl);
                URLConnection conn = url.openConnection();
                // 读取下载文件总大小
                int fileSize = conn.getContentLength();
                if (fileSize <= 0) {
                    System.out.println("读取文件失败");
                    return;
                }
                // 设置ProgressBar最大的长度为文件Size
                progressBar.setMax(fileSize);

                // 计算每条线程下载的数据长度
                blockSize = (fileSize % threadNum) == 0 ? fileSize / threadNum
                        : fileSize / threadNum + 1;

                File file = new File(filePath);
                for (int i = 0; i < threads.length; i++) {
                    // 启动线程，分别下载每个线程需要下载的部分
                    threads[i] = new FileDownloadThread(url, file, blockSize,
                            (i + 1));
                    threads[i].setName("Thread:" + i);
                    threads[i].start();
                }

                boolean isfinished = false;
                int downloadedAllSize = 0;
                while (!isfinished) {
                    isfinished = true;
                    // 当前所有线程下载总量
                    downloadedAllSize = 0;
                    for (int i = 0; i < threads.length; i++) {
                        downloadedAllSize += threads[i].getDownloadLength();
                        if (!threads[i].isCompleted()) {
                            isfinished = false;
                        }
                    }
                    // 通知handler去更新视图组件
                    Message msg = mHandler.obtainMessage();
                    msg.getData().putInt("size", downloadedAllSize);
                    mHandler.sendMessage(msg);
                    // Log.d(TAG, "current downloadSize:" + downloadedAllSize);
                    Thread.sleep(1000);// 休息1秒后再读取下载进度
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

}

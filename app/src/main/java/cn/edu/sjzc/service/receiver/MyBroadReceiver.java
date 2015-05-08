package cn.edu.sjzc.service.receiver;


import cn.edu.sjzc.service.activity.MainActivity;
import cn.edu.sjzc.service.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MyBroadReceiver extends BroadcastReceiver {

    private NotificationManager mNotificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

        if (intent.getAction().equals("cn.edu.sjzc.fanyafeng.start")) {

            Log.i("接收的信息为：", "xuliang's qimiaozhongdejiyi");
            // 播放音乐,每次当收到广播都会新建一个任务，这样，虽然下面可以收到停止的广播系统也停止不了
            // 播放音乐，不仅如此，每次都会新建一个任务，这样，当你多次点击广播播放时就会听到有一首歌
            // 在分不同的起点进行播放
            // 那么肯定有人想通过广播既能开始又能停止，绑定Service即可实现
            MediaPlayer.create(context, R.raw.qimiaozhongdejiyi).start();

        }
        if (intent.getAction().equals("cn.edu.sjzc.fanyafeng.shut")) {
            Log.i("接收的信息为：", "shut down the music");
            // 停止播放音乐
            MediaPlayer.create(context, R.raw.qimiaozhongdejiyi).stop();
        }

        if (intent.getAction().equals("cn.edu.sjzc.fanyafeng.broad.two")) {
            Log.i("接收的信息为：", "broad download one");
            // 下载图片

        }
        if (intent.getAction().equals("cn.edu.sjzc.fanyafeng.shut.two")) {
            Log.i("接收的信息为：", "shut the music");
            // 支持断点下载

        }
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Log.i("接收的信息为：", "系统启动完成");
            // 系统启动完成,测试3.0以下的版本可以检测到此广播，3.1以后收不到此广播，具体的大家可以查查资料
            /**
             * 经过查资料得 Android 3.1开始, 由于安全性的考虑.
             * 程序在安装后,用户没有通过自己的操作来启动程序的话,那么这个程序将收不到android
             * .intent.action.BOOT_COMPLETED这个Intent; 用户通过自己的操作启动过一次程序后,
             * receiver将被激活, 从而收的到android.intent.action.BOOT_COMPLETED Intent.
             */

            Toast.makeText(context, "系统启动完成", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public IBinder peekService(Context myContext, Intent service) {
        // TODO Auto-generated method stub
        //查了查资料不知道干嘛的，没看google源码
        return super.peekService(myContext, service);
    }


}

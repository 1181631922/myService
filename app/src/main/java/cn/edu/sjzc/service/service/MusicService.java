package cn.edu.sjzc.service.service;

import cn.edu.sjzc.service.R;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class MusicService extends Service {
    // 定义输入日志
    private static String TAG = "生命周期";
    // 定义音乐播放变量
    private MediaPlayer mPlayer;

    private MyBinder mBinder = new MyBinder();

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        Log.i(TAG, "onBind()执行");
//		mPlayer.start();
        return mBinder;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        Log.i(TAG, "onCreate()执行");//第一个执行
        //R.raw.xxxx其中raw不能换成其他的名称，不然Android识别不出来，囧
        mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.qimiaozhongdejiyi);
        //设置音乐循环播放,这个大家可以想一下Handler其中的Looper
        mPlayer.setLooping(true);
        super.onCreate();
    }



    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        Log.i(TAG, "onDestroy()执行");//停止播放时执行
        mPlayer.stop();
        super.onDestroy();
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        Log.i(TAG, "onStartCommand()执行");//第二个执行
        mPlayer.start();
        return super.onStartCommand(intent, flags, startId);
    }




    @Override
    public boolean onUnbind(Intent intent) {
        // TODO Auto-generated method stub
        Log.i(TAG, "onUnbind()执行");
        return super.onUnbind(intent);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onStart(Intent intent, int startId) {
        // TODO Auto-generated method stub
        //onStart方法被谷歌打上了横杠，不建议大家使用
        Log.i(TAG, "onStart()执行");//第三个执行
        //start()方法放在onStartCommand
//		mPlayer.start();
        super.onStart(intent, startId);
    }

    public class MyBinder extends Binder{
        MusicService getService(){
            return MusicService.this;
        }
    }

}

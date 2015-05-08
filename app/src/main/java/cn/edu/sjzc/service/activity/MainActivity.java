package cn.edu.sjzc.service.activity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;
import cn.edu.sjzc.service.service.MusicService;
import cn.edu.sjzc.service.R;

public class MainActivity extends BaseActivity implements OnClickListener {

    private Button start, end, bind, unbind, login, exit, sign, broad, shut,
            broad_two, shut_two;
    private TextView mtime;
    private TextView battery;

    private static String TAG = "生命周期";
    private static String nowbattery;

    private boolean isBind = false;

    private BatteryBroadcastReceiver batteryBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        Log.i(TAG, "onCreate方法执行");

        initView();
        trendsBroad();

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        // 取消注册
        // unregisterReceiver(batteryBroadcastReceiver);
    }

    private void trendsBroad() {
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction("android.intent.action.BATTERY_CHANGED");
        batteryBroadcastReceiver = new BatteryBroadcastReceiver();
        registerReceiver(batteryBroadcastReceiver, mFilter);
    }

    private void initView() {
        this.start = (Button) MainActivity.this.findViewById(R.id.start);
        this.start.setOnClickListener(this);

        this.end = (Button) MainActivity.this.findViewById(R.id.end);
        this.end.setOnClickListener(this);

        this.bind = (Button) MainActivity.this.findViewById(R.id.bind);
        this.bind.setOnClickListener(this);

        this.unbind = (Button) MainActivity.this.findViewById(R.id.unbind);
        this.unbind.setOnClickListener(this);

        this.login = (Button) MainActivity.this.findViewById(R.id.denglu);
        this.login.setOnClickListener(this);

        this.exit = (Button) MainActivity.this.findViewById(R.id.tuichu);
        this.exit.setOnClickListener(this);

        this.sign = (Button) MainActivity.this.findViewById(R.id.sign);
        this.sign.setOnClickListener(this);

        this.broad = (Button) MainActivity.this.findViewById(R.id.broad);
        this.broad.setOnClickListener(this);

        this.shut = (Button) MainActivity.this.findViewById(R.id.shut);
        this.shut.setOnClickListener(this);

        this.broad_two = (Button) MainActivity.this.findViewById(R.id.kaishi);
        this.broad_two.setOnClickListener(this);

        this.shut_two = (Button) MainActivity.this.findViewById(R.id.tingzhi);
        this.shut_two.setOnClickListener(this);

        this.mtime = (TextView) MainActivity.this.findViewById(R.id.mtime);
        this.mtime.setText(getNowTime());

        this.battery = (TextView) MainActivity.this.findViewById(R.id.battery);

    }

    private String getNowTime() {
        SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyy年MM月dd日 HH:mm:ss ");
        Date curDate = new Date(System.currentTimeMillis());
        return formatter.format(curDate);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        Intent intent = new Intent(MainActivity.this, MusicService.class);

        switch (v.getId()) {
            case R.id.start:
                startService(intent);
                break;
            case R.id.end:
                stopService(intent);
                break;
            case R.id.bind:
                isBind = true;
                bindService(intent, conn, BIND_AUTO_CREATE);
                break;
            case R.id.unbind:
                if (isBind) {
                    isBind = false;
                    unbindService(conn);
                } else {
                    Toast.makeText(getApplicationContext(), "请先绑定",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.denglu:
                mlogin();
                break;
            case R.id.tuichu:
                isLogin = false;
                Toast.makeText(getApplicationContext(), "退出！！！isLogin = false",
                        Toast.LENGTH_SHORT).show();
                break;
            case R.id.sign:
                Intent it_sign = new Intent(MainActivity.this, SignActivity.class);
                startActivity(it_sign);
                break;
            case R.id.broad:
                Intent it_broad = new Intent().setAction(
                        "cn.edu.sjzc.fanyafeng.start").putExtra("music",
                        "xuliang's qimiaozhongdejiyi");
                sendBroadcast(it_broad);
                break;
            case R.id.shut:
                Intent it_shut = new Intent().setAction(
                        "cn.edu.sjzc.fanyafeng.shut").putExtra("stop",
                        "shut down the music");
                sendBroadcast(it_shut);
                break;
            case R.id.kaishi:
                Intent it_broad_two = new Intent().setAction(
                        "cn.edu.sjzc.fanyafeng.broad.two").putExtra("broad download",
                        "broad download one");
                sendBroadcast(it_broad_two);
                break;
            case R.id.tingzhi:
                Intent it_shut_two = new Intent().setAction(
                        "cn.edu.sjzc.fanyafeng.shut.two").putExtra("shut music",
                        "shut the music");
                sendBroadcast(it_shut_two);
                break;

            default:
                break;
        }

    }

    private void mlogin() {
        if (isLogin == false) {
            Toast.makeText(getApplicationContext(), "请先注册！！！isLogin = false",
                    Toast.LENGTH_SHORT).show();
            Intent it_failed = new Intent(MainActivity.this,
                    LoginFailedActivity.class);
            startActivity(it_failed);
        }
        if (isLogin == true) {
            Toast.makeText(getApplicationContext(), "登陆成功！！！isLogin = true",
                    Toast.LENGTH_SHORT).show();
            Intent it_login = new Intent(MainActivity.this,
                    LoginSuccessActivity.class);
            startActivity(it_login);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            MainActivity.this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    final ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // TODO Auto-generated method stub

        }
    };

    public class BatteryBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub

            if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
                int level = intent.getIntExtra("level", 0);

                int scale = intent.getIntExtra("scale", 100);

                nowbattery = "电池电量：" + ((level * 100) / scale) + "%";
                Log.i("现在的电池电量", nowbattery);
                MainActivity.this.battery.setText(nowbattery);

                if (level < 15) {
                    Toast.makeText(context, "请充电", Toast.LENGTH_SHORT).show();
                }

            }

        }

    }

}

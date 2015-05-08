package cn.edu.sjzc.service.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import cn.edu.sjzc.service.R;

public class SignActivity extends BaseActivity implements OnClickListener{
	
	private Button sign;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign);
		
		initView();
	}
	
	private void initView(){
		this.sign = (Button)SignActivity.this.findViewById(R.id.sign);
		this.sign.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.sign:
			isLogin = true;
            Toast.makeText(getApplicationContext(), "注册成功！！！isLogin = true",
                    Toast.LENGTH_SHORT).show();
			break;

		default:
			break;
		}
		
	}
}

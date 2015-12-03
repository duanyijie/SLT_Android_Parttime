package com.niit.parttime.ui.users;

import org.apache.http.message.BasicNameValuePair;

import android.R.integer;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.niit.parttime.common.util.IntentUtil;
import com.niit.parttime.common.util.LogUtil;
import com.niit.parttime.common.util.PreferencesUtils;
import com.niit.parttime.common.util.ToastUtils;
import com.niit.parttime.ui.BaseActivity;
import com.niit.parttime.ui.job.PartTimeJobList;
import com.niit.parttime.ui.job.ParttimeJobCollectionList;
import com.niit.slt_andriod_parttime.MainActivity;
import com.niit.slt_andriod_parttime.R;

public class UsersHome extends BaseActivity {
	
	@ViewInject(R.id.user_name)
	TextView user_name;
	
	@ViewInject(R.id.log_before)
	LinearLayout log_before;
	
	@ViewInject(R.id.log_after)
	LinearLayout log_after;
	
	private int usersID;
	private String usersName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_home);
		ViewUtils.inject(this);// 1-注入View和相关事件
		usersID = PreferencesUtils.getInt(this, "usersID");
		LogUtils.e(usersID+"");
		usersName = PreferencesUtils.getString(this, "usersName");
		if(usersName!=null){
			log_before.setVisibility(View.GONE);
			log_after.setVisibility(View.VISIBLE);
			user_name.setText(usersName);
		}
		
	}
	
	@OnClick(R.id.login_btn)
	private void Login_btn(View v){
		IntentUtil.start_activity(UsersHome.this, LoginActivity.class);		
	}
	
	@OnClick(R.id.job_collection)
	private void Job_collection(View v){
	
		IntentUtil.start_activity(UsersHome.this, ParttimeJobCollectionList.class);
	}
	
	@OnClick(R.id.setting_list)
	private void Setting_list(View v){
		IntentUtil.start_activity(this, UsersSettingActivity.class);
	}
	
	@OnClick(R.id.tv_home)
	private void tv_home(View v){
		IntentUtil.start_activity(this, MainActivity.class);
	}
	
	@OnClick(R.id.tv_job)
	private void tv_job(View v){
		IntentUtil.start_activity(this, PartTimeJobList.class);
	}
	
	@OnClick(R.id.tv_parttime)
	private void tv_parttime(View v){
		IntentUtil.start_activity(this, PartTimeJobList.class);
	}
	
	@OnClick(R.id.tv_profile)
	private void tv_profile(View v){
		IntentUtil.start_activity(this, UsersHome.class);
	}
}

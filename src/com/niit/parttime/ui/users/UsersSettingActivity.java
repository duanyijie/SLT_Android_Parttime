package com.niit.parttime.ui.users;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.style.BackgroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.niit.parttime.common.util.IntentUtil;
import com.niit.parttime.common.util.PreferencesUtils;
import com.niit.parttime.ui.BaseActivity;
import com.niit.parttime.ui.setting.ChangePwdActivity;
import com.niit.parttime.ui.setting.UsersAbout;
import com.niit.parttime.ui.setting.UsersFeedbackActivity;
import com.niit.parttime.util.DataCleanManager;
import com.niit.slt_andriod_parttime.MainActivity;
import com.niit.slt_andriod_parttime.R;

public class UsersSettingActivity extends BaseActivity{
	
	
	@ViewInject(R.id.totalCacheSize)
	TextView totalCacheSize;
	
	String cacheSize = "0KB";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_users_setting_layout);
		ViewUtils.inject(this);
		
		try {
			cacheSize = DataCleanManager.getTotalCacheSize(this);
			totalCacheSize.setText(cacheSize);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void logOut(){
		SharedPreferences sharedPreferences = getSharedPreferences(PreferencesUtils.PREFERENCE_NAME,MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.remove("usersID");
		editor.remove("usersName");
		editor.commit();
	}
	
	
	
	@OnClick(R.id.back)
	private void Back(View v){
		IntentUtil.start_activity(this, UsersHome.class);
	}
	
	@OnClick(R.id.clear_cache)
	private void Clear_cache(View v){
		DataCleanManager.clearAllCache(UsersSettingActivity.this);
		try {
			DataCleanManager.getTotalCacheSize(UsersSettingActivity.this);
			totalCacheSize.setText(cacheSize);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@OnClick(R.id.present)
	private void Present(View v){
		
	}
	@OnClick(R.id.function_introduce)
	private void Function_introduce(View v){
		
	}
	@OnClick(R.id.baodian)
	private void Baodian(View v){
		
	}
	@OnClick(R.id.check_update)
	private void Check_update(View v){
		
	}
	@OnClick(R.id.user_feedback)
	private void User_feedback(View v){
		IntentUtil.start_activity(UsersSettingActivity.this, UsersFeedbackActivity.class);
	}
	@OnClick(R.id.about)
	private void About(View v){
		IntentUtil.start_activity(UsersSettingActivity.this, UsersAbout.class);
	}
	@OnClick(R.id.remove_login)
	private void Remove_login(View v){
		logOut();
		showLongToast(2, "退出账号成功");
		IntentUtil.start_activity(UsersSettingActivity.this, LoginActivity.class);
	}
	@OnClick(R.id.exit)
	private void Exit(View v){
		System.exit(0);
	}

	@OnClick(R.id.change_password)
	private void Change_password(View v){
		IntentUtil.start_activity(UsersSettingActivity.this, ChangePwdActivity.class);
	}
	@OnClick(R.id.changephone)
	private void Changephone(View v){
		
	}
	
	

}

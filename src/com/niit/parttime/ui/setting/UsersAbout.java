package com.niit.parttime.ui.setting;


import android.os.Bundle;
import android.view.View;

import com.lidroid.xutils.view.annotation.event.OnClick;
import com.niit.parttime.common.util.IntentUtil;
import com.niit.parttime.ui.BaseActivity;
import com.niit.parttime.ui.users.UsersSettingActivity;
import com.niit.slt_andriod_parttime.R;

public class UsersAbout extends BaseActivity{
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
	}
	
	@OnClick(R.id.back)
	private void Back(View v){
		IntentUtil.start_activity(this, UsersSettingActivity.class);
	}
}

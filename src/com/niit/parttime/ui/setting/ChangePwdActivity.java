package com.niit.parttime.ui.setting;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.niit.parttime.common.util.IntentUtil;
import com.niit.parttime.common.util.PreferencesUtils;
import com.niit.parttime.config.URLs;
import com.niit.parttime.entity.Joblist;
import com.niit.parttime.entity.Message;
import com.niit.parttime.ui.BaseActivity;
import com.niit.parttime.ui.LoadingDialog;
import com.niit.parttime.ui.job.PartTimeJobList;
import com.niit.parttime.ui.users.LoginActivity;
import com.niit.slt_andriod_parttime.R;

public class ChangePwdActivity extends BaseActivity {

	@ViewInject(R.id.oldPassword)
	TextView oldPassword;

	@ViewInject(R.id.password)
	TextView password;

	@ViewInject(R.id.confirm_password)
	TextView confirm_password;

	LoadingDialog dialog;
	String usersName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_password);
		ViewUtils.inject(this);
		dialog = new LoadingDialog(this);
		usersName = PreferencesUtils.getString(this, "usersName", "0");
	}

	@OnClick(R.id.md_password_submit)
	private void Md_password_submit(View v) {
		String oldpwd = oldPassword.getText().toString().trim();
		String pwd = password.getText().toString().trim();
		String confirmpwd = confirm_password.getText().toString().trim();

		if (TextUtils.isEmpty(oldpwd)) {
			showShortToast(1, "旧密码必须填写");
			return;
		}
		if (TextUtils.isEmpty(pwd)) {
			showShortToast(1, "新密码必须填写");
			return;
		}
		if (TextUtils.isEmpty(confirmpwd)) {
			showShortToast(1, "再次确认密码必须填写");
			return;
		}
		if (!confirmpwd.equals(pwd)) {
			showShortToast(1, "两次密码输入必须一致");
			return;
		}
		RequestParams params = new RequestParams();

		params.addBodyParameter("usersName", usersName);
		params.addBodyParameter("usersOldPwd",oldpwd);
		params.addBodyParameter("usersPwd", pwd);

		HttpUtils http = new HttpUtils();

		http.send(HttpRequest.HttpMethod.POST, URLs.USERS_MD_PWD, params,
				new RequestCallBack<String>() {

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						// TODO Auto-generated method stub
						super.onLoading(total, current, isUploading);
						dialog.show();
					}

					@Override
					public void onFailure(HttpException arg0, String msg) {
						if (dialog != null && dialog.isShowing()) {
							dialog.dismiss();
						}
						showLongToast(3, msg);
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						if (dialog != null && dialog.isShowing()) {
							dialog.dismiss();
						}

						ObjectMapper mapper = new ObjectMapper();
						Message message = null;
						try {
							message = mapper.readValue(responseInfo.result,
									Message.class);
						} catch (IOException e) {
							e.printStackTrace();
						}
						int resultID = message.res;
						if (resultID > 0) {
							showLongToast(2, message.msg);
							IntentUtil.start_activity(ChangePwdActivity.this,
									PartTimeJobList.class);

						} else {
							showLongToast(3, message.msg);
						}
					}
				});
	}
}

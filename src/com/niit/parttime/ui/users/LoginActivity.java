package com.niit.parttime.ui.users;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.niit.parttime.common.util.IntentUtil;
import com.niit.parttime.common.util.PreferencesUtils;
import com.niit.parttime.config.URLs;
import com.niit.parttime.entity.Message;
import com.niit.parttime.entity.Users;
import com.niit.parttime.ui.BaseActivity;
import com.niit.parttime.ui.LoadingDialog;
import com.niit.parttime.ui.job.PartTimeJobList;
import com.niit.slt_andriod_parttime.MainActivity;
import com.niit.slt_andriod_parttime.R;

public class LoginActivity extends BaseActivity {

	@ViewInject(R.id.login_username)
	EditText login_username;

	@ViewInject(R.id.login_password)
	EditText login_password;

	private LoadingDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		ViewUtils.inject(this);// 1-注入View和相关事件
		dialog = new LoadingDialog(this);
	}

	@OnClick(R.id.login_submit)
	public void Submit(View v) {
		String username = login_username.getText().toString().trim();
		String userpwd = login_password.getText().toString().trim();
		if (TextUtils.isEmpty(username)) {
			showShortToast(1, "用户名必须填写");
			return;
		}
		if (TextUtils.isEmpty(userpwd)) {
			showShortToast(1, "密码必须填写");
			return;
		}
		RequestParams params = new RequestParams();
		params.addBodyParameter("usersName", username);
		params.addBodyParameter("usersPwd", userpwd);

		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, URLs.LOGIN, params,
				new RequestCallBack<String>() {

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						super.onLoading(total, current, isUploading);
						dialog.show();
					}

					@Override
					public void onFailure(HttpException exception, String msg) {
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
						LogUtils.e("服务器端回传结果：" + responseInfo.result);						
						ObjectMapper mapper = new ObjectMapper();
						Message message = null;
						try {
							// 将服务器端回传的JSON反序列化为Message类
							message = mapper.readValue(responseInfo.result,
									Message.class);
						} catch (IOException e) {
							e.printStackTrace();
						}
						if (message != null) {
							int resultID = message.res;
							if (resultID > 0) {// 说明登录成功
								showLongToast(2, "登录成功");
								// 登录成功后还要做什么？？
								Users users;
								try {
									users = mapper.readValue(message.data,
											Users.class);
									// 1-保存用户登录成功后的信息进入会话
									PreferencesUtils.putInt(LoginActivity.this,
											"usersID", users.usersID);
									LogUtils.e(users.usersID+"");
									PreferencesUtils.putString(
											LoginActivity.this, "usersName",
											users.usersName);

									// 2-跳转
									IntentUtil.start_activity(
											LoginActivity.this,
											PartTimeJobList.class);
									LoginActivity.this.finish();
								} catch (IOException e) {
									e.printStackTrace();
								}

							} else {// 说明登录失败
								showLongToast(3, "登录失败");
							}
						} else {
							// message==null
							// 1-服务器端回传json为null
							// 2-如果服务器端回传了JOSN，则可能是JSON无法反序列化为对象
							showLongToast(3, "未解析到对象");
						}
					}
				});
	}

	@OnClick(R.id.login_forget_password)
	private void login_forget_password(View v) {
		IntentUtil.start_activity(LoginActivity.this, ResetPwdActivity.class);
		LoginActivity.this.finish();
	}

	@OnClick(R.id.login_register)
	private void login_register(View v) {
		IntentUtil.start_activity(LoginActivity.this, RegisterActivity.class);
		LoginActivity.this.finish();
	}

	@OnClick(R.id.back)
	private void back(View v) {
		IntentUtil.start_activity(LoginActivity.this, MainActivity.class);
		LoginActivity.this.finish();
	}

}

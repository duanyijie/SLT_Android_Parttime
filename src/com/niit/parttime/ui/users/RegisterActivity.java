package com.niit.parttime.ui.users;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
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
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
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

public class RegisterActivity extends BaseActivity {

	@ViewInject(R.id.register_username)
	EditText register_username;
	
	@ViewInject(R.id.register_password)
	EditText register_password;
	@ViewInject(R.id.register_confirm_password)
	EditText register_confirm_password;
	@ViewInject(R.id.register_invite_code)
	EditText register_invite_code;

	private LoadingDialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_userregist_layout);
		ViewUtils.inject(this);
		dialog = new LoadingDialog(this);
	}
	
	@OnClick(R.id.register_submit)
	private void register_submit(View v) {
		String username = register_username.getText().toString().trim();
		String userpwd = register_password.getText().toString().trim();
		String confirm_userpwd = register_confirm_password.getText().toString()
				.trim();
		String invite_code = register_invite_code.getText().toString().trim();

		if (TextUtils.isEmpty(username)) {
			showShortToast(3, "用户名必须填写");
			return;
		}
		if (TextUtils.isEmpty(userpwd)) {
			showShortToast(3, "密码必须填写");
			return;
		}
		if (TextUtils.isEmpty(confirm_userpwd)) {
			showShortToast(3, "请再次输入密码");
			return;
		}
		if (!confirm_userpwd.equals(userpwd)) {
			showShortToast(3, "两次密码输入不一致");
			return;
		}
		// 先组装服务器需要的参数
		RequestParams params = new RequestParams();
		params.addBodyParameter("usersName", username);
		params.addBodyParameter("usersPwd", userpwd);
		params.addBodyParameter("usersInvalitCode", invite_code);
		params.addBodyParameter("usersIsForgot", "0");
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String date = sd.format(new Date());
		params.addBodyParameter("usersRegDate", date);
		// 异步提交至服务器端
		HttpUtils http = new HttpUtils();
		http.send(HttpMethod.POST, URLs.REGISTER, params,
				new RequestCallBack<String>() {

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						// TODO Auto-generated method stub
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
						LogUtils.e("" + responseInfo);
						ObjectMapper mapper = new ObjectMapper();
						Message message = null;
						try {
							message = mapper.readValue(responseInfo.result,
									Message.class);
						} catch (JsonParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (JsonMappingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (message != null) {
							int resultId = message.res;
							if (resultId > 0) {// 注册成功
								// 1.提示成功信息
								showLongToast(2, "注册成功");
								// 2.用户信息写入共享存储中 会话
								try {
									Users users = mapper.readValue(
											message.data, Users.class);
									PreferencesUtils.putInt(
											RegisterActivity.this, "usersID",
											users.usersID);
									LogUtils.e(users.usersID+"");
									PreferencesUtils.putString(
											RegisterActivity.this, "usersName",
											users.usersName);
									IntentUtil.start_activity(RegisterActivity.this, PartTimeJobList.class);
									RegisterActivity.this.finish();
								} catch (JsonParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (JsonMappingException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								// 3.跳转至列表页面
								IntentUtil.start_activity(RegisterActivity.this,PartTimeJobList.class);
							} else {
								showLongToast(3, "注册失败");
							}
						} else {
							showLongToast(3, "未解析到对象");
						}

					}
				});
	}
	
	@OnClick(R.id.back)
	private void Back(View v){
		IntentUtil.start_activity(RegisterActivity.this, MainActivity.class);
		RegisterActivity.this.finish();
	}
}


package com.niit.parttime.ui.job;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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
import com.niit.parttime.entity.Joblist;
import com.niit.parttime.entity.Message;
import com.niit.parttime.ui.BaseActivity;
import com.niit.parttime.ui.LoadingDialog;
import com.niit.slt_andriod_parttime.MainActivity;
import com.niit.slt_andriod_parttime.R;

public class PartTimeJobView extends BaseActivity {
	@ViewInject(R.id.job_theme)
	TextView job_theme;

	@ViewInject(R.id.job_pay)
	TextView job_pay;
	@ViewInject(R.id.job_distance)
	TextView job_distance;
	@ViewInject(R.id.publishTime)
	TextView publishTime;
	@ViewInject(R.id.job_company)
	TextView job_company;
	@ViewInject(R.id.paytime)
	TextView paytime;

	@ViewInject(R.id.job_requestCount)
	TextView job_requestCount;

	@ViewInject(R.id.job_time)
	TextView job_time;
	@ViewInject(R.id.job_address)
	TextView job_address;
	@ViewInject(R.id.inerview_time)
	TextView inerview_time;
	@ViewInject(R.id.inerview_address)
	TextView inerview_address;
	@ViewInject(R.id.job_detailshow)
	TextView job_detailshow;

	@ViewInject(R.id.job_contact)
	TextView job_contact;

	@ViewInject(R.id.job_phone)
	TextView job_phone;

	private LoadingDialog dialog;
	private int userID = 0;
	private String jobID = "0";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jobdetail_layout);
		ViewUtils.inject(this);

		userID = PreferencesUtils.getInt(this, "usersID", 0);
		LogUtils.e(userID + "");
		dialog = new LoadingDialog(this);
		Intent intent = this.getIntent();
		jobID = intent.getStringExtra("jobid");
		LogUtils.e(jobID + "");
		LoadData();
	}

	private void LoadData() {
		HttpUtils http = new HttpUtils();
		String jobURL = String.format(URLs.JOB_VIEW, jobID);
		LogUtils.e(jobURL);
		http.send(HttpRequest.HttpMethod.GET, jobURL,
				new RequestCallBack<String>() {
					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						dialog.show();
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						ObjectMapper mapper = new ObjectMapper();
						if (dialog != null && dialog.isShowing()) {
							dialog.dismiss();
						}
						Message message = null;
						try {
							message = mapper.readValue(responseInfo.result,
									Message.class);
							Log.e("--test---", responseInfo.result);
						} catch (IOException e) {
							e.printStackTrace();
						}
						try {
							Joblist job = mapper.readValue(message.data,
									Joblist.class);
							job_theme.setText(job.jobTitle);
							job_pay.setText(job.jobPayFee);

							job_distance.setText(job.jobPostAddress);

							publishTime.setText(job.jobPostDate);
							job_company.setText(job.jobPostCompany);
							paytime.setText(job.jobJSRQ);
							job_requestCount.setText(job.jobZPRS + "");
							job_time.setText(job.jobGZRQ);
							job_address.setText(job.jobGZDZ);
							inerview_time.setText(job.jobMSSJ);
							inerview_address.setText(job.jobMSDZ);
							job_detailshow.setText(job.jobZWMS);
							job_contact.setText(job.jobContactUser);
							LogUtils.e(job.jobContactUser);
							LogUtils.e(userID + "");
							if (userID > 0) {
								job_phone.setText(job.jobPhone);
							} else {
								job_phone.setText("联系信息请登录后查看。");
							}

						} catch (IOException e) {
							e.printStackTrace();
						}

					}

					@Override
					public void onFailure(HttpException error, String msg) {
						if (dialog != null && dialog.isShowing()) {
							dialog.dismiss();
						}
						showLongToast(3, "信息获取出错");
					}
				});
	}

	@OnClick(R.id.save)
	private void Save(View v) {
		String URL = String.format(URLs.JOB_COLLECTION_ADD, userID, jobID);

		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, URL,
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
						showLongToast(3, "出错了：" + msg);
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfosInfo) {
						if (dialog != null && dialog.isShowing()) {
							dialog.dismiss();

						}
						LogUtils.e("服务器回传结果：" + responseInfosInfo);
						ObjectMapper mapper = new ObjectMapper();
						Message message = null;
						try {
							message = mapper.readValue(
									responseInfosInfo.result, Message.class);
							if (message != null) {
								if (message.res > 0) {
									showLongToast(2, "添加收藏成功");
								} else {
									showLongToast(3, "添加收藏失败");
								}
							} else {
								showLongToast(3, "解析失败");
							}
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

					}
				});
	}

	@OnClick(R.id.callPhone)
	public void callPhone(View v) {
		if (!job_contact.getText().toString().trim().equals("联系信息请登录后查看。")) {
			Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
					+ job_contact.getText().toString().trim()));
			startActivity(intent);
		}
	}

	@OnClick(R.id.back)
	private void Back(View v) {
		IntentUtil.start_activity(PartTimeJobView.this, MainActivity.class);
		PartTimeJobView.this.finish();
	}
}

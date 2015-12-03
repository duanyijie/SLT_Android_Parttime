package com.niit.parttime.ui.job;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import android.R.integer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.leelistview.view.LeeListView;
import com.leelistview.view.LeeListView.IXListViewListener;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.niit.parttime.common.util.IntentUtil;
import com.niit.parttime.common.util.LogUtil;
import com.niit.parttime.common.util.PreferencesUtils;
import com.niit.parttime.config.URLs;
import com.niit.parttime.entity.Joblist;
import com.niit.parttime.entity.Message;
import com.niit.parttime.ui.BaseActivity;
import com.niit.parttime.ui.LoadingDialog;
import com.niit.parttime.ui.users.RegisterActivity;
import com.niit.parttime.ui.users.UsersHome;
import com.niit.slt_andriod_parttime.MainActivity;
import com.niit.slt_andriod_parttime.R;
import com.squareup.picasso.UrlConnectionDownloader;

public class ParttimeJobCollectionList extends BaseActivity implements
		IXListViewListener {

	@ViewInject(R.id.my_listview)
	LeeListView mXListView;

	private PartTimeJobAdapter mAdapter;
	private LoadingDialog dialog;

	private int start = 0;
	private int currentPage = 0;
	private static String refreshTime = "";
	private static String refreshCount = "0";
	private List<Joblist> mDatas = new ArrayList<Joblist>();
	private Handler mHandler;
	private int usersID = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_job_collection_layout);
		ViewUtils.inject(this);

		dialog = new LoadingDialog(this);
		LoadData();
		mAdapter = new PartTimeJobAdapter(this, mDatas);

		usersID = PreferencesUtils.getInt(this, "usersID", 0);
		LogUtils.e(usersID+"");
		mXListView.setAdapter(mAdapter);
		mXListView.setPullLoadEnable(true, "共" + refreshCount + "条数据");
		mXListView.setPullRefreshEnable(true);
		mXListView.setXListViewListener(this);

		mXListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Joblist item = (Joblist) mAdapter.getItem(position - 1);

				BasicNameValuePair pair = new BasicNameValuePair("jobid", item
						.getJobID() + "");
				Log.e("-------test------", item.getJobID() + "");

				IntentUtil.start_activity(ParttimeJobCollectionList.this,
						PartTimeJobView.class, pair);
			}
		});
		mHandler = new Handler();
	}

	/**
	 * 下拉刷新
	 */
	@Override
	public void onRefresh() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				LogUtil.e("-----onRefresh---------", "onRefresh");

				LoadData();

				mAdapter.addAll(mDatas);
				mAdapter.notifyDataSetChanged();// 更新数据
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				refreshTime = sdf.format(new Date());
				refreshCount = mDatas.size() + "";
				// 停止UI刷新
				onLoad();
			}
		}, 0);
	}

	@Override
	public void onLoadMore() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {

				LoadData();
				LogUtil.e("-----onLoadMore---------", "onLoadMore");

				mAdapter.addAll(mDatas);
				mAdapter.notifyDataSetChanged();// 更新数据
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				refreshTime = sdf.format(new Date());
				refreshCount = mDatas.size() + "";
				// 停止UI刷新
				onLoad();
			}
		}, 0);
	}

	private void LoadData() {
		String url = String.format(URLs.JOB_COLLECTION_List, usersID);
		LogUtil.e("LoadData", url);
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.GET, url,
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
						showLongToast(3, "出错：" + msg + "exception:" + exception);

					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						if (dialog != null && dialog.isShowing()) {
							dialog.dismiss();
						}
						// 解析responseInfo
						LogUtils.e("服务器端回传结果" + responseInfo.result);
						ObjectMapper mapper = new ObjectMapper();
						Message message = null;
						try {
							message = mapper.readValue(responseInfo.result,
									Message.class);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (message != null) {
							try {
								List<Joblist> lists = mapper.readValue(
										message.data,
										new TypeReference<List<Joblist>>() {
										});
								mDatas = lists;
								SimpleDateFormat sdf = new SimpleDateFormat(
										"yyyy-MM-dd HH:mm:ss");
								refreshTime = sdf.format(new Date());
								refreshCount = mDatas.size() + "";
								mAdapter.addAll(mDatas);
								mAdapter.notifyDataSetChanged();
								mXListView.stopRefresh();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						} else {
							showLongToast(3, "未解析到对象");
						}
					}
				});
	}

	private void onLoad() {
		mXListView.stopRefresh();
		mXListView.stopLoadMore();
		mXListView.setRefreshTime(refreshTime);
	}

	
	@OnClick(R.id.back)
	private void Back(View v) {
		IntentUtil.start_activity(ParttimeJobCollectionList.this,
				UsersHome.class);
		ParttimeJobCollectionList.this.finish();
	}
}

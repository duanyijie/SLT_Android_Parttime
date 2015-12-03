package com.niit.parttime.ui.job;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.niit.parttime.entity.Joblist;
import com.niit.slt_andriod_parttime.R;

public class PartTimeJobAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<Joblist> mDatas;

	public PartTimeJobAdapter(Context context, List<Joblist> datas) {
		if (datas == null) {
			return;
		}
		this.mDatas = datas;
		mInflater = LayoutInflater.from(context);
	}

	/**
	 * 加载数据
	 * 
	 * @param mDatas
	 */
	public void addAll(List<Joblist> mDatas) {
		if (mDatas == null) {
			return;
		}
		this.mDatas.addAll(mDatas);
	}

	@Override
	public int getCount() {
		return mDatas.size();
	}

	@Override
	public Object getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			// 匹配UI
			convertView = mInflater.inflate(R.layout.adapter_job_item2, null);
			holder = new ViewHolder();

			holder.theme_content = (TextView) convertView
					.findViewById(R.id.theme_content);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.distance = (TextView) convertView
					.findViewById(R.id.distance);
			holder.wage = (TextView) convertView.findViewById(R.id.wage);
			holder.companyName = (TextView) convertView
					.findViewById(R.id.companyName);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// 数据源和UI匹配

		Joblist job = mDatas.get(position);
		holder.theme_content.setText(job.jobTitle);
		holder.time.setText(job.jobPostDate);
		holder.distance.setText(job.jobPostAddress);
		holder.wage.setText(job.jobPayFee);
		holder.companyName.setText(job.jobPostCompany);
		return convertView;
	}

	private final class ViewHolder {
		TextView theme_content;
		TextView time;
		TextView distance;
		TextView wage;
		TextView companyName;
	}

}

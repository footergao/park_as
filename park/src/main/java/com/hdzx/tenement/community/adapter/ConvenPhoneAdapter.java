package com.hdzx.tenement.community.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.hdzx.tenement.R;
import com.hdzx.tenement.community.vo.ConvenPhone;
import com.hdzx.tenement.ui.MainActivity;
import com.hdzx.tenement.ui.common.MyLifeCircleActivity;

import java.util.List;

/**
 * User: hope chen Date: 2015/12/23 Description: 便民服务电话适配器
 */
public class ConvenPhoneAdapter extends BaseExpandableListAdapter {

	private List<String> groupItem;
	private List<List<ConvenPhone>> childtem;
	private LayoutInflater minflater;
	private Activity activity;

	public ConvenPhoneAdapter(List<String> grList,
			List<List<ConvenPhone>> childItem) {
		groupItem = grList;
		this.childtem = childItem;
	}

	public void setInflater(LayoutInflater mInflater, Activity activity) {
		this.minflater = mInflater;
		this.activity = activity;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return null;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		final List<ConvenPhone> tempChild = childtem.get(groupPosition);
		final TextView text;
		if (convertView == null) {
			convertView = minflater.inflate(R.layout.common_conven_phone_child,
					null);
		}
		text = (TextView) convertView.findViewById(R.id.phone_tv);
		text.setText(tempChild.get(childPosition).getPhoneName());
		convertView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				new AlertDialog.Builder(activity)
						.setMessage("确定给"+text.getText()+"拨号？")
						.setNegativeButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {

										Uri uri = Uri.parse("tel:"
												+ tempChild.get(childPosition)
														.getPhoneNum());
										Intent intent = new Intent(
												Intent.ACTION_DIAL, uri);
										activity.startActivity(intent);
									}
								})
						.setPositiveButton("取消",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.cancel();

									}
								}).show();

			}
		});
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return childtem.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return null;
	}

	@Override
	public int getGroupCount() {
		return groupItem.size();
	}

	@Override
	public void onGroupCollapsed(int groupPosition) {
		super.onGroupCollapsed(groupPosition);
	}

	@Override
	public void onGroupExpanded(int groupPosition) {
		super.onGroupExpanded(groupPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return 0;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = minflater.inflate(R.layout.common_conven_phone_group,
					null);
		}
		((CheckedTextView) convertView).setText(groupItem.get(groupPosition));
		((CheckedTextView) convertView).setChecked(isExpanded);
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}
}

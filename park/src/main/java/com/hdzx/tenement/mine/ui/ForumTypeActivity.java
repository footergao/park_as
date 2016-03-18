package com.hdzx.tenement.mine.ui;

import java.util.List;

import com.hdzx.tenement.R;
import com.hdzx.tenement.http.protocol.IContentReportor;
import com.hdzx.tenement.http.protocol.ResponseContentTamplate;
import com.hdzx.tenement.mine.adaper.TxtSimpleAdapter;
import com.hdzx.tenement.mine.vo.BbsForums;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class ForumTypeActivity extends Activity {

	private ListView lst_simple;
	private TxtSimpleAdapter adapter;
	private List<BbsForums> forumlist;
	private int pos = 0;
	private TextView title_tv ;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_simple);
		forumlist = (List<BbsForums>) getIntent().getExtras().getSerializable(
				"list");
		pos = getIntent().getIntExtra("pos", 0);

		title_tv= (TextView) findViewById(R.id.titile_tv);
		title_tv.setText("版块");
		lst_simple = (ListView) findViewById(R.id.lst_simple);
		adapter = new TxtSimpleAdapter(this, forumlist, pos);
		lst_simple.setAdapter(adapter);

		lst_simple.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub

				pos = arg2;
				arg1.findViewById(R.id.img_checked).setVisibility(View.VISIBLE);
				Log.v("gl", "pos send==" + pos);
				Intent intent = new Intent(ForumTypeActivity.this,
						ForumPostsActivity.class);
				intent.putExtra("pos", pos);
				setResult(RESULT_OK, intent);
				finish();

			}
		});

	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.back_iv:
			finish();
			break;
		}
	}

}

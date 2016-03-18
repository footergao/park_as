package com.hdzx.tenement.community.ui;

import java.util.ArrayList;
import java.util.List;
import com.hdzx.tenement.R;
import com.hdzx.tenement.community.adapter.TxtOnlySimpleAdapter;
import com.hdzx.tenement.community.vo.DictionaryType;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class RepairTypeActivity extends Activity{

	private ListView lst_simple;
	List<DictionaryType> list;
	private TxtOnlySimpleAdapter adapter;
	private int pos = 0;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_simple);
		
		lst_simple = (ListView) findViewById(R.id.lst_simple);
		
		pos = getIntent().getIntExtra("pos", -1);
		list = (List<DictionaryType>) getIntent().getSerializableExtra("list");
		
		adapter = new TxtOnlySimpleAdapter(this, list, pos);
		lst_simple.setAdapter(adapter);
		

		lst_simple.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub

				pos = arg2;
				arg1.findViewById(R.id.img_checked).setVisibility(View.VISIBLE);
				Log.v("gl", "pos send==" + pos);
				Intent intent = new Intent(RepairTypeActivity.this,
						CommunityRepairActivity.class);
				intent.putExtra("pos",pos);
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

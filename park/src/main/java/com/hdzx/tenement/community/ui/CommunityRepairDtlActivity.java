package com.hdzx.tenement.community.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hdzx.tenement.R;
import com.hdzx.tenement.common.UserSession;
import com.hdzx.tenement.community.adapter.SuggestDtlAdapter;
import com.hdzx.tenement.community.vo.SuggestReply;
import com.hdzx.tenement.http.protocol.HttpAsyncTask;
import com.hdzx.tenement.http.protocol.HttpRequestEntity;
import com.hdzx.tenement.http.protocol.IContentReportor;
import com.hdzx.tenement.http.protocol.RequestContentTemplate;
import com.hdzx.tenement.http.protocol.ResponseContentTamplate;
import com.hdzx.tenement.mine.adaper.SimpleImgAdapter;
import com.hdzx.tenement.mine.vo.BbsAttachments;
import com.hdzx.tenement.utils.Contants;
import com.hdzx.tenement.utils.DownloadAudio;
import com.hdzx.tenement.utils.MyGridView;
import com.hdzx.tenement.widget.CircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class CommunityRepairDtlActivity extends Activity implements
		IContentReportor {

	//
	private List<SuggestReply> bbsPostsVoList;
	private SuggestDtlAdapter myPostsAdapter;
	private ListView lst_my_posts;
	private String RQ_MY_POSTS = "rq_my_posts";
	private String RQ_MY_REPLY = "rq_my_reply";
	private HttpAsyncTask task = null;
	private String repairId;
	//
	TextView tv_name;
	TextView tv_title;
	TextView tv_time;
	TextView tv_phone;
	MyGridView gridview;
	LinearLayout ll_audio;
	private ImageView back_imageView;
	private ImageView img_audio;
	private Button btn_ok;
	private Button btn_reply;
	List<BbsAttachments> list;
	SimpleImgAdapter adapter ;
	private Context context;
	//
	private AnimationDrawable animationDrawable;
	private MediaPlayer mediaPlayer = null;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.community_suggest_dtl_activity);
		
		context =this;
		lst_my_posts = (ListView) findViewById(R.id.lst_my_posts);
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_time = (TextView) findViewById(R.id.tv_time);
		tv_phone = (TextView) findViewById(R.id.tv_phone);
		gridview = (MyGridView) findViewById(R.id.gridview);
		ll_audio = (LinearLayout) findViewById(R.id.ll_audio);
		img_audio =  (ImageView) findViewById(R.id.img_audio);
		back_imageView = (ImageView) findViewById(R.id.back_imageView);
		btn_ok = (Button) findViewById(R.id.btn_ok);
		btn_ok = (Button) findViewById(R.id.btn_reply);

		//
		String repairIdStr = getIntent().getStringExtra("repairId");
		double repair = Double.parseDouble(repairIdStr);
		repairId = (int) repair+"";

		TextView titleTv = (TextView) this.findViewById(R.id.titile_tv);
		titleTv.setText("报修记录详情");

		
		initAudioAndAnim();
		initData();

	}

	private void initData() {
		// TODO Auto-generated method stub
		list = new ArrayList<BbsAttachments>();
		adapter = new SimpleImgAdapter(this, list);
		gridview.setAdapter(adapter);

		bbsPostsVoList = new ArrayList<SuggestReply>();
		myPostsAdapter = new SuggestDtlAdapter(this, bbsPostsVoList,mediaPlayer,animationDrawable);
		lst_my_posts.setAdapter(myPostsAdapter);

		back_imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		getData(repairId);
	}

	/**
	 * 初始化数据
	 */
	private void getData(String adviceId) {
		// HEARD
		RequestContentTemplate reqContent = new RequestContentTemplate();
		reqContent.setEncryptoType(Contants.CryptoTyepEnum.aes);// 请求使用AES加密

		// BODY
		reqContent.setRequestTicket(true);
		reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.operType.name(),
				"000");
		reqContent.appendData(Contants.PROTOCOL_RESP_BODY.repairId.name(),
				adviceId);

		// SEND
		HttpRequestEntity httpRequestEntity = new HttpRequestEntity(reqContent,
				Contants.SERVER_HOST,
				Contants.PROTOCOL_COMMAND.GET_REPAIR_DTL.getValue());
		httpRequestEntity.setRequestCode(RQ_MY_POSTS);
		httpRequestEntity.setHasData(true);

		httpRequestEntity.setResponseDecryptoType(Contants.CryptoTyepEnum.aes);// 返回使用AES密钥解密
		task = new HttpAsyncTask(this, this);
		task.execute(httpRequestEntity);
	}
	
	

	/**
	 * 初始化数据
	 */
	private void updatedSure(String repairId) {
		// HEARD
		RequestContentTemplate reqContent = new RequestContentTemplate();
		reqContent.setEncryptoType(Contants.CryptoTyepEnum.aes);// 请求使用AES加密

		// BODY
		reqContent.setRequestTicket(true);
		reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.operType.name(),
				"000");
		reqContent.appendData(Contants.PROTOCOL_RESP_BODY.repairId.name(),
				repairId);

		// SEND
		HttpRequestEntity httpRequestEntity = new HttpRequestEntity(reqContent,
				Contants.SERVER_HOST,
				Contants.PROTOCOL_COMMAND.UPDATE_REPAIR_SURE.getValue());
		httpRequestEntity.setRequestCode(RQ_MY_REPLY);
		httpRequestEntity.setHasData(true);

		httpRequestEntity.setResponseDecryptoType(Contants.CryptoTyepEnum.aes);// 返回使用AES密钥解密
		task = new HttpAsyncTask(this, this);
		task.execute(httpRequestEntity);
	}
	


	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.lay_messagecircle_back:
			finish();
			break;
		case R.id.btn_ok:
			updatedSure(repairId);
			
			
			break;
		case R.id.btn_reply:
			Intent intent = new Intent(this,
					CommunitySuggestReplyActivity.class);
			intent.putExtra("replayType", "1");// 消息类型 0:投诉建议，1:设备报修
			intent.putExtra("relationId", repairId);
			intent.putExtra("msgType", "1");// 1享帮帮 2 物业 3 咚咚侠
			startActivityForResult(intent, 0);

			break;
		default:
			break;
		}
	}

	@Override
	public void reportBackContent(ResponseContentTamplate responseContent) {
		// TODO Auto-generated method stub
		String rtnCode = (String) responseContent
				.getInMapHead(Contants.PROTOCOL_RESP_HEAD.rtnCode.name());
		if (rtnCode == null || "".equals(rtnCode)) {
			Toast.makeText(getApplicationContext(), "返回为空", Toast.LENGTH_SHORT)
					.show();
		} else if (!Contants.ResponseCode.CODE_000000.equals(rtnCode)) {
			Toast.makeText(getApplicationContext(),
					responseContent.getErrorMsg(), Toast.LENGTH_SHORT).show();
		} else {

			if (RQ_MY_POSTS.equals(responseContent.getResponseCode())) {
				String jsonStr = responseContent.getDataJson().trim();
				System.out.println("data=" + jsonStr);
				if (jsonStr != null && !"".equals(jsonStr)
						&& jsonStr instanceof String) {

					try {
						JSONObject json = new JSONObject(jsonStr);

						String propertyAdviceEx = json.get("equipmentRepairEx")
								.toString();

						JSONObject property = new JSONObject(propertyAdviceEx);
						String adviceContent = property.get("repairContent")
								.toString();
						String files = property.get("files").toString();
						String adviceTime = property.get("repairTime")
								.toString();

						initProperty(adviceContent, files, adviceTime);

						String data = json.get("msgs").toString();

						Gson gson = new Gson();
						List<SuggestReply> list = gson.fromJson(data,
								new TypeToken<List<SuggestReply>>() {
								}.getType());

						for (SuggestReply post : list) {

							bbsPostsVoList.add(post);
						}

						showInfo();

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}else if(RQ_MY_REPLY.equals(responseContent.getResponseCode())){
				

	            Object data = responseContent.getData();
	            
	            Log.v("gl", "data=="+data);
	            if (data != null && data instanceof Boolean)
	            {
	                Boolean b = (Boolean) data;
	                if (b.booleanValue())
	                {
	                    finish();
	                }
	                else
	                {
	                    Toast.makeText(this, "设置失败，请稍后尝试。", Toast.LENGTH_SHORT).show();
	                }
	            }
	            else
	            {
	                Toast.makeText(this, "服务器异常，请稍后尝试。", Toast.LENGTH_SHORT).show();
	            }
			}
		}
	}

	private void initProperty(String adviceContent, String files,
			String adviceTime) {
		// TODO Auto-generated method stub

		if (adviceContent.equals("") || adviceContent.equals("null"))
			tv_title.setVisibility(View.GONE);
		else
			tv_title.setText(adviceContent);

		if (adviceTime.equals("") || adviceTime.equals("null"))
			tv_time.setVisibility(View.GONE);
		else
			tv_time.setText(adviceTime);

		if (files.equals("") || files.equals("null")) {
//			gridview.setVisibility(View.GONE);
		} else {
			String[] urls = files.split("\\|");
			for (final String attach : urls) {
				Log.v("gl", "attach==" + attach);
				if (attach.contains("jpg") || attach.contains("jpe")
						|| attach.contains("jpeg") || attach.contains("png")
						|| attach.contains("JPEG") || attach.contains("PNG")
						|| attach.contains("JPE") || attach.contains("JPG")) {
					BbsAttachments att = new BbsAttachments();
					att.setFileUrl(attach);
					att.setFileType("1");
					list.add(att);
				} else if (attach.contains("mp4") || attach.contains("mp3")) {

					ll_audio.setVisibility(View.VISIBLE);
					ll_audio.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							
							final String audioUrl =attach;
		        			int index = attach.lastIndexOf("/");
		    				final String name = audioUrl.substring(index+1);
		    				
		    				DownloadAudio down = new DownloadAudio(context, mediaPlayer,animationDrawable, img_audio);
		    				down.downloadVideo(UserSession.getInstance().getImageHost() + audioUrl, name);
						}
					});
				}
			}
			adapter.notifyDataSetChanged();
		}
	}

	private void showInfo() {
		// TODO Auto-generated method stub

		myPostsAdapter.notifyDataSetChanged();

	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode==0){
			list.clear();
			bbsPostsVoList.clear();
			getData(repairId);
		}
	}

	
	class ViewHolder {
		com.hdzx.tenement.widget.CircleImageView img_photo;
		TextView tv_name;
		TextView tv_title;
		TextView tv_time;
		TextView tv_num;
		MyGridView gridview;
		LinearLayout ll_audio;

		public ViewHolder(View view) {
			img_photo = (CircleImageView) view.findViewById(R.id.img_photo);
			tv_name = (TextView) view.findViewById(R.id.tv_name);
			tv_title = (TextView) view.findViewById(R.id.tv_title);
			tv_time = (TextView) view.findViewById(R.id.tv_time);
			tv_num = (TextView) view.findViewById(R.id.tv_num);
			gridview = (MyGridView) view.findViewById(R.id.gridview);
			ll_audio = (LinearLayout) view.findViewById(R.id.ll_audio);
			view.setTag(this);
		}
	}

	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		if (myPostsAdapter!=null&&myPostsAdapter.mediaPlayer != null) {
			myPostsAdapter.mediaPlayer.release();
		}
		
		killMediaPlayer();
	}
	
	private void killMediaPlayer() {
		if (mediaPlayer != null) {
			mediaPlayer.release();
		}
	}
	
	private void initAudioAndAnim() {
		// TODO Auto-generated method stub
		mediaPlayer = new MediaPlayer();
		animationDrawable = (AnimationDrawable) getResources().getDrawable(R.anim.animation_audio);
	}

}
package com.hdzx.tenement.mine.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hdzx.tenement.MyApplication;
import com.hdzx.tenement.R;
import com.hdzx.tenement.common.UserSession;
import com.hdzx.tenement.common.vo.MediaDataHolder;
import com.hdzx.tenement.http.protocol.HttpAsyncTask;
import com.hdzx.tenement.http.protocol.HttpRequestEntity;
import com.hdzx.tenement.http.protocol.IContentReportor;
import com.hdzx.tenement.http.protocol.RequestContentTemplate;
import com.hdzx.tenement.http.protocol.ResponseContentTamplate;
import com.hdzx.tenement.mine.adaper.MyPostsDtlAdapter;
import com.hdzx.tenement.mine.adaper.SimpleImgAdapter;
import com.hdzx.tenement.mine.vo.BbsAttachments;
import com.hdzx.tenement.mine.vo.BbsPosts;
import com.hdzx.tenement.pulltorefresh.library.ILoadingLayout;
import com.hdzx.tenement.pulltorefresh.library.PullToRefreshBase;
import com.hdzx.tenement.pulltorefresh.library.PullToRefreshListView;
import com.hdzx.tenement.utils.CloseKeyBoard;
import com.hdzx.tenement.utils.Contants;
import com.hdzx.tenement.utils.DownloadAudio;
import com.hdzx.tenement.utils.MyGridView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MinePostsDtlActivity extends Activity implements IContentReportor {
	//
	private List<BbsPosts> bbsPostsVoList;
	private List<BbsPosts> bbsPostsVoListThis;
	private MyPostsDtlAdapter myPostsAdapter;
	private com.hdzx.tenement.pulltorefresh.library.PullToRefreshListView lst_my_posts;
	private String RQ_MY_POSTS = "rq_my_posts";
	private String RQ_SEND_MSG = "rq_send_msg";
	private HttpAsyncTask task = null;
	private final int PAGE_SIZE = 20;
	private int pageIndex = 1;
	private String threadId;
	private String postId;
	// 
	private ImageView img_photo;
	private TextView tv_name;
    private TextView tv_title;
    private TextView tv_time;
    private TextView tv_num;
    private MyGridView gridview;
    private LinearLayout ll_audio;
    private ImageView img_audio;
    private String replys;
    private EditText edt_content;
    private TextView tv_send;
    private ImageView back_imageView;
    private Context context;
    //
	private AnimationDrawable animationDrawable;
	private MediaPlayer mediaPlayer = null;
    
    
    Handler handler = new Handler() {
    	  public void handleMessage(Message msg) {
    	   super.handleMessage(msg);
    	   switch (msg.what) {
    	   case 1:
				postId = msg.getData().get("postId").toString();
				String nickName = msg.getData().get("nickName").toString();
				edt_content.setHint("回复  "+nickName+":");
    	   case 2:
       			lst_my_posts.onRefreshComplete();  
    	   default:
    	    break;
    	   }
    	  }
    	 };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.mine_posts_dtl_activity);
		lst_my_posts = (PullToRefreshListView) findViewById(R.id.lst_my_posts);
		img_photo = (ImageView)findViewById(R.id.img_photo);
    	tv_name = (TextView) findViewById(R.id.tv_name);
    	tv_title = (TextView) findViewById(R.id.tv_title);
    	tv_time = (TextView) findViewById(R.id.tv_time);
    	tv_num = (TextView) findViewById(R.id.tv_num);
    	gridview = (MyGridView) findViewById(R.id.gridview);
    	ll_audio = (LinearLayout) findViewById(R.id.ll_audio);
    	img_audio = (ImageView) findViewById(R.id.img_audio);
    	edt_content = (EditText) findViewById(R.id.edt_content);
    	tv_send = (TextView) findViewById(R.id.tv_send);
    	back_imageView= (ImageView) findViewById(R.id.back_imageView);
    	
		threadId = getIntent().getStringExtra("threadId");
//		postId = getIntent().getStringExtra("postId");
		
		context = this;
		
		
		initAudioAndAnim();
		initData();

	}

	private void initData() {
		// TODO Auto-generated method stub
		initIndicator();
		bbsPostsVoList = new ArrayList<BbsPosts>();
		bbsPostsVoListThis = new ArrayList<BbsPosts>();
		
		myPostsAdapter= new MyPostsDtlAdapter(this, bbsPostsVoList,handler);
		lst_my_posts.setAdapter(myPostsAdapter);
		
		lst_my_posts.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
	            @Override
	            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
	            	 
	            	pageIndex = 1;
	            	bbsPostsVoList.clear();
	            	bbsPostsVoListThis.clear();
	                getData(pageIndex);
	            	
	            	
	            }

	            @Override
	            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
	            	
	            	double r = Double.parseDouble(replys);
	            	if(r>pageIndex*PAGE_SIZE){
	            		++pageIndex;
	            		getData(pageIndex);
	            	}else{
	            		Log.v("gl", "onRefreshComplete");
	            		handler.sendEmptyMessage(2);
	            	}
	            }
	        });
		
		tv_send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				if(edt_content.getText()!=null&&!edt_content.getText().toString().trim().equals(""))
				sendReply(threadId, postId, edt_content.getText().toString());
				
				CloseKeyBoard.closeInputKeyBoard(MinePostsDtlActivity.this);
			}
		});
		
		back_imageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		getData(pageIndex);
	}
	
	 /**
     * 初始化数据
     */
    private void getData(int pageIndex) {
        //HEARD
        RequestContentTemplate reqContent = new RequestContentTemplate();
        reqContent.setEncryptoType(Contants.CryptoTyepEnum.aes);//请求使用AES加密

        //BODY
        reqContent.setRequestTicket(true);
        reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.operType.name(), "000");
        reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.pageIndex.name(), pageIndex+"");
        reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.pageSize.name(), PAGE_SIZE+"");
        reqContent.appendData(Contants.PROTOCOL_RESP_BODY.threadId.name(), threadId);

        //SEND
        HttpRequestEntity httpRequestEntity = new HttpRequestEntity(reqContent, Contants.SERVER_HOST, Contants.PROTOCOL_COMMAND.GET_Theme_POST.getValue());
        httpRequestEntity.setRequestCode(RQ_MY_POSTS);
        httpRequestEntity.setHasData(true);
        
        httpRequestEntity.setResponseDecryptoType(Contants.CryptoTyepEnum.aes);//返回使用AES密钥解密
        task = new HttpAsyncTask(this, this);
        task.execute(httpRequestEntity);
    }
    
    
    /**
     * 	type	String	是	类型（1-回复）
		threadId	String	是	主题ID
		postId	String	否	回复的帖子的ID
		content	String	是	内容
     */
    private void sendReply(String threadId,String postId,String content) {
        //HEARD
        RequestContentTemplate reqContent = new RequestContentTemplate();
        reqContent.setEncryptoType(Contants.CryptoTyepEnum.aes);//请求使用AES加密

        //BODY
        reqContent.setRequestTicket(true);
        reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.operType.name(), "000");
        reqContent.appendData(Contants.PROTOCOL_RESP_BODY.postId.name(),postId);
        reqContent.appendData(Contants.PROTOCOL_RESP_BODY.type.name(), "1");
        reqContent.appendData(Contants.PROTOCOL_RESP_BODY.threadId.name(), threadId);
        reqContent.appendData(Contants.PROTOCOL_RESP_BODY.content.name(), content);


        //SEND
        HttpRequestEntity httpRequestEntity = new HttpRequestEntity(reqContent, Contants.SERVER_HOST, Contants.PROTOCOL_COMMAND.SEND_Message.getValue());
        httpRequestEntity.setRequestCode(RQ_SEND_MSG);
        httpRequestEntity.setHasData(true);
        
        httpRequestEntity.setResponseDecryptoType(Contants.CryptoTyepEnum.aes);//返回使用AES密钥解密
        task = new HttpAsyncTask(this, this);
        task.execute(httpRequestEntity);
    }
    

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.lay_messagecircle_back:
			finish();
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
				if (jsonStr != null && !"".equals(jsonStr) && jsonStr instanceof String) {
					
					try {
						JSONObject json = new JSONObject(jsonStr);
						replys= json.get("replys").toString();
						
						String data= json.get("data").toString();
						
						Gson gson = new Gson();
						List<BbsPosts> list = gson.fromJson(data,
								new TypeToken<List<BbsPosts>>() {
								}.getType());
						
						
						for(BbsPosts post:list){
							
							if(post.getType().equals("1")){//回复帖
								
								bbsPostsVoList.add(post);
							}else if(post.getType().equals("0"))
								bbsPostsVoListThis.add(post);//主题贴
						}
						
						showInfo();
						
						showthis();
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}else if(RQ_SEND_MSG.equals(responseContent.getResponseCode())){
				
				
				edt_content.setText("");
				pageIndex = 1;
            	bbsPostsVoList.clear();
            	bbsPostsVoListThis.clear();
                getData(pageIndex);
				
			}
		}
	}

	private void showthis() {
		
		
		if(bbsPostsVoListThis!=null&&bbsPostsVoListThis.size()>0)
			
			for(BbsPosts item:bbsPostsVoListThis){
				// TODO Auto-generated method stub
				tv_name.setText(item.getUserNickName());
		        tv_title.setText(item.getContent());
		        tv_time.setText(item.getCreateDate());
		        tv_num.setText(item.getReplies());
		        
		        if(item.getUserHeadPhoto()!=null&&!item.getUserHeadPhoto().equals(""))
		        	ImageLoader.getInstance().displayImage(
							UserSession.getInstance().getImageHost() + item.getUserHeadPhoto(),
							 img_photo,MyApplication.getInstance().getSimpleOptions());
		        
		        
		        if(item.getAttachmentses()!=null&&item.getAttachmentses().size()>0){
		        	
		        	List<BbsAttachments> list = new ArrayList<BbsAttachments>();
		        	
		        	for(final BbsAttachments attach:item.getAttachmentses()){
		        		
		        		if(attach.getFileType().equals("1")){
		        			list.add(attach);
		        			SimpleImgAdapter adapter  = new SimpleImgAdapter(this, list);
		                	gridview.setAdapter(adapter);
		        		}
		        		else if(attach.getFileType().equals("2")){
		        			
		        			ll_audio.setVisibility(View.VISIBLE);
		        			ll_audio.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View arg0) {
									// TODO Auto-generated method stub
									
									final String audioId = attach.getFileName();
				        			final String audioUrl = attach.getFileUrl();
				        			int index = audioUrl.lastIndexOf(".");
				    				final String suffix = audioUrl.substring(index);
								
									DownloadAudio down = new DownloadAudio(context, mediaPlayer,animationDrawable, img_audio);
				    				down.downloadVideo(UserSession.getInstance().getImageHost() + audioUrl, audioId+ suffix);
								
								}
							});
		        			
		        		}
		        	}
		        }
			}
	}

	private void showInfo() {
		// TODO Auto-generated method stub
		
		myPostsAdapter.notifyDataSetChanged();
		
		lst_my_posts.onRefreshComplete();  
	}
	
	private void initIndicator()
	{
        ILoadingLayout loadingLayout = lst_my_posts.getLoadingLayoutProxy(true, false);
        loadingLayout.setPullLabel("下拉刷新");
        loadingLayout.setRefreshingLabel("加载中……");
        loadingLayout.setReleaseLabel("释放刷新当前画面");

        ILoadingLayout loadingLayout2 = lst_my_posts.getLoadingLayoutProxy(false, true);
        loadingLayout2.setPullLabel("上拉加载更多");
        loadingLayout2.setRefreshingLabel("加载中……");
        loadingLayout2.setReleaseLabel("释放加载更多");
	
}

	private void killMediaPlayer() {
		if (mediaPlayer != null) {
			mediaPlayer.release();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		killMediaPlayer();
	}
	
	private void initAudioAndAnim() {
		// TODO Auto-generated method stub
		mediaPlayer = new MediaPlayer();
		animationDrawable = (AnimationDrawable) getResources().getDrawable(R.anim.animation_audio);
	}
	
}
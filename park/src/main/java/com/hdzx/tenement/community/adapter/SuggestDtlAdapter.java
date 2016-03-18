package com.hdzx.tenement.community.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hdzx.tenement.R;
import com.hdzx.tenement.common.UserSession;
import com.hdzx.tenement.community.vo.SuggestReply;
import com.hdzx.tenement.mine.adaper.SimpleImgAdapter;
import com.hdzx.tenement.mine.vo.BbsAttachments;
import com.hdzx.tenement.utils.DownloadAudio;
import com.hdzx.tenement.utils.MyGridView;

/**
 */
public class SuggestDtlAdapter extends BaseAdapter {

	private List<SuggestReply> postsVoList;

	private Context context;

	public MediaPlayer mediaPlayer = null;
	
	private AnimationDrawable animationDrawable;
	

	public SuggestDtlAdapter(Context context, List<SuggestReply> postsVoList, MediaPlayer mediaPlayer, AnimationDrawable animationDrawable) {
		this.postsVoList = postsVoList;
		this.context = context;
		this.mediaPlayer = mediaPlayer;
		this.animationDrawable = animationDrawable;
	}

	@Override
	public int getCount() {
		return postsVoList.size();
	}

	@Override
	public Object getItem(int position) {
		return postsVoList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		view = View
				.inflate(context, R.layout.tenement_item_suggest_reply, null);

		TextView tv_type;
		TextView tv_title;
		TextView tv_time;
		MyGridView gridview;
		LinearLayout ll_audio;
		final ImageView img_audio;

		tv_title = (TextView) view.findViewById(R.id.tv_title);
		tv_time = (TextView) view.findViewById(R.id.tv_time);
		tv_type = (TextView) view.findViewById(R.id.tv_type);
		gridview = (MyGridView) view.findViewById(R.id.gridview);
		ll_audio = (LinearLayout) view.findViewById(R.id.ll_audio);
		img_audio = (ImageView) view.findViewById(R.id.img_audio);

		img_audio.setTag("gl"+img_audio.getId());
		SuggestReply item = (SuggestReply) getItem(position);

		tv_title.setText(item.getMessage());
		tv_time.setText(item.getReplayTime());

		// 1：用户提交，2：管理员回复 3：咚咚侠回复
		if (item.getMsgType().equals("1"))
			tv_type.setText("用户提交");
		else if (item.getMsgType().equals("2"))
			tv_type.setText("管理员回复");
		else if (item.getMsgType().equals("3"))
			tv_type.setText("咚咚侠回复");

		if (item.getFiles() != null) {
			String[] files = item.getFiles().split("\\|");
			List<BbsAttachments> list = new ArrayList<BbsAttachments>();
			String audioId = "";
			String audioUrl = "";
			for (final String attach : files) {
				if (attach.contains("jpg") || attach.contains("jpe")
						|| attach.contains("jpeg") || attach.contains("png")) {
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

			SimpleImgAdapter adapter = new SimpleImgAdapter(context, list);
			gridview.setAdapter(adapter);
		}

		return view;
	}
}
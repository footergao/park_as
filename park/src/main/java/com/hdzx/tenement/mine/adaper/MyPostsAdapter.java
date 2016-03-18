package com.hdzx.tenement.mine.adaper;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hdzx.tenement.R;
import com.hdzx.tenement.common.UserSession;
import com.hdzx.tenement.mine.vo.BbsAttachments;
import com.hdzx.tenement.mine.vo.BbsPosts;
import com.hdzx.tenement.utils.DownloadAudio;
import com.hdzx.tenement.utils.MyGridView;
import com.hdzx.tenement.utils.MyGridViewCanTouch;
import com.hdzx.tenement.widget.CircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 */
public class MyPostsAdapter extends BaseAdapter {

	private List<BbsPosts> postsVoList;

	private Context context;

	private MediaPlayer mediaPlayer = null;
	
	private AnimationDrawable animationDrawable;

	public MyPostsAdapter(Context context, List<BbsPosts> postsVoList, MediaPlayer mediaPlayer, AnimationDrawable animationDrawable) {
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
		view = View.inflate(context, R.layout.tenement_item_myposts, null);

		TextView tv_name;
		TextView tv_title;
		TextView tv_time;
		TextView tv_num;
		MyGridViewCanTouch gridview;
		LinearLayout ll_audio;
		ImageView img_photo;
		final ImageView img_audio;
		TextView tv_video_time;
		

		img_photo = (CircleImageView) view.findViewById(R.id.img_photo);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		tv_title = (TextView) view.findViewById(R.id.tv_title);
		tv_time = (TextView) view.findViewById(R.id.tv_time);
		tv_num = (TextView) view.findViewById(R.id.tv_num);
		gridview = (MyGridViewCanTouch) view.findViewById(R.id.gridview);
		ll_audio = (LinearLayout) view.findViewById(R.id.ll_audio);
		img_audio= (ImageView) view.findViewById(R.id.img_audio);
		tv_video_time = (TextView) view.findViewById(R.id.tv_video_time);

		BbsPosts item = (BbsPosts) getItem(position);

		ll_audio.setVisibility(View.GONE);
		tv_name.setText(item.getUserNickName());
		tv_title.setText(item.getContent());
		tv_time.setText(item.getCreateDate());
		tv_num.setText(item.getReplies());

		if (item.getUserHeadPhoto() != null
				&& !item.getUserHeadPhoto().equals("")) {
			ImageLoader.getInstance().displayImage(
					UserSession.getInstance().getImageHost()
							+ item.getUserHeadPhoto(), img_photo,
					getSimpleOptions());
		}

		if (item.getAttachmentses() != null
				&& item.getAttachmentses().size() > 0) {

			Log.v("gl", "item.getAttachmentses()" + item.getAttachmentses());
			List<BbsAttachments> list = new ArrayList<BbsAttachments>();

			for (BbsAttachments attach : item.getAttachmentses()) {

				if (attach.getFileType().equals("1")) {
					list.add(attach);
				} else if (attach.getFileType().equals("2")) {
					if(attach.getAttrExtend()!=null&&!attach.getAttrExtend().equals(""))
					tv_video_time.setText(attach.getAttrExtend()+"''");
					
					final String audioId = attach.getFileName();
					final String audioUrl = attach.getFileUrl();
					int index = audioUrl.lastIndexOf(".");
					final String suffix = audioUrl.substring(index);

					ll_audio.setVisibility(View.VISIBLE);
					ll_audio.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							
							
							DownloadAudio down = new DownloadAudio(context, mediaPlayer,animationDrawable, img_audio);
		    				down.downloadVideo(UserSession.getInstance().getImageHost() + audioUrl, audioId+ suffix);
							
						}
					});

				}
			}
			SimpleImgAdapter adapter = new SimpleImgAdapter(context, list);
			gridview.setAdapter(adapter);
		}

		return view;
	}

	public DisplayImageOptions getSimpleOptions() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.default_header)
				.showImageForEmptyUri(R.drawable.default_header)
				.showImageOnFail(R.drawable.default_header).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		return options;

	}
}

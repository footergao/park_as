package com.hdzx.tenement.community.ui;

import java.io.IOException;

import com.hdzx.tenement.R;
import com.hdzx.tenement.photo.Configs;
import com.hdzx.tenement.photo.FileTools;
import com.hdzx.tenement.photo.SelectHeadTools;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MediaSelectedActivity extends Activity implements OnClickListener {

	private Button btn_audio, btn_close_dialog, btn_take_photo,
			btn_gallery_photo;
	
	 private Uri photoUri = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.release_media);

		btn_audio = (Button) findViewById(R.id.btn_audio);
		btn_close_dialog = (Button) findViewById(R.id.btn_close_dialog);
		btn_take_photo = (Button) findViewById(R.id.btn_take_photo);
		btn_gallery_photo = (Button) findViewById(R.id.btn_gallery_photo);
		
		setImagePhoto();
	}
	
	  protected void setImagePhoto(){
			if (!FileTools.hasSdcard()) {
				Toast.makeText(this, "不存在SD卡", Toast.LENGTH_SHORT).show();
				return;
			}
			try {
				photoUri = FileTools.getUriByFileDirAndFileName(
						Configs.SystemPicture.SAVE_DIRECTORY,
						Configs.SystemPicture.SAVE_PIC_NAME);
			} catch (IOException e) {
				Toast.makeText(this, "不能创建图片文件", Toast.LENGTH_SHORT).show();
				return;
			}
		}
	

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub

		switch (view.getId()) {
		case R.id.btn_audio:

			break;
		case R.id.btn_take_photo:
			
			SelectHeadTools.startCamearPicCut(this, photoUri);
			break;
		case R.id.btn_gallery_photo:
			SelectHeadTools.startImageCaptrue(this);
			break;
		case R.id.btn_close_dialog:
			finish();
			break;

		default:
			break;
		}

	}

}

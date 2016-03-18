package com.hdzx.tenement.photo;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

/**
 * Created by APP on 2015/12/16.
 */
public class SelectHeadTools {

    /*****
     * 打开选择框
     * @param context Context  Activity上下文对象
     * @param uri  Uri
     */
    public static void openDialog(final Activity context, final Uri uri){
        new ActionSheetDialog(context)
                .builder()
                .setTitle("选择图片")
                .setCancelable(true)
                .setCanceledOnTouchOutside(true)
                .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Red, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        startCamearPicCut(context,uri);
                    }
                })
                .addSheetItem("从手机相册选择", ActionSheetDialog.SheetItemColor.Red, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        startImageCaptrue(context);
                    }
                })
                .show();
    }

    /****
     * 调用系统的拍照功能
     * @param context Activity上下文对象
     * @param uri  Uri
     */
    public static void startCamearPicCut(Activity context,Uri uri) {
        // 调用系统的拍照功能
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        intent.putExtra("camerasensortype", 2);// 调用前置摄像头
        intent.putExtra("autofocus", true);// 自动对焦
        intent.putExtra("fullScreen", true);// 全屏
        intent.putExtra("showActionIcons", false);
        // 指定调用相机拍照后照片的储存路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        context.startActivityForResult(intent, Configs.SystemPicture.PHOTO_REQUEST_TAKEPHOTO);
    }
    /***
     * 调用系统的图库
     * @param context Activity上下文对象
     */
    public static void startImageCaptrue(Activity context) {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        context.startActivityForResult(intent, Configs.SystemPicture.PHOTO_REQUEST_GALLERY);
    }


    /*****
     * 进行截图
     * @param context Activity上下文对象
     * @param uri  Uri
     * @param size  大小
     */
    public static void startPhotoZoom(Activity context,Uri uri, int size,String photo_name) {
    	Uri photoUri = null;
		try {
			photoUri = FileTools.getUriByFileDirAndFileName(
					Configs.SystemPicture.SAVE_DIRECTORY,
					photo_name);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.v("gl","message==="+e.getMessage());
		}
    	
		
		if(photoUri==null){
			return;
		}
    	Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", Configs.SystemPicture.size);
		intent.putExtra("outputY", Configs.SystemPicture.size);
//		intent.putExtra("return-data", false);// 不返回缩略图
		intent.putExtra("return-data", true);
		intent.putExtra("scale", true);// 缩放
		intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
		
		context.startActivityForResult(intent, Configs.SystemPicture.PHOTO_REQUEST_CUT);
    	
    }
}

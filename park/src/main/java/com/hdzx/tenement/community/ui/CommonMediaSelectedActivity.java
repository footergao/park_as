package com.hdzx.tenement.community.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.PaintDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.*;
import com.hdzx.tenement.R;
import com.hdzx.tenement.common.vo.MediaDataHolder;
import com.hdzx.tenement.community.vo.ServiceBean;
import com.hdzx.tenement.utils.Contants;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.IOException;

public class CommonMediaSelectedActivity extends Activity
{
    private static final int ACTIVITY_REQUEST_CODE_IMAGE = 100;
    
    private static final int ACTIVITY_REQUEST_CODE_GALLERY = 101;
    
    private static final int RESULT_CAPTURE_RECORDER_SOUND = 102;
    
    private static final int REQUEST_CODE_TAKE_VIDEO = 103;
    
    private static final String IMAGE_FILE_NAME = "33322222.jpg";
    
    private TextView titleTv = null;
    
    private PopupWindow mediaPopupWindow = null;
    
    private PopupWindow imagePopupWindow = null;
    
    private ImageView mediaIv = null;
    
    private MediaDataHolder mediaDataHolder = null;
    
    private DisplayImageOptions displayImageOptions = null;
    
    private Button recorddingButton = null;
    
    private MediaRecorder mediaRecorder = null;
    
    private MediaPlayer mediaPlayer = null;
    
    private ServiceBean serviceBean = null;
    
    private EditText contentText = null;
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.common_media_layout);
        
        Intent intent = this.getIntent();
        if (intent != null)
        {
            serviceBean = (ServiceBean) intent.getSerializableExtra("ServiceBean");
            mediaDataHolder = (MediaDataHolder) intent.getParcelableExtra("MediaDataHolder");
        }
        
        initDisplayImageOptions();
        initView();
    }
    
    private void initDisplayImageOptions()
    {
         displayImageOptions = new DisplayImageOptions.Builder().build();
    }
    
    private void initView()
    {
        initCommonView();
        displayImage();
    }
    
    private void initCommonView()
    {
        titleTv = (TextView) this.findViewById(R.id.titile_tv);
        if (serviceBean != null)
        {
            titleTv.setText(serviceBean.getServiceName());
        }
        
        contentText = (EditText) this.findViewById(R.id.context_text);
        if (mediaDataHolder != null)
        {
            contentText.setText(mediaDataHolder.getText());
        }
        
        ImageView backIv = (ImageView) this.findViewById(R.id.back_iv);
        backIv.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                onBackPressed();
            }
            
        });
        
        mediaIv = (ImageView) this.findViewById(R.id.media_iv);
        mediaIv.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                if (mediaDataHolder == null)
                {
                    if (mediaPopupWindow == null)
                    {
                        initMediaPopupWindow();
                    }
                    WindowManager.LayoutParams params = getWindow().getAttributes();
                    params.alpha = 0.5f;
                    getWindow().setAttributes(params);
                    mediaPopupWindow.setOutsideTouchable(true);
                    
                    mediaPopupWindow.showAtLocation(titleTv, Gravity.BOTTOM|Gravity.LEFT, 0, 0);
                }
                else
                {
                    if (mediaDataHolder.getUri() == null)
                    {
                        if (mediaPopupWindow == null)
                        {
                            initMediaPopupWindow();
                        }
                        WindowManager.LayoutParams params = getWindow().getAttributes();
                        params.alpha = 0.5f;
                        getWindow().setAttributes(params);
                        mediaPopupWindow.setOutsideTouchable(true);
                        
                        mediaPopupWindow.showAtLocation(titleTv, Gravity.BOTTOM|Gravity.LEFT, 0, 0);
                    }
                    else if (mediaDataHolder.getType() == Contants.MEDIA_TYPE.AUDIO)
                    {
                        playAudio(mediaDataHolder.getUri().toString());
                    }
                    else if (mediaDataHolder.getType() == Contants.MEDIA_TYPE.VEDIO)
                    {
                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
                        intent.setDataAndType(mediaDataHolder.getUri(), "video/mp4");
                        startActivity(intent);
                    }
                    else if (mediaDataHolder.getType() == Contants.MEDIA_TYPE.IMAGE)
                    {
                        Intent intent = new Intent(CommonMediaSelectedActivity.this, CommonImageViewActivity.class);
                        intent.putExtra("uri", mediaDataHolder.getUri());
                        startActivity(intent);
                    }
                }
            }
            
        });
        
        mediaIv.setOnLongClickListener(new OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                if (mediaDataHolder == null)
                {
                    return false;
                }
                else
                {
                    new AlertDialog.Builder(CommonMediaSelectedActivity.this)
                    .setMessage("是否删除？")
                    .setNegativeButton("确定", new DialogInterface.OnClickListener()
                    {
                        
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            deleteMediaDataHolder();
                            displayImage();
                        }
                    })
                    .setPositiveButton("取消", null)
                    .show();
                    
                    return true;
                }
            }
            
        });

        
        recorddingButton = (Button) this.findViewById(R.id.recording_button);
        recorddingButton.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                if (mediaRecorder != null)
                {
                    mediaRecorder.stop();
                    recorddingButton.setVisibility(View.GONE);
                    displayImage();
                }
            }
            
        });
        
        Button submit = (Button) this.findViewById(R.id.submit_btn);
        submit.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (contentText.getText() != null && !"".equals(contentText.getText().toString().trim()))
                {
                    if (mediaDataHolder == null)
                    {
                        mediaDataHolder = new MediaDataHolder();
                    }
                    
                    mediaDataHolder.setText(contentText.getText().toString().trim());
                }
                else
                {
                    if (mediaDataHolder == null)
                    {
                        Toast.makeText(CommonMediaSelectedActivity.this, "请输入文本、图片、声音或者视屏。", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                
                Intent data = new Intent();
                data.putExtra("MediaDataHolder", mediaDataHolder);
                setResult(Activity.RESULT_OK, data);
                finish();
            }
        });
    }
    
    private void displayImage()
    {
        if (mediaDataHolder != null)
        {
            if (mediaDataHolder.getType() == Contants.MEDIA_TYPE.IMAGE)
            {
                ImageLoader.getInstance().displayImage(mediaDataHolder.getUri().toString(), mediaIv, displayImageOptions);
            }
            else if (mediaDataHolder.getType() == Contants.MEDIA_TYPE.AUDIO)
            {
                mediaIv.setImageResource(R.drawable.audio);
            }
            else if (mediaDataHolder.getType() == Contants.MEDIA_TYPE.VEDIO)
            {
                mediaIv.setImageResource(R.drawable.vedio);
            }
        }
        else
        {
            mediaIv.setImageResource(R.drawable.add_media);
        }
    }
    
    private void initMediaRecorder()
    {
        if (mediaRecorder == null)
        {
            mediaRecorder = new MediaRecorder();
            // 第1步：设置音频来源（MIC表示麦克风）  
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            //第2步：设置音频输出格式（默认的输出格式）  
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            //第3步：设置音频编码方式（默认的编码方式）  
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        }
    }
    
    private void killMediaRecorder()
    {
        if (mediaRecorder == null)
        {
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }
    
    private void createImageMediaData(Uri uri, boolean isDelete)
    {
        if (mediaDataHolder == null)
        {
            mediaDataHolder = new MediaDataHolder();
        }
        
        mediaDataHolder.setType(Contants.MEDIA_TYPE.IMAGE);
        mediaDataHolder.setUri(uri);
        mediaDataHolder.setDelete(isDelete);
    }
    
    private void createAudioMediaData(Uri uri)
    {
        if (mediaDataHolder == null)
        {
            mediaDataHolder = new MediaDataHolder();
        }
        
        mediaDataHolder.setType(Contants.MEDIA_TYPE.AUDIO);
        mediaDataHolder.setUri(uri);
        mediaDataHolder.setDelete(true);
    }
    
    private void createVedioMediaData(Uri uri)
    {
        if (mediaDataHolder == null)
        {
            mediaDataHolder = new MediaDataHolder();
        }
        
        mediaDataHolder.setType(Contants.MEDIA_TYPE.VEDIO);
        mediaDataHolder.setUri(uri);
        mediaDataHolder.setDelete(true);
    }
    
    protected void initMediaPopupWindow()
    {
        View view = getLayoutInflater().inflate(R.layout.common_media_selected_pop, null);
        mediaPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mediaPopupWindow.setBackgroundDrawable(new PaintDrawable(0));
        mediaPopupWindow.setFocusable(true);
        mediaPopupWindow.setOutsideTouchable(true);

        mediaPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener()
        {

            @Override
            public void onDismiss()
            {
                if (mediaPopupWindow != null)
                {
                    // 设置透明度（这是窗体本身的透明度，非背景）
                    // alpha在0.0f到1.0f之间。1.0完全不透明，0.0f完全透明
                    WindowManager.LayoutParams params = getWindow().getAttributes();
                    params.alpha = 1.0f;
                    getWindow().setAttributes(params);
                }
            }
        });

        Button imageButton = (Button) view.findViewById(R.id.media_image_btn);
        Button soundButton = (Button) view.findViewById(R.id.media_sound_btn);
        Button vedioButton = (Button) view.findViewById(R.id.media_vedio_btn);
        imageButton.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                mediaPopupWindow.dismiss();
                if (imagePopupWindow == null)
                {
                    initImagePopupWindow();
                }
                WindowManager.LayoutParams params = getWindow().getAttributes();
                params.alpha = 0.5f;
                getWindow().setAttributes(params);
                imagePopupWindow.setOutsideTouchable(true);
                
                imagePopupWindow.showAtLocation(titleTv, Gravity.BOTTOM|Gravity.LEFT, 0, 0);
            }
        });
        
        soundButton.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                mediaPopupWindow.dismiss();
                Toast.makeText(CommonMediaSelectedActivity.this, "录音已开始，请对准麦克风讲话 ", Toast.LENGTH_SHORT).show();
                initMediaRecorder();
                
                try
                {
                    //创建一个临时的音频输出文件  
                    File audioFile = File.createTempFile("record_", ".amr");
                    //recordUri = Uri.fromFile(audioFile);
                    
                    createAudioMediaData(Uri.fromFile(audioFile));
                    
                    //第4步：指定音频输出文件  
                    mediaRecorder.setOutputFile(audioFile.getAbsolutePath());  
                    //第5步：调用prepare方法  
                    mediaRecorder.prepare();  
                    //第6步：调用start方法开始录音  
                    mediaRecorder.start();
                    
                    recorddingButton.setVisibility(View.VISIBLE);
                }
                catch (IOException e)
                {
                    killMediaRecorder();
                    e.printStackTrace();
                }
            }
        });
        
        vedioButton.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                mediaPopupWindow.dismiss();
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
                startActivityForResult(intent, REQUEST_CODE_TAKE_VIDEO);
            }
        });
    }
    
    
    protected void initImagePopupWindow()
    {
        View view = getLayoutInflater().inflate(R.layout.tenement_main_image_selector, null);
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);

        imagePopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        imagePopupWindow.setBackgroundDrawable(new PaintDrawable(0));

        imagePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener()
        {

            @Override
            public void onDismiss()
            {
                if (imagePopupWindow != null)
                {
                    // 设置透明度（这是窗体本身的透明度，非背景）
                    // alpha在0.0f到1.0f之间。1.0完全不透明，0.0f完全透明
                    WindowManager.LayoutParams params = getWindow().getAttributes();
                    params.alpha = 1.0f;
                    getWindow().setAttributes(params);
                }
            }
        });

        Button taking_pictures = (Button) view.findViewById(R.id.taking_pictures);
        Button gallery = (Button) view.findViewById(R.id.select_from_photo);
        Button cancle = (Button) view.findViewById(R.id.add_cancel);
        taking_pictures.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                photo();
                imagePopupWindow.dismiss();
            }
        });

        gallery.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                // 相册
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, ACTIVITY_REQUEST_CODE_GALLERY);
                imagePopupWindow.dismiss();
            }
        });

        cancle.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                imagePopupWindow.dismiss();
            }
        });
    }
    
    protected void photo()
    {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED))
        {
            Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME)));

            openCameraIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);

            startActivityForResult(openCameraIntent, ACTIVITY_REQUEST_CODE_IMAGE);
        }
        else
        {
            Toast.makeText(this, "没有储存卡", Toast.LENGTH_LONG).show();
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode != RESULT_OK)
        {
            return;
        }
        else
        {
            Cursor cursor = null;
            switch (requestCode)
            {
                case ACTIVITY_REQUEST_CODE_IMAGE:
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
                    {
                        File tempFile = new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME);
                        createImageMediaData(Uri.fromFile(tempFile), true);
                        displayImage();
                    }
                    else
                    {
                        Toast.makeText(this, "未找到存储卡，无法存储照片！", Toast.LENGTH_LONG).show();
                    }
                    break;
                    
                case ACTIVITY_REQUEST_CODE_GALLERY:
                    cursor = this.getContentResolver().query(data.getData(), null, null, null, null);
                    if (cursor.moveToNext())
                    {
                        String strImagePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                        System.out.println("strImagePath=" + strImagePath);
                        createImageMediaData(Uri.fromFile(new File(strImagePath)), false);
                        displayImage();
                    }
                    
                    break;
                    
                    
                case REQUEST_CODE_TAKE_VIDEO:
                    System.out.println("vedioUri=" + data.getData());
                    cursor = this.getContentResolver().query(data.getData(), null, null, null, null);
                    if (cursor.moveToNext())
                    {
                        String strVideoPath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                        System.out.println("strVideoPath=" + strVideoPath);
                        createVedioMediaData(Uri.fromFile(new File(strVideoPath)));
                        displayImage();
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    
    private void killMediaPlayer()
    {
        if (mediaPlayer != null)
        {
            mediaPlayer.release();
        }
    }
    
    private void playAudio(String url)
    {
        killMediaPlayer();
        try
        {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(url);
            mediaPlayer.setWakeMode(this, PowerManager.PARTIAL_WAKE_LOCK);
            mediaPlayer.prepare();
            mediaPlayer.start();
        }
        catch (IllegalArgumentException e)
        {
            e.printStackTrace();
        }
        catch (SecurityException e)
        {
            e.printStackTrace();
        }
        catch (IllegalStateException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    private void deleteMediaDataHolder()
    {
        if (mediaDataHolder != null)
        {
            if (mediaDataHolder.isDelete())
            {
                File f = new File(mediaDataHolder.getUri().getPath());
                if (f.exists())
                {
                    f.delete();
                }
            }
            
            mediaDataHolder = null;
        }
    }
    
    protected void onDestroy()
    {
        super.onDestroy();
        killMediaPlayer();
    }
    
    public void onBackPressed()
    {
        deleteMediaDataHolder();
        super.onBackPressed();
    }
    
}

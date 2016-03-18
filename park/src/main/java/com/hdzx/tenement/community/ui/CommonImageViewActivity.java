package com.hdzx.tenement.community.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import com.hdzx.tenement.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class CommonImageViewActivity extends Activity
{
    private Uri uri = null;
    
    private ImageView imageView = null;
    
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.common_image_view_layout);
        
        imageView = (ImageView) this.findViewById(R.id.imageView);
        
        Intent intent = this.getIntent();
        if (intent != null)
        {
            uri = (Uri) intent.getParcelableExtra("uri");
            DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder().build();
            ImageLoader.getInstance().displayImage(uri.toString(), imageView, displayImageOptions);
        }
    }
}

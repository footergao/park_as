package com.hdzx.tenement.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.*;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.DisplayMetrics;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 
 * @author Jesley
 * 
 */
public class ImageUtil
{
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx)
	{
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Bitmap output = Bitmap.createBitmap(w, h, Config.ARGB_8888);

		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, w, h);
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);

		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));

		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
		/* int width = bitmap.getWidth();
         int height = bitmap.getHeight();
         float roundPx;
         float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
         if (width <= height) {
                 roundPx = width / 2;
                 top = 0;
                 bottom = width;
                 left = 0;
                 right = width;
                 height = width;
                 dst_left = 0;
                 dst_top = 0;
                 dst_right = width;
                 dst_bottom = width;
         } else {
                 roundPx = height / 2;
                 float clip = (width - height) / 2;
                 left = clip;
                 right = width - clip;
                 top = 0;
                 bottom = height;
                 width = height;
                 dst_left = 0;
                 dst_top = 0;
                 dst_right = height;
                 dst_bottom = height;
         }

         Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
         Canvas canvas = new Canvas(output);

         final int color = 0xff424242;
         final Paint paint = new Paint();
         final Rect src = new Rect((int) left, (int) top, (int) right,
                         (int) bottom);
         final Rect dst = new Rect((int) dst_left, (int) dst_top,
                         (int) dst_right, (int) dst_bottom);
         final RectF rectF = new RectF(dst);

         paint.setAntiAlias(true);

         canvas.drawARGB(0, 0, 0, 0);
         paint.setColor(color);
         canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

         paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
         canvas.drawBitmap(bitmap, src, dst, paint);
         return output;*/
		
		 /* Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
	        Canvas canvas = new Canvas(output);

	        final Paint paint = new Paint();
	        //保证是方形，并且从中心画
	        int width = bitmap.getWidth();
	        int height = bitmap.getHeight();
	        int w;
	        int deltaX = 0;
	        int deltaY = 0;
	        if (width <= height) {
	            w = width;
	            deltaY = height - w;
	        } else {
	            w = height;
	            deltaX = width - w;
	        }
	        final Rect rect = new Rect(deltaX, deltaY, w, w);
	        final RectF rectF = new RectF(rect);

	        paint.setAntiAlias(true);
	        canvas.drawARGB(0, 0, 0, 0);
	        //圆形，所有只用一个
	        
	        int radius = (int) (Math.sqrt(w * w * 2.0d) / 2);
	        canvas.drawRoundRect(rectF, radius, radius, paint);

	        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	        canvas.drawBitmap(bitmap, rect, rect, paint);
	        return output;*/
	}

	public static Bitmap drawableToBitmap(Drawable drawable)
	{
		// 取 drawable 的长宽
		int w = drawable.getIntrinsicWidth();
		int h = drawable.getIntrinsicHeight();

		// 取 drawable 的颜色格式
		Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888 : Config.RGB_565;
		
		// 建立对应 bitmap
		Bitmap bitmap = Bitmap.createBitmap(w, h, config);
		// 建立对应 bitmap 的画布
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, w, h);
		// 把 drawable 内容画到画布中
		drawable.draw(canvas);
		return bitmap;
	}

    public static Bitmap getNetBitmap(String address) {
        Bitmap bitmap = null;
        try {
            //通过代码 模拟器浏览器访问图片的流程
            URL imageUrl = new URL(address);
            HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3000);
            conn.setInstanceFollowRedirects(true);
            //获取服务器返回回来的流
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * (缩放)重绘图片
     *
     * @param context
     *            Activity
     * @param bitmap
     * @return
     */
    public static Bitmap reDrawBitMap(Activity context, Bitmap bitmap) {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int rWidth = dm.widthPixels;
        int width = bitmap.getWidth();
        float zoomScale;

        /** 方式3 **/
        if (width >= rWidth)
            zoomScale = ((float) rWidth) / width;
        else
            zoomScale = 1.0f;
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 缩放图片动作
        matrix.postScale(zoomScale, zoomScale);
        return Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /**
     * 写图片文件 在Android系统中，文件保存在 /data/data/PACKAGE_NAME/files 目录下
     *
     * @throws java.io.IOException
     */
    public static void saveImage(Context context, String fileName, Bitmap bitmap)
            throws IOException {
        saveImage(context, fileName, bitmap, 100);
    }

    public static void saveImage(Context context, String fileName,
                                 Bitmap bitmap, int quality) throws IOException {
        if (bitmap == null || fileName == null || context == null)
            return;

        FileOutputStream fos = context.openFileOutput(fileName,
                Context.MODE_PRIVATE);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);
        byte[] bytes = stream.toByteArray();
        fos.write(bytes);
        fos.close();
    }

    /**
     * 写图片文件到SD卡
     *
     * @throws java.io.IOException
     */
    public static void saveImageToSD(Context ctx, String filePath,
                                     Bitmap bitmap, int quality) throws IOException {
        if (bitmap != null) {
            File file = new File(filePath.substring(0,
                    filePath.lastIndexOf(File.separator)));
            if (!file.exists()) {
                file.mkdirs();
            }
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(filePath));
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bos);
            bos.flush();
            bos.close();
            if (ctx != null) {
                scanPhoto(ctx, filePath);
            }
        }
    }

    /**
     * 让Gallery上能马上看到该图片
     */
    private static void scanPhoto(Context ctx, String imgFileName) {
        Intent mediaScanIntent = new Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(imgFileName);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        ctx.sendBroadcast(mediaScanIntent);
    }

    /**
     * 获取bitmap
     *
     * @param context
     * @param fileName
     * @return
     */
    public static Bitmap getBitmap(Context context, String fileName) {
        FileInputStream fis = null;
        Bitmap bitmap = null;
        try {
            fis = context.openFileInput(fileName);
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inSampleSize = 4;
            bitmap = BitmapFactory.decodeStream(fis, null, opts);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (Exception e) {
            }
        }
        return bitmap;
    }
}

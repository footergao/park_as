package com.hdzx.tenement.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.provider.Settings.Secure;
import android.util.DisplayMetrics;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hdzx.tenement.common.vo.AdvertisementVO;
import com.hdzx.tenement.http.protocol.HttpAsyncTask;
import com.hdzx.tenement.http.protocol.HttpRequestEntity;
import com.hdzx.tenement.http.protocol.IContentReportor;
import com.hdzx.tenement.http.protocol.RequestContentTemplate;
import com.hdzx.tenement.utils.Contants.CryptoTyepEnum;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.UUID;

public class CommonUtil
{
    public static String uuid = null;
    
    private static final String INSTALLATION = "installation.dat";
    
    public static String appVersion = null;
    
    public static void initAppVersion(Context context)
    {
        if (appVersion == null)
        {
            try
            {
                appVersion = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            }
            catch (NameNotFoundException e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * 返回该设备在此程序上的唯一标识符。
     * 
     * @param context
     *            Context对象。
     * @return 表示该设备在此程序上的唯一标识符。
     */
    public synchronized static void initUUID(Context context)
    {
        if (uuid == null)
        {
            //File installation = new File(context.getFilesDir(), INSTALLATION);
            File dir = Environment.getExternalStoragePublicDirectory(".conif/" + context.getPackageName());
            File installation = new File(dir, INSTALLATION);
            try
            {
                if (!dir.exists())
                {
                    dir.mkdirs();
                }
                
                if (!installation.exists())
                {
                    writeInstallationFile(context, installation);
                }
                uuid = readInstallationFile(installation);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * 将表示此设备在该程序上的唯一标识符写入程序文件系统中。
     * 
     * @param installation
     *            保存唯一标识符的File对象。
     * @return 唯一标识符。
     * @throws IOException
     *             IO异常。
     */
    private static String readInstallationFile(File installation) throws IOException
    {
        RandomAccessFile accessFile = new RandomAccessFile(installation, "r");
        byte[] bs = new byte[(int) accessFile.length()];
        accessFile.readFully(bs);
        accessFile.close();
        return new String(bs);
    }

    /**
     * 读出保存在程序文件系统中的表示该设备在此程序上的唯一标识符。
     * 
     * @param context
     *            Context对象。
     * @param installation
     *            保存唯一标识符的File对象。
     * @throws IOException
     *             IO异常。
     */
    private static void writeInstallationFile(Context context, File installation) throws IOException
    {
        FileOutputStream out = new FileOutputStream(installation);
        String uuid = UUID.nameUUIDFromBytes(Secure.getString(context.getContentResolver(), Secure.ANDROID_ID).getBytes()).toString();
        Log.i("CommonUtil", uuid);
        out.write(uuid.getBytes());
        out.close();
    }

    public static String getUUID()
    {
        String uuidStr = UUID.randomUUID().toString();
        System.out.println("uuid=" + uuidStr);
        uuidStr = uuidStr.substring(0, 8) + uuidStr.substring(9, 13) + uuidStr.substring(14, 18) + uuidStr.substring(19, 23) + uuidStr.substring(24);
        System.out.println("uuid2=" + uuidStr);

        return uuidStr;
    }
    
    public static HttpAsyncTask makeAdvertisementRquest(Context context, String advertiseId, IContentReportor callback)
    {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        RequestContentTemplate reqContent = new RequestContentTemplate();
        reqContent.setEncryptoType(CryptoTyepEnum.aes);

        reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.appAdvertiseId.name(), advertiseId);
        reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.resolution.name(), displayMetrics.heightPixels + "*" + displayMetrics.widthPixels);
        
        HttpRequestEntity httpRequestEntity = new HttpRequestEntity(reqContent, Contants.SERVER_HOST, Contants.PROTOCOL_COMMAND.GET_ADVERTISEMENT.getValue());
        httpRequestEntity.setResponseDecryptoType(CryptoTyepEnum.aes);
        HttpAsyncTask task = new HttpAsyncTask(context, callback);
        task.execute(httpRequestEntity);
        
        return task;
    }
    
    public static AdvertisementVO parseMap2AdvertisementVO(String dataJsonStr)
    {
        AdvertisementVO vo = null;
        Gson gson = new Gson();
        try
        {
            vo = gson.fromJson(dataJsonStr, new TypeToken<AdvertisementVO>(){}.getType());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        return vo;
    }
    
    public static String hiden4CharBefor4(String s, String anch)
    {
        String ret = null;
        if (s != null)
        {
            int len = s.length();
            if (len > 4)
            {
                StringBuffer sb = new StringBuffer();
                if (len > 8)
                {
                    sb.append(s.substring(0, len - 8));
                    for (int i = 0; i < 4; i++)
                    {
                        sb.append(anch);
                    }
                }
                else
                {
                    for (int i = 0; i < len - 4; i++)
                    {
                        sb.append(anch);
                    }
                }
                
                sb.append(s.subSequence(len - 4, len));
                ret = sb.toString();
            }
            else
            {
                ret = s;
            }
        }
        
        return ret;
    }
    
    public static ProgressDialog showProgressDialog(Context context, String message)
    {
        // 创建ProgressDialog对象
        ProgressDialog progressDialog = new ProgressDialog(context);
        // 设置进度条风格，风格为圆形，旋转的
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // 设置ProgressDialog提示信息
        progressDialog.setMessage(message);

        // 设置ProgressDialog标题图标
        // pDialog.setIcon(R.drawable.img1);

        // 设置ProgressDialog 的进度条是否不明确 false 就是不设置为不明确
        progressDialog.setIndeterminate(false);

        // 设置ProgressDialog 是否可以按退回键取消
        progressDialog.setCancelable(false);

        // 让ProgressDialog显示
        progressDialog.show();
        
        return progressDialog;
    }
    
    public static void closeProgressDialog(ProgressDialog progressDialog)
    {
        if (progressDialog != null && progressDialog.isShowing())
        {
            progressDialog.dismiss();
        }
    }
    
    public static String getSexFromCode(String code)
    {
        String sex = null;
        if ("0".equals(code))
        {
            sex = "保密";
        }
        else if ("1".equals(code))
        {
            sex = "男";
        }
        else if ("2".equals(code))
        {
            sex = "女";
        }
        
        return sex;
    }
    
}

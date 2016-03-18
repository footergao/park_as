package com.hdzx.tenement.utils;

import android.content.Context;
import android.util.Log;

import java.lang.reflect.Field;

public class ResUtil {

    private static ResUtil instance;
    private Context context;
    private static String TAG = "KFResUtil";
    private static Class id = null;
    private static Class drawable = null;
    private static Class layout = null;
    private static Class anim = null;
    private static Class style = null;
    private static Class string = null;
    private static Class array = null;
    private static Class raw = null;
    private static Class color = null;
    private static Class dimen = null;
    private static Class styleable = null;

    public static void init(Context context) {
        if (instance == null)
            instance = new ResUtil(context);
    }

    private ResUtil(Context paramContext) {
        this.context = paramContext.getApplicationContext();
        try {
            drawable = Class.forName(this.context.getPackageName() + ".R$drawable");
        } catch (ClassNotFoundException localClassNotFoundException1) {
            Log.d(TAG, localClassNotFoundException1.getMessage());
        }
        try {
            layout = Class.forName(this.context.getPackageName() + ".R$layout");
        } catch (ClassNotFoundException localClassNotFoundException2) {
            Log.d(TAG, localClassNotFoundException2.getMessage());
        }
        try {
            id = Class.forName(this.context.getPackageName() + ".R$id");
        } catch (ClassNotFoundException localClassNotFoundException3) {
            Log.d(TAG, localClassNotFoundException3.getMessage());
        }
        try {
            anim = Class.forName(this.context.getPackageName() + ".R$anim");
        } catch (ClassNotFoundException localClassNotFoundException4) {
            Log.d(TAG, localClassNotFoundException4.getMessage());
        }
        try {
            style = Class.forName(this.context.getPackageName() + ".R$style");
        } catch (ClassNotFoundException localClassNotFoundException5) {
            Log.d(TAG, localClassNotFoundException5.getMessage());
        }
        try {
            string = Class.forName(this.context.getPackageName() + ".R$string");
        } catch (ClassNotFoundException localClassNotFoundException6) {
            Log.d(TAG, localClassNotFoundException6.getMessage());
        }
        try {
            array = Class.forName(this.context.getPackageName() + ".R$array");
        } catch (ClassNotFoundException localClassNotFoundException7) {
            Log.d(TAG, localClassNotFoundException7.getMessage());
        }
        try {
            raw = Class.forName(this.context.getPackageName() + ".R$raw");
        } catch (ClassNotFoundException localClassNotFoundException8) {
            Log.d(TAG, localClassNotFoundException8.getMessage());
        }
        try {
            color = Class.forName(this.context.getPackageName() + ".R$color");
        } catch (ClassNotFoundException localClassNotFoundException8) {
            Log.d(TAG, localClassNotFoundException8.getMessage());
        }
        try {
            dimen = Class.forName(this.context.getPackageName() + ".R$dimen");
        } catch (ClassNotFoundException localClassNotFoundException8) {
            Log.d(TAG, localClassNotFoundException8.getMessage());
        }

        try {
            styleable = Class.forName(this.context.getPackageName() + ".R$styleable");
        } catch (ClassNotFoundException localClassNotFoundException8) {
            Log.d(TAG, localClassNotFoundException8.getMessage());
        }
    }

    public static ResUtil getResofR(Context paramContext) {
        if (instance == null)
            instance = new ResUtil(paramContext);
        return instance;
    }

    public int getAnim(String paramString) {
        return getResofR(anim, paramString);
    }

    public int getDimen(String paramString) {
        return getResofR(dimen, paramString);
    }

    public int getId(String paramString) {
        return getResofR(id, paramString);
    }

    public int getColor(String paramString) {
        return getResofR(color, paramString);
    }

    public int getDrawable(String paramString) {
        return getResofR(drawable, paramString);
    }

    public int getLayout(String paramString) {
        return getResofR(layout, paramString);
    }

    public int getStyle(String paramString) {
        return getResofR(style, paramString);
    }

    public int getString(String paramString) {
        return getResofR(string, paramString);
    }

    public int getArray(String paramString) {
        return getResofR(array, paramString);
    }

    public int getRaw(String paramString) {
        return getResofR(raw, paramString);
    }

    public int getStyleablei(String param) {
        return getResofR(styleable, param);
    }

    public int[] getStyleable(String param) {
        if (styleable == null) {
            Log.d(TAG, "getRes(null," + param + ")");
            throw new IllegalArgumentException("ResClass is not initialized.");
        }

        try {
            Field localField = styleable.getField(param);
            localField.setAccessible(true);
            int[] k = (int[]) localField.get(null);
            return k;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return null;
    }

    private int getResofR(Class<?> paramClass, String paramString) {
        if (paramClass == null) {
            Log.d(TAG, "getRes(null," + paramString + ")");
            throw new IllegalArgumentException("ResClass is not initialized.");
        }

        int k;
        try {
            Field localField = paramClass.getField(paramString);
            localField.setAccessible(true);
            k = localField.getInt(paramString);
            return k;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

//	 try
//     {
//       Field localField = paramClass.getField(paramString);
//       localField.setAccessible(true);
//       int k = localField.getInt(paramString);
//       return k;
//     }
//     catch (Exception localException)
//     {
//   	  Log.d(TAG, "getRes(" + paramClass.getName() + ", " + paramString + ")");
//   	  Log.d(TAG, "Error getting resource. Make sure you have copied all resources (res/) from SDK to your project. ");
//   	  Log.d(TAG, localException.getMessage());
//     }

        return -1;
    }

}

package com.hdzx.tenement.utils;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class AndroidControllerUtil
{
	public static boolean isEmptyTextEditText(EditText editText)
	{
		boolean isEmpty = true;
		
		if (editText.getText() != null && editText.getText().toString() != null
				&& !"".equals(editText.getText().toString().trim()))
		{
			isEmpty = false;
		}
		
		return isEmpty;
	}
/*	
	public static AlertDialog showAlertDialog(Context context, String title, String message, String negativeButtonText, OnClickListener okListener)
	{
		return new Builder(context)
		.setTitle(title)
		.setMessage(message)
		.setNegativeButton(negativeButtonText, okListener)
		.show();
		
	}*/
	public static AlertDialog showAlertDialog(Context context, String message, String negativeButtonText, OnClickListener okListener)
	{
		return new Builder(context)
		.setMessage(message)
		.setNegativeButton(negativeButtonText, okListener)
		.show();
		
	}
	/*
	public static AlertDialog showAlertDialog(Context context, String title, String message, String negativeButtonText, OnClickListener okListener, String positiveButtonText, OnClickListener noListener)
	{
		return new Builder(context)
		.setTitle(title)
		.setMessage(message)
		.setNegativeButton(negativeButtonText, okListener)
		.setPositiveButton(positiveButtonText, noListener)
		.show();
	}*/
	public static AlertDialog showAlertDialog(Context context, String message, String negativeButtonText, OnClickListener okListener, String positiveButtonText, OnClickListener noListener)
	{
		return new Builder(context)
		.setMessage(message)
		.setNegativeButton(negativeButtonText, okListener)
		.setPositiveButton(positiveButtonText, noListener)
		.show();
	}
	/*public static AlertDialog showAlertDialog(Context context, String title, String message)
	{
		return new Builder(context)
		.setTitle(title)
		.setMessage(message)
		.show();
	}*/
	public static AlertDialog showAlertDialog(Context context,String message)
	{
		return new Builder(context)
		.setMessage(message)
		.show();
	}
	
	public static void showToast(Context context, String message, int duration)
	{
		Toast.makeText(context, message, duration).show();;
	}
	
/*	public static void showConfirmDialog(Context context, String title, String message, String positiveButtonText, OnClickListener okListener, String negativeButtonText, OnClickListener cancelListener)
	{
		new Builder(context)
		.setTitle(title)
		.setMessage(message)
		.setPositiveButton(positiveButtonText, okListener)
		.setNegativeButton(negativeButtonText, cancelListener)
		.show();
	}*/
	public static void showConfirmDialog(Context context,String message, String positiveButtonText, OnClickListener okListener, String negativeButtonText, OnClickListener cancelListener)
	{
		new Builder(context)
		.setMessage(message)
		.setPositiveButton(positiveButtonText, okListener)
		.setNegativeButton(negativeButtonText, cancelListener)
		.show();
	}

    /**
     * 方法描述：动态设置ListView的高度
     * @param listView {@link android.widget.ListView}
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        if(listView == null) return;

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}

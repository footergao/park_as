package com.hdzx.tenement.utils;


import android.app.Activity;
import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * tools for operating UI
 *
 * @author zdjd
 */
public class UIUtil {
    /**
     * hide the software keyboard
     *
     * @param context
     * @param et
     */
    public static void hideSoftInput(Context context, EditText et) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
    }

    public static void showOrHideKeyboard(Context context, EditText et) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInputFromWindow(et.getWindowToken(), 0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * recalculate the height of listView
     *
     * @param listView
     */
    public static void setListViewHeight(ListView listView) {
        if (listView == null) {
            return;
        }
        ListAdapter la = listView.getAdapter();
        if (null == la) {
            return;
        }
        // calculate height of all items.
        int h = 0;
        final int count = la.getCount();
        for (int i = 0; i < count; i++) {
            View item = la.getView(i, null, listView);

            item.measure(0, 0);
            h += item.getMeasuredHeight();
        }
        // reset ListView height
        ViewGroup.LayoutParams lp = listView.getLayoutParams();
        lp.height = h + (listView.getDividerHeight() * count) + UIUtil.dip2px(listView.getContext(), 40);
        listView.setLayoutParams(lp);
    }

    public static void setListViewHeight2(ListView listView) {
        if (listView == null) {
            return;
        }
        ListAdapter la = listView.getAdapter();
        if (null == la) {
            return;
        }
        // calculate height of all items.
        int h = 0;
        final int count = la.getCount();
        for (int i = 0; i < count; i++) {
            View item = la.getView(i, null, listView);

            item.measure(0, 0);
            h += item.getMeasuredHeight();
        }
        // reset ListView height
        ViewGroup.LayoutParams lp = listView.getLayoutParams();
        lp.height = h + ((listView.getDividerHeight() + 20) * count);
        listView.setLayoutParams(lp);
    }

    public static void setListViewHeight(ListView listView, int count) {
        if (listView == null) {
            return;
        }
        ListAdapter la = listView.getAdapter();
        if (null == la) {
            return;
        }
        // calculate height of all items.
        int h = 0;
        if (la.getCount() < 3) {
            count = la.getCount();
        }
        ;
        for (int i = 0; i < count; i++) {
            View item = la.getView(i, null, listView);
            item.measure(0, 0);
            h += item.getMeasuredHeight();
        }
        // reset ListView height
        ViewGroup.LayoutParams lp = listView.getLayoutParams();
        lp.height = h + (listView.getDividerHeight() * count);
        listView.setLayoutParams(lp);
    }

    /**
     *
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     *
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static SpannableString getSizeSpanSpToPx(Context context, String str, int start, int end, int spSize) {
        SpannableString ss = new SpannableString(str);
        ss.setSpan(new AbsoluteSizeSpan(sp2px(context, spSize)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    private static long lastClickTime;

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (timeD >= 0 && timeD <= 1000) {
            return true;
        } else {
            lastClickTime = time;
            return false;
        }
    }
}

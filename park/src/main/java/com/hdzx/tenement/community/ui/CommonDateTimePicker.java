package com.hdzx.tenement.community.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.ViewFlipper;
import com.hdzx.tenement.R;
import com.hdzx.tenement.community.vo.ServiceBean;
import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.NumericWheelAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 
 * @author Jesley
 *
 */
public class CommonDateTimePicker extends PopupWindow
{

    private Activity mContext;
    
    private ServiceBean serviceBean = null;
    
    private View mMenuView;
    
    private ViewFlipper viewfipper;
    
    private Button btn_submit, btn_cancel;
    
    private String age;
    
    private DateNumericAdapter monthAdapter, dayAdapter, yearAdapter, hourAdapter, minuteAdapter;
    
    private WheelView year, month, day, hour, minute;
    
    private int mCurYear = 80, mCurMonth = 5, mCurDay = 14;
    
    private String[] dateType;
    
    private Calendar pickedCalendar = null;
    
    private int visibleItemCount = 0;
    
    private TextView targetTv = null;
    
    public CommonDateTimePicker(Activity context)
    {
        this(context, 3);
    }
    
    public CommonDateTimePicker(Activity context, int visibleItemCount)
    {
        super(context);
        mContext = context;
        this.visibleItemCount = visibleItemCount;
        this.age = "2012-9-25";
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.common_datetime_picker_layout, null);
        viewfipper = new ViewFlipper(context);
        viewfipper.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        year = (WheelView) mMenuView.findViewById(R.id.year);
        year.setVisibleItems(visibleItemCount);
        
        month = (WheelView) mMenuView.findViewById(R.id.month);
        month.setVisibleItems(visibleItemCount);
        day = (WheelView) mMenuView.findViewById(R.id.day);
        day.setVisibleItems(visibleItemCount);
        
        hour = (WheelView) mMenuView.findViewById(R.id.hour);
        hour.setVisibleItems(visibleItemCount);
        
        minute = (WheelView) mMenuView.findViewById(R.id.minute);
        minute.setVisibleItems(visibleItemCount);
        
        btn_submit = (Button) mMenuView.findViewById(R.id.submit);
        btn_cancel = (Button) mMenuView.findViewById(R.id.cancel);
        Calendar calendar = Calendar.getInstance();
        OnWheelChangedListener listener = new OnWheelChangedListener()
        {
            public void onChanged(WheelView wheel, int oldValue, int newValue)
            {
                updateDays(year, month, day);

            }
        };
        int curYear = calendar.get(Calendar.YEAR);
        if (age != null && age.contains("-"))
        {
            String str[] = age.split("-");
            mCurYear = 100 - (curYear - Integer.parseInt(str[0]));
            mCurMonth = Integer.parseInt(str[1]) - 1;
            mCurDay = Integer.parseInt(str[2]) - 1;
            ;
        }
        dateType = mContext.getResources().getStringArray(R.array.date);
        
        monthAdapter = new DateNumericAdapter(context, 1, 12, 5);
        monthAdapter.setDataType(dateType[1]);
        month.setViewAdapter(monthAdapter);
        month.setCurrentItem(mCurMonth);
        month.addChangingListener(listener);
        // year
        dateType = mContext.getResources().getStringArray(R.array.date); 
        yearAdapter = new DateNumericAdapter(context, curYear - 100, curYear + 100, 100 - 20);
        yearAdapter.setDataType(dateType[0]);
        year.setViewAdapter(yearAdapter);
        year.setCurrentItem(mCurYear);
        year.addChangingListener(listener);
        // day

        updateDays(year, month, day);
        day.setCurrentItem(mCurDay);
        updateDays(year, month, day);
        day.addChangingListener(listener);
        
        hourAdapter = new DateNumericAdapter(context, 0, 23, 0);
        hourAdapter.setDataType(dateType[3]);
        hour.setViewAdapter(hourAdapter);
        hour.addChangingListener(listener);

        minuteAdapter = new DateNumericAdapter(context, 0, 59, 0);
        minuteAdapter.setDataType(dateType[4]);
        minute.setViewAdapter(minuteAdapter);
        minute.addChangingListener(listener);
        
        viewfipper.addView(mMenuView);
        viewfipper.setFlipInterval(6000000);
        this.setContentView(viewfipper);
        this.setWidth(LayoutParams.MATCH_PARENT);
        this.setHeight(LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        this.setBackgroundDrawable(dw);
        this.update();
        
        btn_cancel.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dismiss();
            }
        });
        
        btn_submit.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                String dateStr = f.format(pickedCalendar.getTime());
                if (targetTv != null)
                {
                    
                    targetTv.setText(dateStr);
                }
                
                serviceBean.setValue(dateStr + ":00");
                
                dismiss();
            }
        });
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y)
    {
        super.showAtLocation(parent, gravity, x, y);
        viewfipper.startFlipping();
    }

    private void updateDays(WheelView year, WheelView month, WheelView day)
    {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + year.getCurrentItem() - 100);
        calendar.set(Calendar.MONTH, month.getCurrentItem() + 1);
        calendar.set(Calendar.DAY_OF_MONTH, 0);
        calendar.set(Calendar.HOUR_OF_DAY, hour.getCurrentItem());
        calendar.set(Calendar.MINUTE, minute.getCurrentItem());

        int maxDays = calendar.get(Calendar.DAY_OF_MONTH);
        dayAdapter = new DateNumericAdapter(mContext, 1, maxDays, calendar.get(Calendar.DAY_OF_MONTH) - 1);
        dayAdapter.setDataType(dateType[2]);
        day.setViewAdapter(dayAdapter);
        int curDay = Math.min(maxDays, day.getCurrentItem() + 1);
        day.setCurrentItem(curDay - 1, true);
        calendar.set(Calendar.DAY_OF_MONTH, curDay);
        int years = calendar.get(Calendar.YEAR);
        age = years + "-" + (month.getCurrentItem() + 1) + "-" + (day.getCurrentItem() + 1) + " " + hour.getCurrentItem() + ":" + minute.getCurrentItem();
        pickedCalendar = calendar;
    }

    /**
     * Adapter for numeric wheels. Highlights the current value.
     */
    private class DateNumericAdapter extends NumericWheelAdapter
    {

        private String dataType = "";
        
        /**
         * Constructor
         */
        public DateNumericAdapter(Context context, int minValue, int maxValue, int current)
        {
            super(context, minValue, maxValue);
            setTextSize(16);
        }

        protected void configureTextView(TextView view)
        {
            super.configureTextView(view);
            view.setTypeface(Typeface.SANS_SERIF);
        }

        public CharSequence getItemText(int index)
        {
            return super.getItemText(index) + dataType;
        }

        public String getDataType()
        {
            return dataType;
        }

        public void setDataType(String dataType)
        {
            this.dataType = dataType;
        }
    }

    public Calendar getPickedCalendar()
    {
        return pickedCalendar;
    }

    public void setLeftListener(OnClickListener leftListener)
    {
        btn_cancel.setOnClickListener(leftListener);
    }

    public void setRightListener(OnClickListener rightListener)
    {
        btn_submit.setOnClickListener(rightListener);
    }

    public int getVisibleItemCount()
    {
        return visibleItemCount;
    }

    public void setVisibleItemCount(int visibleItemCount)
    {
        this.visibleItemCount = visibleItemCount;
        
        year.setVisibleItems(visibleItemCount);
        month.setVisibleItems(visibleItemCount);
        day.setVisibleItems(visibleItemCount);
        hour.setVisibleItems(visibleItemCount);
        minute.setVisibleItems(visibleItemCount);
    }

    public TextView getTargetTv()
    {
        return targetTv;
    }

    public void init(ServiceBean serviceBean, TextView targetTv)
    {
        this.serviceBean = serviceBean;
        this.targetTv = targetTv;
    }
}

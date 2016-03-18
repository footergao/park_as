package com.hdzx.tenement.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

public class MyGridViewCanTouch extends GridView {

    private OnTouchInvalidPositionListener onTouchInvalidPositionListener;  
	
	public MyGridViewCanTouch(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyGridViewCanTouch(Context context) {
		super(context);
	}

	public MyGridViewCanTouch(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	// 该自定义控件只是重写了GridView的onMeasure方法，使其不会出现滚动条，ScrollView嵌套ListView也是同样的道理，不再赘述。
	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
	
	  @Override  
	    public boolean onTouchEvent(MotionEvent ev) {
			return false;  
	        //先创建一个监听接口，一旦点击了无效区域，便实现onTouchInvalidPosition方法，返回true or false来确认是否消费了这个事件  
//	        if(onTouchInvalidPositionListener!=null){  
//	            if(!isEnabled()){  
//	                return isClickable()||isLongClickable();  
//	            }  
//	            int motionPosition = pointToPosition((int)ev.getX(), (int)ev.getY());  
//	            if(ev.getAction()==MotionEvent.ACTION_UP&&motionPosition == INVALID_POSITION){  
//	                super.onTouchEvent(ev);  
//	                return onTouchInvalidPositionListener.onTouchInvalidPosition(motionPosition);  
//	            }  
//	        }  
//	        return super.onTouchEvent(ev);  
	    }  
	      
	    public void setOnTouchInvalidPositionListener(  
	            OnTouchInvalidPositionListener onTouchInvalidPositionListener) {  
	        this.onTouchInvalidPositionListener = onTouchInvalidPositionListener;  
	    }  
	  
	    public interface OnTouchInvalidPositionListener{  
	        public boolean onTouchInvalidPosition(int motionEvent);  
	    }  

}
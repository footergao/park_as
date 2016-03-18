package com.hdzx.tenement.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * @author Zhouxia
 * 
 */
public class ScrollLinerLayout extends LinearLayout
{

    /**
     * @param context 相应的context
     * @param attrs  参数
     * 构造函数.
     */
    public ScrollLinerLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context);
    }

    /**
     * @param context
     * 初始化.
     */
    private void init(Context context)
    {
        mScroller = new Scroller(context);
    }
    /**Scroller实例.*/
    private Scroller mScroller;
    
    /**是否按下的标记.*/
    private boolean pressed = true;

    /**
     * 按下的方法.
     */
    public void onDown()
    {
        if (!mScroller.isFinished())
        {
            mScroller.abortAnimation(); //停止动画.
        }
    }

    @Override
    public void setPressed(boolean pressed)
    {
        if (this.pressed)
        {
            super.setPressed(pressed);
        }
        else
        {
            super.setPressed(this.pressed);
        }
    }

    /**
     * @param pressed
     * 设置pressed值.
     */
    public void setSingleTapUp(boolean pressed)
    {
        this.pressed = pressed;
    }

    @Override
    public void computeScroll()
    {
        if (mScroller.computeScrollOffset())
        {
            scrollTo(mScroller.getCurrX(), 0);
            postInvalidate();
        }
    }

    /**
     * @return
     * 获得当前位置的x值.
     */
    public int getToX()
    {
        return mScroller.getCurrX();
    }

    /**
     * @param whichScreen 起始移动的x坐标值.
     * 完成滚动.
     */
    public void snapToScreen(int whichScreen)
    {
        mScroller.startScroll(whichScreen, 0, 0, 0, 50);
        invalidate();

    }

}

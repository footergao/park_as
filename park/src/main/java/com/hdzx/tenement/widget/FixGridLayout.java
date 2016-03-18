package com.hdzx.tenement.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.hdzx.tenement.R;

public class FixGridLayout extends ViewGroup
{
	private static final int DEFAULT_HORIZONTAL_SPACING = 15;
	private static final int DEFAULT_VERTICAL_SPACING = 15;

	private int mHorizontalSpacing;
	private int mVerticalSpacing;
	int line_height = 0;

	public FixGridLayout(Context context, AttributeSet attrs) {
		super(context, attrs);

		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.FlowLayout);
		try {
			mHorizontalSpacing = a.getDimensionPixelSize(
					R.styleable.FlowLayout_horizontalSpacing,
					DEFAULT_HORIZONTAL_SPACING);
			mVerticalSpacing = a.getDimensionPixelSize(
					R.styleable.FlowLayout_verticalSpacing,
					DEFAULT_VERTICAL_SPACING);
		} finally {
			a.recycle();
		}

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int myWidth = resolveSize(0, widthMeasureSpec);

		int paddingLeft = getPaddingLeft();
		int paddingTop = getPaddingTop();
		int paddingRight = getPaddingRight();
		int paddingBottom = getPaddingBottom();

		int childLeft = paddingLeft;
		int childTop = paddingTop;

		int lineHeight = 0;

		// Measure each child and put the child to the right of previous child
		// if there's enough room for it, otherwise, wrap the line and put the
		// child to next line.
		for (int i = 0, childCount = getChildCount(); i < childCount; ++i) {
			View child = getChildAt(i);
			if (child.getVisibility() != View.GONE) {
				measureChild(child, widthMeasureSpec, heightMeasureSpec);
			} else {
				continue;
			}

			int childWidth = child.getMeasuredWidth();
			int childHeight = child.getMeasuredHeight();

			lineHeight = Math.max(childHeight, lineHeight);

			if (childLeft + childWidth + paddingRight > myWidth) {
				childLeft = paddingLeft;
				childTop += mVerticalSpacing + lineHeight;
				lineHeight = childHeight;
			} else {
				childLeft += childWidth + mHorizontalSpacing;
			}
		}

		int wantedHeight = childTop + lineHeight + paddingBottom;

		setMeasuredDimension(myWidth,
				resolveSize(wantedHeight, heightMeasureSpec));
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int myWidth = r - l;

		int paddingLeft = getPaddingLeft();
		int paddingTop = getPaddingTop();
		int paddingRight = getPaddingRight();

		int childLeft = paddingLeft;
		int childTop = paddingTop;

		int lineHeight = 0;

		for (int i = 0, childCount = getChildCount(); i < childCount; ++i) {
			View childView = getChildAt(i);

			if (childView.getVisibility() == View.GONE) {
				continue;
			}

			int childWidth = childView.getMeasuredWidth();
			int childHeight = childView.getMeasuredHeight();

			lineHeight = Math.max(childHeight, lineHeight);

			if (childLeft + childWidth + paddingRight > myWidth) {
				childLeft = paddingLeft;
				childTop += mVerticalSpacing + lineHeight;
				lineHeight = childHeight;
			}

			childView.layout(childLeft, childTop, childLeft + childWidth,
					childTop + childHeight);
			childLeft += childWidth + mHorizontalSpacing;
		}
	}
	@Override
	protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
		return p instanceof LayoutParams;
	}

	@Override
	protected LayoutParams generateDefaultLayoutParams() {
		return new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
	}

	@Override
	public LayoutParams generateLayoutParams(AttributeSet attrs) {
		return new LayoutParams(getContext(), attrs);
	}

	@Override
	protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
		return new LayoutParams(p.width, p.height);
	}

}

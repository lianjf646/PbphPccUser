package com.android.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("unused")
public class FlowLayout extends RelativeLayout {
    // 存储所有子View
    private List<List<View>> mAllChildViews = new ArrayList<List<View>>();
    // 每一行的高度
    private List<Integer> mLineHeight = new ArrayList<Integer>();

    LayoutParams layoutParams;

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressWarnings("ResourceType")
    public FlowLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.leftMargin = layoutParams.rightMargin = layoutParams.topMargin = layoutParams.bottomMargin = 10;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // 父控件传进来的宽度和高度以及对应的测量模式
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

//        sizeHeight=0;

        // 自己测量的宽度
        int max_width = 0;
        int max_height = 0;

        // 记录每一行的宽度和高度
        int line_width = 0;
        int line_height = 0;

        mAllChildViews.clear();
        mLineHeight.clear();

        List<View> lineViews = new ArrayList<View>();
        // 获取子view的个数
        for (int i = 0, childCount = getChildCount(); i < childCount; i++) {
            View child = getChildAt(i);

            if (child.getVisibility() == GONE) continue;

            // 测量子View的宽和高
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;

            // 换行时候
            if (line_width + childWidth > sizeWidth) {

                max_height += line_height;
                // 记录LineHeight
                mLineHeight.add(line_height);
                // 记录当前行的Views
                mAllChildViews.add(lineViews);
                lineViews = new ArrayList();

                line_width = 0;
                line_height = 0;
            }

            line_width += childWidth;  // 叠加行宽

            max_width = Math.max(line_width, max_width); // 得到最大行高

            line_height = Math.max(line_height, childHeight); // 得到最大行高

            lineViews.add(child);
        }

        if (lineViews.size() > 0) {

            max_height += line_height;

            mLineHeight.add(line_height);
            // 记录当前行的Views
            mAllChildViews.add(lineViews);

        }

//        // 父控件传进来的宽度和高度以及对应的测量模式
//        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
//        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        setMeasuredDimension(sizeWidth > max_width ? sizeWidth : max_width, sizeHeight > max_height ? sizeHeight : max_height);
//        setMeasuredDimension(sizeWidth, sizeHeight > max_height ? sizeHeight : max_height);
//        setMeasuredDimension(sizeWidth, modeHeight == MeasureSpec.EXACTLY ? max_height : sizeHeight);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {


        // 设置子View的位置
        int left = 0;
        int top = 0;

        for (int i = 0; i < mAllChildViews.size(); i++) {

            // 当前行的views和高度
            int lineHeight = mLineHeight.get(i);

            List<View> lineViews = mAllChildViews.get(i);
            for (int j = 0; j < lineViews.size(); j++) {
                View child = lineViews.get(j);

                // 判断是否显示
                if (child.getVisibility() == View.GONE) continue;

                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                int cLeft = left + lp.leftMargin;
                int cTop = top + lp.topMargin;
                int cRight = cLeft + child.getMeasuredWidth();
                int cBottom = cTop + child.getMeasuredHeight();
                // 进行子View进行布局
                child.layout(cLeft, cTop, cRight, cBottom);

                left = cRight + lp.rightMargin;
            }
            left = 0;
            top += lineHeight;
        }

    }

    public void setMargins(int... ints) {
        if (ints == null || ints.length <= 0) return;

//        if (ints.length == 1) {
//            layoutParams.leftMargin = layoutParams.rightMargin = layoutParams.topMargin = layoutParams.bottomMargin = ints[0];
//            return;
//        }

        for (int i = 0; i < ints.length; i++) {
            switch (i) {
                case 0:
                    layoutParams.leftMargin = ints[i];
                    break;
                case 1:
                    layoutParams.rightMargin = ints[i];
                    break;
                case 2:
                    layoutParams.topMargin = ints[i];
                    break;
                case 3:
                    layoutParams.bottomMargin = ints[i];
                    break;
                default:
                    return;
            }
        }
    }

    public void addViewByLayoutParams(View view) {
        this.addView(view, layoutParams);
    }

}

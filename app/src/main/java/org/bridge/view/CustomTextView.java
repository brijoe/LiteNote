package org.bridge.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.TextView;

/**
 * 自定义TextView 用于Noteitem 显示
 */
public class CustomTextView extends TextView {

    private static int width;

    /**
     * 构造代码块，优先构造方法执行
     */

    {
        //获取屏幕尺寸
        DisplayMetrics dm = getResources().getDisplayMetrics();
        width = dm.widthPixels;

    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = getMeasuredWidth();
        int heightSize = getMeasuredHeight();
        //调整高度，避免显示内容高度过大
        if (heightSize > width * 0.8) {
            heightSize = (int) (width * 0.8);
            heightSize -= 30;
        }
        setMeasuredDimension(widthSize, heightSize);
        requestLayout();
    }
}
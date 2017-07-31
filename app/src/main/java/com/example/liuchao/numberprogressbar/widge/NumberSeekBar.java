package com.example.liuchao.numberprogressbar.widge;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Created by liuchao on 2017/7/29.
 */

@SuppressLint("AppCompatCustomView")
public class NumberSeekBar extends SeekBar {
    TextPaint mPaint;
    Paint mTabPaint;
    float mTextHeight;
    float mTextWidth;
    float mFontBottom, mFontTop;

    private int mTextColor;             //文本颜色
    private int mColor;                 //背景颜色
    private int mPadding = 2;           //进度tag文本的padding
    private int mTextSize = 10;         //文字大小


    public NumberSeekBar(Context context) {
        super(context);
        init();
    }

    public NumberSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NumberSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @SuppressLint("NewApi")
    public NumberSeekBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setFieldValue("mOnlyIndeterminate", new Boolean(false));
        setIndeterminate(false);
        setMinimumHeight(160);
        //文本背景
        mTabPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTabPaint.setColor(mColor > 0 ? getResources().getColor(mColor) : Color.BLUE);
        //画进度百分比文本
        mPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(mTextColor > 0 ? getResources().getColor(mTextColor) : Color.WHITE);
        mPaint.setTextSize(getContext().getResources().getDisplayMetrics().density * 10);
        Paint.FontMetrics metrics = mPaint.getFontMetrics();
        mFontBottom = metrics.bottom;
        mFontTop = metrics.top;
        mTextHeight = metrics.bottom - metrics.top;
        mTextWidth = mPaint.measureText("88%");
        setPadding((int) (mTextWidth / 2 + mPadding), 0, (int) (mTextWidth / 2 + mPadding), 0);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int resetHeight = MeasureSpec.makeMeasureSpec((int) (5 * mTextHeight), MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, resetHeight);
        setTranslationY(-mTextHeight * 1);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (100 == getProgress()) {
            mPaint.setTextSize(getContext().getResources().getDisplayMetrics().density * (mTextSize - 2));
        } else {
            mPaint.setTextSize(getContext().getResources().getDisplayMetrics().density * mTextSize);
        }
        float radius = mTextWidth / 2 + mPadding;                                                   //水滴圆半径
        float progressX = (getMeasuredWidth() - 2 * radius) * getProgress() / getMax();             //进度条X
        float textCenterX = progressX + getPaddingLeft();                                           //文本中心X
        float textLeft = progressX - mTextWidth / 2 + getPaddingLeft();                             //文本开始X
        float baselineY = getMeasuredHeight() / 2 + 2 * mTextHeight + mFontTop;                     //文本基线Y
        float circleY = baselineY + (mFontBottom + mFontTop) / 2;                                   //圆心Y
        //画水滴文本圆
        canvas.drawCircle(textCenterX, circleY + mTextWidth / 2, radius, mTabPaint);
        //画水滴圆切边三角形
        Path path = new Path();
        double tangle = Math.acos(radius / mTextWidth);
        float rightX = (float) (textCenterX + Math.sin(tangle) * radius);
        float rightY = (float) (circleY + mTextWidth / 2 - Math.cos(tangle) * radius);
        float leftX = (float) (textCenterX - Math.sin(tangle) * radius);
        float leftY = (float) (circleY + mTextWidth / 2 - Math.cos(tangle) * radius);
        path.moveTo(textCenterX, circleY - mTextWidth / 2);
        path.lineTo(rightX, rightY);
        path.lineTo(leftX, leftY);
        path.close();
        canvas.drawPath(path, mTabPaint);
        //画文本
        String text = String.format("%02d%%", getProgress());
        canvas.drawText(text, textLeft, baselineY + mTextWidth / 2, mPaint);
    }

    /**
     * 直接设置对象属性值,无视private/protected修饰符,不经过setter函数.
     */

    public void setFieldValue(final String fieldName, final Object value) {
        Field field = getDeclaredField(getClass().getSuperclass(), fieldName);
        if (field == null)
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + this + "]");
        if (!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers())) {//强制转换fileld可访问.
            field.setAccessible(true);
        }
        try {
            field.set(this, value);
        } catch (IllegalAccessException e) {
            Log.e("zbkc", "", e);
        }
    }

    /**
     * 循环向上转型,获取类的DeclaredField.
     */

    @SuppressWarnings("unchecked")
    protected Field getDeclaredField(final Class clazz, final String fieldName) {
        for (Class superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                return superClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                // Field不在当前类定义,继续向上转型
            }
        }
        return null;
    }


    public void setTextColor(int mTextColor) {
        this.mTextColor = mTextColor;
        mPaint.setColor(mTextColor > 0 ? getResources().getColor(mTextColor) : Color.WHITE);
        postInvalidate();
    }

    public void setColor(int mColor) {
        this.mColor = mColor;
        mTabPaint.setColor(mColor > 0 ? getResources().getColor(mColor) : Color.BLUE);
        postInvalidate();
    }

    public void setPadding(int mPadding) {
        this.mPadding = mPadding;
    }

    public void setTextSize(int mTextSize) {
        this.mTextSize = mTextSize;
    }
}

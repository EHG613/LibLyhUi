/*
 *  SwitchView.java, 2022-09-28
 *  Copyright © 2015-2022  Liangyihui. All rights reserved.
 */

package net.liangyihui.android.ui.widget.switchview;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.view.GestureDetectorCompat;

import net.liangyihui.android.ui.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liudong on 2017/5/19.
 */

public class SwitchView extends View {
    private static final String TAG = "SwitchView";
    private int selectedTextColor;
    private int unSelectedTextColor;
    private int slideBgColor;
    private int bgColor;
    private float textSize;
    private int animateInterval;

    private Paint bgPaint;//背景画笔
    private Paint mSlidePaint;//点击画笔
    private Paint selectedTextPaint;//左边字的画笔
    private Paint unselectedTextPaint;//点击字的画笔

    private float mWidth, mHeight;

    private float mItemWidth;//偏移量

    private RectF roundRect;//圆角矩形坐标

    private Path bgPath;//大的路径
    private Path mItemSlidePath;//小的路径

    private float padding;

    private ValueAnimator anim;

    private CharSequence[] texts;

    private final List<Item> items = new ArrayList<>();

    private AnimatorSet animSet;

    public SwitchView(Context context) {
        this(context, null);
    }

    public SwitchView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwitchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
        initPaint();
    }

    private void init(@Nullable AttributeSet attrs) {
        // 获取自定义属性
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.SwitchView);
        try {
            texts = a.getTextArray(R.styleable.SwitchView_sv_texts);
            if (texts.length < 2) {
                throw new IllegalArgumentException("SwitchView_sv_texts at least two items");
            }
            bgColor = a.getColor(R.styleable.SwitchView_sv_bgColor, Color.parseColor("#F4F4FA"));
            selectedTextColor = a.getColor(R.styleable.SwitchView_sv_selectedTextColor, Color.parseColor("#454556"));
            unSelectedTextColor = a.getColor(R.styleable.SwitchView_sv_unselectedTextColor, Color.parseColor("#9E9EAB"));
            slideBgColor = a.getColor(R.styleable.SwitchView_sv_slideColor, Color.WHITE);
            padding = a.getDimension(R.styleable.SwitchView_sv_padding, dp2px(1));
            textSize = a.getDimension(R.styleable.SwitchView_sv_textSize, dp2px(12));
            animateInterval = a.getInteger(R.styleable.SwitchView_sv_interval, 125);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            a.recycle();
        }

    }


    //初始化画笔
    private void initPaint() {
        animSet = new AnimatorSet();
        anim = new ValueAnimator();
        roundRect = new RectF();
        bgPath = new Path();
        bgPaint = new Paint();
        bgPaint.setColor(bgColor);
        bgPaint.setAntiAlias(true);
        mItemSlidePath = new Path();
        mSlidePaint = new Paint();
        mSlidePaint.setAntiAlias(true);
        mSlidePaint.setColor(slideBgColor);
        selectedTextPaint = new Paint();
        selectedTextPaint.setTextSize(textSize);
        selectedTextPaint.setAntiAlias(true);
        unselectedTextPaint = new Paint();
        unselectedTextPaint.setTextSize(textSize);
        unselectedTextPaint.setAntiAlias(true);
        selectedTextPaint.setColor(selectedTextColor);
        unselectedTextPaint.setColor(unSelectedTextColor);
    }


    private int currentSelectedPosition = NO_ID;

    //初始化Path
    private void initPath() {
        mItemWidth = (mWidth - padding * 2) / texts.length;
        getPath(bgPath, 0, mWidth, 0);
//        offsetWidth(0);
        items.clear();
        for (int i = 0; i < texts.length; i++) {
            Paint.FontMetrics fontMetrics = selectedTextPaint.getFontMetrics();
            float y = mHeight / 2 + (Math.abs(fontMetrics.ascent) - fontMetrics.descent) / 2;
            float x;
            if (currentSelectedPosition == i) {
                x = (mItemWidth * i) + (mItemWidth - selectedTextPaint.measureText(String.valueOf(texts[i]))) / 2 + padding;
            } else {
                x = (mItemWidth * i) + (mItemWidth - unselectedTextPaint.measureText(String.valueOf(texts[i]))) / 2 + padding;
            }
            items.add(new Item(x, y, String.valueOf(texts[i])));
        }
    }


    private void initAnim(long time, float... values) {
        if (animSet != null && animSet.isRunning()) {
            animSet.cancel();
        }
        animSet = new AnimatorSet();
        anim.setFloatValues(values);
        anim.addUpdateListener(animation -> {
            offsetWidth((Float) animation.getAnimatedValue());
            invalidate();
        });
        animSet.play(anim);
        animSet.setDuration(time);
        animSet.start();

    }

    private void offsetWidth(float w) {
        getPath(mItemSlidePath, padding, mItemWidth, w);
    }

    //绘制圆角矩形
    private void getPath(Path path, float padding, float width, float offset) {
        //矩形坐标
        roundRect.set(0 + padding + offset, 0 + padding, width + padding + offset, mHeight - padding);
        //回放路径：清除路径中的任何直线和曲线，但保留内部数据结构，以便更快地重用。
        path.rewind();
        //绘制 矩形圆角
        path.addRoundRect(roundRect, (mHeight - padding) / 2, (mHeight - padding) / 2, Path.Direction.CW);//顺时针
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        float widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        float heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize; //获取到当前view的宽度
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize;//获取当前view的高度
        }
        setMeasuredDimension((int) mWidth, (int) mHeight);
        initPath();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);
        canvas.drawPath(bgPath, bgPaint);
        canvas.drawPath(mItemSlidePath, mSlidePaint);
        for (int i = 0; i < items.size(); i++) {
            canvas.drawText(items.get(i).getText(), items.get(i).getX(), items.get(i).getY(), currentSelectedPosition == i ? selectedTextPaint : unselectedTextPaint);
        }
    }


    private int dp2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * sp转px
     *
     * @param spValue sp值
     * @return px值
     */
    public int sp2px(float spValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    private OnClickCheckedListener onClickCheckedListener;
    private GestureDetectorCompat mDetector;

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mDetector = new GestureDetectorCompat(getContext(), new MyGestureListener(this));
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mDetector = null;
    }

    private static class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        private final SwitchView switchView;

        public MyGestureListener(SwitchView _switchView) {
            this.switchView = _switchView;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent event) {
            for (int i = 0; i < this.switchView.texts.length; i++) {
                if (event.getX() >= i * this.switchView.mItemWidth && event.getX() < (i + 1) * this.switchView.mItemWidth) {
                    this.switchView.checkWithAnim(i);
                }
            }
            return true;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mDetector.onTouchEvent(event);
    }

    public void checkWithAnim(int position) {
        if (currentSelectedPosition == position) {
            invalidate();
            return;
        }
        float start = currentSelectedPosition * mItemWidth;
        float end = position * mItemWidth;
        initAnim(((long) (Math.abs(currentSelectedPosition - position) + 1) * animateInterval), start, end);
        this.currentSelectedPosition = position;
        if (onClickCheckedListener != null) {
            onClickCheckedListener.onClick(this, this.currentSelectedPosition);
        }
    }

    public void check(int position) {
        if (position < 0) {
            this.currentSelectedPosition = 0;
        } else if (position + 1 > texts.length) {
            this.currentSelectedPosition = texts.length - 1;
        } else {
            this.currentSelectedPosition = position;
        }
        offsetWidth(this.currentSelectedPosition * mItemWidth);
        invalidate();
    }

    public int getCurrentPosition() {
        return this.currentSelectedPosition;
    }

    public interface OnClickCheckedListener {
        void onClick(SwitchView switchView, int currentPosition);
    }

    public void setOnClickCheckedListener(OnClickCheckedListener onClickCheckedListener) {
        this.onClickCheckedListener = onClickCheckedListener;
    }

    private static class Item {
        private float x;
        private float y;
        private String text;

        public Item(float x, float y, String text) {
            this.x = x;
            this.y = y;
            this.text = text;
        }

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

}
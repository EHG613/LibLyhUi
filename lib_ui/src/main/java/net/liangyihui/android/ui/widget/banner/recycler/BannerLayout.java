/*
 *  BannerLayout.java, 2022-09-28
 *  Copyright © 2015-2022  Liangyihui. All rights reserved.
 */

package net.liangyihui.android.ui.widget.banner.recycler;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import net.liangyihui.android.ui.R;
import net.liangyihui.android.ui.utils.ResUtils;
import net.liangyihui.android.ui.widget.banner.recycler.layout.BannerLayoutManager;
import net.liangyihui.android.ui.widget.banner.recycler.layout.CenterSnapHelper;

import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_IDLE;

/**
 * 使用RecyclerView实现轮播Banner
 *
 * @author xuexiang
 * @since 2019/5/29 10:52
 */
public class BannerLayout extends FrameLayout {
    private final static int WHAT_AUTO_PLAY = 1000;

    private RecyclerView mIndicatorContainer;
    private IndicatorAdapter mIndicatorAdapter;
    private int mIndicatorMargin;//指示器间距
    private Drawable mSelectedDrawable;
    private Drawable mUnselectedDrawable;

    private RecyclerView mRecyclerView;
    private BannerLayoutManager mLayoutManager;

    private boolean mHasInit;
    private int mBannerSize = 1;
    private int mCurrentIndex;
    private boolean mIsPlaying = false;

    private boolean mIsAutoPlaying = true;
    private int mAutoPlayDuration;//刷新间隔时间
    private boolean mShowIndicator;//是否显示指示器
    private int mItemSpace;
    private float mCenterScale;
    private float mMoveSpeed;

    private OnIndicatorIndexChangedListener mOnIndicatorIndexChangedListener;

    protected Handler mHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == WHAT_AUTO_PLAY) {
                if (mCurrentIndex == mLayoutManager.getCurrentPosition()) {
                    ++mCurrentIndex;
                    mRecyclerView.smoothScrollToPosition(mCurrentIndex);
                    mHandler.sendEmptyMessageDelayed(WHAT_AUTO_PLAY, mAutoPlayDuration);
                    refreshIndicator();
                }
            }
            return false;
        }
    });

    public BannerLayout(Context context) {
        this(context, null);
    }

    public BannerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.BannerLayoutStyle);
    }

    public BannerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    protected void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BannerLayout, defStyleAttr, 0);
        mShowIndicator = typedArray.getBoolean(R.styleable.BannerLayout_bl_showIndicator, true);
        mAutoPlayDuration = typedArray.getInt(R.styleable.BannerLayout_bl_interval, 4000);
        mIsAutoPlaying = typedArray.getBoolean(R.styleable.BannerLayout_bl_autoPlaying, true);
        mItemSpace = typedArray.getDimensionPixelSize(R.styleable.BannerLayout_bl_itemSpace, ResUtils.getDimensionPixelSize(context, R.dimen.default_recycler_banner_itemSpace));
        mCenterScale = typedArray.getFloat(R.styleable.BannerLayout_bl_centerScale, 1.2F);
        mMoveSpeed = typedArray.getFloat(R.styleable.BannerLayout_bl_moveSpeed, 1.0F);
        mSelectedDrawable = ResUtils.getDrawableAttrRes(getContext(), typedArray, R.styleable.BannerLayout_bl_indicatorSelectedSrc);
        mUnselectedDrawable = ResUtils.getDrawableAttrRes(getContext(), typedArray, R.styleable.BannerLayout_bl_indicatorUnselectedSrc);
        int indicatorSize = typedArray.getDimensionPixelSize(R.styleable.BannerLayout_bl_indicatorSize, ResUtils.getDimensionPixelSize(context, R.dimen.default_recycler_banner_indicatorSize));
        int indicatorSelectedColor = typedArray.getColor(R.styleable.BannerLayout_bl_indicatorSelectedColor, Color.parseColor("#FFFFFF"));
        int indicatorUnselectedColor = typedArray.getColor(R.styleable.BannerLayout_bl_indicatorUnselectedColor, Color.parseColor("#80FFFFFF"));
        if (mSelectedDrawable == null) {
            //绘制默认选中状态图形
            GradientDrawable selectedGradientDrawable = new GradientDrawable();
            selectedGradientDrawable.setShape(GradientDrawable.RECTANGLE);
            selectedGradientDrawable.setColor(indicatorSelectedColor);
            selectedGradientDrawable.setSize(indicatorSize, ResUtils.getDimensionPixelSize(context, R.dimen.default_recycler_banner_indicatorSize_Radius));
            selectedGradientDrawable.setCornerRadius(ResUtils.getDimensionPixelSize(context, R.dimen.default_recycler_banner_indicatorSize_Radius));
            mSelectedDrawable = new LayerDrawable(new Drawable[]{selectedGradientDrawable});
        }
        if (mUnselectedDrawable == null) {
            //绘制默认未选中状态图形
            GradientDrawable unSelectedGradientDrawable = new GradientDrawable();
            unSelectedGradientDrawable.setShape(GradientDrawable.RECTANGLE);
            unSelectedGradientDrawable.setColor(indicatorUnselectedColor);
            unSelectedGradientDrawable.setSize(indicatorSize, ResUtils.getDimensionPixelSize(context, R.dimen.default_recycler_banner_indicatorSize_Radius));
            unSelectedGradientDrawable.setCornerRadius(ResUtils.getDimensionPixelSize(context, R.dimen.default_recycler_banner_indicatorSize_Radius));
            mUnselectedDrawable = new LayerDrawable(new Drawable[]{unSelectedGradientDrawable});
        }

        mIndicatorMargin = typedArray.getDimensionPixelSize(R.styleable.BannerLayout_bl_indicatorSpace, ResUtils.getDimensionPixelSize(context, R.dimen.default_recycler_banner_indicatorSpace));
        int marginStart = typedArray.getDimensionPixelSize(R.styleable.BannerLayout_bl_indicatorMarginLeft, ResUtils.getDimensionPixelSize(context, R.dimen.default_recycler_banner_indicatorMarginLeft));
        int marginEnd = typedArray.getDimensionPixelSize(R.styleable.BannerLayout_bl_indicatorMarginRight, ResUtils.getDimensionPixelSize(context, R.dimen.default_recycler_banner_indicatorMarginRight));
        int marginBottom = typedArray.getDimensionPixelSize(R.styleable.BannerLayout_bl_indicatorMarginBottom, ResUtils.getDimensionPixelSize(context, R.dimen.default_recycler_banner_indicatorMarginBottom));
        int g = typedArray.getInt(R.styleable.BannerLayout_bl_indicatorGravity, 0);
        int gravity;
        if (g == 0) {
            gravity = GravityCompat.START;
        } else if (g == 2) {
            gravity = GravityCompat.END;
        } else {
            gravity = Gravity.CENTER;
        }
        int orientation = typedArray.getInt(R.styleable.BannerLayout_bl_orientation, OrientationHelper.HORIZONTAL);
        typedArray.recycle();

        //轮播图部分
        mRecyclerView = new RecyclerView(context);
        LayoutParams vpLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(mRecyclerView, vpLayoutParams);
        mLayoutManager = new BannerLayoutManager(getContext(), orientation);
        mLayoutManager.setOnPageChangeListener(new BannerLayoutManager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (onPageChangeListener != null) {
                    onPageChangeListener.onPageSelected(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (onPageChangeListener != null) {
                    onPageChangeListener.onPageScrollStateChanged(state);
                }
            }
        });
        mLayoutManager.setItemSpace(mItemSpace);
        mLayoutManager.setCenterScale(mCenterScale);
        mLayoutManager.setMoveSpeed(mMoveSpeed);
        mRecyclerView.setLayoutManager(mLayoutManager);
        new CenterSnapHelper().attachToRecyclerView(mRecyclerView);

        //指示器部分
        mIndicatorContainer = new RecyclerView(context);
        LinearLayoutManager indicatorLayoutManager = new LinearLayoutManager(context, orientation, false);
        mIndicatorContainer.setLayoutManager(indicatorLayoutManager);
        mIndicatorAdapter = new IndicatorAdapter();
        mIndicatorContainer.setAdapter(mIndicatorAdapter);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.BOTTOM | gravity;
        params.setMargins(marginStart, 0, marginEnd, marginBottom);
        params.setMarginStart(marginStart);
        params.setMarginEnd(marginEnd);
        addView(mIndicatorContainer, params);
        if (!mShowIndicator) {
            mIndicatorContainer.setVisibility(GONE);
        }
    }

    // 设置是否禁止滚动播放
    public BannerLayout setAutoPlaying(boolean isAutoPlaying) {
        mIsAutoPlaying = isAutoPlaying;
        setPlaying(this.mIsAutoPlaying);
        return this;
    }

    public boolean isPlaying() {
        return mIsPlaying;
    }

    public void setInfinite() {
        if (mLayoutManager != null) {
            mLayoutManager.setInfinite(mBannerSize >= 2);
        }
    }

    public void smoothScrollToPosition() {
        if (mRecyclerView != null && mRecyclerView.getAdapter() != null && mRecyclerView.getAdapter().getItemCount() > 0) {
            mBannerSize = mRecyclerView.getAdapter().getItemCount();
            mIndicatorAdapter.notifyDataSetChanged();
            mCurrentIndex = 0;
            refreshIndicator();
            mRecyclerView.smoothScrollToPosition(0);
        }
    }

    //设置是否显示指示器
    public BannerLayout setShowIndicator(boolean showIndicator) {
        mShowIndicator = showIndicator;
        mIndicatorContainer.setVisibility(showIndicator ? VISIBLE : GONE);
        return this;
    }

    //设置当前图片缩放系数
    public BannerLayout setCenterScale(float centerScale) {
        mCenterScale = centerScale;
        mLayoutManager.setCenterScale(centerScale);
        return this;
    }

    //设置跟随手指的移动速度
    public BannerLayout setMoveSpeed(float moveSpeed) {
        mMoveSpeed = moveSpeed;
        mLayoutManager.setMoveSpeed(moveSpeed);
        return this;
    }

    //设置图片间距
    public BannerLayout setItemSpace(int itemSpace) {
        mItemSpace = itemSpace;
        mLayoutManager.setItemSpace(itemSpace);
        return this;
    }

    /**
     * 设置轮播间隔时间
     *
     * @param autoPlayDuration 时间毫秒
     */
    public BannerLayout setAutoPlayDuration(int autoPlayDuration) {
        mAutoPlayDuration = autoPlayDuration;
        return this;
    }

    public BannerLayout setOrientation(int orientation) {
        mLayoutManager.setOrientation(orientation);
        return this;
    }

    /**
     * 设置是否自动播放（上锁）
     *
     * @param playing 开始播放
     */
    protected synchronized void setPlaying(boolean playing) {
        if (mIsAutoPlaying && mHasInit) {
            if (!mIsPlaying && playing) {
                mHandler.sendEmptyMessageDelayed(WHAT_AUTO_PLAY, mAutoPlayDuration);
                mIsPlaying = true;
            } else if (mIsPlaying && !playing) {
                mHandler.removeMessages(WHAT_AUTO_PLAY);
                mIsPlaying = false;
            }
        }
    }


    /**
     * 设置轮播数据集
     */
    public void setAdapter(RecyclerView.Adapter adapter) {
        mHasInit = false;
        mRecyclerView.setAdapter(adapter);
        mBannerSize = adapter.getItemCount();
//        mLayoutManager.setInfinite(mBannerSize >= 2);
        mLayoutManager.setInfinite(true);
        if (mIndicatorAdapter != null) {
            mIndicatorAdapter.notifyDataSetChanged();
        }
        setPlaying(true);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dx != 0) {
                    setPlaying(false);
                }
            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                int first = mLayoutManager.getCurrentPosition();
                if (mCurrentIndex != first) {
                    mCurrentIndex = first;
                }
                if (newState == SCROLL_STATE_IDLE) {
                    setPlaying(true);
                }
                refreshIndicator();
            }
        });
        mHasInit = true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setPlaying(false);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                setPlaying(true);
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setPlaying(true);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        setPlaying(false);
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility == VISIBLE) {
            setPlaying(true);
        } else {
            setPlaying(false);
        }
    }

    /**
     * fragment嵌套可能导致{@link BannerLayout#onWindowVisibilityChanged(int)} 不会触发,需要手动调用停止动画
     */
    public void onPause() {
        setPlaying(false);
    }

    /**
     * fragment嵌套可能导致{@link BannerLayout#onWindowVisibilityChanged(int)} 不会触发,需要手动调用开始动画
     */
    public void onResume() {
        setPlaying(true);
    }

    /**
     * 标示点适配器
     */
    protected class IndicatorAdapter extends RecyclerView.Adapter {

        int currentPosition = 0;

        public void setPosition(int currentPosition) {
            this.currentPosition = currentPosition;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ImageView bannerPoint = new ImageView(getContext());
            RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(mIndicatorMargin, mIndicatorMargin, mIndicatorMargin, 0);
            bannerPoint.setLayoutParams(lp);
            return new RecyclerView.ViewHolder(bannerPoint) {
            };
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ImageView bannerPoint = (ImageView) holder.itemView;
            bannerPoint.setImageDrawable(currentPosition == position ? mSelectedDrawable : mUnselectedDrawable);

        }

        @Override
        public int getItemCount() {
            return mBannerSize;
        }
    }

    /**
     * 改变导航的指示点
     */
    protected synchronized void refreshIndicator() {
        if (mBannerSize > 0) {
            final int position = mCurrentIndex % mBannerSize;
            if (mShowIndicator) {
                mIndicatorAdapter.setPosition(position);
                mIndicatorAdapter.notifyDataSetChanged();
            }
            if (mOnIndicatorIndexChangedListener != null) {
                mOnIndicatorIndexChangedListener.onIndexChanged(position);
            }
        }
    }

    public interface OnBannerItemClickListener {
        void onItemClick(int position);
    }

    public BannerLayout setOnIndicatorIndexChangedListener(OnIndicatorIndexChangedListener onIndicatorIndexChangedListener) {
        mOnIndicatorIndexChangedListener = onIndicatorIndexChangedListener;
        return this;
    }

    /**
     * 索引变化监听
     */
    public interface OnIndicatorIndexChangedListener {
        /**
         * 索引变化
         *
         * @param position
         */
        void onIndexChanged(int position);
    }

    public void setOnPageChangeListener(BannerLayoutManager.OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
    }

    private BannerLayoutManager.OnPageChangeListener onPageChangeListener;

    public interface OnPageChangeListener {
        void onPageSelected(int position);

        void onPageScrollStateChanged(int state);
    }
}
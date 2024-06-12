/*
 *  NavigationBar.java, 2023-08-07
 *  Copyright © 2015-2023  Liangyihui. All rights reserved.
 */

package net.liangyihui.android.ui.widget.bottomnavi;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;

import net.liangyihui.android.ui.BuildConfig;
import net.liangyihui.android.ui.R;
import net.liangyihui.android.ui.utils.ResUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lijian
 * @date 2023/8/7
 * @description
 * @since
 */
public class NavigationBar extends LinearLayoutCompat {
    private String current;
    private float mTextSize;
    private int mTextSelectColor;
    private int mTextUnselectColor;

    public NavigationBar(Context context) {
        this(context, null, 0);
    }

    public NavigationBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NavigationBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        obtainAttributes(context, attrs);
        init();
    }

    private void obtainAttributes(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.NavigationBar);
        mTextSize = array.getDimension(R.styleable.NavigationBar_nb_textSize, dp2px(11f));
        mTextSelectColor = array.getColor(R.styleable.NavigationBar_nb_textSelectColor, Color.parseColor("#DA1572"));
        mTextUnselectColor = array.getColor(R.styleable.NavigationBar_nb_textUnselectColor, Color.parseColor("#999999"));
        array.recycle();
    }

    private void init() {
        setOrientation(HORIZONTAL);
    }

    private List<Tab> tabs = new ArrayList<>();
    private Map<String, Integer> unreadMaps = new HashMap<>();

    public void setTabs(@NonNull List<Tab> tabs) {
        this.tabs = tabs;
        if (TextUtils.isEmpty(this.current) && tabs.size() > 0) {
            this.current = tabs.get(0).getTag();
        }
//        if (this.current >= tabs.size()) {
//            this.current--;
//        }
        notifyDataSetChanged();
        if (this.targetTag != null) {
            Tab tab = null;
            for (int i = 0; i < tabs.size(); i++) {
                if (tabs.get(i).getTag().equals(this.targetTag)) {
                    tab = tabs.get(i);
                    break;
                }
            }
            if (tab != null) {
                this.targetTag = null;
                updateTabStyle(tab.getTag());
            } else {
                if (this.onTabSelectListener != null) {
                    this.onTabSelectListener.onTabSelect(this.getTabByTag(this.current), this.current);
                }
            }
        } else {
            if (this.onTabSelectListener != null) {
                this.onTabSelectListener.onTabSelect(this.getTabByTag(this.current), this.current);
            }
        }
    }

    public void setCurrent(@NonNull String tag) {
//        if (position < 0 || position >= this.tabs.size()) {
//            return;
//        }
        this.updateTabStyle(tag);
    }

    public void setCurrentByPosition(int position) {
        if (this.tabs.size() > 0) {
            if (position < 0) {
                position = 0;
            } else if (position >= this.tabs.size()) {
                position--;
            }
            this.updateTabStyle(this.tabs.get(position).getTag());

        }

    }

    public Tab getTabByTag(@NonNull String tag) {
        for (Tab t : this.tabs) {
            if (tag.equals(t.getTag())) {
                return t;
            }
        }
        return null;
    }

//    public int getCurrent() {
//        return this.current;
//    }

    public Tab getCurrentTab() {
        return this.getTabByTag(this.current);
    }

    private void notifyDataSetChanged() {
        removeAllViews();
        for (int i = 0; i < tabs.size(); i++) {
            Tab tab = tabs.get(i);
            ConstraintLayout constraintLayout = new ConstraintLayout(getContext());
            constraintLayout.setTag(tab);
            constraintLayout.setOnClickListener(v -> {
                if (v.getTag() instanceof Tab) {
                    Tab clickedTab = (Tab) v.getTag();
                    if (clickedTab.getGranted()) {
                        this.targetTag = null;
                        updateTabStyle(clickedTab.getTag());
                    } else {
                        this.targetTag = clickedTab.getTag();
                        if (this.onTabSelectListener != null) {
                            this.onTabSelectListener.onTabRequest(clickedTab, clickedTab.getTag());
                        }
                    }
                }
            });
            constraintLayout.setLayoutParams(new LinearLayoutCompat.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
            ConstraintLayout.LayoutParams itemLayoutParamsImage = new ConstraintLayout.LayoutParams(ResUtils.getDimensionPixelSize(getContext(), R.dimen.default_navigation_bar_itemSize), ResUtils.getDimensionPixelSize(getContext(), R.dimen.default_navigation_bar_itemSize));
            itemLayoutParamsImage.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
            itemLayoutParamsImage.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
            itemLayoutParamsImage.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
            itemLayoutParamsImage.topMargin = dp2px(8f);
            ImageView imageView = new ImageView(getContext());
            imageView.setId(R.id.navi_bar_item_iv);
            this.loadImage(tab, imageView, tab.getTag());
            imageView.setLayoutParams(itemLayoutParamsImage);
            constraintLayout.addView(imageView);
            ConstraintLayout.LayoutParams itemLayoutParamsTitle = new ConstraintLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            itemLayoutParamsTitle.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
            itemLayoutParamsTitle.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
            itemLayoutParamsTitle.topToBottom = R.id.navi_bar_item_iv;
            itemLayoutParamsTitle.topMargin = dp2px(1f);
            TextView textView = new TextView(getContext());
            textView.setId(R.id.navi_bar_item_title);
            textView.setText(tab.getTitle());
            textView.setTextColor(current.equals(tab.getTag()) ? mTextSelectColor : mTextUnselectColor);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
            textView.setGravity(Gravity.CENTER);
            textView.setLayoutParams(itemLayoutParamsTitle);
            constraintLayout.addView(textView);
            addView(constraintLayout);
        }
        for (int i = 0; i < tabs.size(); i++) {
            Tab tab = tabs.get(i);
            if (!TextUtils.isEmpty(tab.getTag())) {
                //未授权则移除未读数
                if (!tab.getGranted()) {
                    this.unreadMaps.remove(tab.getTag());
                }
                if (this.unreadMaps.containsKey(tab.getTag())) {
                    Integer num = this.unreadMaps.get(tab.getTag());
                    if (num != null) {
                        showMsg(tab.getTag(), num);
                    } else {
                        hideMsg(tab.getTag());
                    }
                }
            }
        }
    }

    /**
     * 请求权限的目标tag,用于授权后，跳转指定tab
     */
    private String targetTag = null;

    private void updateTabStyle(String tag) {
        if (this.current.equals(tag)) {
            if (this.onTabSelectListener != null) {
                this.onTabSelectListener.onTabReselect(this.getTabByTag(this.current), this.current);
            }
            return;
        }
        Tab tab = this.getTabByTag(this.current);
        if (tab != null) {
            View view = getChildAt(getPositionByTag(this.current));
            ImageView imageView = view.findViewById(R.id.navi_bar_item_iv);
            this.loadImage(tab, imageView, tag);
            TextView textView = view.findViewById(R.id.navi_bar_item_title);
            textView.setTextColor(mTextUnselectColor);
            textView.setText(tab.getTitle());
            this.current = tag;
            tab = this.getTabByTag(this.current);
            if (tab != null) {
                view = getChildAt(getPositionByTag(this.current));
                imageView = view.findViewById(R.id.navi_bar_item_iv);
                this.loadImage(tab, imageView, tag);
                textView = view.findViewById(R.id.navi_bar_item_title);
                textView.setTextColor(mTextSelectColor);
                if (this.onTabSelectListener != null) {
                    this.onTabSelectListener.onTabSelect(this.getTabByTag(this.current), this.current);
                }
            }

        }


    }

    // show MsgTipView

    /**
     * 显示未读消息
     *
     * @param position 显示tab位置
     * @param num      num小于等于0显示红点,num大于0显示数字
     */
    public void showMsg(int position, int num) {
        if (position >= this.tabs.size()) {
            return;
        }

        ConstraintLayout tabView = (ConstraintLayout) getChildAt(position);
        NavigationBarMsgView tipView = tabView.findViewById(R.id.navi_bar_item_msg_tip);
        if (tipView == null) {
            ConstraintLayout.LayoutParams itemLayoutParamsMsgView = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            itemLayoutParamsMsgView.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
            itemLayoutParamsMsgView.startToEnd = R.id.navi_bar_item_iv;
            itemLayoutParamsMsgView.topMargin = dp2px(2f);
            itemLayoutParamsMsgView.leftMargin = dp2px(-10f);
            tipView = new NavigationBarMsgView(getContext());
            tipView.setId(R.id.navi_bar_item_msg_tip);
            tipView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 11f);
            tipView.setGravity(Gravity.CENTER);
            tipView.setTextColor(Color.WHITE);
            tipView.setLayoutParams(itemLayoutParamsMsgView);
            tipView.setVisibility(GONE);
            tabView.addView(tipView);
        }
        UnreadMsgUtils.show(tipView, num);
    }

    public void showMsg(@NonNull String tag, int num) {
        for (int i = 0; i < this.tabs.size(); i++) {
            Tab tab = this.tabs.get(i);
            if (tag.equals(tab.getTag())) {
                this.unreadMaps.put(tab.getTag(), num);
                this.showMsg(i, num);
                break;
            }
        }
    }

    /**
     * 显示未读红点
     *
     * @param position 显示tab位置
     */
    public void showDot(int position) {
        showMsg(position, 0);
    }

    public void showDot(@NonNull String tag) {
        this.showMsg(tag, 0);
    }

    /**
     * 隐藏未读红点
     *
     * @param position 隐藏tab位置
     */
    public void hideDot(int position) {
        hideMsg(position);
    }

    public void hideDot(@NonNull String tag) {
        hideMsg(tag);
    }

    public void hideMsg(int position) {
        if (position >= this.tabs.size()) {
            return;
        }

        View tabView = getChildAt(position);
        NavigationBarMsgView tipView = tabView.findViewById(R.id.navi_bar_item_msg_tip);
        if (tipView != null) {
            tipView.setVisibility(View.GONE);
        }
    }

    public void hideMsg(@NonNull String tag) {
        this.unreadMaps.remove(tag);
        for (int i = 0; i < this.tabs.size(); i++) {
            Tab tab = this.tabs.get(i);
            if (tag.equals(tab.getTag())) {
                this.hideMsg(i);
                break;
            }
        }
    }

    public int getPositionByTag(Tab clickTab) {
        if (clickTab == null || clickTab.getTag() == null) {
            return 0;
        }
        for (int i = 0; i < this.tabs.size(); i++) {
            Tab tab = this.tabs.get(i);
            if (clickTab.getTag().equals(tab.getTag())) {
                return i;
            }
        }
        return 0;
    }

    public int getPositionByTag(String tag) {
        if (TextUtils.isEmpty(tag)) {
            return 0;
        }
        for (int i = 0; i < this.tabs.size(); i++) {
            Tab tab = this.tabs.get(i);
            if (tag.equals(tab.getTag())) {
                return i;
            }
        }
        return 0;
    }


    private void loadImage(@Nullable Tab tab, ImageView imageView, String tag) {
        if (tab == null) return;
        if (this.iconLoader != null) {
            if (!TextUtils.isEmpty(tab.getSelectedIconUrl())) {
                this.iconLoader.loadImageUrl(imageView, current.equals(tag) ? tab.getSelectedIconUrl() : tab.getUnSelectedIconUrl());
            } else {
                this.iconLoader.loadImageUrl(imageView, current.equals(tag) ? tab.getSelectedIcon() : tab.getUnSelectedIcon());
            }
        } else {
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageResource(current.equals(tag) ? tab.getSelectedIcon() : tab.getUnSelectedIcon());
        }
    }

    private int dp2px(float dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    private int sp2px(float sp) {
        final float scale = getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * scale + 0.5f);
    }

    private OnTabSelectListener onTabSelectListener;

    public void setOnTabSelectListener(OnTabSelectListener listener) {
        this.onTabSelectListener = listener;
    }

    /**
     * 图片加载loader
     */
    private IconLoader iconLoader;

    public void setIconLoader(IconLoader iconLoader) {
        this.iconLoader = iconLoader;
    }

    public interface IconLoader {
        /**
         * 加载网络资源
         */
        void loadImageUrl(ImageView iv, String url);

        /**
         * 加载本地资源
         */
        void loadImageUrl(ImageView iv, @RawRes @DrawableRes @Nullable Integer resourceId);
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable ss = super.onSaveInstanceState();
        SavedState savedState = new SavedState(ss);
        savedState.currentPosition = this.current;
        savedState.targetTag = this.targetTag;
        log("onSaveInstanceState" + savedState.currentPosition);
        return savedState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        //调用别的方法，把保存的数据重新赋值给当前的自定义View
        log("onRestoreInstanceState" + ss.currentPosition);
        this.current = ss.currentPosition;
        this.targetTag = ss.targetTag;
        if (this.onTabSelectListener != null && tabs.size() > 0) {
//            if (this.current >= tabs.size()) {
//                this.current--;
//            }
            if (TextUtils.isEmpty(this.current)) {
                this.current = tabs.get(0).getTag();
            }
            this.onTabSelectListener.onTabSelect(this.getTabByTag(this.current), this.current);
        }
        this.notifyDataSetChanged();
    }

    private static class SavedState extends BaseSavedState {

        //当前的tab的位置
        private String currentPosition;
        private String targetTag;

        public SavedState(Parcel source) {
            super(source);
            currentPosition = source.readString();
            targetTag = source.readString();
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeString(currentPosition);
            out.writeString(targetTag);
        }

        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {

            @Override
            public SavedState createFromParcel(Parcel source) {
                return new SavedState(source);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    private void log(String message) {
        if (BuildConfig.DEBUG) {
            Log.e("NavigationBar", TextUtils.isEmpty(message) ? "" : message);
        }

    }
}

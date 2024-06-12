/*
 *  MainActivity2.java, 2023-08-09
 *  Copyright © 2015-2023  Liangyihui. All rights reserved.
 */

package com.lyh.ywx.application;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentOnAttachListener;

import com.bumptech.glide.Glide;
import com.lyh.ywx.application.bottombar.FindFragment;
import com.lyh.ywx.application.bottombar.MineFragment;
import com.lyh.ywx.application.bottombar.MsgFragment;
import com.lyh.ywx.application.bottombar.NaviFragment;
import com.lyh.ywx.application.bottombar.WorkFragment;

import net.liangyihui.android.ui.widget.bottomnavi.FragmentUtils;
import net.liangyihui.android.ui.widget.bottomnavi.NavigationBar;
import net.liangyihui.android.ui.widget.bottomnavi.OnTabSelectListener;
import net.liangyihui.android.ui.widget.bottomnavi.Tab;

import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends AppCompatActivity implements NaviFragment.OnClickListener {
    private NavigationBar bar;

   private final FragmentOnAttachListener fragmentOnAttachListener= (fragmentManager, fragment) -> {
       if (fragment instanceof NaviFragment) {
           NaviFragment naviFragment = (NaviFragment) fragment;
           naviFragment.setOnClickListener(MainActivity2.this);
       }
   };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().addFragmentOnAttachListener(fragmentOnAttachListener);
        setContentView(R.layout.activity_main2);
        bar = findViewById(R.id.navi_bar);
        bar.setIconLoader(new NavigationBar.IconLoader() {
            @Override
            public void loadImageUrl(ImageView iv, String url) {
//                Glide.with(iv).clear(iv);
                Glide.with(iv).load(url).centerCrop().into(iv);
            }

            @Override
            public void loadImageUrl(ImageView iv, @Nullable Integer resourceId) {
//                iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                iv.setImageResource(resourceId);
                Glide.with(iv).load(resourceId).centerCrop().into(iv);
            }
        });
        bar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(@Nullable Tab tab, @NonNull String tag) {
                log("onTabSelect=" + tag);
                FragmentUtils.show(getSupportFragmentManager(), R.id.fl_container, tab, null);
            }

            @Override
            public void onTabReselect(@Nullable Tab tab, @NonNull String tag) {
                log("onTabReselect=" + tag);
            }

            @Override
            public void onTabRequest(@Nullable Tab tab, @NonNull String tag) {

            }

        });
        bar.setTabs(getMockTab());
//        bar.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                bar.showDot(4);
////                bar.showMsg(4,5);
//                bar.showMsg(2,99);
//                bar.showMsg(0,2);
//            }
//        },2000);
    }

    private void log(String message) {
        Log.e("MainActivity2", TextUtils.isEmpty(message) ? "" : message);
    }

    private List<Tab> getMockTab() {
        List<Tab> tabs = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Tab tab = null;
            switch (i) {
                case 0:
                    tab = new Tab(NaviFragment.class, "" + i);
                    tab.setSelectedIcon(R.mipmap.ic_tab_news_checked);
                    tab.setUnSelectedIcon(R.mipmap.ic_tab_news_unchecked);
//                    tab.setSelectedIconUrl("https://bosdev.liangyihui.net/temp/1638871004.png");
//                    tab.setUnSelectedIconUrl("https://bosdev.liangyihui.net/temp/1638870814.png");
                    tab.setTitle("首页");
                    break;
                case 1:
                    tab = new Tab(FindFragment.class, "" + i);
                    tab.setSelectedIcon(R.mipmap.ic_tab_tool_checked);
                    tab.setUnSelectedIcon(R.mipmap.ic_tab_tool_unchecked);
                    tab.setTitle("发现");
                    break;
                case 2:
                    tab = new Tab(WorkFragment.class, "" + i);
                    tab.setSelectedIcon(R.mipmap.iv_tab_consult_doctor_checked);
                    tab.setUnSelectedIcon(R.mipmap.iv_tab_consult_doctor_unchecked);
                    tab.setTitle("工作");
                    break;
                case 3:
                    tab = new Tab(MsgFragment.class, "" + i);
                    tab.setSelectedIcon(R.mipmap.ic_tab_suffer_manage_checked);
                    tab.setUnSelectedIcon(R.mipmap.ic_tab_suffer_manage_unchecked);
                    tab.setTitle("消息");
                    break;
                case 4:
                    tab = new Tab(MineFragment.class, "" + i);
                    tab.setSelectedIcon(R.mipmap.ic_tab_me_checked);
                    tab.setUnSelectedIcon(R.mipmap.ic_tab_me_unchecked);
                    tab.setTitle("我的");
                    break;
                default:
                    break;
            }
            tabs.add(tab);
        }
        return tabs;
    }

//    @Override
//    public void onAttachFragment(Fragment fragment) {
//        if (fragment instanceof NaviFragment) {
//            NaviFragment naviFragment = (NaviFragment) fragment;
//            naviFragment.setOnClickListener(this);
//        }
//    }

    @Override
    public void showDot() {
        bar.showDot(0);
        bar.showDot(4);
    }

    @Override
    public void hideDot() {
        bar.hideDot(0);
        bar.hideDot(4);
    }

    @Override
    public void showMsg(int count) {
        bar.showMsg(0, count);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getSupportFragmentManager().removeFragmentOnAttachListener(fragmentOnAttachListener);
    }
}
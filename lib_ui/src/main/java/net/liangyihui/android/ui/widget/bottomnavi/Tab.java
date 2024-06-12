/*
 *  Tab.java, 2023-08-07
 *  Copyright © 2015-2023  Liangyihui. All rights reserved.
 */

package net.liangyihui.android.ui.widget.bottomnavi;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/**
 * @author lijian
 * @date 2023/8/7
 * @description
 * @since
 */
public class Tab {
    private Class<? extends Fragment> fragmentClass;
    private String tag;
    private String extra;
    private String title;
    private int selectedIcon;
    private int unSelectedIcon;
    private String selectedIconUrl;
    private String unSelectedIconUrl;

    /**
     * 1.默认都是授权的
     * 2.需要权限才能切换的tab，赋值false，授权成功后更新为true，并调用事件回调
     */
    private boolean granted = true;

    public Tab() {
//        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public Tab(@NonNull Class<? extends Fragment> fragmentClass, @NonNull String tag) {
        this.fragmentClass = fragmentClass;
        this.tag = tag;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Class<? extends Fragment> getFragmentClass() {
        return fragmentClass;
    }

    public void setFragmentClass(Class<? extends Fragment> fragmentClass) {
        this.fragmentClass = fragmentClass;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSelectedIcon() {
        return selectedIcon;
    }

    public void setSelectedIcon(int selectedIcon) {
        this.selectedIcon = selectedIcon;
    }

    public int getUnSelectedIcon() {
        return unSelectedIcon;
    }

    public void setUnSelectedIcon(int unSelectedIcon) {
        this.unSelectedIcon = unSelectedIcon;
    }

    public String getSelectedIconUrl() {
        return selectedIconUrl;
    }

    public void setSelectedIconUrl(String selectedIconUrl) {
        this.selectedIconUrl = selectedIconUrl;
    }

    public String getUnSelectedIconUrl() {
        return unSelectedIconUrl;
    }

    public void setUnSelectedIconUrl(String unSelectedIconUrl) {
        this.unSelectedIconUrl = unSelectedIconUrl;
    }

    public void setGranted(boolean granted) {
        this.granted = granted;
    }

    public boolean getGranted() {
        return this.granted;
    }
}

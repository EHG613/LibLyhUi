/*
 *  FragmentUtils.java, 2023-08-09
 *  Copyright © 2015-2023  Liangyihui. All rights reserved.
 */

package net.liangyihui.android.ui.widget.bottomnavi;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;

/**
 * @author lijian
 * @date 2023/8/9
 * @description
 * @since
 */
public class FragmentUtils {

    /**
     * 显示指定tag的Fragment
     */
    public static void show(@NonNull final FragmentManager fragmentManager, final int containerId, @NonNull final Tab tab, @Nullable final Bundle args) {
        Fragment fragment = fragmentManager.findFragmentByTag(tab.getTag());
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        for (Fragment old : fragmentManager.getFragments()) {
            if (!old.isHidden()) {
                fragmentTransaction.hide(old);
            }
        }
        if (fragment == null) {
            fragmentTransaction.add(containerId, tab.getFragmentClass(), args, tab.getTag());
        } else {
            fragmentTransaction.show(fragment);
        }
        fragmentTransaction.commitAllowingStateLoss();
    }

    /**
     * 移除未授权的Fragment
     */
    public static void remove(@NonNull final FragmentManager fragmentManager, @Nullable final List<Tab> tabList) {
        if (tabList == null) {
            return;
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        for (Tab tab : tabList) {
            if (!tab.getGranted()) {
                Fragment fragment = fragmentManager.findFragmentByTag(tab.getTag());
                if (fragment != null) {
                    fragmentTransaction.remove(fragment);
                }
            }
        }
        fragmentTransaction.commitAllowingStateLoss();
        fragmentManager.executePendingTransactions();
    }

    @Nullable
    public static Fragment getFragment(@NonNull final FragmentManager fragmentManager, @NonNull final Tab tab) {
        return fragmentManager.findFragmentByTag(tab.getTag());
    }

    @Nullable
    public static Fragment getFragment(@NonNull final FragmentManager fragmentManager, @Nullable final String tag) {
        return fragmentManager.findFragmentByTag(tag);
    }

    /**
     * 是否真正的对用户可见
     *
     * @return
     */
    public static boolean isRealVisible(Fragment fragment) {
        if (fragment.getParentFragment() == null) {
            return fragment.isVisible();
        } else {
            return fragment.isVisible() && fragment.getParentFragment().isVisible();
        }
    }

}

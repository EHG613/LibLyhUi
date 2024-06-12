/*
 *  OnTabSelectListener.java, 2023-08-04
 *  Copyright © 2015-2023  Liangyihui. All rights reserved.
 */

package net.liangyihui.android.ui.widget.bottomnavi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author lijian
 * @date 2023/8/4
 * @description
 * @since
 */
public interface OnTabSelectListener {
    /**
     * {@link Tab#getGranted()},true:有权限，触发tab选中回调事件
     * @param tab 选中的tab对象
     * @param tag 选中的tab标记
     */
    void onTabSelect(@Nullable Tab tab, @NonNull String tag);
    /**
     * {@link Tab#getGranted()},true:有权限，触发tab再次选中回调事件
     * @param tab 选中的tab对象
     * @param tag 选中的tab标记
     */
    void onTabReselect(@Nullable Tab tab,@NonNull String tag);
    /**
     * {@link Tab#getGranted()},false:无权限，触发tab权限请求回调
     * @param tab 选中的tab对象
     * @param tag 选中的tab标记
     */
    void onTabRequest(@Nullable Tab tab,@NonNull String tag);
}

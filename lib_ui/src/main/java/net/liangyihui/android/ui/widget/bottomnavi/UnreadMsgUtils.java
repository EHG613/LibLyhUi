/*
 *  UnreadMsgUtils.java, 2023-08-10
 *  Copyright © 2015-2023  Liangyihui. All rights reserved.
 */

package net.liangyihui.android.ui.widget.bottomnavi;

import android.util.DisplayMetrics;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * @author lijian
 * @date 2023/8/10
 * @description 未读消息提示View, 显示小红点或者带有数字的红点:
 * 数字一位,圆
 * 数字两位,圆角矩形,圆角是高度的一半
 * 数字超过两位,显示99+
 */
public class UnreadMsgUtils {
    public static void show(NavigationBarMsgView msgView, int num) {
        if (msgView == null) {
            return;
        }
        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) msgView.getLayoutParams();
        DisplayMetrics dm = msgView.getResources().getDisplayMetrics();
        msgView.setVisibility(View.VISIBLE);
        msgView.setPadding(0, 0, 0, 0);
        if (num <= 0) {
            //圆点,设置默认宽高
            msgView.setStrokeWidth(0);
            msgView.setText("");
            lp.width = dp2px(8, dm.density);
            lp.height = dp2px(8, dm.density);
            lp.leftMargin = dp2px(-4f, dm.density);
            lp.topMargin = dp2px(8f, dm.density);
            msgView.setLayoutParams(lp);
        } else {
            msgView.setStrokeWidth(1);
            lp.height = dp2px(16, dm.density);
            lp.topMargin = dp2px(2f, dm.density);
            lp.leftMargin = dp2px(-10f, dm.density);
            if (num > 0 && num < 10) {
                //圆
                lp.width = dp2px(16, dm.density);
                msgView.setText(num + "");
            } else if (num > 9 && num < 100) {
                //圆角矩形,圆角是高度的一半,设置默认padding
                lp.width = ConstraintLayout.LayoutParams.WRAP_CONTENT;
                msgView.setPadding(dp2px(5, dm.density), 0, dp2px(5, dm.density), 0);
                msgView.setText(num + "");
            } else {//数字超过两位,显示99+
                lp.width = ConstraintLayout.LayoutParams.WRAP_CONTENT;
                msgView.setPadding(dp2px(5, dm.density), 0, dp2px(5, dm.density), 0);
                msgView.setText("99+");
            }
            msgView.setLayoutParams(lp);
        }
    }

    public static void setSize(NavigationBarMsgView rtv, int size) {
        if (rtv == null) {
            return;
        }
        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) rtv.getLayoutParams();
        lp.width = size;
        lp.height = size;
        rtv.setLayoutParams(lp);
    }

    private static int dp2px(float dp, final float density) {
//        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }


}
/*
 *  SimpleNoticeMF.java, 2022-09-28
 *  Copyright © 2015-2022  Liangyihui. All rights reserved.
 */

package net.liangyihui.android.ui.widget.textview.marqueen;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.TextView;

import net.liangyihui.android.ui.R;


/**
 * 简单字幕
 */
public class SimpleNoticeMF extends MarqueeFactory<TextView, String> {
    private LayoutInflater inflater;

    public SimpleNoticeMF(Context context) {
        super(context);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public TextView generateMarqueeItemView(String data) {
        TextView view = (TextView) inflater.inflate(R.layout.marqueen_layout_notice_item, null);
        view.setText(data);
        return view;
    }
}
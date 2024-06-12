/*
 *  NaviFragment.java, 2023-08-09
 *  Copyright © 2015-2023  Liangyihui. All rights reserved.
 */

package com.lyh.ywx.application.bottombar;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.lyh.ywx.application.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NaviFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NaviFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NaviFragment() {
        // Required empty public constructor
    }

    private OnClickListener callback;

    public void setOnClickListener(OnClickListener callback) {
        this.callback = callback;
    }

    public interface OnClickListener {
        void showDot();

        void hideDot();

        void showMsg(int count);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NaviFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NaviFragment newInstance(String param1, String param2) {
        NaviFragment fragment = new NaviFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_navi, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView textView = view.findViewById(R.id.tv);
        textView.setText("首页");
        Button buttonShowDot = view.findViewById(R.id.btn_show_dot);
        Button buttonHideDot = view.findViewById(R.id.btn_hide_dot);
        Button buttonShowMsg2 = view.findViewById(R.id.btn_show_msg_2);
        Button buttonShowMsg99 = view.findViewById(R.id.btn_show_msg_10);
        Button buttonShowMsg100 = view.findViewById(R.id.btn_show_msg_100);
        buttonShowDot.setOnClickListener(v -> {
            if (this.callback != null) {
                this.callback.showDot();
            }
        });
        buttonHideDot.setOnClickListener(v -> {
            if (this.callback != null) {
                this.callback.hideDot();
            }
        });
        buttonShowMsg2.setOnClickListener(v -> {
            if (this.callback != null) {
                this.callback.showMsg(2);
            }
        });
        buttonShowMsg99.setOnClickListener(v -> {
            if (this.callback != null) {
                this.callback.showMsg(99);
            }
        });
        buttonShowMsg100.setOnClickListener(v -> {
            if (this.callback != null) {
                this.callback.showMsg(100);
            }
        });
    }
}
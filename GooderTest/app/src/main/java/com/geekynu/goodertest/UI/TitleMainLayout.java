package com.geekynu.goodertest.UI;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.geekynu.goodertest.R;

/**
 * Created by yuanhonglei on 8/8/16.
 */
public class TitleMainLayout extends LinearLayout {
    public TitleMainLayout(Context context, AttributeSet atrrs) {
        super(context, atrrs);
        LayoutInflater.from(context).inflate(R.layout.title_main, this);
    }
}

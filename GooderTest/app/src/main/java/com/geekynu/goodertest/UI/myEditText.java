package com.geekynu.goodertest.UI;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by yuanhonglei on 8/5/16.
 */
public class myEditText extends EditText {
    private Paint paint;

    public myEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);
    }
}


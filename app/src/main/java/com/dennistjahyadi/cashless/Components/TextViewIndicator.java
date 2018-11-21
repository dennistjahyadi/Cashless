package com.dennistjahyadi.cashless.Components;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;

import com.dennistjahyadi.cashless.R;

/**
 * Created by Denn on 10/2/2016.
 */
public class TextViewIndicator extends android.support.v7.widget.AppCompatTextView {
    Context con;

    public TextViewIndicator(Context context) {
        super(context);
        this.con = context;
        setAttribute();
    }

    public TextViewIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.con = context;
        setAttribute();
    }

    public TextViewIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.con = context;
        setAttribute();
    }

    @Override
    public void setTypeface(Typeface tf) {
        super.setTypeface(tf);
    }

    private void setAttribute() {
        setBackground(ContextCompat.getDrawable(con, R.drawable.circle_background));
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        setGravity(Gravity.CENTER);
    }
}

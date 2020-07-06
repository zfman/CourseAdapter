package com.zhuangfei.toolkit.widget.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import com.zhuangfei.toolkit.R;

public class TransitTextView extends AppCompatTextView {
    public TransitTextView(Context context) {
        this(context,null,0);
    }

    public TransitTextView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TransitTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        textPaint = getPaint();
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setAntiAlias(true);
        textPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        str = getText().toString();
        if(attrs != null){
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TransitTextView, 0, 0);
            try {
                normalColor = a.getColor(R.styleable.TransitTextView_normalColor,0);
                selectColor = a.getColor(R.styleable.TransitTextView_selectColor, 0);
                endProgress = a.getInteger(R.styleable.TransitTextView_endProgress,1);
            } finally {
                a.recycle();
            }
        }
    }


    public void setStr(String str){
        this.str = str;
        setText(str);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (textPaint != null && !TextUtils.isEmpty(str)) {
            textPaint.setColor(normalColor);
            float start = startProgress > 0 ? 0.0f : endProgress;
            float end = startProgress > 0 ? startProgress : 1.0f;
            canvas.save();
            canvas.clipRect(getWidth() * start, 0, getWidth() * end, getHeight());
            canvas.drawText(str, getWidth() / 2, getHeight() - 6, textPaint);
            canvas.restore();

            textPaint.setColor(selectColor);
            clipRect.set(getWidth() * startProgress, 0, getWidth() * endProgress, getHeight());
            canvas.save();
            canvas.clipRect(clipRect);
            canvas.drawText(str, getWidth() / 2, getHeight()-6, textPaint);
            canvas.restore();
        }
    }


    public void setTransitProgress(float startProgress,float endProgress) {
        this.startProgress = startProgress;
        this.endProgress = endProgress;
        postInvalidate();
    }


    private int normalColor;
    private int selectColor;
    private float startProgress,endProgress = 0.0f;
    private String str;
    private RectF clipRect = new RectF();
    private TextPaint textPaint = null;
    private Context context;
}

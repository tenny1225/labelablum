package com.lenovo.album.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.lenovo.album.R;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * Created by noahkong on 17-6-8.
 */

public class FilletView extends View {
    private String text = "";
    private float textSize = 12;
    private int textColor = Color.WHITE;
    private int bgColor = Color.BLACK;
    private Drawable icon;
    private int iconTint = Color.WHITE;
    private float textMargin = 0;

    private Paint paint;

    public FilletView(Context context) {
        super(context);
        init();
    }

    public FilletView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FilletView);
        text = typedArray.getString(R.styleable.FilletView_fv_text);
        textSize = typedArray.getDimension(R.styleable.FilletView_fv_textSize, 12);
        //textSize =  AutoUtils.getPercentWidth1px()*textSize;
        textColor = typedArray.getColor(R.styleable.FilletView_fv_textColor, Color.WHITE);
        bgColor = typedArray.getColor(R.styleable.FilletView_fv_bgColor, Color.BLACK);
        icon = typedArray.getDrawable(R.styleable.FilletView_fv_icon);
        iconTint = typedArray.getColor(R.styleable.FilletView_fv_iconTint, Color.WHITE);
        textMargin = typedArray.getDimension(R.styleable.FilletView_fv_textMargin, 8);
       // textMargin =  AutoUtils.getPercentWidth1px()*textMargin;
        typedArray.recycle();
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    public void setText(String text) {
        this.text = text;
        invalidate();
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
        invalidate();
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        invalidate();
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
        invalidate();
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
        invalidate();
    }

    public void setIconTint(int iconTint) {
        this.iconTint = iconTint;
        invalidate();
    }

    public void setTextMargin(float textMargin) {
        this.textMargin = textMargin;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int w = 0;
        int h = 0;



        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY) {
            h = MeasureSpec.getSize(heightMeasureSpec);

        } else {

            Rect bound = new Rect();
            paint.setTextSize(textSize);
            paint.getTextBounds(text, 0, text.length(), bound);

            int textHeight = bound.height();
            int paddingHeight = getPaddingTop() + getPaddingBottom();

            h = (int) (h + textHeight + paddingHeight + textMargin * 2);

        }

        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY) {
            w = MeasureSpec.getSize(widthMeasureSpec);
        } else {
            int paddingWidth = getPaddingLeft() + getPaddingRight();
            w += paddingWidth;
            Rect bound = new Rect();
            paint.setTextSize(textSize);
            paint.getTextBounds(text, 0, text.length(), bound);
            w += bound.width();
            w += (h - getPaddingTop() - getPaddingBottom());
            w += textMargin ;
            if (icon != null) {
                w += 3 * (bound.height()) / 2;
            }
        }

        setMeasuredDimension(w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(bgColor);
        paint.setStyle(Paint.Style.FILL);

        float radius = (getHeight() - getPaddingTop() - getPaddingBottom()) / 2;
        RectF c1 = new RectF(getPaddingLeft(), getPaddingTop(), getPaddingLeft() + radius * 2, getHeight() - getPaddingBottom());
        canvas.drawArc(c1, -270, 180, true, paint);

        RectF c2 = new RectF(getWidth() - getPaddingRight() - radius * 2, getPaddingTop(), getWidth() - getPaddingRight(), getHeight() - getPaddingBottom());
        canvas.drawArc(c2, -90, 180, true, paint);

        RectF rect = new RectF(
                getPaddingLeft() + radius,
                getPaddingTop(),
                getWidth() - getPaddingRight() - radius,
                getHeight() - getPaddingBottom()
        );

        canvas.drawRect(rect, paint);

        paint.setAlpha(255);
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.CENTER);

        Paint.FontMetricsInt metricsInt = paint.getFontMetricsInt();


        if (icon != null) {
            Rect rectF = new Rect(
                    (int) (rect.right - rect.height()+textMargin/2),
                    (int) (rect.top+textMargin/2),
                    (int) (rect.right-textMargin/2),
                    (int) (rect.bottom-textMargin/2)
            );

            canvas.drawText(text, rect.centerX() - rectF.width() / 2, getHeight() / 2 - metricsInt.bottom / 2 - metricsInt.top / 2, paint);



            DrawableCompat.setTint(icon,iconTint);
            icon.setBounds(rectF);
            icon.draw(canvas);


            Log.d("xz", "22222");
        } else {
            Log.d("xz", "11111");
            canvas.drawText(text, getWidth() / 2, getHeight() / 2 - metricsInt.bottom / 2 - metricsInt.top / 2, paint);
        }

    }
}

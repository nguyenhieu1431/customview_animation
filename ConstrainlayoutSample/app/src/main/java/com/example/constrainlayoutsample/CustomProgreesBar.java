package com.example.constrainlayoutsample;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.concurrent.TimeUnit;

/**
 * Created by Admin on 2/21/2017.
 */

public class CustomProgreesBar extends View {
    private int color;
    private int percent;
    private ValueAnimator mTimerAnimator;

    public CustomProgreesBar(Context context) {
        super(context);
    }

    public CustomProgreesBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    public CustomProgreesBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomProgreesBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttrs(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int desiredWidth = 125;
        int desiredHeight = 80;

        int width;
        int height;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = Math.min(widthSize, desiredWidth);
        } else {
            width = desiredWidth;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.min(desiredHeight, heightSize);
        } else {
            height = desiredHeight;
        }
        if (width < 0) {
            width = 0;
        }
        if (height < 0) {
            height = 0;
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int canvasWidth = getWidth();
        int canvasHeight = getHeight();
        Point centerOfCanvas = new Point(canvasWidth / 2, canvasHeight / 2);


        RectF rect = new RectF(0, 0, canvasWidth, canvasHeight);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        canvas.drawRoundRect(rect, 0f, 0f, paint);

        //draw rect center
        int recWidth = 300;
        int recHeight = 200;

        //left + recWidth/2 = centerOfCanvas.x;
        // recWidth/2+ centerOfCanvas.x=right;

        int left = centerOfCanvas.x - recWidth / 2;
        int right = centerOfCanvas.x + recWidth / 2;
        int top = centerOfCanvas.y - recHeight / 2;
        int bot = centerOfCanvas.y + recHeight / 2;

        Rect rect1 = new Rect(left, top, right, bot);
        Paint rectPaint = new Paint();
        rectPaint.setStyle(Paint.Style.STROKE);
        rectPaint.setColor(Color.WHITE);

        //canvas.drawRect(rect1, rectPaint);

        //draw text center
        float widthText = rectPaint.measureText("CENTER");
        float xText = centerOfCanvas.x - widthText / 2;

        //canvas.drawText("CENTER", xText, canvasHeight / 2, rectPaint);

//        String text = "Center";
//        Rect r = new Rect();
//        canvas.getClipBounds(r);
//        int cHeight = r.height();
//        int cWidth = r.width();
//        paint.setTextAlign(Paint.Align.LEFT);
//        paint.setTextSize(45);
//        paint.getTextBounds(text, 0, text.length(), r);
//        paint.setColor(Color.WHITE);
//        float x = cWidth / 2f - r.width() / 2f - r.left;
//        float y = cHeight / 2f + r.height() / 2f - r.bottom;
//        canvas.drawText(text, x, y, paint);

        //draw line
        Paint linePaint = new Paint();
        linePaint.setColor(Color.WHITE);
        linePaint.setTextSize(30);
        //canvas.drawLine(0, 12, canvasWidth, 12, linePaint);


        Rect barRect = new Rect(0, 0, getWidth(), 80);
        Paint barPaint = new Paint();
        barPaint.setColor(Color.BLUE);
        canvas.drawRect(barRect, barPaint);


        int full = getWidth();
        int progressWidth = percent * full / 100;
        Rect progressRect = new Rect(0, 0, progressWidth, 80);
        barPaint.setColor(Color.WHITE);
        canvas.drawRect(progressRect, barPaint);

        String percentStr = percent + "%";
        float width = barPaint.measureText(percentStr);
        barPaint.setColor(Color.BLACK);
        canvas.drawText(percentStr, progressWidth - width - 12, progressRect.centerY() + 6, barPaint);


        Paint p = new Paint();
        // smooths
        p.setAntiAlias(true);
        p.setColor(Color.WHITE);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(5);
        // opacity
        //p.setAlpha(0x80); //

        //25%=90, 1%=3.6;
        //width rect =200
        RectF rectF = new RectF(centerOfCanvas.x - 100, centerOfCanvas.y - 100, centerOfCanvas.x + 100, centerOfCanvas.y + 100);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, 100f, p);
        p.setColor(Color.BLACK);
        canvas.drawArc(rectF, 270, (float) (percent * 3.6), false, p);

    }

    private void initAttrs(Context c, AttributeSet attributeSet) {
        TypedArray a = c.getTheme().obtainStyledAttributes(attributeSet, R.styleable.CustomProgreesBar, 0, 0);
        color = a.getColor(R.styleable.CustomProgreesBar_colorProgress, Color.BLACK);
        percent = a.getColor(R.styleable.CustomProgreesBar_percentProgress, 50);
        a.recycle();
    }

    public void start() {
        stop();

        mTimerAnimator = new ValueAnimator();
        mTimerAnimator.setObjectValues(0, percent);
        mTimerAnimator.setDuration(2000);
        mTimerAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Log.i("TAG_CUSTOM", animation.getAnimatedValue() + "<===");
                drawProgress((Integer) animation.getAnimatedValue());
            }
        });
        mTimerAnimator.start();
    }

    private void rotate() {
//        ValueAnimator animator = ValueAnimator.ofFloat(0, 360);
//
//        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                float value = (float) animation.getAnimatedValue();
//                // 2
//                mRocket.setRotation(value);
//            }
//        });
//
//        animator.setInterpolator(new LinearInterpolator());
//        animator.setDuration(DEFAULT_ANIMATION_DURATION);
//        animator.start();
    }


    public void stop() {
        if (mTimerAnimator != null && mTimerAnimator.isRunning()) {
            mTimerAnimator.cancel();
            mTimerAnimator = null;

            drawProgress(0);
        }
    }

    private void drawProgress(int progress) {
        percent = progress;
        invalidate();
    }

    public void setPercent(int percent) {
        this.percent = percent;
        start();
    }
}

package com.example.a1649618.ui.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.example.a1649618.createnote.R;

/**
 * A custom view that draws a circle with a border.
 * @author Ian Clement (ian.clement@johnabbott.qc.ca)
 */
public class CircleView extends View {

    private static final float STROKE_WIDTH = 3.0f;

    // The paint for the circle
    private Paint paint;
    private Paint borderPaint;


    // The position of the circle
    private RectF position;


    public CircleView(Context context) {
        super(context);
        init(-1);

    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // get the custom attribute "circleColor" from the attribute set
        int[] ids = new int[] { R.attr.circleColor };
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, ids, 0, 0);
        int colorResourceId = a.getColor(0, -1); // our values is the 0th element, default to -1 if not set

        // intialize with the set attribute or color WHITE otherwise
        init(colorResourceId != -1 ? colorResourceId : Color.WHITE);
    }

    /**
     * Initialized the view with default colors.
     */
    private void init(int colorResId) {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);

        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setColor(Color.BLACK);
        borderPaint.setStrokeWidth(STROKE_WIDTH);

        setColor(colorResId);
    }

    /**
     * Get the circle's color
     * @return The circle's color
     */
    public int getColor() {
        return paint.getColor();
    }

    /**
     * Set the circle's color using a resource ID.
     * @param resId
     */
    public void setColor(int resId) {
        paint.setColor(resId);

        // force a redraw
        invalidate();
    }

    /**
     * Set the circle's color using RGB
     * @param red The red component (0-255)
     * @param green The green component (0-255)
     * @param blue The blue component (0-255)
     */
    public void setColor(int red, int green, int blue) {
        paint.setColor(Color.rgb(red, green, blue));

        // force a redraw
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawOval(position, paint);
        canvas.drawOval(position, borderPaint);
    }


    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);

        float xPadding = (float) (getPaddingLeft() + getPaddingRight());
        float yPadding = (float) (getPaddingTop() + getPaddingBottom());
        float actualWidth = (float) width - xPadding;
        float actualHeight = (float) height - yPadding;

        float diameter = Math.min(actualWidth, actualHeight);

        // center the circle
        float xCenteringOffset = (actualWidth - diameter) / 2.0f;
        float yCenteringOffset = (actualHeight - diameter) / 2.0f;

        // Fix the position of the circle offset by top/left padding
        // ensure that the stroke width will not result in cutoff
        position = new RectF(STROKE_WIDTH / 2.0f, STROKE_WIDTH / 2.0f, diameter - STROKE_WIDTH, diameter - STROKE_WIDTH);
        position.offset(getPaddingLeft() + xCenteringOffset, getPaddingTop() + yCenteringOffset);
    }

}

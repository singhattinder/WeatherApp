package com.attinder.weatherdemoapp.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;

public class CustomTextView extends AppCompatTextView
{

    public CustomTextView(Context context)
    {
        super(context);
        init(context);
    }

    public CustomTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init(context);
    }

    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
    }

    public void init(Context context)
    {
        try
        {
            Typeface myFont = Typeface.createFromAsset(context.getAssets(), "fonts/SFLight.ttf");

            setTypeface(myFont);
        }
        catch (Exception e)
        {
            Log.d("error",e.toString());
        }
    }
}

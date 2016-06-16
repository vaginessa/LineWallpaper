package de.jeanpierrehotz.drawyaownwallpapers.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import de.jeanpierrehotz.drawyaownwallpapers.R;

/**
 * Created by Admin on 16.06.2016.
 */
public class DividerView extends View{
    private Paint p;

    public DividerView(Context context){
        super(context);
        this.p = new Paint();
        p.setColor(ContextCompat.getColor(getContext(), R.color.dividercolor));
        p.setStyle(Paint.Style.FILL);
        this.postInvalidate();
    }

    public DividerView(Context context, AttributeSet attrs){
        super(context, attrs);
        this.p = new Paint();
        p.setColor(ContextCompat.getColor(getContext(), R.color.dividercolor));
        p.setStyle(Paint.Style.FILL);
        this.postInvalidate();
    }

    public DividerView(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        this.p = new Paint();
        p.setColor(ContextCompat.getColor(getContext(), R.color.dividercolor));
        p.setStyle(Paint.Style.FILL);
        this.postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

        canvas.drawRect(0, 0, getWidth(), getHeight(), p);
    }
}

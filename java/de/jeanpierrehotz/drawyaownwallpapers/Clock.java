package de.jeanpierrehotz.drawyaownwallpapers;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.provider.CalendarContract;

import java.util.Calendar;

/**
 * Created by Admin on 22.03.2016.
 */
public abstract class Clock{
    protected int hou, min, sec, ms;
    protected String eve;

    private void refreshTime(){
        Calendar cal = Calendar.getInstance();

        hou = cal.get(Calendar.HOUR_OF_DAY);
        eve = (hou >= 12)? "PM": "AM";
        hou %= 12;
        min = cal.get(Calendar.MINUTE);
        sec = cal.get(Calendar.SECOND);
        ms = cal.get(Calendar.MILLISECOND);
    }

    /**
     * Point P(x|y) is the upper left corner of the rectangle in which the clock might be lying
     * @param x
     * @param y
     * @param diam
     * @param c
     * @param p
     */
    protected void draw(float x, float y, float diam, Canvas c, Paint p){
        refreshTime();
    }
}
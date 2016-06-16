package de.jeanpierrehotz.drawyaownwallpapers.wallpaper.data.clock;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.Calendar;

/**
 * Created by Admin on 16.06.2016.
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
     * This method is used to draw the clock.<br>
     * Since it should first update its time you should definitely call the super method.
     * @param x     The x-position of the upper left corner of the rectangle in which the clock lies
     * @param y     The y-position of the upper left corner of the rectangle in which the clock lies
     * @param diam  The diameter of the clock
     * @param c     The canvas onto which the clock has to be drawn
     * @param p     the paint we need to draw something
     */
    public void draw(float x, float y, float diam, Canvas c, Paint p){
        refreshTime();
    }
}

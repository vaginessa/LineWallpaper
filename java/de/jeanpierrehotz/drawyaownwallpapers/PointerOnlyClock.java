package de.jeanpierrehotz.drawyaownwallpapers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Admin on 26.03.2016.
 *
 */
public class PointerOnlyClock extends Clock{
    private int pointer_hoursColor;
    private int pointer_minutesColor;
    private int pointer_seondsColor;

    public PointerOnlyClock(int hCol, int mCol, int sCol){
        super();
        pointer_hoursColor      = hCol;
        pointer_minutesColor    = mCol;
        pointer_seondsColor     = sCol;
    }

    @Override
    public void draw(float x, float y, float diam, Canvas c, Paint p){
        super.draw(x, y, diam, c, p);

        int     tempColor           = p.getColor();
        float   tempStrokeWidth     = p.getStrokeWidth();
        float   rad                 = diam / 2;

        p.setTextAlign(Paint.Align.CENTER);
        p.setStrokeWidth(4f);
        p.setColor(Color.WHITE);
        p.setStyle(Paint.Style.FILL);
        c.drawText(eve, x + (3 * rad / 2f), y + rad, p);
        p.setStyle(Paint.Style.STROKE);
        p.setColor(Color.BLACK);
        c.drawText(eve, x + (3 * rad / 2f), y + rad, p);

        p.setStrokeWidth(18f);

        float   sDeg                = sec * 6;
        float   mDeg                = min * 6 + sec / 10f;
        float   hDeg                = hou * 30 + min / 2f + sec / 120f;

        p.setColor(pointer_seondsColor);
        c.drawLine(
                (float) (x + rad + (Math.sin(Math.toRadians(sDeg)) * (rad - 80))),
                (float) (y + rad - (Math.cos(Math.toRadians(sDeg)) * (rad - 80))),
                (float) (x + rad - (Math.sin(Math.toRadians(sDeg)) * 80)),
                (float) (y + rad + (Math.cos(Math.toRadians(sDeg)) * 80)),
                p
        );

        p.setColor(pointer_minutesColor);
        c.drawLine(
                (float) (x + rad + (Math.sin(Math.toRadians(mDeg)) * (rad - 120))),
                (float) (y + rad - (Math.cos(Math.toRadians(mDeg)) * (rad - 120))),
                (float) (x + rad - (Math.sin(Math.toRadians(mDeg)) * 60)),
                (float) (y + rad + (Math.cos(Math.toRadians(mDeg)) * 60)),
                p
        );

        p.setColor(pointer_hoursColor);
        c.drawLine(
                (float) (x + rad + (Math.sin(Math.toRadians(hDeg)) * (rad - 160))),
                (float) (y + rad - (Math.cos(Math.toRadians(hDeg)) * (rad - 160))),
                (float) (x + rad - (Math.sin(Math.toRadians(hDeg)) * 40)),
                (float) (y + rad + (Math.cos(Math.toRadians(hDeg)) * 40)),
                p
        );

        p.setStyle(Paint.Style.FILL);
        p.setColor(Color.rgb(0xF4, 0x51, 0x1E));
        c.drawCircle(x + rad, y + rad, 20, p);

        p.setStrokeWidth(tempStrokeWidth);
        p.setColor(tempColor);
    }
}

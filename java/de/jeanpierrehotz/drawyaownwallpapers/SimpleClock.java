package de.jeanpierrehotz.drawyaownwallpapers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Admin on 22.03.2016.
 *
 */
public class SimpleClock extends Clock{
    private int background_color;
    private int pointer_hoursColor;
    private int pointer_minutesColor;
    private int pointer_seondsColor;

    public SimpleClock(int bgCol, int hourCol, int minCol, int secCol){
        super();
        background_color = bgCol;
        pointer_hoursColor = hourCol;
        pointer_minutesColor = minCol;
        pointer_seondsColor = secCol;
    }

    @Override
    protected void draw(float x, float y, float diam, Canvas c, Paint p){
        super.draw(x, y, diam, c, p);

        int     tempColor           = p.getColor();
        float   tempStrokeWidth     = p.getStrokeWidth();

        p.setStrokeWidth(24f);
        float rad = diam / 2;

        p.setColor(background_color);
        p.setStyle(Paint.Style.FILL);
        c.drawCircle(x + rad, y + rad, rad, p);

        p.setStyle(Paint.Style.STROKE);
        p.setColor(Color.BLACK);

        c.drawCircle(x + rad, y + rad, rad, p);

        for(int i = 0; i < 60; i++){
            if(i % 15 == 0){
                p.setStrokeWidth(24f);
                c.drawLine(
                        (float) (x + rad + (Math.sin(Math.toRadians(i * 6)) * (rad - 80))),
                        (float) (y + rad - (Math.cos(Math.toRadians(i * 6)) * (rad - 80))),
                        (float) (x + rad + (Math.sin(Math.toRadians(i * 6)) * (rad - 2))),
                        (float) (y + rad - (Math.cos(Math.toRadians(i * 6)) * (rad - 2))),
                        p
                );
            }else if(i % 5 == 0){
                p.setStrokeWidth(12f);
                c.drawLine(
                        (float) (x + rad + (Math.sin(Math.toRadians(i * 6)) * (rad - 60))),
                        (float) (y + rad - (Math.cos(Math.toRadians(i * 6)) * (rad - 60))),
                        (float) (x + rad + (Math.sin(Math.toRadians(i * 6)) * (rad - 2))),
                        (float) (y + rad - (Math.cos(Math.toRadians(i * 6)) * (rad - 2))),
                        p
                );
            }else{
                p.setStrokeWidth(6f);
                c.drawLine(
                        (float) (x + rad + (Math.sin(Math.toRadians(i * 6)) * (rad - 40))),
                        (float) (y + rad - (Math.cos(Math.toRadians(i * 6)) * (rad - 40))),
                        (float) (x + rad + (Math.sin(Math.toRadians(i * 6)) * (rad - 2))),
                        (float) (y + rad - (Math.cos(Math.toRadians(i * 6)) * (rad - 2))),
                        p
                );
            }
        }

        p.setTextAlign(Paint.Align.CENTER);
        p.setStrokeWidth(4f);
        p.setColor(Color.BLACK);
        c.drawText(eve, x + (3 * rad / 2f), y + rad, p);

        p.setStrokeWidth(18f);

        float sDeg = sec * 6;
        float mDeg = min * 6 + sec / 10f;
        float hDeg = hou * 30 + min / 2f + sec / 120f;

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

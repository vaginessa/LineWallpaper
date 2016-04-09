package de.jeanpierrehotz.drawyaownwallpapers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 *
 * Created by Admin on 26.03.2016.
 */
public class ParabolaClock extends Clock{
    private int sec_min_Color;
    private int min_hou_Color;
    private int backgroundColor;

    public ParabolaClock(int bgCol, int minHouCol, int secMinCol){
        backgroundColor = bgCol;
        min_hou_Color = minHouCol;
        sec_min_Color = secMinCol;
    }

    @Override
    protected void draw(float x, float y, float diam, Canvas c, Paint p){
        super.draw(x, y, diam, c, p);

        int     tempColor           = p.getColor();
        float   tempStrokeWidth     = p.getStrokeWidth();

        p.setStrokeWidth(24f);
        float rad = diam / 2;
        float rad_in = rad - 80;
        float rad_h = rad_in - 40;

        p.setColor(backgroundColor);
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

        /**
         * Draw actual clock here:
         */
        double ang_h = (hou * 30) + (min * 0.5) + (sec * 0.00833333333333333) + (ms * 0.0000083333333333);
        double ang_m = (min * 6) + (sec * 0.1f) + (ms * 0.0001);
        double ang_s = (sec * 6) + (ms * 0.006);

        Point ps = new Point(x + rad + (Math.sin(Math.toRadians(ang_s)) * rad_in), y + rad - (Math.cos(Math.toRadians(ang_s)) * rad_in));
        Point pm = new Point(x + rad + (Math.sin(Math.toRadians(ang_m)) * rad_in), y + rad - (Math.cos(Math.toRadians(ang_m)) * rad_in));
        Point ph = new Point(x + rad + (Math.sin(Math.toRadians(ang_h)) * rad_h), y + rad - (Math.cos(Math.toRadians(ang_h)) * rad_h));
        Point ci = new Point(x + rad + (Math.sin(Math.toRadians(ang_s)) * ((rad + rad_in) / 2f)), y + rad - (Math.cos(Math.toRadians(ang_s)) * ((rad + rad_in) / 2f)));
        Point mid = new Point(x + rad, y + rad);

        p.setStrokeWidth(10f);
        p.setColor(min_hou_Color);
        drawBezier(new Point[]{pm, mid, ph}, c, p);

        p.setStrokeWidth(p.getStrokeWidth() / 2f);
        p.setColor(sec_min_Color);
        c.drawCircle((float) ci.x, (float) ci.y, (rad - rad_in) / 2f, p);
        drawBezier(new Point[]{ps, mid, pm}, c, p);

        p.setColor(tempColor);
        p.setStrokeWidth(tempStrokeWidth);
    }

    private void drawBezier(Point[] p, Canvas g, Paint paint){
        int nMax = 500;
        double t = 1.0d / (double)nMax; // Schrittweite, 0.0 < (t * n) < 1.0
        Point lastPoint = null;
        for (int n = 1; n < nMax; ++n){
            Point bezPoint = berechneBezPoint(n, t, p);
            // Zeichne Kurve, solange beide Punkte nicht gleich sind
            if (n > 1 && !lastPoint.equals(bezPoint)){
                g.drawLine((float) lastPoint.x, (float) lastPoint.y, (float) bezPoint.x, (float) bezPoint.y, paint);
            }
            lastPoint = bezPoint;
        }
    }

    private Point berechneBezPoint(int n, double t, Point[] p){
        int len = p.length;
        if (len == 1)
            return p[0]; // Unser Ergebnis (für t * n)
        else {
            Point[] tmpPointList = new Point[len - 1];

            // einmal alle anfassen (iterativ)...
            for (int i = 1; i < len; ++i){
                Point tmpPoint = new Point(p[i - 1].x + (-(p[i - 1].x - p[i].x) * t * n), p[i - 1].y + (-(p[i - 1].y - p[i].y) * t * n));
                // ... und eintüten
                tmpPointList[i - 1] = (tmpPoint);
            }
            // hier die Rekursion
            return berechneBezPoint(n, t, tmpPointList);
        }
    }

    private static class Point {
        public double x, y;

        public Point(double x, double y){
            this.x = x;
            this.y = y;
        }

        public boolean equals(Point toCheck){
            return this.x == toCheck.x && this.y == toCheck.y;
        }
    }
}

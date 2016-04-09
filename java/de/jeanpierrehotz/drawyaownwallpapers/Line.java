package de.jeanpierrehotz.drawyaownwallpapers;

import android.content.Context;
import android.content.SharedPreferences;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import android.os.Handler;

import java.util.ArrayList;

/**
 * Created by Admin on 20.03.2016.
 *
 */
public class Line{

    private ArrayList<Float> xPoints, yPoints;

    private int col;

    private boolean doomed;

    public boolean isDoomed(){
        return doomed;
    }

    private boolean killed;

    private boolean rainbowColor;

    private boolean isRainbowColor(){
        return rainbowColor;
    }

    public Line(float x, float y, boolean rainb, int rainbsteps){
        xPoints = new ArrayList<>();
        yPoints = new ArrayList<>();

        addPoint(x, y);

        doomed = false;
        killed = false;

        col = Color.rgb(
                (int) (Math.random() * 256),
                (int) (Math.random() * 256),
                (int) (Math.random() * 256)
        );

        rainbowColor = rainb;
        rainbowsteps = rainbsteps;
    }

    public Line(float x, float y, int color, boolean rainb, int rainbsteps){
        xPoints = new ArrayList<>();
        yPoints = new ArrayList<>();

        addPoint(x, y);

        doomed = false;
        killed = false;

        col = color;

        rainbowColor = rainb;
        rainbowsteps = rainbsteps;
    }

    public void addPoint(float x, float y){
        xPoints.add(x);
        yPoints.add(y);
    }

    public int getCount(){
        return xPoints.size();
    }

    public void addTimer(Handler h, final ArrayList<Line> toRemoveFrom, long time){
        doomed = true;
        final Line toRemove = this;
        h.postDelayed(new Runnable(){
            @Override
            public void run(){
                killed = true;
                toRemoveFrom.remove(toRemove);
            }
        }, time);
    }

    public void injectParasite(final int time){
        new Thread(new Runnable(){
            @Override
            public void run(){
                while(!killed && xPoints.size() != 0){
                    try{
                        Thread.sleep(time);
                    }catch(Exception exc){
                        return;
                    }

                    xPoints.remove(xPoints.size() - 1);
                    yPoints.remove(yPoints.size() - 1);
                }
            }
        }).start();
    }

    public void fade(final int time){
        new Thread(new Runnable(){
            @Override
            public void run(){
                while(!killed && Color.alpha(col) != 0){
                    try{
                        Thread.sleep(time);
                    }catch(Exception exc){
                        return;
                    }

                    col = Color.argb(
                            Color.alpha(col) - 1,
                            Color.red(col),
                            Color.green(col),
                            Color.blue(col)
                    );
                }
            }
        }).start();
    }

    private int rainbowsteps;

    private int getRainbowsteps(){
        return rainbowsteps;
    }

    public void draw(Canvas c, Paint p, boolean drawBalls, float ballsRadius){
//        this.draw(c, p, drawBalls, col);
        if(xPoints.size() != 0){
            int temp = p.getColor();

            p.setColor(col);

            if(drawBalls){
                c.drawCircle(
                        xPoints.get(0),
                        yPoints.get(0),
                        ballsRadius, p
                );
            }

            for(int i = 0; i < xPoints.size() - 1; i++){
                c.drawLine(
                        xPoints.get(i),
                        yPoints.get(i),
                        xPoints.get(i + 1),
                        yPoints.get(i + 1), p
                );

                if(rainbowColor){
                    p.setColor(RainbowColor.getIn(rainbowsteps, p.getColor()));
                }
            }

            if(drawBalls){
                c.drawCircle(
                        xPoints.get(xPoints.size() - 1),
                        yPoints.get(yPoints.size() - 1),
                        ballsRadius, p
                );
            }


/*
            if(drawBalls){
                c.drawCircle(
                        xPoints.get(0),
                        yPoints.get(0),
                        ballsRadius, p
                );
                c.drawCircle(
                        xPoints.get(xPoints.size() - 1),
                        yPoints.get(yPoints.size() - 1),
                        ballsRadius, p
                );
            }
*/

            p.setColor(temp);
        }
    }

    public static ArrayList<Line> loadFromSharedPreferences(SharedPreferences load, Context c){
        ArrayList<Line> loaded = new ArrayList<>();
        int length = load.getInt(c.getString(R.string.permanentlines_length), 0);

        for(int i = 0; i < length; i++){
            int pointsInLine = load.getInt(c.getString(R.string.permanentlines_dotsinlinenr) + i, 0);

            loaded.add(new Line(
                    load.getFloat(c.getString(R.string.permanentlines_xvalueinnr) + i + "" + 0, 0f),
                    load.getFloat(c.getString(R.string.permanentlines_yvalueinnr) + i + "" + 0, 0f),
                    load.getInt(c.getString(R.string.permanentlines_colorinnr) + i, Color.BLACK),
                    load.getBoolean(c.getString(R.string.permanentLines_rainbowColor) + i, false),
                    load.getInt(c.getString(R.string.permanentLines_rainbowColorSteps) + i, 10)
            ));

            for(int dot = 1; dot < pointsInLine; dot++){
                loaded.get(loaded.size() - 1).addPoint(
                        load.getFloat(c.getString(R.string.permanentlines_xvalueinnr) + i + "" + dot, 0f),
                        load.getFloat(c.getString(R.string.permanentlines_yvalueinnr) + i + "" + dot, 0f)
                );
            }
        }

        return loaded;
    }

    public static void saveToSharedPreferences(ArrayList<Line> lines, SharedPreferences save, Context c){
        SharedPreferences.Editor edit = save.edit();
        edit.clear();

        edit.putInt(c.getString(R.string.permanentlines_length), lines.size());

        for(int i = 0; i < lines.size(); i++){
            edit.putInt(c.getString(R.string.permanentlines_dotsinlinenr) + i, lines.get(i).getCount());
            edit.putInt(c.getString(R.string.permanentlines_colorinnr) + i, lines.get(i).getColor());
            edit.putBoolean(c.getString(R.string.permanentLines_rainbowColor) + i, lines.get(i).isRainbowColor());
            edit.putInt(c.getString(R.string.permanentLines_rainbowColorSteps) + i, lines.get(i).getRainbowsteps());

            for(int dot = 0; dot < lines.get(i).getCount(); dot++){
                edit.putFloat(c.getString(R.string.permanentlines_xvalueinnr) + i + "" + dot, lines.get(i).getXIn(dot));
                edit.putFloat(c.getString(R.string.permanentlines_yvalueinnr) + i + "" + dot, lines.get(i).getYIn(dot));
            }
        }

        edit.apply();
    }

    private int getColor(){
        return col;
    }

    private float getXIn(int ind){
        return xPoints.get(ind);
    }

    private float getYIn(int ind){
        return yPoints.get(ind);
    }

    public static class RainbowColor{
        public static int getNext(int col){
            int a = Color.alpha(col);
            int r = Color.red(col);
            int g = Color.green(col);
            int b = Color.blue(col);

            if(r == 255 && g != 255 && b == 0){
                g++;
            }else if(r != 0 && g == 255 && b == 0){
                r--;
            }else if(r == 0 && g == 255 && b != 255){
                b++;
            }else if(r == 0 && g != 0 && b == 255){
                g--;
            }else if(r != 255 && g == 0 && b == 255){
                r++;
            }else if(r == 255 && g == 0/* && b != 0*/){
                b--;
            }else{
                if(r != 255){
                    r++;
                }else /*if(g != 0)*/{
                    g--;
                }/*else{
                    b--;
                }*/
            }

            return Color.argb(a, r, g, b);
        }

        public static int getIn(int n, int col){
            for(int i = 0; i < n; i++){
                col = getNext(col);
            }
            return col;
        }
    }
}

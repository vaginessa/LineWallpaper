package de.jeanpierrehotz.drawyaownwallpapers.wallpaper.data.clock;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Admin on 16.06.2016.
 */
public class DigitalClock extends Clock{
    private boolean dots_blinking;
    private static final int BLINKINGRATE = 500;

    long lastToggle;
    boolean drawBalls;

    public DigitalClock(boolean blinking){
        dots_blinking = blinking;
        lastToggle = System.currentTimeMillis();
        drawBalls = true;
    }

    @Override
    public void draw(float x, float y, float diam, Canvas c, Paint p){
        super.draw(x, y, diam, c, p);

        float topMarg = diam / 4f;
        float base = diam / 208f;
        float rad = diam / 2f;

        hou += (eve.equals("PM"))? 12: 0;
//        if(eve.equals("PM")){
//            hou += 12;
//        }

        for(int i = 0; i < 2; i++){
            if(i == 0){
                p.setStrokeWidth(28f);
                p.setColor(Color.BLACK);
            }else{
                p.setStrokeWidth(18f);
                p.setColor(Color.WHITE);
            }

            drawSegments(translateTo7Segment(hou / 10), x, y + topMarg, base, c, p);
            drawSegments(translateTo7Segment(hou % 10), x + 50 * base, y + topMarg, base, c, p);

            if(drawBalls){
                if(i == 0){
                    c.drawCircle(x + rad, y + rad - 14 * base, 16, p);
                    c.drawCircle(x + rad, y + rad + 14 * base, 16, p);
                }else{
                    c.drawCircle(x + rad, y + rad - 14 * base, 8, p);
                    c.drawCircle(x + rad, y + rad + 14 * base, 8, p);
                }
            }

            drawSegments(translateTo7Segment(min / 10), x + 118 * base, y + topMarg, base, c, p);
            drawSegments(translateTo7Segment(min % 10), x + 168 * base, y + topMarg, base, c, p);

//            for(int bla = 0; bla < 10; bla++){
//                drawSegments(translateTo7Segment(bla), 50 + bla * 80, 500, 1, c, p);
//            }
        }

        if(dots_blinking && System.currentTimeMillis() - lastToggle >= BLINKINGRATE){
            drawBalls = !drawBalls;
            lastToggle = System.currentTimeMillis();
        }
//        if(segm[0]) c.drawLine();
    }

    private void drawSegments(boolean[] segm, float x, float y, float base, Canvas c, Paint p){
        if(segm[0]) c.drawLine(x + 40 * base, y, x + 40 * base, y + 40 * base, p);
        if(segm[1]) c.drawLine(x + 40 * base, y + 40 * base, x + 40 * base, y + 80 * base, p);
        if(segm[2]) c.drawLine(x + 40 * base, y + 80 * base, x, y + 80 * base, p);
        if(segm[3]) c.drawLine(x, y + 80 * base, x, y + 40 * base, p);
        if(segm[4]) c.drawLine(x, y + 40 * base, x, y, p);
        if(segm[5]) c.drawLine(x, y, x + 40 * base, y, p);
        if(segm[6]) c.drawLine(x, y + 40 * base, x + 40 * base, y + 40 * base, p);
    }

    private boolean[] translateTo7Segment(int digit){
        if(digit % 10 != digit)
            return null;

        boolean[] sevenSegment = new boolean[7];

        sevenSegment[0] = digit != 5 && digit != 6;
        sevenSegment[1] = digit != 2;
        sevenSegment[2] = digit != 1 && digit != 4 && digit != 7;
        sevenSegment[3] = digit == 0 || digit == 2 || digit == 6 || digit == 8;
        sevenSegment[4] = digit == 0 || digit == 4 || digit == 5 || digit == 6 || digit == 8 || digit == 9;
        sevenSegment[5] = digit != 1 && digit != 4;
        sevenSegment[6] = digit != 1 && digit != 7 && digit != 0;

        return sevenSegment;
    }
}

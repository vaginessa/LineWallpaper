package de.jeanpierrehotz.drawyaownwallpapers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.media.audiofx.Visualizer;

/**
 * Created by Admin on 18.05.2016.
 */
public class ProximityAudioVisualizer extends AudioVisualizer{

    private static final Vector2D[] POINTS = new Vector2D[]{
            new Vector2D(482d / 900d, 0d / 900d),    // P_1 | 0
            new Vector2D(122d / 900d, 115d / 900d),  // P_2 | 1
            new Vector2D(436d / 900d, 473d / 900d),  // P_3 | 2
            new Vector2D(358d / 900d, 498d / 900d),  // P_4 | 3
            new Vector2D(738d / 900d, 835d / 900d),  // P_5 | 4
            new Vector2D(592d / 900d, 563d / 900d),  // P_6 | 5
            new Vector2D(645d / 900d, 545d / 900d),  // P_7 | 6
            new Vector2D(526d / 900d, 303d / 900d),  // P_8 | 7
            new Vector2D(614d / 900d, 274d / 900d),  // P_9 | 8
            new Vector2D(418d / 900d, 129d / 900d), // P_10 | 9
            new Vector2D(467d / 900d, 217d / 900d), // P_11 | 10
            new Vector2D(380d / 900d, 245d / 900d), // P_12 | 11
            new Vector2D(317d / 900d, 162d / 900d), // P_13 | 12
            new Vector2D(-16d / 900d, 33d / 900d),   // L_1 | 13
            new Vector2D(-32d / 900d, 66d / 900d)    // L_2 | 14
    };

    private static final int YELLOW_LAYER = Color.rgb(232, 237, 32);
    private static final int RED_LAYER = Color.rgb(213, 33, 153);
    private static final int BLUE_LAYER = Color.rgb(124, 206, 231);

    private float visualizer_outerradius;
    private float visualizer_innerradius;

    private float proximity_offset_x;
    private float proximity_offset_y;
    private float proximity_diameter;

    public ProximityAudioVisualizer(Visualizer vis, VisualizationType vistype, int x, int y, int diameter){
        super(vis, vistype, x, y, diameter);
    }

    @Override
    protected void init(){
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setStrokeWidth(12f);

        visualizer_outerradius = diameter / 2f;
        visualizer_innerradius = visualizer_outerradius * (3f / 4f);

        proximity_offset_x = (diameter / 2f) - (float) (Math.sin(Math.toRadians(45)) * visualizer_innerradius);
        proximity_offset_y = (diameter / 2f) - (float) (Math.sin(Math.toRadians(45)) * visualizer_innerradius);
        proximity_diameter = (float) (Math.sin(Math.toRadians(45)) * visualizer_innerradius * 2);
    }

    @Override
    protected void drawWaveFormData(byte[] vals, Canvas c){
        // DEBUGGING-PURPOSE
//        c.drawCircle(x + diameter / 2f, y + diameter / 2f, visualizer_outerradius, mPaint);
//        c.drawCircle(x + diameter / 2f, y + diameter / 2f, visualizer_innerradius, mPaint);

        drawProximity(c);

        float degrPerFreq = 360f / (float) vals.length;
        float rad_inbetw = (visualizer_innerradius + visualizer_outerradius) / 2f;
        float dist = (visualizer_outerradius - rad_inbetw) / 128f;

        Path p = new Path();
        p.moveTo(
                x + (diameter / 2f) - (float) (Math.sin(Math.toRadians(0 * degrPerFreq)) * (rad_inbetw - dist * Math.abs(vals[0]))),
                y + (diameter / 2f) - (float) (Math.cos(Math.toRadians(0 * degrPerFreq)) * (rad_inbetw - dist * Math.abs(vals[0])))
        );

        for(int i = 1; i < vals.length; i++){
            p.lineTo(
                    x + (diameter / 2f) - (float) (Math.sin(Math.toRadians(i * degrPerFreq)) * (rad_inbetw - dist * Math.abs(vals[i]))),
                    y + (diameter / 2f) - (float) (Math.cos(Math.toRadians(i * degrPerFreq)) * (rad_inbetw - dist * Math.abs(vals[i])))
            );
        }
        p.close();
        c.drawPath(p, mPaint);
    }

    @Override
    protected void drawFFTData(byte[] vals, Canvas c){
        // DEBUGGING-PURPOSE
//        c.drawCircle(x + diameter / 2f, y + diameter / 2f, visualizer_outerradius, mPaint);
//        c.drawCircle(x + diameter / 2f, y + diameter / 2f, visualizer_innerradius, mPaint);

        drawProximity(c);

        float degrPerFreq = 360f / ((vals.length - 2) / 2f);
        float dist = (visualizer_outerradius - visualizer_innerradius) / 128f;

        Path p = new Path();
        p.moveTo(
                x + (diameter / 2f) - (float) (Math.sin(Math.toRadians(0 * degrPerFreq)) * (visualizer_innerradius + dist * getAbsolute(vals[1], vals[2]))),
                y + (diameter / 2f) - (float) (Math.cos(Math.toRadians(0 * degrPerFreq)) * (visualizer_innerradius + dist * getAbsolute(vals[1], vals[2])))
        );

        for(int i = 1; i < vals.length - 1; i += 2){
            p.lineTo(
                    x + (diameter / 2f) - (float) (Math.sin(Math.toRadians(((i / 2f) - 1) * degrPerFreq)) * (visualizer_innerradius + dist * getAbsolute(vals[i], vals[i + 1]))),
                    y + (diameter / 2f) - (float) (Math.cos(Math.toRadians(((i / 2f) - 1) * degrPerFreq)) * (visualizer_innerradius + dist * getAbsolute(vals[i], vals[i + 1])))
            );
        }
        p.close();
        c.drawPath(p, mPaint);
    }

    private void drawProximity(Canvas c){
        // caching the paint status
        int prevCol = mPaint.getColor();
        Paint.Style prevStyle = mPaint.getStyle();

        // creating needed settings and objects
        mPaint.setStyle(Paint.Style.FILL);

        Path p = new Path();
        p.setFillType(Path.FillType.WINDING);

        //drawing the blue shapes
        mPaint.setColor(BLUE_LAYER);
        p.moveTo(
                (POINTS[1].getFX() + POINTS[14].getFX()) * proximity_diameter + proximity_offset_x + x,
                (POINTS[1].getFY() + POINTS[14].getFY()) * proximity_diameter + proximity_offset_y + y
        );
        p.lineTo(
                (POINTS[2].getFX() + POINTS[14].getFX()) * proximity_diameter + proximity_offset_x + x,
                (POINTS[2].getFY() + POINTS[14].getFY()) * proximity_diameter + proximity_offset_y + y
        );
        p.lineTo(
                (POINTS[2].getFX() + POINTS[13].getFX()) * proximity_diameter + proximity_offset_x + x,
                (POINTS[2].getFY() + POINTS[13].getFY()) * proximity_diameter + proximity_offset_y + y
        );
        p.lineTo(
                (POINTS[1].getFX() + POINTS[13].getFX()) * proximity_diameter + proximity_offset_x + x,
                (POINTS[1].getFY() + POINTS[13].getFY()) * proximity_diameter + proximity_offset_y + y
        );
        p.close();
        c.drawPath(p, mPaint);

        p.reset();
        p.moveTo(
                (POINTS[3].getFX() + POINTS[14].getFX()) * proximity_diameter + proximity_offset_x + x,
                (POINTS[3].getFY() + POINTS[14].getFY()) * proximity_diameter + proximity_offset_y + y
        );
        p.lineTo(
                (POINTS[4].getFX() + POINTS[14].getFX()) * proximity_diameter + proximity_offset_x + x,
                (POINTS[4].getFY() + POINTS[14].getFY()) * proximity_diameter + proximity_offset_y + y
        );
        p.lineTo(
                (POINTS[4].getFX() + POINTS[13].getFX()) * proximity_diameter + proximity_offset_x + x,
                (POINTS[4].getFY() + POINTS[13].getFY()) * proximity_diameter + proximity_offset_y + y
        );
        p.lineTo(
                (POINTS[3].getFX() + POINTS[13].getFX()) * proximity_diameter + proximity_offset_x + x,
                (POINTS[3].getFY() + POINTS[13].getFY()) * proximity_diameter + proximity_offset_y + y
        );
        p.close();
        c.drawPath(p, mPaint);

        p.reset();
        p.moveTo(
                (POINTS[5].getFX() + POINTS[14].getFX()) * proximity_diameter + proximity_offset_x + x,
                (POINTS[5].getFY() + POINTS[14].getFY()) * proximity_diameter + proximity_offset_y + y
        );
        p.lineTo(
                (POINTS[6].getFX() + POINTS[14].getFX()) * proximity_diameter + proximity_offset_x + x,
                (POINTS[6].getFY() + POINTS[14].getFY()) * proximity_diameter + proximity_offset_y + y
        );
        p.lineTo(
                (POINTS[6].getFX() + POINTS[13].getFX()) * proximity_diameter + proximity_offset_x + x,
                (POINTS[6].getFY() + POINTS[13].getFY()) * proximity_diameter + proximity_offset_y + y
        );
        p.lineTo(
                (POINTS[5].getFX() + POINTS[13].getFX()) * proximity_diameter + proximity_offset_x + x,
                (POINTS[5].getFY() + POINTS[13].getFY()) * proximity_diameter + proximity_offset_y + y
        );
        p.close();
        c.drawPath(p, mPaint);

        p.reset();
        p.moveTo(
                (POINTS[7].getFX() + POINTS[14].getFX()) * proximity_diameter + proximity_offset_x + x,
                (POINTS[7].getFY() + POINTS[14].getFY()) * proximity_diameter + proximity_offset_y + y
        );
        p.lineTo(
                (POINTS[8].getFX() + POINTS[14].getFX()) * proximity_diameter + proximity_offset_x + x,
                (POINTS[8].getFY() + POINTS[14].getFY()) * proximity_diameter + proximity_offset_y + y
        );
        p.lineTo(
                (POINTS[8].getFX() + POINTS[13].getFX()) * proximity_diameter + proximity_offset_x + x,
                (POINTS[8].getFY() + POINTS[13].getFY()) * proximity_diameter + proximity_offset_y + y
        );
        p.lineTo(
                (POINTS[7].getFX() + POINTS[13].getFX()) * proximity_diameter + proximity_offset_x + x,
                (POINTS[7].getFY() + POINTS[13].getFY()) * proximity_diameter + proximity_offset_y + y
        );
        p.close();
        c.drawPath(p, mPaint);

        p.reset();
        p.moveTo(
                (POINTS[12].getFX() + POINTS[14].getFX()) * proximity_diameter + proximity_offset_x + x,
                (POINTS[12].getFY() + POINTS[14].getFY()) * proximity_diameter + proximity_offset_y + y
        );
        p.lineTo(
                (POINTS[9].getFX() + POINTS[14].getFX()) * proximity_diameter + proximity_offset_x + x,
                (POINTS[9].getFY() + POINTS[14].getFY()) * proximity_diameter + proximity_offset_y + y
        );
        p.lineTo(
                (POINTS[10].getFX() + POINTS[14].getFX()) * proximity_diameter + proximity_offset_x + x,
                (POINTS[10].getFY() + POINTS[14].getFY()) * proximity_diameter + proximity_offset_y + y
        );
        p.lineTo(
                (POINTS[10].getFX() + POINTS[13].getFX()) * proximity_diameter + proximity_offset_x + x,
                (POINTS[10].getFY() + POINTS[13].getFY()) * proximity_diameter + proximity_offset_y + y
        );
        p.lineTo(
                (POINTS[9].getFX() + POINTS[13].getFX()) * proximity_diameter + proximity_offset_x + x,
                (POINTS[9].getFY() + POINTS[13].getFY()) * proximity_diameter + proximity_offset_y + y
        );
        p.lineTo(
                (POINTS[12].getFX() + POINTS[13].getFX()) * proximity_diameter + proximity_offset_x + x,
                (POINTS[12].getFY() + POINTS[13].getFY()) * proximity_diameter + proximity_offset_y + y
        );
        p.close();
        c.drawPath(p, mPaint);


        //drawing the red shapes
        mPaint.setColor(RED_LAYER);
        p.reset();
        p.moveTo(
                (POINTS[1].getFX() + POINTS[13].getFX()) * proximity_diameter + proximity_offset_x + x,
                (POINTS[1].getFY() + POINTS[13].getFY()) * proximity_diameter + proximity_offset_y + y
        );
        p.lineTo(
                (POINTS[2].getFX() + POINTS[13].getFX()) * proximity_diameter + proximity_offset_x + x,
                (POINTS[2].getFY() + POINTS[13].getFY()) * proximity_diameter + proximity_offset_y + y
        );
        p.lineTo(
                POINTS[2].getFX() * proximity_diameter + proximity_offset_x + x,
                POINTS[2].getFY() * proximity_diameter + proximity_offset_y + y
        );
        p.lineTo(
                POINTS[1].getFX() * proximity_diameter + proximity_offset_x + x,
                POINTS[1].getFY() * proximity_diameter + proximity_offset_y + y
        );
        p.close();
        c.drawPath(p, mPaint);

        p.reset();
        p.moveTo(
                (POINTS[3].getFX() + POINTS[13].getFX()) * proximity_diameter + proximity_offset_x + x,
                (POINTS[3].getFY() + POINTS[13].getFY()) * proximity_diameter + proximity_offset_y + y
        );
        p.lineTo(
                (POINTS[4].getFX() + POINTS[13].getFX()) * proximity_diameter + proximity_offset_x + x,
                (POINTS[4].getFY() + POINTS[13].getFY()) * proximity_diameter + proximity_offset_y + y
        );
        p.lineTo(
                POINTS[4].getFX() * proximity_diameter + proximity_offset_x + x,
                POINTS[4].getFY() * proximity_diameter + proximity_offset_y + y
        );
        p.lineTo(
                POINTS[3].getFX() * proximity_diameter + proximity_offset_x + x,
                POINTS[3].getFY() * proximity_diameter + proximity_offset_y + y
        );
        p.close();
        c.drawPath(p, mPaint);

        p.reset();
        p.moveTo(
                (POINTS[5].getFX() + POINTS[13].getFX()) * proximity_diameter + proximity_offset_x + x,
                (POINTS[5].getFY() + POINTS[13].getFY()) * proximity_diameter + proximity_offset_y + y
        );
        p.lineTo(
                (POINTS[6].getFX() + POINTS[13].getFX()) * proximity_diameter + proximity_offset_x + x,
                (POINTS[6].getFY() + POINTS[13].getFY()) * proximity_diameter + proximity_offset_y + y
        );
        p.lineTo(
                POINTS[6].getFX() * proximity_diameter + proximity_offset_x + x,
                POINTS[6].getFY() * proximity_diameter + proximity_offset_y + y
        );
        p.lineTo(
                POINTS[5].getFX() * proximity_diameter + proximity_offset_x + x,
                POINTS[5].getFY() * proximity_diameter + proximity_offset_y + y
        );
        p.close();
        c.drawPath(p, mPaint);

        p.reset();
        p.moveTo(
                (POINTS[7].getFX() + POINTS[13].getFX()) * proximity_diameter + proximity_offset_x + x,
                (POINTS[7].getFY() + POINTS[13].getFY()) * proximity_diameter + proximity_offset_y + y
        );
        p.lineTo(
                (POINTS[8].getFX() + POINTS[13].getFX()) * proximity_diameter + proximity_offset_x + x,
                (POINTS[8].getFY() + POINTS[13].getFY()) * proximity_diameter + proximity_offset_y + y
        );
        p.lineTo(
                POINTS[8].getFX() * proximity_diameter + proximity_offset_x + x,
                POINTS[8].getFY() * proximity_diameter + proximity_offset_y + y
        );
        p.lineTo(
                POINTS[7].getFX() * proximity_diameter + proximity_offset_x + x,
                POINTS[7].getFY() * proximity_diameter + proximity_offset_y + y
        );
        p.close();
        c.drawPath(p, mPaint);

        p.reset();
        p.moveTo(
                (POINTS[12].getFX() + POINTS[13].getFX()) * proximity_diameter + proximity_offset_x + x,
                (POINTS[12].getFY() + POINTS[13].getFY()) * proximity_diameter + proximity_offset_y + y
        );
        p.lineTo(
                (POINTS[9].getFX() + POINTS[13].getFX()) * proximity_diameter + proximity_offset_x + x,
                (POINTS[9].getFY() + POINTS[13].getFY()) * proximity_diameter + proximity_offset_y + y
        );
        p.lineTo(
                (POINTS[10].getFX() + POINTS[13].getFX()) * proximity_diameter + proximity_offset_x + x,
                (POINTS[10].getFY() + POINTS[13].getFY()) * proximity_diameter + proximity_offset_y + y
        );
        p.lineTo(
                POINTS[10].getFX() * proximity_diameter + proximity_offset_x + x,
                POINTS[10].getFY() * proximity_diameter + proximity_offset_y + y
        );
        p.lineTo(
                POINTS[9].getFX() * proximity_diameter + proximity_offset_x + x,
                POINTS[9].getFY() * proximity_diameter + proximity_offset_y + y
        );
        p.lineTo(
                POINTS[12].getFX() * proximity_diameter + proximity_offset_x + x,
                POINTS[12].getFY() * proximity_diameter + proximity_offset_y + y
        );
        p.close();
        c.drawPath(p, mPaint);

        // drawing the yellow shape
        mPaint.setColor(YELLOW_LAYER);
        p.reset();
        p.moveTo(POINTS[0].getFX() * proximity_diameter + proximity_offset_x + x, POINTS[0].getFY() * proximity_diameter + proximity_offset_y + y);
        for(int i = 1; i < POINTS.length - 2; i++){
            if(i != 9){
                p.lineTo(POINTS[i].getFX() * proximity_diameter + proximity_offset_x + x, POINTS[i].getFY() * proximity_diameter + proximity_offset_y + y);
            }else{
                p.lineTo(POINTS[0].getFX() * proximity_diameter + proximity_offset_x + x, POINTS[0].getFY() * proximity_diameter + proximity_offset_y + y);
                p.moveTo(POINTS[i].getFX() * proximity_diameter + proximity_offset_x + x, POINTS[i].getFY() * proximity_diameter + proximity_offset_y + y);
            }
        }
        p.lineTo(POINTS[9].getFX() * proximity_diameter + proximity_offset_x + x, POINTS[9].getFY() * proximity_diameter + proximity_offset_y + y);
        c.drawPath(p, mPaint);

        // restoring the paint
        mPaint.setColor(prevCol);
        mPaint.setStyle(prevStyle);

        // DEBUGGING-PURPOSE
//        c.drawRect(proximity_offset_x + x, proximity_offset_y + y, proximity_offset_x + proximity_diameter + x, proximity_offset_y + proximity_diameter + y, mPaint);
    }

    private static class Vector2D{
        private double mX;
        private double mY;

        public Vector2D(double x, double y){
            mX = x;
            mY = y;
        }

        public double getX(){
            return mX;
        }

        public float getFX(){
            return (float) mX;
        }

        public double getY(){
            return mY;
        }

        public float getFY(){
            return (float) mY;
        }
    }
}

package de.jeanpierrehotz.drawyaownwallpapers.wallpaper.data.visualizer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.media.audiofx.Visualizer;

/**
 * Created by Admin on 16.06.2016.
 */
public class LinearAudioVisualizer extends AudioVisualizer{

    public LinearAudioVisualizer(Visualizer v, VisualizationType vt, int x, int y, int d){
        super(v, vt, x, y, d);
    }

    @Override
    protected void init(){
        mPaint.setStrokeWidth(16f);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void drawWaveFormData(byte[] vals, Canvas c){
        float xPerVal = (float) diameter / (float) vals.length;
        float yPerVal = (float) diameter / 256f;

        Path p = new Path();
        p.moveTo(
                x,
                y + (((int) vals[0]) & 0xFF) * yPerVal
        );
        for(int i = 1; i < vals.length; i++){
            p.lineTo(
                    x + i * xPerVal,
                    y + (((int) vals[i]) & 0xFF) * yPerVal
            );
        }
        c.drawPath(p, mPaint);

    }

    @Override
    protected void drawFFTData(byte[] vals, Canvas c){
        float xPerVal = (float) diameter / ((vals.length / 2f) - 1f);
        float yPerVal = (float) diameter / 128f;


        Path p = new Path();
        p.moveTo(
                x,
                y + diameter - getAbsolute(vals[1], vals[2]) * yPerVal
        );

        for(int i = 1; i < vals.length - 1; i += 2){
            p.lineTo(
                    x + (((i - 1) / 2f) * xPerVal),
                    y + diameter - getAbsolute(vals[i], vals[i + 1]) * yPerVal
            );
        }
        c.drawPath(p, mPaint);
    }
}

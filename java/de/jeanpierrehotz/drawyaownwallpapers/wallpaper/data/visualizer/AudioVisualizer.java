package de.jeanpierrehotz.drawyaownwallpapers.wallpaper.data.visualizer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.audiofx.Visualizer;

/**
 * Created by Admin on 16.06.2016.
 */
public abstract class AudioVisualizer{
    protected final int x;
    protected final int y;
    protected final int diameter;

    private int color;

    protected Paint mPaint;
    protected Visualizer mVisualizer;

    private VisualizationType mVisualizationType;
    private boolean invoked;

    public AudioVisualizer(Visualizer vis, VisualizationType vistype, int x, int y, int diameter){
        if(vistype != null){
            this.mPaint = new Paint();
            this.mVisualizer = vis;
            this.mVisualizationType = vistype;
            this.invoked = false;

            color = Color.WHITE;

            this.x = x;
            this.y = y;
            this.diameter = diameter;

            init();
        }else{
            throw new RuntimeException("Visualizationtype may not be null!");
        }
    }

    public final VisualizationType getVisualizationType(){
        return mVisualizationType;
    }

    public final void setVisualizationType(VisualizationType vistype){
        if(vistype != null){
            this.mVisualizationType = vistype;
        }
    }

    public final void setVisualizer(Visualizer vis){
        while(invoked);
        this.mVisualizer = vis;
    }

    public final  void setColor(int col){
        this.color = col;
    }

    protected void init(){}

    public final void draw(Canvas c){
        mPaint.setColor(color);
        invoked = true;
        if(mVisualizer != null && mVisualizer.getEnabled()){
            byte[] freqVals = new byte[mVisualizer.getCaptureSize()];

            if(mVisualizationType == VisualizationType.waveform && mVisualizer.getWaveForm(freqVals) == Visualizer.SUCCESS){
                drawWaveFormData(freqVals, c);
            }else if(mVisualizationType == VisualizationType.fft && mVisualizer.getFft(freqVals) == Visualizer.SUCCESS){
                drawFFTData(freqVals, c);
            }else{
                for(int i = 0; i < freqVals.length; i++){
                    freqVals[i] = 0;
                }
                drawWaveFormData(freqVals, c);
            }
        }
        invoked = false;
    }

    protected float getAbsolute(byte real, byte imaginary){
        return (float) Math.sqrt(Math.pow(real, 2) + Math.pow(imaginary, 2));
    }

    protected abstract void drawWaveFormData(byte[] vals, Canvas c);
    protected abstract void drawFFTData(byte[] vals, Canvas c);

    public enum VisualizationType{
        waveform, fft
    }
}

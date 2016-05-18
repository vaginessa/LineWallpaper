package de.jeanpierrehotz.drawyaownwallpapers;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.audiofx.Visualizer;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import java.util.ArrayList;

/**
 * Created by Admin on 20.03.2016.
 *
 */
public class LineWallpaperService extends WallpaperService{

    private final Handler handler = new Handler();

    @Override
    public Engine onCreateEngine(){
        return new LineWallpaper();
    }

    private class LineWallpaper extends Engine{
        private final Paint p;
        private final Runnable run;

        private float x, y;
        private boolean visible;

        private int settings_index;

        private ArrayList<Line> permLines;
        private ArrayList<Line> tempLines;

        private Clock       clk;
        private float       clk_xCoordinate;
        private float       clk_yCoordinate;
        private float       clk_diameter;

        private Bitmap      background_bitmap;
        private boolean     background_drawPicture;
        private int         background_color;

        private boolean     lines_drawBalls;
        private boolean     lines_currentlyPermanent;
        private boolean     lines_remove;
        private boolean     lines_fading;
        private boolean     lines_UniColor;
        private boolean     lines_rainbowColor;
        private float       lines_width;
        private float       lines_ballsRadius;
        private int         lines_timeUntilRemoved;
        private int         lines_timeUntilActionPerformed;
        private int         lines_color;
        private int         lines_rainbowSteps;
        private Line.EatingDirection lines_eatingdirection;

        private AudioVisualizer audiovisualizer;
        private Visualizer  audiovisualizer_helpingvisualizer;

        public LineWallpaper(){
            p = new Paint();
            p.setTextAlign(Paint.Align.RIGHT);
            p.setStyle(Paint.Style.FILL);
            p.setTextSize(48f);

            tempLines = new ArrayList<>();
            permLines = new ArrayList<>();

            loadPreferences();

            run = new Runnable(){
                @Override
                public void run(){
                    draw();
                }
            };
        }

        private void loadPreferences(){
            settings_index                      = getSharedPreferences(getString(R.string.settings_settingsPreferences), MODE_PRIVATE).getInt(getString(R.string.settings_selectedSettingPreferences), 0);
            SharedPreferences prefs             = getSharedPreferences(getString(R.string.settings_settingsAt) + settings_index, MODE_PRIVATE);

            lines_UniColor                      = prefs.getBoolean(getString(R.string.lines_unicolor_preferences), false);
            lines_color                         = prefs.getInt(getString(R.string.lines_unicolor_color_preferences), 0xFF0000);
            lines_width                         = prefs.getFloat(getString(R.string.lines_width_preferences), 12f);
            lines_currentlyPermanent            = prefs.getBoolean(getString(R.string.lines_permanent_preferences), false);
            lines_drawBalls                     = prefs.getBoolean(getString(R.string.lines_drawBall_preferences), false);
            lines_ballsRadius                   = prefs.getFloat(getString(R.string.lines_ballSize_preferences), 24f);
            lines_remove                        = prefs.getBoolean(getString(R.string.lines_fadeComplete_preferences), false);
            lines_fading                        = prefs.getBoolean(getString(R.string.lines_fadeSlowly_preferences), true);
            lines_timeUntilRemoved              = prefs.getInt(getString(R.string.lines_fadeComplete_time_preferences), 5000);
            lines_timeUntilActionPerformed      = prefs.getInt(getString(R.string.lines_fadeActionTime_preferences), 50);
            lines_rainbowColor                  = prefs.getBoolean(getString(R.string.lines_rainbowcolor_preferences), true);
            lines_rainbowSteps                  = prefs.getInt(getString(R.string.lines_rainbowcolorsteps_preferences), 10);
            lines_eatingdirection               = Line.EatingDirection.generateFromInt(prefs.getInt(getString(R.string.lines_eatingitselfdirection_preferences), 0));
            background_drawPicture              = prefs.getBoolean(getString(R.string.background_pictureshown_preferences), false);
            background_color                    = prefs.getInt(getString(R.string.background_alternateColor_preferences), 0xF3A8A8);

            if(prefs.getBoolean(getString(R.string.clock_drawClock_preferences), false)){
                clk_diameter                    = ((x > y)? y: x) * prefs.getFloat(getString(R.string.clock_diameter_preferences), 0.8f);
                clk_xCoordinate                 = (x - clk_diameter) * prefs.getFloat(getString(R.string.clock_xposition_preferences), 05f);
                clk_yCoordinate                 = (y - clk_diameter) * prefs.getFloat(getString(R.string.clock_yposition_preferences), 0.5f);

                if(prefs.getBoolean(getString(R.string.clock_simpleClockchosen_preferences), true)){
                    clk                         = new SimpleClock(
                            prefs.getInt(getString(R.string.clock_simpleclock_alphaColor_preferences), 0xC0000000),
                            prefs.getInt(getString(R.string.clock_simpleclock_stdcolor_preferences), 0xFF0000),
                            prefs.getInt(getString(R.string.clock_simpleclock_mincolor_preferences), 0x00FF00),
                            prefs.getInt(getString(R.string.clock_simpleclock_seccolor_preferences), 0x0000FF)
                    );
                }else if(prefs.getBoolean(getString(R.string.clock_pointerOnlyClockchosen_preferences), true)){
                    clk                         = new PointerOnlyClock(
                            prefs.getInt(getString(R.string.clock_pointeronlyclock_stdcolor_preferences), 0xFF0000),
                            prefs.getInt(getString(R.string.clock_pointeronlyclock_mincolor_preferences), 0x00FF00),
                            prefs.getInt(getString(R.string.clock_pointeronlyclock_seccolor_preferences), 0x0000FF)
                    );
                }else if(prefs.getBoolean(getString(R.string.clock_parabolaclockchosen_preferences), true)){
                    clk                         = new ParabolaClock(
                            prefs.getInt(getString(R.string.clock_parabolaclock_alphaColor_preferences), 0xC0FFFFFF),
                            prefs.getInt(getString(R.string.clock_parabolaclock_stdmincolor_preferences), 0xFF7F00),
                            prefs.getInt(getString(R.string.clock_parabolaclock_minsekcolor_preferences), 0x007FFF)
                    );
                }else if(prefs.getBoolean(getString(R.string.clock_digitalClockchosen_preferences), true)){
                    clk                         = new DigitalClock(
                            prefs.getBoolean(getString(R.string.clock_digitalClock_dotsblinking_preferences), true)
                    );
                }
            }else{
                clk = null;
            }

            if(prefs.getBoolean(getString(R.string.visualizer_showVisualizer_preferences), false)){
                audiovisualizer_helpingvisualizer = new Visualizer(0);
                audiovisualizer_helpingvisualizer.setEnabled(true);

                float vis_diameter = prefs.getFloat(getString(R.string.visualizer_diameter_preferences), 0.8f) * x;
                float vis_x = prefs.getFloat(getString(R.string.visualizer_xposition_preferences), 0.5f) * (x - vis_diameter);
                float vis_y = prefs.getFloat(getString(R.string.visualizer_yposition_preferences), 0.5f) * (y - vis_diameter);

                AudioVisualizer.VisualizationType vis_t =
                        (prefs.getBoolean(getString(R.string.visualizer_visualizationtype_waveform_preferences), true))?
                                AudioVisualizer.VisualizationType.waveform:
                                AudioVisualizer.VisualizationType.fft;

                if(prefs.getBoolean(getString(R.string.visualzer_proximityvisualizerchosen_preferences), true)){
                    audiovisualizer = new ProximityAudioVisualizer(audiovisualizer_helpingvisualizer, vis_t, (int) vis_x, (int) vis_y, (int) vis_diameter);
                }else{
                    audiovisualizer = new LinearAudioVisualizer(audiovisualizer_helpingvisualizer, vis_t, (int) vis_x, (int) vis_y, (int) vis_diameter);
                }

                audiovisualizer.setColor(prefs.getInt(getString(R.string.visualizer_visualizationcolor_preferences), Color.WHITE));
            }else{
                audiovisualizer = null;
                if(audiovisualizer_helpingvisualizer != null){
                    audiovisualizer_helpingvisualizer.release();
                    audiovisualizer_helpingvisualizer = null;
                }
            }

            if(background_drawPicture){
                try{
                    Bitmap temp                 = BitmapFactory.decodeFile(prefs.getString(getString(R.string.background_picturepath_preferences), ""));

                    double widthRatio           = x / (double) temp.getWidth();
                    double heightRatio          = y / (double) temp.getHeight();

                    if(widthRatio > heightRatio){
                        background_bitmap       = Bitmap.createScaledBitmap(temp, (int) (widthRatio * temp.getWidth()), (int) (widthRatio * temp.getHeight()), false);
                    }else{
                        background_bitmap       = Bitmap.createScaledBitmap(temp, (int) (heightRatio * temp.getWidth()), (int) (heightRatio * temp.getHeight()), false);
                    }
                }catch(Exception exc){
                    background_bitmap           = null;
                }
            }

            permLines                           = Line.loadFromSharedPreferences(getSharedPreferences(getString(R.string.permanentlines_lineSP) + settings_index, MODE_PRIVATE), getBaseContext());
        }

        public void savePermanentLines(){
            Line.saveToSharedPreferences(permLines, getSharedPreferences(getString(R.string.permanentlines_lineSP) + settings_index, MODE_PRIVATE), getBaseContext());
        }

        private void draw(){
            final SurfaceHolder holder = getSurfaceHolder();
            Canvas c = null;
            try{
                c = holder.lockCanvas();
                if(c != null){
                    if(background_bitmap != null && background_drawPicture){
                        c.drawBitmap(background_bitmap, 0, 0, p);
                    }else{
                        p.setColor(background_color);
                        c.drawPaint(p);
                    }

                    p.setStrokeWidth(lines_width);

                    for(Line l : permLines){
                        l.draw(c, p, lines_drawBalls, lines_ballsRadius);
                    }

                    for(Line l : tempLines){
                        l.draw(c, p, lines_drawBalls, lines_ballsRadius);
                    }

                    if(clk != null){
                        clk.draw(clk_xCoordinate, clk_yCoordinate, clk_diameter, c, p);
                    }

                    if(audiovisualizer != null){
                        audiovisualizer.draw(c);
                    }
                }
            }finally{
                if(c != null){
                    holder.unlockCanvasAndPost(c);
                }
            }
            handler.removeCallbacks(run);
            if(visible && clk != null){
                handler.postDelayed(run, 20);
            }else if(visible){
                handler.postDelayed(run, lines_timeUntilActionPerformed);
            }
        }

        @Override
        public void onTouchEvent(MotionEvent event){
            super.onTouchEvent(event);

            if(event.getAction() == MotionEvent.ACTION_DOWN){
                if(tempLines.size() >= 1 && !tempLines.get(tempLines.size() - 1).isDoomed()){
                    if(lines_remove){
                        tempLines.get(tempLines.size() - 1).addTimer(new Handler(), tempLines, lines_timeUntilRemoved);
                    }else if(lines_fading){
                        tempLines.get(tempLines.size() - 1).fade(lines_timeUntilActionPerformed);
                        tempLines.get(tempLines.size() - 1).addTimer(new Handler(), tempLines, lines_timeUntilActionPerformed * 256 + 1000);
                    }else{
                        tempLines.get(tempLines.size() - 1).injectParasite(lines_timeUntilActionPerformed, lines_eatingdirection);
                        tempLines.get(tempLines.size() - 1).addTimer(new Handler(), tempLines, lines_timeUntilActionPerformed * tempLines.get(tempLines.size() - 1).getCount() + 1000);
                    }
                }

                if(lines_currentlyPermanent){
                    if(lines_UniColor){
                        permLines.add(new Line(event.getX(), event.getY(), lines_color, lines_rainbowColor, lines_rainbowSteps));
                    }else{
                        permLines.add(new Line(event.getX(), event.getY(), lines_rainbowColor, lines_rainbowSteps));
                    }
                }else{
                    if(lines_UniColor){
                        tempLines.add(new Line(event.getX(), event.getY(), lines_color, lines_rainbowColor, lines_rainbowSteps));
                    }else{
                        tempLines.add(new Line(event.getX(), event.getY(), lines_rainbowColor, lines_rainbowSteps));
                    }
                }
            }else if(event.getAction() == MotionEvent.ACTION_MOVE){
                if(lines_currentlyPermanent){
                    permLines.get(permLines.size() - 1).addPoint(event.getX(), event.getY());
                }else{
                    tempLines.get(tempLines.size() - 1).addPoint(event.getX(), event.getY());
                }
            }else if(event.getAction() == MotionEvent.ACTION_UP){
                if(!lines_currentlyPermanent){
                    if(lines_remove){
                        tempLines.get(tempLines.size() - 1).addTimer(new Handler(), tempLines, lines_timeUntilRemoved);
                    }else if(lines_fading){
                        tempLines.get(tempLines.size() - 1).fade(lines_timeUntilActionPerformed);
                        tempLines.get(tempLines.size() - 1).addTimer(new Handler(), tempLines, lines_timeUntilActionPerformed * 256 + 1000);
                    }else{
                        tempLines.get(tempLines.size() - 1).injectParasite(lines_timeUntilActionPerformed, lines_eatingdirection);
                        tempLines.get(tempLines.size() - 1).addTimer(new Handler(), tempLines, lines_timeUntilActionPerformed * tempLines.get(tempLines.size() - 1).getCount() + 1000);
                    }
                }
            }

            handler.post(run);
        }

        @Override
        public void onCreate(SurfaceHolder surfaceHolder){
            super.onCreate(surfaceHolder);

            loadPreferences();
        }

        @Override
        public void onDestroy(){
            super.onDestroy();

            savePermanentLines();
            if(audiovisualizer != null){
                audiovisualizer.setVisualizer(null);
                audiovisualizer_helpingvisualizer.release();
            }
            handler.removeCallbacks(run);
        }

        @Override
        public void onVisibilityChanged(boolean v){
            visible = v;

            if(v){
                loadPreferences();
                draw();
            }else{
                savePermanentLines();
                if(audiovisualizer != null){
                    audiovisualizer.setVisualizer(null);
                    audiovisualizer_helpingvisualizer.release();
                }
                handler.removeCallbacks(run);
            }
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height){
            super.onSurfaceChanged(holder, format, width, height);
            x = width;
            y = height;
            draw();
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder){
            super.onSurfaceDestroyed(holder);
            visible = false;

            savePermanentLines();
            if(audiovisualizer != null){
                audiovisualizer.setVisualizer(null);
                audiovisualizer_helpingvisualizer.release();
            }
            handler.removeCallbacks(run);
        }
    }
}

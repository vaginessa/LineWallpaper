package de.jeanpierrehotz.drawyaownwallpapers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class ChangeSettingsActivity extends AppCompatActivity{

    private static final int FULL_ALPHA = 255 << 24;

    private int settings_index;
    private AlertDialog dialog;

    /*
     * BACKGROUND-PICTURE-SETTINGS:
     */
    private WallpaperPictureSelector mWallpaperSelector;
    private WallpaperPictureSelector.Callback mWallpaperSelectorCallback = new WallpaperPictureSelector.Callback(){
        @Override
        public void onSelectedResult(String file){
            Snackbar.make(findViewById(R.id.space), getString(R.string.bg_image_youselected) + file, Snackbar.LENGTH_SHORT).show();
        }

        @Override
        public void onCropperResult(WallpaperPictureSelector.CropResult result, File srcFile, File outFile){
            if(result == WallpaperPictureSelector.CropResult.success){
                getSharedPreferences(getString(R.string.settings_settingsAt) + settings_index, MODE_PRIVATE).edit().putString(getString(R.string.background_picturepath_preferences), outFile.getAbsolutePath()).apply();
                Snackbar.make(findViewById(R.id.space), R.string.bg_image_youcropped, Snackbar.LENGTH_SHORT).show();
            }else{
                Snackbar.make(findViewById(R.id.space), R.string.bg_image_abortedSelecting, Snackbar.LENGTH_SHORT).show();
            }
        }
    };

    /*
     * LINES-SETTINGS:
     */
    private CheckBox        lines_unicolor_checkbox;
    private TextView        lines_unicolor_colortextview;
    private IntegerPointer  lines_unicolor_color;

    private CheckBox        lines_rainbowcolor_checkbox;
    private TextView        lines_rainbowcolor_textview;
    private SeekBar         lines_rainbowcolor_seekbar;

    private SeekBar         lines_linewidth_seekbar;
    private CheckBox        lines_linesave_checkbox;
    private Button          lines_linesave_deleteLast_Button;
    private Button          lines_linesave_deleteAll_Button;
    private CheckBox        lines_linedrawBall_checkbox;
    private TextView        lines_linesballsize_caption;
    private SeekBar         lines_linesballsize_seekbar;
    private RadioButton     lines_linesfadecomplete_radiobutton;
    private TextView        lines_linesfadecomplete_caption_textview;
    private SeekBar         lines_linesfadecompletetime_seekbar;
    private RadioButton     lines_linesfadeslowly_radiobutton;
    private RadioButton     lines_linesfadeparasite_radiobutton;
    private TextView        lines_linesfadeparasite_direction_caption_textview;
    private TextView        lines_linesfadeparasite_direction_showing_textview;
    private SeekBar         lines_linesfadeparasite_direction_seekbar;
    private TextView        lines_linesfadeslowlyActionTime_caption_textview;
    private SeekBar         lines_linesfadeslowlyActionTime_seekbar;

    /*
     * BACKGROUND-SETTINGS:
     */
    private CheckBox        background_backgroundpicture_checkbox;
    private Button          background_backgroundpicture_button;
    private TextView        background_backgroundcolor_colortextview;
    private IntegerPointer  background_backgroundcolor_color;

    /*
     * CLOCK-SETTINGS:
     */
    private CheckBox        clock_clockenable_checkbox;

    private Button          clock_clockselectClock_Btn;
    private Button          clock_clockchangePosAndSize_Btn;
    private Button          clock_clockadditionalSettings_Btn;

    private float           clock_clockxposition;
    private float           clock_clockyposition;
    private float           clock_clockdiameter;

    private boolean         clock_clock_simpleclock_selected;
    private int             clock_clock_simpleclock_alphabehind;
    private IntegerPointer  clock_clock_simpleclock_color_stunde_color;
    private IntegerPointer  clock_clock_simpleclock_color_minute_color;
    private IntegerPointer  clock_clock_simpleclock_color_sekunde_color;

    private boolean         clock_clock_pointeronly_selected;
    private IntegerPointer  clock_clock_pointeronly_color_stunde_color;
    private IntegerPointer  clock_clock_pointeronly_color_minute_color;
    private IntegerPointer  clock_clock_pointeronly_color_sekunde_color;

    private boolean         clock_clock_parabolaclock_selected;
    private int             clock_clock_parabolaclock_alphabehind;
    private IntegerPointer  clock_clock_parabolaclock_color_stundeminute_color;
    private IntegerPointer  clock_clock_parabolaclock_color_minutesekunde_color;

    private boolean         clock_clock_digitalclock_selected;
    private boolean         clock_clock_digitalclock_dotsblinking;

    /*
     * VISUALIZER-SETTINGS:
     */
    private CheckBox        visualizer_visualizerenable_checkbox;
    private Button          visualizer_visualizer_changesettings_btn;

    private boolean         visualizer_proximity_selected;
    private boolean         visualizer_linear_selected;

    private IntegerPointer  visualizer_visualizationcolor;

    private float           visualizer_x_pos;
    private float           visualizer_y_pos;
    private float           visualizer_diameter;

    private boolean         visualizer_visualizationtype_fft;
    private boolean         visualizer_visualizationtype_waveform;

    /*
     * LISTENER
     */
    private CheckBox.OnCheckedChangeListener        lines_unicolor_checkbox_listener                        = new CompoundButton.OnCheckedChangeListener(){
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b){
            int vis = (lines_unicolor_checkbox.isChecked())? View.VISIBLE: View.GONE;

            lines_unicolor_colortextview.setVisibility(vis);
        }
    };
    private CheckBox.OnCheckedChangeListener        lines_rainbowcolor_checkbox_listener                    = new CompoundButton.OnCheckedChangeListener(){
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b){
            int vis = (lines_rainbowcolor_checkbox.isChecked())? View.VISIBLE: View.GONE;

            lines_rainbowcolor_textview.setVisibility(vis);
            lines_rainbowcolor_seekbar.setVisibility(vis);
        }
    };
    private CheckBox.OnCheckedChangeListener        lines_linesave_listener                                 = new CompoundButton.OnCheckedChangeListener(){
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b){
            int vis = (lines_linesave_checkbox.isChecked())? View.VISIBLE: View.GONE;

            lines_linesave_deleteLast_Button.setVisibility(vis);
            lines_linesave_deleteAll_Button.setVisibility(vis);
        }
    };
    private CheckBox.OnCheckedChangeListener        lines_linedrawBall_checkbox_listener                    = new CompoundButton.OnCheckedChangeListener(){
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b){
            int vis = (lines_linedrawBall_checkbox.isChecked())? View.VISIBLE: View.GONE;

            lines_linesballsize_caption.setVisibility(vis);
            lines_linesballsize_seekbar.setVisibility(vis);
        }
    };
    private RadioButton.OnCheckedChangeListener     lines_linesfade_listener                                = new CompoundButton.OnCheckedChangeListener(){
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b){
            int completeVis = (lines_linesfadecomplete_radiobutton.isChecked())? View.VISIBLE: View.GONE;
            int fadeVis = (lines_linesfadecomplete_radiobutton.isChecked())? View.GONE: View.VISIBLE;
            int eatingVis = (lines_linesfadeparasite_radiobutton.isChecked())? View.VISIBLE: View.GONE;

            lines_linesfadecomplete_caption_textview.setVisibility(completeVis);
            lines_linesfadecompletetime_seekbar.setVisibility(completeVis);
            lines_linesfadeslowlyActionTime_caption_textview.setVisibility(fadeVis);
            lines_linesfadeslowlyActionTime_seekbar.setVisibility(fadeVis);
            lines_linesfadeparasite_direction_caption_textview.setVisibility(eatingVis);
            lines_linesfadeparasite_direction_showing_textview.setVisibility(eatingVis);
            lines_linesfadeparasite_direction_seekbar.setVisibility(eatingVis);
        }
    };
    private SeekBar.OnSeekBarChangeListener         lines_linesfadeparasite_direction_listener = new SeekBar.OnSeekBarChangeListener(){
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b){
            if(lines_linesfadeparasite_direction_seekbar.getProgress() == 0){
                lines_linesfadeparasite_direction_showing_textview.setText(R.string.changeSettings_disappearance_eatingDirection_back);
            }else if(lines_linesfadeparasite_direction_seekbar.getProgress() == 1){
                lines_linesfadeparasite_direction_showing_textview.setText(R.string.changeSettings_disappearance_eatingDirection_front);
            }else{
                lines_linesfadeparasite_direction_showing_textview.setText(R.string.changeSettings_disappearance_eatingDirection_both);
            }
        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar){}
        @Override
        public void onStopTrackingTouch(SeekBar seekBar){}
    };
    private CheckBox.OnCheckedChangeListener        background_backgroundpicture_checkbox_listener          = new CompoundButton.OnCheckedChangeListener(){
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b){
            int pictureVis = (background_backgroundpicture_checkbox.isChecked())? View.VISIBLE: View.GONE;
            int colorVis = (background_backgroundpicture_checkbox.isChecked())? View.GONE: View.VISIBLE;

            background_backgroundpicture_button.setVisibility(pictureVis);
            background_backgroundcolor_colortextview.setVisibility(colorVis);
        }
    };
    private CheckBox.OnCheckedChangeListener        clock_clockenable_listener                              = new CompoundButton.OnCheckedChangeListener(){// TO/DO: Changes
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b){
            int vis = (clock_clockenable_checkbox.isChecked())? View.VISIBLE: View.GONE;

            clock_clockselectClock_Btn.setVisibility(vis);
            clock_clockchangePosAndSize_Btn.setVisibility(vis);
            clock_clockadditionalSettings_Btn.setVisibility(vis);
        }
    };
    private CheckBox.OnCheckedChangeListener        visualizer_visualizerenable_checkbox_listener           = new CompoundButton.OnCheckedChangeListener(){
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b){
            int vis = (visualizer_visualizerenable_checkbox.isChecked())? View.VISIBLE: View.GONE;

            visualizer_visualizer_changesettings_btn.setVisibility(vis);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_change_settings);
        setSupportActionBar(toolbar);
        settings_index = getIntent().getIntExtra(getString(R.string.intent_settings_index), 0);

        /*
         * First initialize layout and objects, so they will work
         */
        mWallpaperSelector = new WallpaperPictureSelector(this);
        mWallpaperSelector.setCallback(mWallpaperSelectorCallback);

        lines_unicolor_color                                            = new IntegerPointer();
        background_backgroundcolor_color                                = new IntegerPointer();
        clock_clock_simpleclock_color_stunde_color                      = new IntegerPointer();
        clock_clock_simpleclock_color_minute_color                      = new IntegerPointer();
        clock_clock_simpleclock_color_sekunde_color                     = new IntegerPointer();
        clock_clock_pointeronly_color_stunde_color                      = new IntegerPointer();
        clock_clock_pointeronly_color_minute_color                      = new IntegerPointer();
        clock_clock_pointeronly_color_sekunde_color                     = new IntegerPointer();
        clock_clock_parabolaclock_color_stundeminute_color              = new IntegerPointer();
        clock_clock_parabolaclock_color_minutesekunde_color             = new IntegerPointer();
        visualizer_visualizationcolor                                   = new IntegerPointer();

        lines_unicolor_checkbox                                         = (CheckBox)        findViewById(R.id.unicolor_checkbox);
        lines_unicolor_colortextview                                    = (TextView)        findViewById(R.id.unicolor_colortextview);
        lines_unicolor_checkbox.setOnCheckedChangeListener(lines_unicolor_checkbox_listener);

        lines_rainbowcolor_checkbox                                     = (CheckBox)        findViewById(R.id.rainbowcolor_checkbox);
        lines_rainbowcolor_textview                                     = (TextView)        findViewById(R.id.rainbowcolor_textview);
        lines_rainbowcolor_seekbar                                      = (SeekBar)         findViewById(R.id.rainbowcolor_seekbar);
        lines_rainbowcolor_checkbox.setOnCheckedChangeListener(lines_rainbowcolor_checkbox_listener);

        lines_linewidth_seekbar                                         = (SeekBar)         findViewById(R.id.linewidth_seekbar);

        lines_linesave_checkbox                                         = (CheckBox)        findViewById(R.id.linesave_checkbox);
        lines_linesave_deleteLast_Button                                = (Button)          findViewById(R.id.linesave_deleteLast_Button);
        lines_linesave_deleteAll_Button                                 = (Button)          findViewById(R.id.linesave_deleteAll_Button);
        lines_linesave_checkbox.setOnCheckedChangeListener(lines_linesave_listener);

        lines_linedrawBall_checkbox                                     = (CheckBox)        findViewById(R.id.linedrawBall_checkbox);
        lines_linesballsize_caption                                     = (TextView)        findViewById(R.id.linesballsize_caption);
        lines_linesballsize_seekbar                                     = (SeekBar)         findViewById(R.id.linesballsize_seekbar);
        lines_linedrawBall_checkbox.setOnCheckedChangeListener(lines_linedrawBall_checkbox_listener);

        lines_linesfadecomplete_radiobutton                             = (RadioButton)     findViewById(R.id.linesfadecomplete_radiobutton);
        lines_linesfadecomplete_caption_textview                        = (TextView)        findViewById(R.id.linesfadecomplete_caption_textview);
        lines_linesfadecompletetime_seekbar                             = (SeekBar)         findViewById(R.id.linesfadecompletetime_seekbar);
        lines_linesfadeslowly_radiobutton                               = (RadioButton)     findViewById(R.id.linesfadeslowly_radiobutton);
        lines_linesfadeparasite_radiobutton                             = (RadioButton)     findViewById(R.id.linesfadeparasite_radiobutton);
        lines_linesfadeparasite_direction_caption_textview              = (TextView)        findViewById(R.id.linesfadeparasite_direction_caption_textview);
        lines_linesfadeparasite_direction_showing_textview              = (TextView)        findViewById(R.id.linesfadeparasite_direction_showing_textview);
        lines_linesfadeparasite_direction_seekbar                       = (SeekBar)         findViewById(R.id.linesfadeparasite_direction_seekbar);
        lines_linesfadeslowlyActionTime_caption_textview                = (TextView)        findViewById(R.id.linesfadeslowlyActionTime_caption_textview);
        lines_linesfadeslowlyActionTime_seekbar                         = (SeekBar)         findViewById(R.id.linesfadeslowlyActionTime_seekbar);
        lines_linesfadecomplete_radiobutton.setOnCheckedChangeListener(lines_linesfade_listener);
        lines_linesfadeslowly_radiobutton.setOnCheckedChangeListener(lines_linesfade_listener);
        lines_linesfadeparasite_radiobutton.setOnCheckedChangeListener(lines_linesfade_listener);
        lines_linesfadeparasite_direction_seekbar.setOnSeekBarChangeListener(lines_linesfadeparasite_direction_listener);

        background_backgroundpicture_checkbox                           = (CheckBox)        findViewById(R.id.backgroundpicture_checkbox);
        background_backgroundpicture_button                             = (Button)          findViewById(R.id.backgroundpicture_button);
        background_backgroundcolor_colortextview                        = (TextView)        findViewById(R.id.backgroundcolor_colortextview);
        background_backgroundpicture_checkbox.setOnCheckedChangeListener(background_backgroundpicture_checkbox_listener);

        clock_clockenable_checkbox                                      = (CheckBox)        findViewById(R.id.clockenable_checkbox);
        clock_clockselectClock_Btn                                      = (Button)          findViewById(R.id.clockselectClock_Btn);
        clock_clockchangePosAndSize_Btn                                 = (Button)          findViewById(R.id.clockchangePosAndSize_Btn);
        clock_clockadditionalSettings_Btn                               = (Button)          findViewById(R.id.clockadditionalSettings_Btn);
        clock_clockenable_checkbox.setOnCheckedChangeListener(clock_clockenable_listener);

        visualizer_visualizerenable_checkbox                            = (CheckBox)        findViewById(R.id.visualizerenable_checkbox);
        visualizer_visualizer_changesettings_btn                        = (Button)          findViewById(R.id.visualizer_changesettings_btn);
        visualizer_visualizerenable_checkbox.setOnCheckedChangeListener(visualizer_visualizerenable_checkbox_listener);

        /*
         * Make the drop-down layout responsive to set-Calls
         *  -> make it show nothing and if it's set to true it will show its stuff
         */
        lines_unicolor_checkbox.setChecked(true);
        lines_rainbowcolor_checkbox.setChecked(true);
        lines_linesave_checkbox.setChecked(true);
        lines_linedrawBall_checkbox.setChecked(true);
        lines_linesfadecomplete_radiobutton.setChecked(true);
        lines_linesfadeparasite_direction_seekbar.setProgress(1);
        background_backgroundpicture_checkbox.setChecked(true);
        clock_clockenable_checkbox.setChecked(true);
        visualizer_visualizerenable_checkbox.setChecked(true);

        if(getSharedPreferences(getString(R.string.settings_settingsAt) + settings_index, MODE_PRIVATE).getBoolean("firstTime", true)){
            getSharedPreferences(getString(R.string.settings_settingsAt) + settings_index, MODE_PRIVATE).edit().putBoolean("firstTime", false).apply();

            /*
            * On first-launch we set everything to the default-values
            */

            lines_unicolor_checkbox.setChecked(false);
            lines_unicolor_color.value = Color.rgb(255, 0, 0);

            lines_rainbowcolor_checkbox.setChecked(true);
            lines_rainbowcolor_seekbar.setProgress(10);

            lines_linewidth_seekbar.setProgress(120);
            lines_linesave_checkbox.setChecked(false);
            lines_linedrawBall_checkbox.setChecked(false);
            lines_linesballsize_seekbar.setProgress(240);
            lines_linesfadeslowly_radiobutton.setChecked(true);
            lines_linesfadecompletetime_seekbar.setProgress(20);
            lines_linesfadeparasite_direction_seekbar.setProgress(0);
            lines_linesfadeslowlyActionTime_seekbar.setProgress(50);

            background_backgroundpicture_checkbox.setChecked(false);
            background_backgroundcolor_color.value = Color.rgb(243, 168, 168);

            clock_clockenable_checkbox.setChecked(false);

            clock_clockxposition = 0.5f;
            clock_clockyposition = 0.5f;
            clock_clockdiameter = 0.8f;

            clock_clock_simpleclock_selected = true;
            clock_clock_simpleclock_alphabehind = 192;
            clock_clock_simpleclock_color_stunde_color.value = Color.rgb(255, 0, 0);
            clock_clock_simpleclock_color_minute_color.value = Color.rgb(0, 255, 0);
            clock_clock_simpleclock_color_sekunde_color.value = Color.rgb(0, 0, 255);

            clock_clock_pointeronly_selected = false;
            clock_clock_pointeronly_color_stunde_color.value = Color.rgb(255, 0, 0);
            clock_clock_pointeronly_color_minute_color.value = Color.rgb(0, 255, 0);
            clock_clock_pointeronly_color_sekunde_color.value = Color.rgb(0, 0, 255);

            clock_clock_parabolaclock_selected = false;
            clock_clock_parabolaclock_alphabehind = 192;
            clock_clock_parabolaclock_color_stundeminute_color.value = Color.rgb(255, 127, 0);
            clock_clock_parabolaclock_color_minutesekunde_color.value = Color.rgb(0, 127, 255);

            clock_clock_digitalclock_selected = false;
            clock_clock_digitalclock_dotsblinking = true;

            visualizer_visualizerenable_checkbox.setChecked(false);

            visualizer_proximity_selected = true;
            visualizer_linear_selected = false;

            visualizer_visualizationcolor.value = Color.WHITE;

            visualizer_x_pos = 0.5f;
            visualizer_y_pos = 0.5f;
            visualizer_diameter = 0.8f;

            visualizer_visualizationtype_fft = false;
            visualizer_visualizationtype_waveform = false;

        }else{

            /*
            * Otherwise we load all the values and show them in the layout
            */

            SharedPreferences prefs = getSharedPreferences(getString(R.string.settings_settingsAt) + settings_index, MODE_PRIVATE);

            lines_unicolor_checkbox.setChecked(prefs.getBoolean(getString(R.string.lines_unicolor_preferences), false));
            lines_unicolor_color.value = prefs.getInt(getString(R.string.lines_unicolor_color_preferences), 0xFF0000);

            lines_rainbowcolor_checkbox.setChecked(prefs.getBoolean(getString(R.string.lines_rainbowcolor_preferences), false));
            lines_rainbowcolor_seekbar.setProgress(prefs.getInt(getString(R.string.lines_rainbowcolorsteps_preferences), 10));

            lines_linewidth_seekbar.setProgress((int) (prefs.getFloat(getString(R.string.lines_width_preferences), 12f) * 10));

            lines_linesave_checkbox.setChecked(prefs.getBoolean(getString(R.string.lines_permanent_preferences), false));

            lines_linedrawBall_checkbox.setChecked(prefs.getBoolean(getString(R.string.lines_drawBall_preferences), false));
            lines_linesballsize_seekbar.setProgress((int) (prefs.getFloat(getString(R.string.lines_ballSize_preferences), 24f) * 10));

            lines_linesfadecomplete_radiobutton.setChecked(prefs.getBoolean(getString(R.string.lines_fadeComplete_preferences), false));
            lines_linesfadecompletetime_seekbar.setProgress(prefs.getInt(getString(R.string.lines_fadeComplete_time_preferences), 5000) / 100);
            lines_linesfadeslowly_radiobutton.setChecked(prefs.getBoolean(getString(R.string.lines_fadeSlowly_preferences), true));
            lines_linesfadeparasite_radiobutton.setChecked(prefs.getBoolean(getString(R.string.lines_fadeParasite_preferences), false));
            lines_linesfadeparasite_direction_seekbar.setProgress(prefs.getInt(getString(R.string.lines_eatingitselfdirection_preferences), 0));
            lines_linesfadeslowlyActionTime_seekbar.setProgress(prefs.getInt(getString(R.string.lines_fadeActionTime_preferences), 50));

            background_backgroundpicture_checkbox.setChecked(prefs.getBoolean(getString(R.string.background_pictureshown_preferences), false));
            background_backgroundcolor_color.value = prefs.getInt(getString(R.string.background_alternateColor_preferences), 0xF3A8A8);

            clock_clockenable_checkbox.setChecked(prefs.getBoolean(getString(R.string.clock_drawClock_preferences), false));

            clock_clockxposition = prefs.getFloat(getString(R.string.clock_xposition_preferences), 0.5f);
            clock_clockyposition = prefs.getFloat(getString(R.string.clock_yposition_preferences), 0.5f);
            clock_clockdiameter = prefs.getFloat(getString(R.string.clock_diameter_preferences), 0.8f);

            clock_clock_simpleclock_selected = prefs.getBoolean(getString(R.string.clock_simpleClockchosen_preferences), true);
            clock_clock_simpleclock_alphabehind = Color.alpha(prefs.getInt(getString(R.string.clock_simpleclock_alphaColor_preferences), 0xC0FFFFFF));
            clock_clock_simpleclock_color_stunde_color.value = prefs.getInt(getString(R.string.clock_simpleclock_stdcolor_preferences), 0xFF0000);
            clock_clock_simpleclock_color_minute_color.value = prefs.getInt(getString(R.string.clock_simpleclock_mincolor_preferences), 0x00FF00);
            clock_clock_simpleclock_color_sekunde_color.value = prefs.getInt(getString(R.string.clock_simpleclock_seccolor_preferences), 0x0000FF);

            clock_clock_pointeronly_selected = prefs.getBoolean(getString(R.string.clock_pointerOnlyClockchosen_preferences), false);
            clock_clock_pointeronly_color_stunde_color.value = prefs.getInt(getString(R.string.clock_pointeronlyclock_stdcolor_preferences), 0xFF0000);
            clock_clock_pointeronly_color_minute_color.value = prefs.getInt(getString(R.string.clock_pointeronlyclock_mincolor_preferences), 0x00FF00);
            clock_clock_pointeronly_color_sekunde_color.value = prefs.getInt(getString(R.string.clock_pointeronlyclock_seccolor_preferences), 0x0000FF);

            clock_clock_parabolaclock_selected = prefs.getBoolean(getString(R.string.clock_parabolaclockchosen_preferences), false);
            clock_clock_parabolaclock_alphabehind = Color.alpha(prefs.getInt(getString(R.string.clock_parabolaclock_alphaColor_preferences), 0xC0FFFFFF));
            clock_clock_parabolaclock_color_stundeminute_color.value = prefs.getInt(getString(R.string.clock_parabolaclock_stdmincolor_preferences), 0xFF7F00);
            clock_clock_parabolaclock_color_minutesekunde_color.value = prefs.getInt(getString(R.string.clock_parabolaclock_minsekcolor_preferences), 0x007FFF);

            clock_clock_digitalclock_selected = prefs.getBoolean(getString(R.string.clock_digitalClockchosen_preferences), false);
            clock_clock_digitalclock_dotsblinking = prefs.getBoolean(getString(R.string.clock_digitalClock_dotsblinking_preferences), true);

            visualizer_visualizerenable_checkbox.setChecked(prefs.getBoolean(getString(R.string.visualizer_showVisualizer_preferences), false));
            visualizer_proximity_selected = prefs.getBoolean(getString(R.string.visualzer_proximityvisualizerchosen_preferences), true);
            visualizer_linear_selected = prefs.getBoolean(getString(R.string.visualzer_linearvisualizerchosen_preferences), true);
            visualizer_visualizationcolor.value = prefs.getInt(getString(R.string.visualizer_visualizationcolor_preferences), Color.WHITE);
            visualizer_x_pos = prefs.getFloat(getString(R.string.visualizer_xposition_preferences), 0.5f);
            visualizer_y_pos = prefs.getFloat(getString(R.string.visualizer_yposition_preferences), 0.5f);;
            visualizer_diameter = prefs.getFloat(getString(R.string.visualizer_diameter_preferences), 0.8f);;
            visualizer_visualizationtype_fft = prefs.getBoolean(getString(R.string.visualizer_visualizationtype_fft_preferences), false);
            visualizer_visualizationtype_waveform = prefs.getBoolean(getString(R.string.visualizer_visualizationtype_waveform_preferences), true);

        }

        /*
        * Finally we show the set colors
        */
        setColorOfTextView(lines_unicolor_colortextview, lines_unicolor_color.value);
        setColorOfTextView(background_backgroundcolor_colortextview, background_backgroundcolor_color.value);
    }//end of onCreate()

    private void setColorOfTextView(TextView tv, int col){
        tv.setBackgroundColor(col);
        tv.setTextColor(~col | FULL_ALPHA);
    }

    private void editColor(final TextView tv, final IntegerPointer col, int title){
        final int prevCol = col.value;

        AlertDialog shown = new AlertDialog.Builder(ChangeSettingsActivity.this)
                .setTitle(title)
                .setView(R.layout.layout_dialog_changecolor)
                .setPositiveButton(R.string.dialog_ok, null)
                .setNegativeButton(R.string.dialog_abort, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i){
                        col.value = prevCol;
                        setColorOfTextView(tv, col.value);
                    }
                })
                .show();

        GradientView top = (GradientView) shown.findViewById(R.id.top);
        GradientView bottom = (GradientView) shown.findViewById(R.id.bottom);

        top.setBrightnessGradientView(bottom);

        bottom.setOnColorChangedListener(new GradientView.OnColorChangedListener(){
            @Override
            public void onColorChanged(GradientView view, int color){
                col.value = color;
                setColorOfTextView(tv, col.value);
            }
        });

        top.setColor(prevCol);
    }

    private void editDigitalClockSettings(){
        dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.changeSettings_clock_digital_caption)
                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i){
                        clock_clock_digitalclock_dotsblinking = ((CheckBox) dialog.findViewById(R.id.dialog_changespecificclocksettings_digitalclock_dotsblinking_checkbox)).isChecked();
                    }
                })
                .setNegativeButton(R.string.dialog_abort, null)
                .setView(R.layout.layout_dialog_changespecificclocksettings_digital)
                .show();

        ((CheckBox) dialog.findViewById(R.id.dialog_changespecificclocksettings_digitalclock_dotsblinking_checkbox)).setChecked(clock_clock_digitalclock_dotsblinking);
    }
    private void editParabolaClockSettings(){
        final int prevHouMinCol = clock_clock_parabolaclock_color_stundeminute_color.value;
        final int prevSecMinCol = clock_clock_parabolaclock_color_minutesekunde_color.value;

        dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.changeSettings_clock_parabola_caption)
                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i){
                        clock_clock_parabolaclock_alphabehind = ((SeekBar) dialog.findViewById(R.id.dialog_changespecificclocksettings_parabola_alphabehind_seekbar)).getProgress();
                    }
                })
                .setNegativeButton(R.string.dialog_abort, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i){
                        clock_clock_parabolaclock_color_stundeminute_color.value  = prevHouMinCol;
                        clock_clock_parabolaclock_color_minutesekunde_color.value = prevSecMinCol;
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener(){
                    @Override
                    public void onCancel(DialogInterface dialogInterface){
                        clock_clock_parabolaclock_color_stundeminute_color.value  = prevHouMinCol;
                        clock_clock_parabolaclock_color_minutesekunde_color.value = prevSecMinCol;
                    }
                })
                .setView(R.layout.layout_dialog_changespecificclocksettings_parabola)
                .show();

        ((SeekBar) dialog.findViewById(R.id.dialog_changespecificclocksettings_parabola_alphabehind_seekbar)).setProgress(clock_clock_parabolaclock_alphabehind);
        final TextView stdMinTextView = (TextView) dialog.findViewById(R.id.dialog_changespecificclocksettings_parabola_colorstundeminute_textview);
        setColorOfTextView(stdMinTextView, clock_clock_parabolaclock_color_stundeminute_color.value);
        stdMinTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
//                editParabolaClockHourMinuteColor(stdMinTextView);
                editColor(
                        stdMinTextView,
                        clock_clock_parabolaclock_color_stundeminute_color,
                        R.string.changeSettings_clock_parabola_houmincolor
                );
            }
        });

        final TextView minSecTextView = (TextView) dialog.findViewById(R.id.dialog_changespecificclocksettings_parabola_colorminutesekunde_textview);
        setColorOfTextView(minSecTextView, clock_clock_parabolaclock_color_minutesekunde_color.value);
        minSecTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
//                editParabolaClockMinuteSecondColor(minSecTextView);
                editColor(
                        minSecTextView,
                        clock_clock_parabolaclock_color_minutesekunde_color,
                        R.string.changeSettings_clock_parabola_minseccolor
                );
            }
        });
    }
    private void editPointerOnlyClockSettings(){
        final int prevStdCol = clock_clock_pointeronly_color_stunde_color.value;
        final int prevMinCol = clock_clock_pointeronly_color_minute_color.value;
        final int prevSekCol = clock_clock_pointeronly_color_sekunde_color.value;

        dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.changeSettings_clock_pointeronly_caption)
                .setPositiveButton(R.string.dialog_ok, null)
                .setNegativeButton(R.string.dialog_abort, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i){
                        clock_clock_pointeronly_color_stunde_color.value  = prevStdCol;
                        clock_clock_pointeronly_color_minute_color.value  = prevMinCol;
                        clock_clock_pointeronly_color_sekunde_color.value = prevSekCol;
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener(){
                    @Override
                    public void onCancel(DialogInterface dialogInterface){
                        clock_clock_pointeronly_color_stunde_color.value  = prevStdCol;
                        clock_clock_pointeronly_color_minute_color.value  = prevMinCol;
                        clock_clock_pointeronly_color_sekunde_color.value = prevSekCol;
                    }
                })
                .setView(R.layout.layout_dialog_changespecificclocksettings_pointeronly)
                .show();

        final TextView stdTextView = (TextView) dialog.findViewById(R.id.dialog_changespecificclocksettings_pointeronly_colorstunde_textview);
        setColorOfTextView(stdTextView, clock_clock_pointeronly_color_stunde_color.value);
        stdTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
//                editPointerOnlyClockHourColor(stdTextView);
                editColor(
                        stdTextView,
                        clock_clock_pointeronly_color_stunde_color,
                        R.string.changeSettings_clock_pointeronly_hourcolor
                );
            }
        });

        final TextView minTextView = (TextView) dialog.findViewById(R.id.dialog_changespecificclocksettings_pointeronly_colorminute_textview);
        setColorOfTextView(minTextView, clock_clock_pointeronly_color_minute_color.value);
        minTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
//                editPointerOnlyClockMinuteColor(minTextView);
                editColor(
                        minTextView,
                        clock_clock_pointeronly_color_minute_color,
                        R.string.changeSettings_clock_pointeronly_minutecolor
                );
            }
        });

        final TextView sekTextView = (TextView) dialog.findViewById(R.id.dialog_changespecificclocksettings_pointeronly_colorsekunde_textview);
        setColorOfTextView(sekTextView, clock_clock_pointeronly_color_sekunde_color.value);
        sekTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
//                editPointerOnlyClockSekundeColor(sekTextView);
                editColor(
                        sekTextView,
                        clock_clock_pointeronly_color_sekunde_color,
                        R.string.changeSettings_clock_pointeronly_secondcolor
                );
            }
        });
    }
    private void editSimpleClockSettings(){
        final int prevAlpha =  clock_clock_simpleclock_alphabehind;
        final int prevStdCol = clock_clock_simpleclock_color_stunde_color.value;
        final int prevMinCol = clock_clock_simpleclock_color_minute_color.value;
        final int prevSekCol = clock_clock_simpleclock_color_sekunde_color.value;

        dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.changeSettings_clock_simpleclock_caption)
                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i){
                        clock_clock_simpleclock_alphabehind = ((SeekBar) dialog.findViewById(R.id.dialog_changespecificclocksettings_simpleclock_alphabehind_seekbar)).getProgress();
                    }
                })
                .setNegativeButton(R.string.dialog_abort, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i){
                        clock_clock_simpleclock_alphabehind = prevAlpha;
                        clock_clock_simpleclock_color_stunde_color.value = prevStdCol;
                        clock_clock_simpleclock_color_minute_color.value = prevMinCol;
                        clock_clock_simpleclock_color_sekunde_color.value = prevSekCol;
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener(){
                    @Override
                    public void onCancel(DialogInterface dialogInterface){
                        clock_clock_simpleclock_alphabehind = prevAlpha;
                        clock_clock_simpleclock_color_stunde_color.value = prevStdCol;
                        clock_clock_simpleclock_color_minute_color.value = prevMinCol;
                        clock_clock_simpleclock_color_sekunde_color.value = prevSekCol;
                    }
                })
                .setView(R.layout.layout_dialog_changespecificclocksettings_simpleclock)
                .show();

        ((SeekBar) dialog.findViewById(R.id.dialog_changespecificclocksettings_simpleclock_alphabehind_seekbar)).setProgress(clock_clock_simpleclock_alphabehind);

        final TextView stdTextView = (TextView) dialog.findViewById(R.id.dialog_changespecificclocksettings_simpleclock_color_stunde_textview);
        setColorOfTextView(stdTextView, clock_clock_simpleclock_color_stunde_color.value);
        stdTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
//                editSimpleClockHourColor(stdTextView);
                editColor(
                        stdTextView,
                        clock_clock_simpleclock_color_stunde_color,
                        R.string.changeSettings_clock_simpleclock_hourcolor
                );
            }
        });

        final TextView minTextView = (TextView) dialog.findViewById(R.id.dialog_changespecificclocksettings_simpleclock_color_minute_textview);
        setColorOfTextView(minTextView, clock_clock_simpleclock_color_minute_color.value);
        minTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
//                editSimpleClockMinuteColor(minTextView);
                editColor(
                        minTextView,
                        clock_clock_simpleclock_color_minute_color,
                        R.string.changeSettings_clock_simpleclock_minutecolor
                );
            }
        });

        final TextView sekTextView = (TextView) dialog.findViewById(R.id.dialog_changespecificclocksettings_simpleclock_color_sekunde_textview);
        setColorOfTextView(sekTextView, clock_clock_simpleclock_color_sekunde_color.value);
        sekTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
//                editSimpleClockSekundeColor(sekTextView);
                editColor(
                        sekTextView,
                        clock_clock_simpleclock_color_sekunde_color,
                        R.string.changeSettings_clock_simpleclock_secondcolor
                );
            }
        });
    }

    /*
    ONCLICK-LISTENER METHODS
     */
    public void changeUniColorColor(View view){
        editColor(lines_unicolor_colortextview, lines_unicolor_color, R.string.changeColor_unicolor_title);
    }

    public void changeBackgroundColorColor(View view){
        editColor(background_backgroundcolor_colortextview, background_backgroundcolor_color, R.string.changeColor_backgroundcolor_title);
    }

    public void deleteAllPermanentLines(View v){
        new AlertDialog.Builder(this)
                .setTitle(R.string.deleteAllPermanentLines_title)
                .setMessage(R.string.deleteAllPermanentLines_message)
                .setPositiveButton(R.string.dialog_positive, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i){
                        ArrayList<Line> lines = Line.loadFromSharedPreferences(getSharedPreferences(getString(R.string.permanentlines_lineSP) + settings_index, MODE_PRIVATE), getBaseContext());

                        if(lines.size() >= 1){
                            do{
                                lines.remove(lines.size() - 1);
                            }while(lines.size() != 0);
                            Line.saveToSharedPreferences(lines, getSharedPreferences(getString(R.string.permanentlines_lineSP) + settings_index, MODE_PRIVATE), getBaseContext());
                            Snackbar.make(findViewById(R.id.space), R.string.deleteLines_success, Snackbar.LENGTH_SHORT).show();
                        }else{
                            final Snackbar sb = Snackbar.make(findViewById(R.id.space), R.string.alertMessage_noPermLines, Snackbar.LENGTH_INDEFINITE);
                            sb.setAction(R.string.dialog_ok, new View.OnClickListener(){
                                @Override
                                public void onClick(View view){
                                    sb.dismiss();
                                }
                            }).show();
                        }
                    }
                })
                .setNegativeButton(R.string.dialog_negative, null)
                .show();
    }

    public void deleteLastPermanentLine(View v){
        new AlertDialog.Builder(this)
                .setTitle(R.string.deleteLastPermanentLine_title)
                .setMessage(R.string.deleteLastPermanentLine_message)
                .setPositiveButton(R.string.dialog_positive, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i){
                        ArrayList<Line> lines = Line.loadFromSharedPreferences(getSharedPreferences(getString(R.string.permanentlines_lineSP) + settings_index, MODE_PRIVATE), getBaseContext());

                        if(lines.size() >= 1){
                            lines.remove(lines.size() - 1);
                            Line.saveToSharedPreferences(lines, getSharedPreferences(getString(R.string.permanentlines_lineSP) + settings_index, MODE_PRIVATE), getBaseContext());
                            Snackbar.make(findViewById(R.id.space), R.string.deleteLines_success, Snackbar.LENGTH_SHORT).show();
                        }else{
                            final Snackbar sb = Snackbar.make(findViewById(R.id.space), R.string.alertMessage_noPermLines, Snackbar.LENGTH_INDEFINITE);
                            sb.setAction(R.string.dialog_ok, new View.OnClickListener(){
                                @Override
                                public void onClick(View view){
                                    sb.dismiss();
                                }
                            }).show();
                        }
                    }
                })
                .setNegativeButton(R.string.dialog_negative, null)
                .show();
    }

    public void selectBackgroundPicture(View v){
        mWallpaperSelector.selectImage(this);
    }

    public void selectClock(View view){
        dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.changeSettings_clock_selectClock_caption)
                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i){
                        clock_clock_simpleclock_selected = ((RadioButton) dialog.findViewById(R.id.dialog_selectclock_selectedsimpleclock_radiobutton)).isChecked();
                        clock_clock_pointeronly_selected = ((RadioButton) dialog.findViewById(R.id.dialog_selectclock_selectedpointeronlyclock_radiobutton)).isChecked();
                        clock_clock_parabolaclock_selected = ((RadioButton) dialog.findViewById(R.id.dialog_selectclock_selectedparabolaclock_radiobutton)).isChecked();
                        clock_clock_digitalclock_selected = ((RadioButton) dialog.findViewById(R.id.dialog_selectclock_selecteddigitalclock_radiobutton)).isChecked();
                    }
                })
                .setNegativeButton(R.string.dialog_abort, null)
                .setView(R.layout.layout_dialog_selectclock)
                .show();

        ((RadioButton) dialog.findViewById(R.id.dialog_selectclock_selectedsimpleclock_radiobutton)).setChecked(clock_clock_simpleclock_selected);
        ((RadioButton) dialog.findViewById(R.id.dialog_selectclock_selectedpointeronlyclock_radiobutton)).setChecked(clock_clock_pointeronly_selected);
        ((RadioButton) dialog.findViewById(R.id.dialog_selectclock_selectedparabolaclock_radiobutton)).setChecked(clock_clock_parabolaclock_selected);
        ((RadioButton) dialog.findViewById(R.id.dialog_selectclock_selecteddigitalclock_radiobutton)).setChecked(clock_clock_digitalclock_selected);
    }

    public void changeClockPositionAndSize(View view){
        dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.changeSettings_clock_changeposandsize_caption)
                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i){
                        clock_clockxposition = ((SeekBar) dialog.findViewById(R.id.dialog_changeclocksizeandposition_clockxposition_seekbar)).getProgress() / 1000f;
                        clock_clockyposition = ((SeekBar) dialog.findViewById(R.id.dialog_changeclocksizeandposition_clockyposition_seekbar)).getProgress() / 1000f;
                        clock_clockdiameter = ((SeekBar) dialog.findViewById(R.id.dialog_changeclocksizeandposition_clockdiameter_seekbar)).getProgress() / 1000f;
                    }
                })
                .setNegativeButton(R.string.dialog_abort, null)
                .setView(R.layout.layout_dialog_changeclocksizeandposition)
                .show();

        ((SeekBar) dialog.findViewById(R.id.dialog_changeclocksizeandposition_clockxposition_seekbar)).setProgress((int) (clock_clockxposition * 1000));
        ((SeekBar) dialog.findViewById(R.id.dialog_changeclocksizeandposition_clockyposition_seekbar)).setProgress((int) (clock_clockyposition * 1000));
        ((SeekBar) dialog.findViewById(R.id.dialog_changeclocksizeandposition_clockdiameter_seekbar)).setProgress((int) (clock_clockdiameter * 1000));
    }

    public void changeAdditionalClockSettings(View view){
        if(clock_clock_simpleclock_selected){
            editSimpleClockSettings();
        }else if(clock_clock_pointeronly_selected){
            editPointerOnlyClockSettings();
        }else if(clock_clock_parabolaclock_selected){
            editParabolaClockSettings();
        }else if(clock_clock_digitalclock_selected){
            editDigitalClockSettings();
        }
    }

    public void changeVisualizerSettings(View view){
        final int prevVisualizerColor = visualizer_visualizationcolor.value;

        dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.changeSettings_visualizer_caption)
                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i){
                        visualizer_proximity_selected = ((RadioButton) dialog.findViewById(R.id.dialog_changeVisualizerSettings_proximityselected_radiobtn)).isChecked();
                        visualizer_linear_selected = ((RadioButton) dialog.findViewById(R.id.dialog_changeVisualizerSettings_linearselected_radiobtn)).isChecked();

                        visualizer_x_pos = (float) ((SeekBar) dialog.findViewById(R.id.dialog_changeVisualizerSettings_visualizerxposition_seekbar)).getProgress() / 1000f;
                        visualizer_y_pos = (float) ((SeekBar) dialog.findViewById(R.id.dialog_changeVisualizerSettings_visualizeryposition_seekbar)).getProgress() / 1000f;
                        visualizer_diameter = (float) ((SeekBar) dialog.findViewById(R.id.dialog_changeVisualizerSettings_visualizerdiameter_seekbar)).getProgress() / 1000f;

                        visualizer_visualizationtype_waveform = ((RadioButton) dialog.findViewById(R.id.dialog_changeVisualizerSettings_visualizertypewaveform_radiobtn)).isChecked();
                        visualizer_visualizationtype_fft = ((RadioButton) dialog.findViewById(R.id.dialog_changeVisualizerSettings_visualizertypefft_radiobtn)).isChecked();
                    }
                })
                .setNegativeButton(R.string.dialog_abort, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i){
                        visualizer_visualizationcolor.value = prevVisualizerColor;
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener(){
                    @Override
                    public void onCancel(DialogInterface dialogInterface){
                        visualizer_visualizationcolor.value = prevVisualizerColor;
                    }
                })
                .setView(R.layout.layout_dialog_changevisualizersettings)
                .show();

        ((RadioButton) dialog.findViewById(R.id.dialog_changeVisualizerSettings_proximityselected_radiobtn)).setChecked(visualizer_proximity_selected);
        ((RadioButton) dialog.findViewById(R.id.dialog_changeVisualizerSettings_linearselected_radiobtn)).setChecked(visualizer_linear_selected);

        final TextView visualizerColorTV = (TextView) dialog.findViewById(R.id.dialog_changeVisualizerSettings_visualizercolor_textview);
        setColorOfTextView(visualizerColorTV, visualizer_visualizationcolor.value);
        visualizerColorTV.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                editColor(visualizerColorTV, visualizer_visualizationcolor, R.string.changeColor_visualizercolor_title);
            }
        });

        ((SeekBar) dialog.findViewById(R.id.dialog_changeVisualizerSettings_visualizerxposition_seekbar)).setProgress((int) (visualizer_x_pos * 1000f));
        ((SeekBar) dialog.findViewById(R.id.dialog_changeVisualizerSettings_visualizeryposition_seekbar)).setProgress((int) (visualizer_y_pos * 1000f));
        ((SeekBar) dialog.findViewById(R.id.dialog_changeVisualizerSettings_visualizerdiameter_seekbar)).setProgress((int) (visualizer_diameter * 1000f));

        ((RadioButton) dialog.findViewById(R.id.dialog_changeVisualizerSettings_visualizertypewaveform_radiobtn)).setChecked(visualizer_visualizationtype_waveform);
        ((RadioButton) dialog.findViewById(R.id.dialog_changeVisualizerSettings_visualizertypefft_radiobtn)).setChecked(visualizer_visualizationtype_fft);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mWallpaperSelector.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mWallpaperSelector.onSaveInstanceState(outState);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mWallpaperSelector.onRestoreInstanceState(savedInstanceState);
    }
    @Override
    protected void onPause(){
        super.onPause();

        /**
         * SAVE ALL VALUES!!
         */
        getSharedPreferences(getString(R.string.settings_settingsAt) + settings_index, MODE_PRIVATE).edit()
                /*LINES*/
                .putBoolean(        getString(R.string.lines_unicolor_preferences),                         lines_unicolor_checkbox.isChecked())
                .putInt(            getString(R.string.lines_unicolor_color_preferences),                   lines_unicolor_color.value)
                .putBoolean(        getString(R.string.lines_rainbowcolor_preferences),                     lines_rainbowcolor_checkbox.isChecked())
                .putInt(            getString(R.string.lines_rainbowcolorsteps_preferences),                lines_rainbowcolor_seekbar.getProgress())
                .putFloat(          getString(R.string.lines_width_preferences),                            lines_linewidth_seekbar.getProgress() * 0.1f)
                .putBoolean(        getString(R.string.lines_permanent_preferences),                        lines_linesave_checkbox.isChecked())
                .putBoolean(        getString(R.string.lines_drawBall_preferences),                         lines_linedrawBall_checkbox.isChecked())
                .putFloat(          getString(R.string.lines_ballSize_preferences),                         lines_linesballsize_seekbar.getProgress() * 0.1f)
                .putBoolean(        getString(R.string.lines_fadeComplete_preferences),                     lines_linesfadecomplete_radiobutton.isChecked())
                .putInt(            getString(R.string.lines_fadeComplete_time_preferences),                lines_linesfadecompletetime_seekbar.getProgress() * 100)
                .putBoolean(        getString(R.string.lines_fadeSlowly_preferences),                       lines_linesfadeslowly_radiobutton.isChecked())
                .putBoolean(        getString(R.string.lines_fadeParasite_preferences),                     lines_linesfadeparasite_radiobutton.isChecked())
                .putInt(            getString(R.string.lines_eatingitselfdirection_preferences),            lines_linesfadeparasite_direction_seekbar.getProgress())
                .putInt(            getString(R.string.lines_fadeActionTime_preferences),                   lines_linesfadeslowlyActionTime_seekbar.getProgress())
                /*BACKGROUND*/
                .putBoolean(        getString(R.string.background_pictureshown_preferences),                background_backgroundpicture_checkbox.isChecked())
                .putInt(            getString(R.string.background_alternateColor_preferences),              background_backgroundcolor_color.value)
                /*CLOCK GENERAL*/
                .putBoolean(        getString(R.string.clock_drawClock_preferences),                        clock_clockenable_checkbox.isChecked())
                .putFloat(          getString(R.string.clock_diameter_preferences),                         clock_clockdiameter)
                .putFloat(          getString(R.string.clock_xposition_preferences),                        clock_clockxposition)
                .putFloat(          getString(R.string.clock_yposition_preferences),                        clock_clockyposition)
                /*SIMPLE CLOCK*/
                .putBoolean(        getString(R.string.clock_simpleClockchosen_preferences),                clock_clock_simpleclock_selected)
                .putInt(            getString(R.string.clock_simpleclock_alphaColor_preferences),           Color.argb(clock_clock_simpleclock_alphabehind, 0xFF, 0xFF, 0xFF))
                .putInt(            getString(R.string.clock_simpleclock_stdcolor_preferences),             clock_clock_simpleclock_color_stunde_color.value)
                .putInt(            getString(R.string.clock_simpleclock_mincolor_preferences),             clock_clock_simpleclock_color_minute_color.value)
                .putInt(            getString(R.string.clock_simpleclock_seccolor_preferences),             clock_clock_simpleclock_color_sekunde_color.value)
                /*POINTER-ONLY CLOCK*/
                .putBoolean(        getString(R.string.clock_pointerOnlyClockchosen_preferences),           clock_clock_pointeronly_selected)
                .putInt(            getString(R.string.clock_pointeronlyclock_stdcolor_preferences),        clock_clock_pointeronly_color_stunde_color.value)
                .putInt(            getString(R.string.clock_pointeronlyclock_mincolor_preferences),        clock_clock_pointeronly_color_minute_color.value)
                .putInt(            getString(R.string.clock_pointeronlyclock_seccolor_preferences),        clock_clock_pointeronly_color_sekunde_color.value)
                /*PARABOLACLOCK*/
                .putBoolean(        getString(R.string.clock_parabolaclockchosen_preferences),              clock_clock_parabolaclock_selected)
                .putInt(            getString(R.string.clock_parabolaclock_alphaColor_preferences),         Color.argb(clock_clock_parabolaclock_alphabehind, 0xFF, 0xFF, 0xFF))
                .putInt(            getString(R.string.clock_parabolaclock_stdmincolor_preferences),        clock_clock_parabolaclock_color_stundeminute_color.value)
                .putInt(            getString(R.string.clock_parabolaclock_minsekcolor_preferences),        clock_clock_parabolaclock_color_minutesekunde_color.value)
                /*DIGITALCLOCK*/
                .putBoolean(        getString(R.string.clock_digitalClockchosen_preferences),               clock_clock_digitalclock_selected)
                .putBoolean(        getString(R.string.clock_digitalClock_dotsblinking_preferences),        clock_clock_digitalclock_dotsblinking)
                /*AUDIO-VISUALIZER*/
                .putBoolean(        getString(R.string.visualizer_showVisualizer_preferences),              visualizer_visualizerenable_checkbox.isChecked())
                .putBoolean(        getString(R.string.visualzer_proximityvisualizerchosen_preferences),    visualizer_proximity_selected)
                .putBoolean(        getString(R.string.visualzer_linearvisualizerchosen_preferences),       visualizer_linear_selected)
                .putInt(            getString(R.string.visualizer_visualizationcolor_preferences),          visualizer_visualizationcolor.value)
                .putFloat(          getString(R.string.visualizer_xposition_preferences),                   visualizer_x_pos)
                .putFloat(          getString(R.string.visualizer_yposition_preferences),                   visualizer_y_pos)
                .putFloat(getString(R.string.visualizer_diameter_preferences), visualizer_diameter)
                .putBoolean(getString(R.string.visualizer_visualizationtype_fft_preferences), visualizer_visualizationtype_fft)
                .putBoolean(getString(R.string.visualizer_visualizationtype_waveform_preferences), visualizer_visualizationtype_waveform)

                .apply();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_changesettingsmenu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.action_selectsetting_menuitem){

            if(settings_index == getSharedPreferences(getString(R.string.settings_settingsPreferences), MODE_PRIVATE).getInt(getString(R.string.settings_selectedSettingPreferences), 0)){

                Snackbar.make(findViewById(R.id.space), R.string.changeSettings_changeindex_failed, Snackbar.LENGTH_SHORT).show();

            }else{
                getSharedPreferences(getString(R.string.settings_settingsPreferences), MODE_PRIVATE)
                        .edit()
                        .putInt(getString(R.string.settings_selectedSettingPreferences), settings_index)
                        .apply();

                Snackbar.make(findViewById(R.id.space), R.string.changeSettings_changeindex_succeeded, Snackbar.LENGTH_SHORT).show();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    /*
//     * Small annotation at this point, at which I'm writing all these methods to change the colors of the pointers of the clocks.
//     * I HATE Java for not having (data-) pointers.
//     * The method could have been so easy with pointers:
//
//    /**
//     * @param tv the Textview currently showing the color
//     * @param col the pointer to the color-attribute
//     * @param title the R.string-constant for the title
//     * /
//    private void editColor(final TextView tv, final int* col, int title){
//        final int prevCol = *col;
//
//        AlertDialog shown = new AlertDialog.Builder(this)
//            .setTitle(title)
//            .setView(R.layout.layout_dialog_changecolor)
//            .setPositiveButton(R.string.dialog_ok, null)
//            .setNegativeButton(R.string.dialog_abort, new DialogInterface.OnClickListener(){
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i){
//                    *col = prevCol;
//                    setColorOfTextView(tv, *col);
//                }
//            })
//            .show();
//
//        GradientView top = (GradientView) shown.findViewById(R.id.top);
//        GradientView bottom = (GradientView) shown.findViewById(R.id.bottom);
//
//        top.setBrightnessGradientView(bottom);
//
//        bottom.setOnColorChangedListener(new GradientView.OnColorChangedListener(){
//            @Override
//            public void onColorChanged(GradientView view, int color){
//                *col = color;
//                setColorOfTextView(tv, *col);
//            }
//        });
//
//        top.setColor(prevCol);
//    }
//
//     *
//     * THAT WOULD FUCKING WORK FOR ALL OF THEM!!!!
//     * But NOOOO... I'm Java and don't want any fucking pointers... EXCEPT WITH FUCKING OBJECTS!!!!
//     * Well fuck it... It's too late to begin over with a native Android App
//     *
//     * [EDIT:]
//     * Well I decided to make a class to simply "simulate" a pointer for a int-variable.
//     * Now I have to rewrite everything again...
//     */
    private static class IntegerPointer{
        public int value;
    }
}

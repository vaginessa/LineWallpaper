package de.jeanpierrehotz.drawyaownwallpapers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

public class ChangeSettingsActivity extends AppCompatActivity{

    /*
     * LINES-SETTINGS:
     */
    private CheckBox        lines_unicolor_checkbox;
    private TextView        lines_unicolor_colortextview;
    private int             lines_unicolor_color;

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
    private TextView        lines_linesfadeslowlyActionTime_caption_textview;
    private SeekBar         lines_linesfadeslowlyActionTime_seekbar;

    /*
     * BACKGROUND-SETTINGS:
     */
    private CheckBox        background_backgroundpicture_checkbox;
    private Button          background_backgroundpicture_button;
    private TextView        background_backgroundcolor_colortextview;
    private int             background_backgroundcolor_color;

    /*
     * CLOCK-SETTINGS:
     */
    private CheckBox        clock_clockenable_checkbox;
    private TextView        clock_clockxposition_caption_texview;
    private SeekBar         clock_clockxposition_seekbar;
    private TextView        clock_clockyposition_caption_textview;
    private SeekBar         clock_clockyposition_seekbar;
    private TextView        clock_clockdiameter_caption_textview;
    private SeekBar         clock_clockdiameter_seekbar;
    private RadioGroup      clock_clockchooser_radiogroup;

    /*
     * SIMPLE-CLOCK-SETTINGS:
     */
    private RadioButton     clock_clock_simpleclock_radiobtn;
    private TextView        clock_clock_simpleclock_alphabehind_caption_textview;
    private SeekBar         clock_clock_simpleclock_alphabehind_seekbar;
    private TextView        clock_clock_simpleclock_color_stunde_caption_textview;
    private TextView        clock_clock_simpleclock_color_stunde_textview;
    private int             clock_clock_simpleclock_color_stunde_color;
    private TextView        clock_clock_simpleclock_color_minute_caption_textview;
    private TextView        clock_clock_simpleclock_color_minute_textview;
    private int             clock_clock_simpleclock_color_minute_color;
    private TextView        clock_clock_simpleclock_color_sekunde_caption_textview;
    private TextView        clock_clock_simpleclock_color_sekunde_textview;
    private int             clock_clock_simpleclock_color_sekunde_color;

    /*
     * POINTER-ONLY CLOCK-SETTINGS:
     */
    private RadioButton     clock_clock_pointeronly_radiobtn;
    private TextView        clock_clock_pointeronly_color_stunde_caption_textview;
    private TextView        clock_clock_pointeronly_color_stunde_textview;
    private int             clock_clock_pointeronly_color_stunde_color;
    private TextView        clock_clock_pointeronly_color_minute_caption_textview;
    private TextView        clock_clock_pointeronly_color_minute_textview;
    private int             clock_clock_pointeronly_color_minute_color;
    private TextView        clock_clock_pointeronly_color_sekunde_caption_textview;
    private TextView        clock_clock_pointeronly_color_sekunde_textview;
    private int             clock_clock_pointeronly_color_sekunde_color;

    /*
     * PARABLE-CLOCK-SETTINGS:
     */
    private RadioButton     clock_clock_parabolaclock_radiobtn;
    private TextView        clock_clock_parabolaclock_alphabehind_caption_textview;
    private SeekBar         clock_clock_parabolaclock_alphabehind_seekbar;
    private TextView        clock_clock_parabolaclock_color_stundeminute_caption_textview;
    private TextView        clock_clock_parabolaclock_color_stundeminute_textview;
    private int             clock_clock_parabolaclock_color_stundeminute_color;
    private TextView        clock_clock_parabolaclock_color_minutesekunde_caption_textview;
    private TextView        clock_clock_parabolaclock_color_minutesekunde_textview;
    private int             clock_clock_parabolaclock_color_minutesekunde_color;

    /*
     * DIGITAL-CLOCK-SETTINGS:
     */
    private RadioButton     clock_clock_digitalclock_radiobtn;
    private CheckBox        clock_clock_digitalclock_dotsblinking_checkbox;

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

            lines_linesfadecomplete_caption_textview.setVisibility(completeVis);
            lines_linesfadecompletetime_seekbar.setVisibility(completeVis);
            lines_linesfadeslowlyActionTime_caption_textview.setVisibility(fadeVis);
            lines_linesfadeslowlyActionTime_seekbar.setVisibility(fadeVis);
        }
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
    private CheckBox.OnCheckedChangeListener        clock_clockenable_listener                              = new CompoundButton.OnCheckedChangeListener(){
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b){
            int vis = (clock_clockenable_checkbox.isChecked())? View.VISIBLE: View.GONE;

            clock_clockxposition_caption_texview.setVisibility(vis);
            clock_clockxposition_seekbar.setVisibility(vis);
            clock_clockyposition_caption_textview.setVisibility(vis);
            clock_clockyposition_seekbar.setVisibility(vis);
            clock_clockdiameter_caption_textview.setVisibility(vis);
            clock_clockdiameter_seekbar.setVisibility(vis);
            clock_clockchooser_radiogroup.setVisibility(vis);
        }
    };
    private RadioButton.OnCheckedChangeListener     clock_clockSelected_listener                            = new CompoundButton.OnCheckedChangeListener(){
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b){
            int simpleClockVis = (clock_clock_simpleclock_radiobtn.isChecked())? View.VISIBLE: View.GONE;
            int pointerOnlyVis = (clock_clock_pointeronly_radiobtn.isChecked())? View.VISIBLE: View.GONE;
            int parabolaVisibl = (clock_clock_parabolaclock_radiobtn.isChecked())? View.VISIBLE: View.GONE;
            int digitalCloVisi = (clock_clock_digitalclock_radiobtn.isChecked())? View.VISIBLE: View.GONE;

            clock_clock_simpleclock_alphabehind_caption_textview.setVisibility(simpleClockVis);
            clock_clock_simpleclock_alphabehind_seekbar.setVisibility(simpleClockVis);
            clock_clock_simpleclock_color_stunde_caption_textview.setVisibility(simpleClockVis);
            clock_clock_simpleclock_color_stunde_textview.setVisibility(simpleClockVis);
            clock_clock_simpleclock_color_minute_caption_textview.setVisibility(simpleClockVis);
            clock_clock_simpleclock_color_minute_textview.setVisibility(simpleClockVis);
            clock_clock_simpleclock_color_sekunde_caption_textview.setVisibility(simpleClockVis);
            clock_clock_simpleclock_color_sekunde_textview.setVisibility(simpleClockVis);

            clock_clock_pointeronly_color_stunde_caption_textview.setVisibility(pointerOnlyVis);
            clock_clock_pointeronly_color_stunde_textview.setVisibility(pointerOnlyVis);
            clock_clock_pointeronly_color_minute_caption_textview.setVisibility(pointerOnlyVis);
            clock_clock_pointeronly_color_minute_textview.setVisibility(pointerOnlyVis);
            clock_clock_pointeronly_color_sekunde_caption_textview.setVisibility(pointerOnlyVis);
            clock_clock_pointeronly_color_sekunde_textview.setVisibility(pointerOnlyVis);

            clock_clock_parabolaclock_alphabehind_caption_textview.setVisibility(parabolaVisibl);
            clock_clock_parabolaclock_alphabehind_seekbar.setVisibility(parabolaVisibl);
            clock_clock_parabolaclock_color_stundeminute_caption_textview.setVisibility(parabolaVisibl);
            clock_clock_parabolaclock_color_stundeminute_textview.setVisibility(parabolaVisibl);
            clock_clock_parabolaclock_color_minutesekunde_caption_textview.setVisibility(parabolaVisibl);
            clock_clock_parabolaclock_color_minutesekunde_textview.setVisibility(parabolaVisibl);

            clock_clock_digitalclock_dotsblinking_checkbox.setVisibility(digitalCloVisi);
        }
    };
//    private SeekBar.OnSeekBarChangeListener         clock_clock_simpleclock_color_stunde_listener           = new SeekBar.OnSeekBarChangeListener(){
//        @Override
//        public void onProgressChanged(SeekBar seekBar, int i, boolean b){
//            clock_clock_simpleclock_color_stunde_textview.setBackgroundColor(Color.rgb(
//                    clock_clock_simpleclock_color_stunde_r_seekbar.getProgress(),
//                    clock_clock_simpleclock_color_stunde_g_seekbar.getProgress(),
//                    clock_clock_simpleclock_color_stunde_b_seekbar.getProgress()
//            ));
//        }
//        @Override
//        public void onStartTrackingTouch(SeekBar seekBar){}
//        @Override
//        public void onStopTrackingTouch(SeekBar seekBar){}
//    };
//    private SeekBar.OnSeekBarChangeListener         clock_clock_simpleclock_color_minute_listener           = new SeekBar.OnSeekBarChangeListener(){
//        @Override
//        public void onProgressChanged(SeekBar seekBar, int i, boolean b){
//            clock_clock_simpleclock_color_minute_textview.setBackgroundColor(Color.rgb(
//                    clock_clock_simpleclock_color_minute_r_seekbar.getProgress(),
//                    clock_clock_simpleclock_color_minute_g_seekbar.getProgress(),
//                    clock_clock_simpleclock_color_minute_b_seekbar.getProgress()
//            ));
//        }
//        @Override
//        public void onStartTrackingTouch(SeekBar seekBar){}
//        @Override
//        public void onStopTrackingTouch(SeekBar seekBar){}
//    };
//    private SeekBar.OnSeekBarChangeListener         clock_clock_simpleclock_color_sekunde_listener          = new SeekBar.OnSeekBarChangeListener(){
//        @Override
//        public void onProgressChanged(SeekBar seekBar, int i, boolean b){
//            clock_clock_simpleclock_color_sekunde_textview.setBackgroundColor(Color.rgb(
//                    clock_clock_simpleclock_color_sekunde_r_seekbar.getProgress(),
//                    clock_clock_simpleclock_color_sekunde_g_seekbar.getProgress(),
//                    clock_clock_simpleclock_color_sekunde_b_seekbar.getProgress()
//            ));
//        }
//        @Override
//        public void onStartTrackingTouch(SeekBar seekBar){}
//        @Override
//        public void onStopTrackingTouch(SeekBar seekBar){}
//    };
//    private SeekBar.OnSeekBarChangeListener         clock_clock_pointeronly_color_stunde_listener           = new SeekBar.OnSeekBarChangeListener(){
//        @Override
//        public void onProgressChanged(SeekBar seekBar, int i, boolean b){
//            clock_clock_pointeronly_color_stunde_textview.setBackgroundColor(Color.rgb(
//                    clock_clock_pointeronly_color_stunde_r_seekbar.getProgress(),
//                    clock_clock_pointeronly_color_stunde_g_seekbar.getProgress(),
//                    clock_clock_pointeronly_color_stunde_b_seekbar.getProgress()
//            ));
//        }
//        @Override
//        public void onStartTrackingTouch(SeekBar seekBar){}
//        @Override
//        public void onStopTrackingTouch(SeekBar seekBar){}
//    };
//    private SeekBar.OnSeekBarChangeListener         clock_clock_pointeronly_color_minute_listener           = new SeekBar.OnSeekBarChangeListener(){
//        @Override
//        public void onProgressChanged(SeekBar seekBar, int i, boolean b){
//            clock_clock_pointeronly_color_minute_textview.setBackgroundColor(Color.rgb(
//                    clock_clock_pointeronly_color_minute_r_seekbar.getProgress(),
//                    clock_clock_pointeronly_color_minute_g_seekbar.getProgress(),
//                    clock_clock_pointeronly_color_minute_b_seekbar.getProgress()
//            ));
//        }
//        @Override
//        public void onStartTrackingTouch(SeekBar seekBar){}
//        @Override
//        public void onStopTrackingTouch(SeekBar seekBar){}
//    };
//    private SeekBar.OnSeekBarChangeListener         clock_clock_pointeronly_color_sekunde_listener          = new SeekBar.OnSeekBarChangeListener(){
//        @Override
//        public void onProgressChanged(SeekBar seekBar, int i, boolean b){
//            clock_clock_pointeronly_color_sekunde_textview.setBackgroundColor(Color.rgb(
//                    clock_clock_pointeronly_color_sekunde_r_seekbar.getProgress(),
//                    clock_clock_pointeronly_color_sekunde_g_seekbar.getProgress(),
//                    clock_clock_pointeronly_color_sekunde_b_seekbar.getProgress()
//            ));
//        }
//        @Override
//        public void onStartTrackingTouch(SeekBar seekBar){}
//        @Override
//        public void onStopTrackingTouch(SeekBar seekBar){}
//    };
//    private SeekBar.OnSeekBarChangeListener         clock_clock_parabolaclock_color_stundeminute_listener   = new SeekBar.OnSeekBarChangeListener(){
//        @Override
//        public void onProgressChanged(SeekBar seekBar, int i, boolean b){
//            clock_clock_parabolaclock_color_stundeminute_textview.setBackgroundColor(Color.rgb(
//                    clock_clock_parabolaclock_color_stundeminute_r_seekbar.getProgress(),
//                    clock_clock_parabolaclock_color_stundeminute_g_seekbar.getProgress(),
//                    clock_clock_parabolaclock_color_stundeminute_b_seekbar.getProgress()
//            ));
//        }
//        @Override
//        public void onStartTrackingTouch(SeekBar seekBar){}
//        @Override
//        public void onStopTrackingTouch(SeekBar seekBar){}
//    };
//    private SeekBar.OnSeekBarChangeListener         clock_clock_parabolaclock_color_minutesekunde_listener  = new SeekBar.OnSeekBarChangeListener(){
//        @Override
//        public void onProgressChanged(SeekBar seekBar, int i, boolean b){
//            clock_clock_parabolaclock_color_minutesekunde_textview.setBackgroundColor(Color.rgb(
//                    clock_clock_parabolaclock_color_minutesekunde_r_seekbar.getProgress(),
//                    clock_clock_parabolaclock_color_minutesekunde_g_seekbar.getProgress(),
//                    clock_clock_parabolaclock_color_minutesekunde_b_seekbar.getProgress()
//            ));
//        }
//        @Override
//        public void onStartTrackingTouch(SeekBar seekBar){}
//        @Override
//        public void onStopTrackingTouch(SeekBar seekBar){}
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_change_settings);
        setSupportActionBar(toolbar);
        settings_index = getIntent().getIntExtra(getString(R.string.intent_settings_index), 0);

        /*
         * First initialize layout, so it will work
         */
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
        lines_linesfadeslowlyActionTime_caption_textview                = (TextView)        findViewById(R.id.linesfadeslowlyActionTime_caption_textview);
        lines_linesfadeslowlyActionTime_seekbar                         = (SeekBar)         findViewById(R.id.linesfadeslowlyActionTime_seekbar);
        lines_linesfadecomplete_radiobutton.setOnCheckedChangeListener(lines_linesfade_listener);
        lines_linesfadeslowly_radiobutton.setOnCheckedChangeListener(lines_linesfade_listener);
        lines_linesfadeparasite_radiobutton.setOnCheckedChangeListener(lines_linesfade_listener);

        background_backgroundpicture_checkbox                           = (CheckBox)        findViewById(R.id.backgroundpicture_checkbox);
        background_backgroundpicture_button                             = (Button)          findViewById(R.id.backgroundpicture_button);
        background_backgroundcolor_colortextview                        = (TextView)        findViewById(R.id.backgroundcolor_colortextview);
        background_backgroundpicture_checkbox.setOnCheckedChangeListener(background_backgroundpicture_checkbox_listener);

        clock_clockenable_checkbox                                      = (CheckBox)        findViewById(R.id.clockenable_checkbox);
        clock_clockxposition_caption_texview                            = (TextView)        findViewById(R.id.clockxposition_caption_texview);
        clock_clockxposition_seekbar                                    = (SeekBar)         findViewById(R.id.clockxposition_seekbar);
        clock_clockyposition_caption_textview                           = (TextView)        findViewById(R.id.clockyposition_caption_textview);
        clock_clockyposition_seekbar                                    = (SeekBar)         findViewById(R.id.clockyposition_seekbar);
        clock_clockdiameter_caption_textview                            = (TextView)        findViewById(R.id.clockdiameter_caption_textview);
        clock_clockdiameter_seekbar                                     = (SeekBar)         findViewById(R.id.clockdiameter_seekbar);
        clock_clockchooser_radiogroup                                   = (RadioGroup)      findViewById(R.id.clockchooser_radiogroup);
        clock_clockenable_checkbox.setOnCheckedChangeListener(clock_clockenable_listener);

        clock_clock_simpleclock_radiobtn                                = (RadioButton)     findViewById(R.id.clock_simpleclock_radiobtn);
        clock_clock_simpleclock_alphabehind_caption_textview            = (TextView)        findViewById(R.id.clock_simpleclock_alphabehind_caption_textview);
        clock_clock_simpleclock_alphabehind_seekbar                     = (SeekBar)         findViewById(R.id.clock_simpleclock_alphabehind_seekbar);
        clock_clock_simpleclock_color_stunde_caption_textview           = (TextView)        findViewById(R.id.clock_simpleclock_color_stunde_caption_textview);
        clock_clock_simpleclock_color_stunde_textview                   = (TextView)        findViewById(R.id.clock_simpleclock_color_stunde_textview);
        clock_clock_simpleclock_color_minute_caption_textview           = (TextView)        findViewById(R.id.clock_simpleclock_color_minute_caption_textview);
        clock_clock_simpleclock_color_minute_textview                   = (TextView)        findViewById(R.id.clock_simpleclock_color_minute_textview);
        clock_clock_simpleclock_color_sekunde_caption_textview          = (TextView)        findViewById(R.id.clock_simpleclock_color_sekunde_caption_textview);
        clock_clock_simpleclock_color_sekunde_textview                  = (TextView)        findViewById(R.id.clock_simpleclock_color_sekunde_textview);
        clock_clock_simpleclock_radiobtn.setOnCheckedChangeListener(clock_clockSelected_listener);

        clock_clock_pointeronly_radiobtn                                = (RadioButton)     findViewById(R.id.clock_pointeronly_radiobtn);
        clock_clock_pointeronly_color_stunde_caption_textview           = (TextView)        findViewById(R.id.clock_pointeronly_color_stunde_caption_textview);
        clock_clock_pointeronly_color_stunde_textview                   = (TextView)        findViewById(R.id.clock_pointeronly_color_stunde_textview);
        clock_clock_pointeronly_color_minute_caption_textview           = (TextView)        findViewById(R.id.clock_pointeronly_color_minute_caption_textview);
        clock_clock_pointeronly_color_minute_textview                   = (TextView)        findViewById(R.id.clock_pointeronly_color_minute_textview);
        clock_clock_pointeronly_color_sekunde_caption_textview          = (TextView)        findViewById(R.id.clock_pointeronly_color_sekunde_caption_textview);
        clock_clock_pointeronly_color_sekunde_textview                  = (TextView)        findViewById(R.id.clock_pointeronly_color_sekunde_textview);
        clock_clock_pointeronly_radiobtn.setOnCheckedChangeListener(clock_clockSelected_listener);

        clock_clock_parabolaclock_radiobtn                              = (RadioButton)     findViewById(R.id.clock_parabolaclock_radiobtn);
        clock_clock_parabolaclock_alphabehind_caption_textview          = (TextView)        findViewById(R.id.clock_parabolaclock_alphabehind_caption_textview);
        clock_clock_parabolaclock_alphabehind_seekbar                   = (SeekBar)         findViewById(R.id.clock_parabolaclock_alphabehind_seekbar);
        clock_clock_parabolaclock_color_stundeminute_caption_textview   = (TextView)        findViewById(R.id.clock_parabolaclock_color_stundeminute_caption_textview);
        clock_clock_parabolaclock_color_stundeminute_textview           = (TextView)        findViewById(R.id.clock_parabolaclock_color_stundeminute_textview);
        clock_clock_parabolaclock_color_minutesekunde_caption_textview  = (TextView)        findViewById(R.id.clock_parabolaclock_color_minutesekunde_caption_textview);
        clock_clock_parabolaclock_color_minutesekunde_textview          = (TextView)        findViewById(R.id.clock_parabolaclock_color_minutesekunde_textview);
        clock_clock_parabolaclock_radiobtn.setOnCheckedChangeListener(clock_clockSelected_listener);

        clock_clock_digitalclock_radiobtn                               = (RadioButton)     findViewById(R.id.clock_digitalclock_radiobtn);
        clock_clock_digitalclock_dotsblinking_checkbox                  = (CheckBox)        findViewById(R.id.clock_digitalclock_dotsblinking_checkbox);
        clock_clock_digitalclock_radiobtn.setOnCheckedChangeListener(clock_clockSelected_listener);

        /*
         * Make the drop-down layout responsive to set-Calls
         *  -> make it show nothing and if it's set to true it will show its stuff
         */
        lines_unicolor_checkbox.setChecked(true);
        lines_rainbowcolor_checkbox.setChecked(true);
        lines_linesave_checkbox.setChecked(true);
        lines_linedrawBall_checkbox.setChecked(true);
        lines_linesfadecomplete_radiobutton.setChecked(true);
        background_backgroundpicture_checkbox.setChecked(true);
        clock_clockenable_checkbox.setChecked(true);
        clock_clock_simpleclock_radiobtn.setChecked(true);

        /*
        Then load and show settings, so the layout will (hopefully) handle itself
         */
        if(getSharedPreferences(getString(R.string.settings_settingsAt) + settings_index, MODE_PRIVATE).getBoolean("firstTime", true)){
            getSharedPreferences(getString(R.string.settings_settingsAt) + settings_index, MODE_PRIVATE).edit().putBoolean("firstTime", false).apply();

            lines_unicolor_checkbox.setChecked(false);
            lines_unicolor_color = Color.rgb(255, 0, 0);

            lines_rainbowcolor_checkbox.setChecked(true);
            lines_rainbowcolor_seekbar.setProgress(10);

            lines_linewidth_seekbar.setProgress(120);
            lines_linesave_checkbox.setChecked(false);
            lines_linedrawBall_checkbox.setChecked(false);
            lines_linesballsize_seekbar.setProgress(240);
            lines_linesfadeslowly_radiobutton.setChecked(true);
            lines_linesfadecompletetime_seekbar.setProgress(20);
            lines_linesfadeslowlyActionTime_seekbar.setProgress(50);

            background_backgroundpicture_checkbox.setChecked(false);
            background_backgroundcolor_color = Color.rgb(243, 168, 168);

            clock_clockenable_checkbox.setChecked(false);
            clock_clockxposition_seekbar.setProgress(500);
            clock_clockyposition_seekbar.setProgress(500);
            clock_clockdiameter_seekbar.setProgress(800);

            clock_clock_simpleclock_radiobtn.setChecked(true);
            clock_clock_simpleclock_alphabehind_seekbar.setProgress(192);
            clock_clock_simpleclock_color_stunde_color = Color.rgb(255, 0, 0);
            clock_clock_simpleclock_color_minute_color = Color.rgb(0, 255, 0);
            clock_clock_simpleclock_color_sekunde_color = Color.rgb(0, 0, 255);

            clock_clock_pointeronly_color_stunde_color = Color.rgb(255, 0, 0);
            clock_clock_pointeronly_color_minute_color = Color.rgb(0, 255, 0);
            clock_clock_pointeronly_color_sekunde_color = Color.rgb(0, 0, 255);

            clock_clock_parabolaclock_alphabehind_seekbar.setProgress(192);
            clock_clock_parabolaclock_color_stundeminute_color = Color.rgb(255, 127, 0);
            clock_clock_parabolaclock_color_minutesekunde_color = Color.rgb(0, 127, 255);

        }else{
            SharedPreferences prefs = getSharedPreferences(getString(R.string.settings_settingsAt) + settings_index, MODE_PRIVATE);

            lines_unicolor_checkbox.setChecked(prefs.getBoolean(getString(R.string.lines_unicolor_preferences), false));
            lines_unicolor_color = prefs.getInt(getString(R.string.lines_unicolor_color_preferences), 0xFF0000);

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
            lines_linesfadeslowlyActionTime_seekbar.setProgress(prefs.getInt(getString(R.string.lines_fadeActionTime_preferences), 50));

            background_backgroundpicture_checkbox.setChecked(prefs.getBoolean(getString(R.string.background_pictureshown_preferences), false));
            background_backgroundcolor_color = prefs.getInt(getString(R.string.background_alternateColor_preferences), 0xF3A8A8);

//            Display display = getWindowManager().getDefaultDisplay();
//            Point size = new Point();
//            display.getSize(size);
//            int width = size.x;
//            int height = size.y;
//
//            float diam = prefs.getFloat(getString(R.string.clock_diameter_preferences), ((width > height)? height: width) * 0.800f);
//
//            clock_clockdiameter_seekbar.setProgress((int) (diam * 1000 / ((width > height)? height: width)));
//            clock_clockxposition_seekbar.setProgress((int) (prefs.getFloat(getString(R.string.clock_xposition_preferences), (width - diam) * 0.5f) * 1000 / (width - diam)));
//            clock_clockyposition_seekbar.setProgress((int) (prefs.getFloat(getString(R.string.clock_yposition_preferences), (height - diam) * 0.5f) * 1000 / (height - diam)));

            clock_clockenable_checkbox.setChecked(prefs.getBoolean(getString(R.string.clock_drawClock_preferences), false));

            clock_clockdiameter_seekbar.setProgress((int) (prefs.getFloat(getString(R.string.clock_diameter_preferences), 0.8f) * 1000));
            clock_clockxposition_seekbar.setProgress((int) (prefs.getFloat(getString(R.string.clock_xposition_preferences), 0.5f) * 1000));
            clock_clockyposition_seekbar.setProgress((int) (prefs.getFloat(getString(R.string.clock_yposition_preferences), 0.5f) * 1000));

            clock_clock_simpleclock_radiobtn.setChecked(prefs.getBoolean(getString(R.string.clock_simpleClockchosen_preferences), true));
            clock_clock_simpleclock_alphabehind_seekbar.setProgress(Color.alpha(prefs.getInt(getString(R.string.clock_simpleclock_alphaColor_preferences), 0xC0000000)));
            clock_clock_simpleclock_color_stunde_color = prefs.getInt(getString(R.string.clock_simpleclock_stdcolor_preferences), 0xFF0000);
            clock_clock_simpleclock_color_minute_color = prefs.getInt(getString(R.string.clock_simpleclock_mincolor_preferences), 0x00FF00);
            clock_clock_simpleclock_color_sekunde_color = prefs.getInt(getString(R.string.clock_simpleclock_seccolor_preferences), 0x0000FF);

            clock_clock_pointeronly_radiobtn.setChecked(prefs.getBoolean(getString(R.string.clock_pointerOnlyClockchosen_preferences), false));
            clock_clock_pointeronly_color_stunde_color = prefs.getInt(getString(R.string.clock_pointeronlyclock_stdcolor_preferences), 0xFF0000);
            clock_clock_pointeronly_color_minute_color = prefs.getInt(getString(R.string.clock_pointeronlyclock_mincolor_preferences), 0x00FF00);
            clock_clock_pointeronly_color_sekunde_color = prefs.getInt(getString(R.string.clock_pointeronlyclock_seccolor_preferences), 0x0000FF);

            clock_clock_parabolaclock_radiobtn.setChecked(prefs.getBoolean(getString(R.string.clock_parabolaclockchosen_preferences), false));
            clock_clock_parabolaclock_alphabehind_seekbar.setProgress(Color.alpha(prefs.getInt(getString(R.string.clock_parabolaclock_alphaColor_preferences), 0xC0FFFFFF)));
            clock_clock_parabolaclock_color_stundeminute_color = prefs.getInt(getString(R.string.clock_parabolaclock_stdmincolor_preferences), 0xFF7F00);
            clock_clock_parabolaclock_color_minutesekunde_color = prefs.getInt(getString(R.string.clock_parabolaclock_minsekcolor_preferences), 0x007FFF);

            clock_clock_digitalclock_radiobtn.setChecked(prefs.getBoolean(getString(R.string.clock_digitalClockchosen_preferences), false));
            clock_clock_digitalclock_dotsblinking_checkbox.setChecked(prefs.getBoolean(getString(R.string.clock_digitalClock_dotsblinking_preferences), true));
        }

        lines_unicolor_colortextview.setBackgroundColor(lines_unicolor_color);
        lines_unicolor_colortextview.setTextColor(~lines_unicolor_color | FULL_ALPHA);

        background_backgroundcolor_colortextview.setBackgroundColor(background_backgroundcolor_color);
        background_backgroundcolor_colortextview.setTextColor(~background_backgroundcolor_color | FULL_ALPHA);

        clock_clock_simpleclock_color_stunde_textview.setBackgroundColor(clock_clock_simpleclock_color_stunde_color);
        clock_clock_simpleclock_color_stunde_textview.setTextColor(~clock_clock_simpleclock_color_stunde_color | FULL_ALPHA);

        clock_clock_simpleclock_color_minute_textview.setBackgroundColor(clock_clock_simpleclock_color_minute_color);
        clock_clock_simpleclock_color_minute_textview.setTextColor(~clock_clock_simpleclock_color_minute_color | FULL_ALPHA);

        clock_clock_simpleclock_color_sekunde_textview.setBackgroundColor(clock_clock_simpleclock_color_sekunde_color);
        clock_clock_simpleclock_color_sekunde_textview.setTextColor(~clock_clock_simpleclock_color_sekunde_color | FULL_ALPHA);

        clock_clock_pointeronly_color_stunde_textview.setBackgroundColor(clock_clock_pointeronly_color_stunde_color);
        clock_clock_pointeronly_color_stunde_textview.setTextColor(~clock_clock_pointeronly_color_stunde_color | FULL_ALPHA);

        clock_clock_pointeronly_color_minute_textview.setBackgroundColor(clock_clock_pointeronly_color_minute_color);
        clock_clock_pointeronly_color_minute_textview.setTextColor(~clock_clock_pointeronly_color_minute_color | FULL_ALPHA);

        clock_clock_pointeronly_color_sekunde_textview.setBackgroundColor(clock_clock_pointeronly_color_sekunde_color);
        clock_clock_pointeronly_color_sekunde_textview.setTextColor(~clock_clock_pointeronly_color_sekunde_color | FULL_ALPHA);

        clock_clock_parabolaclock_color_stundeminute_textview.setBackgroundColor(clock_clock_parabolaclock_color_stundeminute_color);
        clock_clock_parabolaclock_color_stundeminute_textview.setTextColor(~clock_clock_parabolaclock_color_stundeminute_color | FULL_ALPHA);

        clock_clock_parabolaclock_color_minutesekunde_textview.setBackgroundColor(clock_clock_parabolaclock_color_minutesekunde_color);
        clock_clock_parabolaclock_color_minutesekunde_textview.setTextColor(~clock_clock_parabolaclock_color_minutesekunde_color | FULL_ALPHA);

    }//end of onCreate()

    private static final int FULL_ALPHA = 255 << 24;

    private int settings_index;

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
//                            sb.setAction("OK", v -> sb.dismiss());
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
//                            sb.setAction("OK", v -> sb.dismiss());
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

    private static  final int REQUEST_CHOOSE_IMAGE = 12345;

    public void selectBackgroundPicture(View v){
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, REQUEST_CHOOSE_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHOOSE_IMAGE) {
            if (resultCode == RESULT_OK) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                getSharedPreferences(getString(R.string.settings_settingsAt) + settings_index, MODE_PRIVATE).edit().putString(getString(R.string.background_picturepath_preferences), picturePath).apply();
            }
        }
    }

    @Override
    protected void onPause(){
        super.onPause();

        /**
         * SAVE ALL VALUES!!
         */
//        Display display = getWindowManager().getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//        int width = size.x;
//        int height = size.y;
//
//        float clockDiam = ((width > height)? height: width) * clock_clockdiameter_seekbar.getProgress() * 0.001f;
//        float clockX = (width - clockDiam) * clock_clockxposition_seekbar.getProgress() * 0.001f;
//        float clockY = (height - clockDiam) * clock_clockyposition_seekbar.getProgress() * 0.001f;

        getSharedPreferences(getString(R.string.settings_settingsAt) + settings_index, MODE_PRIVATE).edit()
                .putBoolean(getString(R.string.lines_unicolor_preferences), lines_unicolor_checkbox.isChecked())
                .putInt(getString(R.string.lines_unicolor_color_preferences), lines_unicolor_color)

                .putBoolean(getString(R.string.lines_rainbowcolor_preferences), lines_rainbowcolor_checkbox.isChecked())
                .putInt(getString(R.string.lines_rainbowcolorsteps_preferences), lines_rainbowcolor_seekbar.getProgress())

                .putFloat(getString(R.string.lines_width_preferences), lines_linewidth_seekbar.getProgress() * 0.1f)
                .putBoolean(getString(R.string.lines_permanent_preferences), lines_linesave_checkbox.isChecked())
                .putBoolean(getString(R.string.lines_drawBall_preferences), lines_linedrawBall_checkbox.isChecked())
                .putFloat(getString(R.string.lines_ballSize_preferences), lines_linesballsize_seekbar.getProgress() * 0.1f)
                .putBoolean(getString(R.string.lines_fadeComplete_preferences), lines_linesfadecomplete_radiobutton.isChecked())
                .putInt(getString(R.string.lines_fadeComplete_time_preferences), lines_linesfadecompletetime_seekbar.getProgress() * 100)
                .putBoolean(getString(R.string.lines_fadeSlowly_preferences), lines_linesfadeslowly_radiobutton.isChecked())
                .putBoolean(getString(R.string.lines_fadeParasite_preferences), lines_linesfadeparasite_radiobutton.isChecked())
                .putInt(getString(R.string.lines_fadeActionTime_preferences), lines_linesfadeslowlyActionTime_seekbar.getProgress())
                .putBoolean(getString(R.string.background_pictureshown_preferences), background_backgroundpicture_checkbox.isChecked())
                .putInt(getString(R.string.background_alternateColor_preferences), background_backgroundcolor_color)
                .putBoolean(getString(R.string.clock_drawClock_preferences), clock_clockenable_checkbox.isChecked())
                .putFloat(getString(R.string.clock_diameter_preferences), (float) clock_clockdiameter_seekbar.getProgress() / 1000f)
                .putFloat(getString(R.string.clock_xposition_preferences), (float) clock_clockxposition_seekbar.getProgress() / 1000f)
                .putFloat(getString(R.string.clock_yposition_preferences), (float) clock_clockyposition_seekbar.getProgress() / 1000f)

                .putBoolean(getString(R.string.clock_simpleClockchosen_preferences), clock_clock_simpleclock_radiobtn.isChecked())
                .putInt(getString(R.string.clock_simpleclock_alphaColor_preferences), Color.argb(clock_clock_simpleclock_alphabehind_seekbar.getProgress(), 0xFF, 0xFF, 0xFF))
                .putInt(getString(R.string.clock_simpleclock_stdcolor_preferences), clock_clock_simpleclock_color_stunde_color)
                .putInt(getString(R.string.clock_simpleclock_mincolor_preferences), clock_clock_simpleclock_color_minute_color)
                .putInt(getString(R.string.clock_simpleclock_seccolor_preferences), clock_clock_simpleclock_color_sekunde_color)

                .putBoolean(getString(R.string.clock_pointerOnlyClockchosen_preferences), clock_clock_pointeronly_radiobtn.isChecked())
                .putInt(getString(R.string.clock_pointeronlyclock_stdcolor_preferences), clock_clock_pointeronly_color_stunde_color)
                .putInt(getString(R.string.clock_pointeronlyclock_mincolor_preferences), clock_clock_pointeronly_color_minute_color)
                .putInt(getString(R.string.clock_pointeronlyclock_seccolor_preferences), clock_clock_pointeronly_color_sekunde_color)

                .putBoolean(getString(R.string.clock_parabolaclockchosen_preferences), clock_clock_parabolaclock_radiobtn.isChecked())
                .putInt(getString(R.string.clock_parabolaclock_alphaColor_preferences), Color.argb(clock_clock_parabolaclock_alphabehind_seekbar.getProgress(), 255, 255, 255))
                .putInt(getString(R.string.clock_parabolaclock_stdmincolor_preferences), clock_clock_parabolaclock_color_stundeminute_color)
                .putInt(getString(R.string.clock_parabolaclock_minsekcolor_preferences), clock_clock_parabolaclock_color_minutesekunde_color)

                .putBoolean(getString(R.string.clock_digitalClockchosen_preferences), clock_clock_digitalclock_radiobtn.isChecked())
                .putBoolean(getString(R.string.clock_digitalClock_dotsblinking_preferences), clock_clock_digitalclock_dotsblinking_checkbox.isChecked())

                .apply();
    }

    private void setColorOfTextView(TextView tv, int col){
        tv.setBackgroundColor(col);
        tv.setTextColor(~col | FULL_ALPHA);
    }

    public void changeUniColorColor(View view){
        final int prevCol = lines_unicolor_color;

        AlertDialog shown = new AlertDialog.Builder(ChangeSettingsActivity.this)
                .setTitle(R.string.changeColor_unicolor_title)
                .setView(R.layout.layout_changecolor_setting)
                .setPositiveButton(R.string.dialog_ok, null)
                .setNegativeButton(R.string.dialog_abort, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i){
                        lines_unicolor_color = prevCol;
                        setColorOfTextView(lines_unicolor_colortextview, lines_unicolor_color);
                    }
                })
                .show();

        GradientView top = (GradientView) shown.findViewById(R.id.top);
        GradientView bottom = (GradientView) shown.findViewById(R.id.bottom);

        top.setBrightnessGradientView(bottom);

        bottom.setOnColorChangedListener(new GradientView.OnColorChangedListener(){
            @Override
            public void onColorChanged(GradientView view, int color){
                lines_unicolor_color = color;
                setColorOfTextView(lines_unicolor_colortextview, lines_unicolor_color);
            }
        });

        top.setColor(prevCol);
    }

    public void changeBackgroundColorColor(View view){
        final int prevCol = background_backgroundcolor_color;

        AlertDialog shown = new AlertDialog.Builder(ChangeSettingsActivity.this)
                .setTitle(R.string.changeColor_backgroundcolor_title)
                .setView(R.layout.layout_changecolor_setting)
                .setPositiveButton(R.string.dialog_ok, null)
                .setNegativeButton(R.string.dialog_abort, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i){
                        background_backgroundcolor_color = prevCol;
                        setColorOfTextView(background_backgroundcolor_colortextview, background_backgroundcolor_color);
                    }
                })
                .show();

        GradientView top = (GradientView) shown.findViewById(R.id.top);
        GradientView bottom = (GradientView) shown.findViewById(R.id.bottom);

        top.setBrightnessGradientView(bottom);

        bottom.setOnColorChangedListener(new GradientView.OnColorChangedListener(){
            @Override
            public void onColorChanged(GradientView view, int color){
                background_backgroundcolor_color = color;
                setColorOfTextView(background_backgroundcolor_colortextview, background_backgroundcolor_color);
            }
        });

        top.setColor(prevCol);
    }

    public void changeSimpleClockHourPointerColor(View view){
        final int prevCol = clock_clock_simpleclock_color_stunde_color;

        AlertDialog shown = new AlertDialog.Builder(ChangeSettingsActivity.this)
                .setTitle(R.string.changeColor_simplclockhourpointercolor_title)
                .setView(R.layout.layout_changecolor_setting)
                .setPositiveButton(R.string.dialog_ok, null)
                .setNegativeButton(R.string.dialog_abort, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i){
                        clock_clock_simpleclock_color_stunde_color = prevCol;
                        setColorOfTextView(clock_clock_simpleclock_color_stunde_textview, clock_clock_simpleclock_color_stunde_color);
                    }
                })
                .show();

        GradientView top = (GradientView) shown.findViewById(R.id.top);
        GradientView bottom = (GradientView) shown.findViewById(R.id.bottom);

        top.setBrightnessGradientView(bottom);

        bottom.setOnColorChangedListener(new GradientView.OnColorChangedListener(){
            @Override
            public void onColorChanged(GradientView view, int color){
                clock_clock_simpleclock_color_stunde_color = color;
                setColorOfTextView(clock_clock_simpleclock_color_stunde_textview, clock_clock_simpleclock_color_stunde_color);
            }
        });

        top.setColor(prevCol);
    }

    public void changeSimpleClockMinutePointerColor(View view){
        final int prevCol = clock_clock_simpleclock_color_minute_color;

        AlertDialog shown = new AlertDialog.Builder(ChangeSettingsActivity.this)
                .setTitle(R.string.changeColor_simplclockminutepointercolor_title)
                .setView(R.layout.layout_changecolor_setting)
                .setPositiveButton(R.string.dialog_ok, null)
                .setNegativeButton(R.string.dialog_abort, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i){
                        clock_clock_simpleclock_color_minute_color = prevCol;
                        setColorOfTextView(clock_clock_simpleclock_color_minute_textview, clock_clock_simpleclock_color_minute_color);
                    }
                })
                .show();

        GradientView top = (GradientView) shown.findViewById(R.id.top);
        GradientView bottom = (GradientView) shown.findViewById(R.id.bottom);

        top.setBrightnessGradientView(bottom);

        bottom.setOnColorChangedListener(new GradientView.OnColorChangedListener(){
            @Override
            public void onColorChanged(GradientView view, int color){
                clock_clock_simpleclock_color_minute_color = color;
                setColorOfTextView(clock_clock_simpleclock_color_minute_textview, clock_clock_simpleclock_color_minute_color);
            }
        });

        top.setColor(prevCol);
    }

    public void changeSimpleClockSecondPointerColor(View view){
        final int prevCol = clock_clock_simpleclock_color_sekunde_color;

        AlertDialog shown = new AlertDialog.Builder(ChangeSettingsActivity.this)
                .setTitle(R.string.changeColor_simplclocksekundepointercolor_title)
                .setView(R.layout.layout_changecolor_setting)
                .setPositiveButton(R.string.dialog_ok, null)
                .setNegativeButton(R.string.dialog_abort, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i){
                        clock_clock_simpleclock_color_sekunde_color = prevCol;
                        setColorOfTextView(clock_clock_simpleclock_color_sekunde_textview, clock_clock_simpleclock_color_sekunde_color);
                    }
                })
                .show();

        GradientView top = (GradientView) shown.findViewById(R.id.top);
        GradientView bottom = (GradientView) shown.findViewById(R.id.bottom);

        top.setBrightnessGradientView(bottom);

        bottom.setOnColorChangedListener(new GradientView.OnColorChangedListener(){
            @Override
            public void onColorChanged(GradientView view, int color){
                clock_clock_simpleclock_color_sekunde_color = color;
                setColorOfTextView(clock_clock_simpleclock_color_sekunde_textview, clock_clock_simpleclock_color_sekunde_color);
            }
        });

        top.setColor(prevCol);
    }

    public void changePointerOnlyClockHourPointerColor(View view){
        final int prevCol = clock_clock_pointeronly_color_stunde_color;

        AlertDialog shown = new AlertDialog.Builder(ChangeSettingsActivity.this)
                .setTitle(R.string.changeColor_pointeronlyhourpointercolor_title)
                .setView(R.layout.layout_changecolor_setting)
                .setPositiveButton(R.string.dialog_ok, null)
                .setNegativeButton(R.string.dialog_abort, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i){
                        clock_clock_pointeronly_color_stunde_color = prevCol;
                        setColorOfTextView(clock_clock_pointeronly_color_stunde_textview, clock_clock_pointeronly_color_stunde_color);
                    }
                })
                .show();

        GradientView top = (GradientView) shown.findViewById(R.id.top);
        GradientView bottom = (GradientView) shown.findViewById(R.id.bottom);

        top.setBrightnessGradientView(bottom);

        bottom.setOnColorChangedListener(new GradientView.OnColorChangedListener(){
            @Override
            public void onColorChanged(GradientView view, int color){
                clock_clock_pointeronly_color_stunde_color = color;
                setColorOfTextView(clock_clock_pointeronly_color_stunde_textview, clock_clock_pointeronly_color_stunde_color);
            }
        });

        top.setColor(prevCol);
    }

    public void changePointerOnlyClockMinutePointerColor(View view){
        final int prevCol = clock_clock_pointeronly_color_minute_color;

        AlertDialog shown = new AlertDialog.Builder(ChangeSettingsActivity.this)
                .setTitle(R.string.changeColor_pointeronlyminutepointercolor_title)
                .setView(R.layout.layout_changecolor_setting)
                .setPositiveButton(R.string.dialog_ok, null)
                .setNegativeButton(R.string.dialog_abort, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i){
                        clock_clock_pointeronly_color_minute_color = prevCol;
                        setColorOfTextView(clock_clock_pointeronly_color_minute_textview, clock_clock_pointeronly_color_minute_color);
                    }
                })
                .show();

        GradientView top = (GradientView) shown.findViewById(R.id.top);
        GradientView bottom = (GradientView) shown.findViewById(R.id.bottom);

        top.setBrightnessGradientView(bottom);

        bottom.setOnColorChangedListener(new GradientView.OnColorChangedListener(){
            @Override
            public void onColorChanged(GradientView view, int color){
                clock_clock_pointeronly_color_minute_color = color;
                setColorOfTextView(clock_clock_pointeronly_color_minute_textview, clock_clock_pointeronly_color_minute_color);
            }
        });

        top.setColor(prevCol);
    }

    public void changePointerOnlyClockSecondPointerColor(View view){
        final int prevCol = clock_clock_pointeronly_color_sekunde_color;

        AlertDialog shown = new AlertDialog.Builder(ChangeSettingsActivity.this)
                .setTitle(R.string.changeColor_pointeronlysecondpointercolor_title)
                .setView(R.layout.layout_changecolor_setting)
                .setPositiveButton(R.string.dialog_ok, null)
                .setNegativeButton(R.string.dialog_abort, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i){
                        clock_clock_pointeronly_color_sekunde_color = prevCol;
                        setColorOfTextView(clock_clock_pointeronly_color_sekunde_textview, clock_clock_pointeronly_color_sekunde_color);
                    }
                })
                .show();

        GradientView top = (GradientView) shown.findViewById(R.id.top);
        GradientView bottom = (GradientView) shown.findViewById(R.id.bottom);

        top.setBrightnessGradientView(bottom);

        bottom.setOnColorChangedListener(new GradientView.OnColorChangedListener(){
            @Override
            public void onColorChanged(GradientView view, int color){
                clock_clock_pointeronly_color_sekunde_color = color;
                setColorOfTextView(clock_clock_pointeronly_color_sekunde_textview, clock_clock_pointeronly_color_sekunde_color);
            }
        });

        top.setColor(prevCol);
    }

    public void changeParabolaClockHourMinutePointerColor(View view){
        final int prevCol = clock_clock_parabolaclock_color_stundeminute_color;

        AlertDialog shown = new AlertDialog.Builder(ChangeSettingsActivity.this)
                .setTitle(R.string.changeColor_parabolastundeminutepointercolor_title)
                .setView(R.layout.layout_changecolor_setting)
                .setPositiveButton(R.string.dialog_ok, null)
                .setNegativeButton(R.string.dialog_abort, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i){
                        clock_clock_parabolaclock_color_stundeminute_color = prevCol;
                        setColorOfTextView(clock_clock_parabolaclock_color_stundeminute_textview, clock_clock_parabolaclock_color_stundeminute_color);
                    }
                })
                .show();

        GradientView top = (GradientView) shown.findViewById(R.id.top);
        GradientView bottom = (GradientView) shown.findViewById(R.id.bottom);

        top.setBrightnessGradientView(bottom);

        bottom.setOnColorChangedListener(new GradientView.OnColorChangedListener(){
            @Override
            public void onColorChanged(GradientView view, int color){
                clock_clock_parabolaclock_color_stundeminute_color = color;
                setColorOfTextView(clock_clock_parabolaclock_color_stundeminute_textview, clock_clock_parabolaclock_color_stundeminute_color);
            }
        });

        top.setColor(prevCol);
    }

    public void changeParabolaClockMinuteSecondPointerColor(View view){
        final int prevCol = clock_clock_parabolaclock_color_minutesekunde_color;

        AlertDialog shown = new AlertDialog.Builder(ChangeSettingsActivity.this)
                .setTitle(R.string.changeColor_parabolaminutesekundepointercolor_title)
                .setView(R.layout.layout_changecolor_setting)
                .setPositiveButton(R.string.dialog_ok, null)
                .setNegativeButton(R.string.dialog_abort, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i){
                        clock_clock_parabolaclock_color_minutesekunde_color = prevCol;
                        setColorOfTextView(clock_clock_parabolaclock_color_minutesekunde_textview, clock_clock_parabolaclock_color_minutesekunde_color);
                    }
                })
                .show();

        GradientView top = (GradientView) shown.findViewById(R.id.top);
        GradientView bottom = (GradientView) shown.findViewById(R.id.bottom);

        top.setBrightnessGradientView(bottom);

        bottom.setOnColorChangedListener(new GradientView.OnColorChangedListener(){
            @Override
            public void onColorChanged(GradientView view, int color){
                clock_clock_parabolaclock_color_minutesekunde_color = color;
                setColorOfTextView(clock_clock_parabolaclock_color_minutesekunde_textview, clock_clock_parabolaclock_color_minutesekunde_color);
            }
        });

        top.setColor(prevCol);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu){
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item){
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if(id == R.id.action_settings){
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

}

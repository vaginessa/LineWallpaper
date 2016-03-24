package de.jeanpierrehotz.drawyaownwallpapers;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

public class MainActivity extends AppCompatActivity{

//    ImageView clockDropDownImage;

//    boolean dropped = false;

//    ImageView theImageV;
//    private static  final int REQUEST_CHOOSE_IMAGE = 12345;

//    private ArrayList<String>   captions;
//    private ListView            settingsListView;

    /*
     * LINES-SETTINGS:
     */
    private CheckBox        lines_unicolor_checkbox;
    private TextView        lines_unicolor_colortextview;
    private SeekBar         lines_unicolor_r_seekbar;
    private SeekBar         lines_unicolor_g_seekbar;
    private SeekBar         lines_unicolor_b_seekbar;
    private SeekBar         lines_linewidth_seekbar;
    private CheckBox        lines_linesave_checkbox;
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
    private SeekBar         background_backgroundcolor_r_Seekbar;
    private SeekBar         background_backgroundcolor_g_Seekbar;
    private SeekBar         background_backgroundcolor_b_Seekbar;

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
    private SeekBar         clock_clock_simpleclock_color_stunde_r_seekbar;
    private SeekBar         clock_clock_simpleclock_color_stunde_g_seekbar;
    private SeekBar         clock_clock_simpleclock_color_stunde_b_seekbar;
    private TextView        clock_clock_simpleclock_color_minute_caption_textview;
    private TextView        clock_clock_simpleclock_color_minute_textview;
    private SeekBar         clock_clock_simpleclock_color_minute_r_seekbar;
    private SeekBar         clock_clock_simpleclock_color_minute_g_seekbar;
    private SeekBar         clock_clock_simpleclock_color_minute_b_seekbar;
    private TextView        clock_clock_simpleclock_color_sekunde_caption_textview;
    private TextView        clock_clock_simpleclock_color_sekunde_textview;
    private SeekBar         clock_clock_simpleclock_color_sekunde_r_seekbar;
    private SeekBar         clock_clock_simpleclock_color_sekunde_g_seekbar;
    private SeekBar         clock_clock_simpleclock_color_sekunde_b_seekbar;

    /*
     * LISTENER
     */
    private CheckBox.OnCheckedChangeListener        lines_unicolor_checkbox_listener                    = new CompoundButton.OnCheckedChangeListener(){
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b){
            int vis = (lines_unicolor_checkbox.isChecked())? View.VISIBLE: View.GONE;

            lines_unicolor_colortextview.setVisibility(vis);
            lines_unicolor_r_seekbar.setVisibility(vis);
            lines_unicolor_g_seekbar.setVisibility(vis);
            lines_unicolor_b_seekbar.setVisibility(vis);
        }
        private void foo(){}
    };
    private SeekBar.OnSeekBarChangeListener         lines_unicolor_listener                             = new SeekBar.OnSeekBarChangeListener(){
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b){
            lines_unicolor_colortextview.setBackgroundColor(Color.rgb(
                    lines_unicolor_r_seekbar.getProgress(),
                    lines_unicolor_g_seekbar.getProgress(),
                    lines_unicolor_b_seekbar.getProgress()
            ));
        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar){}
        @Override
        public void onStopTrackingTouch(SeekBar seekBar){}
    };
    private CheckBox.OnCheckedChangeListener        lines_linedrawBall_checkbox_listener                = new CompoundButton.OnCheckedChangeListener(){
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b){
            int vis = (lines_linedrawBall_checkbox.isChecked())? View.VISIBLE: View.GONE;

            lines_linesballsize_caption.setVisibility(vis);
            lines_linesballsize_seekbar.setVisibility(vis);
        }
        private void foo(){}
    };
    private RadioButton.OnCheckedChangeListener     lines_linesfade_listener                            = new CompoundButton.OnCheckedChangeListener(){
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b){
            int completeVis = (lines_linesfadecomplete_radiobutton.isChecked())? View.VISIBLE: View.GONE;
            int fadeVis = (lines_linesfadecomplete_radiobutton.isChecked())? View.GONE: View.VISIBLE;

            lines_linesfadecomplete_caption_textview.setVisibility(completeVis);
            lines_linesfadecompletetime_seekbar.setVisibility(completeVis);
            lines_linesfadeslowlyActionTime_caption_textview.setVisibility(fadeVis);
            lines_linesfadeslowlyActionTime_seekbar.setVisibility(fadeVis);
        }
        private void foo(){}
    };
    private CheckBox.OnCheckedChangeListener        background_backgroundpicture_checkbox_listener      = new CompoundButton.OnCheckedChangeListener(){
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b){
            int pictureVis = (background_backgroundpicture_checkbox.isChecked())? View.VISIBLE: View.GONE;
            int colorVis = (background_backgroundpicture_checkbox.isChecked())? View.GONE: View.VISIBLE;

            background_backgroundpicture_button.setVisibility(pictureVis);
            background_backgroundcolor_colortextview.setVisibility(colorVis);
            background_backgroundcolor_r_Seekbar.setVisibility(colorVis);
            background_backgroundcolor_g_Seekbar.setVisibility(colorVis);
            background_backgroundcolor_b_Seekbar.setVisibility(colorVis);
        }
        private void foo(){}
    };
    private SeekBar.OnSeekBarChangeListener         background_backgroundcolor_listener                 = new SeekBar.OnSeekBarChangeListener(){
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b){
            background_backgroundcolor_colortextview.setBackgroundColor(Color.rgb(
                    background_backgroundcolor_r_Seekbar.getProgress(),
                    background_backgroundcolor_g_Seekbar.getProgress(),
                    background_backgroundcolor_b_Seekbar.getProgress()
            ));
        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar){}
        @Override
        public void onStopTrackingTouch(SeekBar seekBar){}
    };
    private CheckBox.OnCheckedChangeListener        clock_clockenable_listener                          = new CompoundButton.OnCheckedChangeListener(){
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
        private void foo(){}
    };
    private RadioButton.OnCheckedChangeListener     clock_clockSelected_listener                        = new CompoundButton.OnCheckedChangeListener(){
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b){
            int simpleClockVis = (clock_clock_simpleclock_radiobtn.isChecked())? View.VISIBLE: View.GONE;

            clock_clock_simpleclock_alphabehind_caption_textview.setVisibility(simpleClockVis);
            clock_clock_simpleclock_alphabehind_seekbar.setVisibility(simpleClockVis);
            clock_clock_simpleclock_color_stunde_caption_textview.setVisibility(simpleClockVis);
            clock_clock_simpleclock_color_stunde_textview.setVisibility(simpleClockVis);
            clock_clock_simpleclock_color_stunde_r_seekbar.setVisibility(simpleClockVis);
            clock_clock_simpleclock_color_stunde_g_seekbar.setVisibility(simpleClockVis);
            clock_clock_simpleclock_color_stunde_b_seekbar.setVisibility(simpleClockVis);
            clock_clock_simpleclock_color_minute_caption_textview.setVisibility(simpleClockVis);
            clock_clock_simpleclock_color_minute_textview.setVisibility(simpleClockVis);
            clock_clock_simpleclock_color_minute_r_seekbar.setVisibility(simpleClockVis);
            clock_clock_simpleclock_color_minute_g_seekbar.setVisibility(simpleClockVis);
            clock_clock_simpleclock_color_minute_b_seekbar.setVisibility(simpleClockVis);
            clock_clock_simpleclock_color_sekunde_caption_textview.setVisibility(simpleClockVis);
            clock_clock_simpleclock_color_sekunde_textview.setVisibility(simpleClockVis);
            clock_clock_simpleclock_color_sekunde_r_seekbar.setVisibility(simpleClockVis);
            clock_clock_simpleclock_color_sekunde_g_seekbar.setVisibility(simpleClockVis);
            clock_clock_simpleclock_color_sekunde_b_seekbar.setVisibility(simpleClockVis);
        }
        private void foo(){}
    };
    private SeekBar.OnSeekBarChangeListener         clock_clock_simpleclock_color_stunde_listener       = new SeekBar.OnSeekBarChangeListener(){
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b){
            clock_clock_simpleclock_color_stunde_textview.setBackgroundColor(Color.rgb(
                    clock_clock_simpleclock_color_stunde_r_seekbar.getProgress(),
                    clock_clock_simpleclock_color_stunde_g_seekbar.getProgress(),
                    clock_clock_simpleclock_color_stunde_b_seekbar.getProgress()
            ));
        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar){}
        @Override
        public void onStopTrackingTouch(SeekBar seekBar){}
    };
    private SeekBar.OnSeekBarChangeListener         clock_clock_simpleclock_color_minute_listener       = new SeekBar.OnSeekBarChangeListener(){
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b){
            clock_clock_simpleclock_color_minute_textview.setBackgroundColor(Color.rgb(
                    clock_clock_simpleclock_color_minute_r_seekbar.getProgress(),
                    clock_clock_simpleclock_color_minute_g_seekbar.getProgress(),
                    clock_clock_simpleclock_color_minute_b_seekbar.getProgress()
            ));
        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar){}
        @Override
        public void onStopTrackingTouch(SeekBar seekBar){}
    };
    private SeekBar.OnSeekBarChangeListener         clock_clock_simpleclock_color_sekunde_listener      = new SeekBar.OnSeekBarChangeListener(){
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b){
            clock_clock_simpleclock_color_sekunde_textview.setBackgroundColor(Color.rgb(
                    clock_clock_simpleclock_color_sekunde_r_seekbar.getProgress(),
                    clock_clock_simpleclock_color_sekunde_g_seekbar.getProgress(),
                    clock_clock_simpleclock_color_sekunde_b_seekbar.getProgress()
            ));
        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar){}
        @Override
        public void onStopTrackingTouch(SeekBar seekBar){}
    };


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*
         * First initialize layout, so it will work
         */
        lines_unicolor_checkbox                                 = (CheckBox)        findViewById(R.id.unicolor_checkbox);
        lines_unicolor_colortextview                            = (TextView)        findViewById(R.id.unicolor_colortextview);
        lines_unicolor_r_seekbar                                = (SeekBar)         findViewById(R.id.unicolor_r_Seekbar);
        lines_unicolor_g_seekbar                                = (SeekBar)         findViewById(R.id.unicolor_g_Seekbar);
        lines_unicolor_b_seekbar                                = (SeekBar)         findViewById(R.id.unicolor_b_Seekbar);
        lines_unicolor_checkbox.setOnCheckedChangeListener(lines_unicolor_checkbox_listener);
        lines_unicolor_r_seekbar.setOnSeekBarChangeListener(lines_unicolor_listener);
        lines_unicolor_g_seekbar.setOnSeekBarChangeListener(lines_unicolor_listener);
        lines_unicolor_b_seekbar.setOnSeekBarChangeListener(lines_unicolor_listener);

        lines_linewidth_seekbar                                 = (SeekBar)         findViewById(R.id.linewidth_seekbar);
        lines_linesave_checkbox                                 = (CheckBox)        findViewById(R.id.linesave_checkbox);

        lines_linedrawBall_checkbox                             = (CheckBox)        findViewById(R.id.linedrawBall_checkbox);
        lines_linesballsize_caption                             = (TextView)        findViewById(R.id.linesballsize_caption);
        lines_linesballsize_seekbar                             = (SeekBar)         findViewById(R.id.linesballsize_seekbar);
        lines_linedrawBall_checkbox.setOnCheckedChangeListener(lines_linedrawBall_checkbox_listener);

        lines_linesfadecomplete_radiobutton                     = (RadioButton)     findViewById(R.id.linesfadecomplete_radiobutton);
        lines_linesfadecomplete_caption_textview                = (TextView)        findViewById(R.id.linesfadecomplete_caption_textview);
        lines_linesfadecompletetime_seekbar                     = (SeekBar)         findViewById(R.id.linesfadecompletetime_seekbar);
        lines_linesfadeslowly_radiobutton                       = (RadioButton)     findViewById(R.id.linesfadeslowly_radiobutton);
        lines_linesfadeparasite_radiobutton                     = (RadioButton)     findViewById(R.id.linesfadeparasite_radiobutton);
        lines_linesfadeslowlyActionTime_caption_textview        = (TextView)        findViewById(R.id.linesfadeslowlyActionTime_caption_textview);
        lines_linesfadeslowlyActionTime_seekbar                 = (SeekBar)         findViewById(R.id.linesfadeslowlyActionTime_seekbar);
        lines_linesfadecomplete_radiobutton.setOnCheckedChangeListener(lines_linesfade_listener);
        lines_linesfadeslowly_radiobutton.setOnCheckedChangeListener(lines_linesfade_listener);
        lines_linesfadeparasite_radiobutton.setOnCheckedChangeListener(lines_linesfade_listener);

        background_backgroundpicture_checkbox                   = (CheckBox)        findViewById(R.id.backgroundpicture_checkbox);
        background_backgroundpicture_button                     = (Button)          findViewById(R.id.backgroundpicture_button);
        background_backgroundcolor_colortextview                = (TextView)        findViewById(R.id.backgroundcolor_colortextview);
        background_backgroundcolor_r_Seekbar                    = (SeekBar)         findViewById(R.id.backgroundcolor_r_Seekbar);
        background_backgroundcolor_g_Seekbar                    = (SeekBar)         findViewById(R.id.backgroundcolor_g_Seekbar);
        background_backgroundcolor_b_Seekbar                    = (SeekBar)         findViewById(R.id.backgroundcolor_b_Seekbar);
        background_backgroundpicture_checkbox.setOnCheckedChangeListener(background_backgroundpicture_checkbox_listener);
        background_backgroundcolor_r_Seekbar.setOnSeekBarChangeListener(background_backgroundcolor_listener);
        background_backgroundcolor_g_Seekbar.setOnSeekBarChangeListener(background_backgroundcolor_listener);
        background_backgroundcolor_b_Seekbar.setOnSeekBarChangeListener(background_backgroundcolor_listener);

        clock_clockenable_checkbox                              = (CheckBox)        findViewById(R.id.clockenable_checkbox);
        clock_clockxposition_caption_texview                    = (TextView)        findViewById(R.id.clockxposition_caption_texview);
        clock_clockxposition_seekbar                            = (SeekBar)         findViewById(R.id.clockxposition_seekbar);
        clock_clockyposition_caption_textview                   = (TextView)        findViewById(R.id.clockyposition_caption_textview);
        clock_clockyposition_seekbar                            = (SeekBar)         findViewById(R.id.clockyposition_seekbar);
        clock_clockdiameter_caption_textview                    = (TextView)        findViewById(R.id.clockdiameter_caption_textview);
        clock_clockdiameter_seekbar                             = (SeekBar)         findViewById(R.id.clockdiameter_seekbar);
        clock_clockchooser_radiogroup                           = (RadioGroup)      findViewById(R.id.clockchooser_radiogroup);
        clock_clockenable_checkbox.setOnCheckedChangeListener(clock_clockenable_listener);

        clock_clock_simpleclock_radiobtn                        = (RadioButton)     findViewById(R.id.clock_simpleclock_radiobtn);
        clock_clock_simpleclock_alphabehind_caption_textview    = (TextView)        findViewById(R.id.clock_simpleclock_alphabehind_caption_textview);
        clock_clock_simpleclock_alphabehind_seekbar             = (SeekBar)         findViewById(R.id.clock_simpleclock_alphabehind_seekbar);
        clock_clock_simpleclock_color_stunde_caption_textview   = (TextView)        findViewById(R.id.clock_simpleclock_color_stunde_caption_textview);
        clock_clock_simpleclock_color_stunde_textview           = (TextView)        findViewById(R.id.clock_simpleclock_color_stunde_textview);
        clock_clock_simpleclock_color_stunde_r_seekbar          = (SeekBar)         findViewById(R.id.clock_simpleclock_color_stunde_r_seekbar);
        clock_clock_simpleclock_color_stunde_g_seekbar          = (SeekBar)         findViewById(R.id.clock_simpleclock_color_stunde_g_seekbar);
        clock_clock_simpleclock_color_stunde_b_seekbar          = (SeekBar)         findViewById(R.id.clock_simpleclock_color_stunde_b_seekbar);
        clock_clock_simpleclock_color_minute_caption_textview   = (TextView)        findViewById(R.id.clock_simpleclock_color_minute_caption_textview);
        clock_clock_simpleclock_color_minute_textview           = (TextView)        findViewById(R.id.clock_simpleclock_color_minute_textview);
        clock_clock_simpleclock_color_minute_r_seekbar          = (SeekBar)         findViewById(R.id.clock_simpleclock_color_minute_r_seekbar);
        clock_clock_simpleclock_color_minute_g_seekbar          = (SeekBar)         findViewById(R.id.clock_simpleclock_color_minute_g_seekbar);
        clock_clock_simpleclock_color_minute_b_seekbar          = (SeekBar)         findViewById(R.id.clock_simpleclock_color_minute_b_seekbar);
        clock_clock_simpleclock_color_sekunde_caption_textview  = (TextView)        findViewById(R.id.clock_simpleclock_color_sekunde_caption_textview);
        clock_clock_simpleclock_color_sekunde_textview          = (TextView)        findViewById(R.id.clock_simpleclock_color_sekunde_textview);
        clock_clock_simpleclock_color_sekunde_r_seekbar         = (SeekBar)         findViewById(R.id.clock_simpleclock_color_sekunde_r_seekbar);
        clock_clock_simpleclock_color_sekunde_g_seekbar         = (SeekBar)         findViewById(R.id.clock_simpleclock_color_sekunde_g_seekbar);
        clock_clock_simpleclock_color_sekunde_b_seekbar         = (SeekBar)         findViewById(R.id.clock_simpleclock_color_sekunde_b_seekbar);
        clock_clock_simpleclock_color_stunde_r_seekbar.setOnSeekBarChangeListener(clock_clock_simpleclock_color_stunde_listener);
        clock_clock_simpleclock_color_stunde_g_seekbar.setOnSeekBarChangeListener(clock_clock_simpleclock_color_stunde_listener);
        clock_clock_simpleclock_color_stunde_b_seekbar.setOnSeekBarChangeListener(clock_clock_simpleclock_color_stunde_listener);
        clock_clock_simpleclock_color_minute_r_seekbar.setOnSeekBarChangeListener(clock_clock_simpleclock_color_minute_listener);
        clock_clock_simpleclock_color_minute_g_seekbar.setOnSeekBarChangeListener(clock_clock_simpleclock_color_minute_listener);
        clock_clock_simpleclock_color_minute_b_seekbar.setOnSeekBarChangeListener(clock_clock_simpleclock_color_minute_listener);
        clock_clock_simpleclock_color_sekunde_r_seekbar.setOnSeekBarChangeListener(clock_clock_simpleclock_color_sekunde_listener);
        clock_clock_simpleclock_color_sekunde_g_seekbar.setOnSeekBarChangeListener(clock_clock_simpleclock_color_sekunde_listener);
        clock_clock_simpleclock_color_sekunde_b_seekbar.setOnSeekBarChangeListener(clock_clock_simpleclock_color_sekunde_listener);
        clock_clock_simpleclock_radiobtn.setOnCheckedChangeListener(clock_clockSelected_listener);



        /*
         * Make the drop-down layout responsive to set-Calls
         *  -> make it show nothing and if it's set to true it will show its stuff
         */
        lines_unicolor_checkbox.setChecked(true);
        lines_unicolor_checkbox.setChecked(false);
        lines_linedrawBall_checkbox.setChecked(true);
        lines_linedrawBall_checkbox.setChecked(false);
        lines_linesfadecomplete_radiobutton.setChecked(true);
        background_backgroundpicture_checkbox.setChecked(true);
        clock_clockenable_checkbox.setChecked(true);
        clock_clockenable_checkbox.setChecked(false);
        clock_clock_simpleclock_radiobtn.setChecked(true);

        /*
        Then load and show settings, so the layout will (hopefully) handle itself
         */


        /*
         * Calculate a product of prime numbers out of a given number n
         * Then apply this algorithm to the clocktime where n is built by hours * 100 + minutes
         */

        /**
         * FUCK DIFFERENT SETTINGS! ONLY SUPPORTING ONE FROM NOW ON!
         */
//        if(getSharedPreferences(getString(R.string.indexPreferences), MODE_PRIVATE).getBoolean(getString(R.string.firstTimePreferences), true)){
//            changeSettings(0);
//        }else{
//            captions = new ArrayList<>();
//
//            int length = getSharedPreferences(getString(R.string.indexPreferences), MODE_PRIVATE).getInt(getString(R.string.lengthOfSettingsPreferences), 0);
//
//            for(int i = 0; i < length; i++){
//
//            }
//        }

//        theImageV = (ImageView) findViewById(R.id.imageView);

//        clockDropDownImage = (ImageView) findViewById(R.id.clockDropDownImageID);

/*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

//                Intent i = new Intent(
//                        Intent.ACTION_PICK,
//                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//
//                startActivityForResult(i, REQUEST_CHOOSE_IMAGE);
            }
        });
*/
    }//end of onCreate()

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
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                getSharedPreferences(getString(R.string.settingsAt) + 0, MODE_PRIVATE).edit().putString(getString(R.string.backgroundPreferences), picturePath).apply();
            }
        }
    }

    @Override
    protected void onPause(){
        super.onPause();

        /**
         * SAVE ALL VALUES!!
         */
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

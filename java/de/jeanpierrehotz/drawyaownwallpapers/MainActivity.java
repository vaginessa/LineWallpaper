package de.jeanpierrehotz.drawyaownwallpapers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{

    ArrayList<String> settings_caption;
    ArrayList<Integer> settings_indexes;
    boolean deleted;
    int selectedSetting;
    ListView exampleListView;
    SettingsAdapter ad;

    AdapterView.OnItemClickListener changeSettingListener = new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){
            modifySetting(i);
        }
    };
    AdapterView.OnItemLongClickListener createContextMenuListener = new AdapterView.OnItemLongClickListener(){
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l){
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle(settings_caption.get(i))
                    .setItems(new String[]{"Auswählen", "Umbenennen", "Löschen"}, new DialogInterface.OnClickListener(){
                        AlertDialog dialog;

                        @Override
                        public void onClick(DialogInterface dialogInterface, int selectedMenuItem){
                            switch(selectedMenuItem){
                                case 0:
                                    selectedSetting = i;
                                    ad.selectedChangedTo(selectedSetting);
                                    break;
                                case 1:
                                    dialog = new AlertDialog.Builder(MainActivity.this)
                                            .setTitle("Titel")
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener(){
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int hay){
                                                    EditText newNameET = (EditText) dialog.findViewById(R.id.neuerNameEditText);
                                                    settings_caption.set(i, newNameET.getText().toString());
                                                    ad.notifyDataSetChanged();
                                                }
                                            })
                                            .setNegativeButton("Abbrechen", null)
                                            .setView(R.layout.layout_neuertitel_setting)
                                            .show();

                                    EditText newNameET = (EditText) dialog.findViewById(R.id.neuerNameEditText);
                                    newNameET.setHint("Neuer Titel");
                                    newNameET.setText(settings_caption.get(i));

                                    break;
                                case 2:
                                    if(i == selectedSetting){
                                        dialog = new AlertDialog.Builder(MainActivity.this)
                                                .setTitle("Ausgewählte Einstellung")
                                                .setMessage("Die ausgewählte Einstellung kann leider nicht gelöscht werden.\nDas tut uns wirklich leid...\n:(\nAber stell dir mal vor du wärst an meiner Stelle, und wüsstest plötzlich nicht was du tun sollst :P")
                                                .setPositiveButton("Ok", null)
                                                .setNegativeButton("Fuck dat", new DialogInterface.OnClickListener(){
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i){
                                                        Snackbar.make(exampleListView, "Well fuck you too!", Snackbar.LENGTH_INDEFINITE).show();
                                                    }
                                                })
                                                .show();
                                    }else{
                                        settings_caption.remove(i);
                                        settings_indexes.remove(i);
                                        deleted = true;

                                        if(i < selectedSetting){
                                            ad.selectedChangedTo(--selectedSetting);
                                        }else{
                                            ad.notifyDataSetChanged();
                                        }
                                    }
                                    break;
                                default:
                                    return;
                            }
                        }
                    })
                    .show();

            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        settings_caption = new ArrayList<>();
        settings_indexes = new ArrayList<>();
        deleted = false;

        if(getSharedPreferences(getString(R.string.settingsPreferences), MODE_PRIVATE).getBoolean("first", true)){
            getSharedPreferences(getString(R.string.settingsPreferences), MODE_PRIVATE).edit().putBoolean("first", false).apply();

            settings_caption.add("My first setting :)");
            selectedSetting = 0;
            saveSettings();
            modifySetting(0);
        }else{
            loadSettings();
        }

        exampleListView = (ListView) findViewById(R.id.settingsListView);
        ad = new SettingsAdapter(this, R.layout.listitem_setting, settings_caption, selectedSetting);

        exampleListView.setAdapter(ad);

        exampleListView.setOnItemClickListener(changeSettingListener);
        exampleListView.setOnItemLongClickListener(createContextMenuListener);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_main);
        fab.setOnClickListener(new View.OnClickListener(){
            AlertDialog dialog;
            @Override
            public void onClick(View view){
                settings_caption.add("");

                dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Titel")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int hay){
                                EditText newNameET = (EditText) dialog.findViewById(R.id.neuerNameEditText);
                                settings_caption.set(settings_caption.size() - 1, newNameET.getText().toString());
                                ad.notifyDataSetChanged();

                                exampleListView.smoothScrollToPosition(settings_caption.size() - 1);
                                modifySetting(settings_caption.size() - 1);
                            }
                        })
                        .setNegativeButton("Abbrechen", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i){
                                settings_caption.remove(settings_caption.size() - 1);
                            }
                        })
                        .setView(R.layout.layout_neuertitel_setting)
                        .show();

            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();

        settings_caption = new ArrayList<>();
        settings_indexes = new ArrayList<>();
        deleted = false;

        loadSettings();

        exampleListView = (ListView) findViewById(R.id.settingsListView);
        ad = new SettingsAdapter(this, R.layout.listitem_setting, settings_caption, selectedSetting);

        exampleListView.setAdapter(ad);

        exampleListView.setOnItemClickListener(changeSettingListener);
        exampleListView.setOnItemLongClickListener(createContextMenuListener);
    }

    private void modifySetting(int i){
        Intent intent = new Intent(this, ChangeSettingsActivity.class);
        intent.putExtra(getString(R.string.intent_settings_index), i);
        startActivity(intent);
    }

    private void loadSettings(){
        SharedPreferences settingsPrefs = getSharedPreferences(getString(R.string.settingsPreferences), MODE_PRIVATE);
        int length = settingsPrefs.getInt(getString(R.string.settingsPreferencesLength), 0);

        for(int i = 0; i < length; i++){
            settings_caption.add(settingsPrefs.getString(getString(R.string.settingAt_name) + i, ""));
            settings_indexes.add(i);
        }
        selectedSetting = settingsPrefs.getInt(getString(R.string.selectedSettingPreferences), 0);
    }

    private void saveSettings(){
        boolean firsttime = getSharedPreferences(getString(R.string.settingsPreferences), MODE_PRIVATE).getBoolean("first", true);
        SharedPreferences.Editor edit = getSharedPreferences(getString(R.string.settingsPreferences), MODE_PRIVATE).edit();
        edit.clear()
                .putInt(getString(R.string.settingsPreferencesLength), settings_caption.size())
                .putBoolean("first", firsttime);

        for(int i = 0; i < settings_caption.size(); i++){
            edit.putString(getString(R.string.settingAt_name) + i, settings_caption.get(i));
        }

        edit.putInt(getString(R.string.selectedSettingPreferences), selectedSetting);
        edit.apply();
    }

    public void saveSettingChanges(){
        for(int i = 0; i < settings_caption.size(); i++){
                /**
                 * LOAD THE SETTING AT THE PREVIOUS INDEX
                 */
                SharedPreferences currentSetting = getSharedPreferences(getString(R.string.settingsAt) + settings_indexes.get(i), MODE_PRIVATE);

                boolean     lines_unicolor                      = currentSetting.getBoolean(getString(R.string.lines_unicolor_preferences), false);
                boolean     lines_permanent                     = currentSetting.getBoolean(getString(R.string.lines_permanent_preferences), false);
                boolean     lines_drawBalls                     = currentSetting.getBoolean(getString(R.string.lines_drawBall_preferences), false);
                boolean     lines_removeCompletely              = currentSetting.getBoolean(getString(R.string.lines_fadeComplete_preferences), false);
                boolean     lines_fadeSlowly                    = currentSetting.getBoolean(getString(R.string.lines_fadeSlowly_preferences), true);
                boolean     lines_drawInRainBowColor            = currentSetting.getBoolean(getString(R.string.lines_rainbowcolor_preferences), true);
                float       lines_width                         = currentSetting.getFloat(getString(R.string.lines_width_preferences), 12f);
                float       lines_ballRadius                    = currentSetting.getFloat(getString(R.string.lines_ballSize_preferences), 24f);
                int         lines_color                         = currentSetting.getInt(getString(R.string.lines_unicolor_color_preferences), 0xFF0000);
                int         lines_timeUntilCompletelyRemoved    = currentSetting.getInt(getString(R.string.lines_fadeComplete_time_preferences), 5000);
                int         lines_timeUntilActionPerformed      = currentSetting.getInt(getString(R.string.lines_fadeActionTime_preferences), 50);
                int         lines_rainbowColorSteps             = currentSetting.getInt(getString(R.string.lines_rainbowcolorsteps_preferences), 10);

                boolean     background_drawPicture              = currentSetting.getBoolean(getString(R.string.background_pictureshown_preferences), false);
                String      background_picturePath              = currentSetting.getString(getString(R.string.background_picturepath_preferences), "");
                int         background_color                    = currentSetting.getInt(getString(R.string.background_alternateColor_preferences), 0xF3A8A8);

                boolean     clk_drawClock                       = currentSetting.getBoolean(getString(R.string.clock_drawClock_preferences), false);
                boolean     clk_simpleClockchosen               = currentSetting.getBoolean(getString(R.string.clock_simpleClockchosen_preferences), true);
                boolean     clk_pointerOnlychosen               = currentSetting.getBoolean(getString(R.string.clock_pointerOnlyClockchosen_preferences), true);
                boolean     clk_parabolaClockchosen             = currentSetting.getBoolean(getString(R.string.clock_parabolaclockchosen_preferences), true);
                boolean     clk_digitalClockchosen              = currentSetting.getBoolean(getString(R.string.clock_digitalClockchosen_preferences), true);
                boolean     clk_digitalClock_dotsblinking       = currentSetting.getBoolean(getString(R.string.clock_digitalClock_dotsblinking_preferences), true);
                float       clk_diameter                        = currentSetting.getFloat(getString(R.string.clock_diameter_preferences), 0.8f);
                float       clk_xPosition                       = currentSetting.getFloat(getString(R.string.clock_xposition_preferences), 05f);
                float       clk_yPosition                       = currentSetting.getFloat(getString(R.string.clock_yposition_preferences), 0.5f);
                int         clk_simpleClock_alpha               = currentSetting.getInt(getString(R.string.clock_simpleclock_alphaColor_preferences), 0xC0000000);
                int         clk_simpleClock_std                 = currentSetting.getInt(getString(R.string.clock_simpleclock_stdcolor_preferences), 0xFF0000);
                int         clk_simpleClock_min                 = currentSetting.getInt(getString(R.string.clock_simpleclock_mincolor_preferences), 0x00FF00);
                int         clk_simpleClock_sek                 = currentSetting.getInt(getString(R.string.clock_simpleclock_seccolor_preferences), 0x0000FF);
                int         clk_pointerOnly_std                 = currentSetting.getInt(getString(R.string.clock_pointeronlyclock_stdcolor_preferences), 0xFF0000);
                int         clk_pointerOnly_min                 = currentSetting.getInt(getString(R.string.clock_pointeronlyclock_mincolor_preferences), 0x00FF00);
                int         clk_pointerOnly_sek                 = currentSetting.getInt(getString(R.string.clock_pointeronlyclock_seccolor_preferences), 0x0000FF);
                int         clk_parabolaClock_alpha             = currentSetting.getInt(getString(R.string.clock_parabolaclock_alphaColor_preferences), 0xC0FFFFFF);
                int         clk_parabolaClock_std_min           = currentSetting.getInt(getString(R.string.clock_parabolaclock_stdmincolor_preferences), 0xFF7F00);
                int         clk_parabolaClock_min_sek           = currentSetting.getInt(getString(R.string.clock_parabolaclock_minsekcolor_preferences), 0x007FFF);

                ArrayList<Line> permLines                       = Line.loadFromSharedPreferences(getSharedPreferences(getString(R.string.permanentlines_lineSP) + settings_indexes.get(i), MODE_PRIVATE), MainActivity.this);

                /**
                 * DELETE THE PREFERENCES
                 */

                currentSetting.edit().clear().apply();
                getSharedPreferences(getString(R.string.permanentlines_lineSP) + settings_indexes.get(i), MODE_PRIVATE).edit().clear().commit();

                /**
                 * AND SAVE THE VALUES AT THE NEW INDEX:
                 */

                getSharedPreferences(getString(R.string.settingsAt) + i, MODE_PRIVATE).edit()

                        .putBoolean(getString(R.string.lines_unicolor_preferences), lines_unicolor)
                        .putBoolean(getString(R.string.lines_permanent_preferences), lines_permanent)
                        .putBoolean(    getString(R.string.lines_drawBall_preferences),                     lines_drawBalls)
                        .putBoolean(    getString(R.string.lines_fadeComplete_preferences),                 lines_removeCompletely)
                        .putBoolean(    getString(R.string.lines_fadeSlowly_preferences),                   lines_fadeSlowly)
                        .putBoolean(    getString(R.string.lines_rainbowcolor_preferences),                 lines_drawInRainBowColor)
                        .putFloat(getString(R.string.lines_width_preferences), lines_width)
                        .putFloat(      getString(R.string.lines_ballSize_preferences),                     lines_ballRadius)
                        .putInt(getString(R.string.lines_unicolor_color_preferences), lines_color)
                        .putInt(        getString(R.string.lines_fadeComplete_time_preferences),            lines_timeUntilCompletelyRemoved)
                        .putInt(        getString(R.string.lines_fadeActionTime_preferences),               lines_timeUntilActionPerformed)
                        .putInt(        getString(R.string.lines_rainbowcolorsteps_preferences),            lines_rainbowColorSteps)

                        .putBoolean(getString(R.string.background_pictureshown_preferences), background_drawPicture)
                        .putString(getString(R.string.background_picturepath_preferences), background_picturePath)
                        .putInt(getString(R.string.background_alternateColor_preferences), background_color)

                        .putBoolean(getString(R.string.clock_drawClock_preferences), clk_drawClock)
                        .putBoolean(getString(R.string.clock_simpleClockchosen_preferences), clk_simpleClockchosen)
                        .putBoolean(    getString(R.string.clock_pointerOnlyClockchosen_preferences),       clk_pointerOnlychosen)
                        .putBoolean(    getString(R.string.clock_parabolaclockchosen_preferences),          clk_parabolaClockchosen)
                        .putBoolean(    getString(R.string.clock_digitalClockchosen_preferences),           clk_digitalClockchosen)
                        .putBoolean(    getString(R.string.clock_digitalClock_dotsblinking_preferences),    clk_digitalClock_dotsblinking)
                        .putFloat(getString(R.string.clock_diameter_preferences), clk_diameter)
                        .putFloat(      getString(R.string.clock_xposition_preferences),                    clk_xPosition)
                        .putFloat(      getString(R.string.clock_yposition_preferences),                    clk_yPosition)
                        .putInt(getString(R.string.clock_simpleclock_alphaColor_preferences), clk_simpleClock_alpha)
                        .putInt(        getString(R.string.clock_simpleclock_stdcolor_preferences),         clk_simpleClock_std)
                        .putInt(        getString(R.string.clock_simpleclock_mincolor_preferences),         clk_simpleClock_min)
                        .putInt(        getString(R.string.clock_simpleclock_seccolor_preferences),         clk_simpleClock_sek)
                        .putInt(        getString(R.string.clock_pointeronlyclock_stdcolor_preferences),    clk_pointerOnly_std)
                        .putInt(        getString(R.string.clock_pointeronlyclock_mincolor_preferences),    clk_pointerOnly_min)
                        .putInt(        getString(R.string.clock_pointeronlyclock_seccolor_preferences),    clk_pointerOnly_sek)
                        .putInt(        getString(R.string.clock_parabolaclock_alphaColor_preferences),     clk_parabolaClock_alpha)
                        .putInt(        getString(R.string.clock_parabolaclock_stdmincolor_preferences),    clk_parabolaClock_std_min)
                        .putInt(        getString(R.string.clock_parabolaclock_minsekcolor_preferences),    clk_parabolaClock_min_sek)

                        .putBoolean("firstTime", false)

                        .apply();

                Line.saveToSharedPreferences(permLines, getSharedPreferences(getString(R.string.permanentlines_lineSP) + i, MODE_PRIVATE), MainActivity.this);
        }

        /**
         * THEN DELETE ALL THE SETTINGS, THAT WERE DELETED
         *  -> In [0 .. caption.length[ the deleted ones are overwritten
         *  => Delete in [caption.length .. length[
         */
        int length = getSharedPreferences(getString(R.string.settingsPreferences), MODE_PRIVATE).getInt(getString(R.string.settingsPreferencesLength), 0);
        for(int i = settings_caption.size(); i < length; i++){
            getSharedPreferences(getString(R.string.settingsAt) + i, MODE_PRIVATE).edit().clear().apply();
            getSharedPreferences(getString(R.string.permanentlines_lineSP) + i, MODE_PRIVATE).edit().clear().apply();
        }
    }

    @Override
    protected void onPause(){
        super.onPause();

        if(deleted){
            saveSettingChanges();
        }
        saveSettings();

/*
        boolean firsttime = getSharedPreferences(getString(R.string.settingsPreferences), MODE_PRIVATE).getBoolean("first", true);
        SharedPreferences.Editor edit = getSharedPreferences(getString(R.string.settingsPreferences), MODE_PRIVATE).edit();
        edit.clear()
                .putInt(getString(R.string.settingsPreferencesLength), settings_caption.size())
                .putBoolean("first", firsttime);

        for(int i = 0; i < settings_caption.size(); i++){
            edit.putString(getString(R.string.settingAt_name) + i, settings_caption.get(i));
        }

        edit.putInt(getString(R.string.selectedSettingPreferences), selectedSetting);
        edit.apply();
*/
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.action_settings){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

*/

/*

//    ImageView clockDropDownImage;

//    boolean dropped = false;

//    ImageView theImageV;
//    private static  final int REQUEST_CHOOSE_IMAGE = 12345;

//    private ArrayList<String>   captions;
//    private ListView            settingsListView;

    */
/*
     * LINES-SETTINGS:
     *//*

    private CheckBox        lines_unicolor_checkbox;
    private TextView        lines_unicolor_colortextview;
    private SeekBar         lines_unicolor_r_seekbar;
    private SeekBar         lines_unicolor_g_seekbar;
    private SeekBar         lines_unicolor_b_seekbar;

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

    */
/*
     * BACKGROUND-SETTINGS:
     *//*

    private CheckBox        background_backgroundpicture_checkbox;
    private Button          background_backgroundpicture_button;
    private TextView        background_backgroundcolor_colortextview;
    private SeekBar         background_backgroundcolor_r_Seekbar;
    private SeekBar         background_backgroundcolor_g_Seekbar;
    private SeekBar         background_backgroundcolor_b_Seekbar;

    */
/*
     * CLOCK-SETTINGS:
     *//*

    private CheckBox        clock_clockenable_checkbox;
    private TextView        clock_clockxposition_caption_texview;
    private SeekBar         clock_clockxposition_seekbar;
    private TextView        clock_clockyposition_caption_textview;
    private SeekBar         clock_clockyposition_seekbar;
    private TextView        clock_clockdiameter_caption_textview;
    private SeekBar         clock_clockdiameter_seekbar;
    private RadioGroup      clock_clockchooser_radiogroup;

    */
/*
     * SIMPLE-CLOCK-SETTINGS:
     *//*

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

    */
/*
     * POINTER-ONLY CLOCK-SETTINGS:
     *//*

    private RadioButton     clock_clock_pointeronly_radiobtn;
    private TextView        clock_clock_pointeronly_color_stunde_caption_textview;
    private TextView        clock_clock_pointeronly_color_stunde_textview;
    private SeekBar         clock_clock_pointeronly_color_stunde_r_seekbar;
    private SeekBar         clock_clock_pointeronly_color_stunde_g_seekbar;
    private SeekBar         clock_clock_pointeronly_color_stunde_b_seekbar;
    private TextView        clock_clock_pointeronly_color_minute_caption_textview;
    private TextView        clock_clock_pointeronly_color_minute_textview;
    private SeekBar         clock_clock_pointeronly_color_minute_r_seekbar;
    private SeekBar         clock_clock_pointeronly_color_minute_g_seekbar;
    private SeekBar         clock_clock_pointeronly_color_minute_b_seekbar;
    private TextView        clock_clock_pointeronly_color_sekunde_caption_textview;
    private TextView        clock_clock_pointeronly_color_sekunde_textview;
    private SeekBar         clock_clock_pointeronly_color_sekunde_r_seekbar;
    private SeekBar         clock_clock_pointeronly_color_sekunde_g_seekbar;
    private SeekBar         clock_clock_pointeronly_color_sekunde_b_seekbar;

    */
/*
     * PARABLE-CLOCK-SETTINGS:
     *//*

    private RadioButton     clock_clock_parabolaclock_radiobtn;
    private TextView        clock_clock_parabolaclock_alphabehind_caption_textview;
    private SeekBar         clock_clock_parabolaclock_alphabehind_seekbar;
    private TextView        clock_clock_parabolaclock_color_stundeminute_caption_textview;
    private TextView        clock_clock_parabolaclock_color_stundeminute_textview;
    private SeekBar         clock_clock_parabolaclock_color_stundeminute_r_seekbar;
    private SeekBar         clock_clock_parabolaclock_color_stundeminute_g_seekbar;
    private SeekBar         clock_clock_parabolaclock_color_stundeminute_b_seekbar;
    private TextView        clock_clock_parabolaclock_color_minutesekunde_caption_textview;
    private TextView        clock_clock_parabolaclock_color_minutesekunde_textview;
    private SeekBar         clock_clock_parabolaclock_color_minutesekunde_r_seekbar;
    private SeekBar         clock_clock_parabolaclock_color_minutesekunde_g_seekbar;
    private SeekBar         clock_clock_parabolaclock_color_minutesekunde_b_seekbar;

    */
/*
     * DIGITAL-CLOCK-SETTINGS:
     *//*

    private RadioButton     clock_clock_digitalclock_radiobtn;
    private CheckBox        clock_clock_digitalclock_dotsblinking_checkbox;

    */
/*
     * LISTENER
     *//*

    private CheckBox.OnCheckedChangeListener        lines_unicolor_checkbox_listener                        = new CompoundButton.OnCheckedChangeListener(){
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
    private SeekBar.OnSeekBarChangeListener         lines_unicolor_listener                                 = new SeekBar.OnSeekBarChangeListener(){
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
    private CheckBox.OnCheckedChangeListener        lines_rainbowcolor_checkbox_listener                    = new CompoundButton.OnCheckedChangeListener(){
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b){
            int vis = (lines_rainbowcolor_checkbox.isChecked())? View.VISIBLE: View.GONE;

            lines_rainbowcolor_textview.setVisibility(vis);
            lines_rainbowcolor_seekbar.setVisibility(vis);
        }
        private void foo(){}
    };
    private CheckBox.OnCheckedChangeListener        lines_linesave_listener                                 = new CompoundButton.OnCheckedChangeListener(){
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b){
            int vis = (lines_linesave_checkbox.isChecked())? View.VISIBLE: View.GONE;

            lines_linesave_deleteLast_Button.setVisibility(vis);
            lines_linesave_deleteAll_Button.setVisibility(vis);
        }
        private void foo(){}
    };
    private CheckBox.OnCheckedChangeListener        lines_linedrawBall_checkbox_listener                    = new CompoundButton.OnCheckedChangeListener(){
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b){
            int vis = (lines_linedrawBall_checkbox.isChecked())? View.VISIBLE: View.GONE;

            lines_linesballsize_caption.setVisibility(vis);
            lines_linesballsize_seekbar.setVisibility(vis);
        }
        private void foo(){}
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
        private void foo(){}
    };
    private CheckBox.OnCheckedChangeListener        background_backgroundpicture_checkbox_listener          = new CompoundButton.OnCheckedChangeListener(){
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
    private SeekBar.OnSeekBarChangeListener         background_backgroundcolor_listener                     = new SeekBar.OnSeekBarChangeListener(){
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
        private void foo(){}
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

            clock_clock_pointeronly_color_stunde_caption_textview.setVisibility(pointerOnlyVis);
            clock_clock_pointeronly_color_stunde_textview.setVisibility(pointerOnlyVis);
            clock_clock_pointeronly_color_stunde_r_seekbar.setVisibility(pointerOnlyVis);
            clock_clock_pointeronly_color_stunde_g_seekbar.setVisibility(pointerOnlyVis);
            clock_clock_pointeronly_color_stunde_b_seekbar.setVisibility(pointerOnlyVis);
            clock_clock_pointeronly_color_minute_caption_textview.setVisibility(pointerOnlyVis);
            clock_clock_pointeronly_color_minute_textview.setVisibility(pointerOnlyVis);
            clock_clock_pointeronly_color_minute_r_seekbar.setVisibility(pointerOnlyVis);
            clock_clock_pointeronly_color_minute_g_seekbar.setVisibility(pointerOnlyVis);
            clock_clock_pointeronly_color_minute_b_seekbar.setVisibility(pointerOnlyVis);
            clock_clock_pointeronly_color_sekunde_caption_textview.setVisibility(pointerOnlyVis);
            clock_clock_pointeronly_color_sekunde_textview.setVisibility(pointerOnlyVis);
            clock_clock_pointeronly_color_sekunde_r_seekbar.setVisibility(pointerOnlyVis);
            clock_clock_pointeronly_color_sekunde_g_seekbar.setVisibility(pointerOnlyVis);
            clock_clock_pointeronly_color_sekunde_b_seekbar.setVisibility(pointerOnlyVis);

            clock_clock_parabolaclock_alphabehind_caption_textview.setVisibility(parabolaVisibl);
            clock_clock_parabolaclock_alphabehind_seekbar.setVisibility(parabolaVisibl);
            clock_clock_parabolaclock_color_stundeminute_caption_textview.setVisibility(parabolaVisibl);
            clock_clock_parabolaclock_color_stundeminute_textview.setVisibility(parabolaVisibl);
            clock_clock_parabolaclock_color_stundeminute_r_seekbar.setVisibility(parabolaVisibl);
            clock_clock_parabolaclock_color_stundeminute_g_seekbar.setVisibility(parabolaVisibl);
            clock_clock_parabolaclock_color_stundeminute_b_seekbar.setVisibility(parabolaVisibl);
            clock_clock_parabolaclock_color_minutesekunde_caption_textview.setVisibility(parabolaVisibl);
            clock_clock_parabolaclock_color_minutesekunde_textview.setVisibility(parabolaVisibl);
            clock_clock_parabolaclock_color_minutesekunde_r_seekbar.setVisibility(parabolaVisibl);
            clock_clock_parabolaclock_color_minutesekunde_g_seekbar.setVisibility(parabolaVisibl);
            clock_clock_parabolaclock_color_minutesekunde_b_seekbar.setVisibility(parabolaVisibl);

            clock_clock_digitalclock_dotsblinking_checkbox.setVisibility(digitalCloVisi);
        }
        private void foo(){}
    };
    private SeekBar.OnSeekBarChangeListener         clock_clock_simpleclock_color_stunde_listener           = new SeekBar.OnSeekBarChangeListener(){
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
    private SeekBar.OnSeekBarChangeListener         clock_clock_simpleclock_color_minute_listener           = new SeekBar.OnSeekBarChangeListener(){
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
    private SeekBar.OnSeekBarChangeListener         clock_clock_simpleclock_color_sekunde_listener          = new SeekBar.OnSeekBarChangeListener(){
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
    private SeekBar.OnSeekBarChangeListener         clock_clock_pointeronly_color_stunde_listener           = new SeekBar.OnSeekBarChangeListener(){
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b){
            clock_clock_pointeronly_color_stunde_textview.setBackgroundColor(Color.rgb(
                    clock_clock_pointeronly_color_stunde_r_seekbar.getProgress(),
                    clock_clock_pointeronly_color_stunde_g_seekbar.getProgress(),
                    clock_clock_pointeronly_color_stunde_b_seekbar.getProgress()
            ));
        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar){}
        @Override
        public void onStopTrackingTouch(SeekBar seekBar){}
    };
    private SeekBar.OnSeekBarChangeListener         clock_clock_pointeronly_color_minute_listener           = new SeekBar.OnSeekBarChangeListener(){
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b){
            clock_clock_pointeronly_color_minute_textview.setBackgroundColor(Color.rgb(
                    clock_clock_pointeronly_color_minute_r_seekbar.getProgress(),
                    clock_clock_pointeronly_color_minute_g_seekbar.getProgress(),
                    clock_clock_pointeronly_color_minute_b_seekbar.getProgress()
            ));
        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar){}
        @Override
        public void onStopTrackingTouch(SeekBar seekBar){}
    };
    private SeekBar.OnSeekBarChangeListener         clock_clock_pointeronly_color_sekunde_listener          = new SeekBar.OnSeekBarChangeListener(){
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b){
            clock_clock_pointeronly_color_sekunde_textview.setBackgroundColor(Color.rgb(
                    clock_clock_pointeronly_color_sekunde_r_seekbar.getProgress(),
                    clock_clock_pointeronly_color_sekunde_g_seekbar.getProgress(),
                    clock_clock_pointeronly_color_sekunde_b_seekbar.getProgress()
            ));
        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar){}
        @Override
        public void onStopTrackingTouch(SeekBar seekBar){}
    };
    private SeekBar.OnSeekBarChangeListener         clock_clock_parabolaclock_color_stundeminute_listener   = new SeekBar.OnSeekBarChangeListener(){
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b){
            clock_clock_parabolaclock_color_stundeminute_textview.setBackgroundColor(Color.rgb(
                    clock_clock_parabolaclock_color_stundeminute_r_seekbar.getProgress(),
                    clock_clock_parabolaclock_color_stundeminute_g_seekbar.getProgress(),
                    clock_clock_parabolaclock_color_stundeminute_b_seekbar.getProgress()
            ));
        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar){}
        @Override
        public void onStopTrackingTouch(SeekBar seekBar){}
    };
    private SeekBar.OnSeekBarChangeListener         clock_clock_parabolaclock_color_minutesekunde_listener  = new SeekBar.OnSeekBarChangeListener(){
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b){
            clock_clock_parabolaclock_color_minutesekunde_textview.setBackgroundColor(Color.rgb(
                    clock_clock_parabolaclock_color_minutesekunde_r_seekbar.getProgress(),
                    clock_clock_parabolaclock_color_minutesekunde_g_seekbar.getProgress(),
                    clock_clock_parabolaclock_color_minutesekunde_b_seekbar.getProgress()
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
        setContentView(R.layout.activity_change_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_change_settings);
        setSupportActionBar(toolbar);
        settings_index = 0;

        */
/*
         * First initialize layout, so it will work
         *//*

        lines_unicolor_checkbox                                         = (CheckBox)        findViewById(R.id.unicolor_checkbox);
        lines_unicolor_colortextview                                    = (TextView)        findViewById(R.id.unicolor_colortextview);
        lines_unicolor_r_seekbar                                        = (SeekBar)         findViewById(R.id.unicolor_r_Seekbar);
        lines_unicolor_g_seekbar                                        = (SeekBar)         findViewById(R.id.unicolor_g_Seekbar);
        lines_unicolor_b_seekbar                                        = (SeekBar)         findViewById(R.id.unicolor_b_Seekbar);
        lines_unicolor_checkbox.setOnCheckedChangeListener(lines_unicolor_checkbox_listener);
        lines_unicolor_r_seekbar.setOnSeekBarChangeListener(lines_unicolor_listener);
        lines_unicolor_g_seekbar.setOnSeekBarChangeListener(lines_unicolor_listener);
        lines_unicolor_b_seekbar.setOnSeekBarChangeListener(lines_unicolor_listener);

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
        background_backgroundcolor_r_Seekbar                            = (SeekBar)         findViewById(R.id.backgroundcolor_r_Seekbar);
        background_backgroundcolor_g_Seekbar                            = (SeekBar)         findViewById(R.id.backgroundcolor_g_Seekbar);
        background_backgroundcolor_b_Seekbar                            = (SeekBar)         findViewById(R.id.backgroundcolor_b_Seekbar);
        background_backgroundpicture_checkbox.setOnCheckedChangeListener(background_backgroundpicture_checkbox_listener);
        background_backgroundcolor_r_Seekbar.setOnSeekBarChangeListener(background_backgroundcolor_listener);
        background_backgroundcolor_g_Seekbar.setOnSeekBarChangeListener(background_backgroundcolor_listener);
        background_backgroundcolor_b_Seekbar.setOnSeekBarChangeListener(background_backgroundcolor_listener);

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
        clock_clock_simpleclock_color_stunde_r_seekbar                  = (SeekBar)         findViewById(R.id.clock_simpleclock_color_stunde_r_seekbar);
        clock_clock_simpleclock_color_stunde_g_seekbar                  = (SeekBar)         findViewById(R.id.clock_simpleclock_color_stunde_g_seekbar);
        clock_clock_simpleclock_color_stunde_b_seekbar                  = (SeekBar)         findViewById(R.id.clock_simpleclock_color_stunde_b_seekbar);
        clock_clock_simpleclock_color_minute_caption_textview           = (TextView)        findViewById(R.id.clock_simpleclock_color_minute_caption_textview);
        clock_clock_simpleclock_color_minute_textview                   = (TextView)        findViewById(R.id.clock_simpleclock_color_minute_textview);
        clock_clock_simpleclock_color_minute_r_seekbar                  = (SeekBar)         findViewById(R.id.clock_simpleclock_color_minute_r_seekbar);
        clock_clock_simpleclock_color_minute_g_seekbar                  = (SeekBar)         findViewById(R.id.clock_simpleclock_color_minute_g_seekbar);
        clock_clock_simpleclock_color_minute_b_seekbar                  = (SeekBar)         findViewById(R.id.clock_simpleclock_color_minute_b_seekbar);
        clock_clock_simpleclock_color_sekunde_caption_textview          = (TextView)        findViewById(R.id.clock_simpleclock_color_sekunde_caption_textview);
        clock_clock_simpleclock_color_sekunde_textview                  = (TextView)        findViewById(R.id.clock_simpleclock_color_sekunde_textview);
        clock_clock_simpleclock_color_sekunde_r_seekbar                 = (SeekBar)         findViewById(R.id.clock_simpleclock_color_sekunde_r_seekbar);
        clock_clock_simpleclock_color_sekunde_g_seekbar                 = (SeekBar)         findViewById(R.id.clock_simpleclock_color_sekunde_g_seekbar);
        clock_clock_simpleclock_color_sekunde_b_seekbar                 = (SeekBar)         findViewById(R.id.clock_simpleclock_color_sekunde_b_seekbar);
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

        clock_clock_pointeronly_radiobtn                                = (RadioButton)     findViewById(R.id.clock_pointeronly_radiobtn);
        clock_clock_pointeronly_color_stunde_caption_textview           = (TextView)        findViewById(R.id.clock_pointeronly_color_stunde_caption_textview);
        clock_clock_pointeronly_color_stunde_textview                   = (TextView)        findViewById(R.id.clock_pointeronly_color_stunde_textview);
        clock_clock_pointeronly_color_stunde_r_seekbar                  = (SeekBar)         findViewById(R.id.clock_pointeronly_color_stunde_r_seekbar);
        clock_clock_pointeronly_color_stunde_g_seekbar                  = (SeekBar)         findViewById(R.id.clock_pointeronly_color_stunde_g_seekbar);
        clock_clock_pointeronly_color_stunde_b_seekbar                  = (SeekBar)         findViewById(R.id.clock_pointeronly_color_stunde_b_seekbar);
        clock_clock_pointeronly_color_minute_caption_textview           = (TextView)        findViewById(R.id.clock_pointeronly_color_minute_caption_textview);
        clock_clock_pointeronly_color_minute_textview                   = (TextView)        findViewById(R.id.clock_pointeronly_color_minute_textview);
        clock_clock_pointeronly_color_minute_r_seekbar                  = (SeekBar)         findViewById(R.id.clock_pointeronly_color_minute_r_seekbar);
        clock_clock_pointeronly_color_minute_g_seekbar                  = (SeekBar)         findViewById(R.id.clock_pointeronly_color_minute_g_seekbar);
        clock_clock_pointeronly_color_minute_b_seekbar                  = (SeekBar)         findViewById(R.id.clock_pointeronly_color_minute_b_seekbar);
        clock_clock_pointeronly_color_sekunde_caption_textview          = (TextView)        findViewById(R.id.clock_pointeronly_color_sekunde_caption_textview);
        clock_clock_pointeronly_color_sekunde_textview                  = (TextView)        findViewById(R.id.clock_pointeronly_color_sekunde_textview);
        clock_clock_pointeronly_color_sekunde_r_seekbar                 = (SeekBar)         findViewById(R.id.clock_pointeronly_color_sekunde_r_seekbar);
        clock_clock_pointeronly_color_sekunde_g_seekbar                 = (SeekBar)         findViewById(R.id.clock_pointeronly_color_sekunde_g_seekbar);
        clock_clock_pointeronly_color_sekunde_b_seekbar                 = (SeekBar)         findViewById(R.id.clock_pointeronly_color_sekunde_b_seekbar);
        clock_clock_pointeronly_color_stunde_r_seekbar.setOnSeekBarChangeListener(clock_clock_pointeronly_color_stunde_listener);
        clock_clock_pointeronly_color_stunde_g_seekbar.setOnSeekBarChangeListener(clock_clock_pointeronly_color_stunde_listener);
        clock_clock_pointeronly_color_stunde_b_seekbar.setOnSeekBarChangeListener(clock_clock_pointeronly_color_stunde_listener);
        clock_clock_pointeronly_color_minute_r_seekbar.setOnSeekBarChangeListener(clock_clock_pointeronly_color_minute_listener);
        clock_clock_pointeronly_color_minute_g_seekbar.setOnSeekBarChangeListener(clock_clock_pointeronly_color_minute_listener);
        clock_clock_pointeronly_color_minute_b_seekbar.setOnSeekBarChangeListener(clock_clock_pointeronly_color_minute_listener);
        clock_clock_pointeronly_color_sekunde_r_seekbar.setOnSeekBarChangeListener(clock_clock_pointeronly_color_sekunde_listener);
        clock_clock_pointeronly_color_sekunde_g_seekbar.setOnSeekBarChangeListener(clock_clock_pointeronly_color_sekunde_listener);
        clock_clock_pointeronly_color_sekunde_b_seekbar.setOnSeekBarChangeListener(clock_clock_pointeronly_color_sekunde_listener);
        clock_clock_pointeronly_radiobtn.setOnCheckedChangeListener(clock_clockSelected_listener);

        clock_clock_parabolaclock_radiobtn                              = (RadioButton)     findViewById(R.id.clock_parabolaclock_radiobtn);
        clock_clock_parabolaclock_alphabehind_caption_textview          = (TextView)        findViewById(R.id.clock_parabolaclock_alphabehind_caption_textview);
        clock_clock_parabolaclock_alphabehind_seekbar                   = (SeekBar)         findViewById(R.id.clock_parabolaclock_alphabehind_seekbar);
        clock_clock_parabolaclock_color_stundeminute_caption_textview   = (TextView)        findViewById(R.id.clock_parabolaclock_color_stundeminute_caption_textview);
        clock_clock_parabolaclock_color_stundeminute_textview           = (TextView)        findViewById(R.id.clock_parabolaclock_color_stundeminute_textview);
        clock_clock_parabolaclock_color_stundeminute_r_seekbar          = (SeekBar)         findViewById(R.id.clock_parabolaclock_color_stundeminute_r_seekbar);
        clock_clock_parabolaclock_color_stundeminute_g_seekbar          = (SeekBar)         findViewById(R.id.clock_parabolaclock_color_stundeminute_g_seekbar);
        clock_clock_parabolaclock_color_stundeminute_b_seekbar          = (SeekBar)         findViewById(R.id.clock_parabolaclock_color_stundeminute_b_seekbar);
        clock_clock_parabolaclock_color_minutesekunde_caption_textview  = (TextView)        findViewById(R.id.clock_parabolaclock_color_minutesekunde_caption_textview);
        clock_clock_parabolaclock_color_minutesekunde_textview          = (TextView)        findViewById(R.id.clock_parabolaclock_color_minutesekunde_textview);
        clock_clock_parabolaclock_color_minutesekunde_r_seekbar         = (SeekBar)         findViewById(R.id.clock_parabolaclock_color_minutesekunde_r_seekbar);
        clock_clock_parabolaclock_color_minutesekunde_g_seekbar         = (SeekBar)         findViewById(R.id.clock_parabolaclock_color_minutesekunde_g_seekbar);
        clock_clock_parabolaclock_color_minutesekunde_b_seekbar         = (SeekBar)         findViewById(R.id.clock_parabolaclock_color_minutesekunde_b_seekbar);
        clock_clock_parabolaclock_color_stundeminute_r_seekbar.setOnSeekBarChangeListener(clock_clock_parabolaclock_color_stundeminute_listener);
        clock_clock_parabolaclock_color_stundeminute_g_seekbar.setOnSeekBarChangeListener(clock_clock_parabolaclock_color_stundeminute_listener);
        clock_clock_parabolaclock_color_stundeminute_b_seekbar.setOnSeekBarChangeListener(clock_clock_parabolaclock_color_stundeminute_listener);
        clock_clock_parabolaclock_color_minutesekunde_r_seekbar.setOnSeekBarChangeListener(clock_clock_parabolaclock_color_minutesekunde_listener);
        clock_clock_parabolaclock_color_minutesekunde_g_seekbar.setOnSeekBarChangeListener(clock_clock_parabolaclock_color_minutesekunde_listener);
        clock_clock_parabolaclock_color_minutesekunde_b_seekbar.setOnSeekBarChangeListener(clock_clock_parabolaclock_color_minutesekunde_listener);
        clock_clock_parabolaclock_radiobtn.setOnCheckedChangeListener(clock_clockSelected_listener);

        clock_clock_digitalclock_radiobtn                               = (RadioButton)     findViewById(R.id.clock_digitalclock_radiobtn);
        clock_clock_digitalclock_dotsblinking_checkbox                  = (CheckBox)        findViewById(R.id.clock_digitalclock_dotsblinking_checkbox);
        clock_clock_digitalclock_radiobtn.setOnCheckedChangeListener(clock_clockSelected_listener);

        */
/*
         * Make the drop-down layout responsive to set-Calls
         *  -> make it show nothing and if it's set to true it will show its stuff
         *//*

        lines_unicolor_checkbox.setChecked(true);
        lines_rainbowcolor_checkbox.setChecked(true);
        lines_linesave_checkbox.setChecked(true);
        lines_linedrawBall_checkbox.setChecked(true);
        lines_linesfadecomplete_radiobutton.setChecked(true);
        background_backgroundpicture_checkbox.setChecked(true);
        clock_clockenable_checkbox.setChecked(true);
        clock_clock_simpleclock_radiobtn.setChecked(true);

        lines_unicolor_r_seekbar.setProgress(1);
        background_backgroundcolor_r_Seekbar.setProgress(1);
        clock_clock_simpleclock_color_stunde_r_seekbar.setProgress(1);
        clock_clock_simpleclock_color_minute_r_seekbar.setProgress(1);
        clock_clock_simpleclock_color_sekunde_r_seekbar.setProgress(1);
        clock_clock_pointeronly_color_stunde_r_seekbar.setProgress(1);
        clock_clock_pointeronly_color_minute_r_seekbar.setProgress(1);
        clock_clock_pointeronly_color_sekunde_r_seekbar.setProgress(1);
        clock_clock_parabolaclock_color_stundeminute_r_seekbar.setProgress(1);
        clock_clock_parabolaclock_color_minutesekunde_r_seekbar.setProgress(1);

        */
/*
        Then load and show settings, so the layout will (hopefully) handle itself
         *//*

        if(getSharedPreferences(getString(R.string.settingsAt) + settings_index, MODE_PRIVATE).getBoolean("firstTime", true)){
            getSharedPreferences(getString(R.string.settingsAt) + settings_index, MODE_PRIVATE).edit().putBoolean("firstTime", false).apply();

            lines_unicolor_checkbox.setChecked(false);
            lines_unicolor_r_seekbar.setProgress(255);

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
            background_backgroundcolor_r_Seekbar.setProgress(243);
            background_backgroundcolor_g_Seekbar.setProgress(168);
            background_backgroundcolor_b_Seekbar.setProgress(168);

            clock_clockenable_checkbox.setChecked(false);
            clock_clockxposition_seekbar.setProgress(500);
            clock_clockyposition_seekbar.setProgress(500);
            clock_clockdiameter_seekbar.setProgress(800);

            clock_clock_simpleclock_radiobtn.setChecked(true);
            clock_clock_simpleclock_alphabehind_seekbar.setProgress(192);
            clock_clock_simpleclock_color_stunde_r_seekbar.setProgress(255);
            clock_clock_simpleclock_color_minute_g_seekbar.setProgress(255);
            clock_clock_simpleclock_color_sekunde_b_seekbar.setProgress(255);

            clock_clock_pointeronly_color_stunde_r_seekbar.setProgress(255);
            clock_clock_pointeronly_color_minute_g_seekbar.setProgress(255);
            clock_clock_pointeronly_color_sekunde_b_seekbar.setProgress(255);

            clock_clock_parabolaclock_alphabehind_seekbar.setProgress(192);
            clock_clock_parabolaclock_color_stundeminute_r_seekbar.setProgress(255);
            clock_clock_parabolaclock_color_stundeminute_g_seekbar.setProgress(127);
            clock_clock_parabolaclock_color_minutesekunde_g_seekbar.setProgress(127);
            clock_clock_parabolaclock_color_minutesekunde_b_seekbar.setProgress(255);

        }else{
            int temp;

            SharedPreferences prefs = getSharedPreferences(getString(R.string.settingsAt) + settings_index, MODE_PRIVATE);

            lines_unicolor_checkbox.setChecked(prefs.getBoolean(getString(R.string.lines_unicolor_preferences), false));
            temp = prefs.getInt(getString(R.string.lines_unicolor_color_preferences), 0xFF0000);
            lines_unicolor_r_seekbar.setProgress(Color.red(temp));
            lines_unicolor_g_seekbar.setProgress(Color.green(temp));
            lines_unicolor_b_seekbar.setProgress(Color.blue(temp));

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
            temp = prefs.getInt(getString(R.string.background_alternateColor_preferences), 0xF3A8A8);
            background_backgroundcolor_r_Seekbar.setProgress(Color.red(temp));
            background_backgroundcolor_g_Seekbar.setProgress(Color.green(temp));
            background_backgroundcolor_b_Seekbar.setProgress(Color.blue(temp));

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

            temp = prefs.getInt(getString(R.string.clock_simpleclock_stdcolor_preferences), 0xFF0000);
            clock_clock_simpleclock_color_stunde_r_seekbar.setProgress(Color.red(temp));
            clock_clock_simpleclock_color_stunde_g_seekbar.setProgress(Color.green(temp));
            clock_clock_simpleclock_color_stunde_b_seekbar.setProgress(Color.blue(temp));

            temp = prefs.getInt(getString(R.string.clock_simpleclock_mincolor_preferences), 0x00FF00);
            clock_clock_simpleclock_color_minute_r_seekbar.setProgress(Color.red(temp));
            clock_clock_simpleclock_color_minute_g_seekbar.setProgress(Color.green(temp));
            clock_clock_simpleclock_color_minute_b_seekbar.setProgress(Color.blue(temp));

            temp = prefs.getInt(getString(R.string.clock_simpleclock_seccolor_preferences), 0x0000FF);
            clock_clock_simpleclock_color_sekunde_r_seekbar.setProgress(Color.red(temp));
            clock_clock_simpleclock_color_sekunde_g_seekbar.setProgress(Color.green(temp));
            clock_clock_simpleclock_color_sekunde_b_seekbar.setProgress(Color.blue(temp));


            clock_clock_pointeronly_radiobtn.setChecked(prefs.getBoolean(getString(R.string.clock_pointerOnlyClockchosen_preferences), false));

            temp = prefs.getInt(getString(R.string.clock_pointeronlyclock_stdcolor_preferences), 0xFF0000);
            clock_clock_pointeronly_color_stunde_r_seekbar.setProgress(Color.red(temp));
            clock_clock_pointeronly_color_stunde_g_seekbar.setProgress(Color.green(temp));
            clock_clock_pointeronly_color_stunde_b_seekbar.setProgress(Color.blue(temp));

            temp = prefs.getInt(getString(R.string.clock_pointeronlyclock_mincolor_preferences), 0x00FF00);
            clock_clock_pointeronly_color_minute_r_seekbar.setProgress(Color.red(temp));
            clock_clock_pointeronly_color_minute_g_seekbar.setProgress(Color.green(temp));
            clock_clock_pointeronly_color_minute_b_seekbar.setProgress(Color.blue(temp));

            temp = prefs.getInt(getString(R.string.clock_pointeronlyclock_seccolor_preferences), 0x0000FF);
            clock_clock_pointeronly_color_sekunde_r_seekbar.setProgress(Color.red(temp));
            clock_clock_pointeronly_color_sekunde_g_seekbar.setProgress(Color.green(temp));
            clock_clock_pointeronly_color_sekunde_b_seekbar.setProgress(Color.blue(temp));


            clock_clock_parabolaclock_radiobtn.setChecked(prefs.getBoolean(getString(R.string.clock_parabolaclockchosen_preferences), false));

            clock_clock_parabolaclock_alphabehind_seekbar.setProgress(Color.alpha(prefs.getInt(getString(R.string.clock_parabolaclock_alphaColor_preferences), 0xC0FFFFFF)));

            temp = prefs.getInt(getString(R.string.clock_parabolaclock_stdmincolor_preferences), 0xFF7F00);
            clock_clock_parabolaclock_color_stundeminute_r_seekbar.setProgress(Color.red(temp));
            clock_clock_parabolaclock_color_stundeminute_g_seekbar.setProgress(Color.green(temp));
            clock_clock_parabolaclock_color_stundeminute_b_seekbar.setProgress(Color.blue(temp));

            temp = prefs.getInt(getString(R.string.clock_parabolaclock_minsekcolor_preferences), 0x007FFF);
            clock_clock_parabolaclock_color_minutesekunde_r_seekbar.setProgress(Color.red(temp));
            clock_clock_parabolaclock_color_minutesekunde_g_seekbar.setProgress(Color.green(temp));
            clock_clock_parabolaclock_color_minutesekunde_b_seekbar.setProgress(Color.blue(temp));

            clock_clock_digitalclock_radiobtn.setChecked(prefs.getBoolean(getString(R.string.clock_digitalClockchosen_preferences), false));
            clock_clock_digitalclock_dotsblinking_checkbox.setChecked(prefs.getBoolean(getString(R.string.clock_digitalClock_dotsblinking_preferences), true));
        }
    }//end of onCreate()

    private int settings_index;

    public void deleteAllPermanentLines(View v){
        new AlertDialog.Builder(this)
                .setTitle("Alle permanenten Linien löschen?")
                .setMessage("Wollen sie wirklich alle permanenten Linien löschen? Dies kann nicht rückgängig gemacht werden!")
                .setPositiveButton("Ja", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i){
                        ArrayList<Line> lines = Line.loadFromSharedPreferences(getSharedPreferences(getString(R.string.permanentlines_lineSP) + settings_index, MODE_PRIVATE), getBaseContext());

                        if(lines.size() >= 1){
                            do{
                                lines.remove(lines.size() - 1);
                            }while(lines.size() != 0);
                            Line.saveToSharedPreferences(lines, getSharedPreferences(getString(R.string.permanentlines_lineSP) + settings_index, MODE_PRIVATE), getBaseContext());
                            Snackbar.make(findViewById(R.id.space), "Erfolgreich gelöscht", Snackbar.LENGTH_SHORT).show();
                        }else{
                            final Snackbar sb = Snackbar.make(findViewById(R.id.space), "Es sind derzeitig keine permanenten Linien vorhanden!", Snackbar.LENGTH_INDEFINITE);
//                            sb.setAction("OK", v -> sb.dismiss());
                            sb.setAction("OK", new View.OnClickListener(){
                                @Override
                                public void onClick(View view){
                                    sb.dismiss();
                                }
                            }).show();
                        }
                    }
                })
                .setNegativeButton("Nein", null)
                .show();
    }

    public void deleteLastPermanentLine(View v){
        new AlertDialog.Builder(this)
                .setTitle("Letzte permanente Linie löschen?")
                .setMessage("Wollen sie die letzte permanente Linie wirklich löschen? Dies kann nicht rückgängig gemacht werden!")
                .setPositiveButton("Ja", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i){
                        ArrayList<Line> lines = Line.loadFromSharedPreferences(getSharedPreferences(getString(R.string.permanentlines_lineSP) + settings_index, MODE_PRIVATE), getBaseContext());

                        if(lines.size() >= 1){
                            lines.remove(lines.size() - 1);
                            Line.saveToSharedPreferences(lines, getSharedPreferences(getString(R.string.permanentlines_lineSP) + settings_index, MODE_PRIVATE), getBaseContext());
                            Snackbar.make(findViewById(R.id.space), "Erfolgreich gelöscht", Snackbar.LENGTH_SHORT).show();
                        }else{
                            final Snackbar sb = Snackbar.make(findViewById(R.id.space), "Es sind derzeitig keine permanenten Linien vorhanden!", Snackbar.LENGTH_INDEFINITE);
//                            sb.setAction("OK", v -> sb.dismiss());
                            sb.setAction("OK", new View.OnClickListener(){
                                @Override
                                public void onClick(View view){
                                    sb.dismiss();
                                }
                            }).show();
                        }
                    }
                })
                .setNegativeButton("Nein", null)
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

                getSharedPreferences(getString(R.string.settingsAt) + settings_index, MODE_PRIVATE).edit().putString(getString(R.string.background_picturepath_preferences), picturePath).apply();
            }
        }
    }

    @Override
    protected void onPause(){
        super.onPause();

        */
/**
         * SAVE ALL VALUES!!
         *//*

//        Display display = getWindowManager().getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//        int width = size.x;
//        int height = size.y;
//
//        float clockDiam = ((width > height)? height: width) * clock_clockdiameter_seekbar.getProgress() * 0.001f;
//        float clockX = (width - clockDiam) * clock_clockxposition_seekbar.getProgress() * 0.001f;
//        float clockY = (height - clockDiam) * clock_clockyposition_seekbar.getProgress() * 0.001f;

        getSharedPreferences(getString(R.string.settingsAt) + settings_index, MODE_PRIVATE).edit()
                .putBoolean(getString(R.string.lines_unicolor_preferences), lines_unicolor_checkbox.isChecked())
                .putInt(getString(R.string.lines_unicolor_color_preferences), Color.rgb(
                        lines_unicolor_r_seekbar.getProgress(),
                        lines_unicolor_g_seekbar.getProgress(),
                        lines_unicolor_b_seekbar.getProgress()
                ))

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
                .putInt(getString(R.string.background_alternateColor_preferences), Color.rgb(
                        background_backgroundcolor_r_Seekbar.getProgress(),
                        background_backgroundcolor_g_Seekbar.getProgress(),
                        background_backgroundcolor_b_Seekbar.getProgress()
                ))
                .putBoolean(getString(R.string.clock_drawClock_preferences), clock_clockenable_checkbox.isChecked())
//                .putFloat(getString(R.string.clock_xposition_preferences), clockX)
//                .putFloat(getString(R.string.clock_yposition_preferences), clockY)
//                .putFloat(getString(R.string.clock_diameter_preferences), clockDiam)
                .putFloat(getString(R.string.clock_diameter_preferences), (float) clock_clockdiameter_seekbar.getProgress() / 1000f)
                .putFloat(getString(R.string.clock_xposition_preferences), (float) clock_clockxposition_seekbar.getProgress() / 1000f)
                .putFloat(getString(R.string.clock_yposition_preferences), (float) clock_clockyposition_seekbar.getProgress() / 1000f)

                .putBoolean(getString(R.string.clock_simpleClockchosen_preferences), clock_clock_simpleclock_radiobtn.isChecked())
                .putInt(getString(R.string.clock_simpleclock_alphaColor_preferences), Color.argb(clock_clock_simpleclock_alphabehind_seekbar.getProgress(), 0xFF, 0xFF, 0xFF))
                .putInt(getString(R.string.clock_simpleclock_stdcolor_preferences), Color.rgb(
                        clock_clock_simpleclock_color_stunde_r_seekbar.getProgress(),
                        clock_clock_simpleclock_color_stunde_g_seekbar.getProgress(),
                        clock_clock_simpleclock_color_stunde_b_seekbar.getProgress()
                ))
                .putInt(getString(R.string.clock_simpleclock_mincolor_preferences), Color.rgb(
                        clock_clock_simpleclock_color_minute_r_seekbar.getProgress(),
                        clock_clock_simpleclock_color_minute_g_seekbar.getProgress(),
                        clock_clock_simpleclock_color_minute_b_seekbar.getProgress()
                ))
                .putInt(getString(R.string.clock_simpleclock_seccolor_preferences), Color.rgb(
                        clock_clock_simpleclock_color_sekunde_r_seekbar.getProgress(),
                        clock_clock_simpleclock_color_sekunde_g_seekbar.getProgress(),
                        clock_clock_simpleclock_color_sekunde_b_seekbar.getProgress()
                ))

                .putBoolean(getString(R.string.clock_pointerOnlyClockchosen_preferences), clock_clock_pointeronly_radiobtn.isChecked())
                .putInt(getString(R.string.clock_pointeronlyclock_stdcolor_preferences), Color.rgb(
                        clock_clock_pointeronly_color_stunde_r_seekbar.getProgress(),
                        clock_clock_pointeronly_color_stunde_g_seekbar.getProgress(),
                        clock_clock_pointeronly_color_stunde_b_seekbar.getProgress()
                ))
                .putInt(getString(R.string.clock_pointeronlyclock_mincolor_preferences), Color.rgb(
                        clock_clock_pointeronly_color_minute_r_seekbar.getProgress(),
                        clock_clock_pointeronly_color_minute_g_seekbar.getProgress(),
                        clock_clock_pointeronly_color_minute_b_seekbar.getProgress()
                ))
                .putInt(getString(R.string.clock_pointeronlyclock_seccolor_preferences), Color.rgb(
                        clock_clock_pointeronly_color_sekunde_r_seekbar.getProgress(),
                        clock_clock_pointeronly_color_sekunde_g_seekbar.getProgress(),
                        clock_clock_pointeronly_color_sekunde_b_seekbar.getProgress()
                ))

                .putBoolean(getString(R.string.clock_parabolaclockchosen_preferences), clock_clock_parabolaclock_radiobtn.isChecked())
                .putInt(getString(R.string.clock_parabolaclock_alphaColor_preferences), Color.argb(clock_clock_parabolaclock_alphabehind_seekbar.getProgress(), 255, 255, 255))
                .putInt(getString(R.string.clock_parabolaclock_stdmincolor_preferences), Color.rgb(
                        clock_clock_parabolaclock_color_stundeminute_r_seekbar.getProgress(),
                        clock_clock_parabolaclock_color_stundeminute_g_seekbar.getProgress(),
                        clock_clock_parabolaclock_color_stundeminute_b_seekbar.getProgress()
                ))
                .putInt(getString(R.string.clock_parabolaclock_minsekcolor_preferences), Color.rgb(
                        clock_clock_parabolaclock_color_minutesekunde_r_seekbar.getProgress(),
                        clock_clock_parabolaclock_color_minutesekunde_g_seekbar.getProgress(),
                        clock_clock_parabolaclock_color_minutesekunde_b_seekbar.getProgress()
                ))

                .putBoolean(getString(R.string.clock_digitalClockchosen_preferences), clock_clock_digitalclock_radiobtn.isChecked())
                .putBoolean(getString(R.string.clock_digitalClock_dotsblinking_preferences), clock_clock_digitalclock_dotsblinking_checkbox.isChecked())

                .apply();
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
*/
}

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

    private ArrayList<String> settings_caption;
    private ArrayList<Integer> settings_indexes;
    private boolean deleted;
    private int selectedSetting;
    private ListView exampleListView;
    private SettingsAdapter ad;

    private AdapterView.OnItemClickListener changeSettingListener = new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){
            modifySetting(i);
        }
    };
    private AdapterView.OnItemLongClickListener createContextMenuListener = new AdapterView.OnItemLongClickListener(){
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l){
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle(settings_caption.get(i))
                    .setItems(R.array.context_menu_items, new DialogInterface.OnClickListener(){
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
                                            .setTitle(R.string.selectTitle_caption)
                                            .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener(){
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int hay){
                                                    EditText newNameET = (EditText) dialog.findViewById(R.id.neuerNameEditText);
                                                    settings_caption.set(i, newNameET.getText().toString());
                                                    ad.notifyDataSetChanged();
                                                }
                                            })
                                            .setNegativeButton(R.string.dialog_abort, null)
                                            .setView(R.layout.layout_neuertitel_setting)
                                            .show();

                                    EditText newNameET = (EditText) dialog.findViewById(R.id.neuerNameEditText);
                                    newNameET.setText(settings_caption.get(i));

                                    break;
                                case 2:
                                    if(i == selectedSetting){
                                        dialog = new AlertDialog.Builder(MainActivity.this)
                                                .setTitle(R.string.deleteChosenSetting_caption)
                                                .setMessage(R.string.deleteChosenSetting_message)
                                                .setPositiveButton(R.string.dialog_ok, null)
                                                .setNegativeButton(R.string.dialog_fuckyou, new DialogInterface.OnClickListener(){
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i){
                                                        Snackbar.make(exampleListView, R.string.dialog_fuckyou_answer, Snackbar.LENGTH_INDEFINITE).show();
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

        if(getSharedPreferences(getString(R.string.settings_settingsPreferences), MODE_PRIVATE).getBoolean(getString(R.string.settings_firstTimeLaunched_Preferences), true)){
            getSharedPreferences(getString(R.string.settings_settingsPreferences), MODE_PRIVATE).edit().putBoolean(getString(R.string.settings_firstTimeLaunched_Preferences), false).apply();

            settings_caption.add(getString(R.string.firstSettingName));
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
                        .setTitle(R.string.selectTitle_caption)
                        .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int hay){
                                EditText newNameET = (EditText) dialog.findViewById(R.id.neuerNameEditText);
                                settings_caption.set(settings_caption.size() - 1, newNameET.getText().toString());
                                ad.notifyDataSetChanged();

                                exampleListView.smoothScrollToPosition(settings_caption.size() - 1);
                                modifySetting(settings_caption.size() - 1);
                            }
                        })
                        .setNegativeButton(R.string.dialog_abort, new DialogInterface.OnClickListener(){
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
        SharedPreferences settingsPrefs = getSharedPreferences(getString(R.string.settings_settingsPreferences), MODE_PRIVATE);
        int length = settingsPrefs.getInt(getString(R.string.settings_settingsPreferencesLength), 0);

        for(int i = 0; i < length; i++){
            settings_caption.add(settingsPrefs.getString(getString(R.string.settings_settingAt_name) + i, ""));
            settings_indexes.add(i);
        }
        selectedSetting = settingsPrefs.getInt(getString(R.string.settings_selectedSettingPreferences), 0);
    }

    private void saveSettings(){
        boolean firsttime = getSharedPreferences(getString(R.string.settings_settingsPreferences), MODE_PRIVATE).getBoolean(getString(R.string.settings_firstTimeLaunched_Preferences), true);
        SharedPreferences.Editor edit = getSharedPreferences(getString(R.string.settings_settingsPreferences), MODE_PRIVATE).edit();
        edit.clear()
                .putInt(getString(R.string.settings_settingsPreferencesLength), settings_caption.size())
                .putBoolean(getString(R.string.settings_firstTimeLaunched_Preferences), firsttime);

        for(int i = 0; i < settings_caption.size(); i++){
            edit.putString(getString(R.string.settings_settingAt_name) + i, settings_caption.get(i));
        }

        edit.putInt(getString(R.string.settings_selectedSettingPreferences), selectedSetting);
        edit.apply();
    }

    private void saveSettingChanges(){
        for(int i = 0; i < settings_caption.size(); i++){
            /**
             * LOAD THE SETTING AT THE PREVIOUS INDEX
             */
            SharedPreferences currentSetting = getSharedPreferences(getString(R.string.settings_settingsAt) + settings_indexes.get(i), MODE_PRIVATE);

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
            getSharedPreferences(getString(R.string.permanentlines_lineSP) + settings_indexes.get(i), MODE_PRIVATE).edit().clear().apply();

            /**
             * AND SAVE THE VALUES AT THE NEW INDEX:
             */

            getSharedPreferences(getString(R.string.settings_settingsAt) + i, MODE_PRIVATE).edit()

                    .putBoolean(    getString(R.string.lines_unicolor_preferences),                     lines_unicolor)
                    .putBoolean(    getString(R.string.lines_permanent_preferences),                    lines_permanent)
                    .putBoolean(    getString(R.string.lines_drawBall_preferences),                     lines_drawBalls)
                    .putBoolean(    getString(R.string.lines_fadeComplete_preferences),                 lines_removeCompletely)
                    .putBoolean(    getString(R.string.lines_fadeSlowly_preferences),                   lines_fadeSlowly)
                    .putBoolean(    getString(R.string.lines_rainbowcolor_preferences),                 lines_drawInRainBowColor)
                    .putFloat(      getString(R.string.lines_width_preferences),                        lines_width)
                    .putFloat(      getString(R.string.lines_ballSize_preferences),                     lines_ballRadius)
                    .putInt(        getString(R.string.lines_unicolor_color_preferences),               lines_color)
                    .putInt(        getString(R.string.lines_fadeComplete_time_preferences),            lines_timeUntilCompletelyRemoved)
                    .putInt(        getString(R.string.lines_fadeActionTime_preferences),               lines_timeUntilActionPerformed)
                    .putInt(        getString(R.string.lines_rainbowcolorsteps_preferences),            lines_rainbowColorSteps)

                    .putBoolean(    getString(R.string.background_pictureshown_preferences),            background_drawPicture)
                    .putString(     getString(R.string.background_picturepath_preferences),             background_picturePath)
                    .putInt(        getString(R.string.background_alternateColor_preferences),          background_color)

                    .putBoolean(    getString(R.string.clock_drawClock_preferences),                    clk_drawClock)
                    .putBoolean(    getString(R.string.clock_simpleClockchosen_preferences),            clk_simpleClockchosen)
                    .putBoolean(    getString(R.string.clock_pointerOnlyClockchosen_preferences),       clk_pointerOnlychosen)
                    .putBoolean(    getString(R.string.clock_parabolaclockchosen_preferences),          clk_parabolaClockchosen)
                    .putBoolean(    getString(R.string.clock_digitalClockchosen_preferences),           clk_digitalClockchosen)
                    .putBoolean(    getString(R.string.clock_digitalClock_dotsblinking_preferences),    clk_digitalClock_dotsblinking)
                    .putFloat(      getString(R.string.clock_diameter_preferences),                     clk_diameter)
                    .putFloat(      getString(R.string.clock_xposition_preferences),                    clk_xPosition)
                    .putFloat(      getString(R.string.clock_yposition_preferences),                    clk_yPosition)
                    .putInt(        getString(R.string.clock_simpleclock_alphaColor_preferences),       clk_simpleClock_alpha)
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
        int length = getSharedPreferences(getString(R.string.settings_settingsPreferences), MODE_PRIVATE).getInt(getString(R.string.settings_settingsPreferencesLength), 0);
        for(int i = settings_caption.size(); i < length; i++){
            getSharedPreferences(getString(R.string.settings_settingsAt) + i, MODE_PRIVATE).edit().clear().apply();
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

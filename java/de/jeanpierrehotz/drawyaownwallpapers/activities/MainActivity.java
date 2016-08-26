package de.jeanpierrehotz.drawyaownwallpapers.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.prolificinteractive.swipeactionlayout.widget.ActionItem;
import com.prolificinteractive.swipeactionlayout.widget.SwipeActionLayout;

import java.util.ArrayList;
import java.util.Arrays;

import de.jeanpierrehotz.drawyaownwallpapers.R;
import de.jeanpierrehotz.drawyaownwallpapers.views.ColoredSnackbar;
import de.jeanpierrehotz.drawyaownwallpapers.views.RecyclerSettingsAdapter;
import de.jeanpierrehotz.drawyaownwallpapers.wallpaper.data.line.Line;

/**
 * Created by Admin on 16.06.2016.
 *
 */
public class MainActivity extends AppCompatActivity{

    private ArrayList<String> settings_caption;
    private ArrayList<Integer> settings_indexes;

    private boolean deleted;
    private int selectedSetting;

    private SwipeActionLayout swipeActionLayout;
    private int action; // 0 - edit; 1 - select; 2 - rename; 3 - delete

    private RecyclerView settingsList;
    private RecyclerSettingsAdapter settingsAdapter;
    private RecyclerView.LayoutManager settingsLayoutManager;

    private AlertDialog dialog;

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

            Intent firstLaunchIntent = new Intent(this, AppIntroActivity.class);
            firstLaunchIntent.putExtra(getString(R.string.appIntro_firstLaunch_setting), true);
            startActivity(firstLaunchIntent);
        }else{
            loadSettings();
        }

        initializeLayout();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_main);
        if(fab != null){
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
                                    EditText newNameET = (EditText) dialog.findViewById(R.id.dialog_newsettingname_newnameEditText);
                                    settings_caption.set(settings_caption.size() - 1, newNameET.getText().toString());

                                    settingsAdapter.notifyDataSetChanged();
                                    settingsList.smoothScrollToPosition(settings_caption.size() - 1);

                                    modifySetting(settings_caption.size() - 1);
                                }
                            })
                            .setNegativeButton(R.string.dialog_abort, new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i){
                                    settings_caption.remove(settings_caption.size() - 1);
                                }
                            })
                            .setView(R.layout.layout_dialog_neuertitel)
                            .show();

                }
            });
        }
    }

    public void showCurrentAction(){
        ColoredSnackbar.make(Color.WHITE, settingsList, getActionString(), Snackbar.LENGTH_LONG).show();
    }

    private String getActionString(){
        switch(action){
            case 0: return "Edit a setting";
            case 1: return "Select a setting";
            case 2: return "Rename a setting";
            case 3: return "Delete a setting";
            default: return "Fuck u homo!!";
        }
    }

    @Override
    protected void onResume(){
        super.onResume();

        settings_caption = new ArrayList<>();
        settings_indexes = new ArrayList<>();
        deleted = false;

        loadSettings();

        initializeLayout();

//        exampleListView = (ListView) findViewById(R.id.settingsListView);
//        ad = new SettingsAdapter(this, R.layout.layout_listitem_setting, settings_caption, selectedSetting);
//
//        exampleListView.setAdapter(ad);
//
//        exampleListView.setOnItemClickListener(changeSettingListener);
//        exampleListView.setOnItemLongClickListener(createContextMenuListener);
    }

    private void initializeLayout(){
        settingsList = (RecyclerView) findViewById(R.id.settingsList);
        settingsLayoutManager = new LinearLayoutManager(this);
        settingsList.setLayoutManager(settingsLayoutManager);

        swipeActionLayout = (SwipeActionLayout) findViewById(R.id.swipe_action_layout);

        settingsAdapter = new RecyclerSettingsAdapter(this, settings_caption, selectedSetting);
        settingsList.setAdapter(settingsAdapter);

        settingsAdapter.setOnItemClickListener(new RecyclerSettingsAdapter.OnItemClickListener(){
            @Override
            public void onItemClicked(RecyclerSettingsAdapter.ViewHolder vh, int pos){
                switch(action){
                    case 0:
                        modifySetting(pos);
                        break;
                    case 1:
                        selectSetting(pos);
                        break;
                    case 2:
                        renameSettings(pos);
                        break;
                    case 3:
                        deleteSetting(pos);
                        break;
                }
            }
        });

        swipeActionLayout.setOnActionSelectedListener(new SwipeActionLayout.OnActionListener(){
            @Override
            public void onActionSelected(int index, ActionItem act){
                action = index;
                showCurrentAction();
            }
        });

        swipeActionLayout.populateActionItems(Arrays.asList(
                new ActionItem(R.drawable.ic_edit),
                new ActionItem(R.drawable.ic_select),
                new ActionItem(R.drawable.ic_rename),
                new ActionItem(R.drawable.ic_delete)
        ));
    }

    private void modifySetting(int i){
        Intent intent = new Intent(this, ChangeSettingsActivity.class);
        intent.putExtra(getString(R.string.intent_settings_index), i);
        startActivity(intent);
    }

    private void selectSetting(int i){
        settingsAdapter.notifySelectedChanged(selectedSetting = i);
    }

    private void renameSettings(final int i){
        dialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle(R.string.selectTitle_caption)
                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int hay){
                        EditText newNameET = (EditText) dialog.findViewById(R.id.dialog_newsettingname_newnameEditText);
                        settings_caption.set(i, newNameET.getText().toString());
                        settingsAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton(R.string.dialog_abort, null)
                .setView(R.layout.layout_dialog_neuertitel)
                .show();

        EditText newNameET = (EditText) dialog.findViewById(R.id.dialog_newsettingname_newnameEditText);
        newNameET.setText(settings_caption.get(i));
    }

    private void deleteSetting(int i){
        if(i == selectedSetting){
            dialog = new AlertDialog.Builder(MainActivity.this)
                    .setTitle(R.string.deleteChosenSetting_caption)
                    .setMessage(R.string.deleteChosenSetting_message)
                    .setPositiveButton(R.string.dialog_ok, null)
                    .setNegativeButton(R.string.dialog_fuckyou, new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i){
                            ColoredSnackbar.make(Color.WHITE, settingsList, R.string.dialog_fuckyou_answer, Snackbar.LENGTH_INDEFINITE).show();
                        }
                    })
                    .show();
        }else{
            settings_caption.remove(i);
            settings_indexes.remove(i);
            deleted = true;

            if(i < selectedSetting){
                settingsAdapter.notifySelectedChanged(--selectedSetting);
            }else{
                settingsAdapter.notifyDataSetChanged();
            }
        }

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
            int         lines_eatingdirection               = currentSetting.getInt(getString(R.string.lines_eatingitselfdirection_preferences), Line.EatingDirection.behind.toInteger());

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

            boolean     visualizer_showVis                  = currentSetting.getBoolean(getString(R.string.visualizer_showVisualizer_preferences), false);
            boolean     visualizer_vistype_waveform         = currentSetting.getBoolean(getString(R.string.visualizer_visualizationtype_waveform_preferences), true);
            boolean     visualizer_vistype_fft              = currentSetting.getBoolean(getString(R.string.visualizer_visualizationtype_fft_preferences), false);
            boolean     visualizer_proximity                = currentSetting.getBoolean(getString(R.string.visualzer_proximityvisualizerchosen_preferences), true);
            boolean     visualizer_linear                   = currentSetting.getBoolean(getString(R.string.visualzer_linearvisualizerchosen_preferences), false);
            float       visualizer_diameter                 = currentSetting.getFloat(getString(R.string.visualizer_diameter_preferences), 0.8f);
            float       visualizer_xpos                     = currentSetting.getFloat(getString(R.string.visualizer_xposition_preferences), 0.5f);
            float       visualizer_ypos                     = currentSetting.getFloat(getString(R.string.visualizer_yposition_preferences), 0.5f);
            int         visualizer_color                    = currentSetting.getInt(getString(R.string.visualizer_visualizationcolor_preferences), Color.WHITE);

            ArrayList<Line> permLines                       = Line.loadFromSharedPreferences(getSharedPreferences(getString(R.string.permanentlines_lineSP) + settings_indexes.get(i), MODE_PRIVATE), MainActivity.this);

            /*
            audiovisualizer.setColor(prefs.getInt(getString(R.string.visualizer_visualizationcolor_preferences), Color.WHITE));
             */

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
                    .putInt(        getString(R.string.lines_eatingitselfdirection_preferences),        lines_eatingdirection)

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

                    .putBoolean(    getString(R.string.visualizer_showVisualizer_preferences),          visualizer_showVis)
                    .putBoolean(    getString(R.string.visualizer_visualizationtype_fft_preferences),   visualizer_vistype_fft)
                    .putBoolean(    getString(R.string.visualizer_visualizationtype_waveform_preferences),visualizer_vistype_waveform)
                    .putBoolean(    getString(R.string.visualzer_proximityvisualizerchosen_preferences),visualizer_proximity)
                    .putBoolean(    getString(R.string.visualzer_linearvisualizerchosen_preferences),   visualizer_linear)
                    .putFloat(      getString(R.string.visualizer_diameter_preferences),                visualizer_diameter)
                    .putFloat(      getString(R.string.visualizer_xposition_preferences),               visualizer_xpos)
                    .putFloat(      getString(R.string.visualizer_yposition_preferences),               visualizer_ypos)
                    .putInt(        getString(R.string.visualizer_visualizationcolor_preferences),      visualizer_color)

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
        if(id == R.id.menu_main_reviewappintro){
            Intent reviewIntroIntent = new Intent(this, AppIntroActivity.class);
            reviewIntroIntent.putExtra(getString(R.string.appIntro_firstLaunch_setting), false);
            startActivity(reviewIntroIntent);

            return true;
        }else if(id == R.id.menu_main_about){
            startActivity(new Intent(this, AboutActivity.class));
            return true;
        }
//        else if(id == R.id.menu_main_show_material){
//            startActivity(new Intent(this, ChangeSettingsMaterialTryHard.class));
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
}

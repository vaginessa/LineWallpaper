package de.jeanpierrehotz.drawyaownwallpapers.activities;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

import de.jeanpierrehotz.drawyaownwallpapers.R;

/**
 * Created by Admin on 16.06.2016.
 */
public class AppIntroActivity extends AppIntro{

    /*
     * Since I've gotten too lazy to keep two AppIntros running I just save whether it's
     * started from the first launch of the app.
     */
    private boolean firstLaunch;

    @Override
    public void init(@Nullable Bundle savedInstanceState){
        firstLaunch = getIntent().getBooleanExtra(getString(R.string.appIntro_firstLaunch_setting), false);

        if(firstLaunch)
            showSkipButton(false);

        /* The slide which shows you how to change a setting */
        addSlide(
                AppIntroFragment.newInstance(
                        getString(R.string.appIntro_changeSettings_caption),
                        getString(R.string.appIntro_changeSettings_description),
                        R.drawable.appintro_changesettingspicture,
                        0xFF3F51B5
                )
        );
        /* The slide which shows you how to add a setting */
        addSlide(
                AppIntroFragment.newInstance(
                        getString(R.string.appIntro_addSettings_caption),
                        getString(R.string.appIntro_addSettings_description),
                        R.drawable.appintro_addsettingpicture,
                        0xFF3F51B5
                )
        );
        /* The slide which shows you how to open the context menu */
        addSlide(
                AppIntroFragment.newInstance(
                        getString(R.string.appIntro_contextMenu_caption),
                        getString(R.string.appIntro_contextMenu_description),
                        R.drawable.appintro_contextmenupicture,
                        0xFF3F51B5
                )
        );
        /* The slide which tells you about the background image */ // writestorage - 4
        addSlide(
                AppIntroFragment.newInstance(
                        getString(R.string.appIntro_backgroundImage_caption),
                        getString(R.string.appIntro_backgroundImage_description),
                        R.drawable.appintro_backgroundimagepicture,
                        0xFF3F51B5
                )
        );
        /* The slide which tells you about the visualizers */ // audio and shit - 5
        addSlide(
                AppIntroFragment.newInstance(
                        getString(R.string.appIntro_visualizer_caption),
                        getString(R.string.appIntro_visualizer_description),
                        R.drawable.appintro_visualizerpicture,
                        0xFF3F51B5
                )
        );
        /* The slide which shows you that you're done with the intro */
        addSlide(
                AppIntroFragment.newInstance(
                        getString(R.string.appIntro_doneWithIntro_caption),
                        getString((firstLaunch)? R.string.appIntro_doneWithIntro_description: R.string.appIntro_review_doneWithIntro_description),
                        R.drawable.appintro_donewithintropicture,
                        0xFF3F51B5
                )
        );

        /* Ask for the needed permissions in the app */
        askForPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 4);
        askForPermissions(new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.MODIFY_AUDIO_SETTINGS}, 5);

        /* Make it look all nice */
        setBarColor(0xFF303F9F);
        showStatusBar(false);
        setZoomAnimation();
    }

    @Override
    public void onSkipPressed(){
        /* when skipping we just start the first setting */
        startFirstSetting();
    }

    @Override
    public void onNextPressed(){

    }

    @Override
    public void onDonePressed(){
        /* when done we just start the first setting */
        startFirstSetting();
    }

    @Override
    public void onSlideChanged(){

    }

    /**
     * This method starts the first setting if this activity was started as a result of the
     * first app-launch
     */
    private void startFirstSetting(){
        if(firstLaunch){
            Intent intent = new Intent(this, de.jeanpierrehotz.drawyaownwallpapers.activities.ChangeSettingsActivity.class);
            intent.putExtra(getString(R.string.intent_settings_index), 0);  //we need to give it the settings index
            startActivity(intent);
        }

        finish();                                                           // we don't want the user to come back here
    }
}
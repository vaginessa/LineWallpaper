package de.jeanpierrehotz.drawyaownwallpapers.activities;

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
    @Override
    public void init(@Nullable Bundle savedInstanceState){
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
        /* The slide which shows you that you're done with the intro */
        addSlide(
                AppIntroFragment.newInstance(
                        getString(R.string.appIntro_doneWithIntro_caption),
                        getString(R.string.appIntro_doneWithIntro_description),
                        R.drawable.appintro_donewithintropicture,
                        0xFF3F51B5
                )
        );

        /* MAke it look all nice */
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
     * This method starts the first setting
     */
    private void startFirstSetting(){
        Intent intent = new Intent(this, de.jeanpierrehotz.drawyaownwallpapers.activities.ChangeSettingsActivity.class);
        intent.putExtra(getString(R.string.intent_settings_index), 0);  //we need to give it the settings index
        startActivity(intent);
        finish();                                                       // we don't want the user to come back here
    }
}
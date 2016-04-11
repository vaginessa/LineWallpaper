package de.jeanpierrehotz.drawyaownwallpapers;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

/**
 *
 * Created by Admin on 11.04.2016.
 */
public class AppIntroActivity extends AppIntro{
    @Override
    public void init(@Nullable Bundle savedInstanceState){
        addSlide(
                AppIntroFragment.newInstance(
                        getString(R.string.appIntro_changeSettings_caption),
                        getString(R.string.appIntro_changeSettings_description),
                        R.drawable.appintro_changesettingspicture,
                        0xFF3F51B5
                )
        );
        addSlide(
                AppIntroFragment.newInstance(
                        getString(R.string.appIntro_addSettings_caption),
                        getString(R.string.appIntro_addSettings_description),
                        R.drawable.appintro_addsettingpicture,
                        0xFF3F51B5
                )
        );
        addSlide(
                AppIntroFragment.newInstance(
                        getString(R.string.appIntro_contextMenu_caption),
                        getString(R.string.appIntro_contextMenu_description),
                        R.drawable.appintro_contextmenupicture,
                        0xFF3F51B5
                )
        );
        addSlide(
                AppIntroFragment.newInstance(
                        getString(R.string.appIntro_doneWithIntro_caption),
                        getString(R.string.appIntro_doneWithIntro_description),
                        R.drawable.appintro_donewithintropicture,
                        0xFF3F51B5
                )
        );

        setBarColor(0xFF303F9F);
        showStatusBar(false);
        setZoomAnimation();
    }

    @Override
    public void onSkipPressed(){
        startFirstSetting();
    }

    @Override
    public void onNextPressed(){

    }

    @Override
    public void onDonePressed(){
        startFirstSetting();
    }

    @Override
    public void onSlideChanged(){

    }

    private void startFirstSetting(){
        Intent intent = new Intent(this, ChangeSettingsActivity.class);
        intent.putExtra(getString(R.string.intent_settings_index), 0);
        startActivity(intent);
        finish();
    }
}

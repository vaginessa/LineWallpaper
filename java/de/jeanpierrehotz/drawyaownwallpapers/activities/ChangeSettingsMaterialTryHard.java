package de.jeanpierrehotz.drawyaownwallpapers.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.yarolegovich.mp.MaterialChoicePreference;
import com.yarolegovich.mp.MaterialPreferenceScreen;
import com.yarolegovich.mp.MaterialRightIconPreference;

import java.io.File;
import java.util.Arrays;

import de.jeanpierrehotz.drawyaownwallpapers.R;
import de.jeanpierrehotz.drawyaownwallpapers.utils.WallpaperPictureSelector;
import de.jeanpierrehotz.drawyaownwallpapers.views.ColoredSnackbar;

/**
 * Created by Admin on 08.07.2016.
 *
 */
public class ChangeSettingsMaterialTryHard extends AppCompatActivity{

    private WallpaperPictureSelector mImageSelector;
    private WallpaperPictureSelector.Callback mImageSelectorCallback = new WallpaperPictureSelector.Callback(){
        @Override
        public void onSelectedResult(String file){
            ColoredSnackbar.make(Color.WHITE, mPreferenceScreen, getString(R.string.bg_image_youselected) + file, Snackbar.LENGTH_SHORT).show();
        }

        @Override
        public void onCropperResult(WallpaperPictureSelector.CropResult result, File srcFile, File outFile){
            if(result == WallpaperPictureSelector.CropResult.success){
                mImageView.setImageDrawable(Drawable.createFromPath(outFile.getAbsolutePath()));

                // getSharedPreferences(getString(R.string.settings_settingsAt) + 1/*TODO: settings_index*/, MODE_PRIVATE).edit().putString(getString(R.string.background_picturepath_preferences), outFile.getAbsolutePath()).apply();
                ColoredSnackbar.make(Color.WHITE, mPreferenceScreen, R.string.bg_image_youcropped, Snackbar.LENGTH_SHORT).show();
            }else{
                ColoredSnackbar.make(Color.WHITE, mPreferenceScreen, R.string.bg_image_abortedSelecting, Snackbar.LENGTH_SHORT).show();
            }
        }
    };

    private MaterialPreferenceScreen mPreferenceScreen;
    private ImageView mImageView;
    private MaterialRightIconPreference mRightIconPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_change_settings_materialtrihard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mImageSelector = new WallpaperPictureSelector(this);
        mImageSelector.setCallback(mImageSelectorCallback);

        mImageView = (ImageView) findViewById(R.id.pictureviewbla);

        mPreferenceScreen = (MaterialPreferenceScreen) findViewById(R.id.preferenceScreen);
        mPreferenceScreen.setVisibilityController(R.id.line_samecolor_checkbox, Arrays.asList(R.id.line_samecolor_colorpicker), true);
        mPreferenceScreen.setVisibilityController(R.id.line_rainbowgradient_checkbox, Arrays.asList(R.id.line_rainbowgradient_seekbar), true);
        mPreferenceScreen.setVisibilityController(R.id.lines_savelines_checkbox, Arrays.asList(R.id.lines_savelines_deletelast, R.id.lines_savelines_deleteall, R.id.pictureviewbla), true);
        mPreferenceScreen.setVisibilityController(R.id.lines_drawballs_checkbox, Arrays.asList(R.id.lines_drawballs_width_seekbar), true);
        mPreferenceScreen.setVisibilityController(R.id.lines_savelines_checkbox, Arrays.asList(R.id.lines_disappearance_choice), false);


        mRightIconPreference = (MaterialRightIconPreference) findViewById(R.id.lines_savelines_deleteall);
        mRightIconPreference.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                // selectPhoto(view);
                mImageSelector.selectImage(ChangeSettingsMaterialTryHard.this);
            }
        });


        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        mImageSelector.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        mImageSelector.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        mImageSelector.onSaveInstanceState(outState);
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

package de.jeanpierrehotz.drawyaownwallpapers.views;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by Admin on 02.07.2016.
 */
public class ColoredSnackbar{
    public static Snackbar make(int background, View v, int id, int length){
        Snackbar bar = Snackbar.make(v, id, length);
        bar.getView().setBackgroundColor(background);
        return bar;
    }

    public static Snackbar make(int background, View v, String text, int length){
        Snackbar bar = Snackbar.make(v, text, length);
        bar.getView().setBackgroundColor(background);
        return bar;
    }
}

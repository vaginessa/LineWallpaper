package de.jeanpierrehotz.drawyaownwallpapers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Admin on 05.04.2016.
 */
public class SettingsAdapter extends ArrayAdapter<String>{
    public SettingsAdapter(Context context, int resource, List<String> objects){
        super(context, resource, objects);
    }

    private int selected;

    public SettingsAdapter(Context context, int resource, List<String> objects, int selected){
        super(context, resource, objects);
        this.selected = selected;
    }

    public void selectedChangedTo(int selected){
        this.selected = selected;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View v = LayoutInflater.from(getContext()).inflate(R.layout.listitem_setting, parent, false);

        TextView nameView = (TextView) v.findViewById(R.id.setting_name_textview);
        nameView.setText(getItem(position));

        TextView selectedView = (TextView) v.findViewById(R.id.setting_selected_textview);
        if(position == selected){
            selectedView.setText("Selected");
        }else{
            selectedView.setText("");
        }

        return v;
    }
}

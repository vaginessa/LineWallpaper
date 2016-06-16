package de.jeanpierrehotz.drawyaownwallpapers.views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import de.jeanpierrehotz.drawyaownwallpapers.R;

/**
 * Created by Admin on 16.06.2016.
 */
public class RecyclerSettingsAdapter extends RecyclerView.Adapter<RecyclerSettingsAdapter.ViewHolder>{

    private static Context c;

    private ArrayList<String> captions;
    private int selected;

    public RecyclerSettingsAdapter(Context ctx, ArrayList<String> captions, int selected){
        c = ctx;

        this.captions = captions;
        this.selected = selected;
    }

    public void notifySelectedChanged(int sel){
        this.selected = sel;
        this.notifyDataSetChanged();
    }

    private OnItemClickListener clickListener;

//    private OnItemLongClickListener longclicklistener;

    public void setOnItemClickListener(OnItemClickListener listener){
        clickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_listitem_setting, parent, false);

        final ViewHolder vh = new ViewHolder(v);

        v.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(clickListener != null){
                    clickListener.onItemClicked(vh, vh.getNumber());
                }
            }
        });

//        v.setOnLongClickListener(new View.OnLongClickListener(){
//            @Override
//            public boolean onLongClick(View view){
//                if(longclicklistener != null){
//                    return longclicklistener.onItemLongClicked(vh, vh.getNumber());
//                }
//                return false;
//            }
//        });

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        holder.setCaption(captions.get(position));
        holder.setSelected(selected, position);
    }

    @Override
    public int getItemCount(){
        return captions.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private RelativeLayout rootView;

        private TextView captionTextView;
        private TextView selectedTextView;
        private int position;

        public int getNumber(){
            return position;
        }

        public ViewHolder(View itemView){
            super(itemView);

            rootView = (RelativeLayout) itemView.findViewById(R.id.rootlayout);

            captionTextView = (TextView) itemView.findViewById(R.id.setting_name_textview);
            selectedTextView = (TextView) itemView.findViewById(R.id.setting_selected_textview);
        }

        public void setCaption(String capt){
            captionTextView.setText(capt);
        }

        public void setSelected(int sel, int num){
            position = num;
            if(sel == num){
                selectedTextView.setText("Selected");
            }else{
                selectedTextView.setText("");
            }
        }
    }

    public interface OnItemClickListener{
        void onItemClicked(ViewHolder vh, int pos);
    }

//    public interface OnItemLongClickListener{
//        boolean onItemLongClicked(ViewHolder vh, int pos);
//    }
}

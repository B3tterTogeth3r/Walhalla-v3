package de.b3ttertogeth3r.walhalla.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import de.b3ttertogeth3r.walhalla.design.RowImageItem;
import de.b3ttertogeth3r.walhalla.models.Image;

public class MyImageItem extends BaseAdapter {
    private static final String TAG = "MyImageItemAdapter";
    private Context context;
    private ArrayList<Image> imageList;

    public MyImageItem (Context context, ArrayList<Image> imageList){
        this.context = context;
        this.imageList = imageList;
    }

    @Override
    public int getCount () {
        return imageList.size();
    }

    @Override
    public Object getItem (int position) {
        return imageList.get(position);
    }

    @Override
    public long getItemId (int position) {
        return position;
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent) {
        RowImageItem row = new RowImageItem(context);
        Image image = imageList.get(position);
        row.setText(image.getDescription())
                .setImage(image.getIcon_path());
        return row;
    }
}

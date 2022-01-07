package de.b3ttertogeth3r.walhalla.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import de.b3ttertogeth3r.walhalla.design.RowImageItem;
import de.b3ttertogeth3r.walhalla.firebase.Firebase;
import de.b3ttertogeth3r.walhalla.models.Image;
import de.b3ttertogeth3r.walhalla.models.Person;

public class MyPersonItem extends BaseAdapter {
    private static final String TAG = "MyImageItemAdapter";
    private Context context;
    private ArrayList<Person> personList;

    public MyPersonItem (Context context, ArrayList<Person> imageList){
        this.context = context;
        this.personList = imageList;
    }

    @Override
    public int getCount () {
        return personList.size();
    }

    @Override
    public Object getItem (int position) {
        return personList.get(position);
    }

    @Override
    public long getItemId (int position) {
        return position;
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent) {
        RowImageItem row = new RowImageItem(context);
        Person person = personList.get(position);
        row.setText(person.getFullName())
                .setImage(person.getPicture_path());
        return row;
    }
}

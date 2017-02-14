package com.example.cba.shoppinglist;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Custom ArrayAdapter som tar in 2 st Arraylists i konstruktören istället för 1.
 * Så att färg värdena kan användas här och sätta
 * setBackgroundColor attributet på TextViewen.
 */

public class ListItemAdapter extends ArrayAdapter<String> {
    private Context context;
    private ArrayList<String> items;
    private ArrayList<String> colors;

    public ListItemAdapter(Context context, ArrayList<String> items, ArrayList<String> colors) {
        super(context, R.layout.list_item_view, items);
        this.context = context;
        this.items = items;
        this.colors= colors;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_item_view, parent, false);

        TextView twItems = (TextView) rowView.findViewById(R.id.list_item);
        twItems.setText(items.get(position));
        twItems.setBackgroundColor(Color.parseColor(colors.get(position)));

        return rowView;
    }

}
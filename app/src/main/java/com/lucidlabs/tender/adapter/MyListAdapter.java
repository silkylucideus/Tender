package com.lucidlabs.tender.adapter;

import android.app.Activity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lucidlabs.tender.R;


import java.util.ArrayList;
import java.util.List;

public class MyListAdapter extends ArrayAdapter<String> {

    private ArrayList<String> dataSet;

    private Integer[] imageId;
    Context mContext;

//    private static class ViewHolder {
//        TextView txtVersion;
//        ImageView info;
//    }


    public MyListAdapter(ArrayList<String> data, Integer[] imageId, Context context) {
        super(context, R.layout.account_list_detail, data);
        this.dataSet = data;
        this.mContext=context;
        this.imageId = imageId;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View v = inflater.inflate(R.layout.account_list_detail, null);


        TextView txtVersion = v.findViewById(R.id.title);
        ImageView info = v.findViewById(R.id.icon);



            // getting respective string val from list
            txtVersion.setText(dataSet.get(position));
            info.setImageResource(imageId[position]);







        return v;
    }



}
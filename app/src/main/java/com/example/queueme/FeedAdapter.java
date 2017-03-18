package com.example.queueme;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by anders on 21.02.2017.
 */

public class FeedAdapter extends ArrayAdapter {
    private static final String TAG = "FeedAdapter";
    private final int layoutResourse;
    private final LayoutInflater layoutInflater;
    private List<Person> applications;

    //final cant be changed. only when decleared


    public FeedAdapter(Context context, int resource, List<Person> applocations) {
        super(context, resource);
        this.layoutResourse =resource;
        this.layoutInflater =LayoutInflater.from(context);
        this.applications = applocations;
    }

    @Override
    public int getCount() {
        return applications.size();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;


        if(convertView==null){
            convertView = layoutInflater.inflate(layoutResourse,parent,false);

            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


       // TextView tvname = (TextView) convertView.findViewById(tvname);
       // TextView tvemail =(TextView) convertView.findViewById(R.id.tvemail);

        Person currentApp =applications.get(position);

        viewHolder.tvname.setText(currentApp.getName());
        viewHolder.tvemail.setText(currentApp.getEmail());
        return convertView;
    }
    private class ViewHolder{
        final TextView tvname;
        final TextView tvemail;
        ViewHolder(View v){
            this.tvname=(TextView) v.findViewById(R.id.tvEmnenavn);
            this.tvemail=(TextView) v.findViewById(R.id.tvemail);

        }
    }
}

package com.example.queueme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by magnusknaedal on 14.03.2017.
 */

public class FeedAdapter_ChoosePerson extends ArrayAdapter {


    private static final String TAG= "FeedAdapter_ChoosePerson";
    private final int layoutResource;
    private final LayoutInflater layoutInflater;
    private List<Person> persons;


    public FeedAdapter_ChoosePerson(Context context, int resource, List<Person> persons){
        super(context, resource);
        this.layoutResource = resource;
        this.layoutInflater = LayoutInflater.from(context);
        this.persons = persons;
    }

    @Override
    public int getCount() {
        return persons.size();
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View view = layoutInflater.inflate(layoutResource, parent, false);


        TextView name = (TextView) view.findViewById(R.id.tvname);
       // TextView email = (TextView) view.findViewById(R.id.tvemail);
        TextView time = (TextView) view.findViewById(R.id.time_until);

        Person currentApp = persons.get(position);


        name.setText(currentApp.getName());
        //email.setText(currentApp.getEmail());
        time.setText(currentApp.getTime_to_stop());


        return view;
    }

}

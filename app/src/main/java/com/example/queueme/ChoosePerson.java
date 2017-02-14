package com.example.queueme;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChoosePerson extends AppCompatActivity {

    private ListView l;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_person);

        l=(ListView) findViewById(R.id.listview);

        final ArrayList<Person> persons = new ArrayList<Person>();





        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.child("Person").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //get all of the children of this level.
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                //shake hands with each of them
                for (DataSnapshot child: children){
                    Person person = child.getValue(Person.class);
                    persons.add(person);


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Make arrayadapter t show our result
        ArrayAdapter<Person> personadapter = new ArrayAdapter<Person>(getApplication(),android.R.layout.simple_list_item_1,persons);

        //set the person list in the fragment
        l.setAdapter(personadapter);

    }
}

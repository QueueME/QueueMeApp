package com.example.queueme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChooseSubjectAss extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_subject_ass);


        final ListView l=(ListView) findViewById(R.id.listview_ass);

        final ArrayList<Subject> subjects = new ArrayList<Subject>();



        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();
        myRef.child("Subject").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //get all of the children of this level.
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                //shake hands with each of them
                for (DataSnapshot child: children){
                    Subject subject = child.getValue(Subject.class);
                    subjects.add(subject);

                }

                //Make arrayadapter t show our result
                //final ArrayAdapter<Person> personadapter = new ArrayAdapter<Person>(ChoosePerson.this, android.R.layout.simple_list_item_1,persons);

                //set the person list in the fragment
                //l.setAdapter(personadapter);

                //ArrayAdapter feedAdapter = new ArrayAdapter(CooseSubjectAss.this, android.R.layout.simple_list_item_1,subjects);
                //l.setAdapter(feedAdapter);

                FeedAdapter_ChooseSubject_Ass Adapter = new FeedAdapter_ChooseSubject_Ass(ChooseSubjectAss.this, R.layout.list_subjectitem_ass, subjects);
                l.setAdapter(Adapter);

                l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Subject subject = (Subject) subjects.get(position);
                        Intent moveToDetailIntent = new Intent(ChooseSubjectAss.this, StartSession.class);
                        // moveToDetailIntent.putExtra("bkjb", );
                        String emnekode= subject.getEmnekode();
                        String emnenavn = subject.getEmnenavn();


                        moveToDetailIntent.putExtra("emnekode",emnekode);
                        moveToDetailIntent.putExtra("emnenavn",emnenavn);


                        //startActivityForResult(moveToDetailIntent,position);
                        //Person Anders = new Person();
                        //Anders.setName("nonneanders");
                        //person.getPersons().add(Anders);
                        startActivity(moveToDetailIntent);


                    }
                });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
}
//heihei
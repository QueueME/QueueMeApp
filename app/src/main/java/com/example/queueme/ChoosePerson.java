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

public class ChoosePerson extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_person);

       final ListView l=(ListView) findViewById(R.id.listview);

        final ArrayList<Person> persons = new ArrayList<Person>();





        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();
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

                //Make arrayadapter t show our result
                //final ArrayAdapter<Person> personadapter = new ArrayAdapter<Person>(ChoosePerson.this, android.R.layout.simple_list_item_1,persons);

                //set the person list in the fragment
                //l.setAdapter(personadapter);
                FeedAdapter feedAdapter =new FeedAdapter(ChoosePerson.this,R.layout.list_person, persons);
                l.setAdapter(feedAdapter);
                l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Person person = (Person) persons.get(position);
                        Intent moveToDetailIntent = new Intent(ChoosePerson.this, DetailedActivity.class);
                        // moveToDetailIntent.putExtra("bkjb", );
                        String name= person.getName().toString();
                        String email = person.getEmail().toString();




                        moveToDetailIntent.putExtra("name",name);
                        moveToDetailIntent.putExtra("email",email);


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

   /* @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Person person = (Person) parent.getItemAtPosition(position);
        Intent moveToDetailIntent = new Intent(this, DetailedActivity.class);
        // moveToDetailIntent.putExtra("bkjb", );
        String name= person.getName().toString();
        String email = person.getEmail().toString();

        moveToDetailIntent.putExtra("name",name);

        //startActivityForResult(moveToDetailIntent,position);


        startActivity(moveToDetailIntent);






            }
*/
}

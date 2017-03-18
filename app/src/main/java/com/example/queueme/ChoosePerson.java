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

    private String emnekode;
    private String emnenavn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_person);

        //finner listview
        final ListView l =(ListView) findViewById(R.id.list_persons);

        final ArrayList<Person> persons = new ArrayList<Person>();
        //henter ut info fra forrige side

        Intent intent = getIntent();
        emnenavn = intent.getStringExtra("emnenavn");
        emnekode  = intent.getStringExtra("emnekode");

        //Henter ut perosnene fra databasen og legger dem i persons listen
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();
        myRef.child("Subject").child(emnekode).child("StudAssList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //get all of the children of this level.
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                //shake hands with each of them
                for (DataSnapshot child: children){
                    Person person = child.getValue(Person.class);
                    persons.add(person);
                }



                //lager et adapter

                FeedAdapter_ChoosePerson feedAdapter = new FeedAdapter_ChoosePerson(ChoosePerson.this, R.layout.list_subjectitem_person, persons);
                l.setAdapter(feedAdapter);
                //definerer hva som skal skje når man trykker på en person
                l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Person person = (Person) persons.get(position);
                        Intent moveToDetailIntent = new Intent(ChoosePerson.this, DetailedActivity.class);


                        String email = person.getEmail().toString();
                        String uid = person.getUid().toString();



                        moveToDetailIntent.putExtra("email",email);
                        moveToDetailIntent.putExtra("uid",uid);
                        moveToDetailIntent.putExtra("emnekode",emnekode);
                        moveToDetailIntent.putExtra("emnenavn",emnenavn);

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

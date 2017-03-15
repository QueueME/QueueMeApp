package com.example.queueme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class InQueue extends AppCompatActivity {
    private String email;
    private String personuid;
    private String emnenavn;
    private String emnekode;
    private TextView count;
    private TextView nrinline;
    private TextView emneinfo;
    private ArrayList<Person> students = new ArrayList<Person>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_queue);


        //henter info fra forrige activity
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        personuid = intent.getStringExtra("uid");
        emnenavn = intent.getStringExtra("emnenavn");
        emnekode = intent.getStringExtra("emnekode");

        //finner textview
        count = (TextView) findViewById(R.id.count);
        nrinline = (TextView) findViewById(R.id.nrInLine);
        emneinfo=(TextView)findViewById(R.id.emneinfo);
        emneinfo.setText(emnekode + " " + emnenavn);


        //lager en referanse/kobling  til databasen vår
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Subject");

        //henter ut alle som er i lsiten og legger dem i vår liste. Dette er fordi childeventlistener ikke kjøres i starten, og vi trenger listen med en gang.
        myRef.child(emnekode).child("StudAssList").child(personuid).child("Queue").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child: children){
                    Person person = child.getValue(Person.class);
                    students.add(person);



                }
                //setter teksten i texview
                count.setText(""+ linecount()+"");
                nrinline.setText("" + nrInline() + "");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //setter på en listener slik at vi appen blir opdatert på endringer automatisk og definerer hva som skal skje i de forskjellige tilfellene
        myRef.child(emnekode).child("StudAssList").child(personuid).child("Queue").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //henter elementer som ble lagt til
                fetchData(dataSnapshot);
                //hvis vi plutselig er først i kø skal vi få beskjed
                if (nrInline() == 0) {
                    nrinline.setText("ITS YOUR TURN");
                    //bytte layoutfarge og lage et checkmerke
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                fetchDataDelete(dataSnapshot);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void fetchData(DataSnapshot dataSnapshot) {
        //students.clear();
        Person person = dataSnapshot.getValue(Person.class);
        students.add(person);
    }

    private void fetchDataDelete(DataSnapshot dataSnapshot) {
        //students.clear();
        Person person = dataSnapshot.getValue(Person.class);
        students.remove(person);
    }

    private int linecount() {
        return students.size();
    }

    private int nrInline() {
        int index=10000;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        for (Person person : students) {
            if (person.getUid() == user.getUid()) {
                index = students.indexOf(person);

            }
        }
        return index;


    }
}

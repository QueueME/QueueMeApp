package com.example.queueme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.queueme.MySessionSwipeFunction.ScreenSlidePagerActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StartSession extends AppCompatActivity implements View.OnClickListener {

    private String emnekode;
    //private int queuenr;
    private String emnenavn;
    //private TextView antall;
    private Button queue;
    private EditText time;
    private  TextView subject;

    private String myUID;

    private ArrayList<Person> persons = new ArrayList<Person>();
    private Person Me;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_session);
        //henter brukerens info
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        myUID=user.getUid();
        //henter info fra forrige side

        Intent intent = getIntent();
        emnenavn = intent.getStringExtra("emnenavn");
        emnekode = intent.getStringExtra("emnekode");
        //finner knapper
        queue = (Button) findViewById(R.id.queue);
        queue.setOnClickListener(this);
        //setter tekst
        time = (EditText) findViewById(R.id.time_up_to);
        subject= (TextView) findViewById(R.id.subject);
        subject.setText(emnekode +" "+ emnenavn);


        //henter til liste
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

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
        });




    }



    private void QueueMe(){

        //subject.setText(persons.get(0).getUid());



        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        int index=0;
        for (Person person : persons) {
            if (person.getUid() == user.getUid()) {
                index = persons.indexOf(person);
            }
        }


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Person");
        myRef.child(persons.get(index).getUid()).child("time_to_stop").setValue( time.getText().toString());
        persons.get(index).setTime_to_stop(time.getText().toString());
        DatabaseReference myRef2 = database.getReference("Subject");
        myRef2.child(emnekode).child("StudAssList").child(user.getUid()).setValue(persons.get(index));


        Intent moveToDetailIntent = new Intent(StartSession.this,ScreenSlidePagerActivity.class);
        moveToDetailIntent.putExtra("emnekode",emnekode);
        moveToDetailIntent.putExtra("emnenavn",emnenavn);
        startActivity(moveToDetailIntent);
        finish();

    }


    @Override
    public void onClick(View v) {
        if (v==queue){
            QueueMe();
        }
    }
}

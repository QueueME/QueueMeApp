package com.example.queueme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
    private Button meny;
    private Button home;
    private String myName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startsession);

        Window window = StartSession.this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(StartSession.this,R.color.btn_logut_bg));

        meny = (Button) findViewById(R.id.meny);
        meny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartSession.this, MenyActivity.class));

            }
        });
        home = (Button) findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartSession.this, StudOrAss.class));

            }
        });
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

        DatabaseReference personRef = database.getReference("Person");
        personRef.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Person person = dataSnapshot.getValue(Person.class);
                String firstInLineName = person.getName();
                myName=firstInLineName;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }



    private void QueueMe(){

        //subject.setText(persons.get(0).getUid());



        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();



        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Person");
        myRef.child(user.getUid()).child("time_to_stop").setValue( time.getText().toString());
        Person person = new Person();
        person.setTime_to_stop(time.getText().toString());
        person.setName(myName);
        person.setEmail(user.getEmail());
        person.setUid(user.getUid());
        DatabaseReference myRef2 = database.getReference("Subject");
        myRef2.child(emnekode).child("StudAssList").child(user.getUid()).setValue(person);


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

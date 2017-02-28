package com.example.queueme;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static com.example.queueme.R.id.emnenavn;

public class StartSession extends AppCompatActivity implements View.OnClickListener {

    private String emnekode;
    //private int queuenr;

    //private TextView antall;
    private Button queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_session);

        //TextView antall=(TextView) findViewById(R.id.antall);

        queue = (Button) findViewById(R.id.queue);
        queue.setOnClickListener(this);

        final ArrayList<Person> studass = new ArrayList<Person>();



        Intent intent = getIntent();

        String emnenavn = intent.getStringExtra("emnenavn");
        emnekode = intent.getStringExtra("emnekode");


        //ArrayList<Person> lists = (ArrayList<Person>) intent.getSerializableExtra("list");

        //TextView queuenr = (TextView) findViewById(queunr);
        //queuenr.setText("Du er nr " + lists.indexOf("Anders"));

        TextView emnekode1 = (TextView) findViewById(R.id.emnekode);
        emnekode1.setText(emnekode);
        TextView emnenavn1 = (TextView) findViewById(R.id.emnenavn);
        emnenavn1.setText(emnenavn);


       /* FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Subject");
        ref.child(emnekode).child("StudAssList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //get all of the children of this level.
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                //shake hands with each of them
                for (DataSnapshot child: children){
                    Person person = child.getValue(Person.class);
                    studass.add(person);




                }
                queuenr= studass.size();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
        //antall.setText("Det er " + personsInLine.size()+ "i Kø");

    }

    private void QueueMe(){
        String email="";
        String uid="";
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            uid = user.getUid();
        }
        Person magnus = new Person();
        magnus.setUid(uid);
        magnus.setEmail(email);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Subject");
        myRef.child(emnekode).child("StudAssList").child(uid).setValue(user);
        myRef.child(emnekode).child("StudAssList").child(uid).child("Queue").push().setValue("Preallokering");
        myRef.child(emnekode).child("StudAssList").child(uid).child("Queue").push().setValue("Preallokering");


        Intent moveToDetailIntent = new Intent(StartSession.this,MySession.class);
        moveToDetailIntent.putExtra("emnekode",emnekode);
        moveToDetailIntent.putExtra("emnenavn",emnenavn);
        startActivity(moveToDetailIntent);

    }


    @Override
    public void onClick(View v) {
        if (v==queue){
            QueueMe();
        }
    }
}

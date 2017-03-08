package com.example.queueme;

import android.content.Intent;
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
        //henter brukerens info
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //lager liste
        final ArrayList<Person> studass = new ArrayList<Person>();
        //henter info fra forrige side
        Intent intent = getIntent();
        String emnenavn = intent.getStringExtra("emnenavn");
        emnekode = intent.getStringExtra("emnekode");
        //finner knapper
        queue = (Button) findViewById(R.id.queue);
        queue.setOnClickListener(this);
        //setter tekst
        TextView emnekode1 = (TextView) findViewById(R.id.emnekode);
        emnekode1.setText(emnekode);
        TextView emnenavn1 = (TextView) findViewById(R.id.emnenavn);
        emnenavn1.setText(emnenavn);
        TextView enter = (TextView) findViewById(R.id.enter);
        enter.setText("You'r about to enter "+ user.getEmail()+"s queue");


    }

    private void QueueMe(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email=user.getEmail();
        String uid=user.getUid();

        Person magnus = new Person();
        magnus.setUid(uid);
        magnus.setEmail(email);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Subject");
        myRef.child(emnekode).child("StudAssList").child(uid).setValue(user);
        //myRef.child(emnekode).child("StudAssList").child(uid).child("Queue").push().setValue("Preallokering");
        //myRef.child(emnekode).child("StudAssList").child(uid).child("Queue").push().setValue("Preallokering");


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

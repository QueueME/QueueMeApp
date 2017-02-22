package com.example.queueme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DetailedActivity extends AppCompatActivity implements View.OnClickListener{
    private String email;
    private int queuenr;
    //private TextView antall;
    private Button queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        //TextView antall=(TextView) findViewById(R.id.antall);

        queue = (Button) findViewById(R.id.queue);
        queue.setOnClickListener(this);

        final ArrayList<Person> personsInLine = new ArrayList<Person>();



        Intent intent = getIntent();

        String name = intent.getStringExtra("name");
        email = intent.getStringExtra("email");


        //ArrayList<Person> lists = (ArrayList<Person>) intent.getSerializableExtra("list");

        //TextView queuenr = (TextView) findViewById(queunr);
        //queuenr.setText("Du er nr " + lists.indexOf("Anders"));

        TextView email2 = (TextView) findViewById(R.id.email);
        email2.setText(email);
        TextView name2 = (TextView) findViewById(R.id.name);
        name2.setText(name+queuenr);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Person");
        ref.child(email).child("List").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //get all of the children of this level.
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                //shake hands with each of them
                for (DataSnapshot child: children){
                    Person person = child.getValue(Person.class);
                    personsInLine.add(person);




                }
                queuenr= personsInLine.size();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //antall.setText("Det er " + personsInLine.size()+ "i Kø");

    }

    private void QueueMe(){
        Person magnus = new Person();
        magnus.setEmail("Mgnus10@gmail.com");
        magnus.setName("magnus");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Person");
        myRef.child(email).child("List").push().setValue(magnus);
        //kræsjer når jeg kjører child(magnus.getEmail()) istedenfor push
    }


    @Override
    public void onClick(View v) {
        if (v==queue){
            QueueMe();
        }
    }
}

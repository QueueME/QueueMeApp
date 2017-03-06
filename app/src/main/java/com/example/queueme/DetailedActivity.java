package com.example.queueme;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DetailedActivity extends AppCompatActivity implements View.OnClickListener{
    private String email;
    private String personuid;
    private String emnekode;
    private String emnenavn;

    //private int queuenr;
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


        email = intent.getStringExtra("email");
        personuid=intent.getStringExtra("uid");
        emnenavn =intent.getStringExtra("emnenavn");
        emnekode = intent.getStringExtra("emnekode");


        //ArrayList<Person> lists = (ArrayList<Person>) intent.getSerializableExtra("list");

        //TextView queuenr = (TextView) findViewById(queunr);
        //queuenr.setText("Du er nr " + lists.indexOf("Anders"));

       /* TextView email2 = (TextView) findViewById(R.id.email);
        email2.setText(email);
        TextView name2 = (TextView) findViewById(R.id.name);
        name2.setText(name+);


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
*/
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
        DatabaseReference myRef = database.getReference();
        myRef.child("Subject").child(emnekode).child("StudAssList").child(personuid).child("Queue").child(uid).setValue(user);
////
    }


    @Override
    public void onClick(View v) {
        if (v==queue){
            QueueMe();

            Intent moveToDetailIntent = new Intent(DetailedActivity.this, InQueue.class);


            moveToDetailIntent.putExtra("email",email);
            moveToDetailIntent.putExtra("uid",personuid);
            moveToDetailIntent.putExtra("emnekode",emnekode);
            moveToDetailIntent.putExtra("emnenavn",emnenavn);

            startActivity(moveToDetailIntent);
        }
    }
}

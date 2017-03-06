package com.example.queueme;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MySession extends AppCompatActivity implements View.OnClickListener{
    private String emnenavn;
    private String emnekode;
    private String uid;
    private Button update;
    ArrayList students;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_session);

        update= (Button) findViewById(R.id.update);
        update.setOnClickListener(this);



        Intent intent = getIntent();

        emnenavn = intent.getStringExtra("emnenavn");
        emnekode = intent.getStringExtra("emnekode");

        final TextView nr= (TextView)findViewById(R.id.nr);
        final TextView person = (TextView) findViewById(R.id.person);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            uid = user.getUid();
        }

        final ArrayList<Person> students = new ArrayList<Person>();
        this.students=students;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Subject");
       /* myRef.child(emnekode).child("StudAssList").child(uid).child("Queue").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //get all of the children of this level.
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                //shake hands with each of them
                for (DataSnapshot child: children){
                    //FirebaseUser person = child.getValue(FirebaseUser.class);
                    Person person = child.getValue(Person.class);
                    students.add(person);



                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        */
        myRef.child(emnekode).child("StudAssList").child(uid).child("Queue").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                fetchData(dataSnapshot);
                int studentsnr= students.size();
                nr.setText("the line has "+String.valueOf(studentsnr));

                if (!students.isEmpty()){
                    String student= students.get(0).getEmail();
                    person.setText(student + "are next in line");
                }else{
                    person.setText("no one in line");
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {


            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                fetchDataDelete(dataSnapshot);
                int studentsnr= students.size();
                nr.setText("the line has "+String.valueOf(studentsnr));

                if (!students.isEmpty()){
                    String student= students.get(0).getEmail();
                    person.setText(student + "are next in line");
                }else{
                    person.setText("no one in line");
                }

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
    private void fetchData(DataSnapshot dataSnapshot)
    {
        //students.clear();
        Person person = dataSnapshot.getValue(Person.class);
        students.add(person);
    }
    private void fetchDataDelete(DataSnapshot dataSnapshot)
    {
        //students.clear();
        Person person = dataSnapshot.getValue(Person.class);
        students.remove(person);
    }


    @Override
    public void onClick(View v) {
        if (v==update){
            Toast.makeText(MySession.this, "clicked",
                    Toast.LENGTH_SHORT).show();
            Person anders = new Person();
            anders.setEmail("anders@nogneonw.no");
            anders.setName("vrv");
            anders.setUid("sfrdagf");
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("Subject");
            myRef.child(emnekode).child("StudAssList").child(uid).child("Queue").push().setValue(anders);


        }
    }
}

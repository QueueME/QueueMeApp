package com.example.queueme;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MySession extends AppCompatActivity implements View.OnClickListener,GestureDetector.OnGestureListener{
    private String emnenavn;
    private String emnekode;
    private String uid;
    private Button update;
    private ArrayList<Person> students;

    //til sviping ect
    private GestureDetector detector;
    private static final int SWIPE_THRESHOLD = 100;
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;

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
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Subject");
       //lager funskjoner når endring under denne referansen skjer
        myRef.child(emnekode).child("StudAssList").child(uid).child("Queue").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //henter data og legger til personen som addes til listen over
                fetchData(dataSnapshot);
                //setter tekst i textviewene
                int studentsnr= students.size();
                nr.setText("the line has "+String.valueOf(studentsnr));

                if (!students.isEmpty()){
                    String uid= students.get(0).getUid();

                    //finner navnet på første i kø i persondata i persondatabasen
                    DatabaseReference personRef = database.getReference("Person");
                    personRef.child(uid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Person person = dataSnapshot.getValue(Person.class);
                            String firstInLineName = person.getName();
                            //oppdaterer texviewen
                            TextView firstperson = (TextView) findViewById(R.id.person);
                            firstperson.setText(firstInLineName + " are next in line");
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }else{
                    TextView firstperson = (TextView) findViewById(R.id.person);
                    firstperson.setText("no one in line");
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {


            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                //henter ut personene som slettes og sletter han fra listen
                fetchDataDelete(dataSnapshot);
                //oppdatere texviewene
                int studentsnr= students.size();
                nr.setText("the line has "+String.valueOf(studentsnr));

                if (!students.isEmpty()){
                    String student= students.get(0).getEmail();
                    TextView firstperson = (TextView) findViewById(R.id.person);
                    firstperson.setText(student + " are next in line");
                }else{
                    TextView firstperson = (TextView) findViewById(R.id.person);
                    firstperson.setText("no one in line");
                }

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //swipe
        detector = new GestureDetector(this,this);


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
    //MÅ LEGGE TIL SLIK AT DENNE PERSONEN FJERNER SEG

    }


    @Override
    public void onClick(View v) {
        if (v==update){
            Toast.makeText(MySession.this, "clicked",
                    Toast.LENGTH_SHORT).show();
            Person anders = new Person();
            anders.setEmail("anders@nogneonw.no");
            anders.setName("Anders By Kampenes");
            anders.setUid("sfrdagf");
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("Subject");
            DatabaseReference personRef = database.getReference("Person");
            personRef.child(anders.getUid()).setValue(anders);
            myRef.child(emnekode).child("StudAssList").child(uid).child("Queue").child(anders.getUid()).setValue(anders);
            //startActivity(new Intent(MySession.this,MySessionSwipe.class));

        }
    }


    //touchmetoder som lar oss swipe
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return detector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {


        float diffY = e2.getY() - e1.getY();
        float diffX = e2.getX() - e1.getX();
        if (Math.abs(diffX) > Math.abs(diffY)) {
            if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                if (diffX > 0) {
                    return false;
                } else {
                    Toast.makeText(MySession.this, "SWIPED",
                            Toast.LENGTH_SHORT).show();
                    SvipeLeft();
                    return true;
                }

            }
        }
        return true;
    }

    private void SvipeLeft(){
        String studentuid = students.get(0).getUid();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Subject");
        myRef.child(emnekode).child("StudAssList").child(uid).child("Queue").child(studentuid).removeValue();
        //sletter kun øverste STORT PROBLEM HVIS NOEN ANDRE GÅR UT AV KØEN FRIVILLIG
        students.remove(0);


    }





}

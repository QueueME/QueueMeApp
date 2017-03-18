package com.example.queueme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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

public class DetailedActivity extends AppCompatActivity implements View.OnClickListener{
    private String email;
    private String personuid;
    private String emnekode;
    private String emnenavn;
    private int nrInLine;
    private TextView name;
    private TextView subjectinfo;
    private TextView availible_until;
    private TextView count;
    private ArrayList<Person> persons = new ArrayList<Person>();
    private Person studAss;
    private String studName;
    private Person Me;
    private String myName;


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




        //henter ting fra forrige side
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        personuid=intent.getStringExtra("uid");
        emnenavn =intent.getStringExtra("emnenavn");
        emnekode = intent.getStringExtra("emnekode");



        //lager en referanse/kobling  til databasen vår
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Subject");
        DatabaseReference studass=database.getReference();
        DatabaseReference myself =database.getReference();

        //henter info om meg
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid=user.getUid();
        getMe(myself,uid);


        //henter ut studass
        getStudass(studass);

        name = (TextView) findViewById(R.id.name);
        name.setText(Me.getName());

        subjectinfo = (TextView) findViewById(R.id.subjectinfo);
        subjectinfo.setText(emnekode + " " +emnenavn);

        availible_until = (TextView) findViewById(R.id.avilible_until);
        availible_until.setText("10");

        count= (TextView) findViewById(R.id.count);





        //henter ut alle som er i lsiten og legger dem i vår liste. Dette er fordi childeventlistener ikke kjøres i starten, og vi trenger listen med en gang.
        myRef.child(emnekode).child("StudAssList").child(personuid).child("Queue").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child: children){
                    Person person = child.getValue(Person.class);
                    persons.add(person);



                }
                //setter teksten i texview
                count.setText(""+ linecount()+"");

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


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {


            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                fetchDataDelete(dataSnapshot);
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
        persons.add(person);
    }

    private void fetchDataDelete(DataSnapshot dataSnapshot) {
        //students.clear();
        Person person = dataSnapshot.getValue(Person.class);
        persons.remove(person);
    }

    private int linecount() {
        return persons.size();
    }

private void getStudass(DatabaseReference ref){

    ref.child("Person").addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Iterable<DataSnapshot> children = dataSnapshot.getChildren();
            for (DataSnapshot child: children){
                Person person = child.getValue(Person.class);
                if(person.getUid()==personuid){
                    studAss=person;
                }



            }
            studAss = dataSnapshot.getValue(Person.class);


        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });

}
private void getMe(DatabaseReference ref,String uid){

    ref.child("Person").child(uid).addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            //me = dataSnapshot.getValue(Person.class);
            Person me=dataSnapshot.getValue(Person.class);
            SetMe(Me);



        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });

}
    private void SetMe(Person me){
        Me=me;
    }
    private void QueueMe(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email=user.getEmail();
        String uid=user.getUid();
        //skriver til databse
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.child("Subject").child(emnekode).child("StudAssList").child(personuid).child("Queue").child(uid).setValue(user);

    }


    @Override
    public void onClick(View v) {
        if (v==queue){
            //kjører queueme
            QueueMe();
            //går videre til neste aktivitet og tar med seg info
            Intent moveToDetailIntent = new Intent(DetailedActivity.this, InQueue.class);
            moveToDetailIntent.putExtra("email",email);
            moveToDetailIntent.putExtra("uid",personuid);
            moveToDetailIntent.putExtra("emnekode",emnekode);
            moveToDetailIntent.putExtra("emnenavn",emnenavn);

            startActivity(moveToDetailIntent);
        }
    }
}

package com.example.queueme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddPerson extends AppCompatActivity implements View.OnClickListener{

    private EditText email;
    private EditText name;
    private Button save;
    private Button btnsubject;
    private ImageButton home,menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addperson);


        email=(EditText) findViewById(R.id.email);
        name =(EditText) findViewById(R.id.name);
        save=(Button) findViewById(R.id.save);
        btnsubject=(Button) findViewById(R.id.btnsubject);

        home = (ImageButton) findViewById(R.id.home);
        menu = (ImageButton) findViewById(R.id.menu);

        home.setOnClickListener(this);
        menu.setOnClickListener(this);


        save.setOnClickListener(this);
        btnsubject.setOnClickListener(this);


    }

    public void onSaveClicked(){
        //lager bruker
        Person person = new Person();

        //setter verdier til
        person.setEmail(email.getText().toString());
        person.setName(name.getText().toString());


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Person");
        myRef.child(email.getText().toString()).setValue(person);
        //String key = myRef.push().getKey();
        //myRef.child(email.getText().toString()).child("List").push().setValue(person);



         //myRef.child("Person").push().setValue(person);

        //
       /* myRef.child("Person").orderByChild("email").equalTo("akampenes@gmail.com").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
                myRef.child(key).push().setValue(person);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        */


        //myRef.child("Person").child("list").setValue(person.getPersons());
    }
    private void Switch(){
        startActivity(new Intent(AddPerson.this, AddSubject.class));

    }
//

    @Override
    public void onClick(View v) {
        if(v==save){
            onSaveClicked();
            Toast.makeText(AddPerson.this, "Saved",Toast.LENGTH_SHORT).show();
        }
        if(v==btnsubject){
            Switch();
        }
        if(v.equals(home)) {
            startActivity(new Intent(AddPerson.this, StudOrAss.class));
        }
        else if(v.equals(menu)) {
            startActivity(new Intent(AddPerson.this, MenyActivity.class));
        }
    }
}

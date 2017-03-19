package com.example.queueme;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText name;
    private CheckBox man, woman;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        man = (CheckBox) findViewById(R.id.checkBox);
        woman = (CheckBox) findViewById(R.id.checkBox2);
        name = (EditText) findViewById(R.id.fullname);
        button=(Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPersonFromUser();
                startActivity(new Intent(RegisterActivity.this, StudOrAss.class));
                finish();
            }
        });

    }
    //lager en person i databasen med fulltnavn slik at vi kan bruke fullt navn senere
    private void createPersonFromUser(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        Person person =new Person();
        person.setName(name.getText().toString());
        person.setEmail(user.getEmail());
        person.setUid(user.getUid());
        person.setTime_to_stop("0");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef= database.getReference("Person");
        myRef.child(user.getUid()).setValue(person);

    }
}

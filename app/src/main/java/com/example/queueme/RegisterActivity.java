package com.example.queueme;


import android.content.Intent;
import android.net.Uri;
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

                createPersonFromUser(name.getText().toString());
                startActivity(new Intent(RegisterActivity.this, StudOrAss.class));
                finish();
            }
        });

    }
    //lager en person i databasen med fulltnavn slik at vi kan bruke fullt navn senere
    private void createPersonFromUser(String fullname){
        String useruid="";
        String useremail="";

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String username = user.getDisplayName();
            useremail = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            useruid = user.getUid();
        }

        Person person =new Person();
        person.setName(fullname);
        person.setEmail(useremail);
        person.setUid(useruid);
        person.setTime_to_stop("0");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef= database.getReference("Person");
        myRef.child(useruid).setValue(person);

    }
}

package com.example.queueme;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "MainActivity";
    //legger til brukernavn
    private EditText etInputName;
    private EditText etInputEmail;
    private EditText etInputPassword;
    private Button btnRegister;
    private Button btnsave;
    private Button btnpersons;
    private Button addfag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //finner edittekstene og binder de til variabler
        etInputEmail = (EditText) findViewById(R.id.etInputEmail);
        etInputPassword = (EditText) findViewById(R.id.etInputPassword);
        etInputName=(EditText) findViewById(R.id.etInputName);
        //finner knappene i layouten og binder de til variabler
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnsave=(Button) findViewById(R.id.btnsave);
        btnpersons=(Button) findViewById(R.id.btnpersons);
        addfag=(Button) findViewById(R.id.addfag);
        //setter onclicklistener
        btnRegister.setOnClickListener(this);
        btnsave.setOnClickListener(this);
        btnpersons.setOnClickListener(this);
        addfag.setOnClickListener(this);

        //nødvendig for å opprette bruker
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    //lager selve brukeren
    private void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            finish();
                            createPersonFromUser(etInputName.getText().toString(), etInputEmail.getText().toString(), etInputPassword.getText().toString());
                            Toast.makeText(MainActivity.this,"Created Person",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MainActivity.this, StudOrAss.class));
                            Toast.makeText(MainActivity.this, "Register seccessfukll",
                                    Toast.LENGTH_SHORT).show();

                        }
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });

    }

    private void Switch(){
        startActivity(new Intent(MainActivity.this, StudOrAss.class));

    }

    private void Switch2(){
        startActivity(new Intent(MainActivity.this, AddPerson.class));

    }

    //lager en person i databasen med fulltnavn slik at vi kan bruke fullt navn senere
    private void createPersonFromUser(String fullname, String email,String password){
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
    //det som skal skje når knapper blir trykket
    @Override
    public void onClick(View v) {
        if (v==btnRegister){
            createAccount(etInputEmail.getText().toString(), etInputPassword.getText().toString());


        }
        if (v==btnsave){
            Switch();
        }
        if (v==btnpersons){
            Switch2();
        }
        if (v==addfag){
            startActivity(new Intent(MainActivity.this, AddSubject.class));
        }

    }
}

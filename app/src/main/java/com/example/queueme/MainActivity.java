package com.example.queueme;

import android.content.Intent;
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

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "MainActivity";

    private EditText etInputEmail;
    private EditText etInputPassword;
    private Button btnRegister;
    private Button btnsave;
    private Button btnpersons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etInputEmail = (EditText) findViewById(R.id.etInputEmail);
        etInputPassword = (EditText) findViewById(R.id.etInputPassword);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnsave=(Button) findViewById(R.id.btnsave);
        btnpersons=(Button) findViewById(R.id.btnpersons);

        btnRegister.setOnClickListener(this);
        btnsave.setOnClickListener(this);
        btnpersons.setOnClickListener(this);
        //

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

    private void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
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
        startActivity(new Intent(MainActivity.this, AddPerson.class));

    }

    private void Switch2(){
        startActivity(new Intent(MainActivity.this, ChoosePerson.class));

    }
    @Override
    public void onClick(View v) {
        if (v==btnRegister){
            createAccount(etInputEmail.getText().toString(), etInputPassword.getText().toString());
            Toast.makeText(MainActivity.this, R.string.auth_failed,
                    Toast.LENGTH_SHORT).show();

        }
        if (v==btnsave){
            Switch();
        }
        if (v==btnpersons){
            Switch2();
        }
    }
}

package com.example.queueme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.queueme.infoscreen.WelcomeActivityStudent;

public class StudOrAss extends AppCompatActivity implements View.OnClickListener{

    private Button btnass;
    private Button btnstud;
    private Button swipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stud_or_ass);

        btnass=(Button) findViewById(R.id.btnass);
        btnstud=(Button) findViewById(R.id.btnstud);
        swipe=(Button) findViewById(R.id.swipe);

        btnass.setOnClickListener(this);
        btnstud.setOnClickListener(this);
        swipe.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        //velger studass
        if (v==btnass){
            startActivity(new Intent(StudOrAss.this, ChooseSubjectAss.class));
        }
        //velger student
        if (v==btnstud){
            startActivity(new Intent(StudOrAss.this, ChooseSubjectStud.class));
        }
        if (v==swipe){
            startActivity(new Intent(StudOrAss.this, WelcomeActivityStudent.class));
        }

    }
}

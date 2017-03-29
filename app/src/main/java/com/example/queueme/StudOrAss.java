package com.example.queueme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.queueme.MySessionSwipeFunction.ScreenSlidePagerActivity;

public class StudOrAss extends AppCompatActivity implements View.OnClickListener{

    private Button btnass;
    private Button btnstud;
    private Button swipe;
    private Button meny;
    private Button home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        btnass=(Button) findViewById(R.id.btnass);
        btnstud=(Button) findViewById(R.id.btnstud);
        //swipe=(Button) findViewById(R.id.swipe);

        btnass.setOnClickListener(this);
        btnstud.setOnClickListener(this);
        //swipe.setOnClickListener(this);

        meny = (Button) findViewById(R.id.meny);
        meny.setOnClickListener(this);
        home = (Button) findViewById(R.id.home);
        home.setOnClickListener(this);

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
            startActivity(new Intent(StudOrAss.this, ScreenSlidePagerActivity.class));
        }
        if (v==home){

        }
        if(v==meny){
            startActivity(new Intent(StudOrAss.this, MenyActivity.class));

        }

    }
}

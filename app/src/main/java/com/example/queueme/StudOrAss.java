package com.example.queueme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class StudOrAss extends AppCompatActivity implements View.OnClickListener{

    private Button btnass;
    private Button btnstud;
    private ImageButton meny;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_stud_or_ass);

        btnass=(Button) findViewById(R.id.btnass);
        btnstud=(Button) findViewById(R.id.btnstud);
        meny = (ImageButton) findViewById(R.id.menu);

        meny.setOnClickListener(this);
        btnass.setOnClickListener(this);
        btnstud.setOnClickListener(this);

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
        if(v==meny){
            startActivity(new Intent(StudOrAss.this, MenyActivity.class));
        }

    }
}

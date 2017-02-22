package com.example.queueme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class StudOrAss extends AppCompatActivity implements View.OnClickListener{

    private Button btnass;
    private Button btnstud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stud_or_ass);

        btnass=(Button) findViewById(R.id.btnass);
        btnstud=(Button) findViewById(R.id.btnstud);

        btnass.setOnClickListener(this);
        btnstud.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v==btnass){
            startActivity(new Intent(StudOrAss.this, CooseSubjectAss.class));
        }
        if (v==btnstud){

        }

    }
}

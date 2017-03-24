package com.example.queueme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.queueme.MySessionSwipeFunction.ScreenSlidePagerActivity;

public class Beginning extends AppCompatActivity {
    private Button login;
    private Button reg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro);

        login=(Button) findViewById(R.id.login);
        login=(Button) findViewById(R.id.reg);
        //swipe=(Button) findViewById(R.id.swipe);

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Beginning.this, LoginActivity.class));
                finish();
            }
        });
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Beginning.this, SignupActivity.class));
                finish();
            }
        });
        //swipe.setOnClickListener(this);
    }


}

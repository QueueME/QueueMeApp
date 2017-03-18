package info.androidhive.firebase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.CheckBox;

public class RegisterActivity extends AppCompatActivity {

    private EditText fullname;
    private CheckBox man, woman;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        man = (CheckBox) findViewById(R.id.checkBox);
        woman = (CheckBox) findViewById(R.id.checkBox2);
        fullname = (EditText) findViewById(R.id.fullname);

    }
}

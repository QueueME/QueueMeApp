package com.example.queueme;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import javax.security.auth.Subject;


public class Searchtest extends Activity {
    // Search EditText
    private EditText inputSearch;
    ArrayAdapter feedAdapter;
    ArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coose_subject_ass);

        //finer listview og setter som variabel
        final ListView l=(ListView) findViewById(R.id.listview);
        //lager listen alle fagene skal legger i
        final ArrayList<Subject> subjects = new ArrayList<Subject>();
        final ArrayList<String> jalla = new ArrayList<>();
        jalla.add("ONONNLKNOONOONONBO");
        jalla.add("aaaaaa");
        jalla.add("wwww");

        inputSearch = (EditText) findViewById(R.id.inputSearch);


        //henter ut alle subjects som ligger i databasen og legger i liste
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();
        myRef.child("Subject").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //get all of the children of this level.
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                //shake hands with each of them
                for (DataSnapshot child: children){
                    Subject subject = child.getValue(Subject.class);
                    subjects.add(subject);



                }

                //lager arrayadapter som viser listene

                feedAdapter = new ArrayAdapter(Searchtest.this, android.R.layout.simple_list_item_1,subjects);
                adapter = new ArrayAdapter(Searchtest.this, android.R.layout.simple_list_item_1,jalla);
                l.setAdapter(feedAdapter);
                //lager funkjsonen når man trykker på en av knappene i listviewen
                inputSearch.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (start==0){
                            l.setAdapter(feedAdapter);
                            Searchtest.this.feedAdapter.getFilter().filter(s);
                        }else{
                            l.setAdapter(adapter);
                            Searchtest.this.adapter.getFilter().filter(s);
                        }




                    }

                    @Override
                    public void afterTextChanged(Editable s) {


                    }
                });
                l.setOnItemClickListener(new AdapterView.OnItemClickListener() {


                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {





                    }
                });






            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
}

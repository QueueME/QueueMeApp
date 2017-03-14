package com.example.queueme;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by anders on 08.03.2017.
 */
public class PopSubject extends Activity {

    private EditText inputSearch;
    ArrayAdapter feedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setter størrelse på popup
        setContentView(R.layout.subject_pop);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm. heightPixels;

        getWindow().setLayout((int) (width*.8),(int) (height*.8));



        //finer listview og setter som variabel
        final ListView listView=(ListView) findViewById(R.id.listview);

        //lager listen alle fagene skal legger i
        final ArrayList<Subject> subjects = new ArrayList<Subject>();
        //finner inputsearch
        inputSearch = (EditText) findViewById(R.id.inputSearch);

        //henter ut alle subjects som ligger i databasen og legger i liste
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();
        final DatabaseReference myRef2 = database.getReference();

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

                feedAdapter = new ArrayAdapter(PopSubject.this, android.R.layout.simple_list_item_1,subjects);
                listView.setAdapter(feedAdapter);
                //definerer hva som skjer når man trykker på searchknappen
                inputSearch.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        PopSubject.this.feedAdapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                //lager funkjsonen når man trykker på en av knappene i listviewen
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Subject subject = (Subject) subjects.get(position);
                        String emnekode= subject.getEmnekode();
                        String emnenavn = subject.getEmnenavn();
                        //henter brukerdata
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        myRef2.child("Person").child(user.getUid()).child("FavoriteAssSubject").push().setValue(subject);






                    }
                });






            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

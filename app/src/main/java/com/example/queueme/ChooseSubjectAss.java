package com.example.queueme;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ChooseSubjectAss extends Activity {
    // Search EditText
    //private EditText inputSearch;
    ArrayAdapter feedAdapter;
    private Button popup;
    private ImageButton meny, home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coose_subject_ass);

        //finer listview og setter som variabel
        final ListView l=(ListView) findViewById(R.id.listview);
        //finner buttons
        popup = (Button) findViewById(R.id.popup);


        //lager listen alle fagene skal legger i
        final ArrayList<Subject> subjects = new ArrayList<Subject>();

        //inputSearch = (EditText) findViewById(R.id.inputSearch);

        meny = (ImageButton) findViewById(R.id.menu);
        home = (ImageButton) findViewById(R.id.home);
        meny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChooseSubjectAss.this, MenyActivity.class));
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChooseSubjectAss.this, StudOrAss.class));
            }
        });


        //henter ut alle subjects som ligger i databasen og legger i liste
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();
        // brukes til dialogen:
        final DatabaseReference myRefdialog = database.getReference();
        final DatabaseReference myRefdialog2 = database.getReference();
        //
        //henter info om bruker
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        myRef.child("Person").child(user.getUid()).child("FavoriteAssSubject").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //get all of the children of this level.
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                //shake hands with each of them
                for (DataSnapshot child: children){
                    Subject subject = child.getValue(Subject.class);
                    subjects.add(subject);



                }

                FeedAdapter_ChooseSubject_Ass Adapter = new FeedAdapter_ChooseSubject_Ass(ChooseSubjectAss.this, R.layout.list_subjectitem_ass, subjects);
                l.setAdapter(Adapter);

                l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Subject subject = (Subject) subjects.get(position);
                        Intent moveToDetailIntent = new Intent(ChooseSubjectAss.this, StartSession.class);
                        // moveToDetailIntent.putExtra("bkjb", );
                        String emnekode= subject.getEmnekode();
                        String emnenavn = subject.getEmnenavn();


                        moveToDetailIntent.putExtra("emnekode",emnekode);
                        moveToDetailIntent.putExtra("emnenavn",emnenavn);


                        //startActivityForResult(moveToDetailIntent,position);
                        //Person Anders = new Person();
                        //Anders.setName("nonneanders");
                        //person.getPersons().add(Anders);
                        startActivity(moveToDetailIntent);


                    }
                });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }



        });

        popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder mbuilder = new AlertDialog.Builder(ChooseSubjectAss.this);
                View mView= getLayoutInflater().inflate(R.layout.dialog_subject,null);
                mbuilder.setView(mView);
                final AlertDialog dialog = mbuilder.create();
                dialog.show();
                final EditText inputSearch = (EditText) mView.findViewById(R.id.inputSearch);
                Button finish = (Button) mView.findViewById(R.id.finish);
                final ListView listView=(ListView) mView.findViewById(R.id.listview);

                //lager listen alle fagene skal legger i
                final ArrayList<Subject> subjects = new ArrayList<Subject>();

                //
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

                        feedAdapter = new ArrayAdapter(ChooseSubjectAss.this, android.R.layout.simple_list_item_1,subjects);
                        listView.setAdapter(feedAdapter);
                        //definerer hva som skjer når man trykker på searchknappen
                        inputSearch.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                ChooseSubjectAss.this.feedAdapter.getFilter().filter(s);
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

                                myRefdialog2.child("Person").child(user.getUid()).child("FavoriteAssSubject").push().setValue(subject);
                                Toast.makeText(ChooseSubjectAss.this,"Subject added to favorites",Toast.LENGTH_SHORT).show();





                            }
                        });






                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                //
                finish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

            }

        });

    }


}

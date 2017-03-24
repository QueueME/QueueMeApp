package com.example.queueme.MySessionSwipeFunction;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.queueme.Person;
import com.example.queueme.R;
import com.example.queueme.StudOrAss;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ScreenSlidePagerActivity extends FragmentActivity {
    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 5;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;
    private TextView nr;
    private TextView person;
    Button end;
    private ArrayList<Person> students = new ArrayList<Person>();
    private String emnenavn;
    private String emnekode;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screenslide);

        Intent intent = getIntent();
        emnenavn = intent.getStringExtra("emnenavn");
        emnekode = intent.getStringExtra("emnekode");

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.addOnPageChangeListener(viewPagerPageChangeListener);

        person=(TextView) findViewById(R.id.person);
        nr=(TextView) findViewById(R.id.nr);
        person.setText("There are no person in line");
        nr.setText("0");
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Subject");
        final DatabaseReference myRef2 = database.getReference("Subject");

        end =(Button) findViewById(R.id.end);
        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final AlertDialog.Builder mbuilder = new AlertDialog.Builder(ScreenSlidePagerActivity.this);
                View mView= getLayoutInflater().inflate(R.layout.popup_warning,null);
                mbuilder.setView(mView);
                final AlertDialog dialog = mbuilder.create();
                dialog.show();
                Button yes = (Button) mView.findViewById(R.id.yes);
                Button no = (Button) mView.findViewById(R.id.no);
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeQueue(myRef2);
                        dialog.dismiss();
                    }
                });
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

            }

        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            uid = user.getUid();
        }

        final ArrayList<Person> students = new ArrayList<Person>();
        this.students=students;

        //lager funskjoner når endring under denne referansen skjer
        myRef.child(emnekode).child("StudAssList").child(uid).child("Queue").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //henter data og legger til personen som addes til listen over
                fetchData(dataSnapshot);
                //setter tekst i textviewene
                int studentsnr= students.size();
                nr.setText("the line has "+String.valueOf(studentsnr));

                if (!students.isEmpty()){
                    String uid= students.get(0).getUid();

                    //finner navnet på første i kø i persondata i persondatabasen
                    DatabaseReference personRef = database.getReference("Person");
                    personRef.child(uid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Person person = dataSnapshot.getValue(Person.class);
                            String firstInLineName = person.getName();
                            //oppdaterer texviewen
                            TextView firstperson = (TextView) findViewById(R.id.person);
                            firstperson.setText(firstInLineName + " are next in line");
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }else{
                    TextView firstperson = (TextView) findViewById(R.id.person);
                    firstperson.setText("no one in line");
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {


            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                //henter ut personene som slettes og sletter han fra listen
                fetchDataDelete(dataSnapshot);
                //oppdatere texviewene
                int studentsnr= students.size();
                nr.setText("the line has "+String.valueOf(studentsnr));

                if (!students.isEmpty()){
                    String student= students.get(0).getEmail();
                    TextView firstperson = (TextView) findViewById(R.id.person);
                    firstperson.setText(student + " are next in line");
                }else{
                    TextView firstperson = (TextView) findViewById(R.id.person);
                    firstperson.setText("no one in line");
                }

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
    private void removeQueue(DatabaseReference ref){
        ref.child(emnekode).child("StudAssList").child(uid).removeValue();
        startActivity(new Intent(ScreenSlidePagerActivity.this, StudOrAss.class));
        finish();
    }
    private void fetchData(DataSnapshot dataSnapshot)
    {
        //students.clear();
        Person person = dataSnapshot.getValue(Person.class);
        students.add(person);
    }
    private void fetchDataDelete(DataSnapshot dataSnapshot)
    {
        //students.clear();
        Person person = dataSnapshot.getValue(Person.class);
        //MÅ LEGGE TIL SLIK AT DENNE PERSONEN FJERNER SEG

    }

    private int linecount() {
        return students.size();
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            Toast.makeText(ScreenSlidePagerActivity.this, "YYOYUIO",Toast.LENGTH_SHORT).show();
            //mPager.removeViewAt(0);
            //mPagerAdapter.notifyDataSetChanged();
            students.remove(0);
            person.setText(students.get(0).getName());
            nr.setText(String.valueOf(linecount()));

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new ScreenSlidePageFragment();
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}
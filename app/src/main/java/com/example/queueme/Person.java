package com.example.queueme;

import java.util.ArrayList;

/**
 * Created by anders on 14.02.2017.
 */

public class Person {
    private String name;
    private String email;
    private String uid;
    private String time_to_stop;





    ArrayList<Person> persons = new ArrayList<Person>();



    public void Person(){

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Person> getPersons() {
        return persons;
    }

    @Override
    public String toString() {
        return "Name" + name+'\n' +
                "Email" + email + '\n';
    }

    public String getTime_to_stop() {
        return time_to_stop;
    }

    public void setTime_to_stop(String time_to_stop) {
        this.time_to_stop = time_to_stop;
    }
}

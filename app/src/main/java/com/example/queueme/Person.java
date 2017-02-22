package com.example.queueme;

import java.util.ArrayList;

/**
 * Created by anders on 14.02.2017.
 */

public class Person {
    private String name;
    private String email;
    ArrayList<Person> persons = new ArrayList<Person>();

    public void Person(){
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
}

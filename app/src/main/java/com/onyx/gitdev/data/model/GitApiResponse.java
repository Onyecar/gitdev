package com.onyx.gitdev.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Morph-Deji on 15-Mar-17.
 */

public class GitApiResponse {
    @SerializedName("items")
    private ArrayList<Person> persons;

    public ArrayList<Person> getPersons() {
        return persons;
    }

    public void setPersons(ArrayList<Person> persons) {
        this.persons = persons;
    }
}

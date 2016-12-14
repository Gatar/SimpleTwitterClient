package com.gatar.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {

    private String username = "JAVA";
    private List<String> filterWords = new ArrayList<>();

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getFilterWords() {
        return filterWords;
    }

    public void setFilterWords(List<String> filterWords) {
        this.filterWords = filterWords;
    }
}

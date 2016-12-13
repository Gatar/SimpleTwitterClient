package com.gatar.Domain;

import java.util.ArrayList;

public class TweetDTO {

    private String text;

    private String createDate;

    private ArrayList<String> urlList = new ArrayList<>();

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public ArrayList<String> getUrl() {
        return urlList;
    }

    public void setUrl(ArrayList<String> urlList) {
        this.urlList = urlList;
    }
}

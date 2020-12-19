package com.smule.wikicrawler.repository;

public class Article {
    private String linkName;
    private String titleName;

    public Article(String linkName, String titleName) {
        this.linkName = linkName;
        this.titleName = titleName;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }
}

package com.smule.wikicrawler;

public class WikipediaLinkRequest {
    private String url;

    public WikipediaLinkRequest() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "WikipediaLinkRequest{" +
                "url='" + url + '\'' +
                '}';
    }

    public String getTitle(String url) {
        String[] urlSplit = url.split("/");
        return urlSplit[urlSplit.length - 1];
    }
}

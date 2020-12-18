package com.smule.articles_repository;

import java.util.ArrayList;
import java.util.List;

public class ArticleResult {
    final String articleName;
    final List<String> articleLinks;

    public ArticleResult(String articleName, List<String> articleLinks) {
        this.articleName = articleName;
        this.articleLinks = articleLinks;
    }

    public ArticleResult() {
        this.articleName = "";
        this.articleLinks = new ArrayList<>();
    }

    public ArticleResult(String articleName) {
        this.articleName = articleName;
        this.articleLinks = new ArrayList<>();
    }


    public void add(String linkingArticle) {
        articleLinks.add(linkingArticle);
    }

    public List<String> getArticles() {
        return articleLinks;
    }

    @Override
    public String toString() {
        return "ArticleResult{" +
                "articleName='" + articleName + '\'' +
                ", articleLinks=" + articleLinks +
                '}';
    }
}

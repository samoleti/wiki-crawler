package com.smule.wikicrawler.repository;

import java.util.ArrayList;
import java.util.List;

public class ArticleResult {

    String articleName;
    final List<Article> articleLinks;

    public ArticleResult(String articleName) {
        this.articleName = articleName;
        this.articleLinks = new ArrayList<>();
    }

    public void add(Article linkingArticle) {
        articleLinks.add(linkingArticle);
    }

    @Override
    public String toString() {
        return "ArticleResult{" +
                "articleName='" + articleName + '\'' +
                ", articleLinks=" + articleLinks +
                '}';
    }

    public String getArticleName() {
        return articleName;
    }

    public List<Article> getArticleLinks() {
        return articleLinks;
    }

    public void setArticleName(String articlePrettyName) {
        articleName = articlePrettyName;
    }
}

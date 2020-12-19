package com.smule.wikicrawler.repository;

public interface ArticleRepository {

    boolean containsResultForArticle(String article);

    void addLinkForArticle(String article, Article linkingArticle);

    ArticleResult getResultsFor(String article);
}

package com.smule.articles_repository;

public interface ArticleRepository {
    boolean containsResultForArticle(String article);
    void addLinkForArticle(String article, String linkingArticle);
    ArticleResult getResultsFor(String article);

}

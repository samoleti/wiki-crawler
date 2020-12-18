package com.smule.wikicrawler.repository;

import org.springframework.stereotype.Repository;

public interface ArticleRepository {

    boolean containsResultForArticle(String article);

    void addLinkForArticle(String article, String linkingArticle);

    ArticleResult getResultsFor(String article);
}

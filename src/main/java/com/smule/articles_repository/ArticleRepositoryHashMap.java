package com.smule.articles_repository;

import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ArticleRepositoryHashMap implements ArticleRepository{
    private final Map<String, ArticleResult> results = new ConcurrentHashMap<>();
    public boolean containsResultForArticle(String article) {
        return results.containsKey(article);
    }

    public void addLinkForArticle(String article, String linkingArticle) {
        if (results.containsKey(article)) {
            ArticleResult articleResult = results.get(article);
            articleResult.add(linkingArticle);
        } else {
            ArticleResult result = new ArticleResult(article);
            result.add(linkingArticle);
            results.put(article, result);
        }
    }

    public ArticleResult getResultsFor(String article) {
        return results.get(article);
    }


    @Override
    public String toString() {
        return "ArticleRepositoryHashMap{" +
                "results=" + results +
                '}';
    }
}

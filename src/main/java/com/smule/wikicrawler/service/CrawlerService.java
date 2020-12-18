package com.smule.wikicrawler.service;

import com.smule.wikicrawler.repository.ArticleRepository;
import com.smule.wikicrawler.repository.ArticleResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class CrawlerService {

    private static final String articleCommonPattern = "(?<=<a href=\\\\\"\\/wiki\\/)([^:]*?)(?=\\\\|#(.*?)\\\\)";
    private final ArticleRepository articleRepository;

    public CrawlerService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    private boolean isValidArticle(String article) {
        return !article.contains(" ");
    }

    private List<String> parseArticles(String json) {
        List<String> resultArticles = new ArrayList<>();
        final Matcher matcher = Pattern.compile(articleCommonPattern).matcher(json);
        while (matcher.find()) {
            String matchedGroup = matcher.group(1);
            if (isValidArticle(matchedGroup)) {
                resultArticles.add(matchedGroup);
            }
        }
        return resultArticles.stream().distinct().collect(Collectors.toList());
    }

    private boolean parseArticlesBackwards(String json, String articlePattern) {
        //List<String> resultArticles = new ArrayList<>();
        final Matcher matcher = Pattern.compile(articlePattern).matcher(json);
        return matcher.find();
    }

    private static final String wikiApi = "https://en.wikipedia.org/w/api.php?action=parse&section=0&prop=text&format=json&page=";

    String restTemplate(String articleTitle) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForEntity(wikiApi + articleTitle, String.class).getBody();
    }

    public String getTitle(String url) {
        String[] urlSplit = url.split("/");
        return urlSplit[urlSplit.length - 1];
    }

    public void getArticles(String articleTitle, List<String> originalArticleReferences, String originalArticlePattern) {
        ExecutorService service = Executors.newFixedThreadPool(50);
        for(String article : originalArticleReferences) {
            service.submit(() -> {
                String responseArticle = restTemplate(article); // returns json of responseArticle
                if(parseArticlesBackwards(responseArticle, originalArticlePattern)) {
                    articleRepository.addLinkForArticle(articleTitle,article);
                }
            });
        }
        service.shutdown();
        try {
            service.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public ArticleResult crawl(String url) {
        String articleTitle = getTitle(url);
        if(articleRepository.containsResultForArticle(articleTitle)) {
            return articleRepository.getResultsFor(articleTitle);
        }
        String originalArticlePattern = "<a href=\\\\\"\\/wiki\\/" + articleTitle + "\\\\\"";
        String response = restTemplate(articleTitle);
        List<String> originalArticleReferences = Collections.synchronizedList(parseArticles(response));
        getArticles(articleTitle,originalArticleReferences, originalArticlePattern);
        return articleRepository.getResultsFor(articleTitle);
    }
}

package com.smule.wikicrawler.service;

import com.smule.wikicrawler.repository.Article;
import com.smule.wikicrawler.repository.ArticleRepository;
import com.smule.wikicrawler.repository.ArticleResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
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
    private static final String articleTitlePattern = "(?<=title\":\")(.*?)(?=\")";

    private final ArticleRepository articleRepository;

    public CrawlerService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    private boolean isValidArticle(String article) {
        return !article.contains(" ");
    }

    private String extractTitle(String json) {
        final Matcher matcher = Pattern.compile(articleTitlePattern).matcher(json);
        matcher.find();
        return matcher.group(0);
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
        final Matcher matcher = Pattern.compile(articlePattern).matcher(json);
        return matcher.find();
    }

    private static final String wikiApi = "https://en.wikipedia.org/w/api.php?action=parse&section=0&prop=text&format=json&page=";

    String restTemplate(String articleTitle) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForEntity(wikiApi + articleTitle, String.class).getBody();
    }

//    private String decoded(String articleTitle) {
//        try {
//            return URLDecoder.decode(articleTitle, StandardCharsets.UTF_8.toString());
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        return articleTitle;
//    }

    public String getTitle(String url) {
        String[] urlSplit = url.split("/");
        String encodedString = urlSplit[urlSplit.length - 1];
        return encodedString;
    }

    public void getArticles(String articleTitle, List<String> originalArticleReferences, String originalArticlePattern) {
        ExecutorService service = Executors.newFixedThreadPool(50);
        for(String article : originalArticleReferences) {
           service.submit(() -> {
                String responseArticle = restTemplate(article);
                if(parseArticlesBackwards(responseArticle, originalArticlePattern)) {
                    articleRepository.addLinkForArticle(
                            articleTitle, new Article(article,extractTitle(responseArticle)));
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
        String articlePrettyName = extractTitle(response);
        List<String> originalArticleReferences = Collections.synchronizedList(parseArticles(response));
        getArticles(articleTitle,originalArticleReferences, originalArticlePattern);
        ArticleResult results = articleRepository.getResultsFor(articleTitle);
        results.setArticleName(articlePrettyName);
        return results;
    }
}

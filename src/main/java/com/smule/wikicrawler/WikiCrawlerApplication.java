package com.smule.wikicrawler;

import com.smule.articles_repository.ArticleRepository;
import com.smule.articles_repository.ArticleRepositoryHashMap;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WikiCrawlerApplication {

	public static void main(String[] args) {
		SpringApplication.run(WikiCrawlerApplication.class, args);
	}

	@Bean
	ArticleRepository getArticleRepo() {
		return new ArticleRepositoryHashMap();
	}
}

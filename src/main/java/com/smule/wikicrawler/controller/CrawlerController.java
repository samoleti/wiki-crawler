package com.smule.wikicrawler.controller;

import com.smule.wikicrawler.service.CrawlerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CrawlerController {
    
    CrawlerService crawlerService;

    public CrawlerController(CrawlerService crawlerService) {
        this.crawlerService = crawlerService;
    }

    @GetMapping("/crawl")
    String crawl(@RequestParam("url") String url, Model model) {
        String articleTitle = crawlerService.getTitle(url);
        model.addAttribute("topic", articleTitle);
        model.addAttribute("articles", crawlerService.crawl(url).getArticles());
        return "index2";
    }

    @GetMapping("/")
    String getIndexPage() {
        return "index";
    }
}

package com.smule.wikicrawler;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomepageController {
    Crawler crawler;

    public HomepageController(Crawler crawler) {
        this.crawler = crawler;
    }

    @GetMapping("/call")
    String call(@RequestParam("url") String url, Model model) {
        //crawler.crawl(url);
        String articleTitle = crawler.getTitle(url);
        model.addAttribute("topic", articleTitle);
        model.addAttribute("articles", crawler.crawl(url).getArticles());
        //System.out.println(crawler.crawl(url).toString());
        return "index2";
    }

    @GetMapping("/")
    String indexPage(Model model) {
        WikipediaLinkRequest url = new WikipediaLinkRequest();
        model.addAttribute("user", url);
        return "index";
    }

}

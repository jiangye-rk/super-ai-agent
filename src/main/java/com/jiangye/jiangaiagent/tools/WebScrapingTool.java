package com.jiangye.jiangaiagent.tools;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

public class WebScrapingTool {
    @Tool(description = "Scrap the content of a web page")
    public String scrapingWebPage(@ToolParam(description = "URL of the web page to scrap") String url){
        try {
            Document document=Jsoup.connect(url).get();
            return document.html();
        }catch (Exception e){
            return "Error scraping web page: "+e.getMessage();
        }
    }
}

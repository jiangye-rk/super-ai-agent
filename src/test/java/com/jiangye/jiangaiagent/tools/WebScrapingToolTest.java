package com.jiangye.jiangaiagent.tools;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class WebScrapingToolTest {

    @Test
    void scrapingWebPage() {
        WebScrapingTool webScrapingTool = new WebScrapingTool();
        String url="https://www.codefather.cn";
        String result = webScrapingTool.scrapingWebPage(url);
        assertNotNull(result);
    }
}
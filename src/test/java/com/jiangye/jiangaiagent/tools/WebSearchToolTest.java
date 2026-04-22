package com.jiangye.jiangaiagent.tools;

import org.junit.jupiter.api.Test;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@ActiveProfiles("local")
class WebSearchToolTest {
    @Value("${search-api.api-key}")
    private String searchApiKey;
    @Test
    void searchWeb() {
        WebSearchTool tool = new WebSearchTool(searchApiKey);
        String query = "程序员鱼皮编程导航 codefather.cn";
        String result = tool.searchWeb(query);
        assertNotNull(result);
    }
}
package com.jiangye.jiangaiagent.rag;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class LoveAppDocumentLoader {
    private final ResourcePatternResolver resourcePatternResolver;
    public LoveAppDocumentLoader(ResourcePatternResolver resourcePatternResolver) {
        this.resourcePatternResolver = resourcePatternResolver;
    }
    public List<Document> loadDocuments() {
        List<Document> allDocuments = new ArrayList<>();
        try {
            Resource[] resources = resourcePatternResolver.getResources("classpath:document/*.md");
            for (Resource resource : resources) {
                String fileName = resource.getFilename();
                MarkdownDocumentReaderConfig config = MarkdownDocumentReaderConfig.builder()
                        .withHorizontalRuleCreateDocument(true)  //水平线（---）切分文档
                        .withIncludeCodeBlock(false)             //忽略代码块内容
                        .withIncludeBlockquote(false)            //忽略引用块内容
                        .withAdditionalMetadata("filename", fileName)
                        .build();
                MarkdownDocumentReader reader=new MarkdownDocumentReader(resource,config);
                allDocuments.addAll(reader.get());
            }
        } catch (Exception e) { 
            log.error("Markdown 文档加载失败", e);
        }
        return allDocuments;
    }

}

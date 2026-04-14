package com.jiangye.jiangaiagent;

import org.springframework.ai.autoconfigure.vectorstore.pgvector.PgVectorStoreAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        PgVectorStoreAutoConfiguration.class
})
public class JiangAiAgentApplication {

    public static void main(String[] args) {
        SpringApplication.run(JiangAiAgentApplication.class, args);
    }

}

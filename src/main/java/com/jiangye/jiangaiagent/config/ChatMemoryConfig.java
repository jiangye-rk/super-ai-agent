package com.jiangye.jiangaiagent.config;

import com.jiangye.jiangaiagent.chatmemory.MysqlChatMemory;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * ChatMemory 配置类
 * 配置使用 MySQL 持久化的对话记忆
 */
@Configuration
public class ChatMemoryConfig {

    /**
     * 配置 MySQL ChatMemory 为主要的 ChatMemory 实现
     */
    @Bean
    @Primary
    public ChatMemory chatMemory(MysqlChatMemory mysqlChatMemory) {
        return mysqlChatMemory;
    }
}

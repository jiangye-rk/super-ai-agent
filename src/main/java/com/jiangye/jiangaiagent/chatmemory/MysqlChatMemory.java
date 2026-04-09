package com.jiangye.jiangaiagent.chatmemory;

import com.jiangye.jiangaiagent.entity.ChatMessageEntity;
import com.jiangye.jiangaiagent.mapper.ChatMessageMapper;
import com.jiangye.jiangaiagent.service.ConversationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * MySQL 持久化的 ChatMemory 实现
 * 用于 Spring AI 的对话记忆管理
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MysqlChatMemory implements ChatMemory {

    private final ChatMessageMapper chatMessageMapper;
    private final ConversationService conversationService;

    /**
     * 添加消息到会话
     *
     * @param conversationId 会话ID
     * @param message        消息对象
     */
    @Override
    public void add(String conversationId, Message message) {
        if (conversationId == null || message == null) {
            log.warn("添加消息失败：conversationId 或 message 为空");
            return;
        }

        // 如果会话不存在，先创建会话（使用指定的 conversationId）
        if (!conversationService.exists(conversationId)) {
            conversationService.createConversationWithId(conversationId, "anonymous", "新会话", "love_master");
            log.debug("自动创建新会话: conversationId={}", conversationId);
        }

        ChatMessageEntity entity = new ChatMessageEntity();
        entity.setConversationId(conversationId);
        entity.setRole(getRoleString(message.getMessageType()));
        entity.setContent(message.getText());
        entity.setMessageType("text");
        entity.setMetadata(extractMetadata(message));
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());

        chatMessageMapper.insert(entity);
        conversationService.updateMessageCount(conversationId);

        log.debug("添加消息到会话: conversationId={}, role={}", conversationId, entity.getRole());
    }

    /**
     * 批量添加消息到会话
     *
     * @param conversationId 会话ID
     * @param messages       消息列表
     */
    @Override
    public void add(String conversationId, List<Message> messages) {
        if (conversationId == null || messages == null || messages.isEmpty()) {
            return;
        }

        for (Message message : messages) {
            add(conversationId, message);
        }
    }

    /**
     * 获取会话的最近 N 条消息
     *
     * @param conversationId 会话ID
     * @param lastN          获取消息数量
     * @return 消息列表
     */
    @Override
    public List<Message> get(String conversationId, int lastN) {
        if (conversationId == null || lastN <= 0) {
            return Collections.emptyList();
        }

        List<ChatMessageEntity> entities = chatMessageMapper.selectRecentByConversationId(conversationId, lastN);
        // 需要反转顺序，因为查询是按时间倒序的
        Collections.reverse(entities);

        List<Message> messages = new ArrayList<>();
        for (ChatMessageEntity entity : entities) {
            Message message = convertToMessage(entity);
            if (message != null) {
                messages.add(message);
            }
        }

        log.debug("获取会话消息: conversationId={}, count={}", conversationId, messages.size());
        return messages;
    }

    /**
     * 清除会话的所有消息
     *
     * @param conversationId 会话ID
     */
    @Override
    public void clear(String conversationId) {
        if (conversationId == null) {
            return;
        }

        chatMessageMapper.deleteByConversationId(conversationId);
        log.info("清除会话消息: conversationId={}", conversationId);
    }

    /**
     * 获取会话的所有消息
     *
     * @param conversationId 会话ID
     * @return 所有消息列表
     */
    public List<Message> getAll(String conversationId) {
        if (conversationId == null) {
            return Collections.emptyList();
        }

        List<ChatMessageEntity> entities = chatMessageMapper.selectByConversationId(conversationId);
        List<Message> messages = new ArrayList<>();
        for (ChatMessageEntity entity : entities) {
            Message message = convertToMessage(entity);
            if (message != null) {
                messages.add(message);
            }
        }

        return messages;
    }

    /**
     * 将实体转换为 Spring AI Message
     */
    private Message convertToMessage(ChatMessageEntity entity) {
        String role = entity.getRole();
        String content = entity.getContent();

        if (content == null) {
            content = "";
        }

        return switch (role.toLowerCase()) {
            case "user" -> new UserMessage(content);
            case "assistant" -> new AssistantMessage(content);
            case "system" -> new SystemMessage(content);
            default -> {
                log.warn("未知的消息角色: {}", role);
                yield null;
            }
        };
    }

    /**
     * 获取角色字符串
     */
    private String getRoleString(MessageType messageType) {
        return switch (messageType) {
            case USER -> "user";
            case ASSISTANT -> "assistant";
            case SYSTEM -> "system";
            case TOOL -> "tool";
        };
    }

    /**
     * 提取消息元数据
     */
    private String extractMetadata(Message message) {
        // 可以根据需要提取更多元数据，如工具调用信息等
        if (message instanceof AssistantMessage assistantMessage) {
            if (assistantMessage.getToolCalls() != null && !assistantMessage.getToolCalls().isEmpty()) {
                return "{\"hasToolCalls\": true}";
            }
        }
        return null;
    }
}

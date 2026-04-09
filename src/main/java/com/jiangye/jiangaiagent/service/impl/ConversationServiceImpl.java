package com.jiangye.jiangaiagent.service.impl;

import com.jiangye.jiangaiagent.entity.ConversationEntity;
import com.jiangye.jiangaiagent.mapper.ConversationMapper;
import com.jiangye.jiangaiagent.service.ConversationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * 会话服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ConversationServiceImpl implements ConversationService {
    
    private final ConversationMapper conversationMapper;
    
    @Override
    public ConversationEntity createConversation(String userId, String title, String conversationType) {
        String conversationId = UUID.randomUUID().toString().replace("-", "");
        return createConversationWithId(conversationId, userId, title, conversationType);
    }
    
    @Override
    public ConversationEntity createConversationWithId(String conversationId, String userId, String title, String conversationType) {
        ConversationEntity conversation = new ConversationEntity();
        conversation.setConversationId(conversationId);
        conversation.setUserId(userId);
        conversation.setTitle(title);
        conversation.setConversationType(conversationType);
        conversation.setMessageCount(0);
        conversation.setCreateTime(LocalDateTime.now());
        conversation.setLastUpdateTime(LocalDateTime.now());
        
        conversationMapper.insert(conversation);
        log.info("创建新会话: conversationId={}, userId={}", conversationId, userId);
        return conversation;
    }
    
    @Override
    public ConversationEntity getConversation(String conversationId) {
        return conversationMapper.selectByConversationId(conversationId);
    }
    
    @Override
    public List<ConversationEntity> getUserConversations(String userId) {
        return conversationMapper.selectByUserId(userId);
    }
    
    @Override
    public void updateConversationTitle(String conversationId, String title) {
        ConversationEntity conversation = conversationMapper.selectByConversationId(conversationId);
        if (conversation != null) {
            conversation.setTitle(title);
            conversation.setLastUpdateTime(LocalDateTime.now());
            conversationMapper.update(conversation);
        }
    }
    
    @Override
    public void deleteConversation(String conversationId) {
        conversationMapper.deleteByConversationId(conversationId);
        log.info("删除会话: conversationId={}", conversationId);
    }
    
    @Override
    public void updateMessageCount(String conversationId) {
        // 这里可以通过ChatMessageMapper查询实际数量，简化处理
        ConversationEntity conversation = conversationMapper.selectByConversationId(conversationId);
        if (conversation != null) {
            conversation.setMessageCount(conversation.getMessageCount() + 1);
            conversation.setLastUpdateTime(LocalDateTime.now());
            conversationMapper.update(conversation);
        }
    }
    
    @Override
    public boolean exists(String conversationId) {
        return conversationMapper.existsByConversationId(conversationId) > 0;
    }
}

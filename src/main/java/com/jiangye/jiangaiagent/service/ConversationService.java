package com.jiangye.jiangaiagent.service;

import com.jiangye.jiangaiagent.entity.ConversationEntity;

import java.util.List;

/**
 * 会话服务接口
 */
public interface ConversationService {
    
    /**
     * 创建新会话
     */
    ConversationEntity createConversation(String userId, String title, String conversationType);
    
    /**
     * 创建新会话（指定会话ID）
     */
    ConversationEntity createConversationWithId(String conversationId, String userId, String title, String conversationType);
    
    /**
     * 根据会话ID获取会话
     */
    ConversationEntity getConversation(String conversationId);
    
    /**
     * 获取用户的所有会话
     */
    List<ConversationEntity> getUserConversations(String userId);
    
    /**
     * 更新会话标题
     */
    void updateConversationTitle(String conversationId, String title);
    
    /**
     * 删除会话
     */
    void deleteConversation(String conversationId);
    
    /**
     * 更新消息数量
     */
    void updateMessageCount(String conversationId);
    
    /**
     * 检查会话是否存在
     */
    boolean exists(String conversationId);
}

package com.jiangye.jiangaiagent.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 会话实体类
 */
@Data
public class ConversationEntity {
    
    /**
     * 会话ID
     */
    private String conversationId;
    
    /**
     * 用户ID
     */
    private String userId;
    
    /**
     * 会话标题
     */
    private String title;
    
    /**
     * 会话类型：love_master/yumanus等
     */
    private String conversationType;
    
    /**
     * 消息数量
     */
    private Integer messageCount;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 最后更新时间
     */
    private LocalDateTime lastUpdateTime;
    
    /**
     * 是否删除：0-未删除，1-已删除
     */
    private Integer deleted;
}

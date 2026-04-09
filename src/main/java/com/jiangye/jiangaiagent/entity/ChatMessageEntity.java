package com.jiangye.jiangaiagent.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 对话消息实体类
 */
@Data
public class ChatMessageEntity {
    
    /**
     * 主键ID
     */
    private Long id;
    
    /**
     * 会话ID
     */
    private String conversationId;
    
    /**
     * 消息角色：user/assistant/system
     */
    private String role;
    
    /**
     * 消息内容
     */
    private String content;
    
    /**
     * 消息类型：text/image/tool_call等
     */
    private String messageType;
    
    /**
     * 消息元数据（JSON格式，存储额外信息）
     */
    private String metadata;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
    /**
     * 是否删除：0-未删除，1-已删除
     */
    private Integer deleted;
}

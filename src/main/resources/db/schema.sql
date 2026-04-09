-- =============================================
-- 对话记忆数据库表结构
-- 用于 Spring AI ChatMemory MySQL 持久化
-- =============================================

-- 会话表
CREATE TABLE IF NOT EXISTS conversation (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    conversation_id VARCHAR(64) NOT NULL COMMENT '会话ID（业务主键）',
    user_id VARCHAR(64) NOT NULL COMMENT '用户ID',
    title VARCHAR(255) DEFAULT '新会话' COMMENT '会话标题',
    conversation_type VARCHAR(32) DEFAULT 'love_master' COMMENT '会话类型：love_master/yumanus',
    message_count INT DEFAULT 0 COMMENT '消息数量',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    last_update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    INDEX idx_user_id (user_id),
    INDEX idx_conversation_id (conversation_id),
    INDEX idx_last_update_time (last_update_time),
    UNIQUE KEY uk_conversation_id (conversation_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='会话表';

-- 对话消息表
CREATE TABLE IF NOT EXISTS chat_message (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    conversation_id VARCHAR(64) NOT NULL COMMENT '会话ID',
    role VARCHAR(32) NOT NULL COMMENT '消息角色：user/assistant/system/tool',
    content TEXT COMMENT '消息内容',
    message_type VARCHAR(32) DEFAULT 'text' COMMENT '消息类型：text/image/tool_call等',
    metadata JSON DEFAULT NULL COMMENT '消息元数据（JSON格式）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    INDEX idx_conversation_id (conversation_id),
    INDEX idx_create_time (create_time),
    FOREIGN KEY (conversation_id) REFERENCES conversation(conversation_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='对话消息表';

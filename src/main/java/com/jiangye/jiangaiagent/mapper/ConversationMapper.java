package com.jiangye.jiangaiagent.mapper;

import com.jiangye.jiangaiagent.entity.ConversationEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 会话Mapper
 */
@Mapper
public interface ConversationMapper {
    
    /**
     * 插入会话
     */
    @Insert("INSERT INTO conversation (conversation_id, user_id, title, conversation_type, message_count, create_time, last_update_time, deleted) " +
            "VALUES (#{conversationId}, #{userId}, #{title}, #{conversationType}, #{messageCount}, #{createTime}, #{lastUpdateTime}, 0)")
    int insert(ConversationEntity conversation);
    
    /**
     * 根据会话ID查询
     */
    @Select("SELECT conversation_id, user_id, title, conversation_type, message_count, create_time, last_update_time, deleted " +
            "FROM conversation WHERE conversation_id = #{conversationId} AND deleted = 0")
    @Results({
        @Result(property = "conversationId", column = "conversation_id"),
        @Result(property = "userId", column = "user_id"),
        @Result(property = "title", column = "title"),
        @Result(property = "conversationType", column = "conversation_type"),
        @Result(property = "messageCount", column = "message_count"),
        @Result(property = "createTime", column = "create_time"),
        @Result(property = "lastUpdateTime", column = "last_update_time"),
        @Result(property = "deleted", column = "deleted")
    })
    ConversationEntity selectByConversationId(String conversationId);
    
    /**
     * 根据用户ID查询会话列表
     */
    @Select("SELECT conversation_id, user_id, title, conversation_type, message_count, create_time, last_update_time, deleted " +
            "FROM conversation WHERE user_id = #{userId} AND deleted = 0 ORDER BY last_update_time DESC")
    @Results({
        @Result(property = "conversationId", column = "conversation_id"),
        @Result(property = "userId", column = "user_id"),
        @Result(property = "title", column = "title"),
        @Result(property = "conversationType", column = "conversation_type"),
        @Result(property = "messageCount", column = "message_count"),
        @Result(property = "createTime", column = "create_time"),
        @Result(property = "lastUpdateTime", column = "last_update_time"),
        @Result(property = "deleted", column = "deleted")
    })
    List<ConversationEntity> selectByUserId(String userId);
    
    /**
     * 更新会话信息
     */
    @Update("UPDATE conversation SET title = #{title}, message_count = #{messageCount}, last_update_time = #{lastUpdateTime} " +
            "WHERE conversation_id = #{conversationId}")
    int update(ConversationEntity conversation);
    
    /**
     * 更新消息数量
     */
    @Update("UPDATE conversation SET message_count = #{messageCount}, last_update_time = NOW() WHERE conversation_id = #{conversationId}")
    int updateMessageCount(@Param("conversationId") String conversationId, @Param("messageCount") int messageCount);
    
    /**
     * 删除会话（逻辑删除）
     */
    @Update("UPDATE conversation SET deleted = 1, last_update_time = NOW() WHERE conversation_id = #{conversationId}")
    int deleteByConversationId(String conversationId);
    
    /**
     * 检查会话是否存在
     */
    @Select("SELECT COUNT(*) FROM conversation WHERE conversation_id = #{conversationId} AND deleted = 0")
    int existsByConversationId(String conversationId);
}

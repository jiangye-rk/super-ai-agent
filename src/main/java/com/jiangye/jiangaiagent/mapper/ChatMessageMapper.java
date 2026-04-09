package com.jiangye.jiangaiagent.mapper;

import com.jiangye.jiangaiagent.entity.ChatMessageEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 对话消息Mapper
 */
@Mapper
public interface ChatMessageMapper {
    
    /**
     * 插入消息
     */
    @Insert("INSERT INTO chat_message (conversation_id, role, content, message_type, metadata, create_time, update_time, deleted) " +
            "VALUES (#{conversationId}, #{role}, #{content}, #{messageType}, #{metadata}, #{createTime}, #{updateTime}, 0)")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(ChatMessageEntity message);
    
    /**
     * 根据会话ID查询消息列表
     */
    @Select("SELECT id, conversation_id, role, content, message_type, metadata, create_time, update_time, deleted " +
            "FROM chat_message WHERE conversation_id = #{conversationId} AND deleted = 0 ORDER BY create_time ASC")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "conversationId", column = "conversation_id"),
        @Result(property = "role", column = "role"),
        @Result(property = "content", column = "content"),
        @Result(property = "messageType", column = "message_type"),
        @Result(property = "metadata", column = "metadata"),
        @Result(property = "createTime", column = "create_time"),
        @Result(property = "updateTime", column = "update_time"),
        @Result(property = "deleted", column = "deleted")
    })
    List<ChatMessageEntity> selectByConversationId(String conversationId);
    
    /**
     * 根据会话ID查询最近N条消息
     */
    @Select("SELECT id, conversation_id, role, content, message_type, metadata, create_time, update_time, deleted " +
            "FROM chat_message WHERE conversation_id = #{conversationId} AND deleted = 0 " +
            "ORDER BY create_time DESC LIMIT #{limit}")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "conversationId", column = "conversation_id"),
        @Result(property = "role", column = "role"),
        @Result(property = "content", column = "content"),
        @Result(property = "messageType", column = "message_type"),
        @Result(property = "metadata", column = "metadata"),
        @Result(property = "createTime", column = "create_time"),
        @Result(property = "updateTime", column = "update_time"),
        @Result(property = "deleted", column = "deleted")
    })
    List<ChatMessageEntity> selectRecentByConversationId(@Param("conversationId") String conversationId, @Param("limit") int limit);
    
    /**
     * 根据会话ID删除消息（逻辑删除）
     */
    @Update("UPDATE chat_message SET deleted = 1, update_time = NOW() WHERE conversation_id = #{conversationId}")
    int deleteByConversationId(String conversationId);
    
    /**
     * 根据ID删除单条消息（逻辑删除）
     */
    @Update("UPDATE chat_message SET deleted = 1, update_time = NOW() WHERE id = #{id}")
    int deleteById(Long id);
    
    /**
     * 统计会话消息数量
     */
    @Select("SELECT COUNT(*) FROM chat_message WHERE conversation_id = #{conversationId} AND deleted = 0")
    int countByConversationId(String conversationId);
}

package com.jiangye.jiangaiagent.controller;

import com.jiangye.jiangaiagent.entity.ConversationEntity;
import com.jiangye.jiangaiagent.service.ConversationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 会话管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/conversation")
@RequiredArgsConstructor
@Tag(name = "会话管理", description = "对话会话的创建、查询、删除等操作")
public class ConversationController {

    private final ConversationService conversationService;

    /**
     * 创建新会话
     */
    @PostMapping("/create")
    @Operation(summary = "创建新会话", description = "为用户创建一个新的对话会话")
    public ResponseEntity<ConversationEntity> createConversation(
            @Parameter(description = "用户ID") @RequestParam String userId,
            @Parameter(description = "会话标题") @RequestParam(required = false, defaultValue = "新会话") String title,
            @Parameter(description = "会话类型：love_master/yumanus") @RequestParam(required = false, defaultValue = "love_master") String conversationType) {
        
        ConversationEntity conversation = conversationService.createConversation(userId, title, conversationType);
        return ResponseEntity.ok(conversation);
    }

    /**
     * 获取会话详情
     */
    @GetMapping("/{conversationId}")
    @Operation(summary = "获取会话详情", description = "根据会话ID获取会话详细信息")
    public ResponseEntity<ConversationEntity> getConversation(
            @Parameter(description = "会话ID") @PathVariable String conversationId) {
        
        ConversationEntity conversation = conversationService.getConversation(conversationId);
        if (conversation == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(conversation);
    }

    /**
     * 获取用户的所有会话
     */
    @GetMapping("/user/{userId}")
    @Operation(summary = "获取用户会话列表", description = "获取指定用户的所有会话")
    public ResponseEntity<List<ConversationEntity>> getUserConversations(
            @Parameter(description = "用户ID") @PathVariable String userId) {
        
        List<ConversationEntity> conversations = conversationService.getUserConversations(userId);
        return ResponseEntity.ok(conversations);
    }

    /**
     * 更新会话标题
     */
    @PutMapping("/{conversationId}/title")
    @Operation(summary = "更新会话标题", description = "修改指定会话的标题")
    public ResponseEntity<Void> updateConversationTitle(
            @Parameter(description = "会话ID") @PathVariable String conversationId,
            @Parameter(description = "新标题") @RequestParam String title) {
        
        conversationService.updateConversationTitle(conversationId, title);
        return ResponseEntity.ok().build();
    }

    /**
     * 删除会话
     */
    @DeleteMapping("/{conversationId}")
    @Operation(summary = "删除会话", description = "删除指定会话及其所有消息")
    public ResponseEntity<Void> deleteConversation(
            @Parameter(description = "会话ID") @PathVariable String conversationId) {
        
        conversationService.deleteConversation(conversationId);
        return ResponseEntity.ok().build();
    }

    /**
     * 检查会话是否存在
     */
    @GetMapping("/{conversationId}/exists")
    @Operation(summary = "检查会话是否存在", description = "检查指定会话ID是否存在")
    public ResponseEntity<Map<String, Boolean>> checkConversationExists(
            @Parameter(description = "会话ID") @PathVariable String conversationId) {
        
        boolean exists = conversationService.exists(conversationId);
        Map<String, Boolean> result = new HashMap<>();
        result.put("exists", exists);
        return ResponseEntity.ok(result);
    }
}

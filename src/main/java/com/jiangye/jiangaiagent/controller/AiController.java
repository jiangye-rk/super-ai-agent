package com.jiangye.jiangaiagent.controller;

import com.jiangye.jiangaiagent.agent.JiangManus;
import com.jiangye.jiangaiagent.app.LoveApp;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.awt.*;

@RestController
@RequestMapping("/ai")
public class AiController {

    @Resource
    private LoveApp loveApp;

    @Resource
    private ToolCallback[] allTools;

    @Resource
    private ChatModel dashscopeChatModel;

    /**
     * 同步调用LoveApp
     * @param message
     * @param chatId
     * @return
     */
    @GetMapping("/love_app/chat/sync")
    public String doChatWithLoveAppSync(String message, String chatId) {
        return loveApp.doChat(message, chatId);
    }

    /**
     * sse 流式（异步）调用LoveApp
     * @param message
     * @param chatId
     * @return
     */
    @GetMapping(value = "/love_app/chat/sse",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> doChatWithLoveAppSSE(String message, String chatId) {
        return loveApp.doChatByStream(chatId, message);
    }

    /**
     * sse 流式（异步）调用LoveApp（使用SseEmitter）更灵活

     * @param message
     * @param chatId
     * @return
     */
    @GetMapping(value = "/love_app/chat/sse_emitter")
       public SseEmitter doChatWithLoveAppSSEmitter(String message, String chatId) {
        //创建一个超时时间较长的SseEmitter
        SseEmitter  emitter = new SseEmitter(180000L);//180 秒超时
        //获取Flux响应数据流并且直接通过订阅推送给SseEmitter
        loveApp.doChatByStream(message, chatId)
                .subscribe(
                        chunk->{
                            try {
                                emitter.send(chunk);
                            }catch (Exception e){
                                emitter.completeWithError(e);
                            }
                        },
                        emitter::completeWithError,
                        emitter::complete
                );
        return emitter;
    }

    /**
     * sse 流式（异步）调用JiangManus
     * @param message
     * @return
     */
    @GetMapping("/manus/chat")
    public SseEmitter doChatWithManus(String message) {
        JiangManus jiangManus = new JiangManus(allTools, dashscopeChatModel);
        return jiangManus.runStream(message);
    }
}

package com.jiangye.jiangaiagent.agent;

import com.jiangye.jiangaiagent.agent.model.AgentState;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import opennlp.tools.util.StringUtil;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 抽象基础代理类，用于管理代理状态和执行流程
 *
 * 提供状态转换、内存管理和基于步骤的执行循环的基础功能
 * 子类必须实现step方法
 */
@Slf4j
@Data
public abstract class BaseAgent {

    //核心属性
    private String name;

    //提示词
    private String systemPrompt;
    private String nextStepPrompt;

    //代理状态
    private AgentState state=AgentState.IDLE;

    //执行管理
    private int currentStep=0;
    private int maxSteps=10;

    //llm 模型
    private ChatClient chatClient;

    //Message管理
    private List<Message> messageList=new ArrayList<>();

    /**
     * 运行代理
     *
     * @param userPrompt 用户提示词
     * @return
     */
    public String run(String userPrompt) {
        if(this.state!=AgentState.IDLE){
            throw new RuntimeException("Cannot run agent from stae:"+state);
        }
        if(StringUtil.isEmpty(userPrompt)){
            throw new RuntimeException("Cannot run agent with empty user prompt");
        }
        this.state=AgentState.RUNNING;

        messageList.add(new UserMessage(userPrompt));

        List<String> results=new ArrayList<>();
        try {
            for (int i = 0; i < maxSteps && state != AgentState.FINISHED; i++) {
                int stepNumber = i + 1;
                currentStep = stepNumber;
                log.info("Executing step " + stepNumber + "/" + maxSteps);

                String stepResult = step();
                String result = "Step " + stepNumber + ": " + stepResult;
                results.add(result);
            }

            if (currentStep >= maxSteps) {
                state = AgentState.FINISHED;
                results.add("Terminated: Reached max steps (" + maxSteps + ")");
            }
            return String.join("\n", results);
        } catch (Exception e) {
            state = AgentState.ERROR;
            log.error("Error executing agent", e);
            return "执行错误" + e.getMessage();
        } finally {

            this.cleanup();
        }
    }

    /**
     * 运行代理并返回流式响应
     * 每个步骤的结果会通过SseEmitter发送给客户端
     * 完成或发生错误时，会发送完成或错误状态
     *
     * @param userPrompt 用户提示词
     * @return
     */
    public SseEmitter runStream(String userPrompt) {

        //创建一个超时时间较长的SseEmitter
        SseEmitter  emitter = new SseEmitter(300000L);//300 秒超时
        CompletableFuture.runAsync(() -> {
           try {
               if(this.state!=AgentState.IDLE){
                   emitter.send("错误：无法在该状态下运行代理："+state);
                   emitter.complete();
                   return  ;
               }
               if(StringUtil.isEmpty(userPrompt)){
                   emitter.send("错误：用户提示词不能为空");
                   emitter.complete();
                   return  ;
               }
               this.state=AgentState.RUNNING;

               messageList.add(new UserMessage(userPrompt));

               try {
                   for (int i = 0; i < maxSteps && state != AgentState.FINISHED; i++) {
                       int stepNumber = i + 1;
                       currentStep = stepNumber;
                       log.info("Executing step " + stepNumber + "/" + maxSteps);

                       String stepResult = step();
                       String result = "Step " + stepNumber + ": " + stepResult;

                       emitter.send(result);
                   }

                   if (currentStep >= maxSteps) {
                       state = AgentState.FINISHED;
                       emitter.send("执行结束：达到最大步骤数 (" + maxSteps + ")");
                   }
                   emitter.complete();

               } catch (Exception e) {
                   state = AgentState.ERROR;
                   log.error("Error executing agent", e);
                   emitter.send("执行错误" + e.getMessage());
                   emitter.complete();

               } finally {

                   this.cleanup();
               }
           }catch (Exception e){
               emitter.completeWithError(e);
           }
        });
        //设置超时回调
        emitter.onTimeout(() -> {
            this.state=AgentState.ERROR;
            this.cleanup();
            log.warn("SSE connection timeout");
        });
        //设置完成回调
        emitter.onCompletion(() -> {
            if(this.state==AgentState.RUNNING){
                this.state=AgentState.FINISHED;
            }
            this.cleanup();
            log.info("SSE connection completed");
        });
        return emitter;
    }

    /**
     * 定义单个步骤
     * @return
     */
    public abstract String step();

    /**
     * 清理资源
     */
    protected void cleanup(){
        //子类可以重写此方法，用于清理资源
    }
}

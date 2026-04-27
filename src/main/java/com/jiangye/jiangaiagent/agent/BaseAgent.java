package com.jiangye.jiangaiagent.agent;

import com.jiangye.jiangaiagent.agent.model.AgentState;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import opennlp.tools.util.StringUtil;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;

import java.util.ArrayList;
import java.util.List;

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
    private List<Message> messagesList=new ArrayList<>();

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

        messagesList.add(new UserMessage(userPrompt));

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

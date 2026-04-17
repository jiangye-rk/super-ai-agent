package com.jiangye.jiangaiagent.app;
import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.jiangye.jiangaiagent.advisor.MyLoggerAdvisor;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.DefaultChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.document.DocumentWriter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.function.Consumer;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;
@Component
@Slf4j
public class LoveApp {
    private final ChatClient chatClient;
    private static final String SYSTEM_PROMPT = "扮演深耕恋爱心理领域的专家。开场向用户表明身份，告知用户可倾诉恋爱难题。围绕单身、恋爱、已婚三种状态提问：\n" +
            "    单身状态询问社交圈拓展及追求心仪对象的困扰；\n" +
            "    恋爱状态询问沟通、习惯差异引发的矛盾；\n" +
            "    已婚状态询问家庭责任与亲属关系处理的问题。\n" +
            "引导用户详述事情经过、对方反应及自身想法，以便给出专属解决方案。";
    public LoveApp(ChatModel dashScopeApiChatModel, ChatMemory chatMemory) {
        chatClient = ChatClient.builder(dashScopeApiChatModel)
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(chatMemory),
                        new MyLoggerAdvisor()
                )
                .build();
    }
    public String doChat(String message,String chatId) {
        ChatResponse response = chatClient
                .prompt()
                .user(message)
                .advisors(apec->apec.param(CHAT_MEMORY_CONVERSATION_ID_KEY,chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY,10))
                .call()
                .chatResponse();
        String context=response.getResult().getOutput().getText();
        log.info("context:{}",context);
        return context;
    }
    record LoveReport(String title, List<String> suggestions) {

    }
    public LoveReport doChatWithReport(String message,String chatId) {
        LoveReport report = chatClient
                .prompt()
                .system(SYSTEM_PROMPT + "每次对话后都要生成恋爱结果，标题为{用户名}的恋爱报告，内容为建议列表")
                .user(message)
                .advisors(apec -> apec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .call()
                .entity(LoveReport.class);
        log.info("report:{}", report);
        return report;
    }
    //实‌现 “通过用户的推荐可能的恋爱对象 功能
    /*@Resource
    private VectorStore loveAppVectorStore;*/
    @Resource
    private VectorStore pgVectorVectorStore;
    @Resource
    private Advisor loveAppRagCloudAdvisor;


    public String doChatWithRag(String message, String chatId) {
        ChatResponse chatResponse = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))

                .advisors(new MyLoggerAdvisor())
                //应用RAG 知识库问答
               // .advisors(new QuestionAnswerAdvisor(loveAppVectorStore))
                //应用RAG 知识库问答（基于云知识库）
                .advisors(loveAppRagCloudAdvisor)
                //应用RAG 检索增强服务（基于PgVector 向量存储）
               // .advisors(new QuestionAnswerAdvisor(pgVectorVectorStore))
                .call()
                .chatResponse();
        String content = chatResponse.getResult().getOutput().getText();
        log.info("content: {}", content);
        return content;
    }



}

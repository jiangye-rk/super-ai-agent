package com.jiangye.jiangaiagent.app;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class LoveAppTest {
    @Resource
    private  LoveApp loveApp;
    @Test
    void doChat() {
        String chatId= UUID.randomUUID().toString();
        String message="你好，我是程序员小江";
        String answer=loveApp.doChat(message,chatId);
        Assertions.assertNotNull(answer);

        message="我想让我的另一半小琴更喜欢我";
        answer=loveApp.doChat(message,chatId);
        Assertions.assertNotNull(answer);

        message="我的另一半叫什么来着？刚跟你说过，带我回忆一下";
        answer=loveApp.doChat(message,chatId);
        Assertions.assertNotNull(answer);

    }
}
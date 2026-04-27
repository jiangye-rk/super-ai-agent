package com.jiangye.jiangaiagent.agent.model;

public enum AgentState {
   /**
    * 空闲状态
    * 无任务，等待新任务
    */
    IDLE,
   /**
    * 运行状态
    * 有任务，正在执行
    */
    RUNNING,
   /**
    * 完成状态
    * 任务执行完成
    */
    FINISHED,
   /**
    * 错误状态
    * 任务执行过程中发生错误
    */
       ERROR
}

# 🤖 Jiang AI Agent - AI 恋爱大师 & JiangManus 智能体

<p align="center">
  <img src="https://img.shields.io/badge/Spring%20Boot-3.5.13-brightgreen?logo=spring" alt="Spring Boot">
  <img src="https://img.shields.io/badge/Java-21-orange?logo=openjdk" alt="Java 21">
  <img src="https://img.shields.io/badge/Spring%20AI-1.0.0--M6-blue?logo=spring" alt="Spring AI">
  <img src="https://img.shields.io/badge/Vue-3-4FC08D?logo=vue.js" alt="Vue 3">
  <img src="https://img.shields.io/badge/License-MIT-yellow.svg" alt="License">
</p>

<p align="center">
  <b>基于 Spring Boot 3 + Java 21 + Spring AI 的智能体应用</b>
</p>

<p align="center">
  💕 AI 恋爱大师 · 🔧 JiangManus 智能体 · 🧠 ReAct 模式 · 📚 RAG 增强
</p>

---

## 📋 目录

- [项目简介](#-项目简介)
- [功能演示](#-功能演示)
- [技术栈](#-技术栈)
- [项目结构](#-项目结构)
- [快速开始](#-快速开始)
- [API 接口](#-api-接口)
- [配置说明](#-配置说明)
- [开发指南](#-开发指南)
- [常见问题](#-常见问题)
- [技术亮点](#-技术亮点)

---

## ✨ 项目简介

Jiang AI Agent 是一个基于 **Spring AI** 的智能体应用，实现了两个核心功能：

| 功能模块 | 描述 | 特点 |
|---------|------|------|
| 💕 **AI 恋爱大师** | 提供情感建议、对话练习、约会策划等恋爱辅助功能 | 对话记忆、RAG 知识库、恋爱报告生成 |
| 🔧 **YuManus 智能体** | 基于 **ReAct 模式** 的自主规划智能体 | 工具调用、多步推理、MCP 协议支持 |

---

## 🎬 功能演示

> 📸 **截图占位区** - 建议添加以下截图：
> 
> 1. 恋爱大师聊天界面 (`jiang-ai-agent-frontend/src/views/LoveChat.vue`)
> 2. YuManus 智能体界面 (`jiang-ai-agent-frontend/src/views/ManusChat.vue`)
> 3. API 文档界面 (Swagger UI)

```markdown
![恋爱大师界面](./docs/images/love-chat.png)
![YuManus 界面](./docs/images/manus-chat.png)
```

### 核心功能展示

```
💕 AI 恋爱大师
├── 单身状态咨询 → 社交圈拓展、追求建议
├── 恋爱状态咨询 → 沟通技巧、矛盾化解
└── 已婚状态咨询 → 家庭责任、亲属关系

🔧 YuManus 智能体 (ReAct 模式)
├── 任务理解 → 分析用户需求
├── 工具选择 → 调用合适工具
├── 执行观察 → 获取执行结果
└── 总结输出 → 生成最终回答
```

---

## 🛠️ 技术栈

### 后端技术

| 技术 | 版本 | 用途 |
|------|------|------|
| **Spring Boot** | 3.5.13 | 核心框架 |
| **Java** | 21 | 编程语言（Record、虚拟线程） |
| **Spring AI** | 1.0.0-M6 | AI 开发框架 |
| **阿里云 DashScope** | 2.19.3 | 大模型接入（通义千问） |
| **PgVector** | - | 向量数据库（RAG） |
| **MySQL** | 8.0+ | 对话记忆存储 |
| **MyBatis** | 3.0+ | ORM 框架 |
| **MCP** | - | 模型上下文协议 |

### 前端技术

| 技术 | 版本 | 用途 |
|------|------|------|
| **Vue** | 3.4+ | 前端框架 |
| **Vue Router** | 4.3+ | 路由管理 |
| **Vite** | 5.2+ | 构建工具 |
| **Axios** | 1.6+ | HTTP 客户端 |

---

## 📁 项目结构

```
jiang-ai-agent/
├── 📂 src/main/java/com/jiangye/jiangaiagent/
│   ├── 📂 agent/                    # 🤖 ReAct 智能体核心
│   │   ├── JiangManus.java          # YuManus 主智能体
│   │   ├── ReActAgent.java          # ReAct 模式基类
│   │   ├── ToolCallAgent.java       # 工具调用智能体
│   │   └── model/AgentState.java    # 智能体状态
│   ├── 📂 app/
│   │   └── LoveApp.java             # 💕 AI 恋爱大师应用
│   ├── 📂 chatmemory/
│   │   └── MysqlChatMemory.java     # MySQL 对话记忆实现
│   ├── 📂 config/                   # ⚙️ 配置类
│   ├── 📂 controller/               # 🌐 REST API 接口
│   ├── 📂 rag/                      # 📚 RAG 检索增强
│   ├── 📂 service/                  # 🏢 业务逻辑层
│   └── 📂 tools/                    # 🛠️ 工具集合
│       ├── FileOperationTool.java
│       ├── PDFGenerationTool.java
│       ├── WebSearchTool.java
│       ├── WebScrapingTool.java
│       └── ...
├── 📂 src/main/resources/
│   ├── 📂 document/                 # 💌 恋爱知识库文档
│   ├── 📂 db/schema.sql             # 🗄️ 数据库初始化脚本
│   └── application.yml              # ⚙️ 应用配置
├── 📂 jiang-ai-agent-frontend/      # 🎨 Vue 前端项目
│   ├── 📂 src/views/
│   │   ├── LoveChat.vue             # 恋爱大师聊天界面
│   │   └── ManusChat.vue            # YuManus 智能体界面
│   └── ...
└── 📂 image-search-mcp-server/      # 🔍 MCP 图片搜索服务
```

---

## 🚀 快速开始

### 环境要求

- ☕ JDK 21+
- 📦 Maven 3.9+
- 🐬 MySQL 8.0+
- 🐘 PostgreSQL 14+ (with PgVector 扩展)
- 🟢 Node.js 18+ (前端)

### 后端启动

1. **配置数据库**
   ```yaml
   # src/main/resources/application.yml
   spring:
     datasource:
       mysql:
         url: jdbc:mysql://localhost:3306/your-db-name
         username: your-username
         password: your-password
       postgresql:
         url: jdbc:postgresql://your-host/yu_ai_agent
         username: your-username
         password: your-password
     ai:
       dashscope:
         api-key: your-dashscope-api-key
   ```

2. **初始化数据库**
   ```bash
   mysql -u root -p < src/main/resources/db/schema.sql
   ```

3. **启动应用**
   ```bash
   # macOS / Linux
   ./mvnw spring-boot:run
   
   # Windows
   mvnw.cmd spring-boot:run
   ```

### 前端启动

```bash
cd jiang-ai-agent-frontend
npm install
npm run dev
```

访问 http://localhost:5173 查看前端界面。

---

## 🔌 API 接口

### 💕 恋爱大师接口

```bash
# 恋爱对话
POST /api/ai/love/chat
Content-Type: application/json

{
  "message": "如何向喜欢的人表白？",
  "conversationId": "conv_123"
}
```

### 🔧 YuManus 智能体接口

```bash
# 智能体对话
POST /api/ai/manus/chat
Content-Type: application/json

{
  "message": "搜索 Spring AI 最新版本信息并生成 PDF 报告",
  "conversationId": "conv_456"
}
```

### 💬 对话管理接口

```bash
# 获取对话历史
GET /api/conversations/{conversationId}/messages

# 创建新对话
POST /api/conversations
```

📘 **完整 API 文档**: 启动后访问 http://localhost:8123/api/swagger-ui.html

---

## ⚙️ 配置说明

### 大模型配置

```yaml
spring:
  ai:
    dashscope:
      api-key: ${DASHSCOPE_API_KEY}
      chat:
        options:
          model: qwen-plus-latest  # 或 qwen-max, qwen-turbo
      embedding:
        options:
          model: text-embedding-v4
```

### 向量数据库配置

```yaml
spring:
  ai:
    vectorstore:
      pgvector:
        index-type: HNSW
        distance-type: COSINE_DISTANCE
        dimensions: 1536
```

### MCP 服务配置

```json
{
  "mcpServers": {
    "image-search": {
      "command": "java",
      "args": ["-jar", "image-search-mcp-server.jar"]
    }
  }
}
```

---

## 🛠️ 开发指南

### 添加新工具

1. 实现 `ToolCallback` 接口或使用 `@Tool` 注解
2. 在 `ToolRegistration` 中注册工具
3. 重启应用即可生效

### 自定义 Advisor

```java
@Component
public class CustomAdvisor implements Advisor {
    @Override
    public AdvisedRequest advise(AdvisedRequest request, Map<String, Object> context) {
        // 自定义处理逻辑
        return request;
    }
}
```

### RAG 知识库扩展

将 Markdown 文档放入 `src/main/resources/document/` 目录，应用启动时自动加载到向量数据库。

---

## ❓ 常见问题

### 1. PgVector 扩展未安装

**问题**: 启动时报错 `ERROR: extension "vector" is not available`

**解决**:
```sql
-- 在 PostgreSQL 中执行
CREATE EXTENSION IF NOT EXISTS vector;
```

### 2. DashScope API Key 无效

**问题**: 调用模型时报 `401 Unauthorized`

**解决**: 
- 检查 `application.yml` 中的 `api-key` 配置
- 确保 API Key 有对应模型的调用权限
- 在阿里云控制台查看额度使用情况

### 3. 前端跨域问题

**问题**: 前端调用 API 报 CORS 错误

**解决**: 检查 `CorsConfig.java` 配置，确保允许前端域名访问。

### 4. 向量检索结果为空

**问题**: RAG 查询无结果返回

**解决**:
- 检查文档是否已正确加载到 PgVector
- 确认 `text-embedding-v4` 模型调用正常
- 调整相似度阈值参数

---

## ⭐ 技术亮点

1. **🧠 ReAct 模式实现** - 自主规划与工具调用的完整闭环
2. **💾 多模态记忆** - MySQL 持久化 + 向量检索的混合记忆方案
3. **🔌 MCP 协议支持** - 标准化的模型上下文扩展机制
4. **⚡ 流式响应** - 支持 SSE 实时输出
5. **📋 结构化输出** - 使用 Java Record 实现 JSON Schema 约束

---

## 📄 许可证

[MIT License](LICENSE)

---

## 👤 贡献者

- **项目作者**: jiangye

---

<p align="center">
  💕 愿每个人都能找到属于自己的幸福 · Made with ❤️ by jiangye
</p>

<p align="center">
  <b>注意</b>: 本项目仅供学习研究使用，恋爱建议仅供参考，请理性对待 AI 生成的内容。
</p>
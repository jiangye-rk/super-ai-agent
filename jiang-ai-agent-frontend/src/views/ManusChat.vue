<template>
  <div class="chat-container">
    <div class="chat-header">
      <button class="back-btn" @click="goBack">← 返回</button>
      <div class="header-info">
        <h2>🧠 AI 超级智能体</h2>
        <span class="subtitle">基于 ReAct 模式的自主规划智能体</span>
      </div>
    </div>
    
    <div class="chat-messages" ref="messagesContainer">
      <div
        v-for="(msg, index) in messages"
        :key="index"
        :class="['message', msg.role === 'user' ? 'user-message' : 'ai-message']"
      >
        <div class="message-avatar">
          {{ msg.role === 'user' ? '👤' : '🧠' }}
        </div>
        <div class="message-content">
          <div class="message-text" v-html="formatMessage(msg.content)"></div>
          <div v-if="msg.toolCalls && msg.toolCalls.length > 0" class="tool-calls">
            <div v-for="(tool, tIndex) in msg.toolCalls" :key="tIndex" class="tool-call">
              <div class="tool-header">
                <span class="tool-icon">🔧</span>
                <span class="tool-name">{{ tool.name }}</span>
              </div>
              <div class="tool-result">{{ tool.result }}</div>
            </div>
          </div>
          <div class="message-time">{{ msg.time }}</div>
        </div>
      </div>
      <div v-if="isLoading" class="message ai-message">
        <div class="message-avatar">🧠</div>
        <div class="message-content">
          <div class="typing-indicator">
            <span></span>
            <span></span>
            <span></span>
          </div>
        </div>
      </div>
    </div>
    
    <div class="chat-input-area">
      <div class="input-container">
        <input
          v-model="inputMessage"
          type="text"
          placeholder="输入您的任务或问题，我将使用工具为您解决..."
          @keyup.enter="sendMessage"
          :disabled="isLoading"
        />
        <button
          class="send-btn"
          @click="sendMessage"
          :disabled="!inputMessage.trim() || isLoading"
        >
          发送
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import { API_ENDPOINTS, getApiUrl } from '../config/api.js'

const router = useRouter()
const messagesContainer = ref(null)
const inputMessage = ref('')
const messages = ref([])
const isLoading = ref(false)

// 格式化时间
const formatTime = () => {
  const now = new Date()
  return `${now.getHours().toString().padStart(2, '0')}:${now.getMinutes().toString().padStart(2, '0')}`
}

// 格式化消息内容
const formatMessage = (content) => {
  return content.replace(/\n/g, '<br>')
}

// 滚动到底部
const scrollToBottom = () => {
  nextTick(() => {
    if (messagesContainer.value) {
      messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
    }
  })
}

// 返回主页
const goBack = () => {
  router.push('/')
}

// 解析 SSE 数据
const parseSseData = (data) => {
  try {
    // 尝试解析为 JSON（可能包含工具调用信息）
    const parsed = JSON.parse(data)
    return parsed
  } catch {
    // 普通文本消息
    return { type: 'text', content: data }
  }
}

// 发送消息
const sendMessage = async () => {
  const message = inputMessage.value.trim()
  if (!message || isLoading.value) return

  // 添加用户消息
  messages.value.push({
    role: 'user',
    content: message,
    time: formatTime()
  })
  
  inputMessage.value = ''
  isLoading.value = true
  scrollToBottom()

  // 创建 AI 消息占位
  const aiMessageIndex = messages.value.length
  messages.value.push({
    role: 'assistant',
    content: '',
    toolCalls: [],
    time: formatTime()
  })

  try {
    // 使用 SSE 连接
    const eventSource = new EventSource(
      getApiUrl(API_ENDPOINTS.MANUS_CHAT, { message: message })
    )

    eventSource.onmessage = (event) => {
      const data = event.data
      if (data === '[DONE]') {
        eventSource.close()
        isLoading.value = false
        return
      }

      const parsed = parseSseData(data)
      
      if (parsed.type === 'tool_call') {
        // 工具调用信息
        messages.value[aiMessageIndex].toolCalls.push({
          name: parsed.tool_name || '工具调用',
          result: parsed.content || parsed.result || ''
        })
      } else if (parsed.type === 'text') {
        // 普通文本
        messages.value[aiMessageIndex].content += parsed.content
      } else {
        // 默认处理
        messages.value[aiMessageIndex].content += data
      }
      
      scrollToBottom()
    }

    eventSource.onerror = (error) => {
      console.error('SSE Error:', error)
      eventSource.close()
      isLoading.value = false
      if (!messages.value[aiMessageIndex].content) {
        messages.value[aiMessageIndex].content = '抱歉，连接出现问题，请稍后重试。'
      }
    }
  } catch (error) {
    console.error('Error:', error)
    messages.value[aiMessageIndex].content = '抱歉，发送消息失败，请稍后重试。'
    isLoading.value = false
  }
}

onMounted(() => {
  // 添加欢迎消息
  messages.value.push({
    role: 'assistant',
    content: '你好！我是 AI 超级智能体 🧠\n\n我基于 ReAct 模式构建，具备以下能力：\n• 自主规划和任务分解\n• 多工具调用与协作\n• 多步推理与反思\n• 实时任务执行反馈\n\n请告诉我你需要完成的任务，我会自动规划并执行！',
    time: formatTime()
  })
})
</script>

<style scoped>
.chat-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background: #f5f5f5;
}

.chat-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 15px 20px;
  display: flex;
  align-items: center;
  gap: 15px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.back-btn {
  background: rgba(255, 255, 255, 0.2);
  border: none;
  color: white;
  padding: 8px 15px;
  border-radius: 20px;
  cursor: pointer;
  font-size: 0.9rem;
  transition: all 0.3s;
}

.back-btn:hover {
  background: rgba(255, 255, 255, 0.3);
}

.header-info h2 {
  font-size: 1.3rem;
  margin: 0;
}

.subtitle {
  font-size: 0.75rem;
  opacity: 0.8;
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.message {
  display: flex;
  gap: 10px;
  max-width: 80%;
}

.user-message {
  align-self: flex-end;
  flex-direction: row-reverse;
}

.ai-message {
  align-self: flex-start;
}

.message-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.2rem;
  background: white;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  flex-shrink: 0;
}

.message-content {
  background: white;
  padding: 12px 16px;
  border-radius: 18px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.05);
}

.user-message .message-content {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.message-text {
  line-height: 1.5;
  word-break: break-word;
}

.message-time {
  font-size: 0.7rem;
  opacity: 0.6;
  margin-top: 5px;
}

.tool-calls {
  margin-top: 10px;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.tool-call {
  background: #f8f9fa;
  border-left: 3px solid #667eea;
  padding: 10px;
  border-radius: 0 8px 8px 0;
  font-size: 0.85rem;
}

.tool-header {
  display: flex;
  align-items: center;
  gap: 5px;
  margin-bottom: 5px;
  color: #667eea;
  font-weight: 600;
}

.tool-result {
  color: #666;
  font-family: monospace;
  white-space: pre-wrap;
  word-break: break-word;
}

.typing-indicator {
  display: flex;
  gap: 5px;
  padding: 5px 0;
}

.typing-indicator span {
  width: 8px;
  height: 8px;
  background: #ccc;
  border-radius: 50%;
  animation: typing 1.4s infinite;
}

.typing-indicator span:nth-child(2) {
  animation-delay: 0.2s;
}

.typing-indicator span:nth-child(3) {
  animation-delay: 0.4s;
}

@keyframes typing {
  0%, 60%, 100% {
    transform: translateY(0);
  }
  30% {
    transform: translateY(-10px);
  }
}

.chat-input-area {
  background: white;
  padding: 15px 20px;
  border-top: 1px solid #eee;
}

.input-container {
  display: flex;
  gap: 10px;
  max-width: 800px;
  margin: 0 auto;
}

.input-container input {
  flex: 1;
  padding: 12px 20px;
  border: 2px solid #eee;
  border-radius: 25px;
  font-size: 1rem;
  outline: none;
  transition: border-color 0.3s;
}

.input-container input:focus {
  border-color: #667eea;
}

.send-btn {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  padding: 12px 30px;
  border-radius: 25px;
  font-size: 1rem;
  cursor: pointer;
  transition: all 0.3s;
}

.send-btn:hover:not(:disabled) {
  transform: scale(1.05);
  box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
}

.send-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

@media (max-width: 768px) {
  .message {
    max-width: 90%;
  }
  
  .chat-header {
    padding: 10px 15px;
  }
  
  .header-info h2 {
    font-size: 1.1rem;
  }
}
</style>

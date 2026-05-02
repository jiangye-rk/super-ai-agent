<template>
  <div class="home-container">
    <h1 class="title">🤖 AI 智能应用平台</h1>
    <p class="subtitle">选择您想要体验的智能应用</p>
    
    <div class="app-cards">
      <div class="app-card" @click="goToLoveChat">
        <div class="card-icon">💕</div>
        <h2>AI 恋爱大师</h2>
        <p>专业的情感顾问，为您提供恋爱建议、情感分析和约会指导</p>
        <div class="card-footer">
          <span class="tag">情感咨询</span>
          <span class="tag">恋爱建议</span>
        </div>
      </div>
      
      <div class="app-card" @click="goToManusChat">
        <div class="card-icon">🧠</div>
        <h2>AI 超级智能体</h2>
        <p>基于 ReAct 模式的自主规划智能体，具备工具调用和多步推理能力</p>
        <div class="card-footer">
          <span class="tag">智能体</span>
          <span class="tag">工具调用</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

const goToLoveChat = () => {
  router.push('/love-chat')
}

const goToManusChat = () => {
  router.push('/manus-chat')
}
</script>

<style scoped>
.home-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 40px 20px;
}

.title {
  font-size: 3rem;
  color: white;
  margin-bottom: 10px;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.2);
}

.subtitle {
  font-size: 1.2rem;
  color: rgba(255, 255, 255, 0.9);
  margin-bottom: 50px;
}

.app-cards {
  display: flex;
  gap: 30px;
  flex-wrap: wrap;
  justify-content: center;
  max-width: 1000px;
}

.app-card {
  background: white;
  border-radius: 20px;
  padding: 40px;
  width: 380px;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);
}

.app-card:hover {
  transform: translateY(-10px);
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
}

.card-icon {
  font-size: 4rem;
  margin-bottom: 20px;
}

.app-card h2 {
  font-size: 1.8rem;
  color: #333;
  margin-bottom: 15px;
}

.app-card p {
  color: #666;
  line-height: 1.6;
  margin-bottom: 25px;
}

.card-footer {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
}

.tag {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 6px 14px;
  border-radius: 20px;
  font-size: 0.85rem;
}

@media (max-width: 768px) {
  .title {
    font-size: 2rem;
  }
  
  .app-card {
    width: 100%;
    max-width: 350px;
  }
}
</style>

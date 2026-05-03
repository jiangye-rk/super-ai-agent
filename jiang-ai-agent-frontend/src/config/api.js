// API 基础配置
const API_BASE_URL = process.env.NODE_ENV === 'production'
  ? '/api'
  : 'http://localhost:8123/api'

// API 端点配置
export const API_ENDPOINTS = {
  // AI 恋爱大师相关接口
  LOVE_CHAT: `${API_BASE_URL}/ai/love_app/chat/sse`,

  // AI 超级智能体相关接口
  MANUS_CHAT: `${API_BASE_URL}/ai/manus/chat`
}

// 获取完整的 API URL
export function getApiUrl(endpoint, params = {}) {
  let url = endpoint

  // 添加查询参数
  const queryParams = new URLSearchParams()
  Object.entries(params).forEach(([key, value]) => {
    if (value !== undefined && value !== null) {
      queryParams.append(key, value)
    }
  })

  const queryString = queryParams.toString()
  if (queryString) {
    url += `?${queryString}`
  }

  return url
}

export { API_BASE_URL }
export default API_BASE_URL

<template>
  <div class="chat-container">
    <!-- 显示聊天记录 -->
    <div v-for="(item, index) in chatHistory" :key="index" class="chat-item">
      <div v-if="item.role === 'user'" class="chat-message user-message">
        <p>{{ item.content }}</p>
      </div>
      <div v-if="item.role === 'assistant'" class="chat-message assistant-message">
        <p>{{ item.content }}</p>
      </div>
    </div>
    <div v-if="isSending" class="loading">请求处理中...</div>
    <div class="chat-box">
      <input v-model="userQuestion" placeholder="请输入你的问题" class="question-input" />
      <button @click="sendQuestionToOpenAI" :disabled="isSending" class="submit-btn">向AI提问</button>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      userQuestion: '',         // 用户输入的提问
      isSending: false,         // 请求状态
      chatHistory: [],          // 存储聊天记录
    };
  },
  methods: {
    async sendQuestionToOpenAI() {
      // 防止重复发送请求
      if (this.isSending || !this.userQuestion.trim()) return;

      this.isSending = true; // 设置请求状态为正在发送
      const question = this.userQuestion;

      // 将用户的问题添加到聊天记录中
      this.chatHistory.push({ role: 'user', content: question });

      try {
        // 请求后端 Spring Boot 服务器
        const response = await axios.post('http://localhost:8080/api/openai/ask', question);

        // 显示 AI 响应
        const aiResponse = response.data;  // 假设返回的是完整的聊天数据对象
        
        // 将 AI 的回答添加到聊天记录中
        this.chatHistory.push({
          role: 'assistant',
          content: aiResponse.choices[0].message.content,  // 从返回的 JSON 中提取内容
        });
        
        // 清空输入框
        this.userQuestion = '';
      } catch (error) {
        console.error(error);
        this.chatHistory.push({
          role: 'assistant',
          content: '很抱歉，出现了错误，请稍后再试。',
        });
      } finally {
        this.isSending = false; // 请求结束
      }
    }
  }
};
</script>

<style scoped>
.chat-container {
  width: 100%;
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
  box-sizing: border-box;
}

.chat-box {
  display: flex;
  flex-direction: column;
}

.question-input {
  padding: 10px;
  font-size: 14px;
  border: 1px solid #ccc;
  border-radius: 4px;
  margin-bottom: 10px;
}

.submit-btn {
  padding: 10px;
  font-size: 14px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.submit-btn:disabled {
  background-color: #cccccc;
}

.submit-btn:hover {
  background-color: #0056b3;
}

.chat-item {
  margin-top: 10px;
  margin-bottom: 10px;
}

.chat-message {
  padding: 10px;
  border-radius: 4px;
}

.user-message {
  background-color: #e1f5fe;
  align-self: flex-start;
  border: 1px solid #b3e5fc;
}

.assistant-message {
  background-color: #f1f8e9;
  align-self: flex-end;
  border: 1px solid #c8e6c9;
}

.loading {
  font-size: 16px;
  color: #007bff;
  margin-top: 10px;
}
</style>

// src/services/openaiService.js
import OpenAI from 'openai';
import { openaiConfig } from '@/api/openai';

const openai = new OpenAI({
  apiKey: openaiConfig.apiKey,
  dangerouslyAllowBrowser: true, // 允许在浏览器中运行
});

export async function sendMessage(message) {
  try {
    const response = await openai.chat.completions.create({
      model: openaiConfig.model,
      messages: [{ role: 'user', content: message }],
    });

    return response.choices[0].message.content;
  } catch (error) {
    console.error('Error sending message:', error);
    return 'Sorry, I encountered an error.';
  }
}
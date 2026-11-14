import api from './api';

export const aiService = {
  async askAssistant(payload) {
    return api.post('/ai/assistant', payload);
  }
};

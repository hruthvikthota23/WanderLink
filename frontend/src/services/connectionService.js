import api from './api';

export const connectionService = {
  async sendConnection(receiverId, message) {
    const response = await api.post('/connections', { receiverId, message });
    return response.data;
  },

  async getUserConnections(userId) {
    const response = await api.get(`/connections/user/${userId}`);
    return response.data;
  },

  async acceptConnection(id) {
    const response = await api.put(`/connections/${id}/accept`);
    return response.data;
  },

  async rejectConnection(id) {
    const response = await api.put(`/connections/${id}/reject`);
    return response.data;
  }
};

import api from './api';

export const authService = {
  async register(userData) {
    const response = await api.post('/auth/register', userData);
    return response.data;
  },

  async login(email, password) {
    const response = await api.post('/auth/login', { email, password });
    if (response?.token) {
      localStorage.setItem('wanderlink_token', response.token);
      localStorage.setItem('wanderlink_user', JSON.stringify(response.user));
    }
    return response;
  },

  async getMe() {
    const response = await api.get('/auth/me');
    if (response.data) {
      localStorage.setItem('wanderlink_user', JSON.stringify(response.data));
    }
    return response.data;
  },

  logout() {
    localStorage.removeItem('wanderlink_token');
    localStorage.removeItem('wanderlink_user');
  },

  getCurrentUser() {
    const userStr = localStorage.getItem('wanderlink_user');
    return userStr ? JSON.parse(userStr) : null;
  },

  getToken() {
    return localStorage.getItem('wanderlink_token');
  },

  isAuthenticated() {
    return !!localStorage.getItem('wanderlink_token');
  }
};

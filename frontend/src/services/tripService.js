import api from './api';

export const tripService = {
  async getAllTrips() {
    const response = await api.get('/trips');
    return response.data;
  },

  async getTripById(id) {
    const response = await api.get(`/trips/${id}`);
    return response.data;
  },

  async getUserTrips(userId) {
    const response = await api.get(`/trips/user/${userId}`);
    return response.data;
  },

  async createTrip(tripData) {
    const response = await api.post('/trips', tripData);
    return response.data;
  },

  async updateTrip(id, tripData) {
    const response = await api.put(`/trips/${id}`, tripData);
    return response.data;
  },

  async deleteTrip(id) {
    const response = await api.delete(`/trips/${id}`);
    return response.data;
  }
};

import api from './api';

export const matchingService = {
  async getMatchesForTrip(tripId) {
    const response = await api.get(`/matches?tripId=${tripId}`);
    return response.data;
  }
};

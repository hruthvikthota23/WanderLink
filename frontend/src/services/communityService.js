import api from './api';

export const communityService = {
  // Accommodations
  async getAccommodations(location, maxPrice) {
    let url = '/accommodations';
    const params = new URLSearchParams();
    if (location) params.append('location', location);
    if (maxPrice) params.append('maxPrice', maxPrice);
    if (params.toString()) url += `?${params.toString()}`;
    const response = await api.get(url);
    return response.data;
  },

  async getAccommodationById(id) {
    const response = await api.get(`/accommodations/${id}`);
    return response.data;
  },

  async createAccommodation(data) {
    const response = await api.post('/accommodations', data);
    return response.data;
  },

  // Food
  async getFoodListings(location, cuisine) {
    let url = '/food';
    const params = new URLSearchParams();
    if (location) params.append('location', location);
    if (cuisine) params.append('cuisine', cuisine);
    if (params.toString()) url += `?${params.toString()}`;
    const response = await api.get(url);
    return response.data;
  },

  async getFoodListingById(id) {
    const response = await api.get(`/food/${id}`);
    return response.data;
  },

  async createFoodListing(data) {
    const response = await api.post('/food', data);
    return response.data;
  },

  // Guides
  async getGuides(destination) {
    let url = '/guides';
    if (destination) url += `?destination=${encodeURIComponent(destination)}`;
    const response = await api.get(url);
    return response.data;
  },

  async getGuideById(id) {
    const response = await api.get(`/guides/${id}`);
    return response.data;
  },

  async createGuide(data) {
    const response = await api.post('/guides', data);
    return response.data;
  },

  // Groups
  async getGroups(destination) {
    let url = '/groups';
    if (destination) url += `?destination=${encodeURIComponent(destination)}`;
    const response = await api.get(url);
    return response.data;
  },

  async getGroupById(id) {
    const response = await api.get(`/groups/${id}`);
    return response.data;
  },

  async createGroup(data) {
    const response = await api.post('/groups', data);
    return response.data;
  },

  async joinGroup(id) {
    const response = await api.post(`/groups/${id}/join`);
    return response.data;
  },

  async leaveGroup(id) {
    const response = await api.post(`/groups/${id}/leave`);
    return response.data;
  },

  async getGroupMembers(id) {
    const response = await api.get(`/groups/${id}/members`);
    return response.data;
  }
};

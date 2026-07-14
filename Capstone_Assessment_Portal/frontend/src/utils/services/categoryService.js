import api from "../api";
import API_ENDPOINTS from "../constants";


export const getCategories = async () => {
  const response = await api.get(API_ENDPOINTS.CATEGORY.GET_ALL);
  return response.data;
};

export const getCategoryById = async (categoryId) => {
  const response = await api.get(API_ENDPOINTS.CATEGORY.GET_BY_ID(categoryId));
  return response.data;
};

export const createCategory = async (categoryData) => {
  const response = await api.post(API_ENDPOINTS.CATEGORY.CREATE, categoryData);
  return response.data;
};

export const updateCategory = async (categoryId, categoryData) => {
  const response = await api.put(API_ENDPOINTS.CATEGORY.UPDATE(categoryId), categoryData);
  return response.data;
};

export const deleteCategory = async (categoryId) => {
  const response = await api.delete(API_ENDPOINTS.CATEGORY.DELETE(categoryId));
  return response.data;
};
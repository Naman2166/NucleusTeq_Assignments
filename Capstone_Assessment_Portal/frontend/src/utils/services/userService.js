import api from "../api";
import API_ENDPOINTS from "../constants";


export const getStudents = async () => {
  const response = await api.get(API_ENDPOINTS.AUTH.GET_ALL_STUDENTS);
  return response.data;
};
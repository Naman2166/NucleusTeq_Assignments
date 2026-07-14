import api from "../api";
import API_ENDPOINTS from "../constants";


export const getAllResults = async () => {
  const response = await api.get(API_ENDPOINTS.RESULT.GET_ALL);
  return response.data;
};

export const getStudentResultHistory = async () => {
  const response = await api.get(API_ENDPOINTS.RESULT.GET_HISTORY);
  return response.data;
};

export const getResultByAttempt = async (attemptId) => {
  const response = await api.get(API_ENDPOINTS.RESULT.GET_BY_ATTEMPT(attemptId));
  return response.data;
};

export const getAdminResultByAttempt = async (attemptId) => {
  const response = await api.get(API_ENDPOINTS.RESULT.GET_BY_ATTEMPT_ADMIN(attemptId));
  return response.data;
};

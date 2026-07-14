import api from "../api";
import API_ENDPOINTS from "../constants";


export const getQuizzes = async () => {
  const response = await api.get(API_ENDPOINTS.QUIZ.GET_ALL);
  return response.data;
};

export const getQuizById = async (quizId) => {
  const response = await api.get(API_ENDPOINTS.QUIZ.GET_BY_ID(quizId));
  return response.data;
};

export const getQuizzesByCategory = async (categoryId) => {
  const response = await api.get(API_ENDPOINTS.QUIZ.GET_BY_CATEGORY(categoryId));
  return response.data;
};

export const createQuiz = async (quizData) => {
  const response = await api.post(API_ENDPOINTS.QUIZ.CREATE, quizData);
  return response.data;
};

export const updateQuiz = async (quizId, quizData) => {
  const response = await api.put(API_ENDPOINTS.QUIZ.UPDATE(quizId), quizData);
  return response.data;
};

export const deleteQuiz = async (quizId) => {
  const response = await api.delete(API_ENDPOINTS.QUIZ.DELETE(quizId));
  return response.data;
};

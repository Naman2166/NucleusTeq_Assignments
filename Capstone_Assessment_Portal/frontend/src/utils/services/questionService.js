import api from "../api";
import API_ENDPOINTS from "../constants";


export const getQuestionsByQuiz = async (quizId) => {
  const response = await api.get(API_ENDPOINTS.QUESTION.GET_ALL_BY_QUIZ(quizId));
  return response.data;
};

export const getQuestionById = async (questionId) => {
  const response = await api.get(API_ENDPOINTS.QUESTION.GET_BY_ID(questionId));
  return response.data;
};

export const createQuestion = async (questionData) => {
  const response = await api.post(API_ENDPOINTS.QUESTION.CREATE, questionData);
  return response.data;
};

export const updateQuestion = async (questionId, questionData) => {
  const response = await api.put(API_ENDPOINTS.QUESTION.UPDATE(questionId), questionData);
  return response.data;
};

export const deleteQuestion = async (questionId) => {
  const response = await api.delete(API_ENDPOINTS.QUESTION.DELETE(questionId));
  return response.data;
};
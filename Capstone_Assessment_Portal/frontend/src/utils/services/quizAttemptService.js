import api from "../api";
import API_ENDPOINTS from "../constants";


export const startAttempt = async (attemptData) => {
  const response = await api.post(API_ENDPOINTS.QUIZ_ATTEMPT.START, attemptData);
  return response.data;
};

export const getStudentAttempts = async () => {
  const response = await api.get(API_ENDPOINTS.QUIZ_ATTEMPT.GET_ALL_ATTEMPTS);
  return response.data;
};

export const getAttemptsByQuiz = async (quizId) => {
  const response = await api.get(API_ENDPOINTS.QUIZ_ATTEMPT.GET_ATTEMPTS_BY_QUIZ(quizId));
  return response.data;
};

export const getAttemptQuestions = async (attemptId, index = 0) => {
  const response = await api.get(API_ENDPOINTS.QUIZ_ATTEMPT.GET_QUESTIONS(attemptId), {
    params: { index },
  });
  return response.data;
};

export const saveAnswer = async (attemptId, answerData) => {
  const response = await api.patch(API_ENDPOINTS.QUIZ_ATTEMPT.SAVE_ANSWER(attemptId), answerData);
  return response.data;
};

export const submitAttempt = async (attemptId) => {
  const response = await api.post(API_ENDPOINTS.QUIZ_ATTEMPT.SUBMIT(attemptId));
  return response.data;
};
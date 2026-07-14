const API_ENDPOINTS = {

  AUTH: {
    LOGIN: "/auth/login",
    REGISTER: "/auth/register",
    PUBLIC_KEY: "/auth/public-key",
    REFRESH: "/auth/refresh",
    GET_ALL_STUDENTS: "/auth/students"
  },

  CATEGORY: {
    GET_ALL: "/categories/",
    GET_BY_ID: (categoryId) => `/categories/${categoryId}`,
    CREATE: "/categories/",
    UPDATE: (categoryId) => `/categories/${categoryId}`,
    DELETE: (categoryId) => `/categories/${categoryId}`,
  },

  QUIZ: {
    GET_ALL: "/quizzes/",
    GET_BY_ID: (quizId) => `/quizzes/${quizId}`,
    GET_BY_CATEGORY: (categoryId) => `/quizzes/category/${categoryId}`,
    CREATE: "/quizzes/",
    UPDATE: (quizId) => `/quizzes/${quizId}`,
    DELETE: (quizId) => `/quizzes/${quizId}`,
  },

  QUESTION: {
    GET_ALL_BY_QUIZ: (quizId) => `/questions/quiz/${quizId}`,
    GET_BY_ID: (questionId) => `/questions/${questionId}`,
    CREATE: "/questions/",
    UPDATE: (questionId) => `/questions/${questionId}`,
    DELETE: (questionId) => `/questions/${questionId}`,
  },

  QUIZ_ATTEMPT: {
    START: "/quiz-attempts/",
    GET_ALL_ATTEMPTS: "/quiz-attempts/all",
    GET_ATTEMPTS_BY_QUIZ: (quizId) => `/quiz-attempts/quiz/${quizId}`,
    GET_QUESTIONS: (attemptId) => `/quiz-attempts/${attemptId}/questions`,
    SAVE_ANSWER: (attemptId) => `/quiz-attempts/${attemptId}/answer`,
    SUBMIT: (attemptId) => `/quiz-attempts/${attemptId}/submit`,
  },

  RESULT: {
    GET_HISTORY: "/results/history",
    GET_ALL: "/results/admin",
    GET_BY_ATTEMPT: (attemptId) => `/results/${attemptId}`,
    GET_BY_ATTEMPT_ADMIN: (attemptId) => `/results/admin/${attemptId}`,
  },
  
};

export default API_ENDPOINTS;

export const PAGE_SIZE = 5;

export const INSTRUCTIONS = [
  "The quiz will auto-submit when the timer reaches zero",
  "You can navigate between questions and change answers before submitting",
  "You may leave and resume this attempt later if time has not expired",
  "Once submitted, answers cannot be changed",
];


import axios from "axios";
import API_ENDPOINTS from "./constants";

const api = axios.create({
  baseURL: "http://127.0.0.1:8000",
});


// Request interceptor
// runs before every API request to add access token in its header 
api.interceptors.request.use((request) => {

  const accessToken = localStorage.getItem("access_token");

  if (accessToken) {
    request.headers.Authorization = `Bearer ${accessToken}`;
  }

  return request;
});


// Response interceptor 
// runs after every API response
api.interceptors.response.use(

  // Return successful responses
  (response) => {
    return response;
  },

  // Handling failed responses                   
  async (error) => {

    if (error.response?.status === 401 && 
        error.config.url !== API_ENDPOINTS.AUTH.LOGIN &&
        error.config.url !== API_ENDPOINTS.AUTH.REGISTER) {

      const refreshToken = localStorage.getItem("refresh_token");

      try {
        if (refreshToken) {
          const response = await axios.post("http://127.0.0.1:8000/auth/refresh", {
            refresh_token: refreshToken
          });

          // storing new access token in local storage
          const newAccessToken = response.data.access_token;
          localStorage.setItem("access_token", newAccessToken);

          // Retrying the original request
          error.config.headers.Authorization = `Bearer ${newAccessToken}`;

          return api(error.config);
        }
      }
      catch {

        // clearing local storage when Refresh token has expired
        localStorage.removeItem("access_token");
        localStorage.removeItem("refresh_token");
        localStorage.removeItem("role");

        window.location.href = "/login";
      }
    }

    return Promise.reject(error);
  }
);



export default api;
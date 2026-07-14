// Authentication utility function

export const clearAuth = () => {
  localStorage.removeItem("access_token");
  localStorage.removeItem("refresh_token");
  localStorage.removeItem("role");
};


export const getRedirectPath = () => {
  const token = localStorage.getItem("access_token");
  const role = localStorage.getItem("role");

  if (!token || !role) {
    return "/login";
  }

  return role === "admin"
    ? "/admin/dashboard"
    : "/student/dashboard";
};

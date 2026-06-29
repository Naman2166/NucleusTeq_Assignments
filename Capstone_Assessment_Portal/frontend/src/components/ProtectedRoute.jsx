import { Navigate, Outlet } from "react-router-dom";


const ProtectedRoute = ({ allowedRole }) => {

  const role = localStorage.getItem("role");
  const accessToken = localStorage.getItem("access_token");

  // User is not logged in
  if (!accessToken) {
    return <Navigate to="/login" replace />;
  }

  // User does not have the required role
  if (role !== allowedRole) {
    return <Navigate to="/unauthorized" replace />;
  }

  return <Outlet />;
};

export default ProtectedRoute;
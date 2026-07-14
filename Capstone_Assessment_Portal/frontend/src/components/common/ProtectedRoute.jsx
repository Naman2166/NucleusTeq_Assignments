import { Navigate, Outlet } from "react-router-dom";


const ProtectedRoute = ({ allowedRole }) => {

  const role = localStorage.getItem("role");
  const accessToken = localStorage.getItem("access_token");

  if (!accessToken) {
    return <Navigate to="/login" replace />;
  }

  if (role !== allowedRole) {
    return <Navigate to="/unauthorized" replace />;
  }

  return <Outlet />;
};

export default ProtectedRoute;
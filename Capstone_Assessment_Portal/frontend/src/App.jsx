import { BrowserRouter, Routes, Route } from "react-router-dom";

import Login from "./pages/auth/Login";
import Register from "./pages/auth/Register";
import AdminDashboard from "./pages/admin/AdminDashboard";
import StudentDashboard from "./pages/student/StudentDashboard";
import ProtectedRoute from "./components/ProtectedRoute";
import Unauthorized from "./pages/Unauthorized";


function App() {
  return (
    <Routes>
      {/* Auth Routes */}
      <Route path="/" element={<Login />} />
      <Route path="/login" element={<Login />} />
      <Route path="/register" element={<Register />} />
      <Route path="/unauthorized" element={<Unauthorized />} />

      {/* Admin Routes */}
      <Route element={<ProtectedRoute allowedRole="admin" />}>
        <Route path="/admin/dashboard" element={<AdminDashboard />} />
      </Route>

      {/* Student Routes */}
      <Route element={<ProtectedRoute allowedRole="student" />}>
        <Route path="/student/dashboard" element={<StudentDashboard />} />
      </Route>
    </Routes>
  );
}

export default App;
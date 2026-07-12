import { BrowserRouter, Routes, Route } from "react-router-dom";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import Login from "./pages/auth/Login";
import Register from "./pages/auth/Register";
import AdminDashboard from "./pages/admin/AdminDashboard";
import StudentDashboard from "./pages/student/StudentDashboard";
import ProtectedRoute from "./components/common/ProtectedRoute";
import Unauthorized from "./pages/auth/Unauthorized";
import AdminLayout from "./pages/admin/AdminLayout";
import Categories from "./pages/admin/Categories";
import Questions from "./pages/admin/Questions";
import Quizzes from "./pages/admin/Quizzes";
import Results from "./pages/admin/Results";
import StudentLayout from "./pages/student/StudentLayout";
import StudentResults from "./pages/student/StudentResults";
import StudentCategories from "./pages/student/StudentCategories";


function App() {
  return (
    <>
      <ToastContainer
        position="top-right"
        autoClose={3000}
        hideProgressBar={false}
        closeOnClick
        pauseOnHover
        theme="light"
      />


      <Routes>

        {/* Auth Routes */}
        <Route path="/" element={<Login />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/unauthorized" element={<Unauthorized />} />

        {/* Admin Routes */}
        <Route element={<ProtectedRoute allowedRole="admin" />}>
          <Route path="/admin" element={<AdminLayout />}>
            <Route path="dashboard" element={<AdminDashboard />} />
            <Route path="categories" element={<Categories />} />
            <Route path="quizzes" element={<Quizzes />} />
            <Route path="questions" element={<Questions />} />
            <Route path="results" element={<Results />} />
          </Route>
        </Route>

        {/* Student Routes */}
        <Route element={<ProtectedRoute allowedRole="student" />}>
          <Route path="/student" element={<StudentLayout />}>
            <Route path="dashboard" element={<StudentDashboard />} />
            <Route path="categories" element={<StudentCategories />} />
            <Route path="results" element={<StudentResults />} />
          </Route>
        </Route>

      </Routes>
    </>
  );
}

export default App;
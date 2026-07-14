import { useNavigate, NavLink } from "react-router-dom";
import "./StudentNavbar.css";
import logo from "../../assets/logo.png";
import { clearAuth } from "../../utils/auth";

const navItems = [
  { name: "Home", path: "/student/dashboard" },
  { name: "Categories", path: "/student/categories" },
  { name: "Results", path: "/student/results" },
];

const StudentNavbar = () => {
  const navigate = useNavigate();

  const handleLogout = () => {
    clearAuth();
    navigate("/login", {replace: true});
  };

  return (
    <header className="student-navbar">

      <div className="student-navbar-left">
        <img
          src={logo}
          alt="QuizPoint Logo"
          className="student-navbar-logo"
        />

        <div className="student-navbar-brand">
          <h2 className="student-navbar-title">QuizPoint</h2>
          <p className="student-navbar-subtitle">Assessment Portal</p>
        </div>
      </div>

      <div className="student-navbar-center">
        {navItems.map(({ name, path }) => (
          <NavLink key={path} to={path} 
            className={({ isActive }) => 
              isActive ? "student-nav-link active" : "student-nav-link" 
            }
          >
            {name}
          </NavLink>
        ))}
      </div>

      <div className="student-navbar-right">
        <button onClick={handleLogout} className="student-logout-btn">
          Logout
        </button>
      </div>
      
    </header>
  );
};

export default StudentNavbar;
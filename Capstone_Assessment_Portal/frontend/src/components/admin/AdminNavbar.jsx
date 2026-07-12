import { useNavigate } from "react-router-dom";
import "./AdminNavbar.css";
import logo from "../../assets/logo.png";
import { clearAuth } from "../../utils/auth";

const AdminNavbar = () => {
  const navigate = useNavigate();

  const handleLogout = () => {
    clearAuth();
    navigate("/login", { replace: true });
  };

  return (
    <header className="navbar">
      <div className="navbar-left">
        <img
          src={logo}
          alt="QuizPoint Logo"
          className="navbar-logo"
        />

        <div className="navbar-brand">
          <h2 className="navbar-title">QuizPoint</h2>
          <p className="navbar-subtitle">Assessment Portal</p>
        </div>

        <span className="admin-role">Admin</span>
      </div>

      <div className="navbar-right">
        <button className="logout-btn" onClick={handleLogout}>
          Logout
        </button>
      </div>
    </header>
  );
};

export default AdminNavbar;
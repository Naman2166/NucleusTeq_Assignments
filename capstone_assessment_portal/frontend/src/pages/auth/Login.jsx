import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import api from "../../utils/api";
import API_ENDPOINTS from "../../utils/constants";
import "./Login.css";
import RegisterImage from "../../assets/RegisterImage.jpg";



function Login() {

  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    email: "",
    password: "",
  });

  const handleChange = (e) => {
    setFormData({...formData, [e.target.name]: e.target.value,});
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await api.post(API_ENDPOINTS.AUTH.LOGIN, formData);

      console.log(response)
      const { access_token, role } = response.data;
      
      // storing data in local stoarge
      localStorage.setItem("token", access_token);
      localStorage.setItem("role", role);
      
      // redirecting based on role
      if (role === "admin") {
        navigate("/admin/dashboard");
      } else {
        navigate("/student/dashboard");
      }
    } 
    catch (error) {
      alert("Login Failed");
      console.log(error);
    }
  };

 return (
  <div className="login-container">

    <div className="login-box">
      
      {/* left Portion: image */}
      <div className="login-left">
        <img src={RegisterImage} alt="Login" />
      </div>

      {/* right Portion: form */}
      <div className="login-right">
        
        <div className="login-card">

          <h1>Login</h1>
          <h3>Assessment Portal</h3>
          <p>Welcome Back</p>

          <form onSubmit={handleSubmit}>
            <input
              type="email"
              name="email"
              placeholder="Email Address"
              value={formData.email}
              onChange={handleChange}
            />

            <input
              type="password"
              name="password"
              placeholder="Password"
              value={formData.password}
              onChange={handleChange}
            />

            <button type="submit">
              Login
            </button>
          </form>

          <div className="bottom-text">
            Don't have an account?
            <Link to="/register"> Register</Link>
          </div>

        </div>
      </div>

    </div>

  </div>
  );
 
}

export default Login;
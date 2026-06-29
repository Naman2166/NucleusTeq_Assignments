import { useState, useEffect } from "react";
import { useNavigate, Link } from "react-router-dom";
import api from "../../utils/api";
import API_ENDPOINTS from "../../utils/constants";
import "./Login.css";
import RegisterImage from "../../assets/RegisterImage.jpg";
import { validateLoginForm } from "../../utils/validation";
import { getErrorMessage } from "../../utils/errorHandler";
import { encryptPassword } from "../../utils/encryption";


function Login() {

  const navigate = useNavigate();

  const [error, setError] = useState("");
  const [successMessage, setSuccessMessage] = useState("");
  const [publicKey, setPublicKey] = useState("");
  const [formData, setFormData] = useState({ email: "", password: "" });


  // fetching public key when page loads
  useEffect(() => {
    const getPublicKey = async () => {
      const response = await api.get(API_ENDPOINTS.AUTH.PUBLIC_KEY);
      setPublicKey(response.data.publicKey);
    };

    getPublicKey();
  }, []);


  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };


  const handleSubmit = async (e) => {
    e.preventDefault();

    // frontend validation
    const error = validateLoginForm(formData);

    if (error) {
      setSuccessMessage("");
      setError(error)
      return;
    }

    // cleaning message before API call
    setSuccessMessage("");
    setError("");

    try {
      // encrypting password using public key
      const encryptedPassword = encryptPassword(formData.password, publicKey);

      const payload = {
        ...formData,
        password: encryptedPassword
      };

      const response = await api.post(API_ENDPOINTS.AUTH.LOGIN, payload);

      const { access_token, refresh_token, role } = response.data;

      // storing data in local stoarge
      localStorage.setItem("access_token", access_token);
      localStorage.setItem("refresh_token", refresh_token);
      localStorage.setItem("role", role);

      setSuccessMessage("Login successful. Redirecting...");

      // redirecting based on role
      if (role === "admin") {
        navigate("/admin/dashboard");
      } else {
        navigate("/student/dashboard");
      }

    }
    catch (error) {
      setError(getErrorMessage(error));
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
            <p>Welcome Back</p>

            <form onSubmit={handleSubmit}>

              <label>Email :</label>
              <input
                type="text"
                name="email"
                placeholder="Email Address"
                value={formData.email}
                onChange={handleChange}
              />

              <label>Password :</label>
              <input
                type="password"
                name="password"
                placeholder="Password"
                value={formData.password}
                onChange={handleChange}
              />

              {error && <span className="error-message">{error}</span>}
              {successMessage && <span className="success-message">{successMessage}</span>}


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
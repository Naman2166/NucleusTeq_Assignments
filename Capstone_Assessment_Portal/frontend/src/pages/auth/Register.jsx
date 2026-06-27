import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import api from "../../utils/api";
import API_ENDPOINTS from "../../utils/constants"
import "./Register.css";
import registerImage from "../../assets/registerImage.jpg";
import { validateForm } from "../../utils/validation";
import { getErrorMessage } from "../../utils/errorHandler";


function Register() {

  const navigate = useNavigate();

  const [error, setError] = useState("");
  const [successMessage, setSuccessMessage] = useState("");
  const [formData, setFormData] = useState({
    first_name: "",
    last_name: "",
    email: "",
    password: "",
  });

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    // frontend validation
    const error = validateForm(formData);

    if (error) {
      setSuccessMessage("");
      setError(error);
      return;
    }

    // cleaning message before API call
    setSuccessMessage("");
    setError("");

    try {
      const response = await api.post(API_ENDPOINTS.AUTH.REGISTER, formData);
      
      setSuccessMessage("Registration successful. Redirecting...");
      
      setTimeout(() => {
        navigate("/login");
      }, 800);

    }
    catch (error) {
      setError(getErrorMessage(error));
    }

  };

  return (
    <div className="register-container">

      <div className="register-box">

        {/* left portion: form */}
        <div className="register-left">
          <div className="register-card">

            <h1>Register</h1>
            <p>Create Account to get started</p>

            <form onSubmit={handleSubmit}>

              <label>First Name :</label>
              <input
                type="text"
                name="first_name"
                placeholder="First Name"
                value={formData.first_name}
                onChange={handleChange}
              />

              <label>Last Name :</label>
              <input
                type="text"
                name="last_name"
                placeholder="Last Name"
                value={formData.last_name}
                onChange={handleChange}
              />

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
                Register
              </button>

            </form>

            <div className="bottom-text">
              Already have an account?
              <Link to="/login"> Login</Link>
            </div>

          </div>
        </div>

        {/* right portion: image */}
        <div className="register-right">
          <img src={registerImage} alt="Register" />
        </div>

      </div>

    </div>
  );
}

export default Register;
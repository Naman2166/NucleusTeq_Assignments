import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import api from "../../utils/api";
import API_ENDPOINTS from "../../utils/constants"
import "./Register.css";
import registerImage from "../../assets/registerImage.jpg";



function Register() {

  const navigate = useNavigate();

  const [formData, setFormData] = useState({
  firstName: "",
  lastName: "",
  email: "",
  password: "",
  });

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await api.post(API_ENDPOINTS.AUTH.REGISTER, formData);

      alert("Registration Successful");
      navigate("/login");
    } 
    catch (error) {
      alert("Registration Failed");
      console.log(error);
      console.log(error.response);
      console.log(error.response.data);
    }
  };

return (
  <div className="register-container">

    <div className="register-box">
      
      {/* left portion: form */}
      <div className="register-left">
        <div className="register-card">

          <h1>Register</h1>
          <h3>Assessment Portal</h3>
          <p>Create Account to get started</p>

          <form onSubmit={handleSubmit}>

            <input
              type="text"
              name="firstName"
              placeholder="First Name"
              value={formData.firstName}
              onChange={handleChange}
            />

            <input
              type="text"
              name="lastName"
              placeholder="Last Name"
              value={formData.lastName}
              onChange={handleChange}
            />

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
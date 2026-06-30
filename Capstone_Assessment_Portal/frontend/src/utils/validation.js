// Utility functions to validate Login and Register forms

const isEmailValid = (email) => {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return emailRegex.test(email);
};


const isPasswordValid = (password) => {
  const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@#$%]).*$/;
  return passwordRegex.test(password);
};


// validate email
const validateEmail = (email) => {
  if (!email.trim()) {
    return "Email is required";
  }

  if (!isEmailValid(email)) {
    return "Please enter a valid email address";
  }

  return "";
};


//validate password
const validatePassword = (password) => {
  if (!password.trim()) {
    return "Password is required";
  }

  if (password.length < 8 || password.length > 30) {
    return "Password must be 8-30 characters";
  }

  if (!isPasswordValid(password)) {
    return "Password must contain a letter, number and special character (@#$%)";
  }

  return "";
};


// validate required input fields
const validateRequiredField = (value, fieldName) => {
  if (!value.trim()) {
    return `${fieldName} is required`;
  }

  return "";
};


// Validate Login Form
export const validateLoginForm = (formData) => {
  const errors = {};

  const emailError = validateEmail(formData.email);
  if (emailError) {
    errors.email = emailError;
  }

  const passwordError = validateRequiredField(formData.password, "Password");

  if (passwordError) {
    errors.password = passwordError;
  }

  return errors;
};


// Validate Register Form
export const validateRegisterForm = (formData) => {
  const errors = {};

  const firstNameError = validateRequiredField(formData.first_name, "First name");
  if (firstNameError) {
    errors.first_name = firstNameError;
  }

  const lastNameError = validateRequiredField(formData.last_name, "Last name");
  if (lastNameError) {
    errors.last_name = lastNameError;
  }

  const emailError = validateEmail(formData.email);
  if (emailError) {
    errors.email = emailError;
  }

  const passwordError = validatePassword(formData.password);
  if (passwordError) {
    errors.password = passwordError;
  }

  return errors;
};
// Utility functions to Validate Login and Register form


const isEmailValid = (email) => {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return emailRegex.test(email);
};

const isPasswordValid = (password) => {
  const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@#$%]).*$/
  return passwordRegex.test(password);
};


// Validate login form 
export const validateLoginForm = (formData) => {

  // when all fields are empty
  if (!formData.email && !formData.password) {
    return "Form cannot be empty";
  }

  // when any field is empty
  for (const field in formData) {
    if (!formData[field].trim()) {
      return `${field.replace("_", " ")} is required`;
    }
  }

  if (!isEmailValid(formData.email)) {
    return "Please enter a valid email address";
  }

  return null;
};



// Validate Register form
export const validateRegisterForm = (formData) => {

  if (!formData.email && !formData.password && !formData.first_name && !formData.last_name) {
    return "Form cannot be empty"
  }

  for (const field in formData) {
    if (!formData[field].trim()) {
      return `${field.replace("_", " ")} is required`;
    }
  }
  
  // email validation
  if (!isEmailValid(formData.email)) {
    return "Please enter a valid email address";
  }

  // password validation
  if (formData.password.length < 8 || formData.password.length > 30) {
    return "Password must be 8-30 characters";
  }

  if (!isPasswordValid(formData.password)) {
    return "Password must contain a letter, number and special character (@#$%).";
  }

  return null;
};
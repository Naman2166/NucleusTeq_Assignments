// function to Validate Login and Register form

import validator from "validator";


export const validateForm = (formData) => {

  // when all fields are empty
  if(!formData.email && !formData.password && !formData.first_name && !formData.last_name ){
       return "Form cannot be empty" 
  }

  // email validation
  if (formData.email && !validator.isEmail(formData.email)) {
    return "Please enter a valid email address";
  }

  // when any field is empty
  for (const field in formData) {
      if (!formData[field].trim()) {
       return `${field.replace("_", " ")} is required`;
      }
  }

  // password validation
  if (formData.password.length < 5 || formData.password.length > 20) {
    return "Password must be between 5 to 20 characters";
  }

  return null;
};
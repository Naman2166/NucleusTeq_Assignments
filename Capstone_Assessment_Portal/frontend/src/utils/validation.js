// function to Validate Login and Register form

export const validateForm = (formData) => {

  // when all fields are empty
  if(!formData.email && !formData.password && !formData.first_name && !formData.last_name ){
       return "Form cannot be empty" 
  }

  if (formData.password.length < 2 || formData.password.length > 20) {
    return "Password must be between 2 to 20 characters";
  }

  // when any field is empty
  for (const field in formData) {
      if (!formData[field].trim()) {
       return `${field.replace("_", " ")} is required`;
      }
  }
  
  return null;
};
// function to Validate Login and Register form

export const validateForm = (formData) => {

  // when all fields are empty
  if(!formData.email && !formData.password && !formData.first_name && !formData.last_name ){
       return "Form cannot be empty" 
  }

  // when any field is empty
  for (const field in formData) {
      if (!formData[field].trim()) {
       return `${field.replace("_", " ")} is required`;
      }
  }
  
  return null;
};
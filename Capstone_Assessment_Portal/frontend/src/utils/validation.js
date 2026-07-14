// Utility functions for validation

const isEmailValid = (email) => {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return emailRegex.test(email);
};

const isPasswordValid = (password) => {
  const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@#$%]).*$/;
  return passwordRegex.test(password);
};

const containsAlphabet = (value) => {
  return /[A-Za-z]/.test(value);
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
    return "Password must be between 8-30 characters";
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


// Validate Category Form
export const validateCategoryForm = (formData) => {
  const errors = {};

  const name = formData.name.trim() || "";
  const description = formData.description.trim() || "";

  if (!name) {
    errors.name = "Category name is required";
  } 
  else if (name.length < 3 || name.length > 50 ) {
    errors.name = "Category name must be between 3-50 characters";
  }
  else if(!containsAlphabet(name)){
    errors.name = "Catagory name must contain at least one alphabet";
  }

  if (!description) {
    errors.description = "Description is required";
  } 
  else if (description.length < 5 || description.length > 200) {
    errors.description = "Description must be between 5-200 characters";
  }
  else if(!containsAlphabet(description)){
    errors.description = "Description must contain at least one alphabet";
  }

  return errors;
};


// Validate Quiz Form
export const validateQuizForm = (formData) => {
  const errors = {};

  const title = formData.title?.trim() || "";
  const description = formData.description?.trim() || "";
  const category = formData.category?.trim() || "";
  const duration = Number(formData.duration);
  const totalMarks = Number(formData.totalMarks);
  const passingMarks = Number(formData.passingMarks);
  const maxAttempts = Number(formData.maxAttempts);

  if (!category) {
    errors.category = "Category is required";
  }

  if (!title) {
    errors.title = "Quiz title is required";
  } 
  else if (title.length < 3 || title.length > 100) {
    errors.title = "Quiz title must be between 3-100 characters";
  }
  else if (!containsAlphabet(title)) {
  errors.title = "Quiz title must contain at least one alphabet";
  }

  if (!description) {
    errors.description = "Description is required";
  } 
  else if (description.length < 5 || description.length > 500) {
    errors.description ="Description must be between 5-500 characters";
  }
  else if(!containsAlphabet(description)){
    errors.description = "Description must contain at least one alphabet";
  }

  if (!formData.duration) {
    errors.duration = "Duration is required";
  } else if (duration <= 0) {
    errors.duration = "Duration must be greater than 0";
  }

  if (!formData.totalMarks) {
    errors.totalMarks = "Total marks is required";
  } 
  else if (totalMarks <= 0) {
    errors.totalMarks = "Total marks must be greater than 0";
  }

  if (formData.passingMarks === "") {
    errors.passingMarks = "Passing marks is required";
  } 
  else if (passingMarks < 0) {
    errors.passingMarks = "Passing marks cannot be negative";
  } 
  else if (passingMarks > totalMarks) {
    errors.passingMarks ="Passing marks cannot exceed total marks";
  }

  if (!formData.maxAttempts) {
    errors.maxAttempts = "Maximum attempts is required";
  } 
  else if (maxAttempts <= 0) {
    errors.maxAttempts ="Maximum attempts must be greater than 0";
  }

  return errors;
};



// Validate Question Form
export const validateQuestionForm = (formData) => {
  const errors = {};

  const question = formData.question.trim();
  const marks = Number(formData.marks);

  if (!question) {
    errors.question = "Question is required";
  } 
  else if (question.length < 5 || question.length > 500) {
    errors.question = "Question must be between 5-500 characters";
  }

  if (!formData.marks) {
    errors.marks = "Marks are required";
  } 
  else if (marks <= 0) {
    errors.marks = "Marks must be greater than 0";
  }

  return errors;
};
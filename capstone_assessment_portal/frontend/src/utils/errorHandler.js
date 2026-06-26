// Function to return Custom message for API errors

export const getErrorMessage = (error) => {

  const detail = error.response?.data?.detail;

  if (!Array.isArray(detail)) {
    return detail || "Something went wrong";
  }
  
  const err = detail[0];
  const field = err.loc[1].replace("_", " ")

  switch (err.type) {
    case "missing":
      return `${field} is required`;

    case "string_pattern_mismatch":
      return `${field} should contain only letters`;

    case "string_too_short":
      return `${field} is too short`;

    case "string_too_long":
      return `${field} is too long`;
    
    case "value_error":
      return "Please enter a valid email address";

    default:
      return err.msg;
  }
};
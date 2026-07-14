import { useEffect, useState } from "react";
import "./QuizForm.css";
import { validateQuizForm } from "../../utils/validation";


const QuizForm = ({
  open,
  title,
  buttonText,
  initialData,
  categories = [],
  onSubmit,
  onClose,
}) => {

  const DEFAULT_FORM = {
    category: "",
    title: "",
    description: "",
    duration: "",
    totalMarks: "",
    passingMarks: "",
    maxAttempts: "",
  };

  const [formData, setFormData] = useState(DEFAULT_FORM);
  const [errors, setErrors] = useState({});

  useEffect(() => {
    setFormData(initialData || DEFAULT_FORM);
    setErrors({});
  }, [initialData]);

  if (!open) return null;

  const handleChange = (e) => {
    const { name, value } = e.target;

    setFormData((prev) => ({
      ...prev, [name]: value,
    }));

    setErrors((prev) => ({
      ...prev, [name]: "",
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    const validationErrors = validateQuizForm(formData);

    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
      return;
    }

    setErrors({});
    onSubmit(formData);
  };

  return (
    <div className="quiz-form-overlay">
      <div className="quiz-form-modal">
        <div className="quiz-form-header">
          <h2>{title}</h2>
        </div>

        <form onSubmit={handleSubmit}>
          <div className="quiz-form-grid">

            <div className="form-group">
              <label>Category</label>

              <select
                name="category"
                value={formData.category}
                onChange={handleChange}
              >

                <option value="">Select Category</option>

                {categories.map((category) => (
                  <option key={category.id} value={category.id}>
                    {category.name}
                  </option>
                ))}
              </select>

              {errors.category && (
                <p className="form-error">
                  {errors.category}
                </p>
              )}
            </div>

            <div className="form-group">
              <label>Quiz Title</label>

              <input
                type="text"
                name="title"
                placeholder="title"
                value={formData.title}
                onChange={handleChange}
              />

              {errors.title && (
                <p className="form-error">
                  {errors.title}
                </p>
              )}
            </div>

            <div className="form-group">
              <label>Duration (Minutes)</label>

              <input
                type="number"
                name="duration"
                placeholder="10"
                value={formData.duration}
                onChange={handleChange}
              />

              {errors.duration && (
                <p className="form-error">
                  {errors.duration}
                </p>
              )}
            </div>

            <div className="form-group">
              <label>Maximum Attempts</label>

              <input
                type="number"
                name="maxAttempts"
                placeholder="3"
                value={formData.maxAttempts}
                onChange={handleChange}
              />
              {errors.maxAttempts && (
                <p className="form-error">
                  {errors.maxAttempts}
                </p>
              )}
            </div>

            <div className="form-group">
              <label>Total Marks</label>

              <input
                type="number"
                name="totalMarks"
                placeholder="100"
                value={formData.totalMarks}
                onChange={handleChange}
              />
              {errors.totalMarks && (
                <p className="form-error">
                  {errors.totalMarks}
                </p>
              )}
            </div>

            <div className="form-group">
              <label>Passing Marks</label>

              <input
                type="number"
                name="passingMarks"
                placeholder="40"
                value={formData.passingMarks}
                onChange={handleChange}
              />
              {errors.passingMarks && (
                <p className="form-error">
                  {errors.passingMarks}
                </p>
              )}
            </div>

          </div>

          <div className="form-group">
            <label>Description</label>

            <textarea
              rows="3"
              name="description"
              placeholder="Enter quiz description..."
              value={formData.description}
              onChange={handleChange}
            />
            {errors.description && (
              <p className="form-error">
                {errors.description}
              </p>
            )}
          </div>

          <div className="quiz-form-buttons">
            <button type="button" className="cancel-btn" onClick={onClose}>
              Cancel
            </button>

            <button type="submit" className="submit-btn">
              {buttonText}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default QuizForm;
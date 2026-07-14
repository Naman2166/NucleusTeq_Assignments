import { useEffect, useState } from "react";
import "./QuestionForm.css";
import { validateQuestionForm } from "../../utils/validation";

const QuestionForm = ({ open, title, buttonText, initialData, onSubmit, onClose }) => {

  const DEFAULT_FORM = {
    question: "",
    questionType: "MCQ",
    options: ["", ""],
    correctAnswer: 1,
    difficulty: "Easy",
    marks: "",
    tags: "",
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

  const handleOptionChange = (index, value) => {
    const updatedOptions = [...formData.options];
    updatedOptions[index] = value;

    setFormData((prev) => ({
      ...prev, options: updatedOptions
    }));

    setErrors((prev) => ({
      ...prev, options: ""
    }));
  };

  const handleQuestionTypeChange = (e) => {
    const type = e.target.value;

    if (type === "TRUE_FALSE") {
      setFormData((prev) => ({
        ...prev,
        questionType: type,
        options: ["True", "False"],
        correctAnswer: 1,
      }));
    }
    else {
      setFormData((prev) => ({
        ...prev,
        questionType: type,
        options: ["", ""],
        correctAnswer: 1,
      }));
    }

    setErrors((prev) => ({
      ...prev, options: ""
    }));
  };


  const handleSubmit = (e) => {
    e.preventDefault();

    const validationErrors = validateQuestionForm(formData);

    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
      return;
    }

    setErrors({});
    onSubmit(formData);
  };

  return (
    <div className="question-form-overlay">
      <div className="question-form-modal">

        <div className="question-form-header">
          <h2>{title}</h2>
        </div>

        <form onSubmit={handleSubmit}>

          <div className="form-group">
            <label>Question</label>

            <textarea
              rows="3"
              name="question"
              placeholder="Enter question..."
              value={formData.question}
              onChange={handleChange}
            />
            {errors.question && (
              <p className="form-error">
                {errors.question}
              </p>
            )}
          </div>

          <div className="question-form-grid">

            <div className="form-group">
              <label>Question Type</label>

              <select
                name="questionType"
                value={formData.questionType}
                onChange={handleQuestionTypeChange}
              >
                <option value="MCQ">MCQ</option>
                <option value="TRUE_FALSE">True / False</option>
              </select>
            </div>

            <div className="form-group">
              <label>Difficulty</label>

              <select
                name="difficulty"
                value={formData.difficulty}
                onChange={handleChange}
              >
                <option>Easy</option>
                <option>Medium</option>
                <option>Hard</option>
              </select>
            </div>

            <div className="form-group">
              <label>Marks</label>

              <input
                type="number"
                name="marks"
                placeholder="5"
                value={formData.marks}
                onChange={handleChange}
              />
              {errors.marks && (
                <p className="form-error">
                  {errors.marks}
                </p>
              )}
            </div>
            <div className="form-group">
              <label>Tags</label>

              <input
                type="text"
                name="tags"
                placeholder="java, basics"
                value={formData.tags}
                onChange={handleChange}
              />
            </div>

          </div>

          <div className="form-group">
            <label>Options</label>

            <div className="options-container">
              {formData.questionType === "MCQ" ? (
                <>
                  {formData.options.map((option, index) => (
                    <div
                      className="option-row"
                      key={index}
                    >
                      <input
                        type="radio"
                        name="correctAnswer"
                        checked={formData.correctAnswer === index + 1}
                        onChange={() =>
                          setFormData((prev) => ({...prev, correctAnswer: index + 1,}))
                        }
                      />

                      <input
                        type="text"
                        placeholder={`Option ${index + 1}`}
                        value={option}
                        onChange={(e) => handleOptionChange(index, e.target.value)}
                      />
                    </div>
                  ))}

                  <div className="option-buttons">
                    <button
                      type="button"
                      onClick={() =>
                        setFormData((prev) => ({...prev, options: [...prev.options, ""]}))
                      }
                      disabled={formData.options.length === 4}
                    >
                      Add Option
                    </button>

                    <button
                      type="button"
                      onClick={() =>
                        setFormData((prev) => {
                          const updatedOptions = prev.options.slice(0, -1);

                          return {
                            ...prev,
                            options: updatedOptions,
                            correctAnswer: Math.min(
                              prev.correctAnswer,
                              updatedOptions.length
                            ),
                          };
                        })
                      }
                      disabled={formData.options.length === 2}
                    >
                      Remove Option
                    </button>
                  </div>

                  {errors.options && (
                    <p className="form-error">
                      {errors.options}
                    </p>
                  )}
                </>
              ) : (
                <>
                  <div className="option-row">
                    <input
                      type="radio"
                      name="correctAnswer"
                      checked={formData.correctAnswer === 1}
                      onChange={() =>
                        setFormData((prev) => ({...prev, correctAnswer: 1}))
                      }
                    />

                    <input type="text" value="True" disabled/>
                  </div>

                  <div className="option-row">
                    <input
                      type="radio"
                      name="correctAnswer"
                      checked={formData.correctAnswer === 2}
                      onChange={() =>
                        setFormData((prev) => ({...prev, correctAnswer: 2}))
                      }
                    />

                    <input type="text" value="False" disabled/>
                  </div>
                </>
              )}

            </div>
          </div>

          <div className="question-form-buttons">
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

export default QuestionForm;
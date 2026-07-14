import { useEffect, useState } from "react";
import "./CategoryForm.css";
import { toast } from "react-toastify";
import { validateCategoryForm } from "../../utils/validation";


const CategoryForm = ({open, title, buttonText, initialData, onSubmit, onClose}) => {

  const [formData, setFormData] = useState({name: "", description: ""});
  const [errors, setErrors] = useState({});

  useEffect(() => {
    setFormData(initialData || {name: "", description: ""}
    );

    setErrors({});
  }, [initialData]);


  if (!open) return null;

  const handleChange = (e) => {
    const { name, value } = e.target;

    setFormData((prev) => ({
      ...prev, [name]: value}
    ));

    setErrors((prev) => ({
      ...prev, [name]: ""}
    ));
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    const validationErrors =
      validateCategoryForm(formData);

    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
      return;
    }

    setErrors({});
    onSubmit(formData);
  };

  return (
    <div className="category-form-overlay">
      <div className="category-form-modal">
        <div className="category-form-header">
          <h2>{title}</h2>
        </div>

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Category Name</label>

            <input
              type="text"
              name="name"
              placeholder="Enter category name"
              value={formData.name}
              onChange={handleChange}
            />

            {errors.name && (
              <p className="form-error">
                {errors.name}
              </p>
            )}
          </div>

          <div className="form-group">
            <label>Description</label>

            <textarea
              name="description"
              rows="5"
              placeholder="Enter category description"
              value={formData.description}
              onChange={handleChange}
            />

            {errors.description && (
              <p className="form-error">
                {errors.description}
              </p>
            )}
          </div>

          <div className="category-form-buttons">
            <button
              type="button"
              className="cancel-btn"
              onClick={onClose}
            >
              Cancel
            </button>

            <button
              type="submit"
              className="submit-btn"
            >
              {buttonText}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default CategoryForm;
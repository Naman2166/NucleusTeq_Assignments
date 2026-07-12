import { useState } from "react";
import { ChevronRight, ChevronDown } from "lucide-react";
import QuizTable from "./QuizTable";
import "./CategoryDropdown.css";


const CategoryDropdown = ({category, onEdit, onDelete}) => {

  const [expanded, setExpanded] = useState(false);

  return (
    <div className="category-section">
      <div
        className="category-header"
        onClick={() => setExpanded(!expanded)}
      >
        <div className="category-left">
          <button className="expand-btn">
            {expanded ? (<ChevronDown size={20} />) : (<ChevronRight size={20} />)}
          </button>

          <h3>{category.name}</h3>
        </div>

        <div className="quiz-count-text">
          {category.quizzes.length}{" "}
          {category.quizzes.length === 1
            ? "Quiz"
            : "Quizzes"}
        </div>
      </div>

      {expanded && (
        <div className="category-content">
          {category.quizzes.length > 0 ? (
            <QuizTable
              quizzes={category.quizzes}
              onEdit={onEdit}
              onDelete={onDelete}
            />
          ) : (
            <div className="empty-quizzes">
              No quizzes available in this category.
            </div>
          )}
        </div>
      )}
    </div>
  );
};

export default CategoryDropdown;
import "./CategoryCard.css";
import { BookOpen, ChevronRight } from "lucide-react";

const CategoryCard = ({ category, onClick }) => {
  
  return (
    <div className="category-card" onClick={() => onClick(category)}>
      <div className="category-card-top">
        <div className="category-card-icon">
          <BookOpen size={26} />
        </div>

        <span className="quiz-badge">
          {category.quizCount} Quizzes
        </span>
      </div>

      <div className="category-card-content">
        <h3>{category.name}</h3>
        <p>{category.description}</p>
      </div>

      <div className="category-card-footer">
        <button className="category-open-btn">
          Explore
          <ChevronRight size={17} />
        </button>
      </div>
    </div>
  );
};

export default CategoryCard;
import "./CategoryCard.css";
import { BookOpen, ChevronRight } from "lucide-react";

const CategoryCard = ({ category, onClick }) => {
  return (
    <div className="category-card" onClick={() => onClick(category)}>
      <div className="category-card-icon">
        <BookOpen size={28} />
      </div>

      <div className="category-card-content">
        <h3>{category.name}</h3>
        <p>{category.description}</p>

        <div className="category-card-footer">
          <span>{category.quizCount} Quizzes</span>

          <button className="category-open-btn">
            Explore
            <ChevronRight size={18} />
          </button>
        </div>
      </div>
    </div>
  );
};

export default CategoryCard;
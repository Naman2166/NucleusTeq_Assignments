import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import "./StudentCategories.css";
import CategoryCard from "../../components/student/CategoryCard";
import { getCategories } from "../../utils/services/categoryService";
import { getQuizzesByCategory } from "../../utils/services/quizService";
import { getErrorMessage } from "../../utils/errorHandler";
import { Search } from "lucide-react";


const StudentCategories = () => {
  const navigate = useNavigate();
  const [categories, setCategories] = useState([]);
  const [quizCounts, setQuizCounts] = useState({});
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [searchTerm, setSearchTerm] = useState("");

  useEffect(() => {
    fetchCategories();
  }, []);

  const fetchCategories = async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await getCategories();
      setCategories(data);
      const counts = {};
      await Promise.all(
        data.map(async (cat) => {
          try {
            const quizzes = await getQuizzesByCategory(cat.id);
            counts[cat.id] = quizzes.length;
          } catch {
            counts[cat.id] = 0;
          }
        })
      );
      setQuizCounts(counts);
    } catch (err) {
      setError(getErrorMessage(err));
      toast.error(getErrorMessage(err));
    } finally {
      setLoading(false);
    }
  };

  const handleCategoryClick = (category) => {
    navigate(`/student/categories/${category.id}/quizzes`);
  };

  const filteredCategories = categories.filter((category) =>
    category.name.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="student-categories-page">
      <div className="student-page-top">
        <div className="student-page-header">
          <h1>Categories</h1>
          <p>Select a category to explore available quizzes.</p>
        </div>

        <div className="category-search">
          <Search size={18} />
          <input
            type="text"
            placeholder="Search categories..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
        </div>
      </div>

      {loading && (
        <div className="student-loading-state">
          <div className="student-spinner" />
          <p>Loading categories…</p>
        </div>
      )}

      {!loading && error && (
        <div className="student-error-state">
          <p>{error}</p>
          <button className="student-retry-btn" onClick={fetchCategories}>
            Try Again
          </button>
        </div>
      )}

      {!loading && !error && filteredCategories.length === 0 && (
        <div className="student-empty-state">
          <p>
            {categories.length === 0
              ? "No categories available yet."
              : "No categories found."}
          </p>
        </div>
      )}

      {!loading && !error && categories.length > 0 && (
        <div className="category-grid">
          {filteredCategories.map((category) => (
            <CategoryCard
              key={category.id}
              category={{ ...category, quizCount: quizCounts[category.id] ?? "…" }}
              onClick={handleCategoryClick}
            />
          ))}
        </div>
      )}
    </div>
  );
};

export default StudentCategories;
import { useState, useEffect } from "react";
import "./Quizzes.css";
import { Plus, Search } from "lucide-react";
import CategoryDropdown from "../../components/admin/CategoryDropdown";
import QuizForm from "../../components/admin/QuizForm";
import { toast } from "react-toastify";
import { getErrorMessage } from "../../utils/errorHandler";
import { getCategories } from "../../utils/services/categoryService";
import { getQuizzes, createQuiz, updateQuiz, deleteQuiz } from "../../utils/services/quizService";
import Pagination from "../../components/common/Pagination";
import { PAGE_SIZE } from "../../utils/constants";


const Quizzes = () => {

  const [search, setSearch] = useState("");
  const [showAddModal, setShowAddModal] = useState(false);
  const [showEditModal, setShowEditModal] = useState(false);
  const [selectedQuiz, setSelectedQuiz] = useState(null);
  const [categories, setCategories] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);

  const filteredCategories = categories.filter((category) =>
    category.name.toLowerCase().includes(search.toLowerCase())
  );

  const totalPages = Math.ceil(
    filteredCategories.length / PAGE_SIZE
  );

  const paginatedCategories = filteredCategories.slice(
    (currentPage - 1) * PAGE_SIZE,
    currentPage * PAGE_SIZE
  );


  const fetchData = async () => {
    try {
      const [categories, quizzes] = await Promise.all([
        getCategories(),
        getQuizzes(),
      ]);

      const categoryData = categories.map((category) => ({
        ...category,
        quizzes: quizzes.filter(
          (quiz) => quiz.category_id === category.id
        ),
      }));

      setCategories(categoryData);
    }
    catch (error) {
      toast.error(getErrorMessage(error));
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  useEffect(() => {
    if (currentPage > totalPages && totalPages > 0) {
      setCurrentPage(totalPages);
    }
  }, [currentPage, totalPages]);


  const handleAddQuiz = async (quiz) => {
    try {
      await createQuiz({
        title: quiz.title,
        description: quiz.description,
        category_id: quiz.category,
        duration: Number(quiz.duration),
        total_marks: Number(quiz.totalMarks),
        passing_marks: Number(quiz.passingMarks),
        max_attempts: Number(quiz.maxAttempts),
      });

      toast.success("Quiz created successfully.");
      fetchData();
      setShowAddModal(false);
    }
    catch (error) {
      toast.error(getErrorMessage(error));
    }
  };


  const handleEdit = (quiz) => {
    setSelectedQuiz({
      ...quiz,
      category: quiz.category_id,
      totalMarks: quiz.total_marks,
      passingMarks: quiz.passing_marks,
      maxAttempts: quiz.max_attempts,
    });

    setShowEditModal(true);
  };


  const handleUpdateQuiz = async (quiz) => {
    try {
      await updateQuiz(selectedQuiz.id, {
        title: quiz.title,
        description: quiz.description,
        category_id: quiz.category,
        duration: Number(quiz.duration),
        total_marks: Number(quiz.totalMarks),
        passing_marks: Number(quiz.passingMarks),
        max_attempts: Number(quiz.maxAttempts),
      });

      toast.success("Quiz updated successfully.");
      fetchData();
      setShowEditModal(false);
      setSelectedQuiz(null);

    }
    catch (error) {
      toast.error(getErrorMessage(error));
    }
  };

  const handleDelete = async (quiz) => {
    try {
      await deleteQuiz(quiz.id);
      toast.success("Quiz deleted successfully.");
      fetchData();
    }
    catch (error) {
      toast.error(getErrorMessage(error));
    }
  };

  const EMPTY_QUIZ = {
    category: "",
    title: "",
    description: "",
    duration: "",
    totalMarks: "",
    passingMarks: "",
    maxAttempts: "",
  };


  return (
    <div className="admin-quizzes-page">
      <div className="admin-quizzes-header">
        <div>
          <h1>Quizzes</h1>

          <p>
            Manage quizzes grouped by categories.
          </p>
        </div>

        <div className="header-actions">
          <div className="quiz-search-box">
            <Search size={18} />

            <input
              type="text"
              placeholder="Search category..."
              value={search}
              onChange={(e) => { setSearch(e.target.value); setCurrentPage(1); }}
            />
          </div>

          <button
            className="add-quiz-btn"
            onClick={() => setShowAddModal(true)}
          >
            <Plus size={18} />
            Add Quiz
          </button>
        </div>

      </div>

      <div className="category-sections">
        {paginatedCategories.map((category) => (
          <CategoryDropdown
            key={category.id}
            category={category}
            onEdit={handleEdit}
            onDelete={handleDelete}
          />
        ))}
      </div>

      <Pagination
        currentPage={currentPage}
        totalPages={totalPages}
        onPageChange={setCurrentPage}
      />

      <QuizForm
        open={showAddModal}
        categories={categories}
        title="Add Quiz"
        buttonText="Add Quiz"
        initialData={EMPTY_QUIZ}
        onSubmit={handleAddQuiz}
        onClose={() => setShowAddModal(false)}
      />

      <QuizForm
        open={showEditModal}
        categories={categories}
        title="Update Quiz"
        buttonText="Update Quiz"
        initialData={selectedQuiz || EMPTY_QUIZ}
        onSubmit={handleUpdateQuiz}
        onClose={() => {
          setShowEditModal(false);
          setSelectedQuiz(null);
        }}
      />
    </div>
  );
};

export default Quizzes;
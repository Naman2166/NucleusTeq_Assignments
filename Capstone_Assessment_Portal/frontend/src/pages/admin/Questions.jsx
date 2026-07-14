import { useEffect, useState } from "react";
import "./Questions.css";
import { Plus, Search } from "lucide-react";
import QuestionTable from "../../components/admin/QuestionTable";
import QuestionForm from "../../components/admin/QuestionForm";
import { toast } from "react-toastify";
import { getErrorMessage } from "../../utils/errorHandler";
import { getCategories } from "../../utils/services/categoryService";
import { getQuizzes } from "../../utils/services/quizService";
import { getQuestionsByQuiz, createQuestion, updateQuestion, deleteQuestion } from "../../utils/services/questionService";
import Pagination from "../../components/common/Pagination";
import { PAGE_SIZE } from "../../utils/constants";


const Questions = () => {

  const [search, setSearch] = useState("");
  const [selectedCategory, setSelectedCategory] = useState("");
  const [selectedQuiz, setSelectedQuiz] = useState("");
  const [showAddModal, setShowAddModal] = useState(false);
  const [showEditModal, setShowEditModal] = useState(false);
  const [selectedQuestion, setSelectedQuestion] = useState(null);
  const [categories, setCategories] = useState([]);
  const [quizzes, setQuizzes] = useState([]);
  const [questions, setQuestions] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);


  const filteredQuizzes = quizzes.filter(
    (quiz) => quiz.category_id === selectedCategory
  );

  const filteredQuestions = questions.filter((question) =>
    question.question.toLowerCase().includes(search.toLowerCase())
  );

  const totalPages = Math.ceil(
    filteredQuestions.length / PAGE_SIZE
  );

  const paginatedQuestions = filteredQuestions.slice(
    (currentPage - 1) * PAGE_SIZE,
    currentPage * PAGE_SIZE
  );

  const fetchData = async () => {
    try {
      const [categories, quizzes] = await Promise.all([
        getCategories(),
        getQuizzes(),
      ]);

      setCategories(categories);
      setQuizzes(quizzes);
    }
    catch (error) {
      toast.error(getErrorMessage(error));
    }
  };


  useEffect(() => {
    fetchData();
  }, []);


  useEffect(() => {
    if (!selectedQuiz) {
      setQuestions([]);
      return;
    }

    fetchQuestions(selectedQuiz);
  }, [selectedQuiz]);


  useEffect(() => {
    if (currentPage > totalPages && totalPages > 0) {
      setCurrentPage(totalPages);
    }
  }, [currentPage, totalPages]);


  const fetchQuestions = async (quizId) => {
    try {
      const data = await getQuestionsByQuiz(quizId);
      setQuestions(data);
    }
    catch (error) {
      toast.error(getErrorMessage(error));
    }
  };

  const handleAddQuestion = async (data) => {
    try {
      await createQuestion({
        quiz_id: selectedQuiz,
        question: data.question,
        question_type: data.questionType,
        options: data.options,
        correct_answer: Number(data.correctAnswer),
        difficulty: data.difficulty,
        tags: data.tags
          .split(",")
          .map((tag) => tag.trim())
          .filter(Boolean),
        marks: Number(data.marks),
      });

      toast.success("Question created successfully.");
      await fetchQuestions(selectedQuiz);
      setShowAddModal(false);
    }
    catch (error) {
      toast.error(getErrorMessage(error));
    }
  };

  const handleEdit = (question) => {
    setSelectedQuestion({
      ...question,
      questionType: question.question_type,
      correctAnswer: question.correct_answer,
      tags: question.tags?.join(", ") || "",
    });

    setShowEditModal(true);
  };

  const handleUpdateQuestion = async (data) => {
    try {
      await updateQuestion(selectedQuestion.id, {
        question: data.question,
        question_type: data.questionType,
        options: data.options,
        correct_answer: Number(data.correctAnswer),
        difficulty: data.difficulty,
        tags: data.tags
          .split(",")
          .map((tag) => tag.trim())
          .filter(Boolean),
        marks: Number(data.marks),
      });

      toast.success("Question updated successfully.");
      await fetchQuestions(selectedQuiz);
      setShowEditModal(false);
      setSelectedQuestion(null);
    }
    catch (error) {
      toast.error(getErrorMessage(error));
    }
  };


  const handleDelete = async (question) => {
    try {
      await deleteQuestion(question.id);
      toast.success("Question deleted successfully.");
      await fetchQuestions(selectedQuiz);
    }
    catch (error) {
      toast.error(getErrorMessage(error));
    }
  };

  const EMPTY_QUESTION = {
    question: "",
    questionType: "MCQ",
    options: ["", ""],
    correctAnswer: 1,
    difficulty: "Easy",
    marks: "",
    tags: "",
  };

  const assignedMarks = questions.reduce(
    (total, question) => total + question.marks, 0
  );

  const selectedQuizData = quizzes.find(
    (quiz) => quiz.id === selectedQuiz
  );

  const totalMarks = selectedQuizData?.total_marks || 0;
  const remainingMarks = totalMarks - assignedMarks;


  return (
    <div className="admin-questions-page">

      <div className="admin-questions-header">

        <div>
          <h1>Questions</h1>

          <p>
            Manage questions for each quiz.
          </p>
        </div>

        <button
          className="add-question-btn"
          disabled={!selectedQuiz}
          onClick={() => setShowAddModal(true)}
        >
          <Plus size={18} />
          Add Question
        </button>

      </div>

      <div className="question-toolbar">

        <div className="question-filters">

          <div className="filter-group">
            <label>Category</label>

            <select
              value={selectedCategory}
              onChange={(e) => {
                setSelectedCategory(e.target.value);
                setSelectedQuiz("");
              }}
            >
              <option value="">Select Category</option>

              {categories.map((category) => (
                <option
                  key={category.id}
                  value={category.id}
                >
                  {category.name}
                </option>
              ))}
            </select>
          </div>

          <div className="filter-group">
            <label>Quiz</label>

            <select
              value={selectedQuiz}
              disabled={!selectedCategory}
              onChange={(e) => { setSelectedQuiz(e.target.value); setCurrentPage(1); }}
            >
              <option value="">Select Quiz</option>

              {filteredQuizzes.map((quiz) => (
                <option key={quiz.id} value={quiz.id}>
                  {quiz.title}
                </option>
              ))}
            </select>
          </div>

        </div>

        {selectedQuiz && (
          <div className="search-box">
            <Search size={18} />

            <input
              type="text"
              placeholder="Search question..."
              value={search}
              onChange={(e) => { setSearch(e.target.value); setCurrentPage(1); }}
            />
          </div>
        )}

      </div>

      {selectedQuizData && (
        <div className="question-marks-card">
          <span>Total Marks: <strong>{totalMarks}</strong></span> |
          <span>Assigned Marks: <strong>{assignedMarks}</strong></span> |
          <span>Remaining Marks: <strong>{remainingMarks}</strong></span>
        </div>
      )}

      {selectedQuiz && (
        <QuestionTable
          questions={paginatedQuestions}
          currentPage={currentPage}
          itemsPerPage={PAGE_SIZE}
          onEdit={handleEdit}
          onDelete={handleDelete}
        />
      )}

      {selectedQuiz && (
        <Pagination
          currentPage={currentPage}
          totalPages={totalPages}
          onPageChange={setCurrentPage}
        />
      )}

      <QuestionForm
        open={showAddModal}
        title="Add Question"
        buttonText="Add Question"
        initialData={selectedQuestion || EMPTY_QUESTION}
        onSubmit={handleAddQuestion}
        onClose={() => setShowAddModal(false)}
      />

      <QuestionForm
        open={showEditModal}
        title="Update Question"
        buttonText="Update Question"
        initialData={selectedQuestion || EMPTY_QUESTION}
        onSubmit={handleUpdateQuestion}
        onClose={() => {
          setShowEditModal(false);
          setSelectedQuestion(null);
        }}
      />

    </div>
  );
};

export default Questions;
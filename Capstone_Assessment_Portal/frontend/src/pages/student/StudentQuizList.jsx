import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import { ArrowLeft } from "lucide-react";
import "./StudentQuizList.css";
import QuizCard from "../../components/student/QuizCard";
import QuizStartModal from "../../components/student/QuizStartModal";
import {getQuizzesByCategory} from "../../utils/services/quizService";
import { getCategoryById } from "../../utils/services/categoryService";
import {startAttempt, getAttemptsByQuiz} from "../../utils/services/quizAttemptService";
import { getErrorMessage } from "../../utils/errorHandler";


const StudentQuizList = () => {

  const { categoryId } = useParams();
  const navigate = useNavigate();

  const [quizzes, setQuizzes] = useState([]);
  const [category, setCategory] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [selectedQuiz, setSelectedQuiz] = useState(null);
  const [starting, setStarting] = useState(false);

  useEffect(() => {
    fetchData();
  }, [categoryId]);


  const getUpdatedQuizzes = async (quizData) => {
    return Promise.all(
      quizData.map(async (quiz) => {
        const attempts = await getAttemptsByQuiz(quiz.id);

        const pendingAttempt = attempts.find(
          (attempt) => attempt.status === "IN_PROGRESS"
        );

        return {
          ...quiz,
          hasPendingAttempt: !!pendingAttempt,
          pendingAttemptId: pendingAttempt?.id,
          attemptsLeft:
            quiz.max_attempts - attempts.filter(
              (attempt) => attempt.status !== "IN_PROGRESS")
              .length,
        };
      })
    );
  };


  const fetchData = async () => {
    try {
      setLoading(true);
      setError(null);

      const [quizData, categoryData] = await Promise.all([
        getQuizzesByCategory(categoryId),
        getCategoryById(categoryId),
      ]);

      setQuizzes(await getUpdatedQuizzes(quizData));
      setCategory(categoryData);
    } 
    catch (err) {
      const message = getErrorMessage(err);
      setError(message);
      toast.error(message);
    } 
    finally {
      setLoading(false);
    }
  };


  const resumeAttempt = async (quizId) => {
    const attempts = await getAttemptsByQuiz(quizId);

    return attempts.find(
      (attempt) =>
        attempt.status === "IN_PROGRESS" ||
        attempt.status === "in_progress"
    );
  };


  const handleStartQuiz = (quiz) => {
    if (quiz.hasPendingAttempt) {
      navigate(`/student/quiz/${quiz.pendingAttemptId}`);
      return;
    }

    setSelectedQuiz(quiz);
  };


  const handleStartError = async (err) => {
    const message = getErrorMessage(err);

    if (message.toLowerCase().includes("already")) {
      try {
        const activeAttempt = await resumeAttempt(selectedQuiz.id);

        if (activeAttempt) {
          setSelectedQuiz(null);
          toast.info("Resuming your existing attempt...");
          navigate(`/student/quiz/${activeAttempt.id}`);
          return;
        }
      } 
      catch {}
    }

    toast.error(message);
  };


  const handleConfirmStart = async () => {
    if (!selectedQuiz) return;

    try {
      setStarting(true);

      const attempt = await startAttempt({
        quiz_id: selectedQuiz.id,
      });

      setSelectedQuiz(null);
      navigate(`/student/quiz/${attempt.id}`);
    } 
    catch (err) {
      await handleStartError(err);
    } 
    finally {
      setStarting(false);
    }
  };

  if (loading) {
    return (
      <div className="student-loading-state">
        <div className="student-spinner" />
        <p>Loading quizzes...</p>
      </div>
    );
  }

  if (error) {
    return (
      <div className="student-error-state">
        <p>{error}</p>
        <button className="student-retry-btn" onClick={fetchData}>
          Try Again
        </button>
      </div>
    );
  }


  return (
    <div className="student-quiz-list-page">
      <button
        className="back-btn" onClick={() => navigate("/student/categories")}>
        <ArrowLeft size={18} />
        Back to Categories
      </button>

      <div className="student-page-header">
        <h1>{category?.name || "Quizzes"}</h1>
        {category?.description && <p>{category.description}</p>}
      </div>

      {quizzes.length === 0 ? (
        <div className="student-empty-state">
          <p>No quizzes available in this category yet.</p>
        </div>
      ) : (
        <div className="quiz-list-grid">
          {quizzes.map((quiz) => (
            <QuizCard
              key={quiz.id}
              quiz={quiz}
              onStart={handleStartQuiz}
            />
          ))}
        </div>
      )}

      {selectedQuiz && (
        <QuizStartModal
          quiz={selectedQuiz}
          onStart={handleConfirmStart}
          onCancel={() => setSelectedQuiz(null)}
          starting={starting}
        />
      )}
    </div>
  );
};

export default StudentQuizList;
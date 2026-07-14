import { useEffect, useState, useRef, useCallback } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import { ChevronLeft, ChevronRight, Clock, Send, X, LogOut } from "lucide-react";
import "./QuizAttempt.css";
import { getAttemptQuestions, saveAnswer, submitAttempt } from "../../utils/services/quizAttemptService";
import { getErrorMessage } from "../../utils/errorHandler";

const QuizAttempt = () => {
  const { attemptId } = useParams();
  const navigate = useNavigate();

  const [currentQuestion, setCurrentQuestion] = useState(null);
  const [totalQuestions, setTotalQuestions] = useState(0);
  const [quizTitle, setQuizTitle] = useState("Quiz");
  const [currentIndex, setCurrentIndex] = useState(0);
  const [answers, setAnswers] = useState({});
  const [visitedIds, setVisitedIds] = useState([]);
  const [loading, setLoading] = useState(true);
  const [questionLoading, setQuestionLoading] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const [showSubmitDialog, setShowSubmitDialog] = useState(false);
  const [showExitDialog, setShowExitDialog] = useState(false);
  const [error, setError] = useState(null);
  const [timeLeft, setTimeLeft] = useState(null);
  const timerRef = useRef(null);
  const autoSubmittedRef = useRef(false);


  useEffect(() => {
    fetchQuestion(0, true);
    return () => clearInterval(timerRef.current);
  }, [attemptId]);

  const fetchQuestion = async (index, isInitial = false) => {
    if (isInitial) setLoading(true);
    else setQuestionLoading(true);
    setError(null);

    try {
      const data = await getAttemptQuestions(attemptId, index);

      setCurrentQuestion(data);
      setTotalQuestions(data.total_questions);
      setCurrentIndex(index);

      if (data.quiz_title) setQuizTitle(data.quiz_title);

      if (data.selected_option !== null && data.selected_option !== undefined) {
        setAnswers((prev) => ({ ...prev, [data.id]: data.selected_option }));
      }

      setVisitedIds((prev) =>
        prev.includes(data.id) ? prev : [...prev, data.id]
      );

      if (isInitial && data.time_remaining !== undefined && data.time_remaining !== null) {
        setTimeLeft(data.time_remaining);
      }
    } catch (err) {
      const msg = getErrorMessage(err);

      if (msg.toLowerCase().includes("time")) {
        navigate(`/student/quiz/${attemptId}/result`, {
          replace: true,
        });
        return;
      }

      setError(msg);
      toast.error(msg);
    } finally {
      if (isInitial) setLoading(false);
      else setQuestionLoading(false);
    }
  };

  const doSubmit = async () => {
    if (submitting) return;

    setSubmitting(true);
    clearInterval(timerRef.current);

    try {
      await submitAttempt(attemptId);
      navigate(`/student/quiz/${attemptId}/result`, {
        replace: true,
      });
    }
    catch (err) {
      const msg = getErrorMessage(err);

      if (msg.toLowerCase().includes("submitted automatically")) {
        navigate(`/student/quiz/${attemptId}/result`, {
          replace: true,
        });
        return;
      }

      toast.error(msg);
      setSubmitting(false);
    }
  };

  const handleAutoSubmit = async () => {
    if (autoSubmittedRef.current || submitting) return;

    autoSubmittedRef.current = true;
    clearInterval(timerRef.current);

    await doSubmit();
  };


  useEffect(() => {
    if (timeLeft === null || timeLeft === undefined) return;
    if (timeLeft <= 0) {
      handleAutoSubmit();
      return;
    }
    clearInterval(timerRef.current);
    timerRef.current = setInterval(() => {
      setTimeLeft((prev) => {
        if (prev <= 1) {
          clearInterval(timerRef.current);
          setTimeout(() => {
            handleAutoSubmit();
          }, 0);
          return 0;
        }
        return prev - 1;
      });
    }, 1000);
    return () => clearInterval(timerRef.current);
  }, [timeLeft, submitting]);


  const handleAnswerSelect = async (questionId, optionIndex) => {
    setAnswers((prev) => ({ ...prev, [questionId]: optionIndex }));
    try {
      await saveAnswer(attemptId, {
        question_id: questionId,
        selected_option: optionIndex,
      });
    } catch (error) {
      toast.error(error.msg);
    }
  };

  const goToQuestion = (index) => {
    if (index === currentIndex) return;
    fetchQuestion(index);
  };


  const handleExit = () => {
    clearInterval(timerRef.current);
    toast.info("Progress saved. You can resume this quiz before time runs out.");
    navigate("/student/dashboard");
  };


  const formatTime = (seconds) => {
    if (seconds == null)
      return "--:--";

    const minutes = String(Math.floor(Math.max(0, seconds) / 60)).padStart(2, "0");
    const remainingSeconds = String(Math.max(0, seconds) % 60).padStart(2, "0");

    return `${minutes}:${remainingSeconds}`;
  };

  const timerUrgent = timeLeft !== null && timeLeft <= 60;
  const answeredCount = Object.keys(answers).length;
  const selectedOptionIndex = currentQuestion ? (answers[currentQuestion.id] ?? null) : null;

  if (loading) {
    return (
      <div className="quiz-attempt-page">
        <div className="qa-loading">
          <div className="student-spinner" />
          <p>Loading quiz…</p>
        </div>
      </div>
    );
  }

  if (error && !currentQuestion) {
    return (
      <div className="quiz-attempt-page">
        <div className="qa-loading">
          <p className="qa-error-text">{error}</p>
          <button className="student-retry-btn" onClick={() => fetchQuestion(0, true)}>
            Retry
          </button>
          <button
            className="student-retry-btn secondary"
            onClick={() => navigate("/student/categories")}
          >
            Back to Categories
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="quiz-attempt-page">
      <header className="qa-header">

        <div className="qa-header-center">
          <h2 className="qa-quiz-title">{quizTitle}</h2>
          <span className="qa-progress-text">
            Question {currentIndex + 1} of {totalQuestions}
          </span>
        </div>

        <div className="qa-header-right">
          <div className={`qa-timer ${timerUrgent ? "urgent" : ""}`}>
            <Clock size={16} />
            <span>{formatTime(timeLeft)}</span>
          </div>

          <button
            className="qa-submit-header-btn"
            onClick={() => setShowSubmitDialog(true)}
            disabled={submitting}
          >
            <Send size={15} />
            <span className="qa-submit-label">Submit</span>
          </button>
        </div>
      </header>

      <div className="qa-progress-bar-wrap">
        <div
          className="qa-progress-bar-fill"
          style={{ width: `${((currentIndex + 1) / totalQuestions) * 100}%` }}
        />
      </div>

      <div className="qa-body">
        <div className="qa-question-panel">
          {questionLoading ? (
            <div className="qa-question-loading">
              <div className="student-spinner" />
            </div>
          ) : currentQuestion ? (
            <>
              <div className="qa-question-meta-row">
                <span className="qa-question-number">Question {currentIndex + 1}</span>
                <span className="qa-question-marks">
                  {currentQuestion.marks} mark{currentQuestion.marks !== 1 ? "s" : ""}
                </span>
              </div>
              <p className="qa-question-text">{currentQuestion.question}</p>

              <div className="qa-options">
                {currentQuestion.options.map((opt, i) => {
                  const optionIndex = i + 1;
                  const label = String.fromCharCode(65 + i);
                  const isSelected = selectedOptionIndex === optionIndex;
                  return (
                    <button
                      key={i}
                      className={`qa-option ${isSelected ? "selected" : ""}`}
                      onClick={() => handleAnswerSelect(currentQuestion.id, optionIndex)}
                      disabled={submitting}
                    >
                      <span className="qa-option-label">{label}</span>
                      <span className="qa-option-text">{opt}</span>
                    </button>
                  );
                })}
              </div>

              <div className="qa-inline-nav">
                <button
                  className="qa-nav-btn"
                  onClick={() => goToQuestion(currentIndex - 1)}
                  disabled={currentIndex === 0 || questionLoading}
                >
                  <ChevronLeft size={17} /> Previous
                </button>
                <button
                  className="qa-nav-btn primary"
                  onClick={() => goToQuestion(currentIndex + 1)}
                  disabled={currentIndex === totalQuestions - 1 || questionLoading}
                >
                  Next <ChevronRight size={17} />
                </button>
              </div>
            </>
          ) : null}
        </div>

      </div>

      {showSubmitDialog && (
        <div className="qa-dialog-overlay">
          <div className="qa-dialog">
            <div className="qa-dialog-header">
              <h3>Submit Quiz?</h3>
              <button className="qa-dialog-close" onClick={() => setShowSubmitDialog(false)}>
                <X size={17} />
              </button>
            </div>
            <p className="qa-dialog-body">
              You have answered <strong>{answeredCount}</strong> out of{" "}
              <strong>{totalQuestions}</strong> questions.
              {answeredCount < totalQuestions && (
                <span className="qa-dialog-warn"> Unanswered questions will be marked as incorrect.</span>
              )}
            </p>
            <div className="qa-dialog-actions">
              <button className="qa-dialog-cancel" onClick={() => setShowSubmitDialog(false)}>
                Continue Quiz
              </button>
              <button
                className="qa-dialog-confirm"
                onClick={() => {
                  setShowSubmitDialog(false);
                  doSubmit();
                }}
                disabled={submitting}
              >
                {submitting ? "Submitting…" : "Yes, Submit"}
              </button>
            </div>
          </div>
        </div>
      )}

      {showExitDialog && (
        <div className="qa-dialog-overlay">
          <div className="qa-dialog">
            <div className="qa-dialog-header">
              <h3>Exit Quiz?</h3>
              <button className="qa-dialog-close" onClick={() => setShowExitDialog(false)}>
                <X size={17} />
              </button>
            </div>
            <p className="qa-dialog-body">
              Your answers are saved automatically. You can resume this quiz anytime
              before the timer runs out.
            </p>
            <div className="qa-dialog-actions">
              <button className="qa-dialog-cancel" onClick={() => setShowExitDialog(false)}>
                Continue Quiz
              </button>
              <button className="qa-dialog-confirm exit" onClick={handleExit}>
                Exit for Now
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default QuizAttempt;
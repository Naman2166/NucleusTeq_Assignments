import { Clock, Target, Award, RefreshCw, Play } from "lucide-react";
import "./QuizCard.css";

const QuizCard = ({ quiz, onStart }) => {
  return (
    <div className="quiz-card">
      <div className="quiz-card-header">
        <h3 className="quiz-card-title">{quiz.title}</h3>
        {quiz.description && (
          <p className="quiz-card-desc">{quiz.description}</p>
        )}
      </div>

      <div className="quiz-card-meta">

        <div className="quiz-meta-item">
          <Clock size={15} />
          <span>{quiz.duration} min</span>
        </div>

        <div className="quiz-meta-item">
          <Award size={15} />
          <span>{quiz.total_marks} marks</span>
        </div>

        <div className="quiz-meta-item">
          <Target size={15} />
          <span>Pass: {quiz.passing_marks}</span>
        </div>

        <div className="quiz-meta-item">
          <RefreshCw size={15} />
          <span>Max {quiz.max_attempts} attempt{quiz.max_attempts !== 1 ? "s" : ""}</span>
        </div>

        <div className="quiz-attempts-left">
          <span>
            Attempts Remaining: <strong>{quiz.attemptsLeft}</strong>
          </span>
        </div>

      </div>

      <button
         className={`quiz-start-btn ${quiz.hasPendingAttempt ? "resume-btn" : ""}`}
        onClick={() => onStart(quiz)}
      >
        {quiz.hasPendingAttempt ? (
          <>
            <Play size={15} />
             Resume Quiz
          </>
        ) : (
          <>
            <Play size={15} />
            Start Quiz
          </>
        )}
      </button>

    </div>
  );
};

export default QuizCard;
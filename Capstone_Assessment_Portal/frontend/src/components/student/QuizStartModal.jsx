import { Clock, Award, Target, AlertCircle, CheckCircle, X } from "lucide-react";
import "./QuizStartModal.css";
import {INSTRUCTIONS} from "../../utils/constants"


const QuizStartModal = ({ quiz, onStart, onCancel, starting }) => {
  return (
    <div className="quiz-modal-overlay" onClick={onCancel}>
      <div className="quiz-modal" onClick={(e) => e.stopPropagation()}>

        <div className="quiz-modal-header">
          <h2>{quiz.title}</h2>
          <button className="quiz-modal-close" onClick={onCancel}>
            <X size={20} />
          </button>
        </div>

        {/* Description */}
        {quiz.description && (
          <p className="quiz-modal-desc">{quiz.description}</p>
        )}

        <div className="quiz-modal-meta">
          <div className="quiz-modal-meta-item">
            <Clock size={20} className="meta-icon" />
            <div>
              <span className="meta-label">Duration</span>
              <strong>{quiz.duration} minutes</strong>
            </div>
          </div>
          <div className="quiz-modal-meta-item">
            <Award size={20} className="meta-icon" />
            <div>
              <span className="meta-label">Total Marks</span>
              <strong>{quiz.total_marks}</strong>
            </div>
          </div>
          <div className="quiz-modal-meta-item">
            <Target size={20} className="meta-icon" />
            <div>
              <span className="meta-label">Passing Marks</span>
              <strong>{quiz.passing_marks}</strong>
            </div>
          </div>
          <div className="quiz-modal-meta-item">
            <CheckCircle size={20} className="meta-icon" />
            <div>
              <span className="meta-label">Max Attempts</span>
              <strong>{quiz.max_attempts}</strong>
            </div>
          </div>
        </div>

        {/* Instructions */}
        <div className="quiz-modal-instructions">
          <div className="instructions-heading">
            <AlertCircle size={18} />
            <span>Important Instructions</span>
          </div>
          <ul>
            {INSTRUCTIONS.map((text, i) => (
              <li key={i}>{text}</li>
            ))}
          </ul>
        </div>

        {/* Actions */}
        <div className="quiz-modal-actions">
          <button
            className="quiz-modal-cancel-btn"
            onClick={onCancel}
            disabled={starting}
          >
            Cancel
          </button>
          <button
            className="quiz-modal-start-btn"
            onClick={onStart}
            disabled={starting}
          >
            {starting ? "Starting…" : "Start Test"}
          </button>
        </div>
      </div>
    </div>
  );
};

export default QuizStartModal;

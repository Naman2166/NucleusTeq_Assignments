import { X, CheckCircle2, XCircle } from "lucide-react";
import "./ResultDetails.css";


const ResultDetails = ({ open, result, onClose }) => {
  if (!open || !result) return null;

  return (
    <div className="result-details-overlay">
      <div className="result-details-modal">

        <div className="result-details-header">
          <h2>Result Details</h2>

          <button className="close-btn" onClick={onClose}>
            <X size={20} />
          </button>
        </div>

        <div className="result-summary">

          <div className="summary-item">
            <span>Student</span>
            <strong>{result.student_name}</strong>
          </div>

          <div className="summary-item">
            <span>Quiz</span>
            <strong>{result.quiz_title}</strong>
          </div>

          <div className="summary-item">
            <span>Score</span>
            <strong>
              {result.score}/{result.total_marks}
            </strong>
          </div>

          <div className="summary-item">
            <span>Status</span>

            <strong
              className={
                result.is_pass
                  ? "status-pass"
                  : "status-fail"
              }
            >
              {result.is_pass ? "Pass" : "Fail"}
            </strong>
          </div>

        </div>

        <div className="question-list">

          {result.questions.length > 0 ? (
            result.questions.map((question, index) => (
              <div className="question-card" key={index}>
                <div className="question-header">

                  <span className="question-number">
                    Question {index + 1}
                  </span>

                  {question.is_correct 
                  ? (<CheckCircle2 size={22} className="correct-icon"/>) 
                  : (<XCircle size={22} className="wrong-icon"/>)}

                </div>

                <p className="question-text">
                  {question.question}
                </p>

                <div className="answers-grid">
                  <div className="answer-box">
                    <span>Student Answer</span>
                    <strong className="student-answer-text">
                      {question.selected_option
                        ? question.options[question.selected_option - 1]
                        : "Not Answered"}
                    </strong>
                  </div>

                  <div className="answer-box right-answer">
                    <span>Correct Answer</span>
                    <strong className="correct-answer-text">
                      {question.options[question.correct_answer - 1]}
                    </strong>
                  </div>
                </div>

              </div>
            ))
          ) : (
            <div className="no-question-data">
              No question details available.
            </div>
          )}

        </div>

      </div>
    </div>
  );
};

export default ResultDetails;
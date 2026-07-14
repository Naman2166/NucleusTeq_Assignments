import { useEffect, useState } from "react";
import "./QuizResult.css";
import { useParams, useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import { CheckCircle2, XCircle, LayoutDashboard, ClipboardList, Award, Target, TrendingUp, Hash} from "lucide-react";
import { getResultByAttempt } from "../../utils/services/resultService";
import { getErrorMessage } from "../../utils/errorHandler";


const QuizResult = () => {

  const { attemptId } = useParams();
  const navigate = useNavigate();
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchResult();
  }, [attemptId]);

  const fetchResult = async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await getResultByAttempt(attemptId);
      setResult(data);
    } 
    catch (err) {
      setError(getErrorMessage(err));
      toast.error(getErrorMessage(err));
    } 
    finally {
      setLoading(false);
    }
  };


  if (loading) {
    return (
      <div className="quiz-result-page">
        <div className="qr-loading">
          <div className="student-spinner" />
          <p>Loading your result…</p>
        </div>
      </div>
    );
  }

  if (error || !result) {
    return (
      <div className="quiz-result-page">
        <div className="qr-loading">
          <p className="qr-error-msg">{error || "Result not found."}</p>
          <button className="student-retry-btn" onClick={fetchResult}>
            Try Again
          </button>
        </div>
      </div>
    );
  }

  const {
    quiz_title,
    score,
    total_marks,
    percentage,
    passing_marks,
    is_pass,
    attempt_number,
    submitted_at,
  } = result;

  const pct = percentage != null ? Math.round(percentage) : 0;

  const dateStr = submitted_at
    ? new Date(submitted_at).toLocaleString("en-IN", {
        day: "2-digit",
        month: "short",
        year: "numeric",
        hour: "2-digit",
        minute: "2-digit",
      })
    : "—";

  return (
    <div className="quiz-result-page">
      <div className={`qr-banner ${is_pass ? "pass" : "fail"}`}>
       
        <div className="qr-banner-icon">
          {is_pass ? <CheckCircle2 size={44} /> : <XCircle size={44} />}
        </div>
        
        <div className="qr-banner-text">
          <h1>{is_pass ? "Congratulations! You Passed" : "Better Luck Next Time"}</h1>
          <p>
            {quiz_title} &nbsp;·&nbsp; Attempt {attempt_number} &nbsp;·&nbsp; {dateStr}
          </p>
        </div>
       
        <div className="qr-banner-pct">
          <span className="qr-big-pct">{pct}%</span>
          <span className={`qr-pct-label ${is_pass ? "pass" : "fail"}`}>
            {is_pass ? "Passed" : "Failed"}
          </span>
        </div>
        
      </div>

      <div className="qr-summary-grid">
        <div className="qr-summary-card">
          <div className="qr-card-icon">
            <Award size={20} />
          </div>
          <div>
            <span className="qr-card-label">Obtained Marks</span>
            <strong className="qr-card-value">
              {score} <span className="qr-card-total">/ {total_marks}</span>
            </strong>
          </div>
        </div>

        <div className="qr-summary-card">
          <div className="qr-card-icon">
            <Target size={20} />
          </div>
          <div>
            <span className="qr-card-label">Passing Marks</span>
            <strong className="qr-card-value">{passing_marks}</strong>
          </div>
        </div>

        <div className="qr-summary-card">
          <div className="qr-card-icon">
            <TrendingUp size={20} />
          </div>
          <div>
            <span className="qr-card-label">Percentage</span>
            <strong className="qr-card-value">{pct}%</strong>
          </div>
        </div>

        <div className="qr-summary-card">
          <div className="qr-card-icon">
            <Hash size={20} />
          </div>
          <div>
            <span className="qr-card-label">Attempt Number</span>
            <strong className="qr-card-value">{attempt_number}</strong>
          </div>
        </div>
      </div>

      <div className="qr-actions">
        <button
          className="qr-action-btn secondary"
          onClick={() => navigate("/student/dashboard")}
        >
          <LayoutDashboard size={18} />
          Go to Dashboard
        </button>
        <button
          className="qr-action-btn primary"
          onClick={() => navigate("/student/results")}
        >
          <ClipboardList size={18} />
          My Results
        </button>
      </div>
    </div>
  );
};

export default QuizResult;
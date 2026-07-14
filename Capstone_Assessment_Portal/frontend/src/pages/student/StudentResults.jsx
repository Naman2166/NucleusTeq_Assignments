import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import { Eye } from "lucide-react";
import "./StudentResults.css";
import { getStudentResultHistory } from "../../utils/services/resultService";
import { getErrorMessage } from "../../utils/errorHandler";
import Pagination from "../../components/common/Pagination";
import { PAGE_SIZE } from "../../utils/constants";


const StudentResults = () => {

  const navigate = useNavigate();
  const [results, setResults] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [currentPage, setCurrentPage] = useState(1);

  useEffect(() => {
    fetchResults();
  }, []);

  const fetchResults = async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await getStudentResultHistory();
      const sortedResults = [...data].sort(
        (a, b) => new Date(b.submitted_at) - new Date(a.submitted_at)
    );
      setResults(sortedResults);
    } 
    catch (err) {
      setError(getErrorMessage(err));
      toast.error(getErrorMessage(err));
    } 
    finally {
      setLoading(false);
    }
  };

  const totalPages = Math.ceil(results.length / PAGE_SIZE);
  const paginated = results.slice(
    (currentPage - 1) * PAGE_SIZE,
    currentPage * PAGE_SIZE
  );

  return (
    <div className="student-results-page">

      <div className="student-page-header">
        <h1>My Results</h1>
        <p>Review all your past quiz attempts and scores.</p>
      </div>

      {/* Loading */}
      {loading && (
        <div className="student-loading-state">
          <div className="student-spinner" />
          <p>Loading results…</p>
        </div>
      )}

      {/* Error */}
      {!loading && error && (
        <div className="student-error-state">
          <p>{error}</p>
          <button className="student-retry-btn" onClick={fetchResults}>
            Try Again
          </button>
        </div>
      )}

      {!loading && !error && results.length === 0 && (
        <div className="student-empty-state">
          <p>You haven't completed any quizzes yet.</p>
          <button
            className="student-retry-btn"
            onClick={() => navigate("/student/categories")}
          >
            Explore Categories
          </button>
        </div>
      )}

      {/* Results table */}
      {!loading && !error && results.length > 0 && (
        <>
          <div className="sr-table-card">
            <div className="sr-table-wrapper">
              <table className="sr-table">
                <thead>
                  <tr>
                    <th>S No.</th>
                    <th>Quiz</th>
                    <th>Attempt</th>
                    <th>Score</th>
                    <th>Marks</th>
                    <th>Status</th>
                    <th>Date</th>
                    <th>View</th>
                  </tr>
                </thead>
                <tbody>
                  {paginated.map((r, i) => {
                    const idx = (currentPage - 1) * PAGE_SIZE + i + 1;

                    const percentage = r.percentage != null
                      ? Math.round(r.percentage)
                      : r.total_marks
                        ? Math.round((r.score / r.total_marks) * 100)
                        : "—";

                    const dateStr = r.submitted_at
                      ? new Date(r.submitted_at).toLocaleDateString("en-IN", {
                          day: "2-digit",
                          month: "short",
                          year: "numeric",
                          hour: "2-digit",
                          minute: "2-digit",
                        })
                      : "—";

                    return (
                      <tr key={r.attempt_id || i}>

                        <td className="sr-td-num">{idx}</td>

                        <td className="sr-td-quiz">{r.quiz_title || "—"}</td>

                        <td className="sr-td-attempt">#{r.attempt_number ?? "—"}</td>

                        <td>
                          <span className="sr-score-badge">
                            {percentage !== "—" ? `${percentage}%` : percentage}
                          </span>
                        </td>

                        <td className="sr-marks">
                          {r.score ?? "—"} / {r.total_marks ?? "—"}
                        </td>

                        <td>
                          <span className={`sr-status-badge ${r.is_pass ? "pass" : "fail"}`}>
                            {r.is_pass ? "Pass" : "Fail"}
                          </span>
                        </td>

                        <td className="sr-date">{dateStr}</td>

                        <td>
                          {r.attempt_id ? (
                            <button
                              className="sr-view-btn"
                              onClick={() =>
                                navigate(`/student/quiz/${r.attempt_id}/result`)
                              }
                              title="View Result"
                            >
                              <Eye size={16} />
                            </button>
                          ) : (
                            <span style={{ color: "#cbd5e1" }}>—</span>
                          )}
                        </td>
                        
                      </tr>
                    );
                  })}
                </tbody>
              </table>
            </div>
          </div>

          {totalPages > 1 && (
            <Pagination
              currentPage={currentPage}
              totalPages={totalPages}
              onPageChange={setCurrentPage}
            />
          )}
        </>
      )}

    </div>
  );
};

export default StudentResults;
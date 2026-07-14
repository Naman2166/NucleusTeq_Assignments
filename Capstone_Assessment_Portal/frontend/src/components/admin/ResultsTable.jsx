import { Eye } from "lucide-react";
import "./ResultsTable.css";

const ResultsTable = ({ title, results, currentPage = 1,  itemsPerPage = results.length, onView, onViewAll }) => {

  return (
    <div className="results-table-card">
      {(title || onViewAll) && (
        <div className="card-header">
          {title && <h2>{title}</h2>}

          {onViewAll && (
            <button className="view-all-btn" onClick={onViewAll}>
              View All
            </button>
          )}
        </div>
      )}

      <div className="results-table-wrapper">
        <table className="results-table">
          <thead>
            <tr>
              <th>No.</th>
              <th>Student</th>
              <th>Category</th>
              <th>Quiz</th>
              <th>Score</th>
              <th>Status</th>
              <th>Attempted On</th>
              {onView && <th>Actions</th>}
            </tr>
          </thead>

          <tbody>
            {results.length > 0 ? (
              [...results]
                .sort((a, b) => new Date(b.submitted_at) - new Date(a.submitted_at))
                .map((result, index) => (
                  <tr key={result.attempt_id}>

                    <td>{(currentPage - 1) * itemsPerPage + index + 1}</td>

                    <td className="student-name">
                      {result.student_name}
                    </td>

                    <td>{result.category_name}</td>

                    <td>{result.quiz_title}</td>

                    <td>
                      <span className="score-badge">
                        {result.score}/{result.total_marks}
                      </span>
                    </td>

                    <td>
                      <span className={`status-badge ${result.is_pass ? "pass" : "fail"}`}>
                        {result.is_pass ? "Pass" : "Fail"}
                      </span>
                    </td>

                    <td>
                      {new Date(result.submitted_at + "Z").toLocaleString()}
                    </td>

                    {onView && (
                      <td>
                        <button className="view-btn" onClick={() => onView(result)}>
                          <Eye size={18} />
                        </button>
                      </td>
                    )}
                  </tr>
                ))
            ) : (
              <tr>
                <td colSpan="8" className="no-results">
                  No results found.
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default ResultsTable;
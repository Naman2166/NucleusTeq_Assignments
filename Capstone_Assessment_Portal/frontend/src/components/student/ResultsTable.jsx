import "./ResultsTable.css";


const ResultsTable = ({ title = "Recent Results", results = [], onViewAll }) => {
  return (
    <div className="dashboard-card results-card">
      <div className="card-header">
        <h2>{title}</h2>
        {onViewAll && (
          <button className="view-all-btn" onClick={onViewAll}>
            View All
          </button>
        )}
      </div>

      <table>
        <thead>
          <tr>
            <th>Quiz</th>
            <th>Marks</th>
            <th>Score</th>
            <th>Status</th>
          </tr>
        </thead>
        <tbody>
          {results.length === 0 ? (
            <tr>
              <td colSpan="4" className="no-results">
                No recent results found.
              </td>
            </tr>
          ) : (
            results.map((result) => {
              const percentage =
                result.percentage != null
                  ? Math.round(result.percentage)
                  : Math.round((result.score / result.total_marks) * 100);

              return (
                <tr key={result.attempt_id}>
                  <td>{result.quiz_title}</td>

                  <td>
                    {result.score} / {result.total_marks}
                  </td>

                  <td>{percentage}%</td>

                  <td>
                    <span
                      className={
                        result.is_pass
                          ? "status pass"
                          : "status fail"
                      }
                    >
                      {result.is_pass ? "Pass" : "Fail"}
                    </span>
                  </td>
                </tr>
              );
            })
          )}
        </tbody>
      </table>
    </div>
  );
};

export default ResultsTable;
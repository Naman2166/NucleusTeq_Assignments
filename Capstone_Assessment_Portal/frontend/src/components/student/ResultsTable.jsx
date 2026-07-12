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
            <th>Category</th>
            <th>Score</th>
            <th>Status</th>
          </tr>
        </thead>
        <tbody>
          {results.map((result, index) => (
            <tr key={index}>
              <td>{result.quiz}</td>
              <td>{result.category}</td>
              <td>{result.score}</td>
              <td>
                <span className={result.status === "Pass" ? "status pass" : "status fail"}>
                  {result.status}
                </span>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default ResultsTable;
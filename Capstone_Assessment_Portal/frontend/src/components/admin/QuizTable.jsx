import { Pencil, Trash2 } from "lucide-react";
import "./QuizTable.css";

const QuizTable = ({quizzes, onEdit, onDelete}) => {
  return (
    <div className="quiz-table-wrapper">
      <table className="quiz-table">
        <thead>
          <tr>
            <th>No.</th>
            <th>Quiz Title</th>
            <th>Duration</th>
            <th>Total Marks</th>
            <th>Passing Marks</th>
            <th>Max Attempts</th>
            <th>Actions</th>
          </tr>
        </thead>

        <tbody>
          {quizzes.map((quiz, index) => (
            <tr key={quiz.id}>
              <td>{index + 1}</td>

              <td>
                <div className="quiz-title-cell">
                  <h4>{quiz.title}</h4>
                  <p>{quiz.description}</p>
                </div>
              </td>

              <td>{quiz.duration} min</td>

              <td>
                <span className="marks-badge">
                  {quiz.total_marks}
                </span>
              </td>

              <td>{quiz.passing_marks}</td>

              <td>{quiz.max_attempts}</td>

              <td>
                <div className="quiz-actions">
                  <button
                    className="quiz-icon-btn"
                    onClick={() => onEdit(quiz)}
                    title="Edit Quiz"
                  >
                    <Pencil size={16} />
                  </button>

                  <button
                    className="quiz-icon-btn danger"
                    onClick={() => onDelete(quiz)}
                    title="Delete Quiz"
                  >
                    <Trash2 size={16} />
                  </button>
                </div>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default QuizTable;
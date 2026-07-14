import { Pencil, Trash2 } from "lucide-react";
import "./QuestionTable.css";


const QuestionTable = ({questions, currentPage, itemsPerPage, onEdit, onDelete}) => {

  return (
    <div className="question-table-wrapper">

      <table className="question-table">

        <thead>
          <tr>
            <th>No.</th>
            <th>Question</th>
            <th>Type</th>
            <th>Difficulty</th>
            <th>Marks</th>
            <th>Actions</th>
          </tr>
        </thead>

        <tbody>

          {questions.length > 0 ? (
            questions.map((question, index) => (
              <tr key={question.id}>

                <td>{(currentPage - 1) * itemsPerPage + index + 1}</td>

                <td className="question-text">
                  {question.question}
                </td>

                <td>
                  <span className="question-type">
                    {question.question_type}
                  </span>
                </td>

                <td>
                  <span
                    className={`difficulty-badge ${question.difficulty
                      .toLowerCase()
                      .replace(" ", "-")}`}
                  >
                    {question.difficulty}
                  </span>
                </td>

                <td>
                  <span className="marks-badge">
                    {question.marks}
                  </span>
                </td>

                <td>

                  <div className="question-actions">

                    <button
                      className="question-icon-btn"
                      title="Edit Question"
                      onClick={() => onEdit(question)}
                    >
                      <Pencil size={16} />
                    </button>

                    <button
                      className="question-icon-btn danger"
                      title="Delete Question"
                      onClick={() => onDelete(question)}
                    >
                      <Trash2 size={16} />
                    </button>

                  </div>

                </td>

              </tr>
            ))
          ) : (
            <tr>
              <td
                colSpan="6"
                className="no-questions"
              >
                No questions found.
              </td>
            </tr>
          )}

        </tbody>

      </table>

    </div>
  );
};

export default QuestionTable;
import { Pencil, Trash2 } from "lucide-react";
import "./CategoryTable.css";

const CategoryTable = ({categories, onEdit, onDelete, currentPage, itemsPerPage}) => {
  return (
    <div className="category-table-wrapper">
      <table className="category-table">
        <thead>
          <tr>
            <th>No.</th>
            <th>Category Name</th>
            <th>Quizzes</th>
            <th>Total Attempts</th>
            <th>Actions</th>
          </tr>
        </thead>

        <tbody>
          {categories.length > 0 ? (
            categories.map((category, index) => (
              <tr key={category.id}>

                <td>
                  {(currentPage - 1) * itemsPerPage + index + 1}
                </td>

                <td>
                  <div className="category-name-cell">
                    <span className="category-name">
                      {category.name}
                    </span>
                  </div>
                </td>

                <td>{category.quizCount}</td>

                <td>{category.attempts}</td>

                <td>
                  <div className="action-buttons">
                    <button
                      className="table-icon-btn"
                      onClick={() => onEdit(category)}
                      title="Edit Category"
                    >
                      <Pencil size={16} />
                    </button>

                    <button
                      className="table-icon-btn danger"
                      onClick={() => onDelete(category)}
                      title="Delete Category"
                    >
                      <Trash2 size={16} />
                    </button>
                  </div>
                </td>
                
              </tr>
            ))
          ) 
          : 
          (<tr>
              <td
                className="empty-state" colSpan="6">
                No categories found.
              </td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
};

export default CategoryTable;
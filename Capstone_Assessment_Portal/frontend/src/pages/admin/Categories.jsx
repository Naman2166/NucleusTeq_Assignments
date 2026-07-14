import { useEffect, useState } from "react";
import "./Categories.css";
import { Plus, Search } from "lucide-react";
import CategoryTable from "../../components/admin/CategoryTable";
import CategoryForm from "../../components/admin/CategoryForm";
import { getQuizzes } from "../../utils/services/quizService";
import { getAllResults } from "../../utils/services/resultService";
import { toast } from "react-toastify";
import { getErrorMessage } from "../../utils/errorHandler";
import Pagination from "../../components/common/Pagination";
import { PAGE_SIZE } from "../../utils/constants";
import { getCategories, createCategory, updateCategory, deleteCategory } from "../../utils/services/categoryService";


const Categories = () => {
  
  const [search, setSearch] = useState("");
  const [showAddModal, setShowAddModal] = useState(false);
  const [showEditModal, setShowEditModal] = useState(false);
  const [selectedCategory, setSelectedCategory] = useState(null);
  const [categories, setCategories] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);


  const filteredCategories = categories.filter(
    (category) => category.name
      .toLowerCase()
      .includes(search.toLowerCase())
  );

  const totalPages = Math.ceil(filteredCategories.length / PAGE_SIZE);

  const paginatedCategories =
    filteredCategories.slice(
      (currentPage - 1) * PAGE_SIZE,
      currentPage * PAGE_SIZE
    );

  const fetchCategories = async () => {
    try {
      const [categories, quizzes, results] = await Promise.all([
        getCategories(),
        getQuizzes(),
        getAllResults(),
      ]);

      const categoryData = categories.map((category) => {
        const quizzesInCategory = quizzes.filter(
          (quiz) => quiz.category_id === category.id
        );

        const quizIds = quizzesInCategory.map((quiz) => quiz.id);

        const totalAttempts = results.filter(
          (result) => quizIds.includes(result.quiz_id)
        ).length;

        return {
          ...category,
          quizCount: quizzesInCategory.length,
          attempts: totalAttempts,
        };
      });

      setCategories(categoryData);
    }
    catch (error) {
      toast.error(getErrorMessage(error));
    }
  };

  useEffect(() => {
    fetchCategories();
  }, []);

  useEffect(() => {
    if (currentPage > totalPages && totalPages > 0) {
      setCurrentPage(totalPages);
    }
  }, [currentPage, totalPages]);

  const handleAddCategory = async (formData) => {
    try {
      await createCategory(formData);
      toast.success("Category created successfully.");

      await fetchCategories();
      setShowAddModal(false);
    }
    catch (error) {
      toast.error(getErrorMessage(error));
    }
  };

  const handleEdit = (category) => {
    setSelectedCategory(category);
    setShowEditModal(true);
  };

  const handleUpdateCategory = async (formData) => {
    try {
      await updateCategory(selectedCategory.id, formData);
      toast.success("Category updated successfully.");

      await fetchCategories();
      setShowEditModal(false);
      setSelectedCategory(null);
    }
    catch (error) {
      toast.error(getErrorMessage(error));
    }
  };

  const handleDelete = async (category) => {
    try {
      await deleteCategory(category.id);
      toast.success("Category deleted successfully.");

      await fetchCategories();
    }
    catch (error) {
      toast.error(getErrorMessage(error));
    }
  };


  return (
    <div className="admin-categories-page">
      <div className="admin-categories-header">
        <div>
          <h1>Categories</h1>
          <p>
            Manage quiz categories and monitor their overall performance.
          </p>
        </div>

        <div className="header-actions">
          <div className="search-box">
            <Search size={18} />
            <input
              type="text"
              placeholder="Search category..."
              value={search}
              onChange={(e) => {setSearch(e.target.value); setCurrentPage(1);}}
            />
          </div>

          <button
            className="add-category-btn"
            onClick={() => setShowAddModal(true)}
          >
            <Plus size={18} />
            Add Category
          </button>
        </div>
      </div>

      <CategoryTable
        categories={paginatedCategories}
        currentPage={currentPage}
        itemsPerPage={PAGE_SIZE}
        onEdit={handleEdit}
        onDelete={handleDelete}
      />

      <Pagination
        currentPage={currentPage}
        totalPages={totalPages}
        onPageChange={setCurrentPage}
      />

      <CategoryForm
        open={showAddModal}
        title="Add Category"
        buttonText="Add Category"
        initialData={{name: "", description: ""}}
        onSubmit={handleAddCategory}
        onClose={() => setShowAddModal(false)}
      />

      <CategoryForm
        open={showEditModal}
        title="Update Category"
        buttonText="Update Category"
        onSubmit={handleUpdateCategory}
        initialData={selectedCategory || {name: "", description: ""}}
        onClose={() => {
          setShowEditModal(false);
          setSelectedCategory(null);
        }}
      />
    </div>
  );
};

export default Categories;
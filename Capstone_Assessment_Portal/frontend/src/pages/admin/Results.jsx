import { useState, useEffect } from "react";
import "./Results.css";
import { Search } from "lucide-react";
import ResultsTable from "../../components/admin/ResultsTable";
import ResultDetails from "../../components/admin/ResultDetails";
import { getAllResults, getAdminResultByAttempt } from "../../utils/services/resultService";
import { getCategories } from "../../utils/services/categoryService";
import { getQuizzes } from "../../utils/services/quizService";
import { toast } from "react-toastify";
import { getErrorMessage } from "../../utils/errorHandler";
import Pagination from "../../components/common/Pagination";
import { PAGE_SIZE } from "../../utils/constants";


const Results = () => {

  const [search, setSearch] = useState("");
  const [selectedCategory, setSelectedCategory] = useState("");
  const [selectedQuiz, setSelectedQuiz] = useState("");
  const [showResultModal, setShowResultModal] = useState(false);
  const [selectedResult, setSelectedResult] = useState(null);
  const [categories, setCategories] = useState([]);
  const [quizzes, setQuizzes] = useState([]);
  const [results, setResults] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);


  const fetchData = async () => {
    try {
      const [categories, quizzes, results] =
        await Promise.all([
          getCategories(),
          getQuizzes(),
          getAllResults(),
        ]);

      setCategories(categories);
      setQuizzes(quizzes);
      setResults(results);
    } 
    catch (error) {
      toast.error(getErrorMessage(error));
    }
  };

  const selectedCategoryName = categories.find(
    (category) => category.id === selectedCategory
  )?.name;


  const filteredResults = results.filter((result) => {

    const studentMatch = result.student_name
      .toLowerCase().includes(search.toLowerCase());

    const categoryMatch = !selectedCategory || result.category_name === selectedCategoryName;
    const quizMatch = !selectedQuiz || result.quiz_id === selectedQuiz;

    return (studentMatch && categoryMatch && quizMatch);
  });

  const totalPages = Math.ceil(filteredResults.length / PAGE_SIZE);

  const paginatedResults = filteredResults.slice(
    (currentPage - 1) * PAGE_SIZE,
    currentPage * PAGE_SIZE
  );


  useEffect(() => {
    fetchData();
  }, []);

  useEffect(() => {
    if (currentPage > totalPages && totalPages > 0) {
      setCurrentPage(totalPages);
    }
  }, [currentPage, totalPages]);


  const filteredQuizzes = quizzes.filter(
    (quiz) => !selectedCategory || quiz.category_id === selectedCategory
  );


  const handleView = async (result) => {
    try {
      const data = await getAdminResultByAttempt(result.attempt_id);

      setSelectedResult({
        ...data,
        student_name: result.student_name,
        category_name: result.category_name,
      });

      setShowResultModal(true);
    } 
    catch (error) {
      toast.error(getErrorMessage(error));
    }
  };


  return (
    <div className="admin-results-page">
      <div className="admin-results-header">
        <div>
          <h1>Results</h1>

          <p>
            Review student quiz results and performance.
          </p>
        </div>
      </div>

      <div className="results-filters">
        <div className="results-search-box">
          <Search size={18} />

          <input
            type="text"
            placeholder="Search student..."
            value={search}
            onChange={(e) => { setSearch(e.target.value); setCurrentPage(1); }}
          />
        </div>

        <div className="filter-group">
          <select
            value={selectedCategory}
            onChange={(e) => {
              setSelectedCategory(e.target.value);
              setSelectedQuiz("");
              setCurrentPage(1);
            }}
          >
            <option value="">All Categories</option>

            {categories.map((category) => (
              <option key={category.id} value={category.id}>
                {category.name}
              </option>
            ))}
          </select>
        </div>

        <div className="filter-group">
          <select
            value={selectedQuiz}
            disabled={!selectedCategory}
            onChange={(e) => {setSelectedQuiz(e.target.value); setCurrentPage(1);}}
          >
            <option value="">All Quizzes</option>

            {filteredQuizzes.map((quiz) => (
              <option key={quiz.id} value={quiz.id}>
                {quiz.title}
              </option>
            ))}
          </select>
        </div>

      </div>

      <ResultsTable
        results={paginatedResults}
        currentPage={currentPage}
        itemsPerPage={PAGE_SIZE}
        onView={handleView}
      />

      <Pagination
        currentPage={currentPage}
        totalPages={totalPages}
        onPageChange={setCurrentPage}
      />

      <ResultDetails
        open={showResultModal}
        result={selectedResult}
        onClose={() => {
          setShowResultModal(false);
          setSelectedResult(null);
        }}
      />
    </div>
  );
};

export default Results;
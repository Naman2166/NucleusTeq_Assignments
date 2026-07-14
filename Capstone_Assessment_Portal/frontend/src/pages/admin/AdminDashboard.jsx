import { useEffect, useState } from "react";
import "./AdminDashboard.css";
import {Layers, BookOpen, Users, UserCheck} from "lucide-react";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import StatCard from "../../components/common/StatCard";
import CircularProgress from "../../components/common/CircularProgress";
import ResultsTable from "../../components/admin/ResultsTable";
import { getStudents } from "../../utils/services/userService";
import { getCategories } from "../../utils/services/categoryService";
import { getQuizzes } from "../../utils/services/quizService";
import { getAllResults } from "../../utils/services/resultService";
import { getErrorMessage } from "../../utils/errorHandler";


const AdminDashboard = () => {

  const navigate = useNavigate();

  const [categories, setCategories] = useState([]);
  const [quizzes, setQuizzes] = useState([]);
  const [results, setResults] = useState([]);
  const [students, setStudents] = useState([]);

  useEffect(() => {
    fetchDashboard();
  }, []);

  const fetchDashboard = async () => {
    try {
      const [
        categoryData,
        quizData,
        resultData,
        studentData,
      ] = await Promise.all([
        getCategories(),
        getQuizzes(),
        getAllResults(),
        getStudents(),
      ]);

      setCategories(categoryData);
      setQuizzes(quizData);
      setResults(resultData);
      setStudents(studentData);
    } 
    catch (error) {
      toast.error(getErrorMessage(error));
    }
  };

  const totalStudents = students.length;

  const studentsAttempted = new Set(
    results.map((r) => r.student_name)
  ).size;

  const stats = [
    {
      title: "Total Categories",
      value: categories.length,
      icon: Layers,
      color: "#3253cb",
    },
    {
      title: "Total Quizzes",
      value: quizzes.length,
      icon: BookOpen,
      color: "#3253cb",
    },
    {
      title: "Total Students",
      value: totalStudents,
      icon: Users,
      color: "#3253cb",
    },
    {
      title: "Students Attempted",
      value: studentsAttempted,
      icon: UserCheck,
      color: "#3253cb",
    },
  ];

  const passCount = results.filter(
    (result) => result.is_pass
  ).length;

  const performance = [
    {
      label: "Overall Success Rate",
      completed: passCount,
      total: results.length || 1,
      color: "#16a34a",
    },
    {
      label: "Student Participation",
      completed: studentsAttempted,
      total: totalStudents || 1,
      color: "#2563eb",
    },
  ];


  const topCategories = categories
    .map((category) => {
      const categoryQuizzes = quizzes.filter(
        (quiz) => quiz.category_id === category.id
      );

      const quizIds = categoryQuizzes.map(
        (quiz) => quiz.id
      );

      const attempts = results.filter((result) =>
        quizIds.includes(result.quiz_id)
      ).length;

      return {name: category.name, attempts};
    })
    .sort((a, b) => b.attempts - a.attempts)
    .slice(0, 4);


  const maxAttempts = Math.max(
    ...topCategories.map((category) => category.attempts),
    1
  );


  const recentSubmissions = [...results]
    .sort((a, b) =>
        new Date(b.submitted_at) -
        new Date(a.submitted_at)
    )
    .slice(0, 4);


  const totalAttempts = topCategories.reduce(
    (sum, category) => sum + category.attempts,
    0
  );


  return (
    <div className="admin-dashboard">

      <div className="admin-header">
        <div>
          <h1>Admin Dashboard</h1>

          <p>
            Overview of categories, quizzes and student
            activity.
          </p>
        </div>
      </div>

      <div className="admin-stats">
        {stats.map((item) => (
          <StatCard
            key={item.title}
            icon={item.icon}
            label={item.title}
            value={item.value}
            color={item.color}
          />
        ))}
      </div>

      <div className="admin-grid">

        <div className="admin-card">
          <div className="admin-card-header">
            <h2>Performance Overview</h2>
          </div>

          <div className="admin-progress-circles">
            {performance.map((item) => (
              <CircularProgress
                key={item.label}
                label={item.label}
                completed={item.completed}
                total={item.total}
                color={item.color}
              />
            ))}
          </div>
        </div>

        <div className="admin-card">
          <div className="admin-card-header">
            <h2>Top Categories by Attempts</h2>

            <button
              className="admin-view-all-btn"
              onClick={() => navigate("/admin/categories")}
            >
              View All
            </button>
          </div>

          {topCategories.length > 0 ? (
            topCategories.map((category) => (
              <div className="admin-bar-item" key={category.name}>
                <div className="admin-bar-info">
                  <span>{category.name}</span>

                  <span>
                    {category.attempts}{" "}
                    {category.attempts === 1
                      ? "Attempt"
                      : "Attempts"}
                  </span>
                </div>


                <div className="admin-progress-bar">
                  <div
                    className="admin-progress-fill"
                    style={{
                      width: `${totalAttempts ? (category.attempts / totalAttempts) * 100 : 0}%`,
                      background: "#2563eb",
                    }}
                  />
                </div>
              </div>
            ))
          ) : (
            <p>No categories found.</p>
          )}
        </div>

      </div>

      <ResultsTable
        title="Recent Submissions"
        results={recentSubmissions}
        onViewAll={() => navigate("/admin/results")}
      />

    </div>
  );
};

export default AdminDashboard;
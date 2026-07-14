import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import "./StudentDashboard.css";
import { Layers, BookOpen, CheckCircle2, Clock } from "lucide-react";
import StatCard from "../../components/common/StatCard";
import CircularProgress from "../../components/common/CircularProgress";
import ResultsTable from "../../components/student/ResultsTable";
import { getStudentResultHistory } from "../../utils/services/resultService";
import { toast } from "react-toastify";
import { getErrorMessage } from "../../utils/errorHandler";
import { getCategories } from "../../utils/services/categoryService";
import { getQuizzes } from "../../utils/services/quizService";


const StudentDashboard = () => {

  const navigate = useNavigate();

  const [stats, setStats] = useState([]);
  const [overallProgress, setOverallProgress] = useState([]);
  const [topCategories, setTopCategories] = useState([]);
  const [recentResults, setRecentResults] = useState([]);


  const fetchDashboardData = async () => {
    try {
      const [categories, quizzes, history] = await Promise.all([
        getCategories(),
        getQuizzes(),
        getStudentResultHistory(),
      ]);

      setRecentResults(history.slice(0, 4));

      const completedQuizCount = new Set(history.map((item) => item.quiz_id)).size;
      const remainingQuizCount = Math.max(quizzes.length - completedQuizCount, 0);

      setStats([
        {
          title: "Categories",
          value: categories.length,
          icon: Layers,
          color: "#122e6f",
        },
        {
          title: "Total Quizzes",
          value: quizzes.length,
          icon: BookOpen,
          color: "#122e6f",
        },
        {
          title: "Completed Quiz",
          value: completedQuizCount,
          icon: CheckCircle2,
          color: "#122e6f",
        },
        {
          title: "Remaning Quiz",
          value: remainingQuizCount,
          icon: Clock,
          color: "#122e6f",
        },
      ]);


      const passedQuizCount = new Set(
        history
        .filter((item) => item.is_pass)
        .map((item) => item.quiz_id)
      ).size;

      setOverallProgress([
        {
          label: "Quiz Success",
          completed: passedQuizCount,
          total: completedQuizCount,
          color: "#16a34a",
        },
        {
          label: "Quizzes Completed",
          completed: completedQuizCount,
          total: quizzes.length,
          color: "#2563eb",
        },
      ]);


      const categoryMap = {};

      categories.forEach((category) => {
        categoryMap[category.id] = {
          name: category.name,
          completed: 0,
          total: 0,
        };
      });

      quizzes.forEach((quiz) => {
        if (categoryMap[quiz.category_id]) {
          categoryMap[quiz.category_id].total++;
        }
      });

      const completedQuizIds = new Set(
        history.map((item) => item.quiz_id)
      );

      quizzes.forEach((quiz) => {
        if (completedQuizIds.has(quiz.id) && categoryMap[quiz.category_id]) {
          categoryMap[quiz.category_id].completed++;
        }
      });

      const topCategoriesData = Object.values(categoryMap)
        .filter((item) => item.total > 0)
        .sort((a, b) => {
          if (b.completed !== a.completed) {
            return b.completed - a.completed;
          }
          return b.total - a.total;
        })
        .slice(0, 4);

      setTopCategories(topCategoriesData);
    }
    catch (err) {
      toast.error(getErrorMessage(err));
    }
  };

  useEffect(() => {
    fetchDashboardData();
  }, []);



  return (
    <div className="student-dashboard">

      <div className="dashboard-header">
        <div>
          <h1>Welcome, Naman </h1>
          <p>Track your progress and continue your learning journey.</p>
        </div>
      </div>

      {/* Statistics */}
      <div className="stats-container">
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


      <div className="dashboard-grid">

        {/* Overall Progress*/}
        <div className="dashboard-card">
          <h2>Overall Progress</h2>

          <div className="progress-circles">
            {overallProgress.map((item) => (
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

        <div className="dashboard-card">
          <div className="card-header">
            <h2>Category Progress</h2>
            <button onClick={() => navigate("/student/categories")} className="view-all-btn">View All</button>
          </div>

          {topCategories.map((category) => (
            <div className="category-item" key={category.name}>
              <div className="category-info">
                <span>{category.name}</span>
                <span>
                  {category.completed}/{category.total}
                </span>
              </div>
              <div className="progress-bar">
                <div
                  className="progress-fill"
                  style={{
                    width: `${(category.completed / category.total) * 100}%`,
                  }}
                ></div>
              </div>
            </div>
          ))}
        </div>

      </div>

      {/* Results table */}
      <ResultsTable
        title="Recent Results"
        results={recentResults}
        onViewAll={() => navigate("/student/results")}
      />

    </div>
  );
};

export default StudentDashboard;
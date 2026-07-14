import { Layers, BookOpen, CheckCircle2, Clock } from "lucide-react";
import StatCard from "../../components/common/StatCard";
import CircularProgress from "../../components/common/CircularProgress";
import ResultsTable from "../../components/student/ResultsTable";
import "./StudentDashboard.css";
import { useNavigate } from "react-router-dom";


const StudentDashboard = () => {
  
  const navigate = useNavigate();

  const stats = [
    { title: "Categories", value: 12, icon: Layers, color: "#7c3aed" },
    { title: "Total Quizzes", value: 48, icon: BookOpen, color: "#d97706" },
    { title: "Completed", value: 25, icon: CheckCircle2, color: "#16c56d" },
    { title: "Pending", value: 3, icon: Clock, color: "#e23431" },
  ];

  const overallProgress = [
    { label: "Categories Covered", completed: 7, total: 12, color: "#2563eb" },
    { label: "Quizzes Completed", completed: 25, total: 48, color: "#16a34a" },
  ];

  const topCategories = [
    { name: "Programming", completed: 8, total: 10 },
    { name: "Aptitude", completed: 6, total: 8 },
    { name: "Database", completed: 5, total: 8 },
    { name: "Operating System", completed: 2, total: 6 },
  ];

  const recentResults = [
    { quiz: "Java Basics", category: "Programming", score: "92%", status: "Pass" },
    { quiz: "DBMS Fundamentals", category: "Database", score: "84%", status: "Pass" },
    { quiz: "Operating System", category: "OS", score: "63%", status: "Pass" },
    { quiz: "Aptitude Test", category: "Aptitude", score: "41%", status: "Fail" },
  ];

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

        {/* Top Categories */}
        <div className="dashboard-card">
          <div className="card-header">
            <h2>Top Categories</h2>
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
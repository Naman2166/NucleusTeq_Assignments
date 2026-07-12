import { Outlet } from "react-router-dom";
import StudentNavbar from "../../components/student/StudentNavbar";
import "./StudentLayout.css";

const StudentLayout = () => {
  return (
    <div className="student-layout">
      <StudentNavbar />

      <main className="student-content">
        <Outlet />
      </main>
    </div>
  );
};

export default StudentLayout;
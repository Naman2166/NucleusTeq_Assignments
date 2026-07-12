import { Outlet } from "react-router-dom";
import Sidebar from "../../components/admin/Sidebar";
import AdminNavbar from "../../components/admin/AdminNavbar";
import "./AdminLayout.css";

const AdminLayout = () => {
  return (
    <div className="admin-layout">
      
      <Sidebar />

      <div className="admin-main">
        <AdminNavbar />

        <main className="admin-content">
          <Outlet />
        </main>
      </div>
      
    </div>
  );
};

export default AdminLayout;
import { NavLink } from "react-router-dom";
import "./Sidebar.css";

const menuItems = [
  {
    name: "Dashboard",
    path: "/admin/dashboard",
  },
  {
    name: "Categories",
    path: "/admin/categories",
  },
  {
    name: "Quizzes",
    path: "/admin/quizzes",
  },
  {
    name: "Questions",
    path: "/admin/questions",
  },
  {
    name: "Results",
    path: "/admin/results",
  },
];

const Sidebar = () => {
  return (
    <aside className="sidebar">
      <nav className="sidebar-nav">
        {menuItems.map((item) => (
          <NavLink
            key={item.path}
            to={item.path}
            className={({ isActive }) =>
              isActive ? "sidebar-link active" : "sidebar-link"
            }
          >
            {item.name}
          </NavLink>
        ))}
      </nav>
    </aside>
  );
};

export default Sidebar;
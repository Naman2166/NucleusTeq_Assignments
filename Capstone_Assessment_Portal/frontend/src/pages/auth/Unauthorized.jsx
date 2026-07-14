import React from "react";
import "./Unauthorized.css";

const Unauthorized = () => {
  return (
    <div className="container">
      <div className="content">
        <h1>403 - Unauthorized</h1>
        <p>You don't have permission to access this page.</p>
      </div>
    </div>
  );
};

export default Unauthorized;
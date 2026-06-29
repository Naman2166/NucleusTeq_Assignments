import React from "react";

const Unauthorized = () => {
  return (
    <div style={{ margin: "10px 10px", textAlign: "left" }}>
      <h1 style={{ fontSize: "38px", marginBottom: "10px" }}>
        403 - Unauthorized
      </h1>
      <p style={{ fontSize: "19px" }}>
        You don't have permission to access this page.
      </p>
    </div>
  );
};

export default Unauthorized;
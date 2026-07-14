import "./StatCard.css";

const StatCard = ({ icon: Icon, label, value, color }) => {
  return (
    <div className="stat-card">

      <div className="stat-card-top">
        <p className="stat-label">{label}</p>
      </div>

      <div className="bottom">
        <h2 className="stat-value">{value}</h2>
        <div
          className="stat-icon"
          style={{ background: `${color}14`, color: color }}
        >
          <Icon size={22} strokeWidth={2.2} />
        </div>
      </div>

      <div className="stat-accent" style={{ background: color }}></div>
    </div>
  );
};

export default StatCard;
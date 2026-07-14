import "./CircularProgress.css";


const CircularProgress = ({completed, total, color = "#2563eb", label, size = 130, strokeWidth = 10,}) => {

  const percentage = total > 0 ? Math.round((completed/total) * 100) : 0;
  const radius = (size - strokeWidth) / 2;
  const circumference = 2 * Math.PI * radius;
  const filled = (percentage/100) * circumference;

  return (
    <div className="circular-progress-item">
      <svg width={size} height={size} viewBox={`0 0 ${size} ${size}`}>
        <circle
          cx={size / 2}
          cy={size / 2}
          r={radius}
          fill="none"
          stroke="#e2e8f0"
          strokeWidth={strokeWidth}
        />
        <circle
          cx={size / 2}
          cy={size / 2}
          r={radius}
          fill="none"
          stroke={color}
          strokeWidth={strokeWidth}
          strokeLinecap="round"
          strokeDasharray={`${filled} ${circumference}`}
          transform={`rotate(-90 ${size / 2} ${size / 2})`}
        />
        <text
          x="50%"
          y="50%"
          textAnchor="middle"
          dominantBaseline="middle"
          className="circular-progress-text"
        >
          {percentage}%
        </text>
      </svg>

      {label && <p className="circular-progress-label">{label}</p>}
      <p className="circular-progress-count"> {completed}/{total}</p>
    </div>
  );
};

export default CircularProgress;
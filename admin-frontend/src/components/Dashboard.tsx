import React from 'react';
import { Link } from 'react-router-dom';

const Dashboard = () => (
  <div className="dashboard">
    <h1>Welcome to GardenHub Admin Dashboard</h1>
    <div className="nav-buttons">
      <Link to="/user"><button>User Management</button></Link>
      <Link to="/garden"><button>Garden Management</button></Link>
      <Link to="/market"><button>Market</button></Link>
      <Link to="/ticket"><button>Ticket System</button></Link>
      <Link to="/logout"><button>Logout</button></Link>
    </div>
  </div>
);

export default Dashboard;

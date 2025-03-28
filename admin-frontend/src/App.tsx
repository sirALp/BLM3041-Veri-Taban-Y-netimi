import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Cookies from 'js-cookie';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import Login from './components/Login.tsx';
import Dashboard from './components/Dashboard.tsx';
import Logout from './components/Logout.tsx';
import UserManagement from './components/UserManagement/UserManagement.tsx'; // Ensure this import is correct
import GardenManagement from './components/GardenManagement/GardenManagement.tsx';
import RentalManagement from './components/RentalManagement/RentalManagement.tsx';
import MarketManagement from './components/MarketManagement/MarketManagement.tsx';
import GardenList from './components/GardenManagement/GardenList.tsx';
import TicketManagement from './components/TicketManagement/TicketManagement.tsx';

const App = () => {
  const [loggedIn, setLoggedIn] = useState(false);

  useEffect(() => {
    const isLoggedIn = Cookies.get('loggedIn') === 'true';
    setLoggedIn(isLoggedIn);
  }, []);

  return (
    <Router>
      <div className="app-container">
        <div id='gardenhub-logo' style={{ height: loggedIn ? '119px' : '238px'}}></div>
        <ToastContainer />
        <Routes>
          <Route path="/login" element={!loggedIn ? <Login setLoggedIn={setLoggedIn} /> : <Navigate to="/" />} />
          <Route path="/logout" element={<Logout setLoggedIn={setLoggedIn} />} />
          <Route path="/" element={loggedIn ? <Dashboard /> : <Navigate to="/login" />} />
          <Route path="/user" element={loggedIn ? <UserManagement /> : <Navigate to="/login" />} />
          <Route path="/garden" element={loggedIn ? <GardenManagement /> : <Navigate to="/login" />}>
            <Route path="rental" element={<RentalManagement />} />
            <Route path='all' element={<GardenList/>}/>
          </Route>
          <Route path="/market" element={loggedIn ? <MarketManagement/> : <Navigate to="/login" /> } />
          <Route path="/ticket" element={loggedIn ? <TicketManagement/> : <Navigate to="/login"/> } />
        </Routes>
      </div>
    </Router>
  );
};

export default App;
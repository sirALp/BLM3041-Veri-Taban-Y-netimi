import React, { useState, useEffect } from 'react';
import UserTable from './UserTable.tsx';
import { useNavigate } from 'react-router-dom';
import { IUser } from '../../interfaces/interfaces.ts';
import '../../styles/UserManagement.css';



const UserManagement = () => {
  const [users, setUsers] = useState<IUser[]>([]);
  const navigate = useNavigate();

  useEffect(() => {
    fetch('http://localhost:4000/api/users/')
      .then((response) => response.json())
      .then((data) => {
        const formattedData = data.map((user) => {
          return {
            user_id: user.user_id,
            username: user.username,
            full_name: {
              first_name: user.full_name.slice(1, -1).split(',')[0],
              last_name: user.full_name.slice(1, -1).split(',')[1],
            },
            user_role: user.user_role,
          };
        });
        setUsers(formattedData);
      })
      .catch((error) => console.error('Error fetching user data:', error));
  }, []);

  return (
    <div className="user-management-container">
      <h1>User Management</h1>
      <UserTable users={users} setUsers={setUsers} />
      <button className='back-button' onClick={() => navigate('/')}>
        Back to Dashboard
      </button>
    </div>
  );
};

export default UserManagement;
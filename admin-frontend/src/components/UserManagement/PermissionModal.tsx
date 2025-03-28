import React from 'react';
import { Role } from '../../types/types.ts';
import { IUser } from '../../interfaces/interfaces.ts';

const PermissionModal = ({ user, onClose, setUsers, onCancel }) => {
  const toggleRole = () => {
    const newRole:Role = user.user_role === 'admin' ? 'member' : 'admin';
    // Placeholder for SQL query to update role
    setUsers((prevUsers) =>
      prevUsers.map((u:IUser) => (u.user_id === user.user_id ? { ...u, user_role: newRole } : u))
    );
    onClose(newRole,user.user_id);
  };

  return (
    <div className="modal permission-modal">
      <h2>Change Permission for <span style={{color:'darkblue'}} > {user.username} </span></h2>
      <p>Current Role: <strong> {user.user_role.toUpperCase() }</strong></p>
      <button onClick={toggleRole}>Switch to {user.user_role === 'admin' ? 'MEMBER' : 'ADMIN'}</button>
      <button onClick={onCancel}>Cancel</button>
    </div>
  );
};

export default PermissionModal;
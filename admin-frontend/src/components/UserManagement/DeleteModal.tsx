import React from 'react';
import { IUser } from '../../interfaces/interfaces.ts';

const DeleteModal = ({ user, onDelete, setUsers,onCancel }) => {
  const handleDelete = () => {
    onDelete(user.user_id);
    setUsers((prevUsers) => prevUsers.filter((u:IUser) => u.user_id !== user.user_id));
  };

  return (
    <div className="modal">
      <h2>Confirm Deletion</h2>
      <p>Are you sure you want to delete <strong>{user.username}</strong> ?</p>
      <button className='delete-button' onClick={handleDelete}>Delete</button>
      <button onClick={onCancel}>Cancel</button>
    </div>
  );
};

export default DeleteModal;
import React, { useState } from 'react';
import PermissionModal from './PermissionModal.tsx';
import DeleteModal from './DeleteModal.tsx';
import { IUser } from '../../interfaces/interfaces.ts';
import { Role } from '../../types/types.ts';

const UserTable = ({ users, setUsers }) => {
  const [selectedUser, setSelectedUser] = useState<IUser | null>(null);
  const [modalType, setModalType] = useState('');

  const handlePermissionClick = (user) => {
    setSelectedUser(user);
    setModalType('permission');
  };

  const handleDeleteClick = (user) => {
    setSelectedUser(user);
    setModalType('delete');
  };

  const handleOnClose = (newRole:Role,user_id:IUser['user_id']) => {
    setModalType('');
    console.log("NEW ROLE ID: " + newRole + "  " + user_id);
    fetch(`http://localhost:4000/api/users/${user_id}/${newRole}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ user_id, newRole }),
    })
  }

  const handleOnDelete = (userId:IUser['user_id']) => {
    setModalType('');
    fetch(`http://localhost:4000/api/users/${userId}`, {
      method: 'DELETE',
    })
      .then((response) => {
        if (response.ok) {
          setUsers((prevUsers) => prevUsers.filter((u:IUser) => u.user_id !== userId));
          console.log(`User with ID ${userId} has been deleted.`);
        } else {
          console.error('Failed to delete user.');
        }
      })
      .catch((error) => console.error('Error deleting user:', error));
  }

  return (
    <div style={{ width: '80%', margin: '0 auto' }}>
      <table className='user-table' style={{ width: '100%', borderCollapse: 'collapse' }}>
        <thead>
          <tr>
            <th>ID</th>
            <th>Username</th>
            <th>Name</th>
            <th>Role</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {users.map((user) => (
            <tr key={user.user_id}>
              <td>{user.user_id}</td>
              <td>{user.username}</td>
              <td>{user.full_name.first_name + ' ' + user.full_name.last_name}</td>
              <td style={{ textTransform: 'capitalize', color: user.user_role === 'admin' ? 'red' : user.user_role === 'tenant' ? 'green' : 'gray'} }>
                {user.user_role}
              </td>
              <td>
                <button onClick={() => handlePermissionClick(user)}>Change Permission</button>
                <button className='delete-button' onClick={() => handleDeleteClick(user)}>Delete</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      {modalType === 'permission' && <PermissionModal user={selectedUser} onClose={handleOnClose} onCancel={()=> {setModalType('');}} setUsers={setUsers} />}
      {modalType === 'delete' && <DeleteModal user={selectedUser} onCancel={()=> {setModalType('');}} onDelete={handleOnDelete} setUsers={setUsers} />}
    </div>
  );
};

export default UserTable;
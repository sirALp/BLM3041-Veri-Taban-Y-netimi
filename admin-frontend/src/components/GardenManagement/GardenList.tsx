import { useState, useEffect } from 'react';
import React from 'react';
import { IGarden } from '../../interfaces/interfaces.ts';

const GardenList = () => {
  const [gardens, setGardens] = useState<IGarden[]>([]);

  useEffect(() => {
    fetch('http://localhost:4000/api/gardens/')
      .then((response) => response.json())
      .then((data) => {
        console.log('Fetched gardens data:', data);
        setGardens(data);
      })
      .catch((error) => console.error('Error fetching gardens data:', error));
  }, []);

  const handleDeleteConfirmation = (gardenId: string) => {
    if (window.confirm('Are you sure you want to delete this garden?')) {
      // Send DELETE request to backend to delete the garden
      fetch(`http://localhost:4000/api/gardens/${gardenId}`, {
        method: 'DELETE',
      })
        .then((response) => {
          if (response.ok) {
            // Remove the deleted garden from the local state
            setGardens(gardens.filter((garden) => garden.garden_id !== gardenId));
            console.log(`Garden with ID ${gardenId} has been deleted.`);
          } else {
            console.error('Failed to delete garden.');
          }
        })
        .catch((error) => console.error('Error deleting garden:', error));
    }
  };

  return (
    <div className="garden-list-container">
      <h2>All Gardens</h2>
      <table className="garden-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Location</th>
            <th>Availability</th>
            <th>Size</th>
            <th>Price</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {gardens.map((garden) => (
            <tr key={garden.garden_id}>
              <td>{garden.garden_id}</td>
              <td>{garden.locations}</td>
              <td style={{textTransform: 'capitalize', color: garden.garden_status === 'occupied' ? 'orange' : 'green'}}>{garden.garden_status}</td>
              <td>{garden.acres}</td>
              <td>{garden.monthly_price}</td>
              <td>
                <button className="delete-button" onClick={() => handleDeleteConfirmation(garden.garden_id)}>Delete</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default GardenList;
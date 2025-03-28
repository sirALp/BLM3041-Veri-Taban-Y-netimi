import React from 'react';

const GardenTable = ({ gardens, setGardens, navigate }) => {
  const handleDeleteClick = (gardenId) => {
    // Logic to delete a garden
    setGardens((prevGardens) => prevGardens.filter((g) => g.id !== gardenId));
  };

  const handleRentalsClick = (gardenId) => {
    // Navigate to the rentals page for the specific garden
    navigate(`/garden/rental/${gardenId}`);
  };

  return (
    <div style={{ width: '80%', margin: '0 auto' }}>
      <table style={{ width: '100%', borderCollapse: 'collapse' }}>
        <thead>
          <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Location</th>
            <th>Size</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {gardens.map((garden) => (
            <tr key={garden.id}>
              <td>{garden.id}</td>
              <td>{garden.name}</td>
              <td>{garden.location}</td>
              <td>{garden.size}</td>
              <td>
                <button onClick={() => handleDeleteClick(garden.id)}>Delete</button>
                <button onClick={() => handleRentalsClick(garden.id)}>Rentals</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default GardenTable;
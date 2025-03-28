// RentalManagement.tsx
import React, { useEffect, useState } from 'react';
import RentalTable from './RentalTable.tsx';
import '../../styles/RentalManagement.css'; // Ensure this CSS file is created
import { IRental } from '../../interfaces/interfaces.ts';


const RentalManagement = () => {
  const [rentals, setRentals] = useState<IRental[]>([]);

  useEffect(() => {
    fetch('http://localhost:4000/api/gardens/rentals/')
      .then((response) => response.json())
      .then((data) => {
        console.log('Fetched rentals data:', data);
        setRentals(data);
      })
      .catch((error) => console.error('Error fetching rentals data:', error));
  }, []);

  return (
    <div className="rental-management-container">
      <h1>Rental Management</h1>
      <RentalTable rentals={rentals} setRentals={setRentals} />
    </div>
  );
};

export default RentalManagement;
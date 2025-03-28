import React from 'react';
import { IRental } from '../../interfaces/interfaces.ts';
import { differenceInMonths, differenceInYears } from 'date-fns';

const RentalTable = ({ rentals, setRentals }) => {
  function calculateRentalPeriod(dateString: string): string {
    const cleaned = dateString.replace(/[\s()]/g, '');
    const [startStr, endStr] = cleaned.split(',');
    
    const startDate = new Date(startStr);
    const endDate = new Date(endStr);

    const monthsDiff = differenceInMonths(endDate, startDate);
    const yearsDiff = differenceInYears(endDate, startDate);
    
    if (yearsDiff >= 1) {
        return yearsDiff === 1 ? '1 year' : `${yearsDiff} years`;
    } else {
        return monthsDiff === 1 ? '1 month' : `${monthsDiff} months`;
    }
}

  const handleDeleteConfirmation = (rentalId :IRental['rental_id']) => {
    if (window.confirm('Are you sure you want to delete this rental?')) {
      fetch(`http://localhost:4000/api/gardens/rentals/${rentalId}`, {
        method: 'DELETE',
      })
        .then((response) => {
          if (response.ok) {
            // Remove the deleted rental from the local state
            setRentals(rentals.filter((rental: IRental) => rental.rental_id !== rentalId));
            console.log(`Rental with ID ${rentalId} has been deleted.`);
          } else {
            console.error('Failed to delete rental.');
          }
        })
        .catch((error) => console.error('Error deleting rental:', error));
    }
  };

  return (
    <div style={{ width: '80%', margin: '0 auto' }}>
      <table style={{ width: '100%', borderCollapse: 'collapse' }}>
        <thead>
          <tr>
            <th>ID</th>
            <th>User ID</th>
            <th>Garden ID</th>
            <th>Period</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {rentals.map((rental:IRental) => (
            <tr key={rental.rental_id}>
              <td>{rental.rental_id}</td>
              <td>{rental.user_id}</td>
              <td>{rental.garden_id}</td>
              <td>{calculateRentalPeriod(rental.rental_date)}</td>
              <td>
                <button className="delete-button" onClick={() => handleDeleteConfirmation(rental.rental_id)}>Delete</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default RentalTable;
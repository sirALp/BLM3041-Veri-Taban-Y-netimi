import React, { useState, useEffect } from 'react';
import { useNavigate, Outlet } from 'react-router-dom';
import '../../styles/GardenManagement.css'; 
import { IGarden,IUser } from '../../interfaces/interfaces.ts';

// Import react-toastify components
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const GardenManagement = () => {
  const [showCreateGardenModal, setShowCreateGardenModal] = useState(false);
  const [showCreateRentalModal, setShowCreateRentalModal] = useState(false);
  
  // create garden form states
  const [acres, setAcres] = useState(0);
  const [location, setLocation] = useState('');
  const [monthlyPrice, setMonthlyPrice] = useState(0);

  // create rental form states
  const [userId, setUserId] = useState('');
  const [gardenId, setGardenId] = useState('');
  const [rentalPeriod, setRentalPeriod] = useState('');

  // State variables to hold users and gardens data
  const [users, setUsers] = useState<IUser[]>([]);
  const [gardens, setGardens] = useState<IGarden[]>([]);

  const navigate = useNavigate();

  const handleCreateGarden = () => {
    setShowCreateGardenModal(true);
  };

  const handleCreateGardenSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
  
    const newGarden = {
      locations: location,
      acres: acres,
      monthly_price: monthlyPrice,
      garden_status: 'vacant'
    };
  
    try {
      const response = await fetch('http://localhost:4000/api/gardens', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(newGarden),
      });
  
      if (response.ok) {
        const createdGarden = await response.json();
        console.log('Garden created:', createdGarden);
        // Optionally, you can reset the form fields
        setAcres(0);
        setLocation('');
        setMonthlyPrice(0);
        setShowCreateGardenModal(false); // Close the modal
        navigate('/garden/all');
      } else {
        // Handle error response
        const errorData = await response.json();
        console.error('Failed to create garden:', errorData);
        toast.error(errorData.error || 'Failed to create garden');
      }
    } catch (error) {
      console.error('Error creating garden:', error);
      toast.error('An error occurred while creating the garden.');
    }
  };

  const handleCreateRental = () => {
    setShowCreateRentalModal(true);
  };

  // Fetch users and gardens when the create rental modal is shown
  useEffect(() => {
    if (showCreateRentalModal) {
      // Fetch users
      fetch('http://localhost:4000/api/users')
        .then(response => {
          if (!response.ok) {
            throw new Error('Failed to fetch users');
          }
          return response.json();
        })
        .then(data => setUsers(data))
        .catch(error => {
          console.error('Error fetching users:', error);
          toast.error('Failed to fetch users.');
        });

      // Fetch gardens
      fetch('http://localhost:4000/api/gardens')
        .then(response => {
          if (!response.ok) {
            throw new Error('Failed to fetch gardens');
          }
          return response.json();
        })
        .then(data => {
          // Filter out gardens with 'occupied' status
          const availableGardens = data.filter((garden:IGarden) => garden.garden_status !== 'occupied');
          setGardens(availableGardens);
        })
        .catch(error => {
          console.error('Error fetching gardens:', error);
          toast.error('Failed to fetch gardens.');
        });
    }
  }, [showCreateRentalModal]);

  const handleCreateRentalSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
  
    // Compute start_date (current date) and end_date based on rentalPeriod
    const startDate = new Date();
    let endDate = new Date(startDate);
  
    switch (rentalPeriod) {
      case '3 months':
        endDate.setMonth(endDate.getMonth() + 3);
        break;
      case '6 months':
        endDate.setMonth(endDate.getMonth() + 6);
        break;
      case '1 year':
        endDate.setFullYear(endDate.getFullYear() + 1);
        break;
      case '2 years':
        endDate.setFullYear(endDate.getFullYear() + 2);
        break;
      case '3 years':
        endDate.setFullYear(endDate.getFullYear() + 3);
        break;
      default:
        console.error('Invalid rental period selected.');
        toast.error('Invalid rental period selected.');
        return;
    }
  
    const rentalData = {
      user_id: userId,
      garden_id: gardenId,
      start_date: startDate.toISOString().split('T')[0], // Format as 'YYYY-MM-DD'
      end_date: endDate.toISOString().split('T')[0],
    };

    console.log('Rental data:', rentalData);
  
    try {
      const response = await fetch('http://localhost:4000/api/gardens/rentals', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(rentalData),
      });
  
      if (response.ok) {
        const createdRental = await response.json();
        console.log('Rental created:', createdRental);
        // Optionally, reset the form fields
        setUserId('');
        setGardenId('');
        setRentalPeriod('');
        setShowCreateRentalModal(false); // Close the modal
        navigate('/garden/rental');
      } else {
        const errorData = await response.json();
        console.error('Failed to create rental:', errorData.error);
        toast.error(errorData.error || 'Failed to create rental.');
      }
    } catch (error) {
      console.error('Error creating rental:', error);
      toast.error('An error occurred while creating the rental.');
    }
  };
  
  const handleListAllGardens = () => {
    navigate('/garden/all');
  };

  const handleListRentals = () => {
    navigate('/garden/rental');
  };

  return (
    <div className="garden-management-container">
      <h1>Garden Management</h1>
      <div className="garden-management-buttons">
        <button onClick={handleCreateGarden}>Create Garden</button>
        <button onClick={handleListAllGardens}>List All Gardens</button>
        <button onClick={handleCreateRental}>Create Rental</button>
        <button onClick={handleListRentals}>List Rentals</button>
      </div>
      <button className='back-button' onClick={() => navigate('/')}>
        Back to Dashboard
      </button>

      {showCreateGardenModal && (
        <div className="modal">
          <h2>Create Garden</h2>
          <form onSubmit={handleCreateGardenSubmit}>
            <div className="form-group">
              <label htmlFor="location">Location</label>
              <input
                id='location'
                type="text"
                placeholder="Istanbul"
                required
                value={location}
                onChange={(e) => setLocation(e.target.value)}
              />
            </div>
            <div className="form-group">
              <label htmlFor="acres">Acres</label>
              <input
                id="acres"
                type="number"
                placeholder="Acres"
                required
                min={1}
                max={1000}
                value={acres}
                onChange={(e) => setAcres(Number(e.target.value))}
              />
            </div>
            <div className='form-group'>
              <label htmlFor="monthlyPrice">Monthly Price</label>
              <input
                id="monthlyPrice"
                type="number"
                placeholder="Monthly Price"
                required
                min={1}
                max={10000}
                value={monthlyPrice}
                onChange={(e) => setMonthlyPrice(Number(e.target.value))}
              />
            </div>
            <button type="submit">Create</button>
          </form>
          <button className='cancel-button' onClick={() => setShowCreateGardenModal(false)}>Close</button>
        </div>
      )}

      {showCreateRentalModal && (
        <div className="modal">
          <h2>Create Rental</h2>
          <form onSubmit={handleCreateRentalSubmit}>
            <div className="form-group" style={{ textWrap: 'nowrap', width: '50%', textAlign: 'right' }}>
              <label htmlFor="userId">User</label>
              <select
                id="userId"
                required
                value={userId}
                onChange={(e) => setUserId(e.target.value)}
                style={{ width: '90%' }}
              >
                <option value="">Select User</option>
                {users.map((user: any) => (
                  <option key={user.user_id} value={user.user_id}>
                    {user.username}
                  </option>
                ))}
              </select>
            </div>
            <div className="form-group" style={{ textWrap: 'nowrap', width: '50%', alignItems: 'right'}}>
              <label htmlFor="gardenId">Garden</label>
              <select
                id="gardenId"
                required
                value={gardenId}
                onChange={(e) => setGardenId(e.target.value)}
                style={{ width: '90%' }}
              >
                <option value="">Select Garden</option>
                {gardens.map((garden: any) => (
                  <option key={garden.garden_id} value={garden.garden_id}>
                    {garden.locations} - {garden.acres} acres
                  </option>
                ))}
              </select>
            </div>
            <div className="form-group" style={{ textWrap: 'nowrap', width: '50%', alignItems: 'right' }}>
              <label htmlFor="rentalPeriod">Rental Period</label>
              <select
                id="rentalPeriod"
                required
                value={rentalPeriod}
                onChange={(e) => setRentalPeriod(e.target.value)}
                style={{ width: '100%' }}
              >
                <option value="">Select Period</option>
                <option value="3 months">3 months</option>
                <option value="6 months">6 months</option>
                <option value="1 year">1 year</option>
                <option value="2 years">2 years</option>
                <option value="3 years">3 years</option>
              </select>
            </div>
            <button type="submit">Create</button>
          </form>
          <button
            className="cancel-button"
            onClick={() => setShowCreateRentalModal(false)}
          >
            Cancel
          </button>
        </div>
      )}

      <Outlet />
      <ToastContainer autoClose={3000} hideProgressBar={false} position='top-right'/>
    </div>
  );
};

export default GardenManagement;
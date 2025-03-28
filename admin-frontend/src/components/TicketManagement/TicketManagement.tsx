import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../../styles/TicketManagement.css';
import { ITicket } from '../../interfaces/interfaces.ts';

const TicketManagement = () => {
  const [tickets, setTickets] = useState<ITicket[]>([]);
  const [pendingTickets, setPendingTickets] = useState<ITicket[]>([]);
  const [expandedTicketId, setExpandedTicketId] = useState<ITicket['ticket_id'] | null>(null);
  const navigate = useNavigate();

  useEffect(() => {
    fetch('http://localhost:4000/api/tickets/')
      .then((response) => response.json())
      .then((ticketData) => {
        const formattedTickets = ticketData.map((ticket) => {
          const e_infos = ticket.equipment.slice(1, -1).split(',');
          return {
            ticket_id: ticket.ticket_id,
            user_id: ticket.user_id,
            equipment: {
              equipment_name: e_infos[0],
              equipment_quantity: parseInt(e_infos[1]),
            },
            ticket_status: ticket.ticket_status,
            ticket_date: ticket.ticket_date,
          };
        });

        // Fetch users after tickets are formatted
        fetch('http://localhost:4000/api/users/')
          .then((response) => response.json())
          .then((userData) => {
            const ticketsWithUserData = formattedTickets.map((ticket) => {
              const user = userData.find(
                (user) => user.user_id === ticket.user_id
              );
              const fullNameArray = user.full_name.slice(1, -1).split(',');
              return {
                ...ticket,
                user_full_name: fullNameArray.join(' '),
                user_name: user.username,
              };
            });

            setTickets(ticketsWithUserData);
          })
          .catch((error) => console.error('Error fetching user data:', error));
      })
      .then(() => {
        fetch('http://localhost:4000/api/tickets/pending')
          .then((response) => response.json())
          .then((ticketData) => {
            const formattedTickets = ticketData.map((ticket) => {
              const e_infos = ticket.equipment.slice(1, -1).split(',');
              return {
                ticket_id: ticket.ticket_id,
                user_id: ticket.user_id,
                equipment: {
                  equipment_name: e_infos[0],
                  equipment_quantity: parseInt(e_infos[1]),
                },
                ticket_status: ticket.ticket_status,
                ticket_date: ticket.ticket_date,
              };
            });

            // Fetch users after tickets are formatted
            fetch('http://localhost:4000/api/users/')
              .then((response) => response.json())
              .then((userData) => {
                const ticketsWithUserData = formattedTickets.map((ticket) => {
                  const user = userData.find(
                    (user) => user.user_id === ticket.user_id
                  );
                  const fullNameArray = user.full_name.slice(1, -1).split(',');
                  return {
                    ...ticket,
                    user_full_name: fullNameArray.join(' '),
                    user_name: user.username,
                  };
                });

                setPendingTickets(ticketsWithUserData);
              })
              .catch((error) => console.error('Error fetching user data:', error));
          });
      })
      .catch((error) => console.error('Error fetching ticket data:', error));
  }, []);

  const toggleDetails = (ticketId: ITicket['ticket_id']) => {
    setExpandedTicketId(expandedTicketId === ticketId ? null : ticketId);
  };

  const handleApprove = (ticketId: ITicket['ticket_id']) => {
    updateTicketStatus(ticketId, 'approved');
  };

  const handleDeny = (ticketId: ITicket['ticket_id']) => {
    updateTicketStatus(ticketId, 'denied');
  };

  const updateTicketStatus = (ticketId: ITicket['ticket_id'], newStatus: 'approved' | 'denied') => {
    fetch(`http://localhost:4000/api/tickets/${ticketId}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ ticket_status: newStatus }),
    })
      .then((response) => {
        if (response.ok) {
          // Update the tickets state
          setTickets((prevTickets:ITicket) =>
            prevTickets.map((ticket:ITicket) =>
              ticket.ticket_id === ticketId ? { ...ticket, ticket_status: newStatus } : ticket
            )
          );

          // Remove the ticket from pendingTickets
          setPendingTickets((prevPendingTickets) =>
            prevPendingTickets.filter((ticket) => ticket.ticket_id !== ticketId)
          );
        } else {
          console.error('Failed to update ticket status.');
        }
      })
      .catch((error) => {
        console.error('Error updating ticket status:', error);
      });
  };

  return (
    <div className="ticket-management-container">
      <h1>Ticket Management</h1>
      <div>
        <button
          className="back-button"
          style={{ marginBottom: '20px' }}
          onClick={() => navigate('/')}
        >
          Back to Main Menu
        </button>
      </div>
      <div className="ticket-tables">
        {/* Pending Tickets */}
        <div className="ticket-table">
          <h2>Pending Tickets</h2>
          <table>
            <thead>
              <tr>
                <th>User</th>
                <th>Equipment Name</th>
                <th>Quantity</th>
                <th>Status</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {pendingTickets
                .filter((ticket) => ticket.ticket_status === 'pending')
                .map((ticket) => (
                  <tr key={ticket.ticket_id}>
                    <td>{ticket.user_full_name}</td>
                    <td>{ticket.equipment.equipment_name}</td>
                    <td>{ticket.equipment.equipment_quantity}</td>
                    <td style={{color: ticket.ticket_status === 'approved' ?  'green' : ticket.ticket_status === 'pending' ? '#e6b842': 'red'}}>
                      {ticket.ticket_status.charAt(0).toUpperCase() +
                        ticket.ticket_status.substring(1)}
                    </td>
                    <td>
                      <div className="button-group">
                        <button
                          className="details-button"
                          onClick={() => toggleDetails(ticket.ticket_id)}
                        >
                          {expandedTicketId === ticket.ticket_id ? 'Hide Details' : 'Show Details'}
                        </button>
                        <button
                          className="approve-button"
                          onClick={() => handleApprove(ticket.ticket_id)}
                        >
                          Approve
                        </button>
                        <button
                          className="deny-button"
                          onClick={() => handleDeny(ticket.ticket_id)}
                        >
                          Deny
                        </button>
                      </div>
                    </td>
                  </tr>
                ))}
            </tbody>
          </table>
        </div>

        {/* Processed Tickets */}
        <div className="ticket-table">
          <h2>Processed Tickets</h2>
          <table>
            <thead>
              <tr>
                <th>User</th>
                <th>Equipment</th>
                <th>Quantity</th>
                <th>Status</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {tickets
                .filter((ticket) => ticket.ticket_status !== 'pending')
                .map((ticket) => (
                  <tr key={ticket.ticket_id}>
                    <td>{ticket.user_full_name}</td>
                    <td>{ticket.equipment.equipment_name}</td>
                    <td>{ticket.equipment.equipment_quantity}</td>
                    <td style={{color: ticket.ticket_status === 'approved' ? 'green' : 'red'}}>
                      {ticket.ticket_status.charAt(0).toUpperCase() +
                        ticket.ticket_status.substring(1)}
                    </td>
                    <td>
                      <button onClick={() => toggleDetails(ticket.ticket_id)}>
                        {expandedTicketId === ticket.ticket_id ? 'Hide Details' : 'Show Details'}
                      </button>
                    </td>
                  </tr>
                ))}
            </tbody>
          </table>
        </div>
      </div>
      {expandedTicketId !== null && (
        <div className="ticket-details">
          <h2>Ticket Details</h2>
          {tickets
            .filter((ticket) => ticket.ticket_id === expandedTicketId)
            .map((ticket) => (
              <div key={ticket.ticket_id}>
                <p>
                  <strong>Ticket ID:</strong> {ticket.ticket_id}
                </p>
                <p>
                  <strong>User ID:</strong> {ticket.user_id}
                </p>
                <p>
                  <strong>Username:</strong> {ticket.user_name}
                </p>
                <p>
                  <strong>First Name:</strong> {ticket.user_full_name.split(' ')[0]}</p>
                <p>
                  <strong>Last Name:</strong> {ticket.user_full_name.split(' ')[1]}</p>
                <p>
                  <strong>Equipment Name:</strong> {ticket.equipment.equipment_name}
                </p>
                <p>
                  <strong>Equipment Quantity:</strong> {ticket.equipment.equipment_quantity}
                </p>
                <p>
                  <strong>Status:</strong> {ticket.ticket_status.charAt(0).toUpperCase() +
                        ticket.ticket_status.substring(1)}
                </p>
              </div>
            ))}
        </div>
      )}
    </div>
  );
};

export default TicketManagement;
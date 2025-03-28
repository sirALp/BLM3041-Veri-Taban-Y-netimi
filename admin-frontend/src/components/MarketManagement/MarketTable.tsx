import React, { useEffect, useState } from 'react';
import { IMarketItem } from '../../interfaces/interfaces.ts';

const MarketTable = ({ items, setItems }) => {
  // Change to store amounts for each item
  const [decAmounts, setDecAmounts] = useState({});

  useEffect(() => {
    console.log(items);
  }, [items]);

  const handleDecrease = (itemId, item) => {
    const decAmount = Number(decAmounts[itemId]);

    if (!decAmount || decAmount <= 0) {
      alert('Please enter a valid quantity to decrease.');
      return;
    }

    if (decAmount > item.product_info.quantity) {
      alert('Cannot decrease by more than the available quantity.');
      return;
    }

    fetch(`http://localhost:4000/api/market/${item.item_id}/decrease`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ dec_amount: decAmount }),
    })
      .then((response) => response.json())
      .then((data) => {
        console.log('Item updated:', data);
        fetch('http://localhost:4000/api/market/')
          .then((response) => response.json())
          .then((data) => {
            const formattedData = data.map((item) => {
              const p_infos = item.product_info.slice(1, -1).split(',');
              return {
                item_id: item.item_id,
                seller_id: item.seller_id,
                product_info: {
                  product_name: p_infos[0],
                  price_per_kg: parseInt(p_infos[1]),
                  quantity: parseInt(p_infos[2]),
                },
              };
            });
            setItems(formattedData);
          })
          .catch((error) => console.error('Error fetching market data:', error));
        // Reset only the specific item's input
        setDecAmounts(prev => ({
          ...prev,
          [itemId]: 0
        }));
      })
      .catch((error) => console.error('Error updating item:', error));
  };

  const handleMax = (itemId, maxQuantity) => {
    setDecAmounts(prev => ({
      ...prev,
      [itemId]: maxQuantity
    }));
  };

  const handleInputChange = (itemId, value) => {
    setDecAmounts(prev => ({
      ...prev,
      [itemId]: value
    }));
  };

  return (
    <div style={{ width: '80%', margin: '0 auto' }}>
      <table style={{ width: '100%', borderCollapse: 'collapse' }}>
        <thead>
          <tr>
            <th>Market ID</th>
            <th>Product Name</th>
            <th>Price per Kilo</th>
            <th>Available Quantity<small>(kg)</small></th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody className='market-table'>
          {items.map((item:IMarketItem) => ( item.is_active ?
            <tr key={item.item_id}>
              <td>{item.item_id}</td>
              <td>{item.product_info.product_name.replace(/"/g, '')}</td>
              <td>{item.product_info.price_per_kg}</td>
              <td>{item.product_info.quantity}</td>
              <td>
                <input
                  type='number'
                  min='0'
                  placeholder='Enter quantity'
                  max={item.product_info.quantity}
                  value={decAmounts[item.item_id] || ''}
                  onChange={(e) => handleInputChange(item.item_id, e.target.value)}
                />
                <button
                  className='delete-button'
                  onClick={() => handleDecrease(item.item_id, item)}
                  disabled={!decAmounts[item.item_id] || decAmounts[item.item_id] <= 0}
                >
                  Decrease
                </button>
                <button onClick={() => handleMax(item.item_id, item.product_info.quantity)}>
                  Max
                </button>
              </td>
            </tr> : null
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default MarketTable;
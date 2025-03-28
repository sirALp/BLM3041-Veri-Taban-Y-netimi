import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import MarketTable from './MarketTable.tsx';
import '../../styles/MarketManagement.css'; // Ensure this CSS file is created
import { IMarketItem } from '../../interfaces/interfaces.ts';

const MarketManagement = () => {
  const [items, setItems] = useState<IMarketItem[]>([]);

  useEffect(() => {
    fetch('http://localhost:4000/api/market/')
      .then((response) => response.json())
      .then((data) => {
        const formattedData = data.map((item) => {
          const p_infos = item.product_info.slice(1, -1).split(',');
          return (
            {
              item_id: item.item_id,
              seller_id: item.seller_id,
              product_info: {
                product_name: p_infos[0],
                price_per_kg: parseInt(p_infos[1]),
                quantity: parseInt(p_infos[2]),
              },
              is_active: item.is_active,
            }
          )
        });
        setItems(formattedData);
      })
      .catch((error) => console.error('Error fetching market data:', error));
  }, []);
  

  const navigate = useNavigate();

  return (
    <div className="market-management-container">
      <h1>Market Management</h1>
      <MarketTable items={items} setItems={setItems} />
      <div className="navigation-buttons">
        <button onClick={() => navigate('/')}>Back to Main Menu</button>
      </div>
    </div>
  );
};

export default MarketManagement;
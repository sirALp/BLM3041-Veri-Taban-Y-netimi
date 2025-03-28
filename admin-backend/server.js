// server.js

const express = require('express');
const { Pool } = require('pg');
const cors = require('cors');

const app = express();
const port = 4000;

app.use(cors());
app.use(express.json());

// PostgreSQL connection configuration
const pool = new Pool({
  user: 'postgres',       
  host: 'localhost',           
  database: 'gardenHubLast',
  password: '1771',   
  port: 5432,
});

// Test database connection
pool.connect((err, client, release) => {
  if (err) {
    return console.error('Error acquiring client', err.stack);
  }
  console.log('Connected to PostgreSQL database');
  release();
});


app.post('/api/login', async (req, res) => {
    const { username, password } = req.body; // password is already hashed
  
    if (!username || !password) {
      return res.status(400).json({ error: 'Username and password are required.' });
    }
  
    try {
      // Call the checkAdminPassword function in the database
      const result = await pool.query('SELECT checkAdminPassword($1, $2) as result', [username, password]);
  
        console.log('Result: ', result.rows[0].result);

      if (result.rows.length > 0 && result.rows[0].result === 'success') {
        // Authentication successful
        res.status(200).json({ message: 'Login successful.' });
        console.log(`Admin ${username} logged in successfully.`);
      } else {
        res.status(401).json({ error: 'Login Denied: ' + result.rows[0].result });
      }
    } catch (err) {
      console.error('Error during login:', err);
      res.status(500).json({ error: 'An error occurred during login.' });
    }
  });  

app.get('/api/users', async (req, res) => {
    try {
        const result = await pool.query('SELECT * FROM users');
        res.json(result.rows);
    } catch (err) {
        console.error('Error executing query', err.stack);
        res.status(500).json({ error: 'Database query failed' });
    }
});


app.put('/api/users/:id/:role', async (req, res) => {
    const userId = req.params.id;
    const role = req.params.role;

    try {
        const result = await pool.query('UPDATE users SET user_role = $1 WHERE user_id = $2', [role, userId]);
        res.json(result.rows);
        console.log("Updated user with id: " + userId + " to role : " + role);
    } catch (err) {
        console.error('Error executing query', err.stack);
        res.status(500).json({ error: 'Database query failed' });
    }
});

app.delete('/api/users/:id', async (req,res) => {
    try {
        const result = await pool.query('DELETE FROM users WHERE user_id = $1', [req.params.id]);
        res.json(result.rows);
        console.log("Deleted user with id: " + req.params.id);
    } catch (err) {
        console.error('Error executing query', err.stack);
        res.status(500).json({ error: 'Database query failed' });
    }
})

app.get('/api/gardens', async (req, res) => {
    try {
        const result = await pool.query('SELECT * FROM gardens');
        res.json(result.rows);
    } catch (err) {
        console.error('Error executing query', err.stack);
        res.status(500).json({ error: 'Database query failed' });
    }
});

app.delete('/api/gardens/:id', async (req, res) => {
    try {
        const result = await pool.query('DELETE FROM gardens WHERE garden_id = $1', [req.params.id]);
        res.json(result.rows);
        console.log("Deleted garden with id: " + req.params.id);
    } catch (err) {
        console.error('Error executing query', err.stack);
        res.status(500).json({ error: 'Database query failed' });
    }
});

app.post('/api/gardens', async (req, res) => {
    try {
        const result = await pool.query('INSERT INTO gardens (acres, locations, monthly_price, garden_status) VALUES ($1, $2, $3, $4)', [req.body.acres, req.body.locations, req.body.monthly_price, req.body.garden_status]);
        res.json(result.rows);
        console.log("Added garden with name: " + req.body.garden_name);
    } catch (err) {
        console.error('Error executing query', err.stack);
        res.status(500).json({ error: 'Database query failed' });
    }
});


app.get('/api/gardens/rentals', async (req, res) => {
    try {
        const result = await pool.query('SELECT * FROM rentals');
        res.json(result.rows);
    } catch (err) {
        console.error('Error executing query', err.stack);
        res.status(500).json({ error: 'Database query failed' });
    }
});

app.delete('/api/gardens/rentals/:id', async (req, res) => {
    try {
        const result = await pool.query('DELETE FROM rentals WHERE rental_id = $1', [req.params.id]);
        res.json(result.rows);
        console.log("Deleted rental with id: " + req.params.id);
    } catch (err) {
        console.error('Error executing query', err.stack);
        res.status(500).json({ error: 'Database query failed' });
    }
});

app.post('/api/gardens/rentals', async (req, res) => {
    try {
        const result = await pool.query('INSERT INTO rentals (user_id, garden_id, rental_date) VALUES ($1, $2, ROW($3,$4)::rental_date_type)', [req.body.user_id, req.body.garden_id, req.body.start_date, req.body.end_date]);
        res.json(result.rows);
        console.log("Added rental with garden_id: " + req.body.garden_id);
    } catch (err) {
        console.error('Error executing query: ', err.stack);
        res.status(409).json({ error: err.message });
    }
});

app.get('/api/market/', async (req, res) => {
    try {
        const result = await pool.query('SELECT * FROM market_items');
        res.json(result.rows);
    } catch (err) {
        console.error('Error executing query', err.stack);
        res.status(500).json({ error: 'Database query failed' });
    }
});

app.put('/api/market/:id/decrease', async (req, res) => {
  const marketId = req.params.id; // marketId is a string
  const { dec_amount } = req.body;

  console.log('Decrease amount:', dec_amount);
  console.log('id : ', marketId);

  if (!dec_amount || dec_amount <= 0) {
    return res.status(400).json({ error: 'Invalid decrease amount' });
  }

  try {
    // Ensure dec_amount does not exceed the available quantity
    const { rows } = await pool.query(
      'SELECT product_info FROM market_items WHERE item_id = $1',
      [marketId]
    );

    if (rows.length === 0) {
      return res.status(404).json({ error: 'Item not found' });
    }

    const productInfo = rows[0].product_info;
    const currentQuantity = productInfo.slice(1,-1).split(",")[2];

    
    if (dec_amount > currentQuantity) {
      return res.status(400).json({ error: 'Decrease amount exceeds available quantity' });
    }

    // Update the quantity
    const newQuantity = currentQuantity - dec_amount;

    if ( newQuantity >= 0 ) {
      await pool.query(
        'UPDATE market_items SET product_info = ROW((product_info).product_name,(product_info).price_per_kg,$1) WHERE item_id = $2',
        [newQuantity, marketId]
      );
    }

    res.json({ message: 'Item quantity decreased successfully', newQuantity });
  } catch (err) {
    console.error('Error updating item quantity', err);
    res.status(500).json({ error: 'Database query failed' });
  }
});

app.get('/api/tickets', async (req, res) => {
    try {
        const result = await pool.query('SELECT * FROM tickets');
        res.json(result.rows);
    } catch (err) {
        console.error('Error executing query', err.stack);
        res.status(500).json({ error: 'Database query failed' });
    }
});

app.get('/api/tickets/pending', async (req, res) => {
    try {
        const result = await pool.query(`SELECT * FROM tickets EXCEPT SELECT * FROM tickets WHERE ticket_status = 'approved' OR ticket_status = 'denied'`);
        res.json(result.rows);
    } catch (err) {
        console.error('Error executing query', err.stack);
        res.status(500).json({ error: 'Database query failed' });
    }
});

app.put('/api/tickets/:id', async (req, res) => {
    const ticketId = req.params.id;
    const { ticket_status } = req.body;

    try {
        const result = await pool.query('UPDATE tickets SET ticket_status = $1 WHERE ticket_id = $2', [ticket_status, ticketId]);
        res.json(result.rows);
        console.log("Updated ticket with id: " + ticketId);
    } catch (err) {
        console.error('Error executing query', err.stack);
        res.status(500).json({ error: 'Database query failed' });
    }
});

app.listen(port, () => {
  console.log(`Backend server is running on http://localhost:${port}`);
});
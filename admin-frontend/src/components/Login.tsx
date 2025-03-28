import React, { useState } from 'react';
import Cookies from 'js-cookie';
import { toast } from 'react-toastify';
import { keccak256 } from 'js-sha3';

const Login = ({ setLoggedIn }: { setLoggedIn: (value: boolean) => void }) => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [isLoading, setIsLoading] = useState(false);

  const handleLogin = async () => {
    // Input validation
    if (!username || !password) {
      toast.error('Please enter both username and password.');
      return;
    }

    try {
      setIsLoading(true);

      // Hash the password using Keccak256
      const hashedPassword = keccak256(password);

      // Prepare the login data
      const loginData = {
        username,
        password: hashedPassword,
      };

      // Send a POST request to the backend API
      const response = await fetch('http://localhost:4000/api/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(loginData),
      });

      const result = await response.json();

      if (response.ok) {
        // Successful login
        Cookies.set('loggedIn', 'true', { expires: 1 });
        setLoggedIn(true);
        toast.success('Login successful!');
      } else {
        // Login failed
        toast.error(result.error || 'Login failed! Please check your credentials.');
      }
    } catch (error) {
      console.error('Error during login:', error);
      toast.error('An error occurred during login. Please try again later.');
    } finally {
      setIsLoading(false);
    }
  };

  const handleKeyPress = (event: React.KeyboardEvent<HTMLInputElement>) => {
    if (event.key === 'Enter') {
      handleLogin();
    }
  };

  return (
    <div className="login-page">
      <h1>GardenHub Admin Login</h1>
      <input
        type="text"
        placeholder="Username"
        value={username}
        onChange={(e) => setUsername(e.target.value)}
        onKeyDown={handleKeyPress}
        autoFocus
      />
      <input
        type="password"
        placeholder="Password"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
        onKeyDown={handleKeyPress}
      />
      <button onClick={handleLogin} disabled={isLoading}>
        {isLoading ? 'Logging in...' : 'Log In'}
      </button>
    </div>
  );
};

export default Login;
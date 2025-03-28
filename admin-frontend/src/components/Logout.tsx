import React, { useEffect } from 'react';
import Cookies from 'js-cookie';
import { Navigate } from 'react-router-dom';

const Logout = ({ setLoggedIn }: { setLoggedIn: (value: boolean) => void }) => {
  useEffect(() => {
    Cookies.remove('loggedIn');
    setLoggedIn(false);
  }, [setLoggedIn]);

  return <Navigate to="/login" />;
};

export default Logout;

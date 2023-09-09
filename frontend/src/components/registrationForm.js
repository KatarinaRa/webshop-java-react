import React, { useState } from 'react';
import axios from 'axios';

const RegistrationForm = () => {
  const [firstName, setfirstName] = useState('');
  const [lastName, setLastName] = useState('');
  const [address, setAddress] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [errorMessage, setErrorMessage] = useState('');

  const handleFormSubmit = async (e) => {
    e.preventDefault();

    const user = {
      firstName: firstName,
      lastName: lastName,
      address: address,
      email: email,
      password: password
    };

    try {
      const response = await axios.post('http://localhost:8080/users/register', user);
      console.log(response.data);
      setfirstName('');
      setLastName('');
      setAddress('');
      setEmail('');
      setPassword('');
      window.location.href = '/login';
    } catch (error) {
      if (error.response && error.response.data) {
        setErrorMessage(error.response.data);
      } else {
        setErrorMessage('An error occurred while registering. Please try again.');
      }
    }
  };

  return (
    <form
      style={{
        maxWidth: '400px',
        margin: '0 auto',
        padding: '20px',
        border: '1px solid #ccc',
        borderRadius: '4px',
        boxShadow: '0 0 5px rgba(0, 0, 0, 0.1)',
        backgroundColor: '#fff',
      }}
      onSubmit={handleFormSubmit}
    >
      {errorMessage && <p>{errorMessage}</p>}
      <label style={{ display: 'block', marginBottom: '10px' }}>
        Name:
        <input type="text" value={name} onChange={(e) => setName(e.target.value)} required />
      </label>
      <br />
      <label style={{ display: 'block', marginBottom: '10px' }}>
        Last Name:
        <input type="text" value={lastName} onChange={(e) => setLastName(e.target.value)} required />
      </label>
      <br />
      <label style={{ display: 'block', marginBottom: '10px' }}>
        Address:
        <input type="text" value={address} onChange={(e) => setAddress(e.target.value)} required />
      </label>
      <br />
      <label style={{ display: 'block', marginBottom: '10px' }}>
        Email:
        <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} required />
      </label>
      <br />
      <label style={{ display: 'block', marginBottom: '10px' }}>
        Password:
        <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} required />
      </label>
      <br />
      <button
        style={{
          backgroundColor: '#4CAF50',
          color: '#fff',
          padding: '10px 15px',
          border: 'none',
          borderRadius: '4px',
          cursor: 'pointer',
        }}
        type="submit"
      >
        Register
      </button>
    </form>
  );
};

export default RegistrationForm;
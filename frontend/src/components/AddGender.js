import React, { useState, useEffect } from 'react';
import axios from 'axios';
import '../AddProduct.css'; 

function AddGender() {
  const [genders, setGenders] = useState([]);
  const [name, setName] = useState('');
  const [editingGenderId, setEditingGenderId] = useState(null);

  useEffect(() => {
    fetchGenders();
  }, []);

  const fetchGenders = async () => {
    try {
      const response = await axios.get('http://localhost:8080/genders');
      setGenders(response.data);
    } catch (error) {
      console.error(error);
      alert('Došlo je do pogreške prilikom dohvaćanja spolova.');
    }
  };

  const handleFormSubmit = async (e) => {
    e.preventDefault();

    const genderData = {
      name,
    };

    try {
      if (editingGenderId) {
        await axios.put(`http://localhost:8080/genders/${editingGenderId}`, genderData);
        alert('Spol je uspješno ažuriran.');
      } else {
        await axios.post('http://localhost:8080/genders', genderData);
        alert('Spol je uspješno dodan.');
      }

     
      setName('');
      setEditingGenderId(null);

      fetchGenders();
    } catch (error) {
      console.error(error);
      alert('Došlo je do pogreške prilikom dodavanja/uređivanja spola.');
    }
  };

  const handleEditGender = (gender) => {
    setName(gender.name);
    setEditingGenderId(gender.id);
  };

  const handleDeleteGender = async (genderId) => {
    try {
      await axios.delete(`http://localhost:8080/genders/${genderId}`);
      alert('Spol je uspješno obrisan.');

   
      fetchGenders();
    } catch (error) {
      console.error(error);
      alert('Došlo je do pogreške prilikom brisanja spola.');
    }
  };

  return (
    <div className="form-container">
      <h2>Add Gender</h2>
      <form onSubmit={handleFormSubmit}>
        <div>
          <label>Naziv:</label>
          <input type="text" value={name} onChange={(e) => setName(e.target.value)} />
        </div>
        <button type="submit">Dodaj</button>
      </form>

      <h2>Genders</h2>
      <table>
        <thead>
          <tr>
            <th>Naziv</th>
            <th>Uredi</th>
            <th>Obriši</th>
          </tr>
        </thead>
        <tbody>
          {genders.map((gender) => (
            <tr key={gender.id}>
              <td>{gender.name}</td>
              <td>
                <button onClick={() => handleEditGender(gender)}>Uredi</button>
              </td>
              <td>
                <button onClick={() => handleDeleteGender(gender.id)}>Obriši</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default AddGender;
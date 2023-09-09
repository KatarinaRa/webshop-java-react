import React, { useState, useEffect } from 'react';
import axios from 'axios';
import '../AddProduct.css';
import authHeader from '../services/auth-header';
import 'bootstrap/dist/css/bootstrap.min.css'; 

function AddColor() {
  const [colors, setColors] = useState([]);
  const [name, setName] = useState('');
  const [editingColorId, setEditingColorId] = useState(null);

  useEffect(() => {
    fetchColors();
  }, []);

  const fetchColors = async () => {
    try {
      const response = await axios.get('http://localhost:8080/colors', {
        headers: authHeader(),
      });
      setColors(response.data);
    } catch (error) {
      console.error(error);
      alert('Došlo je do pogreške prilikom dohvaćanja boja.');
    }
  };

  const handleFormSubmit = async (e) => {
    e.preventDefault();

    const colorData = {
      name,
    };

    try {
      if (editingColorId) {
        await axios.put(`http://localhost:8080/colors/${editingColorId}`, colorData, {
          headers: authHeader(),
        });
        alert('Boja je uspješno ažurirana.');
      } else {
        await axios.post('http://localhost:8080/colors', colorData, {
          headers: authHeader(),
        });
        alert('Boja je uspješno dodana.');
      }

 
      setName('');
      setEditingColorId(null);

     
      fetchColors();
    } catch (error) {
      console.error(error);
      alert('Došlo je do pogreške prilikom dodavanja/uređivanja boje.');
    }
  };

  const handleEditColor = (color) => {
    setName(color.name);
    setEditingColorId(color.id);
  };

  const handleDeleteColor = async (colorId) => {
    try {
      await axios.delete(`http://localhost:8080/colors/${colorId}`, {
        headers: authHeader(),
      });
      alert('Boja je uspješno obrisana.');


      fetchColors();
    } catch (error) {
      console.error(error);
      alert('Došlo je do pogreške prilikom brisanja boje.');
    }
  };

  return (
    <div className="form-container">
      <h2>Add Color</h2>
      <form onSubmit={handleFormSubmit}>
        <div>
          <label>Naziv:</label>
          <input type="text" value={name} onChange={(e) => setName(e.target.value)} />
        </div>
        <button type="submit" className="btn btn-primary">Dodaj</button>
      </form>

      <h2>Colors</h2>
      <table className="table">
        <thead>
          <tr>
            <th>Naziv</th>
            <th>Uredi</th>
            <th>Obriši</th>
          </tr>
        </thead>
        <tbody>
          {colors.map((color) => (
            <tr key={color.id}>
              <td>{color.name}</td>
              <td>
                <button onClick={() => handleEditColor(color)} className="btn btn-primary">Edit</button>
              </td>
              <td>
                <button onClick={() => handleDeleteColor(color.id)} className="btn btn-danger">Delete</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default AddColor;
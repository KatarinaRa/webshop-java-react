import React, { useState, useEffect } from 'react';
import axios from 'axios';
import '../AddProduct.css';
import authHeader from '../services/auth-header';
import 'bootstrap/dist/css/bootstrap.min.css'; 

function AddSize() {
  const [sizes, setSizes] = useState([]);
  const [name, setName] = useState('');
  const [editingSizeId, setEditingSizeId] = useState(null);

  useEffect(() => {
    fetchSizes();
  }, []);

  const fetchSizes = async () => {
    try {
      const response = await axios.get('http://localhost:8080/sizes', {
        headers: authHeader(),
      });
      setSizes(response.data);
    } catch (error) {
      console.error(error);
      alert('An error occurred while fetching sizes.');
    }
  };

  const handleFormSubmit = async (e) => {
    e.preventDefault();

    const sizeData = {
      name,
    };

    try {
      if (editingSizeId) {
        await axios.put('http://localhost:8080/sizes/${editingSizeId}', sizeData, {
          headers: authHeader(),
        });
        alert('Size successfully updated.');
      } else {
        await axios.post('http://localhost:8080/sizes', sizeData, {
          headers: authHeader(),
        });
        alert('Size successfully added.');
      }

  
      setName('');
      setEditingSizeId(null);

   
      fetchSizes();
    } catch (error) {
      console.error(error);
      alert('An error occurred while adding/updating the size.');
    }
  };

  const handleEditSize = (size) => {
    setName(size.name);
    setEditingSizeId(size.id);
  };

  const handleDeleteSize = async (sizeId) => {
    try {
      await axios.delete('http://localhost:8080/sizes/${sizeId}', {
        headers: authHeader(),
      });
      alert('Size successfully deleted.');

      fetchSizes();
    } catch (error) {
      console.error(error);
      alert('An error occurred while deleting the size.');
    }
  };

  return (
    <div className="form-container">
      <h1>Add Size</h1>
      <form onSubmit={handleFormSubmit}>
        <div>
          <label>Name:</label>
          <input type="text" value={name} onChange={(e) => setName(e.target.value)} />
        </div>
        <button type="submit" className="btn btn-primary">Add Size</button>
      </form>

      <h2>Sizes</h2>
      <table className="table">
        <thead>
          <tr>
            <th>Name</th>
            <th>Edit</th>
            <th>Delete</th>
          </tr>
        </thead>
        <tbody>
          {sizes.map((size) => (
            <tr key={size.id}>
              <td>{size.name}</td>
              <td>
                <button onClick={() => handleEditSize(size)} className="btn btn-primary">Edit</button>
              </td>
              <td>
                <button onClick={() => handleDeleteSize(size.id)} className="btn btn-danger">Delete</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default AddSize;
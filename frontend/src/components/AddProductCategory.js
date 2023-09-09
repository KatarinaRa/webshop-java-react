import React, { useState, useEffect } from 'react';
import axios from 'axios';
import '../AddProduct.css';
import authHeader from '../services/auth-header';
import 'bootstrap/dist/css/bootstrap.min.css'; 

function AddProductCategory() {
  const [productCategories, setProductCategories] = useState([]);
  const [name, setName] = useState('');
  const [editingCategoryId, setEditingCategoryId] = useState(null);

  useEffect(() => {
    fetchProductCategories();
  }, []);

  const fetchProductCategories = async () => {
    try {
      const response = await axios.get('http://localhost:8080/productscategory', {
        headers: authHeader(),
      });
      setProductCategories(response.data);
    } catch (error) {
      console.error(error);
      alert('An error occurred while fetching product categories.');
    }
  };

  const handleFormSubmit = async (e) => {
    e.preventDefault();

    const productCategoryData = {
      name,
    };

    try {
      if (editingCategoryId) {
        await axios.put('http://localhost:8080/productscategory/${editingCategoryId}', productCategoryData, {
          headers: authHeader(),
        });
        alert('Product category successfully updated.');
      } else {
        await axios.post('http://localhost:8080/productscategory', productCategoryData, {
          headers: authHeader(),
        });
        alert('Product category successfully added.');
      }

      setName('');
      setEditingCategoryId(null);

 
      fetchProductCategories();
    } catch (error) {
      console.error(error);
      alert('An error occurred while adding/updating the product category.');
    }
  };

  const handleEditProductCategory = (category) => {
    setName(category.name);
    setEditingCategoryId(category.id);
  };

  const handleDeleteProductCategory = async (categoryId) => {
    try {
      await axios.delete('http://localhost:8080/productscategory/${categoryId}', {
        headers: authHeader(),
      });
      alert('Product category successfully deleted.');

      fetchProductCategories();
    } catch (error) {
      console.error(error);
      alert('An error occurred while deleting the product category.');
    }
  };

  return (
    <div className="form-container">
      <h1>Add Product Category</h1>
      <form onSubmit={handleFormSubmit}>
        <div>
          <label>Name:</label>
          <input type="text" value={name} onChange={(e) => setName(e.target.value)} />
        </div>
        <button type="btn btn-primary">Add Product Category</button>
      </form>

      <h2>Product Categories</h2>
      <table className='table'>
        <thead>
          <tr>
            <th>Name</th>
            <th>Edit</th>
            <th>Delete</th>
          </tr>
        </thead>
        <tbody>
          {productCategories.map((category) => (
            <tr key={category.id}>
              <td>{category.name}</td>
              <td>
                <button onClick={() => handleEditProductCategory(category)} className="btn btn-primary">Edit</button>
              </td>
              <td>
                <button onClick={() => handleDeleteProductCategory(category.id)} className="btn btn-danger">Delete</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default AddProductCategory;
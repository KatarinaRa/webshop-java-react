import React, { useState, useEffect } from 'react';
import axios from 'axios';
import '../AddProduct.css';
import authHeader from '../services/auth-header';

function AddProduct() {
  const [name, setName] = useState('');
  const [image, setImage] = useState(null);
  const [description, setDescription] = useState('');
  const [sizeOptions, setSizeOptions] = useState([]);
  const [selectedSize, setSelectedSize] = useState('');
  const [price, setPrice] = useState(0);
  const [colorOptions, setColorOptions] = useState([]);
  const [selectedColor, setSelectedColor] = useState('');
  const [genderOptions, setGenderOptions] = useState([]);
  const [selectedGender, setSelectedGender] = useState('');
  const [categoryOptions, setCategoryOptions] = useState([]);
  const [selectedCategory, setSelectedCategory] = useState('');
  const [sizeQuantities, setSizeQuantities] = useState({});


  useEffect(() => {
    axios.get('http://localhost:8080/sizes', { headers: authHeader() })
      .then(response => {
        setSizeOptions(response.data);
      })
      .catch(error => {
        console.error(error);
      });

    axios.get('http://localhost:8080/colors', { headers: authHeader() })
      .then(response => {
        setColorOptions(response.data);
      })
      .catch(error => {
        console.error(error);
      });

    axios.get('http://localhost:8080/genders', { headers: authHeader() })
      .then(response => {
        setGenderOptions(response.data);
      })
      .catch(error => {
        console.error(error);
      });

    axios.get('http://localhost:8080/productscategory', { headers: authHeader() })
      .then(response => {
        setCategoryOptions(response.data);
      })
      .catch(error => {
        console.error(error);
      });
  }, []);

  const handleFormSubmit = async (e) => {
    e.preventDefault();

    const formData = new FormData();
    formData.append('image', image);
    formData.append('name', name);
    formData.append('description', description);
    formData.append('price', price);
    formData.append('color', selectedColor); 
    formData.append('size', selectedSize); 
    formData.append('gender', selectedGender); 
    formData.append('category', selectedCategory);
    formData.append('sizeQuantities', JSON.stringify(sizeQuantities));

    try {
      await axios.post('http://localhost:8080/products', formData, {
        headers: {
          ...authHeader(),
          'Content-Type': 'multipart/form-data'
        },
      });

      alert('Proizvod uspješno dodan.');

      setName('');
      setImage(null);
      setDescription('');
      setSelectedSize('');
      setPrice(0);
      setSelectedColor('');
      setSelectedGender('');
      setSelectedCategory('');

    } catch (error) {
      console.error(error);
      alert('Došlo je do pogreške prilikom dodavanja proizvoda.');
    }
  };

  return (
    <div className="form-container">
      <h2>Dodaj proizvod</h2>
      <form onSubmit={handleFormSubmit} encType="multipart/form-data">
        <div>
          <label>Naziv:</label>
          <input type="text" value={name} onChange={(e) => setName(e.target.value)} />
        </div>
        <div>
          <label>Slika:</label>
          <input type="file" onChange={(e) => setImage(e.target.files[0])} />
        </div>
        <div>
          <label>Opis:</label>
          <textarea value={description} onChange={(e) => setDescription(e.target.value)} />
        </div>
        <div>
          <label>Veličina i količina:</label>
          {sizeOptions.map((size) => (
            <div key={size.id}>
              <span>{size.name}</span>
              <input 
                type="number" 
                placeholder={`Količina za ${size.name}`} 
                onChange={(e) => setSizeQuantities({ 
                  ...sizeQuantities, 
                  [size.id]: parseInt(e.target.value, 10) 
                })}
              />
            </div>
          ))}
        </div>
        <div>
          <label>Cijena:</label>
          <input type="number" value={price} onChange={(e) => setPrice(parseFloat(e.target.value))} />
        </div>
        <div>
          <label>Boja:</label>
          <select value={selectedColor} onChange={(e) => setSelectedColor(e.target.value)}>
            <option value="">Odaberi boju</option>
            {colorOptions.map(color => (
              <option key={color.id} value={color.id}>{color.name}</option>
            ))}
          </select>
        </div>
        <div>
          <label>Spol:</label>
          <select value={selectedGender} onChange={(e) => setSelectedGender(e.target.value)}>
            <option value="">Odaberi spol</option>
            {genderOptions.map(gender => (
              <option key={gender.id} value={gender.id}>{gender.name}</option>
            ))}
          </select>
        </div>
        <div>
          <label>Kategorija:</label>
          <select value={selectedCategory} onChange={(e) => setSelectedCategory(e.target.value)}>
            <option value="">Odaberi kategoriju</option>
            {categoryOptions.map(category => (
              <option key={category.id} value={category.id}>{category.name}</option>
            ))}
          </select>
        </div>
        <div>
          {image && <img src={'http://localhost:8080/products/images/${image.name}'} alt="Slika proizvoda" />}
        </div>
        <button type="submit">Dodaj</button>
      </form>
    </div>
  );
  
}

export default AddProduct;

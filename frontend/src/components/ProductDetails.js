import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useParams, Link } from 'react-router-dom';
import authHeader from '../services/auth-header';
import './ProductDetails.css';
import { useNavigate } from 'react-router-dom';

function ProductDetails() {
  const { id } = useParams();
  const [product, setProduct] = useState(null);
  const [sizes, setSizes] = useState([]);
  const [selectedSize, setSelectedSize] = useState(null);
  const [isAdmin, setIsAdmin] = useState(false);
  const [isLoading, setIsLoading] = useState(true);
  const [orderId, setOrderId] = useState(null);


  useEffect(() => {
    const user = JSON.parse(localStorage.getItem('user'));
    if (user && user.roles && user.roles.includes('ROLE_ADMIN')) {
      setIsAdmin(true);
    }
  }, []);



  useEffect(() => {
    const fetchProduct = async () => {
      setIsLoading(true);
      try {
        const productResponse = await axios.get(`http://localhost:8080/products/${id}`, {
          headers: authHeader(),
        });
        setProduct(productResponse.data);
        
        const sizesResponse = await axios.get(`http://localhost:8080/products/${id}/sizes`, {
          headers: authHeader(),
        });
  
        if (sizesResponse.data.length === 0) {
          console.log("Sizes data is empty.");
        }
  
        setSizes(sizesResponse.data);
        console.log("Sizes Response:", sizesResponse);
      } catch (error) {
        console.error('There was an error fetching the product details:', error);
      }
      setIsLoading(false);
    };
  
    fetchProduct();
  }, [id]);
  

  const createOrder = async () => {
    const response = await axios.post('http://localhost:8080/orders', {}, { headers: authHeader() });
    setOrderId(response.data.id);
    return response.data.id;
  };
 
  const addToCart = async (productId, price, quantity = 1,selectedSize) => {
    console.log("prije" + orderId)
    let currentOrderId = orderId;
    if (!currentOrderId) {
      console.log("poslije" + orderId)
      currentOrderId = await createOrder();  
    }
    const data = {
      quantity: quantity,
      size: selectedSize,
      price: price,
    };
    await axios.post(`http://localhost:8080/orderDetails?orderId=${currentOrderId}&productId=${productId}&sizeId=${selectedSize}`, data, {
      headers: authHeader(),
    }).then(response => {
      alert('Product added to cart.');
    }).catch(error => {
      if (error.response && error.response.status === 400) {
        alert('Not enough items in stock.');
      } else {
        alert('An unexpected error occurred.');
      }
    });
  };
  

  if (isLoading) {
    return <div>Loading...</div>;
  }

  if (!product) {
    return <div>Product not found</div>;
  }

  return (
    <div className="product-details">
      <div className="product-info">
        <h1>{product.name}</h1>
        <p>{product.description}</p>
        <p><b>Color:</b> {product.color?.name}</p>
        <p><b>Gender:</b> {product.gender?.name}</p>
        <p><b>Price: </b>{product.price}€</p>
        <select onChange={(e) => setSelectedSize(e.target.value)}>
          <option value="">Select Size</option>
          {sizes.map((size, index) => (
            <option key={index} value={size.id}>{size.name}</option>
          ))}
        </select>

        <button 
          className="add-to-cart-button" 
          onClick={() => addToCart(product.id, product.price,1,selectedSize)}
          disabled={!selectedSize}
        >
          Add to Cart
        </button>
      </div>
      <div className="product-image">
        <img 
          src={`http://localhost:8080/products/images/${product.imageURL}`} 
          alt={product.name}
        />
      </div>
      
    </div>
  );
}

export default ProductDetails;
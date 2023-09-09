import React from 'react';
import './Home.css';

function Home() {
  const userRole = 'user';

  const renderMenuItems = () => {
    if (userRole === 'user') {

      return (
        <>
          <li><a href="product-list">Products</a></li>
          <li><a href="cart">Cart</a></li>
          <li><a href="logout" onClick={logoutUser}>Odjava</a></li>
        </>
      );
    } else {
  
      return (
        <>
          <li><a href="#">Home</a></li>
          <li><a href="login">Login</a></li>
          <li><a href="register">Register</a></li>
          <li><a href="add-product">Add Products</a></li>
          <li><a href="add-productcategory">Add Product Category</a></li>
          <li><a href="add-size">Add Size</a></li>
          <li><a href="add-color">Add Color</a></li>
          <li><a href="add-gender">Add Gender</a></li>
          <li><a href="product-list">Products</a></li>
          <li><a href="cart">Cart</a></li>
          <li><a href="logout" onClick={logoutUser}>Odjava</a></li>
        </>
      );
    }
  };

  const logoutUser = () => {
  };

  return (
    <div className="home-container">
      <nav>
        <h1 className="home-title">Vogue</h1>
        <ul>
          {renderMenuItems()}
        </ul>
      </nav>
    </div>
  );
}

export default Home;
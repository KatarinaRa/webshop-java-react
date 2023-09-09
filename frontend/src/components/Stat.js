import React, { useState, useEffect } from 'react';
import axios from 'axios';
import authHeader from '../services/auth-header';
import '../AllOrders.css';

function Stat() {
    const [topSpenders, setTopSpenders] = useState([]);
    const [topBuyers, setTopBuyers] = useState([]);
    const [purchased, setPurchased] = useState([]);

    useEffect(() => {
        fetchTopSpenders();
        fetchTopBuyers();
        fetchPurchased();
    }, []);


    const fetchTopSpenders = async () => {
        try {
            const response = await axios.get('http://localhost:8080/orderDetails/top-spending-users', {
                headers: authHeader(),
            });
            setTopSpenders(response.data);
        } catch (error) {
            console.error('Error fetching top spenders:', error);
        }
    };

    const fetchPurchased= async () => {
        try {
            const response = await axios.get('http://localhost:8080/orderDetails/products-by-day', {
                headers: authHeader(),
            });
            setPurchased(response.data);
        } catch (error) {
            console.error('Error fetching purchased:', error);
        }
    };

    const fetchTopBuyers = async () => {
        try {
            const response = await axios.get('http://localhost:8080/orderDetails/top-selling-products', {
                headers: authHeader(),
            });
            setTopBuyers(response.data);
            console.log('Top Buyers:', response.data);
            
        } catch (error) {
            console.error('Error fetching top buyers:', error);
        }
    };


    return (
        <div className="orders-container">
            

            <h2>Top Spenders</h2>
    <table className="users-table">
    <thead>
             <tr>
                    <th>Username</th>
                    <th>Email</th>
                   
                </tr>
            </thead>
            <tbody>
            {topSpenders.map((user) => (
                    <tr key={user.id}>
                    <td>{user.username}</td>
                    <td>{user.email}</td>
                
                </tr>
            ))}
            </tbody>
            </table>

            <h2>Best selling products</h2>
            <table className="users-table">
                <thead>
                    <tr>
                        <th>Products</th>
                        <th>Images</th>
                    </tr>
                </thead>
                <tbody>
                 {topBuyers.map((product) => (
                    <tr key={product.id}>
                    <td>{product.name}</td>
                    <td>{<img src={`http://localhost:8080/products/images/${product.imageURL}`} alt={product.name} width="100" />}</td>
          
                    </tr>
                ))}
                </tbody>
                </table>

            <h2>Purchased today</h2>
            <table className="users-table">
                <thead>
                    <tr>
                        <th>Products</th>
                        <th>Images</th>
                    </tr>
                </thead>
                <tbody>
                 {purchased.map((product) => (
                    <tr key={product.id}>
                    <td>{product.name}</td>
                    <td>{<img src={`http://localhost:8080/products/images/${product.imageURL}`} alt={product.name} width="100" />}</td>
          
                    </tr>
                ))}
                </tbody>
                </table>

        
            
        </div>
    );
}

export default Stat;

import React, { useState, useEffect } from 'react';
import axios from 'axios';
import authHeader from '../services/auth-header';
import '../AllOrders.css';

function OrderDetailsPage() {
    const [allOrders, setAllOrders] = useState([]);
    const [orderDetails, setOrderDetails] = useState([]);
    const [totalPrice, setTotalPrice] = useState(0);

    

    

    const fetchOrderDetails = async (orderId) => {
        try {
            const response = await axios.get(`http://localhost:8080/orderDetails/${orderId}`, {
                headers: authHeader(),
            });
            setOrderDetails(response.data);
            const total = response.data.reduce((acc, orderDetail) => acc + (orderDetail.quantity * orderDetail.product.price), 0);
            setTotalPrice(total);
        } catch (error) {
            console.error('Error fetching order details:', error);
        }
    };

    const handleOrderIdClick = (orderId) => {
        fetchOrderDetails(orderId);
    };

    return (
        <div className="orders-container">
            <h2>My Orders</h2>
            <ul>
                {allOrders.map((order, index) => (
                    <li key={index}>
                        <a href="#" onClick={() => handleOrderIdClick(order.id)}>Order ID: {order.id}</a>
                    </li>
                ))}
            </ul>
            {orderDetails.length > 0 && (
                <div>
                    <h3>Order Details</h3>
                    <table className="orders-table">
                        <thead>
                            <tr>
                                <th>Product Image</th>
                                <th>Product Name</th>
                                <th>Quantity</th>
                                <th>Price</th>
                                <th>Date of Order</th>
                            </tr>
                        </thead>
                        <tbody>
                            {orderDetails.map((orderDetail) => (
                                <tr key={orderDetail.id}>
                                    <td><img src={`http://localhost:8080/products/images/${orderDetail.product.imageURL}`} alt={orderDetail.product.name} width="100" /></td>
                                    <td>{orderDetail.product.name}</td>
                                    <td>{orderDetail.quantity}</td>
                                    <td>{orderDetail.product.price} €</td>
                                    <td>{new Date(orderDetail.orderTime).toLocaleString()}</td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                    <strong><p>Total Price: {totalPrice} €</p></strong>
                </div>
            )}
        </div>
    );
}

export default OrderDetailsPage;

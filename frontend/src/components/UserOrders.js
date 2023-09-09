import React, { useState, useEffect } from 'react';
import axios from 'axios';
import authHeader from '../services/auth-header';
import AuthService from '../services/auth.service';
import { Link } from 'react-router-dom';
import '../AllOrders.css';

function MyOrders() {
    const [orders, setOrders] = useState([]);
    const [groupedOrders, setGroupedOrders] = useState({});
    const [selectedOrderId, setSelectedOrderId] = useState(null);

    useEffect(() => {
        fetchMyOrders();
    }, []);

    const fetchMyOrders = async () => {
        const user = AuthService.getCurrentUser();
        if (user) {
            try {
                const response = await axios.get(`http://localhost:8080/orderDetails/details/user/${user.id}`, {
                    headers: authHeader(),
                });
                setOrders(response.data);
                
                let tempGroupedOrders = {};
                response.data.forEach(orderDetail => {
                    const orderId = orderDetail.order.id;
                    if (!tempGroupedOrders[orderId]) {
                        tempGroupedOrders[orderId] = [];
                    }
                    tempGroupedOrders[orderId].push(orderDetail);
                });
                setGroupedOrders(tempGroupedOrders);
                
            } catch (error) {
                console.error('Error fetching my orders:', error);
            }
        }
    };

    const handleOrderClick = (orderId) => {
        setSelectedOrderId(orderId === selectedOrderId ? null : orderId);
    };

    return (
        <div className="orders-container">
            <h2>My Orders</h2>
            {Object.keys(groupedOrders).length === 0 ? (
                <p>You haven't placed any orders yet.</p>
            ) : (
                Object.keys(groupedOrders).map((orderId) => (
                    <div key={orderId}>
                        <h3 onClick={() => handleOrderClick(orderId)}>Order ID: {orderId}</h3>
                        {selectedOrderId === orderId && (
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
                                    {groupedOrders[orderId].map((orderDetail, index) => (
                                        <tr key={index}>
                                            <td><img src={`http://localhost:8080/products/images/${orderDetail.product.imageURL}`} alt={orderDetail.product.name} width="100" /></td>
                                            <td>{orderDetail.product.name}</td>
                                            <td>{orderDetail.quantity}</td>
                                            <td>{orderDetail.product.price} €</td>
                                            <td>{new Date(orderDetail.orderTime).toLocaleString()}</td>
                                        </tr>
                                    ))}
                                </tbody>
                            </table>
                        )}
                    </div>
                ))
            )}
        </div>
    );
}

export default MyOrders;

import React, { useState, useEffect } from 'react';
import axios from 'axios';
import authHeader from '../services/auth-header';
import '../AllOrders.css';
import { Link } from 'react-router-dom';  
function AllOrders() {
    const [users, setUsers] = useState([]);
    const [selectedUser, setSelectedUser] = useState(null);
    const [orders, setOrders] = useState([]);
    const [totalPrice, setTotalPrice] = useState(0);
   

    useEffect(() => {
        fetchAllUsers();
     
    }, []);

    const fetchAllUsers = async () => {
        try {
            const response = await axios.get('http://localhost:8080/orderDetails/users', {
                headers: authHeader(),
            });
            setUsers(response.data);
        } catch (error) {
            console.error('Error fetching users:', error);
        }
    };

    const fetchOrdersByUser = async (userId) => {
        try {
            const response = await axios.get('http://localhost:8080/orderDetails/details/user/${userId}', {
                headers: authHeader(),
            });
            setOrders(response.data);

            const total = response.data.reduce((acc, orderDetail) => acc + (orderDetail.quantity * orderDetail.product.price), 0);
            setTotalPrice(total);
        } catch (error) {
            console.error('Error fetching user orders:', error);
        }
    };

   

    

    const handleUserClick = (user) => {
        setSelectedUser(user);
        setOrders([]);
        setTotalPrice(0);
        
        fetchOrdersByUser(user.id);
    };

    return (
        <div className="orders-container">
            <h2>Users</h2>
            <table className="users-table">
                <thead>
                    <tr>
                        <th>Username</th>
                        <th> Address</th>
                    </tr>
                </thead>
                <tbody>
                    {users.map((user) => (
                        <tr key={user.id}>
                            <td>
                                <a href="#" onClick={() => handleUserClick(user)}>{user.username}</a>
                            </td><td>
                    
                
                    
                    
                    </td>
                        </tr>

                    ))}
    

                </tbody>
            </table>

           
            

          
         

            {selectedUser && (
                <div>
                    <h3>Orders of {selectedUser.username}</h3>
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
                            {orders.map((orderDetail) => (
                                <tr key={orderDetail.id}>
                                    <td><img src={`http://localhost:8080/products/images/${orderDetail.product.imageURL}`} alt={orderDetail.product.name} width="100" /></td>
                                    <td>{orderDetail.product.name}</td>
                                    <td>{orderDetail.quantity}</td>
                                    <td>{orderDetail.product.price} €</td>
                                    <td>{new Date(orderDetail.order.orderTime).toLocaleString()}</td>
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

export default AllOrders;

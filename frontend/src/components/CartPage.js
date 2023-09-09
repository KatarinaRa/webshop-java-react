import React, { useState, useEffect } from "react";
import axios from 'axios';
import authHeader from '../services/auth-header';
import { useNavigate } from 'react-router-dom';

const Cart = () => {
    const [cart, setCart] = useState([]);
    const [viewingImage, setViewingImage] = useState(null);
    const [sizeNames, setSizeNames] = useState({});    

    const user = JSON.parse(localStorage.getItem('user'));
    const navigate = useNavigate();

    useEffect(() => {
        const fetchCart = async () => {
            try {
                if (user && user.id) {
                    const response = await axios.get(`http://localhost:8080/orders/${user.id}`, { headers: authHeader() });
                    if (response.data) {
                        setCart(response.data);
                    }
                }
            } catch (error) {
                console.error('Error fetching the cart', error);
            }
        };

        fetchCart();
    }, []);

    const handleDelete = async (id) => {
        try {
            await axios.delete(`http://localhost:8080/orderDetails/${id}`, { headers: authHeader() });
            setCart(cart.filter(item => item.id !== id));
        } catch (error) {
            console.error('Error deleting the item', error);
        }
    }
    const checkProductAvailability = async (productId, sizeId, requestedQuantity) => {
        try {
            const response = await axios.get(`http://localhost:8080/products/${productId}/sizeId/${sizeId}/isAvailable?quantity=${requestedQuantity}`, { headers: authHeader() });
            return response.data;
        } catch (error) {
            console.error('Error checking product availability', error);
            return false;
        }
    }
    const getSizeName = async (sizeId) => {
        try {
            const response = await axios.get(`http://localhost:8080/name/${sizeId}`, { headers: authHeader() });
    
            if (response.status === 200) {
                const sizeName = response.data;
                return sizeName;
            } else {
                return null;
            }
        } catch (error) {
            return null;
        }
    };
    
    useEffect(() => {
        const fetchSizeNames = async () => {
            for (let item of cart) {
                const sizeId = item.sizeId;
                const sizeName = await getSizeName(sizeId);
                setSizeNames(prevSizeNames => ({ ...prevSizeNames, [sizeId]: sizeName }));
            }
        };
    
        fetchSizeNames();
    }, [cart]);
    

    const updateQuantity = async (id, newQuantity) => {
    try {
        const item = cart.find(item => item.id === id);
        const isAvailable = await checkProductAvailability(item.product.id, item.sizeId, newQuantity);
        
        if (!isAvailable) {
            alert("Nema dovoljno proizvoda u skladištu.");
            return;
        }

        
        await axios.put(`http://localhost:8080/orderDetails/${id}`, { quantity: newQuantity }, { headers: authHeader() });

        
        const updatedCart = cart.map(item => {
            if (item.id === id) {
                return {
                    ...item,
                    quantity: newQuantity
                };
            }
            return item;
        });
        
        setCart(updatedCart);

    } catch (error) {
        console.error('Error updating quantity', error);
    }
}

const completeOrder = async () => {
    const response = await axios.post('http://localhost:8080/orderDetails/complite', {}, { headers: authHeader() });
    navigate('/order-success');
}

    const showImage = (imageUrl) => {
        setViewingImage(imageUrl);
    };

    const closeImageModal = () => {
        setViewingImage(null);
    };

    if (!cart || cart.length === 0) {
        return <p>Jos uvijek nemate narudzbi.</p>;
    }

    const total = cart.reduce((sum, item) => sum + (item.price * item.quantity), 0);

    return (
        <div className="container mt-5">
            <h2>Your Cart</h2>

            {viewingImage && (
                <div style={{
                    position: 'fixed',
                    top: 0,
                    left: 0,
                    width: '100%',
                    height: '100%',
                    backgroundColor: 'rgba(0,0,0,0.7)',
                    display: 'flex',
                    justifyContent: 'center',
                    alignItems: 'center'
                }}
                    onClick={closeImageModal}>
                    <img src={viewingImage} alt="Full View" style={{ maxWidth: '80%', maxHeight: '80%' }} />
                </div>
            )}

            <table className="table mt-4">
                <thead>
                    <tr>
                        <th>Product</th>
                        <th>Description</th>
                        <th>Veličina</th>
                        <th>Quantity</th>
                        <th>Price per unit</th>
                        <th>Action</th>  
                    </tr>
                </thead>
                <tbody>
                    {cart.map(item => (
                        <tr key={item.id}>
                            <td>
                                <img
                                    src={`http://localhost:8080/products/images/${item.product.imageURL}`}
                                    alt={item.product.name}
                                    style={{ width: '100px', cursor: 'pointer' }}
                                    onClick={() => showImage(`http://localhost:8080/products/images/${item.product.imageURL}`)}
                                />
                            </td>
                            <td>
                                {item.product.name}
                            </td>
                            <td>
                            {sizeNames[item.sizeId] ? sizeNames[item.sizeId] : 'Loading...'}
                            </td>

                            <td>
                                <div style={{ display: 'flex', flexDirection: 'row' }}>
                                    <span> {item.quantity} </span>
                                </div>
                            </td>
                            <td>{item.price.toFixed(2)}€</td>
                            <td>
                                <button onClick={() => handleDelete(item.id)}>Delete</button>
                            </td>
                       
                        </tr>
                    ))}
                
                </tbody>
            </table>

            <h3 className="mt-4">Total: {total.toFixed(2)}€</h3>
            <button onClick={completeOrder} style={{ marginTop: '20px' }}>Završi narudžbu</button>

        </div>
    );
};

export default Cart;
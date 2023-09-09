import React from 'react';
import '../OrderSuccess.css';

const OrderSuccess = () => {
  return (
    <div className="order-success-container">
      <div className="order-success-message">
        <h2>Narudžba Završena</h2>
        <p>Hvala što ste naručili. Vaša narudžba je uspješno zaprimljena i uskoro će biti poslana.</p>
      </div>
    </div>
  );
};

export default OrderSuccess;
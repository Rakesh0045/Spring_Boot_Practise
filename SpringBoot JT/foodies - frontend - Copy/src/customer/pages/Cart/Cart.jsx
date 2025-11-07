import React, { useContext } from "react";
import "./Cart.css";
import { Link, useNavigate } from "react-router-dom";
import { StoreContext } from "../../../context/StoreContext";
import { calculateCartTotals } from "../../util/cartUtils";
import { updateItemQuantity } from "../../../services/cartService"; // adjust path if needed
import { removeItemFromCart } from "../../../services/cartService"; // Add this at the top


const Cart = () => {
  const navigate = useNavigate();
  const { cart, setCart } = useContext(StoreContext);

  const cartItems = cart?.items || [];

  const increaseQty = async (foodId) => {
    const updatedItems = cartItems.map(item =>
      item.foodId === foodId
        ? { ...item, quantity: item.quantity + 1 }
        : item
    );
    setCart({ ...cart, items: updatedItems });

    const item = cartItems.find(i => i.foodId === foodId);
    await updateItemQuantity(foodId, item.quantity + 1);
  };

  const decreaseQty = async (foodId) => {
    const item = cartItems.find(i => i.foodId === foodId);
    if (item.quantity <= 1) return;

    const updatedItems = cartItems.map(item =>
      item.foodId === foodId
        ? { ...item, quantity: item.quantity - 1 }
        : item
    );
    setCart({ ...cart, items: updatedItems });

    await updateItemQuantity(foodId, item.quantity - 1);
  };


  const removeFromCart = async (foodId) => {
    try {
      const updatedCart = await removeItemFromCart(foodId);
      setCart(updatedCart);
    } catch (error) {
      console.error("Failed to remove item from cart", error);
    }
  };

  const { subtotal, shipping, tax, total } = calculateCartTotals(cartItems);

  return (
    <div className="container py-5">
      <h1 className="mb-5">Your Shopping Cart</h1>
      <div className="row">
        <div className="col-lg-8">
          {cartItems.length === 0 ? (
            <p>Your cart is empty.</p>
          ) : (
            <div className="card mb-4">
              <div className="card-body">
                {cartItems.map(item => (
                  <div key={item.foodId} className="row cart-item mb-3 align-items-center">
                    <div className="col-md-6">
                      <h5>{item.foodName}</h5>
                      <p className="text-muted">Category: {item.foodCategory}</p>
                      <p>Price: ₹{item.foodPrice.toFixed(2)}</p>
                      <div className="input-group mb-2" style={{ maxWidth: "120px" }}>
                        <button className="btn btn-outline-secondary btn-sm" onClick={() => decreaseQty(item.foodId)}>-</button>
                        <input type="text" className="form-control form-control-sm text-center" value={item.quantity} readOnly />
                        <button className="btn btn-outline-secondary btn-sm" onClick={() => increaseQty(item.foodId)}>+</button>
                      </div>
                    </div>
                    <div className="col-md-4 text-end">
                      <p className="fw-bold">₹{(item.foodPrice * item.quantity).toFixed(2)}</p>
                      <button className="btn btn-sm btn-outline-danger" onClick={() => removeFromCart(item.foodId)}>
                        <i className="bi bi-trash"></i> Remove
                      </button>
                    </div>
                    <hr />
                  </div>
                ))}
              </div>
            </div>
          )}
          <Link to="/explore" className="btn btn-outline-primary">
            <i className="bi bi-arrow-left me-2"></i>Continue Shopping
          </Link>
        </div>
        <div className="col-lg-4">
          <div className="card cart-summary">
            <div className="card-body">
              <h5 className="card-title mb-4">Order Summary</h5>
              <div className="d-flex justify-content-between mb-2">
                <span>Subtotal</span><span>₹{subtotal.toFixed(2)}</span>
              </div>
              <div className="d-flex justify-content-between mb-2">
                <span>Shipping</span><span>₹{shipping.toFixed(2)}</span>
              </div>
              <div className="d-flex justify-content-between mb-2">
                <span>Tax</span><span>₹{tax.toFixed(2)}</span>
              </div>
              <hr />
              <div className="d-flex justify-content-between mb-3">
                <strong>Total</strong><strong>₹{total.toFixed(2)}</strong>
              </div>
              <button className="btn btn-primary w-100" disabled={cartItems.length === 0} onClick={() => navigate("/order")}>
                Proceed to Checkout
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Cart;

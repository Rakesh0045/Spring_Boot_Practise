import React, { useContext, useState } from "react";
import { StoreContext } from "../../../context/StoreContext";
import { calculateCartTotals } from "../../util/cartUtils";
import { toast } from "react-toastify";
import { useNavigate } from "react-router-dom";
import { placeOrder } from "../../../services/orderService";
import { clearCartItems } from "../../../services/cartService";

const PlaceOrder = () => {
  const { cart, setCart, token } = useContext(StoreContext);
  const navigate = useNavigate();

  const cartItems = cart?.items || [];

  const [data, setData] = useState({
    fullName : "",
    email: "",
    phoneNumber: "",
    address: "",
    state: "",
    city: "",
    zip: "",
  });

  const { shipping, tax, total } = calculateCartTotals(cartItems);

  const onChangeHandler = (e) => {
    const { name, value } = e.target;
    setData(prev => ({ ...prev, [name]: value }));
  };

  const onSubmitHandler = async (e) => {
    e.preventDefault();

    try {
      const response = await placeOrder(token, data); // Sends request to /api/orders/{userId}/place
      if (response) {
        toast.success("Order placed successfully");
        await clearCartItems(token); // Backend clears cart
        setCart([]); // Frontend clears cart state
        navigate("/myorders");
      } else {
        toast.error("Failed to place order");
      }
    } catch (error) {
      console.error("Order error", error);
      toast.error("Something went wrong");
    }
  };

  return (
    <div className="container mt-4">
      <main>
        <div className="py-5 text-center">
          <h2>Place Your Order</h2>
        </div>
        <div className="row g-5">
          <div className="col-md-5 col-lg-4 order-md-last">
            <h4 className="d-flex justify-content-between align-items-center mb-3">
              <span className="text-primary">Your cart</span>
              <span className="badge bg-primary rounded-pill">{cartItems.length}</span>
            </h4>
            <ul className="list-group mb-3">
              {cartItems.map((item) => (
                <li key={item.foodId} className="list-group-item d-flex justify-content-between lh-sm">
                  <div>
                    <h6 className="my-0">{item.foodName}</h6>
                    <small className="text-muted">Qty: {item.quantity}</small>
                  </div>
                  <span className="text-muted">₹{(item.foodPrice * item.quantity).toFixed(2)}</span>
                </li>
              ))}
              <li className="list-group-item d-flex justify-content-between">
                <span>Shipping</span>
                <span>₹{shipping.toFixed(2)}</span>
              </li>
              <li className="list-group-item d-flex justify-content-between">
                <span>Tax</span>
                <span>₹{tax.toFixed(2)}</span>
              </li>
              <li className="list-group-item d-flex justify-content-between">
                <strong>Total</strong>
                <strong>₹{total.toFixed(2)}</strong>
              </li>
            </ul>
          </div>

          <div className="col-md-7 col-lg-8">
            <h4 className="mb-3">Billing address</h4>
            <form onSubmit={onSubmitHandler}>
              <div className="row g-3">
                <div className="col-12">
                  <label className="form-label">Full name</label>
                  <input type="text" name="fullName" value={data.fullName} onChange={onChangeHandler} required className="form-control" />
                </div>
                <div className="col-sm-6">
                  <label className="form-label">Email</label>
                  <input type="email" name="email" value={data.email} onChange={onChangeHandler} required className="form-control" />
                </div>
                <div className="col-sm-6">
                  <label className="form-label">Phone</label>
                  <input type="text" name="phoneNumber" value={data.phoneNumber} onChange={onChangeHandler} required className="form-control" />
                </div>
                <div className="col-12">
                  <label className="form-label">Address</label>
                  <textarea name="address" value={data.address} onChange={onChangeHandler} required className="form-control"></textarea>
                </div>
                <div className="col-md-5">
                  <label className="form-label">State</label>
                  <input type="text" name="state" value={data.state} onChange={onChangeHandler} required className="form-control" />
                </div>
                <div className="col-md-4">
                  <label className="form-label">City</label>
                  <input type="text" name="cityr" value={data.citysda} onChange={onChangeHandler} required className="form-control" />
                </div>
                <div className="col-md-3">
                  <label className="form-label">Zip</label>
                  <input type="text" name="zip" value={data.zip} onChange={onChangeHandler} required className="form-control" />
                </div>
              </div>
              <hr className="my-4" />
              <button className="w-100 btn btn-primary btn-lg" type="submit" disabled={cartItems.length === 0}>
                Place Order
              </button>
            </form>
          </div>
        </div>
      </main>
    </div>
  );
};

export default PlaceOrder;

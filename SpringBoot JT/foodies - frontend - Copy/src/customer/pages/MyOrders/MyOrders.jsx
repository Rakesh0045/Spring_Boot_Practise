import React, { useEffect, useState, useContext } from "react";
import { StoreContext } from "../../../context/StoreContext";
import { fetchUserOrders } from "../../../services/orderService";
import "./MyOrders.css";

const statusImages = {
  FOOD_PREPARING: "/food_preparing.png",
  OUT_FOR_DELIVERY: "/out_for_delivery.png",
  DELIVERED: "/delivered.png",
};

const statusLabels = {
  FOOD_PREPARING: "FOOD PREPARING",
  OUT_FOR_DELIVERY: "OUT FOR DELIVERY",
  DELIVERED: "DELIVERED",
};

const MyOrders = () => {
  const { token } = useContext(StoreContext);
  const [orders, setOrders] = useState([]);

  const fetchOrders = async () => {
    try {
      const response = await fetchUserOrders(token);
      setOrders(response);
    } catch (error) {
      console.error("Failed to fetch orders:", error);
    }
  };

  useEffect(() => {
    if (token) {
      fetchOrders();
    }
  }, [token]);

  return (
    <div className="container">
      <div className="py-5 row justify-content-center">
        <div className="col-11 card shadow-lg">
          <h3 className="p-3">My Orders</h3>
          {orders.length === 0 ? (
            <p className="text-center">No orders found.</p>
          ) : (
            <table className="table table-responsive table-hover">
              <tbody>
                {orders.map((order, index) => (
                  <tr key={order.orderId}>
                    <td>
                      <img src={statusImages[order.status] || "/default_status.png"}
                       alt="Delivery" height={48} width={48} />
                    </td>
                    <td>
                      {order.items.map((item, idx) => `${item.foodName} x ${item.quantity}`).join(", ")}
                    </td>
                    <td>&#x20B9;{order.totalAmount.toFixed(2)}</td>
                    <td>Items: {order.items.length}</td>
                    <td className="fw-bold text-capitalize">
                      &#x25cf; {statusLabels[order.status] || order.status}
                    </td>
                    {/* <td>
                      <button className="btn btn-sm btn-warning" onClick={fetchOrders}>
                        <i className="bi bi-arrow-clockwise"></i>
                      </button>
                    </td> */}
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      </div>
    </div>
  );
};

export default MyOrders;

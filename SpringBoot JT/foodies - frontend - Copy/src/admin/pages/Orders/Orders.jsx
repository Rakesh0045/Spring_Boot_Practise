  // const statusTransitions = {
  //   FOOD_PREPARING: ["OUT_FOR_DELIVERY", "DELIVERED"],
  //   OUT_FOR_DELIVERY: ["DELIVERED"],
  //   DELIVERED: [], // No transitions from here
  // };
  // const statusLabels = {
  //   FOOD_PREPARING: "Food Preparing",
  //   OUT_FOR_DELIVERY: "Out for Delivery",
  //   DELIVERED: "Delivered",
  // };

  {/* <select
                        className="form-control"
                        onChange={(event) => updateStatus(event, order.orderId)}
                        value={order.status}
                        disabled={statusTransitions[order.status].length === 0} // Disable if no further transitions
                      >
                        <option value={order.status} disabled>
                          {statusLabels[order.status]} 
                        </option>
                        {statusTransitions[order.status].map((nextStatus) => (
                          <option key={nextStatus} value={nextStatus}>
                            {statusLabels[nextStatus]}
                          </option>
                        ))}
                      </select> */}


import React, { useContext, useEffect, useState } from "react";
import { fetchAllOrders, updateOrderStatus } from "../../../services/orderService";
import { toast } from "react-toastify";
import { StoreContext } from "../../../context/StoreContext";

const Orders = () => {
  const { token } = useContext(StoreContext);
  const [orders, setOrders] = useState([]);

  const loadOrders = async () => {
      try {
        const data = await fetchAllOrders(token);
        setOrders(data);
      } catch (err) {
        console.error("Failed to fetch orders:", err);
      }
    };

    useEffect(() => {
      if(token) {
        loadOrders();
      }
    }, [token]);

  const updateStatus = async (event, orderId) => {
    const newStatus = event.target.value;
    const success = await updateOrderStatus(orderId, newStatus, token);

    if (success) {
      const updatedOrders = orders.map(order =>
        order.orderId === orderId ? { ...order, status: newStatus } : order
      );
      setOrders(updatedOrders);
      toast.success("Order status updated.");
    } else {
      toast.error("Failed to update order status.");
    }
  };

  return (
    <div className="container">
      <div className="py-2 row justify-content-center">
        <div className="col-11 card shadow-lg py-2">
          <table className="table table-responsive table-hover">
            <tbody>
              {orders.map((order, index) => (
                <tr key={index}>
                  {/* <td>
                    <img src={`http://localhost:1200/images/${order.foodImage}`}
                      alt="food" height={48} width={48} 
                    />
                  </td> */}
                  <td>
                    <div>
                      <strong>Items:</strong>{" "}
                      {order.items.map((item, idx) => (
                        <span key={idx}>
                          {item.foodName} x {item.quantity}
                          {idx !== order.items.length - 1 && ", "}
                        </span>
                      ))}
                    </div>

                    {/* Contact and Address Info Below Items */}
                    <div className="text-muted mt-1" style={{ fontSize: "0.85rem" }}>
                      <div>
                        <strong>Contact:</strong>{" "}
                        {order.contactInfo && order.contactInfo !== "null, null, null"
                          ? order.contactInfo
                          : "N/A"}
                      </div>
                      <div>
                        <strong>Address:</strong>{" "}
                        {order.addressInfo && !order.addressInfo.includes("null")
                          ? order.addressInfo
                          : "N/A"}
                      </div>
                    </div>
                  </td>
                  <td>&#x20B9;{order.totalAmount.toFixed(2)}</td>
                  <td>Items: {order.items.length}</td>
                  <td>
                    <select
                      className="form-control"
                      onChange={(event) => updateStatus(event, order.orderId)}
                      value={order.status}
                    >
                      <option value="FOOD_PREPARING">Food Preparing</option>
                      <option value="OUT_FOR_DELIVERY">Out for Delivery</option>
                      <option value="DELIVERED">Delivered</option>
                    </select>
                  </td>
                </tr>
              ))}
            </tbody>

          </table>
        </div>
      </div>
    </div>
  );
};


export default Orders;
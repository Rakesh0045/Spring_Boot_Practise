import { createContext, useEffect, useState } from "react";
import { jwtDecode } from "jwt-decode";
import { fetchCategories } from "../services/categoryService";
import { getCartByUserId } from "../services/cartService";

export const StoreContext = createContext(null);

export const StoreContextProvider = (props) => {
  const [categories, setCategories] = useState([]);
  const [token, setToken] = useState(localStorage.getItem("token") || "");
  const [role, setRole] = useState("");
  const [cart, setCart] = useState([]);

  const loadCategories = async () => {
    const data = await fetchCategories();
    setCategories(data);
  };

  const initializeCart = async () => {
    if(!token) {
      return ;
    }
    
    try {
      const cartData = await getCartByUserId(token);
      if (cartData) {
        setCart(cartData);
      }
    } catch (error) {
      console.error("Failed to fetch cart on init:", error);
    }
  };

  // Step 1: Decode token and set role
  useEffect(() => {
    if (token) {
      try {
        const decoded = jwtDecode(token);
        const userRole = decoded.role || "";
        setRole(userRole);
      } catch (err) {
        console.error("Invalid token:", err);
        setToken("");
        setRole("");
        localStorage.removeItem("token");
      }
    }

    loadCategories();
    initializeCart();
  }, [token]);

  const contextValue = {
    categories,
    token,
    setToken,
    role,
    setRole,
    cart,
    setCart,

    // later added
    setCategories
  };

  return (
    <StoreContext.Provider value={contextValue}>
      {props.children}
    </StoreContext.Provider>
  );
};

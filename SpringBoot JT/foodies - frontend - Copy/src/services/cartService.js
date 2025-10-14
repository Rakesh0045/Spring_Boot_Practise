const API_URL = `http://localhost:1205/tasty-town/api/v1/cart`
import axios from "axios";

export const getCartByUserId = async (token) => {
    try {
        const response = await axios.get(`${API_URL}`, 
            {
                headers: { Authorization: `Bearer ${token}` },
            },
        );

        console.log('cart items of the user ', response.data);
        
        return response.data;
    } catch (error) {
        console.error('Error while fetching the cart data', error);
    }
};

export const addItemToCart = async (foodId, quantity) => {
    const token = localStorage.getItem("token");
    try {
        const response = await axios.post(`${API_URL}/items`,
            {
                foodId,
                quantity
            },
            {
                headers: {
                    Authorization: `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            }
        );
        return response.data;
    } catch (error) {
        console.error("Error adding item to cart:", error);
        throw error;
    }
};

export const updateItemQuantity = async (foodId, quantity) => {
    const token = localStorage.getItem("token");

    try {
        const response = await axios.put(
            `${API_URL}/items`,
            {
                foodId, 
                quantity 
            },
            {
                headers: {
                    Authorization: `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            }
        );
        return response.data;
    } catch (error) {
        console.error("Error updating item quantity:", error);
        throw error;
    }
};


export const removeItemFromCart = async (foodId) => {
    const token = localStorage.getItem("token");

    try {
        const response = await axios.delete(
            `${API_URL}/items/${foodId}`,
            {
                headers: {
                    Authorization: `Bearer ${token}`,
                }
            }
        );
        return response.data; // Updated cart
    } catch (error) {
        console.error("Error removing item from cart:", error);
        throw error;
    }
};


export const clearCartItems = async (token) => {
    try {
        await axios.delete(`${API_URL}`, {
            headers: { Authorization: `Bearer ${token}` },
        });
        console.log(`clearing cart items`);
    } catch (error) {
        console.error('Error while clearing the cart', error);
        throw error;
    }
}
import axios from "axios";

const API_URL = "http://localhost:1205/tasty-town/api/v1/auth";

export const login = async (data) => {
    try {
         const response = await axios.post(API_URL + "/login", {
            userEmail: data.email,      // match backend field
            userPassword: data.password // match backend field
        });
        console.log("Logged in user", response.data);

        return response;
    } catch (error) {
        throw error;
    }
}

export const logout = async () => {
    const token = localStorage.getItem('token')
    try {
        const response = await axios.post(`${API_URL}/logout`,
            {}, // Empty body since it's a logout request
            {
                headers: {
                    'Authorization': `Bearer ${token}` // Include token in Authorization header
                }
            }
        );
    
        console.log("User logged out successfully");
        return response;
    } catch (error) {
        console.error('Logout failed:', error);
    }
};

export const registerUser = async (data) => {
    try {
        const response = await axios.post(
            API_URL + "/register",
            {
                userEmail: data.email,
                userPassword: data.password
            }
        );
        console.log('User Registered successfully', response.data);
        return response;
    } catch (error) {
        throw error;
    }
}

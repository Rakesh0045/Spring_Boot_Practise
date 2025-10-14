import axios from "axios"

const BASE_URL = `http://localhost:1205/tasty-town/api/v1/categories`

export const fetchCategories = async () => {
    try {
        const response = await axios.get(`${BASE_URL}`);
        console.log("fetching all categories", response.data);
        return response.data;
    } catch (error) {
        console.error("Error fetching categories", error)   
    }
};

export const deleteCategory = async (categoryId) => {
    const token = localStorage.getItem('token');
    try {
        const response = await axios.delete(`${BASE_URL}/${categoryId}`,
            {
                headers : {
                    Authorization : `Bearer ${token}`
                }
            }
        );       
        console.log(`category deleted`);
        return response; 
    } catch (error) {
        console.error(`Error deleting category`, error);
        throw error;
    }
};

export const addCategory = async (categoryName) => {
    const token = localStorage.getItem('token');
    try {
        const response = await axios.post(`${BASE_URL}`,
            {
                categoryName
            },
            {
                headers : {
                    Authorization : `Bearer ${token}`
                }
            }
        );       
        console.log(`category added `, response.data);
        return response.data; 
    } catch (error) {
        console.error(`Error adding category`, error);
        throw error;
    }
};
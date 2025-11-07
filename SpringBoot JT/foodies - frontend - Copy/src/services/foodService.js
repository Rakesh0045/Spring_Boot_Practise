import axios from "axios";

const BASE_URL = 'http://localhost:1205/tasty-town/api/v1/foods';

export const fetchFoods = async () => {
    try {
        const response = await axios.get(BASE_URL);
        console.log("foods ", response.data);
        
        return response.data;
    } catch (error) {
        console.log('Error fetching food list:', error);
        throw error;
    }    
}

export const fetchFoodById = async (foodId) => {
    try {
        const response = await axios.get(`${BASE_URL}/foodId`);
        console.log("food by id ", response.data);

        return response.data;
    } catch (error) {
        console.log('Error fetching food list:', error);
        throw error;
    }    
}

// Paginated foods
// export const fetchPaginatedFoods = async (pageNumber = 0, pageSize = 4) => {
//     try {
//         const response = await axios.get(`${BASE_URL}/paginated-foods?page=${pageNumber}&size=${pageSize}`);
//         console.log("paginatedfoods ", response.data)
        
//         return response.data.content;
//     } catch (error) {
//         console.log('Error fetching food list:', error);
//         throw error;
//     }    
// }

export const fetchPaginatedFoods = async (pageNumber = 0, pageSize = 4, filters = {}) => {
  try {
    let url = `${BASE_URL}/paginated-foods?page=${pageNumber}&size=${pageSize}`;

    if (filters.categoryId && filters.categoryId !== 'all') {
      url += `&categoryId=${filters.categoryId}`;
    }

    if (filters.searchText && filters.searchText.trim() !== '') {
      url += `&search=${encodeURIComponent(filters.searchText.trim())}`;
    }

    const response = await axios.get(url);
    console.log('paginated foods', response.data);
    
    return response.data;
  } catch (error) {
    console.error('Error fetching food list:', error);
    throw error;
  }
};

// Paginated foods end
export const getFoodsByCategory = async (categoryId) => {
    try {
        const response = await axios.get(`${BASE_URL}/categories/${categoryId}/foods`);
        console.log("Foods By Category", response.data);        
        return response.data; 
    } catch (error) {
        console.error(`Error fetching foods for category ${categoryId}:`, error);
        throw error;
    }
};

export const getFoodById = async (foodId) => {
    try {
        const response = await axios.get(`${BASE_URL}/${foodId}`);
        console.log("Food by id", response.data);        
        return response.data; 
    } catch (error) {
        console.error(`Error fetching food by id ${foodId}:- `, error);
        throw error;
    }
}



// for admin
export const deleteFood = async (foodId) => {
    const token = localStorage.getItem('token');
    try {
        const response = await axios.delete(`${BASE_URL}/${foodId}`,
            {
                headers : {
                    Authorization : `Bearer ${token}`
                }
            }
        );       
        console.log(`food deleted`);
        return response; 
    } catch (error) {
        console.error(`Error deleting food`, error);
        throw error;
    }
}

// export const addFood = async (data) => {
//     const token = localStorage.getItem('token');

//     const payload = {
//         foodName: data.name,
//         foodDescription: data.description,
//         foodPrice: parseFloat(data.price),
//         categoryId: data.category
//     };

//     try {
//         const response = await axios.post(BASE_URL, payload, {
//             headers: {
//                 Authorization: `Bearer ${token}`
//             }
//         });
//         return response.data;
//     } catch (error) {
//         console.error("Error adding food:", error);
//         throw error;
//     }
// };

// export const addFood = async (foodData, image) => {
//   const token = localStorage.getItem('token');
  
//   const formData = new FormData();

//   formData.append(
//     'foodData',
//     new Blob(
//       [JSON.stringify({
//         foodName: foodData.name,
//         foodDescription: foodData.description,
//         foodPrice: foodData.price,
//         categoryId: foodData.category
//       })],
//       { type: 'application/json' }
//     )
//   );

//   if (image) {
//     formData.append("foodImage", image); // 👈 must match @RequestPart("foodImage")
//   }

//   try {
//     const response = await axios.post(`${BASE_URL}`, formData, {
//       headers: {
//         "Content-Type": "multipart/form-data",
//         Authorization: `Bearer ${token}`
//       }
//     });

//     return response.data;
//   } catch (error) {
//     console.error('Error adding food:', error.response?.data || error.message);
//     throw error;
//   }
// };

export const addFood = async (foodData, image) => {
  const token = localStorage.getItem('token');
  const formData = new FormData();

  formData.append(
    'rawJson', // 👈 must match @RequestPart("rawJson")
    new Blob(
      [JSON.stringify({
        foodName: foodData.name,
        foodDescription: foodData.description,
        foodPrice: foodData.price,
        categoryId: foodData.category
      })],
      { type: 'application/json' }
    )
  );

  if (image) {
    formData.append("foodImage", image); // 👈 must match @RequestPart("foodImage")
  }

  try {
    const response = await axios.post(`${BASE_URL}`, formData, {
      headers: {
        "Content-Type": "multipart/form-data",
        Authorization: `Bearer ${token}`
      }
    });

    return response.data;
  } catch (error) {
    console.error('Error adding food:', error.response?.data || error.message);
    throw error;
  }
};


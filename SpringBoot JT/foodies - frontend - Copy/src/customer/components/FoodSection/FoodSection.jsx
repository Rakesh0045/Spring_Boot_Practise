import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import FoodDisplay from '../FoodDisplay/FoodDisplay';
import { fetchPaginatedFoods } from '../../../services/foodService';

const FoodSection = ({ title = "Explore Our Foods" }) => {
  const [foods, setFoods] = useState([]);

  const fetchFoods = async () => {
    try {
      const paginatedFoods = await fetchPaginatedFoods();
      setFoods(paginatedFoods.content);
    } catch (err) {
      console.error('Failed to fetch foods:', err);
    }
  };

  useEffect(() => {
    fetchFoods();
  }, []);

  return (
    <section className="container-fluid shadow-lg rounded-4 p-4 mb-4">
      <div className="d-flex justify-content-between align-items-center mb-3">
        <h4>{title}</h4>
        <Link to="/explore" className="btn btn-outline-primary btn-sm">
          Explore All
        </Link>
      </div>
      
      <FoodDisplay foods={foods} />
    </section>
  );
};

export default FoodSection;

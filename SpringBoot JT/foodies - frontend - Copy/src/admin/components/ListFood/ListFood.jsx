import axios from "axios";
import React, { useContext, useEffect, useState } from "react";
import { toast } from "react-toastify";
import "./ListFood.css";
import { deleteFood, fetchFoods } from "../../../services/foodService";

const ListFood = () => {
  const [foods, setFoods] = useState([]);

  const fetchList = async () => {
    try {
      const data = await fetchFoods();
      setFoods(data);
    } catch (error) {
      toast.error("Error while reading the foods.");
    }
  };

  const removeFood = async (foodId) => {
    try {
      const response = await deleteFood(foodId);
      if (response.status === 204) {
        toast.success("Food removed.");
        await fetchList();
      } else {
        toast.error("Error occred while removing the food");
      }
    } catch (error) {
      toast.error("Error occred while removing the food.");
    }
  };

  useEffect(() => {
    fetchList();
  }, []);

  return (
    <div className="py-5 row justify-content-center">
      <div className="col-11 card shadow-lg py-2">
        <table className="table table-hover">
          <thead>
            <tr>
              <th>Image</th>
              <th>Name</th>
              <th>Category</th>
              <th>Price</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody>
            {foods.map((food) => {
              return (
                <tr key={food.foodId}>
                  <td>
                    <img src={`http://localhost:1205/tasty-town/api/v1/images/${food.foodImage}`} alt="" height={48} width={48} />
                  </td>
                  <td>{food.foodName}</td>
                  <td>{food.categoryName}</td>
                  <td>&#8377;{food.foodPrice}.00</td>
                  <td className="text-danger">
                    <i
                      className="bi bi-trash-fill fs-4"
                      onClick={() => removeFood(food.foodId)}
                    ></i>
                  </td>
                </tr>
              );
            })}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default ListFood;

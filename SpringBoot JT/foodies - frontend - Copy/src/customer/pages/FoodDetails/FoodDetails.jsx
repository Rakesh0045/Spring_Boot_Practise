import React, { useContext, useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { toast } from "react-toastify";
import { addItemToCart } from "../../../services/cartService";
import { StoreContext } from "../../../context/StoreContext";
import { getFoodById } from "../../../services/foodService";
import styles from './FoodDetails.module.css';


const FoodDetails = () => {
  const { id } = useParams();
  const { setCart, token } = useContext(StoreContext); // ⬅️ use context
  const navigate = useNavigate();

  const [data, setData] = useState({});
  const [quantity, setQuantity] = useState(1);
  const [loaded, setLoaded] = useState(false);


  useEffect(() => {
     const fetchFood = async () => {
        try {
          const response = await getFoodById(id); // Fetch directly
          setData(response);
        } catch (error) {
          toast.error("Food item not found.");
        }
      };

      fetchFood();
  }, [id]);

  const addToCart = async (foodId) => {
    try {
      if(!token){
        toast.error('Please login to add item')
        return ;
      }

      const addedItem = await addItemToCart(foodId, quantity);
      setCart(addedItem)
      toast.success("Item added to cart");
      navigate("/cart");
    } catch (error) {
      toast.error("Failed to add to cart");
    }
  };

  return (
    <section className="py-5">
      <div className="container px-4 px-lg-5 my-5">
        <div className="row gx-4 gx-lg-5 align-items-center">
          <div className="col-md-6">
            <img
              onLoad={() => setLoaded(true)}
              className={`card-img-top mb-5 mb-md-0 rounded-4 img-thumbnail ${styles["food-image"]} ${loaded ? styles.fadeIn : styles.imageHidden}`}
              src={`http://localhost:1205/tasty-town/api/v1/images/${data.foodImage}`}
              alt="..."
              // height={450}
              // width={60}
            />
          </div>
          <div className="col-md-6">
            <div className="fs-5 mb-1">
              Category:{" "}
              <span className="badge text-bg-warning">
                {data?.categoryName}
              </span>
            </div>
            <h1 className="display-5 fw-bolder">{data.foodName}</h1>
            <div className="fs-5 mb-2">
              <span>&#8377;{data.foodPrice}.00</span>
            </div>
            <p className="lead">{data.foodDescription}</p>

            <div className="d-flex align-items-center mb-3">
              <label className="me-2">Quantity:</label>
              <input
                type="number"
                min="1"
                value={quantity}
                onChange={(e) => setQuantity(parseInt(e.target.value))}
                className="form-control w-auto"
              />
            </div>

            <button
              className="btn btn-outline-dark flex-shrink-0"
              type="button"
              onClick={() => addToCart(data.foodId)}
            >
              <i className="bi-cart-fill me-1"></i>
              Add to cart
            </button>
          </div>
        </div>
      </div>
    </section>
  );
};

export default FoodDetails;


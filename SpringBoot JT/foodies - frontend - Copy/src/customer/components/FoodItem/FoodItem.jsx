import { Link, useLocation } from "react-router-dom";
import "./FoodItem.css"
import { useState } from "react";

const FoodItem = ({ name, description, id, image, price }) => {
  const location = useLocation();
  const [loaded, setLoaded] = useState(false)

  return (
    <div className="col-12 col-sm-6 col-md-4 col-lg-3 mb-4 d-flex justify-content-center">
      <div className={`card  ${location.pathname === '/' ? 'food-card-section' : 'food-card'}`}>
        <Link to={`/food/${id}`}>
          <img
            src={image}
            onLoad={() => setLoaded(true)}
            className={`card-img-top ${location.pathname === '/' ? 'food-image-section' : 'food-image'} ${loaded ? 'fade-in' : 'image-hidden'} `}
            alt="Product Image"
            // height={300}
            // width={60}
          />
        </Link>

        <div className="card-body">
          <h5 className="card-title">{name}</h5>
          <p className="card-text">{description}</p>
          <div className="d-flex justify-content-between align-items-center">
            <span className="h5 mb-0">&#8377;{price}</span>
          </div>
        </div>

        <div className="card-footer d-flex justify-content-between bg-light">
          <Link className="btn btn-success btn-sm" to={`/food/${id}`}>
            View Food
          </Link>
        </div>
      </div>
    </div>
  );
};

export default FoodItem;

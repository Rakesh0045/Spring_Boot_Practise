import FoodItem from '../FoodItem/FoodItem';

const FoodDisplay = ({ foods }) => {
  return (
    <div className="container">
      <div className="row">
        {foods.length > 0 ? (
          foods.map((food, index) => (
            <FoodItem key={index}
              name={food.foodName}
              description={food.foodDescription}
              id={food.foodId}
              image={`http://localhost:1205/tasty-town/api/v1/images/${food.foodImage}`}
              price={food.foodPrice} />
          ))
        ) : (
          <div className="text-center mt-4">
            <h4>No food found.</h4>
          </div>
        )}
      </div>
    </div>
  );
};

export default FoodDisplay;
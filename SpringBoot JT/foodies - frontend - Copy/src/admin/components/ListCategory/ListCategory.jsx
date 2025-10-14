import  { useContext } from "react";
import { toast } from "react-toastify";
import { deleteCategory } from "../../../services/categoryService";
import { StoreContext } from "../../../context/StoreContext";

const ListCategory = () => {
  const {categories, setCategories} = useContext(StoreContext);

  const removeFood = async (foodId) => {
    try {
      const response = await deleteCategory(foodId);
      if (response.status === 204) {
        toast.success("Category removed.");
        // await fetchList();

        setCategories(prev => prev.filter(category => category.categoryId !== foodId));
      } else {
        toast.error("Error occred while removing the food.");
      }
    } catch (error) {
      toast.error("Error occred while removing the food.");
    }
  };

  return (
    <div className="py-5 row ps-5">
      <div className="col-6 card shadow-lg">
        <table className="table table-hover align-middle">
          <thead>
            <tr>
              <th >Name</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody>
            {categories.map((category) => {
              return (
                <tr key={category.categoryId}>
                  <td>{category.categoryName}</td>
                  <td className="text-danger">
                    <i
                      className="bi bi-trash-fill fs-4"
                      onClick={() => removeFood(category.categoryId)}
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

export default ListCategory;

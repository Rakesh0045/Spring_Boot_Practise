import { useState, useEffect, useContext } from 'react';
import FoodDisplay from '../../components/FoodDisplay/FoodDisplay';
import "./ExploreFood.css"
import { StoreContext } from '../../../context/StoreContext';
import { fetchPaginatedFoods } from '../../../services/foodService';

const ExploreFood = () => {
  const [foods, setFoods] = useState([]);
  const [currentPage, setCurrentPage] = useState(0);
  const [pageSize] = useState(8);
  const [totalPages, setTotalPages] = useState(0);
  const [selectedCategoryId, setSelectedCategoryId] = useState('all');
  const [searchText, setSearchText] = useState('');

  const { categories } = useContext(StoreContext);

  const fetchFoods = async () => {
    try {
      const data = await fetchPaginatedFoods(currentPage, pageSize, {
        categoryId: selectedCategoryId,
        searchText: searchText,
      });

      setFoods(data.content);
      setTotalPages(data.totalPages);
    } catch (err) {
      console.error('Failed to fetch foods:', err);
    }
  };

  useEffect(() => {
      fetchFoods();
      // Reset to page 0 if search/category changes
  }, [currentPage, selectedCategoryId, searchText]);

  const handleSearchSubmit = (e) => {
    e.preventDefault();
    setCurrentPage(0); // Reset to first page on new search
    fetchFoods();
  };

  return (
    <div className="page-container container">
      <div className="content-wrapper">
        <div className="row justify-content-center">
            <div className="col-md-6">
              <form onSubmit={handleSearchSubmit}>
                <div className="input-group mb-3">
                  <select
                    className="form-select mt-2"
                    style={{ maxWidth: '150px' }}
                    onChange={(e) => {
                      setSelectedCategoryId(e.target.value);
                      setCurrentPage(0);
                    }}
                    value={selectedCategoryId}
                  >
                    <option value="all">All</option>
                    {categories.map((category) => (
                      <option key={category.categoryId} value={category.categoryId}>
                        {category.categoryName}
                      </option>
                    ))}
                  </select>

                  <input
                    type="text"
                    className="form-control mt-2"
                    placeholder="Search your favorite dish..."
                    onChange={(e) => setSearchText(e.target.value)}
                    value={searchText}
                  />

                  <button className="btn btn-primary mt-2" type="submit">
                    <i className="bi bi-search"></i>
                  </button>
                </div>
              </form>
            </div>
          </div>

          <FoodDisplay foods={foods} />
      </div>

      {/* Pagination */}
      <div className="d-flex justify-content-center mt-5 mb-4">
        <div className="pagination">
          {Array.from({ length: totalPages }, (_, index) => (
            <button
              key={index}
              className={`btn btn-sm mx-1 ${index === currentPage ? 'btn-primary' : 'btn-outline-primary'}`}
              onClick={() => setCurrentPage(index)}
            >
              {index + 1}
            </button>
          ))}
        </div>
      </div>
    </div>
  );
};

export default ExploreFood;

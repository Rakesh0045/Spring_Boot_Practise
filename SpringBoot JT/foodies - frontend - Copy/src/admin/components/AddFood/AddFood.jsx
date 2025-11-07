import React, { useState, useEffect, useContext } from 'react';
import { addFood } from '../../../services/foodService';
import { fetchCategories } from '../../../services/categoryService'; // Make sure the path is correct
import { toast } from 'react-toastify';
import uploadImage from "../../../assets/upload.png";
import { StoreContext } from '../../../context/StoreContext';

const AddFood = () => {
    const {categories} = useContext(StoreContext)

    const [image, setImage] = useState(null);
    const [food, setFood] = useState({
        name: '',
        description: '',
        price: '',
        category: ''
    });

    useEffect(() => {
        if(categories.length > 0 && !food.category) {
            setFood(prev => ({...prev, category: categories[0].categoryId}))
        }
    }, [categories]);

    const onChangeHandler = (event) => {
        const { name, value } = event.target;
        setFood(food => ({ ...food, [name]: value }));
    };

    const onSubmitHandler = async (event) => {
        event.preventDefault();
        if (!image) {
            toast.error('Please select an image.');
            return;
        }
        try {
            await addFood(food, image);
            toast.success('Food added successfully.');
            setFood({ name: '', description: '', category: categories[0]?.categoryId || '', price: '' });
            setImage(null);
        } catch (error) {
            toast.error('Error adding food.');
        }
    };

    return (
        <div className="mx-2 mt-2">
            <div className="row">
                <div className="card col-md-4">
                    <div className="card-body">
                        <h2 className="mb-4">Add Food</h2>
                        <form onSubmit={onSubmitHandler}>
                            <div className="mb-3">
                                <label htmlFor="image" className="form-label">
                                    <img src={image ? URL.createObjectURL(image) : uploadImage} alt="" width={98} />
                                </label>
                                <input type="file" className="form-control" id="image" hidden onChange={(e) => setImage(e.target.files[0])} />
                            </div>

                            <div className="mb-3">
                                <label htmlFor="name" className="form-label">Name</label>
                                <input type="text" placeholder='Chicken Biryani' className="form-control" id="name" required name='name' onChange={onChangeHandler} value={food.name} />
                            </div>

                            <div className="mb-3">
                                <label htmlFor="description" className="form-label">Description</label>
                                <textarea className="form-control" placeholder='Write description here...' id="description" rows="5" required name='description' onChange={onChangeHandler} value={food.description}></textarea>
                            </div>

                            <div className="mb-3">
                                <label htmlFor="category" className="form-label">Category</label>
                                <select name="category" id="category" className='form-control' onChange={onChangeHandler} value={food.category} required>
                                    {categories.map(category => (
                                        <option key={category.categoryId} value={category.categoryId}>
                                            {category.categoryName}
                                        </option>
                                    ))}
                                </select>
                            </div>

                            <div className="mb-3">
                                <label htmlFor="price" className="form-label">Price</label>
                                <input type="number" name="price" id="price" placeholder='₹200' className='form-control' onChange={onChangeHandler} value={food.price} />
                            </div>

                            <button type="submit" className="btn btn-primary">Save</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default AddFood;

import React, { useContext, useState } from 'react';
import { toast } from 'react-toastify';
import { addCategory } from '../../../services/categoryService'; // <-- Make sure this exists
import { StoreContext } from '../../../context/StoreContext';

const AddCategory = () => {
    const {setCategories} = useContext(StoreContext);
    const [categoryName, setCategoryName] = useState('');

    const onSubmitHandler = async (event) => {
        event.preventDefault();
        if (!categoryName.trim()) {
            toast.error('Category name is required');
            return;
        }

        try {
            const newCategory = await addCategory(categoryName);
            toast.success('Category added successfully');
            setCategoryName('');

            setCategories(prev => [...prev, newCategory]);
        } catch (error) {
            toast.error('Error adding category');
        }
    };

    return (
        <div className="mx-2 mt-2">
            <div className="row">
                <div className="card col-md-4">
                    <div className="card-body">
                        <h2 className="mb-4">Add Category</h2>
                        <form onSubmit={onSubmitHandler}>
                            <div className="mb-3">
                                <label htmlFor="categoryName" className="form-label">Category Name</label>
                                <input
                                    type="text"
                                    className="form-control"
                                    id="categoryName"
                                    name="categoryName"
                                    placeholder="e.g. Biryani"
                                    value={categoryName}
                                    onChange={(e) => setCategoryName(e.target.value)}
                                    required
                                />
                            </div>
                            <button type="submit" className="btn btn-primary">Save</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default AddCategory;

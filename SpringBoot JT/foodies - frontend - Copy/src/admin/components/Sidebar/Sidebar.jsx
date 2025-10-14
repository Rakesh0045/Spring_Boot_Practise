import React, { useContext } from 'react';
import { Link } from 'react-router-dom';
import { StoreContext } from '../../../context/StoreContext';
import { toast } from 'react-toastify';

const Sidebar = () => {
  return (

    <div className= "border-end bg-white shadow-lg" id="sidebar-wrapper">
        <div className="sidebar-heading border-bottom bg-light">
            <header className='text-center'>
              Tasty Town
            </header>
        </div>
        
        <div className="list-group list-group-flush">
            <Link className="list-group-item list-group-item-action list-group-item-light p-3" to="/admin/orders">
            <i className='bi bi-cart me-2'></i> Orders</Link>

            <Link className="list-group-item list-group-item-action list-group-item-light p-3" to="/admin/add-food">
            <i className='bi bi-plus-circle me-2'></i> Add Food</Link>
            
            <Link className="list-group-item list-group-item-action list-group-item-light p-3" to="/admin/add-category">
            <i className='bi bi-plus-circle me-2'></i> Add Category</Link>
            
            <Link className="list-group-item list-group-item-action list-group-item-light p-3" to="/admin/foods">
              <i className='bi bi-list-ul me-2'></i> List Food
            </Link>
            
            <Link className="list-group-item list-group-item-action list-group-item-light p-3" to="/admin/categories">
            <i className='bi bi-list-ul me-2'></i> List Category</Link>

            <Link className="list-group-item list-group-item-action list-group-item-light p-3" to="/" >
            <i className="bi bi-box-arrow-right me-2"></i> Exit</Link>
        </div>
    </div>
  )
}

export default Sidebar;
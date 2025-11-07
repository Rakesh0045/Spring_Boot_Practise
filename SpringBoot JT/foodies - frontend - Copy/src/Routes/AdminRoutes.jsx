import React from "react";
import Orders from "../admin/pages/Orders/Orders";
import ListFood from "../admin/components/ListFood/ListFood";
import AddFood from "../admin/components/AddFood/AddFood";
import ListCategory from "../admin/components/ListCategory/ListCategory";
import AddCategory from "../admin/components/AddCategory/AddCategory";
import { Route, Routes } from "react-router-dom";
import ErrorPage from "../customer/pages/ErrorPage";
import Sidebar from "../admin/components/sidebar/Sidebar";
import AdminNavbar from "../admin/components/Navbar/AdminNavbar";

const AdminRoutes = () => {
    return (
        <div className="d-flex" id="wrapper">
            <Sidebar />

            <div id="page-content-wrapper">
                <AdminNavbar />

                <div className="container-fluid">
                    <Routes>
                        <Route path="/admin/orders" element={<Orders />} />
                        <Route path="/admin/foods" element={<ListFood />} />
                        <Route path="/admin/add-food" element={<AddFood />} />
                        <Route
                            path="/admin/categories"
                            element={<ListCategory />}
                        />
                        <Route
                            path="/admin/add-category"
                            element={<AddCategory />}
                        />
                        <Route path='*' element={<ErrorPage />} />
                    </Routes>
                </div>
            </div>
        </div>
    );
};

export default AdminRoutes;

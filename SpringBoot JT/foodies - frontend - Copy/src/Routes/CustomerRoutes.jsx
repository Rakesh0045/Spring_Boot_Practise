import React, { useContext } from 'react'
import Home from '../customer/pages/Home/Home'
import ExploreFood from '../customer/pages/ExploreFood/ExploreFood'
import FoodDetails from '../customer/pages/FoodDetails/FoodDetails'
import Login from '../customer/components/Auth/Login'
import Register from '../customer/components/Auth/Register'
import Cart from '../customer/pages/Cart/Cart'
import PlaceOrder from '../customer/pages/PlaceOrder/PlaceOrder'
import MyOrders from '../customer/pages/MyOrders/MyOrders'
import { StoreContext } from '../context/StoreContext'
import Navbar from '../customer/components/Navbar/Navbar'
import { Route, Routes } from 'react-router-dom'
import ErrorPage from '../customer/pages/ErrorPage'

const CustomerRoutes = () => {
    const {token} = useContext(StoreContext)
  return (
    <>
        <Navbar />
        <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/explore" element={<ExploreFood />} />
            <Route path="/food/:id" element={<FoodDetails />} />
            <Route path="/login" element={token ? <Home /> : <Login />} />
            <Route path="/register" element={token ? <Home /> : <Register />} />
            <Route path="/cart" element={<Cart />} />
            <Route path="/order" element={token ? <PlaceOrder /> : <Login />} />
            <Route path="/myOrders" element={token ? <MyOrders /> : <Login />} />
            <Route path='*' element={<ErrorPage />} />
        </Routes>
    </>
  )
}

export default CustomerRoutes
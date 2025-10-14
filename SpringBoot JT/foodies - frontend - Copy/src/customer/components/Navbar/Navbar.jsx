import { useContext, useState } from "react";
import logo from "../../../assets/react.svg"
import { Link, useLocation, useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import { StoreContext } from "../../../context/StoreContext";

const Navbar = () => {
    // Write this after creating Login.jsx file & also after changing the navbar
    const {token, setToken, role, cart, setCart} = useContext(StoreContext)
    const uniqueItemsInCart = cart?.items?.length || 0;

    // for handling logout 
    const onLogoutHandler = async () => {
        localStorage.removeItem("token");
        setToken("")
        setCart([]);
        toast.success("Logout successfull");
        navigate("/");
    };

    const navigate = useNavigate();


    // Normal As usual code
    const location = useLocation();

    return (
        <nav className="navbar navbar-expand-lg bg-body-tertiary">
            
            <div className="container">
                <Link to="/">
                    <img
                        src="/logo.png"
                        alt="#"
                        className="mx-4"
                        height={48}
                        width={48}
                    />
                </Link>
                <button
                    className="navbar-toggler"
                    type="button"
                    data-bs-toggle="collapse"
                    data-bs-target="#navbarSupportedContent"
                    aria-controls="navbarSupportedContent"
                    aria-expanded="false"
                    aria-label="Toggle navigation"
                >
                    <span className="navbar-toggler-icon"></span>
                </button>
                <div
                    className="collapse navbar-collapse"
                    id="navbarSupportedContent"
                >
                    <ul className="navbar-nav me-auto mb-2 mb-lg-0">
                        <li className="nav-item">
                            <Link
                                className={`nav-link ${
                                    location.pathname === "/" ? "active fw-bold" : ""
                                }`}
                                to="/"
                                >
                                Home
                            </Link>
                        </li>
                        <li className="nav-item">
                            <Link
                                className={`nav-link ${
                                    location.pathname === "/explore" ? "active fw-bold" : ""
                                }`}
                                to="/explore"
                                >
                                Explore
                            </Link>
                        </li>
                    </ul>

                    {/* Login & register implemente :- done to be later*/}
                    <div className="d-flex align-items-center gap-4">
                        <Link to={`/cart`}>
                        <div className="position-relative">
                            <img
                            src="/cart.png"
                            alt=""
                            height={28}
                            width={28}
                            className="position-relative"
                            />
                            <span className="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-primary">
                            {uniqueItemsInCart}
                            </span>
                        </div>
                        </Link>
                        
                        {!token ? (
                        <>
                            <button
                            className="btn btn-outline-primary btn-sm"
                            onClick={() => navigate("/login")}
                            >
                            Login
                            </button>
                            <button
                            className="btn btn-outline-success btn-sm"
                            onClick={() => navigate("/register")}
                            >
                            Register
                            </button>
                        </>
                        ) : (
                        <div className="dropdown text-end">
                            <a
                            href="#"
                            className="d-block link-body-emphasis text-decoration-none dropdown-toggle"
                            data-bs-toggle="dropdown"
                            aria-expanded="false"
                            >
                            <img
                                src="/profile.png"
                                alt=""
                                width={32}
                                height={32}
                                className="rounded-circle"
                            />
                            </a>
                            <ul className="dropdown-menu text-small">
                                {role === "ROLE_ADMIN" && (
                                    <li
                                    className="dropdown-item"
                                    onClick={() => navigate("/admin/orders")}
                                    >
                                    Admin Dashboard
                                    </li>
                                )}
                                <li className="dropdown-item" onClick={() => navigate("/myorders")}>
                                    Orders
                                </li>
                                <li className="dropdown-item" onClick={onLogoutHandler}>
                                    Logout
                                </li>
                            </ul>
                        </div>
                        )}
                    </div>
                    {/* implementation end */}

                </div>
            </div>
        </nav>
    );
};

export default Navbar;

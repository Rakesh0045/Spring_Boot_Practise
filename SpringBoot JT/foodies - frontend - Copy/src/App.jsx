import { useContext } from 'react';
import { useLocation } from 'react-router-dom';
import { StoreContext } from './context/StoreContext';
import { ToastContainer } from 'react-toastify';
import AdminRoutes from './Routes/AdminRoutes';
import CustomerRoutes from './Routes/CustomerRoutes';

function App() {
  const { token, role } = useContext(StoreContext);
  const location = useLocation();

  // Check if path starts with /admin
  const isAdminRoute = location.pathname.startsWith('/admin');

  // Optionally restrict access to admin routes
  if (isAdminRoute && (!token || role !== "ROLE_ADMIN")) {
    return  (
      <div className="text-center mt-5">
        Access Denied
      </div> // or redirect to login or home
    )
  }

  return (
    <>
      {isAdminRoute ? <AdminRoutes /> : <CustomerRoutes />}
      <ToastContainer position='top-center' autoClose='1500' />
    </>
  );
}

export default App;

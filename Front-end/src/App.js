import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.js';
import './styles/style.scss';
import React, { Fragment } from "react";
import { Routes, Route, BrowserRouter, Navigate, Outlet } from "react-router-dom";
import Cookies from 'universal-cookie';

import PageNotFound from './pages/404';
import MenuBar from './components/Navbar/Navbar';
import Footer from './components/Footer/Footer';
import SideBar from './components/Navbar/Sidebar';

// user page
import Home from './pages/user-page/Home';
import List from "./pages/user-page/List";
import Detail from "./pages/user-page/Detail";

// admin page
import SignIn from "./pages/admin-page/SignIn";
import Dashboard from "./pages/admin-page/Dashboard";
import ManageBikeCategory from "./pages/admin-page/ManageBikeCategory";
import ManageBikeColor from "./pages/admin-page/ManageBikeColor";
import ManageBikeManufacturer from "./pages/admin-page/ManageBikeManufacturer";
import ManageBikeList from "./pages/admin-page/ManageBikeList";
import ManageBikeCreate from "./pages/admin-page/ManageBikeCreate";
import ManageBikeDetail from "./pages/admin-page/ManageBikeDetail";
import ManageBikeUpdate from "./pages/admin-page/ManageBikeUpdate";
import CreateCart from "./pages/admin-page/CreateCart";
import CreateOrder from "./pages/admin-page/CreateOrder";
import ManageOrderList from "./pages/admin-page/ManageOrderList";
import ManageOrderDetail from "./pages/admin-page/ManageOrderDetail";
import ManageMaintain from "./pages/admin-page/ManageMaintain";
import ManageMaintainCreate from "./pages/admin-page/ManageMaintainCreate";
import ManageMaintainDetail from "./pages/admin-page/ManageMaintainDetail";

// Redux
import { useSelector } from "react-redux";

const cookies = new Cookies();

const ProtectedRoute = ({ token, redirectPath = '/signin' }) => {
	if (!token) {
		return <Navigate to={redirectPath} replace />;
	}
	return <Outlet />;
};

function App() {

	let reduxToken = null;
	let reduxIsShowPublicNavBar = null;

	reduxToken = useSelector((state) => state.reduxAuthenticate.accessToken);
	reduxIsShowPublicNavBar = useSelector((state) => state.reduxAuthenticate.isShowPublicNavBar);

	if (reduxToken == null && cookies.get('accessToken')) {
		reduxToken = cookies.get('accessToken')
	}

	return (
		<Fragment>
			{reduxIsShowPublicNavBar && <div id="content-wrap">
				<BrowserRouter>
					{reduxIsShowPublicNavBar && <MenuBar />}
					{!reduxIsShowPublicNavBar && <SideBar />}
					<Routes>
						<Route path='/signin' exact element={<SignIn />} />
						<Route path='/' exact element={<Home />} />
						<Route path='/list' exact element={<List />} />
						<Route path='/list/manual' exact element={<List category={2} />} />
						<Route path='/list/automatic' exact element={<List category={1} />} />
						<Route path='/bike/:id' element={<Detail />} />
						<Route path='*' element={<Navigate to='/404' />} />
						<Route path='/404' exact element={<PageNotFound warn={"Website is developed"} />} />

						<Route element={<ProtectedRoute token={reduxToken} />}>
							<Route path='/dashboard' exact element={<Dashboard />} />
							<Route path='/manage-bike/bike-list' exact element={<ManageBikeList />} />
							<Route path='/manage-bike/bike-create' exact element={<ManageBikeCreate />} />
							<Route path='/manage-bike/bike-update/:id' exact element={<ManageBikeUpdate />} />
							<Route path='/manage-bike/bike/:id' element={<ManageBikeDetail />} />
							<Route path='/manage-bike/category' exact element={<ManageBikeCategory />} />
							<Route path='/manage-bike/color' exact element={<ManageBikeColor />} />
							<Route path='/manage-bike/manufacturer' exact element={<ManageBikeManufacturer />} />
							<Route path='/manage-order/cart-create' exact element={<CreateCart />} />
							<Route path='/manage-order/order-list' exact element={<ManageOrderList />} />
							<Route path='/manage-order/order-create' exact element={<CreateOrder />} />
							<Route path='/manage-order/order/:id' element={<ManageOrderDetail />} />
							<Route path='/manage-maintenance/maintenance-list' exact element={<ManageMaintain />} />
							<Route path='/manage-maintenance/maintenance-create' exact element={<ManageMaintainCreate />} />
							<Route path='/manage-maintenance/maintenance/:id' element={<ManageMaintainDetail />} />
						</Route>
					</Routes>
				</BrowserRouter>
			</div>}
			{!reduxIsShowPublicNavBar && <BrowserRouter>
				{reduxIsShowPublicNavBar && <MenuBar />}
				{!reduxIsShowPublicNavBar && <SideBar />}
				<Routes>
					<Route path='/signin' exact element={<SignIn />} />
					<Route path='/' exact element={<Home />} />
					<Route path='/list' exact element={<List />} />
					<Route path='/list/manual' exact element={<List category={2} />} />
					<Route path='/list/automatic' exact element={<List category={1} />} />
					<Route path='/bike/:id' element={<Detail />} />
					<Route path='*' element={<Navigate to='/404' />} />
					<Route path='/404' exact element={<PageNotFound warn={"Website is developed"} />} />

					<Route element={<ProtectedRoute token={reduxToken} />}>
						<Route path='/dashboard' exact element={<Dashboard />} />
						<Route path='/manage-bike/bike-list' exact element={<ManageBikeList />} />
						<Route path='/manage-bike/bike-create' exact element={<ManageBikeCreate />} />
						<Route path='/manage-bike/bike-update/:id' exact element={<ManageBikeUpdate />} />
						<Route path='/manage-bike/bike/:id' element={<ManageBikeDetail />} />
						<Route path='/manage-bike/category' exact element={<ManageBikeCategory />} />
						<Route path='/manage-bike/color' exact element={<ManageBikeColor />} />
						<Route path='/manage-bike/manufacturer' exact element={<ManageBikeManufacturer />} />
						<Route path='/manage-order/cart-create' exact element={<CreateCart />} />
						<Route path='/manage-order/order-list' exact element={<ManageOrderList />} />
						<Route path='/manage-order/order-create' exact element={<CreateOrder />} />
						<Route path='/manage-order/order/:id' element={<ManageOrderDetail />} />
						<Route path='/manage-maintenance/maintenance-list' exact element={<ManageMaintain />} />
						<Route path='/manage-maintenance/maintenance-create' exact element={<ManageMaintainCreate />} />
						<Route path='/manage-maintenance/maintenance/:id' element={<ManageMaintainDetail />} />
					</Route>
				</Routes>
			</BrowserRouter>}
			{reduxIsShowPublicNavBar && <Footer />}
		</Fragment>
	)
}

export default App;

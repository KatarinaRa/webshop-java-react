import React, { Component } from "react";
import { Routes, Route, Link } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";
import "./App.css";
import AddColor from './components/AddColor';
import AddProduct from './components/AddProduct';
import AddSize from './components/AddSize';
import AddProductCategory from './components/AddProductCategory';
import ProductList from './components/ProductList';
import CartPage from './components/CartPage';
import AllOrders from './components/AllOrders';
import UserOrders from './components/UserOrders';
import ProductDetails from './components/ProductDetails';

import AuthService from "./services/auth.service";

import Login from "./components/login.component";
import Register from "./components/register.component";
import Home from "./components/home.component";
import Profile from "./components/profile.component";
import BoardUser from "./components/board-user.component";
import BoardAdmin from "./components/board-admin.component";
import OrderSuccess from './components/OrderSuccess';
import Stat from './components/Stat';


class App extends Component {
  constructor(props) {
    super(props);
    this.logOut = this.logOut.bind(this);

    this.state = {
      showAdminBoard: false,
      currentUser: undefined,
    };
  }

  componentDidMount() {
    const user = AuthService.getCurrentUser();

    if (user) {
      this.setState({
        currentUser: user,
        showAdminBoard: user.roles.includes("ROLE_ADMIN"),
      });
    }
  }

  logOut() {
    AuthService.logout();
    this.setState({
      showAdminBoard: false,
      currentUser: undefined,
    });
  }

  render() {
    const { currentUser, showAdminBoard } = this.state;

    return (
      <div>
        <nav className="navbar navbar-expand-lg navbar-light bg-light">
          <Link to={"/"} className="navbar-brand">
            <span style={{ color: 'blue', fontWeight: 'bold' }}>P</span>ien
          </Link>
          <div className="navbar-nav mr-auto">
            <li className="nav-item">
              <Link to={"/home"} className="nav-link">
                Home
              </Link>
            </li>

            {showAdminBoard && (
              <>
          
          
                <li className="nav-item">
                  <Link to={"/admin/colors"} className="nav-link">
                    Boje
                  </Link>
                </li>
                <li className="nav-item">
                  <Link to={"/admin/add-product"} className="nav-link">
                    Novi proizvodi
                  </Link>
                </li>
                <li className="nav-item">
                  <Link to={"/admin/sizes"} className="nav-link">
                    Velicine
                  </Link>
                </li>
                <li className="nav-item">
                  <Link to={"/admin/productscategory"} className="nav-link">
                    Kategorije
                  </Link>
                </li>
                <li className="nav-item">
                <Link to={"/admin/all-orders"} className="nav-link">
                  Useri
                </Link>
                <Link to={"/stats"} className="nav-link">
                  Stats
                </Link>
    </li>
              </>
            )}
            {currentUser && (
              <li className="nav-item">
                <div className="nav-links">
                  <Link to={"/products"} className="nav-link">
                    Proizvodi
                  </Link>
                  <Link to={"/cart"} className="nav-link">
                    Kosarica 
                  </Link>
                  <Link to={"/userorders"} className="nav-link">
                    Moje narudžbe
                  </Link>
                </div>
              </li>
            )}
          </div>

          {currentUser ? (
            <div className="navbar-nav ml-auto">
              <li className="nav-item">
                <Link to={"/profile"} className="nav-link">
                 {currentUser.username}
                </Link>
              </li>
              <li className="nav-item">
                <a href="/login" className="nav-link" onClick={this.logOut}>
                  LogOut
                </a>
              </li>

            </div>
          ) : (
            <div className="navbar-nav ml-auto">
              <li className="nav-item">
                <Link to={"/login"} className="nav-link">
                  Login
                </Link>
              </li>

              <li className="nav-item">
                <Link to={"/register"} className="nav-link">
                  Sign Up
                </Link>
              </li>
            </div>
          )}
        </nav>

        <div className="container mt-3">
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/home" element={<Home />} />
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />
            <Route path="/profile" element={<Profile />} />
            <Route path="/user" element={<BoardUser />} />
            <Route path="/admin/colors" element={<AddColor />} />
            <Route path="/admin/add-product" element={<AddProduct />} />
            <Route path="/admin/sizes" element={<AddSize />} />
            <Route path="/admin/productscategory" element={<AddProductCategory />} />
            <Route path="/products" element={<ProductList />} />
            <Route path="/cart" element={<CartPage />} />
            <Route path="/admin" element={<BoardAdmin />} />
            <Route path="/order-success" element={<OrderSuccess />} />
            <Route path="/admin/all-orders" element={<AllOrders />} />
            <Route path="/userorders" element={<UserOrders />} />
            <Route path="/product/:id" element={<ProductDetails />} />
            <Route path="/stats" element={<Stat/>} />


          </Routes>
        </div>
      </div>
    );
  }
}

export default App;
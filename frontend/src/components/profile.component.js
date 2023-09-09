import React, { Component } from "react";
import { Navigate } from "react-router-dom";
import AuthService from "../services/auth.service";
import "./Profile.css"; 

class Profile extends Component {
  state = {
    redirect: null,
    userReady: false,
    currentUser: { username: "" }
  };

  componentDidMount() {
    const currentUser = AuthService.getCurrentUser();

    if (!currentUser) {
      this.setState({ redirect: "/home" });
    } else {
      this.setState({ currentUser: currentUser, userReady: true });
    }
  }

  render() {
    const { redirect, userReady, currentUser } = this.state;

    if (redirect) {
      return <Navigate to={redirect} />;
    }

    return (
      <div className="profile-container">
        {userReady && (
          <div className="profile-jumbotron fade-in">
            <h3 className="profile-header">Bok, {currentUser.username}!</h3>
            
        
            <p className="profile-quote">
              "Moda je umjetnost koja vam omogućuje da izrazite tko ste bez riječi."
              <br /> - Rachel Zoe
            </p>
          </div>
        )}
      </div>
    );
  }
}

export default Profile;

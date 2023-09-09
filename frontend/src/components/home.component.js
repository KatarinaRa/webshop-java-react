import React, { Component } from "react";
import UserService from "../services/user.service";

export default class Home extends Component {
  constructor(props) {
    super(props);

    this.state = {
      content: ""
    };
  }

  componentDidMount() {
    UserService.getPublicContent().then(
      response => {
        this.setState({
          content: response.data
        });
      },
      error => {
        this.setState({
          content:
            (error.response && error.response.data) ||
            error.message ||
            error.toString()
        });
      }
    );
  }

  render() {
    return (
      <div className="container">
        <header className="jumbotron">
          <h3>{this.state.content}</h3>
          <div className="image-container">
            <img src="https://i.pinimg.com/564x/b9/b7/fa/b9b7fa39272359e4a55ae40fc0631a41.jpg" alt="Slika 1" style={{ display: 'inline-block', marginRight: '10px' }} />
          </div>
          <style>
            {`
              .image-container img {
                display: inline-block;
                margin-right: 10px;
              }
            `}
          </style>
        </header>
      </div>
    );
  }
}
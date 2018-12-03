/**
 * Report Simplify App With Firebase Authentication
 *
 * @creator: Jerry Chong
 * @format
 * @flow
 */
//*****React Native Firebase Authentication Email Authentication Sample*****
import React, { Component } from 'react';
import firebase from 'react-native-firebase';
import Navigation from "./Navigation.js";

export default class App extends React.Component {

  constructor() {
    super();
    // this.authSubscription = null;

    this.state = {
      loading: false,
    };
  }

  /**
   * Listen for any auth state changes and update component state
   */
  componentDidMount() {
    // this.authSubscription = firebase.auth().onAuthStateChanged((user) => {
    //   this.setState({ 
    //     loading: false,
    //     user,
    //   });
    // });

    // Need To Check On Above Codes!!!!!!!!!!!!!!!!!!!
  }

    /**
   * Stop listen for any auth state changes when component is unmounted
   */
  componentWillUnmount() {
    this.authSubscription();
  }

  render() {
    
    // // Check if still loading
    // if (this.state.loading) {
    //   console.debug("App.js - Loading");
    //   return null;
    // }

    // Chech if user is logined
    if (!this.state.user) {
      console.debug("App.js - User Loggin In");
      return <Navigation />;      // Login
    }
  }

}
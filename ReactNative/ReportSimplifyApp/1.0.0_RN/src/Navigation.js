// From HomeScreen index.js
import React from "react";
import { DrawerNavigator } from "react-navigation";

// Import Screen
import HomeScreen from "./HomeScreen.js";
import MainScreenNavigator from "./ChatScreen.js";
import ProfileScreen from "./ProfileScreen.js";
import SideBar from "./SideBar.js";
import LoginScreen from './Login.js';       // Added this line

// From HomeScreen index.js`
const NavigationRouter = DrawerNavigator(
    {
      Login: { screen: LoginScreen },           // Added this line
      // Home: { screen: HomeScreen },
      // Chat: { screen: MainScreenNavigator },
      // Profile: { screen: ProfileScreen }
    },
    {
      contentComponent: props => <SideBar {...props} />
    }
  );
  
export default NavigationRouter;
// From HomeScreen index.js
import React from "react";
import HomeScreen from "./HomeScreen.js";
import MainScreenNavigator from "./ChatScreen.js";
import ProfileScreen from "./ProfileScreen.js";
import SideBar from "./SideBar.js";
import { DrawerNavigator } from "react-navigation";

// From HomeScreen index.js
const NavigationRouter = DrawerNavigator(
    {
      Home: { screen: HomeScreen },
      Chat: { screen: MainScreenNavigator },
      Profile: { screen: ProfileScreen }
    },
    {
      contentComponent: props => <SideBar {...props} />
    }
  );
  
export default NavigationRouter;
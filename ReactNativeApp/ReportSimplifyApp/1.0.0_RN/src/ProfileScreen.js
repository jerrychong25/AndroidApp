import React from "react";
import { Alert } from "react-native";
import { Container, Header, Left, Body, Title, Card, CardItem, Content, Right, Icon, Button, Text } from "native-base";
import { StackNavigator } from "react-navigation";
import EditScreenOne from "./EditScreenOne.js";
// import EditScreenTwo from "./EditScreenTwo.js";

// From ProfileScreen index.js
// import React, { Component } from "react";
// import Profile from "./Profile.js";
// import EditScreenOne from "./EditScreenOne.js";
// import EditScreenTwo from "./EditScreenTwo.js";
// import { StackNavigator } from "react-navigation";

export default class Profile extends React.Component {
  componentDidMount() {
    Alert.alert("No Users Found", "Oops, Looks like you are not signed in");
  }
  render() {
    return (
      <Container>
        <Content padder>
          <Card>
            <CardItem>
              <Icon active name="paper-plane" />
              <Text>Show User profiles here</Text>
              <Right>
                <Icon name="close" />
              </Right>
            </CardItem>
          </Card>
          <Button full rounded primary
            style={{ marginTop: 10 }}
            onPress={() => this.props.navigation.navigate("EditScreenOne")}>
            <Text>Goto EditScreen One</Text>
          </Button>
        </Content>
      </Container>
    );
  }
}

// Profile.navigationOptions = ({ navigation }) => ({
//   header: (
//     <Header>
//       <Left>
//         <Button transparent onPress={() => navigation.navigate("DrawerOpen")}>
//           <Icon name="menu" />
//         </Button>
//       </Left>
//       <Body>
//         <Title>Profile</Title>
//       </Body>
//       <Right />
//     </Header>
//   )
// });

// // From ProfileScreen index.js
// // export default (DrawNav = StackNavigator({
// //   Profile: { screen: Profile },
// //   EditScreenOne: { screen: EditScreenOne },
// //   EditScreenTwo: { screen: EditScreenTwo }
// // }));

// export const DrawNav = StackNavigator({
//   Profile: { screen: Profile },
//   EditScreenOne: { screen: EditScreenOne },
//   EditScreenTwo: { screen: EditScreenOne }
// });
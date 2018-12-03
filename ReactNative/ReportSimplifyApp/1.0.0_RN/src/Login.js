import React, { Component, ToastAndroid } from 'react';
import { View, TextInput, Image } from 'react-native';
import { Container, Content, Button, Text, Form, Item, Input, Label } from 'native-base';
import firebase from 'react-native-firebase';
import ChatScreen from './ChatScreen';

export default class Login extends React.Component {

  constructor(props){
    super(props);
 
    this.state = {
       email: null,
       password: null,
    }
 }
    // constructor() {
    //   super();
    //   this.unsubscriber = null;
    //   this.state = {
    //     loading: false,
    //   };
    // }
  
    // /**
    //  * Listen for any auth state changes and update component state
    //  */
    // componentDidMount() {
    //   this.unsubscriber = firebase.auth().onAuthStateChanged((user) => {
    //     this.setState({ 
    //       loading: false,
    //       user,
    //     });
    //   });
    // }
  
    // componentWillUnmount() {
    //   if (this.unsubscriber) {
    //     this.unsubscriber();
    //   }
    // }
  
    onLogin = () => {
        const { email, password } = this.state;

        Email = email;
        Password = password;

        console.log("************Before Send Firebase Host*****************");
        console.log("email value: ");
        console.log(Email);
        console.log("password value: ");
        console.log(Password);

        firebase.auth().signInWithEmailAndPassword(Email, Password)
        // firebase.auth().signInAndRetrieveDataWithEmailAndPassword(Email, Password)
          .then((user) => {
            // If you need to do anything with the user, do it here
            // The user will be logged in automatically by the 
            // `onAuthStateChanged` listener we set up in App.js earlier

            console.debug("Login.js - onLogin Success");
            ToastAndroid.show('Login.js - onLogin Success', ToastAndroid.SHORT);

            this.props.navigation.navigate("ChatScreen");

            // return <ChatScreen />;
          })
          .catch((error) => {
            const { code, message } = error;
            // For details of error codes, see the docs
            // The message contains the default Firebase string
            // representation of the error

            console.debug("Login.js - onLogin Error");
            ToastAndroid.show('Login.js - onLogin Error', ToastAndroid.SHORT);
          });
      }

    onSignup = () => {
        const { email, password } = this.state;

        // email = this.state.Email;
        // password = this.state.Password;

        firebase.auth().createUserWithEmailAndPassword(email, password)
          .then((user) => {
            // If you need to do anything with the user, do it here
            // The user will be logged in automatically by the
            // `onAuthStateChanged` listener we set up in App.js earlier
          })
          .catch((error) => {
            // const { code, message } = error;
            // For details of error codes, see the docs
            // The message contains the default Firebase string
            // representation of the error
            this.setState({ message: `Sign In Error: ${error.message}` })
          });
      }

    render() {
    //   if (!this.state.user) {
    //     return <Login />;
    //   }
  
    const { email, password } = this.state;

    return (
        <Container>
          <Content contentContainerStyle={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>
            <Form>
              <Image 
                source={require('../img/home_icon.png')} 
                style={{ 
                  height: 200, width: 200, 
                  alignSelf: 'center',
                }}
                />
              <Label
                style={{ 
                  height: 40, 
                  fontSize: 25,
                  fontWeight: 'bold',
                  alignSelf: 'center',
                  marginTop: 10, marginBottom: 1 }}
                >
                Report Simplify
              </Label>
              <Label
                style={{ height: 40, marginLeft: 15, marginRight: 15, marginTop: 15, marginBottom: 1 }}
                >
                Email
              </Label>
              <Item rounded
                style={{ 
                  height: 50, width: 330, 
                  alignSelf: 'center',
                  marginBottom: 10 }}
                >
                <Input autoFocus
                  style={{  marginLeft: 15, marginRight: 15 }}
                  onChangeText={value => this.setState({ email: value })}
                  value={email}
                />
              </Item>
              <Label
                style={{ height: 40, marginLeft: 15, marginRight: 15, marginTop: 15, marginBottom: 1 }}
                >
                Password
              </Label>
              <Item rounded
                style={{ 
                  height: 50, width: 330, 
                  alignSelf: 'center',
                  marginBottom: 10 }}
                >
                <Input autoFocus
                  style={{  marginLeft: 15, marginRight: 15 }}
                  onChangeText={value => this.setState({ password: value })}
                  value={password}
                />
              </Item>
            </Form>
            <Button rounded medium full
              style={{ margin:15, backgroundColor: '#66BB6A', marginBottom: 5  }}
              onPress={this.onLogin} 
              >
              <Text>Login</Text>
            </Button>
            <Button rounded medium full
              style={{ margin:15, backgroundColor: '#66BB6A' }}
              onPress={this.onSignup} 
              >
              <Text>Sign Up</Text>
            </Button>
          </Content>
        </Container>
      );
    }
  
}

// Backup For Phone Authentication
// export default class PhoneAuthTest extends Component {
//   constructor(props) {
//     super(props);
//     this.unsubscribe = null;
//     this.state = {
//       user: null,
//       message: '',
//       codeInput: '',
//       phoneNumber: '+60',
//       confirmResult: null,
//     };
//   }

//   componentDidMount() {
//     this.unsubscribe = firebase.auth().onAuthStateChanged((user) => {
//       if (user) {
//         this.setState({ user: user.toJSON() });
//       } else {
//         // User has been signed out, reset the state
//         this.setState({
//           user: null,
//           message: '',
//           codeInput: '',
//           phoneNumber: '+60',
//           confirmResult: null,
//         });
//       }
//     });
//   }

//   componentWillUnmount() {
//      if (this.unsubscribe) this.unsubscribe();
//   }

//   signIn = () => {
//     const { phoneNumber } = this.state;
//     this.setState({ message: 'Sending code ...' });

//     firebase.auth().signInWithPhoneNumber(phoneNumber)
//       .then(confirmResult => this.setState({ confirmResult, message: 'Code has been sent!' }))
//       .catch(error => this.setState({ message: `Sign In With Phone Number Error: ${error.message}` }));
//   };

//   confirmCode = () => {
//     const { codeInput, confirmResult } = this.state;

//     if (confirmResult && codeInput.length) {
//       confirmResult.confirm(codeInput)
//         .then((user) => {
//           this.setState({ message: 'Code Confirmed!' });
//         })
//         .catch(error => this.setState({ message: `Code Confirm Error: ${error.message}` }));
//     }
//   };

//   signOut = () => {
//     firebase.auth().signOut();
//   }

//   renderPhoneNumberInput() {
//    const { phoneNumber } = this.state;

//     return (
//       <Container>
//           <Content contentContainerStyle={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>
//             <Form>
//               <Image 
//                 source={require('./img/home_icon.png')} 
//                 style={{ 
//                   height: 200, width: 200, 
//                   alignSelf: 'center',
//                   // marginLeft: 15, marginRight: 15, marginTop: 15, marginBottom: 5 
//                 }}
//                 />
//               <Label
//                 style={{ 
//                   height: 40, 
//                   fontSize: 25,
//                   fontWeight: 'bold',
//                   alignSelf: 'center',
//                   marginTop: 10, marginBottom: 5 }}
//                 >
//                 Report Simplify
//               </Label>
//               <Label
//                 style={{ height: 40, marginLeft: 15, marginRight: 15, marginTop: 15, marginBottom: 5 }}
//                 >
//                 Phone Number
//               </Label>
//               <Item rounded
//                 style={{ 
//                   height: 50, width: 330, 
//                   alignSelf: 'center',
//                   marginBottom: 10 }}
//                 >
//                 <Input autoFocus
//                   style={{  marginLeft: 15, marginRight: 15 }}
//                   onChangeText={value => this.setState({ phoneNumber: value })}
//                   value={phoneNumber}
//                 />
//               </Item>
//             </Form>
//             <Button rounded medium full
//              style={{ margin:15, backgroundColor: '#66BB6A', marginBottom: 5  }}
//              onPress={this.signIn} 
//               >
//               <Text>Sign In</Text>
//             </Button>
//             <Button rounded medium full
//              style={{ margin:15, backgroundColor: '#66BB6A' }}
//               >
//               <Text>Forgot Password</Text>
//             </Button>
//         </Content>
//       </Container>
//     );
//   }

//   renderMessage() {
//     const { message } = this.state;

//     if (!message.length) return null;

//     return (
//       <Text style={{ padding: 5, backgroundColor: '#000', color: '#fff' }}>{message}</Text>
//     );
//   }

//   renderVerificationCodeInput() {
//     const { codeInput } = this.state;

//     return (
//       <View style={{ marginTop: 25, padding: 25 }}>
//         <Text>Enter verification code below:</Text>
//         <TextInput
//           autoFocus
//           style={{ height: 40, marginTop: 15, marginBottom: 15 }}
//           onChangeText={value => this.setState({ codeInput: value })}
//           placeholder={'Code ... '}
//           value={codeInput}
//         />
//         <Button title="Confirm Code" color="#841584" onPress={this.confirmCode} />
//       </View>
//     );
//   }

//   render() {
//     const { user, confirmResult } = this.state;
//     return (
//       <View style={{ flex: 1 }}>

//         {!user && !confirmResult && this.renderPhoneNumberInput()}

//         {this.renderMessage()}

//         {!user && confirmResult && this.renderVerificationCodeInput()}

//         {user && (
//           <View
//             style={{
//               padding: 15,
//               justifyContent: 'center',
//               alignItems: 'center',
//               backgroundColor: '#77dd77',
//               flex: 1,
//             }}
//           >
//             <Image source={{ uri: successImageUri }} style={{ width: 100, height: 100, marginBottom: 25 }} />
//             <Text style={{ fontSize: 25 }}>Signed In!</Text>
//             <Text>{JSON.stringify(user)}</Text>
//             <Button title="Sign Out" color="red" onPress={this.signOut} />
//           </View>
//         )}
//       </View>
//     );
//   }
// }
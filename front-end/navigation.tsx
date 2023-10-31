import { NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import React from 'react';
import loginScreen from "./screens/loginScreen";
import listsScreen from './screens/listsScreen';
import welcome from './screens/welcome';
import signupScreen from './screens/signupScreen';

const {Navigator, Screen} = createNativeStackNavigator();

const AppNavigator =()=> (
      <NavigationContainer>
        <Navigator
        initialRouteName="Welcome"
        screenOptions={{headerShown: false}}>
        <Screen name="Welcome" component={welcome}/>
        <Screen name="Login" component={loginScreen}/>
        <Screen name="Lists" component={listsScreen}/>
        <Screen name="SignUp" component={signupScreen}/>
        </Navigator>
      </NavigationContainer>
  )
  export default AppNavigator;
  
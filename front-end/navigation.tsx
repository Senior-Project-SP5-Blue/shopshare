import {NavigationContainer} from '@react-navigation/native';
import {createNativeStackNavigator} from '@react-navigation/native-stack';
import React from 'react';
import Welcome from './screens/Welcome';
import LoginScreen from './screens/LoginScreen';
import ListsScreen from './screens/ListsScreen';
import SignupScreen from './screens/SignupScreen';

const {Navigator, Screen} = createNativeStackNavigator();

const AppNavigator = () => (
  <NavigationContainer>
    <Navigator initialRouteName="Welcome" screenOptions={{headerShown: false}}>
      <Screen name="Welcome" component={Welcome} />
      <Screen name="Login" component={LoginScreen} />
      <Screen name="Lists" component={ListsScreen} />
      <Screen name="SignUp" component={SignupScreen} />
    </Navigator>
  </NavigationContainer>
);
export default AppNavigator;

import { NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import React from 'react';
import loginScreen from "./screens/loginScreen";
import listsScreen from './screens/listsScreen';
import welcome from './screens/welcome';
import signupScreen from './screens/signupScreen';
import CreateListsScreen from './screens/CreateListsScreen';
import ShopScreen from './screens/ShopScreen'; 
import { AccessibilityInfo } from 'react-native';
import AccountsScreen from './screens/AccountsScreen';
import Tabs from './components/Tab';
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
        <Screen name="CreateListsScreen" component={CreateListsScreen}/>
        <Screen name="ShopScreen" component={ShopScreen}/>
        <Screen name="AccountsScreen" component={AccountsScreen}/>
        </Navigator>
      </NavigationContainer>
  )
  export default AppNavigator;
  
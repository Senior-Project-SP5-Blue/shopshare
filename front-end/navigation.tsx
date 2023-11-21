import {NavigationContainer} from '@react-navigation/native';
import {createNativeStackNavigator} from '@react-navigation/native-stack';
import React from 'react';

import CreateListsScreen from './screens/CreateListsScreen';
import ShopScreen from './screens/ShopScreen';
import AccountsScreen from './screens/AccountsScreen';
import Welcome from './screens/Welcome';
import LoginScreen from './screens/LoginScreen';
import ListsScreen from './screens/ListsScreen';
import SignupScreen from './screens/SignupScreen';
import EmailConfirmationScreen from './screens/EmailConfirmationScreen';
import {createBottomTabNavigator} from '@react-navigation/bottom-tabs';
import {useSelector} from 'react-redux';
import {selectCurrentUser} from './redux/slices/authSlice';
import GroupsScreen from './screens/GroupsScreen';
const Stack = createNativeStackNavigator();
const Tab = createBottomTabNavigator();
const ListsStack = createNativeStackNavigator();
const AccountStack = createNativeStackNavigator();
const GroupsStack = createNativeStackNavigator();

const ListStackScreen: React.FC = () => {
  return (
    <ListsStack.Navigator screenOptions={{headerShown: false}}>
      <ListsStack.Screen name="Lists" component={ListsScreen} />
      <ListsStack.Screen
        name="CreateListsScreen"
        component={CreateListsScreen}
      />
      <ListsStack.Screen name="ShopScreen" component={ShopScreen} />
    </ListsStack.Navigator>
  );
};

const AccountStackScreen: React.FC = () => {
  return (
    <AccountStack.Navigator screenOptions={{headerShown: false}}>
      <AccountStack.Screen name="AccountsScreen" component={AccountsScreen} />
    </AccountStack.Navigator>
  );
};

const GroupsStackScreen: React.FC = () => {
  return (
    <GroupsStack.Navigator screenOptions={{headerShown: false}}>
      <GroupsStack.Screen name="GroupsScreen" component={GroupsScreen} />
    </GroupsStack.Navigator>
  );
};

const WelcomeStackNav: React.FC = () => {
  return (
    <Stack.Navigator
      initialRouteName="Welcome"
      screenOptions={{headerShown: false}}>
      <Stack.Screen name="Welcome" component={Welcome} />
      <Stack.Screen name="SignUp" component={SignupScreen} />
      <Stack.Screen name="Login" component={LoginScreen} />
      <Stack.Screen
        name="EmailConfirmation"
        component={EmailConfirmationScreen}
      />
    </Stack.Navigator>
  );
};

const MainStackNav: React.FC = () => {
  return (
    <Tab.Navigator screenOptions={{headerShown: false}}>
      <Tab.Screen
        name="ListsStack"
        component={ListStackScreen}
        options={{tabBarLabel: 'Lists'}}
      />
      <Tab.Screen
        name="GroupsStack"
        component={GroupsStackScreen}
        options={{tabBarLabel: 'Groups'}}
      />
      <Tab.Screen
        name="AccountStack"
        component={AccountStackScreen}
        options={{tabBarLabel: 'Account'}}
      />
    </Tab.Navigator>
  );
};

const AppNavigator: React.FC = () => {
  const isSignedIn: boolean = useSelector(selectCurrentUser) != null;

  return (
    <NavigationContainer>
      <Stack.Navigator screenOptions={{headerShown: false}}>
        {isSignedIn ? (
          <Stack.Screen name="MainStack" component={MainStackNav} />
        ) : (
          <Stack.Screen name="WelcomeStack" component={WelcomeStackNav} />
        )}
      </Stack.Navigator>
    </NavigationContainer>
  );
};
export default AppNavigator;

import {NavigationContainer} from '@react-navigation/native';
import {createNativeStackNavigator} from '@react-navigation/native-stack';
import React from 'react';

import ShopScreen from './screens/ShopScreen';
import AccountsScreen from './screens/AccountsScreen';
import Welcome from './screens/WelcomeScreen';
import LoginScreen from './screens/LoginScreen';
import ListsScreen from './screens/ListsScreen';
import SignupScreen from './screens/SignupScreen';
import EmailConfirmationScreen from './screens/EmailConfirmationScreen';
import {createBottomTabNavigator} from '@react-navigation/bottom-tabs';
import {useSelector} from 'react-redux';
import {selectCurrentUser} from './redux/slices/authSlice';
import GroupsScreen from './screens/GroupsScreen';
import CreateListScreen from './screens/CreateListScreen';
import {
  AuthStackParamList,
  ListStackParamList,
  MainTabParamList,
  RootStackParamList,
} from './screens/types';
import ListScreen from './screens/ListScreen';

const Stack = createNativeStackNavigator<RootStackParamList>();
const MainTab = createBottomTabNavigator<MainTabParamList>();
const AuthStack = createNativeStackNavigator<AuthStackParamList>();

const ListsStack = createNativeStackNavigator<ListStackParamList>();
const AccountStack = createNativeStackNavigator();
const GroupsStack = createNativeStackNavigator();

const ListStackScreen: React.FC = () => {
  return (
    <ListsStack.Navigator screenOptions={{headerShown: false}}>
      <ListsStack.Screen name="Lists" component={ListsScreen} />
      <ListsStack.Screen name="CreateListScreen" component={CreateListScreen} />
      <ListsStack.Screen
        name="List"
        component={ListScreen}
        options={{presentation: 'modal'}}
      />
      <ListsStack.Screen name="ShopScreen" component={ShopScreen} />
    </ListsStack.Navigator>
  );
};

const AccountStackScreen: React.FC = () => {
  return (
    <AccountStack.Navigator screenOptions={{headerShown: false}}>
      <AccountStack.Screen name="Accounts" component={AccountsScreen} />
    </AccountStack.Navigator>
  );
};

const GroupsStackScreen: React.FC = () => {
  return (
    <GroupsStack.Navigator screenOptions={{headerShown: false}}>
      <GroupsStack.Screen name="Groups" component={GroupsScreen} />
    </GroupsStack.Navigator>
  );
};

const AuthStackScreens: React.FC = () => {
  return (
    <AuthStack.Navigator
      initialRouteName="Welcome"
      screenOptions={{headerShown: false}}>
      <AuthStack.Screen name="Welcome" component={Welcome} />
      <AuthStack.Screen name="SignUp" component={SignupScreen} />
      <AuthStack.Screen name="Login" component={LoginScreen} />
      <AuthStack.Screen
        name="EmailConfirmation"
        component={EmailConfirmationScreen}
      />
    </AuthStack.Navigator>
  );
};

const MainStackScreens: React.FC = () => {
  return (
    <MainTab.Navigator screenOptions={{headerShown: false}}>
      <MainTab.Screen
        name="ListsStack"
        component={ListStackScreen}
        options={{tabBarLabel: 'Lists'}}
      />
      <MainTab.Screen
        name="GroupsStack"
        component={GroupsStackScreen}
        options={{tabBarLabel: 'Groups'}}
      />
      <MainTab.Screen
        name="AccountStack"
        component={AccountStackScreen}
        options={{tabBarLabel: 'Account'}}
      />
    </MainTab.Navigator>
  );
};

const AppNavigator: React.FC = () => {
  const isSignedIn: boolean = useSelector(selectCurrentUser) != null;

  return (
    <NavigationContainer>
      <Stack.Navigator screenOptions={{headerShown: false}}>
        {isSignedIn ? (
          <Stack.Screen name="MainStack" component={MainStackScreens} />
        ) : (
          <Stack.Screen name="WelcomeStack" component={AuthStackScreens} />
        )}
      </Stack.Navigator>
    </NavigationContainer>
  );
};
export default AppNavigator;

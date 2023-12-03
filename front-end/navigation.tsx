import {NavigationContainer} from '@react-navigation/native';
import {createNativeStackNavigator} from '@react-navigation/native-stack';
import React from 'react';

import {createBottomTabNavigator} from '@react-navigation/bottom-tabs';
import {Alert, StyleSheet, Text, TouchableOpacity, View} from 'react-native';
import PencilSquare from 'react-native-heroicons/mini/PencilSquareIcon';
import Toast from 'react-native-toast-message';
import {useSelector} from 'react-redux';
import COLORS from './constants/colors';
import {selectCurrentUser} from './redux/slices/authSlice';
import AccountsScreen from './screens/AccountsScreen';
import CreateListScreen from './screens/CreateListScreen';
import EditGroupScreen from './screens/EditGroupScreen';
import EditListScreen from './screens/EditListScreen';
import EmailConfirmationScreen from './screens/EmailConfirmationScreen';
import GroupScreen from './screens/GroupScreen';
import GroupsScreen from './screens/GroupsScreen';
import ListScreen from './screens/ListScreen';
import ListsScreen from './screens/ListsScreen';
import LoginScreen from './screens/LoginScreen';
import Requests from './screens/Requests';
import SignupScreen from './screens/SignupScreen';
import Welcome from './screens/WelcomeScreen';
import {
  AuthStackParamList,
  GroupStackParamList,
  GroupsStackParamList,
  ListStackParamList,
  ListsStackParamList,
  MainTabParamList,
  RootStackParamList,
} from './screens/types';

const Stack = createNativeStackNavigator<RootStackParamList>();
const MainTab = createBottomTabNavigator<MainTabParamList>();
const AuthStack = createNativeStackNavigator<AuthStackParamList>();

const ListsStack = createNativeStackNavigator<ListsStackParamList>();
const AccountStack = createNativeStackNavigator();
const GroupsStack = createNativeStackNavigator<GroupsStackParamList>();

const ListStack = createNativeStackNavigator<ListStackParamList>();
const GroupStack = createNativeStackNavigator<GroupStackParamList>();

interface ScreenTitleProps {
  title?: string;
}

export const ScreenTitle: React.FC<ScreenTitleProps> = props => {
  return (
    <View style={styles.screenTitle}>
      <Text style={styles.titleText}>{props.title}</Text>
    </View>
  );
};

const renderEditButton = () => (
  <TouchableOpacity onPress={() => Alert.alert('You clicked')}>
    {<PencilSquare color={COLORS.primary1} />}
  </TouchableOpacity>
);

const ListsStackScreen: React.FC = () => {
  return (
    <ListsStack.Navigator>
      <ListsStack.Screen
        options={{headerShown: false}}
        name="Lists"
        component={ListsScreen}
      />
      <ListsStack.Screen name="Create List" component={CreateListScreen} />
      <ListsStack.Screen
        name="ListStack"
        component={ListStackScreen}
        options={{
          presentation: 'modal',
          headerRight: renderEditButton,
          headerShown: false,
        }}
      />
    </ListsStack.Navigator>
  );
};

const ListStackScreen: React.FC = () => {
  return (
    <ListStack.Navigator>
      <ListStack.Screen name="List" component={ListScreen} />
      <ListStack.Screen name="Edit List" component={EditListScreen} />
    </ListStack.Navigator>
  );
};

const GroupsStackScreen: React.FC = () => {
  return (
    <GroupsStack.Navigator>
      <GroupsStack.Screen
        options={{headerShown: false}}
        name="Groups"
        component={GroupsScreen}
      />
      <GroupsStack.Screen
        name="GroupStack"
        component={GroupStackScreen}
        options={{
          presentation: 'modal',
          headerRight: renderEditButton,
          headerShown: false,
        }}
      />
    </GroupsStack.Navigator>
  );
};

const GroupStackScreen: React.FC = () => {
  return (
    <GroupStack.Navigator>
      <GroupStack.Screen name="Group" component={GroupScreen} />
      <GroupStack.Screen name="Edit Group" component={EditGroupScreen} />
    </GroupStack.Navigator>
  );
};

const AccountStackScreen: React.FC = () => {
  return (
    <AccountStack.Navigator screenOptions={{headerShown: false}}>
      <AccountStack.Screen
        name="Settings"
        component={AccountsScreen}
        options={{headerShown: true, headerTitleStyle: {fontSize: 21}}}
      />
      <AccountStack.Screen name="Requests" component={Requests} />
    </AccountStack.Navigator>
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
        component={ListsStackScreen}
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
      <Toast visibilityTime={2000} />
    </NavigationContainer>
  );
};

const styles = StyleSheet.create({
  screenTitle: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    width: '100%',
  },
  touchable: {
    alignItems: 'flex-start',
  },
  titleText: {
    fontSize: 20,
  },
});
export default AppNavigator;

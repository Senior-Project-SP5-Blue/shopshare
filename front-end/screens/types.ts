import {BottomTabNavigationProp} from '@react-navigation/bottom-tabs';
import {CompositeNavigationProp} from '@react-navigation/native';
import {NativeStackNavigationProp} from '@react-navigation/native-stack';

// ListStack
export type RootStackParamList = {
  MainStack: undefined;
  WelcomeStack: undefined;
};

export type AuthStackParamList = {
  Welcome: undefined;
  SignUp: undefined;
  Login: undefined;
  EmailConfirmation: undefined;
};

export type MainTabParamList = {
  ListsStack: undefined;
  ListStack: undefined;
  GroupsStack: undefined;
  AccountStack: undefined;
};

export type ListStackParamList = {
  List: {
    groupId: string;
    listId: string;
  };
  Lists: undefined;
  'Create List': undefined;
  ShopScreen: undefined;
};

export type AccountStackParamList = {
  Accounts: undefined;
};
export type GroupStackParamList = {
  Group: {
    groupId: string;
  };
  Groups: undefined;
};

export type ListScreenNavigationProp = CompositeNavigationProp<
  NativeStackNavigationProp<ListStackParamList, 'List'>,
  CompositeNavigationProp<
    BottomTabNavigationProp<MainTabParamList>,
    NativeStackNavigationProp<RootStackParamList>
  >
>;

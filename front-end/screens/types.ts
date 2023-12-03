import {BottomTabScreenProps} from '@react-navigation/bottom-tabs';
import {
  CompositeScreenProps,
  NavigatorScreenParams,
} from '@react-navigation/native';
import {
  NativeStackNavigationProp,
  NativeStackScreenProps,
} from '@react-navigation/native-stack';
import ShoppingListDto from '../models/shoppinglist/ShoppingListDto';
import ShopperGroupDto from '../models/shoppergroup/ShopperGroupDto';

// ListStack
export type RootStackParamList = {
  MainStack: NavigatorScreenParams<MainTabParamList>;
  WelcomeStack: undefined;
};

export type AuthStackParamList = {
  Welcome: undefined;
  SignUp: undefined;
  Login: undefined;
  EmailConfirmation: undefined;
};

export type ListsStackParamList = {
  ListStack: NavigatorScreenParams<ListStackParamList>;
  Lists: undefined;
  'Create List': {groupId?: string};
};
export type MainTabParamList = {
  ListsStack: NavigatorScreenParams<ListsStackParamList>;
  GroupsStack: NavigatorScreenParams<GroupsStackParamList>;
  AccountStack: NavigatorScreenParams<AccountStackParamList>;
};

export type ListStackParamList = {
  List: {
    groupId: string;
    listId: string;
    color: string;
  };
  'Add Items': {
    listId: string;
  };
  'Edit List': {
    groupId: string;
    list: ShoppingListDto;
    color: string;
  };
};

export type AccountStackParamList = {
  Accounts: undefined;
};
export type GroupsStackParamList = {
  GroupStack: NavigatorScreenParams<GroupStackParamList>;
  Groups: undefined;
};
export type GroupStackParamList = {
  Group: {
    groupId: string;
    color: string;
  };
  'Edit Group': {
    group: ShopperGroupDto;
    color: string;
  };
};

// composite props
export type ListScreenPropsType = CompositeScreenProps<
  NativeStackScreenProps<ListStackParamList, 'List'>,
  CompositeScreenProps<
    NativeStackScreenProps<ListsStackParamList, 'ListStack'>,
    BottomTabScreenProps<MainTabParamList, 'ListsStack'>
  >
>;

export type EditListScreenPropsType = CompositeScreenProps<
  NativeStackScreenProps<ListStackParamList, 'Edit List'>,
  CompositeScreenProps<
    NativeStackScreenProps<ListsStackParamList, 'ListStack'>,
    BottomTabScreenProps<MainTabParamList, 'ListsStack'>
  >
>;

export type CreateListScreenPropsType = CompositeScreenProps<
  NativeStackScreenProps<ListsStackParamList, 'Create List'>,
  BottomTabScreenProps<MainTabParamList, 'ListsStack'>
>;

export type GroupScreenPropsType = CompositeScreenProps<
  NativeStackScreenProps<GroupStackParamList, 'Group'>,
  CompositeScreenProps<
    NativeStackScreenProps<GroupsStackParamList, 'GroupStack'>,
    BottomTabScreenProps<MainTabParamList, 'GroupsStack'>
  >
>;

export type EditGroupScreenPropsType = CompositeScreenProps<
  NativeStackScreenProps<GroupStackParamList, 'Edit Group'>,
  CompositeScreenProps<
    NativeStackScreenProps<GroupsStackParamList, 'GroupStack'>,
    BottomTabScreenProps<MainTabParamList, 'GroupsStack'>
  >
>;

export type RootNavigationPropType =
  NativeStackNavigationProp<RootStackParamList>;

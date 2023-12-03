import ShoppingListDto from '../models/shoppinglist/ShoppingListDto';

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

export type ListsStackParamList = {
  ListStack: undefined;
  Lists: undefined;
  'Create List': undefined;
  ShopScreen: undefined;
};

export type ListStackParamList = {
  List: {
    groupId: string;
    listId: string;
  };
  'Add Items': {
    listId: string;
  };
  'Edit List': {
    list: ShoppingListDto;
    groupId: string;
    saveHandler: Function;
  };
};

export type AccountStackParamList = {
  Accounts: undefined;
};
export type GroupStackParamList = {
  Group: {
    groupId: string;
  };
  Groups: undefined;
  'Create Group': undefined;

};

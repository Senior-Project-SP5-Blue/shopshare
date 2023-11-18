import Config from 'react-native-config';
import CreateListItemRequest from '../models/listitem/CreateListItemRequest';
import EditListItemRequest from '../models/listitem/EditListItemRequest';
import CreateEditShopperGroupRequest from '../models/shoppergroup/CreateEditShopperGroupRequest';
import CreateEditShoppingListRequest from '../models/shoppinglist/CreateEditShoppingListRequest';
import ChangePasswordRequest from '../models/auth/ChangePasswordRequest';

class ApiRoutes {
  #baseURL: string = Config.API_URL ?? '';
  #url: string;

  constructor() {
    this.#url = '';
  }

  users(userId?: string) {
    this.#url = `${this.#baseURL}/users${userId ?? ''}`;
    return this;
  }

  groups(groupId?: string) {
    this.#url += `/groups${groupId ?? ''}`;
    return this;
  }

  members(memberId?: string) {
    this.#url += `/members${memberId ?? ''}`;
    return this;
  }

  invitations(groupId?: string) {
    this.#url += `/invitations${groupId ?? ''}`;
    return this;
  }

  lists(listId?: string) {
    this.#url += `/lists${listId ?? ''}`;
    return this;
  }

  items(itemId?: string) {
    this.#url += `/items${itemId ?? ''}`;
    return this;
  }

  buildUrl() {
    return this.#url;
  }
}

export type apiPathParams = {
  userId?: string;
  groupId?: string;
  memberId?: string;
  listId?: string;
  itemId?: string;
};

export type listItemApiRequest = {
  userId: string;
  groupId: string;
  listId: string;
  itemId?: string;
  body?: CreateListItemRequest | EditListItemRequest;
};

export type shopperGroupApiRequest = {
  userId: string;
  groupId?: string;
  memberId?: string;
  body?: CreateEditShopperGroupRequest;
};

export type shoppingListApiRequest = {
  userId: string;
  groupId?: string;
  listId?: string;
  body?: CreateEditShoppingListRequest;
};

export type userApiRequest = {
  userId: string;
  body?: ChangePasswordRequest;
};
export default new ApiRoutes();

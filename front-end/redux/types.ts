import ChangePasswordRequest from '../models/auth/ChangePasswordRequest';
import SignInRequest from '../models/auth/SignInRequest';
import SignUpRequest from '../models/auth/SignUpRequest';
import CreateListItemRequest from '../models/listitem/CreateListItemRequest';
import EditListItemRequest from '../models/listitem/EditListItemRequest';
import CreateEditShopperGroupRequest from '../models/shoppergroup/CreateEditShopperGroupRequest';
import CreateEditShoppingListRequest from '../models/shoppinglist/CreateEditShoppingListRequest';

export type apiPathParams = {
  userId: string;
  groupId: string;
  memberId: string;
  listId: string;
  itemId: string;
  body:
    | CreateListItemRequest
    | EditListItemRequest
    | shoppingListApiRequest
    | ChangePasswordRequest
    | SignUpRequest
    | SignInRequest;
};
// Auth Api
export type AuthApiSignUpReq = Pick<apiPathParams, 'body'>;
export type AuthApiSignInReq = Pick<apiPathParams, 'body'>;

// ListItem Api
export type ListItemApiAddItemToListReq = Pick<
  apiPathParams,
  'userId' | 'groupId' | 'listId' | 'body'
>;
export type ListItemApiChangeItemReq = Pick<
  apiPathParams,
  'userId' | 'groupId' | 'listId' | 'itemId' | 'body'
>;
export type ListItemApiRemoveItemReq = Pick<
  apiPathParams,
  'userId' | 'groupId' | 'listId' | 'itemId'
>;
export type ListItemApiRemoveAllItemsReq = Pick<
  apiPathParams,
  'userId' | 'groupId' | 'listId'
>;

// ShopperGroupApi
export type ShopperGroupApiGetGroupsReq = Pick<apiPathParams, 'userId'>;
export type ShopperGroupApiGetGroupMembersReq = Pick<
  apiPathParams,
  'userId' | 'groupId'
>;
export type ShopperGroupApiGetGroupMemberReq = Pick<
  apiPathParams,
  'userId' | 'groupId' | 'memberId'
>;

export type ShopperGroupApiInviteUserToGroupReq = Pick<
  apiPathParams,
  'userId' | 'groupId' | 'memberId'
>;
export type ShopperGroupApiCreateGroupReq = Pick<
  apiPathParams,
  'userId' | 'body'
>;
export type ShopperGroupApiAcceptInvitation = Pick<
  apiPathParams,
  'userId' | 'groupId'
>;
export type ShopperGroupApiChangeGroupNameReq = Pick<
  apiPathParams,
  'userId' | 'groupId' | 'body'
>;
export type ShopperGroupApiRemoveMemberReq = Pick<
  apiPathParams,
  'userId' | 'groupId' | 'memberId'
>;
export type ShopperGroupApiDeleteGroupReq = Pick<
  apiPathParams,
  'userId' | 'groupId'
>;

// ShoppingListApi
export type ShoppingListApiGetShoppingListsReq = Pick<apiPathParams, 'userId'>;
export type ShoppingListApiGetGroupShoppingListsReq = Pick<
  apiPathParams,
  'userId' | 'groupId'
>;
export type ShoppingListApiGetGroupShoppingListReq = Pick<
  apiPathParams,
  'userId' | 'groupId' | 'listId'
>;
export type ShoppingListApiCreateShoppingListReq = Pick<
  apiPathParams,
  'userId' | 'groupId' | 'body'
>;
export type ShoppingListApiChangeShoppingListNameReq = Pick<
  apiPathParams,
  'userId' | 'groupId' | 'listId' | 'body'
>;
export type ShoppingListApiDeleteShoppingListReq = Pick<
  apiPathParams,
  'userId' | 'groupId' | 'listId'
>;
// UserApi
export type UserApiGetInvitations = Pick<apiPathParams, 'userId'>;
export type UserApiChangePassword = Pick<apiPathParams, 'userId' | 'body'>;
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

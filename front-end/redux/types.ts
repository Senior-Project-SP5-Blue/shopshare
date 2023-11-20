import CreateListItemRequest from '../models/listitem/CreateListItemRequest';
import EditListItemRequest from '../models/listitem/EditListItemRequest';
import CreateEditShopperGroupRequest from '../models/shoppergroup/CreateEditShopperGroupRequest';
import CreateEditShoppingListRequest from '../models/shoppinglist/CreateEditShoppingListRequest';
import UserDto from '../models/user/UserDto';

export type apiPathParams = {
  userId: string;
  groupId: string;
  memberId: string;
  listId: string;
  itemId: string;
};

// Auth Api
export type AuthApiSignUpReq = {
  username: string;
  firstname: string;
  lastname: string;
  email: string;
  number: string;
  password: string;
};

export type AuthApiSignInReq = {
  email: string;
  password: string;
};

export type SignInResponse = {
  userContext: UserDto;
  accessToken: string;
  refreshToken: string;
};

export type ChangePasswordRequest = {
  currentPassword: string;
  newPassword: string;
  confirmPassword: string;
};

// ListItem Api
export type ListItemApiAddItemToListReq = Pick<
  apiPathParams,
  'userId' | 'groupId' | 'listId'
> & {body: CreateListItemRequest};
export type ListItemApiChangeItemReq = Pick<
  apiPathParams,
  'userId' | 'groupId' | 'listId' | 'itemId'
> & {body: EditListItemRequest};
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
export type ShopperGroupApiCreateGroupReq = Pick<apiPathParams, 'userId'> & {
  body: CreateEditShopperGroupRequest;
};
export type ShopperGroupApiAcceptInvitation = Pick<
  apiPathParams,
  'userId' | 'groupId'
>;
export type ShopperGroupApiChangeGroupNameReq = Pick<
  apiPathParams,
  'userId' | 'groupId'
> & {body: CreateEditShopperGroupRequest};
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
  'userId' | 'groupId'
> & {body: CreateEditShoppingListRequest};
export type ShoppingListApiChangeShoppingListNameReq = Pick<
  apiPathParams,
  'userId' | 'groupId' | 'listId'
> & {body: CreateEditShoppingListRequest};
export type ShoppingListApiDeleteShoppingListReq = Pick<
  apiPathParams,
  'userId' | 'groupId' | 'listId'
>;
// UserApi
export type UserApiGetInvitations = Pick<apiPathParams, 'userId'>;
export type UserApiChangePassword = Pick<apiPathParams, 'userId'> & {
  body: ChangePasswordRequest;
};

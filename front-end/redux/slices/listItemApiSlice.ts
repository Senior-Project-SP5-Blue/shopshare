import ListItemDto from '../../models/listitem/ListItemDto';
import {
  ListItemApiAddItemToListReq,
  ListItemApiChangeItemReq,
  ListItemApiRemoveAllItemsReq,
  ListItemApiRemoveItemReq,
} from '../types';
import {apiSlice} from './shopshareApiSlice';

export const listItemApiSlice = apiSlice.injectEndpoints({
  endpoints: builder => ({
    addItemToList: builder.mutation<ListItemDto, ListItemApiAddItemToListReq>({
      query: ({userId, groupId, listId, body}) => ({
        // url: ApiRoutes.users(userId).groups(groupId).lists().buildUrl(),
        url: `/users/${userId}/groups/${groupId}/lists/${listId}/items`,
        method: 'POST',
        body: JSON.stringify(body),
      }),
      invalidatesTags: (_result, _error, arg) => [
        {type: 'ShoppingList', id: arg.listId},
        {type: 'ShopperGroup', id: arg.groupId},
      ],
    }),
    /** modifies an item in a shopping list, i.e., change item name, lock item, complete, etc */
    changeItem: builder.mutation<ListItemDto, ListItemApiChangeItemReq>({
      query: ({userId, groupId, listId, itemId, body}) => ({
        url: `/users/${userId}/groups/${groupId}/lists/${listId}/items/${itemId!}`,
        method: 'PATCH',
        body: JSON.stringify(body),
      }),
      invalidatesTags: (_result, _error, arg) => [
        {type: 'ShoppingList', id: arg.listId},
        {type: 'ListItem', id: arg.itemId},
        // {type: 'ShopperGroup', id: arg.groupId},
      ],
    }),
    /** removes an item from a shopping list */
    removeItem: builder.mutation<void, ListItemApiRemoveItemReq>({
      query: ({userId, groupId, listId, itemId}) => ({
        url: `/users/${userId}/groups/${groupId}/lists/${listId}/items/${itemId}`,
        method: 'DELETE',
      }),
      invalidatesTags: (_result, _error, arg) => [
        {type: 'ShoppingList', id: arg.listId},
        {type: 'ShopperGroup', id: arg.groupId},
      ],
    }),
    /** Removes all items in shopping list */
    removeAllItemsFromList: builder.mutation<
      void,
      ListItemApiRemoveAllItemsReq
    >({
      query: ({userId, groupId, listId}) => ({
        url: `/users/${userId}/groups/${groupId}/lists/${listId}/items`,
        method: 'DELETE',
      }),
      invalidatesTags: (_result, _error, arg) => [
        {type: 'ShoppingList', id: arg.listId},
        {type: 'ShoppingList', id: arg.listId},
      ],
    }),
  }),
});

export const {
  useAddItemToListMutation,
  useChangeItemMutation,
  useRemoveAllItemsFromListMutation,
  useRemoveItemMutation,
} = listItemApiSlice;

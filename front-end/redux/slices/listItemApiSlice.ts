import {listItemApiRequest} from '../../api/ApiRoutes';
import ListItemDto from '../../models/listitem/ListItemDto';
import {apiSlice} from './shopshareApiSlice';

export const listItemApiSlice = apiSlice.injectEndpoints({
  endpoints: builder => ({
    addItemToList: builder.mutation<ListItemDto, listItemApiRequest>({
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
    changeListItem: builder.mutation<ListItemDto, listItemApiRequest>({
      query: ({userId, groupId, listId, itemId, body}) => ({
        url: `/users/${userId}/groups/${groupId}/lists/${listId}/items/${itemId!}`,
        method: 'PATCH',
        body: JSON.stringify(body),
      }),
      invalidatesTags: (_result, _error, arg) => [
        {type: 'ShoppingList', id: arg.listId},
        {type: 'ShopperGroup', id: arg.groupId},
      ],
    }),
    /** removes an item from a shopping list */
    removeListItem: builder.mutation<void, listItemApiRequest>({
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
    removeAllItemsFromList: builder.mutation<void, listItemApiRequest>({
      query: ({userId, groupId, listId}) => ({
        url: `/users/${userId}/groups/${groupId}/lists/${listId}/items`,
        method: 'DELETE',
      }),
      invalidatesTags: (_result, _error, arg) => [
        {type: 'ShoppingList', id: arg.listId},
        {type: 'ShopperGroup', id: arg.groupId},
      ],
    }),
  }),
});

export const {
  useAddItemToListMutation,
  useChangeListItemMutation,
  useRemoveAllItemsFromListMutation,
  useRemoveListItemMutation,
} = listItemApiSlice;

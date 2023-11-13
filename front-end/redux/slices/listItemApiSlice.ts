import ApiRoutes from '../../api/ApiRoutes';
import CreateListItemRequest from '../../models/listitem/CreateListItemRequest';
import EditListItemRequest from '../../models/listitem/EditListItemRequest';
import ListItemDto from '../../models/listitem/ListItemDto';
import {apiPathParams, apiSlice} from './shopshareApiSlice';

export const listItemApiSlice = apiSlice.injectEndpoints({
  endpoints: builder => ({
    addItemToList: builder.mutation<
      ListItemDto,
      apiPathParams & CreateListItemRequest
    >({
      query: ({groupId, name, locked}) => ({
        url: ApiRoutes.users('1').groups(groupId).lists().buildUrl(),
        method: 'POST',
        headers: {'Content-Type': 'application/json', Authorization: 'Bearer '},
        body: JSON.stringify({name, locked}),
      }),
      invalidatesTags: (_result, _error, arg) => [
        {type: 'ShoppingList', id: arg.listId},
        {type: 'ShopperGroup', id: arg.groupId},
      ],
    }),
    /** modifies an item in a shopping list, i.e., change item name, lock item, complete, etc */
    changeListItem: builder.mutation<
      ListItemDto,
      apiPathParams & EditListItemRequest
    >({
      query: request => ({
        url: ApiRoutes.users('1')
          .groups(request.groupId)
          .lists(request.listId)
          .items(request.itemId)
          .buildUrl(),
        method: 'PATCH',
        headers: {'Content-Type': 'application/json', Authorization: 'Bearer '},
        body: JSON.stringify({
          name: request.name,
          status: request.status,
          lcoked: request.locked,
        }),
      }),
      invalidatesTags: (_result, _error, arg) => [
        {type: 'ShoppingList', id: arg.listId},
        {type: 'ShopperGroup', id: arg.groupId},
      ],
    }),
    /** removes an item from a shopping list */
    removeListItem: builder.mutation<void, apiPathParams>({
      query: ({groupId, listId, itemId}) => ({
        url: ApiRoutes.users('2')
          .groups(groupId)
          .lists(listId)
          .items(itemId)
          .buildUrl(),
        method: 'DELETE',
        headers: {Authorization: 'Bearer '},
      }),
      invalidatesTags: (_result, _error, arg) => [
        {type: 'ShoppingList', id: arg.listId},
        {type: 'ShopperGroup', id: arg.groupId},
      ],
    }),
    /** Removes all items in shopping list */
    removeAllItemsFromList: builder.mutation<void, apiPathParams>({
      query: ({groupId, listId}) => ({
        url: ApiRoutes.users('1').groups(groupId).lists(listId).buildUrl(),
        method: 'DELETE',
        headers: {Authorization: 'Bearer '},
      }),
      invalidatesTags: (_result, _error, arg) => [
        {type: 'ShoppingList', id: arg.listId},
        {type: 'ShopperGroup', id: arg.groupId},
      ],
    }),
  }),
});

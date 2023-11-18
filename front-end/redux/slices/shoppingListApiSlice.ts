import {shoppingListApiRequest} from '../../api/ApiRoutes';
import ShoppingListDto from '../../models/shoppinglist/ShoppingListDto';
import SlimShoppingListDto from '../../models/shoppinglist/SlimShoppingListDto';
import {apiSlice} from './shopshareApiSlice';

export const shoppingListApiSlice = apiSlice.injectEndpoints({
  endpoints: builder => ({
    getShoppingLists: builder.query<
      SlimShoppingListDto[],
      shoppingListApiRequest
    >({
      query: ({userId}) => ({
        url: `/users/${userId}/groups/lists`,
        method: 'GET',
      }),
      providesTags: (result = []) => [
        ...result.map(({id}) => ({
          type: 'ShoppingList' as const,
          id,
        })),
        {type: 'ShoppingList', id: 'LIST'},
      ],
    }),
    getGroupShoppingLists: builder.query<
      SlimShoppingListDto[],
      shoppingListApiRequest
    >({
      query: ({userId, groupId}) => ({
        url: `/users/${userId}/groups/${groupId}/lists`,
        method: 'GET',
      }),
      providesTags: (result = []) => [
        ...result.map(({id}) => ({
          type: 'ShoppingList' as const,
          id,
        })),
        {type: 'ShoppingList', id: 'LIST'},
      ],
    }),
    getGroupShoppingList: builder.query<
      ShoppingListDto,
      shoppingListApiRequest
    >({
      query: ({userId, groupId, listId}) => ({
        url: `/users/${userId}/groups/${groupId}/lists/${listId}`,
        method: 'GET',
      }),
      providesTags: result =>
        result ? [{type: 'ShoppingList', id: result!.id}] : [],
    }),
    addShoppingList: builder.mutation<ShoppingListDto, shoppingListApiRequest>({
      query: ({userId, groupId, body}) => ({
        url: `/users/${userId}/groups/${groupId}/lists`,
        method: 'POST',
        body: JSON.stringify(body),
      }),
      invalidatesTags: (_result, _error, arg) => [
        {type: 'ShoppingList', id: 'LIST'},
        {type: 'ShoppingList', id: arg.listId},
        {type: 'ShopperGroup', id: arg.groupId},
      ],
    }),
    changeShoppingListName: builder.mutation<
      SlimShoppingListDto,
      shoppingListApiRequest
    >({
      query: ({userId, groupId, listId, body}) => ({
        url: `/users/${userId}/groups/${groupId}/lists/${listId}`,
        method: 'PATCH',
        body: JSON.stringify(body),
      }),
      invalidatesTags: (_result, _error, arg) => [
        {type: 'ShoppingList', id: 'LIST'},
        {type: 'ShoppingList', id: arg.listId},
        {type: 'ShopperGroup', id: arg.groupId},
      ],
    }),
    deleteShoppingList: builder.mutation<void, shoppingListApiRequest>({
      query: ({userId, groupId, listId}) => ({
        url: `/users/${userId}/groups/${groupId}/lists/${listId}`,
        method: 'DELETE',
      }),
      invalidatesTags: (_result, _error, arg) => [
        {type: 'ShoppingList', id: 'LIST'},
        {type: 'ShoppingList', id: arg.listId},
        {type: 'ShopperGroup', id: arg.groupId},
      ],
    }),
  }),
});

export const {
  useGetShoppingListsQuery,
  useGetGroupShoppingListsQuery,
  useGetGroupShoppingListQuery,
  useAddShoppingListMutation,
  useChangeShoppingListNameMutation,
  useDeleteShoppingListMutation,
} = shoppingListApiSlice;

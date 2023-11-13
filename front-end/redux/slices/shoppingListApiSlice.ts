import ApiRoutes, {apiPathParams} from '../../api/ApiRoutes';
import CreateEditShoppingListRequest from '../../models/shoppinglist/CreateEditShoppingListRequest';
import ShoppingListDto from '../../models/shoppinglist/ShoppingListDto';
import SlimShoppingListDto from '../../models/shoppinglist/SlimShoppingListDto';
import {apiSlice} from './shopshareApiSlice';

export const shoppingListApiSlice = apiSlice.injectEndpoints({
  endpoints: builder => ({
    getShoppingLists: builder.query<SlimShoppingListDto[], void>({
      query: () => ({
        url: ApiRoutes.users('1').groups().lists().buildUrl(),
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
    getGroupShoppingLists: builder.query<SlimShoppingListDto[], apiPathParams>({
      query: ({groupId}) => ({
        url: ApiRoutes.users('1').groups(groupId).lists().buildUrl(),
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
    getGroupShoppingList: builder.query<ShoppingListDto, apiPathParams>({
      query: ({groupId, listId}) => ({
        url: ApiRoutes.users('1').groups(groupId).lists(listId).buildUrl(),
        method: 'GET',
      }),
      providesTags: result =>
        result ? [{type: 'ShoppingList', id: result!.id}] : [],
    }),
    addShoppingList: builder.mutation<
      ShoppingListDto,
      apiPathParams & CreateEditShoppingListRequest
    >({
      query: ({groupId, name}) => ({
        url: ApiRoutes.users('1').groups(groupId).lists().buildUrl(),
        method: 'POST',
        body: JSON.stringify(name),
      }),
      invalidatesTags: (_result, _error, arg) => [
        {type: 'ShoppingList', id: 'LIST'},
        {type: 'ShoppingList', id: arg.listId},
        {type: 'ShopperGroup', id: arg.groupId},
      ],
    }),
    changeShoppingListName: builder.mutation<
      SlimShoppingListDto,
      apiPathParams & CreateEditShoppingListRequest
    >({
      query: ({groupId, listId, name}) => ({
        url: ApiRoutes.users('1').groups(groupId).lists(listId).buildUrl(),
        method: 'PATCH',
        body: JSON.stringify(name),
      }),
      invalidatesTags: (_result, _error, arg) => [
        {type: 'ShoppingList', id: 'LIST'},
        {type: 'ShoppingList', id: arg.listId},
        {type: 'ShopperGroup', id: arg.groupId},
      ],
    }),
    deleteShoppingList: builder.mutation<void, apiPathParams>({
      query: ({groupId, listId}) => ({
        url: ApiRoutes.users('1').groups(groupId).lists(listId).buildUrl(),
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
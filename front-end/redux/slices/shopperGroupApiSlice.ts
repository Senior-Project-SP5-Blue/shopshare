import ApiRoutes, {apiPathParams} from '../../api/ApiRoutes';
import CreateEditShopperGroupRequest from '../../models/shoppergroup/CreateEditShopperGroupRequest';
import ShopperGroupDto from '../../models/shoppergroup/ShopperGroupDto';
import UserDto from '../../models/user/UserDto';
import {apiSlice} from './shopshareApiSlice';

export const shopperGroupApiSlice = apiSlice.injectEndpoints({
  endpoints: builder => ({
    getGroups: builder.query<ShopperGroupDto[], void>({
      query: () => ({
        url: `/users/1/groups`,
        method: 'GET',
      }),
      providesTags: (result = []) => [
        ...result.map(({id}) => ({
          type: 'ShopperGroup' as const,
          id,
        })),
        {type: 'ShopperGroup', id: 'LIST'},
      ],
    }),
    getGroupMembers: builder.query<UserDto[], apiPathParams>({
      query: ({groupId}) => ({
        url: ApiRoutes.users('1').groups(groupId).buildUrl(),
        method: 'GET',
      }),
      providesTags: (result = []) => [
        ...result.map(({id}) => ({
          type: 'User' as const,
          id,
        })),
        {type: 'User', id: 'LIST'},
      ],
    }),
    getGroupMember: builder.query<UserDto, apiPathParams>({
      query: ({groupId, memberId}) => ({
        url: ApiRoutes.users('1').groups(groupId).members(memberId).buildUrl(),
        method: 'GET',
      }),
    }),
    inviteUserToGroup: builder.mutation<void, apiPathParams>({
      query: ({groupId, memberId}) => ({
        url: ApiRoutes.users('1')
          .groups(groupId)
          .invitations(memberId)
          .buildUrl(),
        method: 'POST',
      }),
    }),
    createNewGroup: builder.mutation<
      ShopperGroupDto,
      CreateEditShopperGroupRequest
    >({
      query: request => ({
        url: ApiRoutes.users('1').groups().buildUrl(),
        method: 'POST',
        body: JSON.stringify(request.name),
      }),
      invalidatesTags: (_result, _error, _arg) => [
        {type: 'ShopperGroup', id: 'LIST'},
      ],
    }),
    acceptGroupInvitation: builder.mutation<void, apiPathParams>({
      query: ({groupId}) => ({
        url: ApiRoutes.users('1').groups(groupId).buildUrl(),
        method: 'POST',
      }),
      invalidatesTags: (_result, _error, _arg) => [
        {type: 'ShopperGroup', id: 'LIST'},
      ],
    }),
    changeGroupName: builder.mutation<
      void,
      apiPathParams & CreateEditShopperGroupRequest
    >({
      query: ({groupId, name}) => ({
        url: ApiRoutes.users('1').groups(groupId).buildUrl(),
        method: 'PATCH',
        body: JSON.stringify(name),
      }),
      invalidatesTags: (_result, _error, arg) => [
        {type: 'ShopperGroup', id: 'LIST'},
        {type: 'ShopperGroup', id: arg.groupId},
      ],
    }),
    removeMemberFromGroup: builder.mutation<void, apiPathParams>({
      query: ({groupId, memberId}) => ({
        url: ApiRoutes.users('1').groups(groupId).members(memberId).buildUrl(),
        method: 'DELETE',
      }),
      invalidatesTags: (_result, _error, arg) => [
        {type: 'ShopperGroup', id: 'LIST'},
        {type: 'ShopperGroup', id: arg.groupId},
      ],
    }),
    deleteShopperGroup: builder.mutation<void, apiPathParams>({
      query: ({groupId, memberId}) => ({
        url: ApiRoutes.users('1').groups(groupId).members(memberId).buildUrl(),
        method: 'DELETE',
      }),
      invalidatesTags: (_result, _error, arg) => [
        {type: 'ShopperGroup', id: 'LIST'},
        {type: 'ShopperGroup', id: arg.groupId},
      ],
    }),
  }),
});

export const {
  useGetGroupsQuery,
  useGetGroupMembersQuery,
  useGetGroupMemberQuery,
  useCreateNewGroupMutation,
  useInviteUserToGroupMutation,
  useAcceptGroupInvitationMutation,
  useChangeGroupNameMutation,
  useDeleteShopperGroupMutation,
} = shopperGroupApiSlice;

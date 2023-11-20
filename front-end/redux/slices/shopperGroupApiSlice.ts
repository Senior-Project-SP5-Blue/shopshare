import ShopperGroupDto from '../../models/shoppergroup/ShopperGroupDto';
import UserDto from '../../models/user/UserDto';
import {
  ShopperGroupApiAcceptInvitation,
  ShopperGroupApiChangeGroupNameReq,
  ShopperGroupApiCreateGroupReq,
  ShopperGroupApiDeleteGroupReq,
  ShopperGroupApiGetGroupMemberReq,
  ShopperGroupApiGetGroupMembersReq,
  ShopperGroupApiGetGroupsReq,
  ShopperGroupApiInviteUserToGroupReq,
  ShopperGroupApiRemoveMemberReq,
} from '../types';
import {apiSlice} from './shopshareApiSlice';

export const shopperGroupApiSlice = apiSlice.injectEndpoints({
  endpoints: builder => ({
    getGroups: builder.query<ShopperGroupDto[], ShopperGroupApiGetGroupsReq>({
      query: ({userId}) => ({
        url: `/users/${userId}/groups`,
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
    getGroupMembers: builder.query<
      UserDto[],
      ShopperGroupApiGetGroupMembersReq
    >({
      query: ({userId, groupId}) => ({
        url: `/users/${userId}/groups/${groupId}/members`,
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
    getGroupMember: builder.query<UserDto, ShopperGroupApiGetGroupMemberReq>({
      query: ({userId, groupId, memberId}) => ({
        url: `/users/${userId}/groups/${groupId}/members${memberId}`,
        method: 'GET',
      }),
    }),
    inviteUserToGroup: builder.mutation<
      void,
      ShopperGroupApiInviteUserToGroupReq
    >({
      query: ({userId, groupId, memberId}) => ({
        url: `/users/${userId}/groups/${groupId}/invitations/${memberId}`,
        method: 'POST',
      }),
    }),
    createNewGroup: builder.mutation<
      ShopperGroupDto,
      ShopperGroupApiCreateGroupReq
    >({
      query: ({userId, body}) => ({
        url: `/users/${userId}/groups`,
        method: 'POST',
        body: JSON.stringify(body),
      }),
      invalidatesTags: (_result, _error, _arg) => [
        {type: 'ShopperGroup', id: 'LIST'},
      ],
    }),
    acceptGroupInvitation: builder.mutation<
      void,
      ShopperGroupApiAcceptInvitation
    >({
      query: ({userId, groupId}) => ({
        url: `/users/${userId}/groups/${groupId}`,
        method: 'POST',
      }),
      invalidatesTags: (_result, _error, _arg) => [
        {type: 'ShopperGroup', id: 'LIST'},
      ],
    }),
    changeGroupName: builder.mutation<void, ShopperGroupApiChangeGroupNameReq>({
      query: ({userId, groupId, body}) => ({
        url: `/users/${userId}/groups/${groupId}`,
        method: 'PATCH',
        body: JSON.stringify(body),
      }),
      invalidatesTags: (_result, _error, arg) => [
        {type: 'ShopperGroup', id: 'LIST'},
        {type: 'ShopperGroup', id: arg.groupId},
      ],
    }),
    removeMemberFromGroup: builder.mutation<
      void,
      ShopperGroupApiRemoveMemberReq
    >({
      query: ({userId, groupId, memberId}) => ({
        url: `/users/${userId}/groups/${groupId}/members/${memberId}`,
        method: 'DELETE',
      }),
      invalidatesTags: (_result, _error, arg) => [
        {type: 'ShopperGroup', id: 'LIST'},
        {type: 'ShopperGroup', id: arg.groupId},
      ],
    }),
    deleteShopperGroup: builder.mutation<void, ShopperGroupApiDeleteGroupReq>({
      query: ({userId, groupId}) => ({
        url: `/users/${userId}/groups/${groupId}`,
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

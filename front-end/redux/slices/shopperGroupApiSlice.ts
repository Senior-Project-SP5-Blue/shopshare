import InvitationDto from '../../models/shoppergroup/InvitationDto';
import ShopperGroupDto from '../../models/shoppergroup/ShopperGroupDto';
import SlimShopperGroupDto from '../../models/shoppergroup/SlimShopperGroupDto';
import UserDto from '../../models/user/UserDto';
import {
  ShopperGroupApiAcceptInvitation,
  ShopperGroupApiChangeGroupNameReq,
  ShopperGroupApiCreateGroupReq,
  ShopperGroupApiDeclineInvitation,
  ShopperGroupApiDeleteGroupReq,
  ShopperGroupApiGetGroupMemberReq,
  ShopperGroupApiGetGroupMembersReq,
  ShopperGroupApiGetGroupReq,
  ShopperGroupApiGetGroupsReq,
  ShopperGroupApiGetInvitations,
  ShopperGroupApiInviteUserToGroupReq,
  ShopperGroupApiRemoveMemberReq,
} from '../types';
import {apiSlice} from './shopshareApiSlice';

export const shopperGroupApiSlice = apiSlice.injectEndpoints({
  endpoints: builder => ({
    getGroups: builder.query<
      SlimShopperGroupDto[],
      ShopperGroupApiGetGroupsReq
    >({
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
    getGroup: builder.query<ShopperGroupDto, ShopperGroupApiGetGroupReq>({
      query: ({userId, groupId}) => ({
        url: `/users/${userId}/groups/${groupId}`,
        method: 'GET',
      }),
      providesTags: result => [{type: 'ShopperGroup' as const, id: result?.id}],
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
      providesTags: result => [{type: 'User' as const, id: result?.id}],
    }),
    inviteUserToGroup: builder.mutation<
      void,
      ShopperGroupApiInviteUserToGroupReq
    >({
      query: ({userId, groupId, memberUsername}) => ({
        url: `/users/${userId}/groups/${groupId}/invitations/${memberUsername}`,
        method: 'POST',
      }),
    }),
    getGroupInvitations: builder.query<
      InvitationDto[],
      ShopperGroupApiGetInvitations
    >({
      query: ({userId}) => ({
        url: `/users/${userId}/groups/invitations`,
        method: 'GET',
      }),
      providesTags: (result = [], _error, req) => [
        ...result.map(({groupId}) => ({
          type: 'Invitation' as const,
          id: `${groupId}-${req.userId}`,
        })),
        {type: 'Invitation', id: 'LIST'},
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
      invalidatesTags: (_result, _error, args) => [
        {type: 'Invitation', id: 'LIST'},
        {type: 'Invitation', id: `${args.groupId}-${args.userId}`},
        {type: 'ShopperGroup', id: 'LIST'},
        {type: 'ShopperGroup', id: args.groupId},
      ],
    }),
    declineGroupInvitation: builder.mutation<
      void,
      ShopperGroupApiDeclineInvitation
    >({
      query: ({userId, groupId}) => ({
        url: `/users/${userId}/groups/invitations/${groupId}`,
        method: 'DELETE',
      }),
      invalidatesTags: (_result, _error, args) => [
        {type: 'Invitation', id: 'LIST'},
        {type: 'Invitation', id: `${args.groupId}-${args.userId}`},
      ],
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
    editGroup: builder.mutation<void, ShopperGroupApiChangeGroupNameReq>({
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
  useGetGroupQuery,
  useGetGroupMembersQuery,
  useGetGroupMemberQuery,
  useCreateNewGroupMutation,
  useInviteUserToGroupMutation,
  useGetGroupInvitationsQuery,
  useAcceptGroupInvitationMutation,
  useDeclineGroupInvitationMutation,
  useEditGroupMutation,
  useDeleteShopperGroupMutation,
} = shopperGroupApiSlice;

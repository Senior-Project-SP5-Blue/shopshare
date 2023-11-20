import InvitationDto from '../../models/shoppergroup/InvitationDto';
import {UserApiChangePassword, UserApiGetInvitations} from '../types';
import {apiSlice} from './shopshareApiSlice';

export const userApiSlice = apiSlice.injectEndpoints({
  endpoints: builder => ({
    getInvitations: builder.query<InvitationDto[], UserApiGetInvitations>({
      query: ({userId}) => ({
        url: `/users/${userId}/invitations`,
        method: 'GET',
      }),
    }),
    changePassword: builder.mutation<void, UserApiChangePassword>({
      query: ({userId, body}) => ({
        url: `/users/${userId}/password`,
        method: 'PATCH',
        body: JSON.stringify(body),
      }),
    }),
  }),
});

export const {useGetInvitationsQuery, useChangePasswordMutation} = userApiSlice;

import {userApiRequest} from '../../api/ApiRoutes';
import InvitationDto from '../../models/shoppergroup/InvitationDto';
import {apiSlice} from './shopshareApiSlice';

export const userApiSlice = apiSlice.injectEndpoints({
  endpoints: builder => ({
    getInvitations: builder.query<InvitationDto[], string>({
      query: userId => ({
        url: `/users/${userId}/invitations`,
        method: 'GET',
      }),
    }),
    changePassword: builder.mutation<void, userApiRequest>({
      query: ({userId, body}) => ({
        url: `/users/${userId}/password`,
        method: `PATCH`,
        body: JSON.stringify(body),
      }),
    }),
  }),
});

export const {useGetInvitationsQuery, useChangePasswordMutation} = userApiSlice;

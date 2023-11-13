import ApiRoutes from '../../api/ApiRoutes';
import ChangePasswordRequest from '../../models/auth/ChangePasswordRequest';
import InvitationDto from '../../models/shoppergroup/InvitationDto';
import {apiSlice} from './shopshareApiSlice';

export const userApiSlice = apiSlice.injectEndpoints({
  endpoints: builder => ({
    getInvitations: builder.query<InvitationDto[], void>({
      query: () => ({
        url: ApiRoutes.users('1').groups().invitations().buildUrl(),
        method: 'GET',
        headers: {'Content-Type': 'application/json', Authorization: 'Bearer '},
      }),
    }),
    changePassword: builder.mutation<void, ChangePasswordRequest>({
      query: (request: ChangePasswordRequest) => ({
        url: `/users/1/password`,
        method: `PATCH`,
        headers: {'Content-Type': 'application/json', Authorization: 'Bearer'},
        body: JSON.stringify(request),
      }),
    }),
  }),
});

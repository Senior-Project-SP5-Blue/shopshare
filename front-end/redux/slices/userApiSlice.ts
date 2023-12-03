import {UserApiChangePassword} from '../types';
import {apiSlice} from './shopshareApiSlice';

export const userApiSlice = apiSlice.injectEndpoints({
  endpoints: builder => ({
    changePassword: builder.mutation<void, UserApiChangePassword>({
      query: ({userId, body}) => ({
        url: `/users/${userId}/password`,
        method: 'PATCH',
        body: JSON.stringify(body),
      }),
    }),
  }),
});

export const {useChangePasswordMutation} = userApiSlice;

import SignInPayload from '../../models/auth/SignInPayload';
import SignInRequest from '../../models/auth/SignInRequest';
import SignUpRequest from '../../models/auth/SignUpRequest';
import {apiSlice} from './shopshareApiSlice';

export const authApiSlice = apiSlice.injectEndpoints({
  endpoints: builder => ({
    signUp: builder.mutation<void, SignUpRequest>({
      query: request => ({
        url: `/auth/signup`,
        method: `POST`,
        body: JSON.stringify(request),
      }),
    }),
    signIn: builder.mutation<SignInPayload, SignInRequest>({
      query: request => ({
        url: `/auth/signin`,
        method: `POST`,
        body: JSON.stringify(request),
      }),
    }),
    refreshSignIn: builder.mutation<SignInPayload, void>({
      query: () => ({
        url: `/auth/refresh-signin`,
        method: `POST`,
      }),
    }),
    signOut: builder.mutation<void, void>({
      query: () => ({
        url: `/auth/signout`,
        method: `POST`,
      }),
    }),
  }),
});

export const {
  useSignUpMutation,
  useSignInMutation,
  useRefreshSignInMutation,
  useSignOutMutation,
} = authApiSlice;

import {AuthApiSignInReq, AuthApiSignUpReq, SignInResponse} from '../types';
import {apiSlice} from './shopshareApiSlice';

export const authApiSlice = apiSlice.injectEndpoints({
  endpoints: builder => ({
    signUp: builder.mutation<void, AuthApiSignUpReq>({
      query: request => ({
        url: `/auth/signup`,
        method: `POST`,
        body: JSON.stringify(request),
      }),
    }),
    signIn: builder.mutation<SignInResponse, AuthApiSignInReq>({
      query: request => ({
        url: `/auth/signin`,
        method: `POST`,
        body: JSON.stringify(request),
      }),
    }),
    refreshSignIn: builder.mutation<SignInResponse, void>({
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

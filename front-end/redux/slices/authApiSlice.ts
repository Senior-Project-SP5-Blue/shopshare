import SignInPayload from '../../models/auth/SignInPayload';
import SignInRequest from '../../models/auth/SignInRequest';
import SignUpRequest from '../../models/auth/SignUpRequest';
import {apiSlice} from './shopshareApiSlice';

export const authApiSlice = apiSlice.injectEndpoints({
  endpoints: builder => ({
    signUp: builder.mutation<void, SignUpRequest>({
      query: (request: SignUpRequest) => ({
        url: `/auth/signup`,
        method: `POST`,
        body: JSON.stringify(request),
      }),
    }),
    signIn: builder.mutation<SignInPayload, SignInRequest>({
      query: (request: SignInRequest) => ({
        url: `/auth/signin`,
        method: `POST`,
        body: JSON.stringify(request),
      }),
    }),
    refreshSignIn: builder.mutation<SignInPayload, SignInRequest>({
      query: (request: SignInRequest) => ({
        url: `/auth/refresh-signin`,
        method: `POST`,
        body: JSON.stringify(request),
      }),
    }),
  }),
});

export const {useSignUpMutation, useSignInMutation, useRefreshSignInMutation} =
  authApiSlice;

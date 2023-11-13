import {
  BaseQueryApi,
  createApi,
  fetchBaseQuery,
} from '@reduxjs/toolkit/query/react';
import {RootState} from '../store';
import {setAuthContext, signOut} from './authSlice';

// build.mutation<ReturnType, ArgType>. I
// const headers: HeadersInit = {
//   'Content-Type': 'application/json',
//   'X-Request-Id': uuidv4(),
//   Authorization: `Bearer <API_TOKEN>`,
// };

export type apiPathParams = {
  groupId: string;
  memberId?: string;
  listId?: string;
  itemId?: string;
};

const baseQuery = fetchBaseQuery({
  baseUrl: 'http://localhost:8080/api/v1',
  prepareHeaders: (headers, {getState}) => {
    const state: RootState = getState() as RootState;
    const token = state.auth.accessToken;
    headers.set('Content-Type', 'application/json');
    if (token && !headers.has('Authorization')) {
      headers.set('Authorization', `Bearer ${token}`);
    }
    return headers;
  },
});

const baseQueryWithReAuth = async (
  args: any,
  api: BaseQueryApi,
  extraOptions: any,
) => {
  let result = await baseQuery(args, api, extraOptions);

  if (result?.error?.status === 403) {
    const _state = api.getState() as RootState;
    console.log('Sending refresh token');
    const refreshResult: any = await baseQuery(
      {
        url: `/auth/refresh-signin`,
        headers: {Authorization: _state.auth.refreshToken ?? ''},
      },
      api,
      extraOptions,
    );

    console.log(refreshResult);

    if (refreshResult.meta?.response?.ok && refreshResult?.data) {
      const user = _state.auth.user;
      api.dispatch(setAuthContext({...refreshResult.data, user}));
      //   retry OG request with access token
      await baseQuery(args, api, extraOptions);
    } else {
      api.dispatch(signOut());
    }
  }
  return result;
};

/**
 * fetchBaseQuery({
    baseUrl: 'http://localhost:8080/api/v1',
    prepareHeaders: (headers, {getState}) => {
      const state: RootState = getState() as RootState;
      const token = state.auth.accessToken;
      headers.set('Content-Type', 'application/json');
      if (token) {
        headers.set('Authorization', `Bearer ${token}`);
      }
      return headers;
    },
  })
 * 
 */

// Define a service using a base URL and expected endpoints
export const apiSlice = createApi({
  reducerPath: 'api',
  baseQuery: baseQueryWithReAuth,
  tagTypes: ['User', 'ShopperGroup', 'ShoppingList', 'ListItem'],
  endpoints: _builder => ({}),
});

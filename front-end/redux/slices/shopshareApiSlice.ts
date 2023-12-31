import {
  BaseQueryApi,
  createApi,
  fetchBaseQuery,
} from '@reduxjs/toolkit/query/react';
// import {RootState} from '../store';
// import Config from 'react-native-config';
//@ts-ignore
import {REACT_APP_API_URL} from '@env';
import {setAuthContext, clearAuthContext} from './authSlice';

const baseQuery = fetchBaseQuery({
  baseUrl: REACT_APP_API_URL,
  prepareHeaders: (headers, {getState}) => {
    const state = getState() as any;
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

  // Change to 401, WHEN YOU PUSH NEW SPRING CHANGES
  if (result?.error?.status === 401) {
    const _state = api.getState() as any;
    console.log('Sending refresh token');
    const refreshResult: any = await baseQuery(
      {
        url: '/auth/refresh-signin',
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
      api.dispatch(clearAuthContext());
    }
  }
  return result;
};

// Define a service using a base URL and expected endpoints
export const apiSlice = createApi({
  reducerPath: 'api',
  baseQuery: baseQueryWithReAuth,
  tagTypes: ['User', 'ShopperGroup', 'ShoppingList', 'ListItem', 'Invitation'],
  endpoints: _builder => ({}),
});

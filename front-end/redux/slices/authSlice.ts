import {PayloadAction, createSlice} from '@reduxjs/toolkit';
import UserDto from '../../models/user/UserDto';

export type authContext = {
  user: UserDto | null;
  accessToken: string | null;
  refreshToken: string | null;
};

const initialState: authContext = {
  user: null,
  accessToken: null,
  refreshToken: null,
};

const authSlice = createSlice({
  name: 'auth',
  initialState: initialState,
  reducers: {
    setAuthContext: (state, action: PayloadAction<authContext>) => {
      const {user, accessToken, refreshToken} = action.payload;
      state.user = user;
      state.accessToken = accessToken;
      state.refreshToken = refreshToken;
    },
    signOut: (state, _action: PayloadAction<void>) => {
      state.user = null;
      state.accessToken = null;
      state.refreshToken = null;
    },
  },
});

export const {setAuthContext, signOut} = authSlice.actions;

export default authSlice.reducer;

export const selectCurrentUser = (state: authContext) => state.user;
export const selectAccessToken = (state: authContext) => state.accessToken;
export const selectRefreshToken = (state: authContext) => state.refreshToken;

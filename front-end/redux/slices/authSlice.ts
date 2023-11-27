import {PayloadAction, createSlice} from '@reduxjs/toolkit';
import UserDto from '../../models/user/UserDto';
import {RootState} from '../store';

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
    clearAuthContext: (state, _action: PayloadAction<void>) => {
      state.user = null;
      state.accessToken = null;
      state.refreshToken = null;
    },
  },
});

export const {setAuthContext, clearAuthContext} = authSlice.actions;

export default authSlice.reducer;

export const selectCurrentUser = (state: RootState): UserDto | null =>
  state.auth.user;
export const selectCurrentUserId = (state: RootState) => state.auth.user?.id;

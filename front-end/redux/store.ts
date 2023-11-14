import {configureStore} from '@reduxjs/toolkit';
import {useDispatch} from 'react-redux';
import {apiSlice} from './slices/shopshareApiSlice';
import AuthReducer from './slices/authSlice';

export const store = configureStore({
  reducer: {
    auth: AuthReducer,
    [apiSlice.reducerPath]: apiSlice.reducer,
  },
  middleware: getDefaultMiddleware =>
    getDefaultMiddleware().concat(apiSlice.middleware),
  devTools: true,
});

// Infer the `RootState` and `AppDispatch` types from the store itself
export type RootState = ReturnType<typeof store.getState>;
// Inferred type: {posts: PostsState, comments: CommentsState, users: UsersState}
export type AppDispatch = typeof store.dispatch;

export const useAppDispatch: () => AppDispatch = useDispatch;

export default store;

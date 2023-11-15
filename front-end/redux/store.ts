import {configureStore} from '@reduxjs/toolkit';
import {useDispatch} from 'react-redux';
import {apiSlice} from './slices/shopshareApiSlice';
import AuthReducer from './slices/authSlice';
import {persistStore, persistReducer} from 'redux-persist';
import thunk from 'redux-thunk';
import {
  createImmutableStateInvariantMiddleware,
  createActionCreatorInvariantMiddleware,
} from '@reduxjs/toolkit';
// @ts-ignore
import createSensitiveStorage from 'redux-persist-sensitive-storage';

const isActionCreator = (
  action: unknown,
): action is Function & {type: unknown} =>
  typeof action === 'function' && 'type' in action;

const actionCreatorMiddleware = createActionCreatorInvariantMiddleware({
  isActionCreator,
});

const immutableInvariantMiddleware = createImmutableStateInvariantMiddleware({
  ignoredPaths: ['ignoredPath', 'ignoredNested.one', 'ignoredNested.two'],
});

// rpss
const storage = createSensitiveStorage({
  keychainService: 'myKeychain',
  sharedPreferencesName: 'mySharedPrefs',
});

const persistConfig = {
  key: 'user',
  storage: storage,
  blacklist: ['api'],
};

const persistedAuthReducer = persistReducer(persistConfig, AuthReducer);

export const store = configureStore({
  reducer: {
    auth: persistedAuthReducer,
    [apiSlice.reducerPath]: apiSlice.reducer,
  },
  middleware: _getDefaultMiddleware => [
    immutableInvariantMiddleware,
    actionCreatorMiddleware,
    thunk,
    apiSlice.middleware,
  ],
  devTools: true,
});

export const persistor = persistStore(store);

// Infer the `RootState` and `AppDispatch` types from the store itself
export type RootState = ReturnType<typeof store.getState>;
// Inferred type: {posts: PostsState, comments: CommentsState, users: UsersState}
export type AppDispatch = typeof store.dispatch;

export const useAppDispatch: () => AppDispatch = useDispatch;

export default store;

import React from 'react';
import {View, Text, Button} from 'react-native';
import {SafeAreaView} from 'react-native-safe-area-context';
import {useAppDispatch} from '../redux/store';
import {useSignOutMutation} from '../redux/slices/authApiSlice';
import {clearAuthContext} from '../redux/slices/authSlice';

const AccountsScreen = () => {
  const dispatch = useAppDispatch();

  const [signOut] = useSignOutMutation();
  // An example of how to logout
  const handleSignOut = () => {
    signOut()
      .unwrap()
      .then(_res => {
        console.log('signing out...');
        dispatch(clearAuthContext());
      })
      .catch(err => {
        console.log('There was an error signing out.');
        console.log(err);
      });
  };
  return (
    <SafeAreaView>
      <Text>Hi</Text>
      <View>
        <Button title="Log Out" onPress={handleSignOut} />
      </View>
    </SafeAreaView>
  );
};
export default AccountsScreen;

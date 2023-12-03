import {useNavigation} from '@react-navigation/native';
import {NativeStackScreenProps} from '@react-navigation/native-stack';
import React, {useState} from 'react';
import {
  Button,
  Modal,
  StyleSheet,
  Text,
  TextInput,
  TouchableOpacity,
  View,
} from 'react-native';
import {SafeAreaView} from 'react-native-safe-area-context';
import {useSelector} from 'react-redux';
import COLORS from '../../constants/colors';
import {useSignOutMutation} from '../../redux/slices/authApiSlice';
import {
  clearAuthContext,
  selectCurrentUser,
} from '../../redux/slices/authSlice';
import {useAppDispatch} from '../../redux/store';
import {AccountStackParamList} from '../types';

type AccountsScreenPropsType = NativeStackScreenProps<
  AccountStackParamList,
  'Settings'
>;

export type AccountsScreenNavigationProp =
  AccountsScreenPropsType['navigation'];

const AccountsScreen: React.FC<AccountsScreenPropsType> = props => {
  const [isPasswordShown, setIsPasswordShown] = useState(false);
  const [modalVisible, setModalVisible] = useState(false);
  const navigation = useNavigation<AccountsScreenNavigationProp>();

  const user = useSelector(selectCurrentUser);

  const dispatch = useAppDispatch();
  const [signOut] = useSignOutMutation();

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
    <SafeAreaView
      style={{
        backgroundColor: COLORS.white,
        flex: 1,
        paddingLeft: 10,
        paddingRight: 10,
        marginTop: -30,
      }}>
      <Modal
        transparent={true}
        visible={modalVisible}
        onRequestClose={() => setModalVisible(visible => !visible)}>
        <View style={{flex: 1, justifyContent: 'center', alignItems: 'center'}}>
          <View
            style={{
              backgroundColor: 'white',
              padding: 20,
              height: 200,
              width: 350,
              borderWidth: 2,
              borderColor: COLORS.secondary,
              borderRadius: 10,
              justifyContent: 'center',
              alignContent: 'center',
              alignItems: 'center',
            }}>
            <Text
              style={{
                fontSize: 18,
                justifyContent: 'center',
                alignContent: 'center',
                alignItems: 'center',
              }}>
              Enter their username
            </Text>
            <TextInput
              placeholder="Enter their username"
              style={{
                width: '100%',
                height: 40,
                borderColor: COLORS.black,
                borderWidth: 1,
                borderRadius: 8,
                alignItems: 'center',
                justifyContent: 'center',
                marginTop: 20,
                marginBottom: 15,
                paddingLeft: 10,
                paddingRight: 10,
              }}></TextInput>
            <Button
              title="Send Request"
              onPress={() => setModalVisible(false)}
            />
          </View>
        </View>
      </Modal>
      <View>
        <Text
          style={{
            fontSize: 22,
            fontWeight: 'bold',
            marginVertical: 0,
            color: COLORS.black,
          }}>
          Manage Invitations
        </Text>
      </View>
      <View style={{marginTop: 2, marginBottom: 2}}>
        <TouchableOpacity onPress={() => setModalVisible(true)}>
          <Text
            style={{
              fontSize: 18,
              fontWeight: '700',
              color: COLORS.primary,
              marginTop: 10,
            }}>
            Invite People
          </Text>
        </TouchableOpacity>
      </View>
      <View>
        <TouchableOpacity
          onPress={() => {
            navigation.navigate('Requests');
          }}>
          <Text
            style={{
              fontSize: 18,
              fontWeight: '700',
              color: COLORS.primary,
              marginTop: 10,
            }}>
            Invites
          </Text>
        </TouchableOpacity>
      </View>
      <View>
        <Text
          style={{
            fontSize: 22,
            fontWeight: 'bold',
            marginTop: 30,
            color: COLORS.black,
          }}>
          Edit Profile
        </Text>
      </View>
      <View style={styles.wrapperView}>
        <Text style={styles.label}>Username</Text>
        <View style={styles.inputWrapperView}>
          <Text style={styles.input}>{user?.username}</Text>
        </View>
      </View>
      <View style={styles.wrapperView}>
        <Text style={styles.label}>Email Address</Text>
        <View style={styles.inputWrapperView}>
          <Text style={styles.input}>{user?.email}</Text>
        </View>
      </View>
      <View style={{paddingLeft: 5}}>
        <TouchableOpacity
          onPress={() => {
            navigation.navigate('Change Password');
          }}>
          <Text
            style={{
              fontSize: 16,
              fontWeight: '600',
              color: COLORS.red,
            }}>
            Change Password
          </Text>
        </TouchableOpacity>
      </View>
      <View style={{marginTop: 90}}>
        <TouchableOpacity
          onPress={handleSignOut}
          style={{
            marginTop: 24,
            height: 50,
            borderRadius: 6,
            alignItems: 'center',
            justifyContent: 'center',
            backgroundColor: COLORS.secondary,
          }}>
          <Text
            style={{
              color: COLORS.white,
              fontWeight: '600',
              fontSize: 20,
            }}>
            Log Out
          </Text>
        </TouchableOpacity>
      </View>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  wrapperView: {
    marginBottom: 12,
  },
  label: {
    fontSize: 16,
    fontWeight: '400',
    marginVertical: 10,
  },
  inputWrapperView: {
    width: '100%',
    height: 60,
    borderColor: COLORS.black,
    borderWidth: 1,
    borderRadius: 8,
    alignItems: 'center',
    justifyContent: 'center',
    paddingLeft: 22,
  },
  input: {
    width: '100%',
    fontSize: 15,
  },
});
export default AccountsScreen;

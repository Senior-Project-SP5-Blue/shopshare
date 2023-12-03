import {useNavigation} from '@react-navigation/native';
import {NativeStackScreenProps} from '@react-navigation/native-stack';
import React, {useMemo, useState} from 'react';
import {
  Button,
  Modal,
  Platform,
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
} from 'react-native';
import {SelectList} from 'react-native-dropdown-select-list';
import {SafeAreaView} from 'react-native-safe-area-context';
import Toast from 'react-native-toast-message';
import {useSelector} from 'react-redux';
import SSTextInput from '../../components/SSTextInput';
import COLORS from '../../constants/colors';
import {useSignOutMutation} from '../../redux/slices/authApiSlice';
import {
  clearAuthContext,
  selectCurrentUser,
} from '../../redux/slices/authSlice';
import {
  useGetGroupsQuery,
  useInviteUserToGroupMutation,
} from '../../redux/slices/shopperGroupApiSlice';
import {useAppDispatch} from '../../redux/store';
import {AccountStackParamList} from '../types';

type AccountsScreenPropsType = NativeStackScreenProps<
  AccountStackParamList,
  'Settings'
>;

export type AccountsScreenNavigationProp =
  AccountsScreenPropsType['navigation'];

const AccountsScreen: React.FC<AccountsScreenPropsType> = _props => {
  const user = useSelector(selectCurrentUser);

  const [modalVisible, setModalVisible] = useState(false);
  const [newMemberUsername, setNewMemberUsername] = useState<string>();
  const [selectedGroup, setSelectedGroup] = useState<string>();
  const navigation = useNavigation<AccountsScreenNavigationProp>();

  const {data: groups} = useGetGroupsQuery({
    userId: user?.id!,
  });
  const dispatch = useAppDispatch();
  const [signOut] = useSignOutMutation();
  const [sendInvite] = useInviteUserToGroupMutation();

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

  const groupChoices = useMemo(() => {
    if (!groups) {
      return [];
    }
    return groups.map(x => ({
      key: `${x.id}`,
      value: x.name,
    }));
  }, [groups]);

  const getDefaultSelectedGroup = groupChoices.find(
    x => x.key === selectedGroup,
  );

  const handleInviteUser = async () => {
    if (!selectedGroup) {
      Toast.show({
        type: 'error',
        text1: 'Must select a group!',
      });
      return;
    }
    if (!newMemberUsername) {
      Toast.show({
        type: 'error',
        text1: 'Must enter a username!',
      });
      return;
    }

    sendInvite({
      userId: user!.id,
      groupId: selectedGroup,
      memberUsername: newMemberUsername,
    })
      .then(() => {
        Toast.show({
          type: 'success',
          text1: 'Successfully invited!',
          text2: `You invited ${newMemberUsername} to group ${
            groupChoices.find(x => x.key === selectedGroup)?.value
          }`,
        });
        setModalVisible(false);
      })
      .catch(() => {
        Toast.show({
          type: 'error',
          text1: 'Invalid Permissions',
          text2: 'You must be group admin to invite to group',
        });
      });
  };

  return (
    <SafeAreaView
      style={{
        flex: 1,
        backgroundColor: COLORS.white,
        paddingLeft: 10,
        paddingRight: 10,
        paddingTop: Platform.OS === 'ios' ? -30 : 20,
      }}>
      <Modal
        transparent={true}
        visible={modalVisible}
        onRequestClose={() => setModalVisible(visible => !visible)}>
        <View style={{flex: 1, justifyContent: 'center', alignItems: 'center'}}>
          <View
            style={{
              flexBasis: '70%',
              backgroundColor: 'white',
              padding: 20,
              height: 200,
              width: 350,
              borderWidth: 2,
              borderColor: COLORS.secondary,
              borderRadius: 10,
            }}>
            <Text style={{fontSize: 24, fontWeight: '700'}}>Invite User</Text>
            <SSTextInput
              label="Enter username"
              placeholder="Username"
              onChangeText={setNewMemberUsername}
              placeholderTextColor={COLORS.black}
            />
            <Text style={styles.label}>Group</Text>
            <SelectList
              placeholder="Select Group"
              search={false}
              defaultOption={getDefaultSelectedGroup}
              boxStyles={{
                borderColor: COLORS.black,
                borderWidth: 1,
                borderRadius: 6,
                height: 50,
                paddingHorizontal: 18,
              }}
              inputStyles={{fontSize: 18}}
              data={groupChoices}
              setSelected={(val: string) => setSelectedGroup(val)}
              save="key"
            />
            <View
              style={{
                justifyContent: 'space-evenly',
                gap: 10,
                marginTop: 12,
              }}>
              <Button title="Send Invite" onPress={handleInviteUser} />
              <Button title="Cancel" onPress={() => setModalVisible(false)} />
            </View>
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
            navigation.navigate('Invitations');
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
    height: 50,
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

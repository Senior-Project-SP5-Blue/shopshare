import {useNavigation} from '@react-navigation/native';
import React, {useState} from 'react';
import {Button, View} from 'react-native';
import Toast from 'react-native-toast-message';
import {useSelector} from 'react-redux';
import SSPasswordInput from '../../components/SSPasswordInput';
import COLORS from '../../constants/colors';
import {selectCurrentUserId} from '../../redux/slices/authSlice';
import {useChangePasswordMutation} from '../../redux/slices/userApiSlice';
import {ChangePasswordScreenPropsType} from '../types';

type ChangePasswordScreenNavigationProp =
  ChangePasswordScreenPropsType['navigation'];

const ChangePasswordScreen: React.FC<
  ChangePasswordScreenPropsType
> = _props => {
  const _userId = useSelector(selectCurrentUserId);
  const [isPasswordShown, setIsPasswordShown] = useState(false);
  const [currentPassword, setCurrentPassword] = useState<string>('');
  const [newPassword, setNewPassword] = useState<string>('');
  const [confirmPassword, setConfirmPassword] = useState<string>('');

  const navigation = useNavigation<ChangePasswordScreenNavigationProp>();

  const [savePasswordChange] = useChangePasswordMutation();

  const handleSavePasswordChange = async () => {
    if (!currentPassword) {
      Toast.show({
        type: 'error',
        text1: 'Must enter current password',
      });
      return;
    }
    if (!newPassword) {
      Toast.show({
        type: 'error',
        text1: 'Must enter new password',
      });
      return;
    }
    if (confirmPassword !== newPassword) {
      Toast.show({
        type: 'error',
        text1: 'Confirm password does not match',
      });
      return;
    }

    savePasswordChange({
      userId: _userId!,
      body: {
        currentPassword,
        newPassword,
        confirmPassword,
      },
    }).then(() => {
      Toast.show({
        type: 'success',
        text1: 'Changed password',
      });
      navigation.pop();
    });
  };
  return (
    <View
      style={{
        flex: 1,
        paddingTop: 12,
        paddingLeft: 15,
        paddingRight: 15,
        backgroundColor: COLORS.white,
      }}>
      <View>
        <SSPasswordInput
          label="Enter Current Password"
          placeholder="Enter Current Password"
          placeholderTextColor={COLORS.black}
          defaultValue={currentPassword}
          autoCapitalize="none"
          onChangeText={setCurrentPassword}
          secureTextEntry={!isPasswordShown}
          onShowPasswordPress={() => setIsPasswordShown(isShow => !isShow)}
        />
      </View>
      <View>
        <SSPasswordInput
          label="Enter New Password"
          placeholder="Enter New Password"
          placeholderTextColor={COLORS.black}
          defaultValue={newPassword}
          autoCapitalize="none"
          onChangeText={setNewPassword}
          secureTextEntry={!isPasswordShown}
          onShowPasswordPress={() => setIsPasswordShown(isShow => !isShow)}
        />
      </View>
      <View>
        <SSPasswordInput
          label="Confirm New Password"
          placeholder="Confirm New Password"
          placeholderTextColor={COLORS.black}
          defaultValue={confirmPassword}
          autoCapitalize="none"
          onChangeText={setConfirmPassword}
          secureTextEntry={!isPasswordShown}
          onShowPasswordPress={() => setIsPasswordShown(isShow => !isShow)}
        />
      </View>
      <View>
        <Button title="Save Changes" onPress={handleSavePasswordChange} />
      </View>
    </View>
  );
};

export default ChangePasswordScreen;

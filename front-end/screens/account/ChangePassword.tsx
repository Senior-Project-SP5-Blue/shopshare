import React, {useState} from 'react';
import {Button, View} from 'react-native';
import SSPasswordInput from '../../components/SSPasswordInput';
import COLORS from '../../constants/colors';

interface ChangePasswordProps {
  navigation: any;
}

const ChangePassword = (props: ChangePasswordProps) => {
  const [isPasswordShown, setIsPasswordShown] = useState(false);
  const [password, setPassword] = useState<string>();

  const change = () => props.navigation.navigate('Settings');
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
          defaultValue={password}
          autoCapitalize="none"
          onChangeText={setPassword}
          secureTextEntry={isPasswordShown}
          onShowPasswordPress={setIsPasswordShown}
        />
      </View>
      <View>
        <SSPasswordInput
          label="Enter New Password"
          placeholder="Enter New Password"
          placeholderTextColor={COLORS.black}
          defaultValue={password}
          autoCapitalize="none"
          onChangeText={setPassword}
          secureTextEntry={isPasswordShown}
          onShowPasswordPress={setIsPasswordShown}
        />
      </View>
      <View>
        <SSPasswordInput
          label="Confirm New Password"
          placeholder="Confirm New Password"
          placeholderTextColor={COLORS.black}
          defaultValue={password}
          autoCapitalize="none"
          onChangeText={setPassword}
          secureTextEntry={isPasswordShown}
          onShowPasswordPress={setIsPasswordShown}
        />
      </View>
      <View>
        <Button title="Save Changes" onPress={change} />
      </View>
    </View>
  );
};
export default ChangePassword;

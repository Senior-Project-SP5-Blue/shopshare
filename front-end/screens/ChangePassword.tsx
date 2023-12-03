import React, { useState } from 'react';
import {Button, Text, TextInput, TouchableOpacity, View} from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import COLORS from '../constants/colors';
import SSPasswordInput from '../components/SSPasswordInput';

interface ChangePasswordProps {
  navigation: any;
  // email: string;
}


const ChangePassword = (props:ChangePasswordProps) => {
  const [isPasswordShown, setIsPasswordShown] = useState(false);
  const [password, setPassword] = useState<string>();

  const change = () => props.navigation.navigate("Settings" )
    return (
    <SafeAreaView style={{backgroundColor:COLORS.white, borderRadius:15}}>
      <View style={{
        marginTop:-40,
        paddingLeft: 15,
        paddingRight: 15,
        backgroundColor: COLORS.white
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
    </SafeAreaView>
  );
}
export default ChangePassword

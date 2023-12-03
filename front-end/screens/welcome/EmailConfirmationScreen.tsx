import {useNavigation} from '@react-navigation/native';
import {NativeStackScreenProps} from '@react-navigation/native-stack';
import React from 'react';
import {SafeAreaView, Text, TouchableOpacity, View} from 'react-native';
import COLORS from '../../constants/colors';
import {AuthStackParamList} from '../types';
import Toast from 'react-native-toast-message';

type EmailConfirmationScreenPropsType = NativeStackScreenProps<
  AuthStackParamList,
  'EmailConfirmation'
>;

type EmailConfirmationScreenNavigationProp =
  EmailConfirmationScreenPropsType['navigation'];
const EmailConfirmationScreen: React.FC<
  EmailConfirmationScreenPropsType
> = _props => {
  const navigation = useNavigation<EmailConfirmationScreenNavigationProp>();
  return (
    <>
      <Toast />
      <SafeAreaView>
        <View
          style={{
            paddingTop: 20,
            paddingLeft: 15,
            paddingRight: 15,
          }}>
          <Text
            style={{
              fontSize: 18,
              fontWeight: '400',
            }}>
            A confirmation email has been sent.
          </Text>
        </View>
        <View
          style={{
            paddingLeft: 30,
            paddingRight: 30,
          }}>
          <TouchableOpacity
            onPress={() => navigation.navigate('Login')}
            style={{
              marginTop: 24,
              height: 40,
              alignItems: 'center',
              justifyContent: 'center',
            }}>
            <Text
              style={{
                color: COLORS.blue,
                fontWeight: '400',
                fontSize: 20,
              }}>
              Okay
            </Text>
          </TouchableOpacity>
        </View>
      </SafeAreaView>
    </>
  );
};
export default EmailConfirmationScreen;

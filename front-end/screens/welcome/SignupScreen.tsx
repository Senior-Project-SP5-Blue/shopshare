import CheckBox from '@react-native-community/checkbox';
import {useNavigation} from '@react-navigation/native';
import React, {useState} from 'react';
import {Platform, Pressable, Text, TextInput, View} from 'react-native';
import Button from '../../components/Button';
import KeyboardAvoidingContainer from '../../components/KeyboardAvoidingContainer';
import SSPasswordInput from '../../components/SSPasswordInput';
import SSTextInput from '../../components/SSTextInput';
import COLORS from '../../constants/colors';
import {useSignUpMutation} from '../../redux/slices/authApiSlice';
import {AuthApiSignUpReq} from '../../redux/types';
import {NativeStackScreenProps} from '@react-navigation/native-stack';
import {AuthStackParamList} from '../types';
import Toast from 'react-native-toast-message';

type SignUpScreenPropsType = NativeStackScreenProps<
  AuthStackParamList,
  'SignUp'
>;
type SignUpScreenNavigationProp = SignUpScreenPropsType['navigation'];
const SignupScreen: React.FC<SignUpScreenPropsType> = _props => {
  const [isPasswordShown, setIsPasswordShown] = useState(false);
  const [isChecked, setIsChecked] = useState(false);
  const [signUpReq, setSignUpReq] = useState<AuthApiSignUpReq>({
    firstName: '',
    lastName: '',
    username: '',
    email: '',
    number: '',
    password: '',
  });
  const [signUp] = useSignUpMutation();
  const navigation = useNavigation<SignUpScreenNavigationProp>();

  const handleSignUp = () => {
    signUp(signUpReq)
      .unwrap()
      .then(_res => {
        navigation.navigate('EmailConfirmation');
      });
  };

  return (
    <>
      <Toast />
      <KeyboardAvoidingContainer
        backgroundColor="white"
        style={{backgroundColor: COLORS.white}}>
        <View>
          <Pressable onPress={() => navigation.navigate('Welcome')}>
            <Text
              style={{
                fontSize: 20,
                color: COLORS.primary,
                fontWeight: 'bold',
                marginLeft: 5,
                marginTop: Platform.OS === 'android' ? 25 : 0,
              }}>
              Back
            </Text>
          </Pressable>
        </View>
        <View style={{marginVertical: 5}}>
          <Text
            style={{
              fontSize: 22,
              fontWeight: 'bold',
              marginVertical: 1,
              color: COLORS.black,
            }}>
            Create Your Account
          </Text>
        </View>
        <SSTextInput
          label="First Name"
          placeholder="Enter your first name"
          placeholderTextColor={COLORS.black}
          onChangeText={firstName => setSignUpReq({...signUpReq, firstName})}
        />
        <SSTextInput
          label="Last Name"
          placeholder="Enter your last name"
          placeholderTextColor={COLORS.black}
          onChangeText={lastName => setSignUpReq({...signUpReq, lastName})}
        />
        <SSTextInput
          label="Username"
          placeholder="Enter your username"
          placeholderTextColor={COLORS.black}
          onChangeText={username => setSignUpReq({...signUpReq, username})}
        />
        <SSTextInput
          label="Email Address"
          placeholder="Enter your email address"
          placeholderTextColor={COLORS.black}
          keyboardType="email-address"
          onChangeText={email => setSignUpReq({...signUpReq, email})}
        />
        <View
          style={{
            marginBottom: 12,
          }}>
          <Text
            style={{
              fontSize: 16,
              fontWeight: '400',
              marginVertical: 10,
            }}>
            Mobile Number
          </Text>

          <View
            style={{
              width: '100%',
              height: 50,
              borderColor: COLORS.black,
              borderWidth: 1,
              borderRadius: 8,
              alignItems: 'center',
              flexDirection: 'row',
              justifyContent: 'space-between',
              paddingLeft: 22,
            }}>
            <TextInput
              placeholder="+1"
              placeholderTextColor={COLORS.black}
              keyboardType="number-pad"
              style={{
                width: '12%',
                fontSize: 15,
                borderRightWidth: 1,
                height: '100%',
              }}
            />
            <TextInput
              placeholder="Enter your phone number"
              placeholderTextColor={COLORS.black}
              keyboardType="numeric"
              style={{
                width: '80%',
              }}
              onChangeText={number => setSignUpReq({...signUpReq, number})}
            />
          </View>
        </View>
        <SSPasswordInput
          label="Password"
          placeholder="Enter your password"
          placeholderTextColor={COLORS.black}
          secureTextEntry={isPasswordShown}
          autoCapitalize="none"
          onShowPasswordPress={setIsPasswordShown}
          onChangeText={password => setSignUpReq({...signUpReq, password})}
        />

        <View style={{marginVertical: 4, flexDirection: 'row'}}>
          <CheckBox
            style={{flex: 1, padding: 10, width: 15, height: 15}}
            value={isChecked}
            onValueChange={setIsChecked}
            boxType="square"
            onCheckColor="#39B68D"
          />
          <Text
            style={{
              fontSize: 15,
              marginTop: 0,
              marginLeft: 10,
            }}>
            I agree to the terms and conditions.
          </Text>
        </View>
        <Button
          onPress={handleSignUp}
          title="Sign Up"
          filled
          style={{
            marginTop: 18,
            marginBottom: 4,
          }}
        />
        <View
          style={{
            flexDirection: 'row',
            alignItems: 'center',
            marginVertical: 20,
          }}>
          <View
            style={{
              flex: 1,
              height: 1,
              backgroundColor: COLORS.grey,
              marginHorizontal: 1,
            }}
          />
          <Text style={{fontSize: 15}}>Already have an account ?</Text>
          <Pressable onPress={() => navigation.navigate('Login')}>
            <Text
              style={{
                fontSize: 18,
                color: COLORS.primary,
                fontWeight: 'bold',
                marginLeft: 4,
              }}>
              Login
            </Text>
          </Pressable>
          <View
            style={{
              flex: 1,
              height: 1,
              backgroundColor: COLORS.grey,
              marginHorizontal: 1,
            }}
          />
        </View>
      </KeyboardAvoidingContainer>
    </>
  );
};
export default SignupScreen;

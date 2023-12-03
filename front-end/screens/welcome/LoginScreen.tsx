import React, {useState} from 'react';
import {
  Image,
  ImageBackground,
  Platform,
  Text,
  TouchableOpacity,
  View,
} from 'react-native';
import Toast from 'react-native-toast-message';
import Button from '../../components/Button';
import KeyboardAvoidingContainer from '../../components/KeyboardAvoidingContainer';
import SSPasswordInput from '../../components/SSPasswordInput';
import SSTextInput from '../../components/SSTextInput';
import COLORS from '../../constants/colors';
import {useSignInMutation} from '../../redux/slices/authApiSlice';
import {setAuthContext} from '../../redux/slices/authSlice';
import {useAppDispatch} from '../../redux/store';

interface LoginScreenProps {
  navigation: any;
}

const LoginScreen = (props: LoginScreenProps) => {
  const [isPasswordShown, setIsPasswordShown] = useState(false);
  const home = () => props.navigation.navigate('Welcome');

  const [email, setEmail] = useState<string>();
  const [password, setPassword] = useState<string>();
  const dispatch = useAppDispatch();
  const [signIn] = useSignInMutation();

  const handleSignIn = (email?: string, password?: string) => {
    if (!(email && password)) {
      return;
    }
    signIn({email, password})
      .unwrap()
      .then(userData => {
        dispatch(setAuthContext({...userData, user: userData.userContext}));
        setEmail('');
        setPassword('');
      })
      .catch(_err => {
        Toast.show({
          type: 'error',
          text1: 'Invalid username or password',
        });
      });
  };

  return (
    <>
      <Toast />
      <ImageBackground
        style={{
          flex: 1,
          position: 'absolute',
          height: '100%',
          width: '100%',
        }}
        source={require('../../assets/background.png')}
      />
      <KeyboardAvoidingContainer
        scrollEnabled={false}
        style={{overflow: 'hidden'}}>
        <View>
          <View>
            <TouchableOpacity onPress={home}>
              <Text
                style={{
                  fontSize: 20,
                  color: COLORS.white,
                  fontWeight: 'bold',
                  marginLeft: 8,
                  marginTop: Platform.OS === 'android' ? 25 : 0,
                }}>
                Back
              </Text>
            </TouchableOpacity>
          </View>
          <View style={{justifyContent: 'center', top: 100, marginBottom: 250}}>
            <Text
              style={{
                fontSize: 50,
                fontWeight: '800',
                color: COLORS.white,
                textAlign: 'center',
              }}>
              Welcome Back
            </Text>
          </View>
        </View>
        <View>
          <SSTextInput
            label="Email"
            placeholder="Enter your email"
            onChangeText={setEmail}
            placeholderTextColor={COLORS.black}
          />
          <SSPasswordInput
            label="Password"
            placeholder="Enter your password"
            placeholderTextColor={COLORS.black}
            defaultValue={password}
            autoCapitalize="none"
            onChangeText={setPassword}
            secureTextEntry={isPasswordShown}
            onShowPasswordPress={setIsPasswordShown}
          />
          <Button
            title="Login"
            filled
            style={{marginTop: 20, marginBottom: 4}}
            onPress={() => handleSignIn(email, password)}
          />
          <View style={{flex: 1, zIndex: 0}}>
            <Image
              style={{
                top: -4,
                width: '100%',
                height: 300,
                alignItems: 'center',
                justifyContent: 'center',
              }}
              resizeMode="cover"
              source={require('../../assets/loginpic2.jpg')}
            />
          </View>
        </View>
      </KeyboardAvoidingContainer>
    </>
  );
};

export default LoginScreen;

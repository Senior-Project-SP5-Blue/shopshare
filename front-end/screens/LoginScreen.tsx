import React, {useState} from 'react';
import {
  Image,
  ImageBackground,
  KeyboardAvoidingView,
  Platform,
  StatusBar,
  Text,
  TextInput,
  TouchableOpacity,
  View,
} from 'react-native';
import {SafeAreaView} from 'react-native-safe-area-context';
import COLORS from '../constants/colors';
import {useSignInMutation} from '../redux/slices/authApiSlice';
import {useAppDispatch} from '../redux/store';
import {setAuthContext} from '../redux/slices/authSlice';
import Button from '../components/Button';
import SSTextInput from '../components/SSTextInput';
import SSPasswordInput from '../components/SSPasswordInput';
import KeyboardAvoidingContainer from '../components/KeyboardAvoidingContainer';
import {Colors} from 'react-native/Libraries/NewAppScreen';

interface LoginScreenProps {
  navigation: any;
}

const LoginScreen = (props: LoginScreenProps) => {
  const [isPasswordShown, setIsPasswordShown] = useState(false);
  const home = () => props.navigation.navigate('Welcome');
  const list = () => props.navigation.navigate('Lists')
  const account = () => props.navigation.navigate('Settings')

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
      .catch(err => {
        console.log('ERROR!! ');
        console.log(err);
      });
  };

  return (
    <>
      <ImageBackground
        style={{
          flex: 1,
          position: 'absolute',
          // right: 0,
          // top: 0,
          // zIndex: 1,
          height: '100%',
          width: '100%',
        }}
        source={require('../assets/background.png')}
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
            <Text style={{fontSize: 50, fontWeight: '800', color: COLORS.white, textAlign: 'center'}}>
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
            //onPress={account}
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
              source={require('../assets/loginpic2.jpg')}
            />
          </View>
        </View>
      </KeyboardAvoidingContainer>
    </>
  );
};

export default LoginScreen;

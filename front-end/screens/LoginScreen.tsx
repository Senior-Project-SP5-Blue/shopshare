import React, {useState} from 'react';
import {
  View,
  ImageBackground,
  Text,
  TextInput,
  TouchableOpacity,
  Image,
} from 'react-native';
import {SafeAreaView} from 'react-native-safe-area-context';
import {StatusBar} from 'react-native';
import COLORS from '../constants/colors';
import Button from '../components/Button';
import {useSignInMutation} from '../redux/slices/authApiSlice';
import {setAuthContext} from '../redux/slices/authSlice';
import {useAppDispatch} from '../redux/store';
import {AuthApiSignInReq} from '../redux/types';

interface LoginScreenProps {
  navigation: any;
}

const LoginScreen = (props: LoginScreenProps) => {
  const [isPasswordShown, setIsPasswordShown] = useState(false);
  const lists = () => props.navigation.navigate('Lists');
  const home = () => props.navigation.navigate('Welcome');
  const [email, setEmail] = useState<string>('');
  const [password, setPassword] = useState<string>('');
  const dispatch = useAppDispatch();
  const [signIn, {status, isLoading, isError, isSuccess}] = useSignInMutation();

  const handleSignIn = (email: string, password: string) => {
    signIn({email, password} as AuthApiSignInReq)
      .unwrap()
      .then(userData => {
        dispatch(setAuthContext({...userData, user: userData.userContext}));
        setEmail('');
        setPassword('');
        lists();
      })
      .catch(err => {
        console.log('ERROR!! ');
        console.log(err);
      });
  };

  return (
    <SafeAreaView style={{flex: 1, backgroundColor: COLORS.white}}>
      <StatusBar barStyle="light-content" />
      <ImageBackground
        style={{flex: 1, position: 'absolute', height: '100%', width: '100%'}}
        source={require('../assets/background.png')}
      />
      <View style={{flex: 1, marginHorizontal: 18}}>
        <View>
          <TouchableOpacity onPress={home}>
            <Text
              style={{
                fontSize: 20,
                color: COLORS.white,
                fontWeight: 'bold',
                marginLeft: 8,
              }}>
              Back
            </Text>
          </TouchableOpacity>
        </View>
        <View style={{justifyContent: 'center', top: 100}}>
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
        <View style={{top: 350}}>
          <Text style={{fontSize: 16, fontWeight: '400', marginVertical: 10}}>
            Email or Username
          </Text>
          <View
            style={{
              width: '100%',
              height: 60,
              borderColor: COLORS.black,
              borderWidth: 1,
              borderRadius: 8,
              alignItems: 'center',
              justifyContent: 'center',
              padding: 22,
            }}>
            <TextInput
              placeholder="Enter your username or email"
              defaultValue={email}
              onChangeText={newEmail => setEmail(newEmail)}
              placeholderTextColor={COLORS.black}
              style={{width: '100%', fontSize: 15}}
            />
          </View>
        </View>
        <View style={{marginBottom: 12, marginTop: 380}}>
          <Text style={{fontSize: 16, fontWeight: '400', marginVertical: 10}}>
            Password
          </Text>
          <View
            style={{
              width: '100%',
              height: 60,
              borderColor: COLORS.black,
              borderWidth: 1,
              borderRadius: 8,
              alignItems: 'center',
              justifyContent: 'center',
              padding: 22,
            }}>
            <TextInput
              placeholder="Enter your password"
              placeholderTextColor={COLORS.black}
              defaultValue={password}
              autoCapitalize="none"
              onChangeText={newPassword => setPassword(newPassword)}
              secureTextEntry={isPasswordShown}
              style={{width: '100%', fontSize: 15}}
            />
            <TouchableOpacity
              onPress={() => setIsPasswordShown(!isPasswordShown)}
              style={{position: 'absolute', right: 12}}>
              {isPasswordShown ? (
                <Image
                  source={require('../assets/eye2.png')}
                  style={{
                    height: 33,
                    resizeMode: 'contain',
                    width: 40,
                    borderRadius: 20,
                    position: 'absolute',
                    top: -20,
                    right: -5,
                  }}
                />
              ) : (
                <Image
                  source={require('../assets/eye.png')}
                  style={{
                    height: 33,
                    resizeMode: 'contain',
                    width: 40,
                    borderRadius: 20,
                    position: 'absolute',
                    top: -20,
                    right: -5,
                  }}
                />
              )}
            </TouchableOpacity>
          </View>
        </View>
        <Button
          title="Login"
          filled
          style={{marginTop: 20, marginBottom: 4}}
          onPress={() => handleSignIn(email, password)}
        />
        <View>
          <Image
            style={{
              top: -4,
              width: 445,
              height: 300,
              alignItems: 'center',
              justifyContent: 'center',
            }}
            resizeMode="cover"
            source={require('../assets/loginpic2.jpg')}
          />
        </View>
      </View>
    </SafeAreaView>
  );
};

export default LoginScreen;

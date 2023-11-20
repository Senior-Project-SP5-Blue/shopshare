import {
  Image,
  Pressable,
  Text,
  TextInput,
  TouchableOpacity,
  View,
} from 'react-native';
import React, {useState} from 'react';
import {SafeAreaView} from 'react-native-safe-area-context';
import COLORS from '../constants/colors';
import CheckBox from '@react-native-community/checkbox';
import Button from '../components/Button';
import {ScrollView} from 'react-native';
import {AuthApiSignUpReq} from '../redux/types';
import {useSignUpMutation} from '../redux/slices/authApiSlice';
import {useNavigation} from '@react-navigation/native';

interface SignUpScreenProps {
  navigation: any;
}
const SignupScreen = (props: SignUpScreenProps) => {
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
  const navigation = useNavigation();
  const lists = () => props.navigation.navigate('Lists');
  const login = () => props.navigation.navigate('Login');
  const home = () => props.navigation.navigate('Welcome');
  // fix later
  const emailConfirm = () => navigation.navigate('EmailConfirmation' as never);

  const handleSignUp = () => {
    signUp(signUpReq)
      .unwrap()
      .then(_res => {
        emailConfirm();
      });
  };

  return (
    <SafeAreaView style={{flex: 1, backgroundColor: COLORS.white}}>
      <ScrollView style={{flex: 1, marginHorizontal: 22}}>
        <View>
          <Pressable onPress={home}>
            <Text
              style={{
                fontSize: 20,
                color: COLORS.primary,
                fontWeight: 'bold',
                marginLeft: 5,
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
        <View style={{marginBottom: 0}}>
          <Text
            style={{
              fontSize: 16,
              fontWeight: '400',
              marginVertical: 10,
            }}>
            First Name
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
              placeholder="Enter your first name"
              placeholderTextColor={COLORS.black}
              style={{
                width: '100%',
                fontSize: 15,
              }}
              onChangeText={firstName =>
                setSignUpReq({...signUpReq, firstName})
              }
            />
          </View>
        </View>
        <View style={{marginBottom: 12}}>
          <Text
            style={{
              fontSize: 16,
              fontWeight: '400',
              marginVertical: 10,
            }}>
            Last Name
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
              placeholder="Enter your last name"
              placeholderTextColor={COLORS.black}
              style={{
                width: '100%',
                fontSize: 15,
              }}
              onChangeText={lastName => setSignUpReq({...signUpReq, lastName})}
            />
          </View>
        </View>
        <View style={{marginBottom: 0}}>
          <Text
            style={{
              fontSize: 16,
              fontWeight: '400',
              marginVertical: 10,
            }}>
            Username
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
              placeholder="Enter your username"
              placeholderTextColor={COLORS.black}
              style={{
                width: '100%',
                fontSize: 15,
              }}
              onChangeText={username => setSignUpReq({...signUpReq, username})}
            />
          </View>
        </View>
        <View style={{marginBottom: 12}}>
          <Text
            style={{
              fontSize: 16,
              fontWeight: '400',
              marginVertical: 10,
            }}>
            Email Address
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
              placeholder="Enter your email address"
              placeholderTextColor={COLORS.black}
              keyboardType="email-address"
              style={{
                width: '100%',
                fontSize: 15,
              }}
              onChangeText={email => setSignUpReq({...signUpReq, email})}
            />
          </View>
        </View>

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
              height: 60,
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
              keyboardType="numeric"
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
        <View style={{marginBottom: 12}}>
          <Text
            style={{
              fontSize: 16,
              fontWeight: '400',
              marginVertical: 10,
            }}>
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
              secureTextEntry={isPasswordShown}
              autoCapitalize="none"
              style={{
                width: '100%',
                fontSize: 15,
              }}
              onChangeText={password => setSignUpReq({...signUpReq, password})}
            />
            <TouchableOpacity
              onPress={() => setIsPasswordShown(!isPasswordShown)}
              style={{
                position: 'absolute',
                right: 12,
              }}>
              {isPasswordShown == true ? (
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
          <Pressable onPress={login}>
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
      </ScrollView>
    </SafeAreaView>
  );
};
export default SignupScreen;

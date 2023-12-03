import React from 'react';
import {Image, Pressable, Text, View} from 'react-native';
import {LinearGradient} from 'react-native-linear-gradient';
import {useSafeAreaInsets} from 'react-native-safe-area-context';
import Button from '../../components/Button';
import COLORS from '../../constants/colors';
import Toast from 'react-native-toast-message';

interface WelcomeScreenProps {
  navigation: any;
}
const WelcomeScreen = (props: WelcomeScreenProps) => {
  const signup = () => props.navigation.navigate('SignUp');
  const login = () => props.navigation.navigate('Login');
  const insets = useSafeAreaInsets();

  return (
    <>
      <Toast />
      <LinearGradient
        style={{
          flex: 1,
          paddingBottom: insets.bottom,
        }}
        colors={[COLORS.secondary, COLORS.primary1]}>
        <View style={{flex: 1}}>
          <View>
            <Image
              source={require('../../assets/icon1.png')}
              style={{
                height: 110,
                width: 110,
                borderRadius: 20,
                position: 'absolute',
                top: 10,
                left: 10,
                transform: [
                  {translateX: 20},
                  {translateY: 50},
                  {rotate: '-15deg'},
                ],
              }}
            />
            <Image
              source={require('../../assets/icon2.png')}
              style={{
                height: 110,
                width: 110,
                borderRadius: 20,
                position: 'absolute',
                top: 40,
                left: 150,
                transform: [
                  {translateX: 50},
                  {translateY: 50},
                  {rotate: '-5deg'},
                ],
              }}
            />

            <Image
              source={require('../../assets/icon3.png')}
              style={{
                height: 120,
                width: 120,
                borderRadius: 20,
                position: 'absolute',
                top: 190,
                left: -10,
                transform: [
                  {translateX: 50},
                  {translateY: 50},
                  {rotate: '15deg'},
                ],
              }}
            />

            <Image
              source={require('../../assets/grocery2.png')}
              style={{
                height: 190,
                width: 190,
                borderRadius: 20,
                position: 'absolute',
                top: 190,
                left: 160,
                transform: [
                  {translateX: 50},
                  {translateY: 50},
                  {rotate: '-15deg'},
                ],
              }}
            />
          </View>
          <View
            style={{
              paddingHorizontal: 22,
              position: 'absolute',
              top: 450,
              width: '100%',
            }}>
            <Text
              style={{
                fontSize: 50,
                fontWeight: '800',
                color: COLORS.white,
              }}>
              Let's Get Started
            </Text>

            <View style={{marginVertical: 15}}>
              <Text
                style={{
                  fontSize: 17,
                  color: COLORS.white,
                  marginVertical: 15,
                }}>
                Develop your personal grocery lists
              </Text>

              <Text
                style={{
                  fontSize: 17,
                  color: COLORS.white,
                }}>
                OR share your grocery lists with other individuals{' '}
              </Text>
            </View>

            <Button
              title="Join Now"
              onPress={signup}
              style={{
                marginTop: 22,
                width: '100%',
              }}
            />

            <View
              style={{
                flexDirection: 'row',
                marginTop: 30,
                justifyContent: 'center',
              }}>
              <Text
                style={{
                  fontSize: 17,
                  color: COLORS.white,
                }}>
                Already have an account ?
              </Text>

              <Pressable onPress={login}>
                <Text
                  style={{
                    fontSize: 18,
                    color: COLORS.white,
                    fontWeight: 'bold',
                    marginLeft: 4,
                  }}>
                  Login
                </Text>
              </Pressable>
            </View>
          </View>
        </View>
      </LinearGradient>
    </>
  );
};
export default WelcomeScreen;

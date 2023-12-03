import React from 'react';
import {SafeAreaView, Text, TouchableOpacity, View} from 'react-native';
import COLORS from '../constants/colors';

interface EmailConfirmationScreenProps {
  navigation: any;
  // email: string;
}

const EmailConfirmationScreen: React.FC<
  EmailConfirmationScreenProps
> = _props => {
  const back = () => _props.navigation.navigate("SignUp" )
    return (
    <SafeAreaView>
      <View style={{
        marginTop:-40,
        paddingLeft: 15,
        paddingRight: 15
      }}> 
      <Text style={{
        fontSize: 18,
        fontWeight: '400'
      }}
      >A confirmation email has been sent.</Text>
      </View>
      <View style={{
        paddingLeft:30,
        paddingRight:30
      }}>
      <TouchableOpacity
        onPress={back}
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
  );
}
export default EmailConfirmationScreen;

import React from 'react';
import {Text, View} from 'react-native';

interface EmailConfirmationScreenProps {
  navigation: any;
  // email: string;
}

const EmailConfirmationScreen: React.FC<
  EmailConfirmationScreenProps
> = _props => {
  return (
    <View>
      <Text>A email confirmation has been sent.</Text>
    </View>
  );
};

export default EmailConfirmationScreen;

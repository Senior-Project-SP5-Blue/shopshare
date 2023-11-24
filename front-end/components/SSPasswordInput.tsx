import React from 'react';
import {
  Image,
  StyleSheet,
  Text,
  TextInput,
  TextInputProps,
  TouchableOpacity,
  View,
} from 'react-native';
import COLORS from '../constants/colors';

interface SSPasswordInputProps {
  label?: string;
  onShowPasswordPress: (i: boolean) => void;
}

const SSPasswordInput: React.FC<
  SSPasswordInputProps & TextInputProps
> = props => {
  const {onShowPasswordPress, label, secureTextEntry} = props;
  return (
    <View style={styles.wrapperView}>
      <Text style={styles.label}>{label}</Text>
      <View style={styles.inputWrapperView}>
        <TextInput
          secureTextEntry={secureTextEntry}
          autoCapitalize="none"
          {...props}
        />
        <TouchableOpacity
          onPress={() => onShowPasswordPress(!secureTextEntry)}
          style={styles.passwordVisibilityEmblemOpacity}>
          {secureTextEntry === true ? (
            <Image
              source={require('../assets/eye2.png')}
              style={styles.hidePasswordEmblem}
            />
          ) : (
            <Image
              source={require('../assets/eye.png')}
              style={styles.showPasswordEmblem}
            />
          )}
        </TouchableOpacity>
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  wrapperView: {
    marginBottom: 12,
  },
  label: {
    fontSize: 16,
    fontWeight: '400',
    marginVertical: 10,
  },
  inputWrapperView: {
    width: '100%',
    height: 60,
    borderColor: COLORS.black,
    borderWidth: 1,
    borderRadius: 8,
    // alignItems: 'center',
    justifyContent: 'center',
    paddingLeft: 22,
  },
  input: {
    width: '100%',
    fontSize: 15,
  },
  passwordVisibilityEmblemOpacity: {
    position: 'absolute',
    right: 12,
  },
  showPasswordEmblem: {
    height: 33,
    resizeMode: 'contain',
    width: 40,
    borderRadius: 20,
    position: 'absolute',
    top: -20,
    right: -5,
  },
  hidePasswordEmblem: {
    height: 33,
    resizeMode: 'contain',
    width: 40,
    borderRadius: 20,
    position: 'absolute',
    top: -20,
    right: -5,
  },
});

export default SSPasswordInput;

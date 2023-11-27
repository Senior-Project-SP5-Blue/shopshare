import React from 'react';
import {StyleSheet, Text, TextInput, TextInputProps, View} from 'react-native';
import COLORS from '../constants/colors';

interface SSTextInputProps {
  label?: string;
}

const SSTextInput: React.FC<SSTextInputProps & TextInputProps> = props => {
  return (
    <View style={styles.wrapperView}>
      <Text style={styles.label}>{props.label}</Text>
      <View style={styles.inputWrapperView}>
        <TextInput {...props} style={styles.input} />
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
    alignItems: 'center',
    justifyContent: 'center',
    paddingLeft: 22,
  },
  input: {
    width: '100%',
    fontSize: 15,
  },
});

export default SSTextInput;

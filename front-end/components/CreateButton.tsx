import React from 'react';
import {StyleSheet, TouchableOpacity, View} from 'react-native';
import PlusIcon from 'react-native-heroicons/mini/PlusIcon';
import COLORS from '../constants/colors';

interface CreateButtonProps {
  onPress: Function;
}

const CreateButton: React.FC<CreateButtonProps> = props => {
  return (
    <View>
      <TouchableOpacity
        style={styles.actionButton}
        onPress={() => props.onPress()}>
        <PlusIcon color={COLORS.white} />
      </TouchableOpacity>
    </View>
  );
};

const styles = StyleSheet.create({
  actionButton: {
    zIndex: 500,
    alignItems: 'center',
    justifyContent: 'center',
    width: 70,
    position: 'absolute',
    bottom: 50,
    right: 25,
    height: 70,
    backgroundColor: COLORS.primary1,
    borderRadius: 100,
  },
});

export default CreateButton;

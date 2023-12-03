import React from 'react';
import {StyleSheet, TouchableOpacity, View} from 'react-native';
import PlusIcon from 'react-native-heroicons/mini/PlusIcon';
import XMarkIcon from 'react-native-heroicons/mini/XMarkIcon';
import COLORS from '../constants/colors';

interface CreateButtonProps {
  onPress: Function;
  addActive: boolean;
}

const CreateButton: React.FC<CreateButtonProps> = ({addActive, onPress}) => {
  return (
    <View>
      <TouchableOpacity style={styles.actionButton} onPress={() => onPress()}>
        {addActive ? (
          <XMarkIcon color={COLORS.white} />
        ) : (
          <PlusIcon color={COLORS.white} />
        )}
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

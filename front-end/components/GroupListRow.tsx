import React from 'react';
import {StyleSheet, Text, TouchableOpacity, View} from 'react-native';
import TrashIcon from 'react-native-heroicons/mini/TrashIcon';
import COLORS from '../constants/colors';

interface GroupListRowProps {
  id: string;
  name: string;
}

const GroupListRow: React.FC<GroupListRowProps> = ({name}) => {
  return (
    <View style={styles.wrapper}>
      <Text style={styles.name}>{name}</Text>
      <TouchableOpacity style={styles.trash}>
        <TrashIcon color={COLORS.primary} />
      </TouchableOpacity>
    </View>
  );
};

const styles = StyleSheet.create({
  wrapper: {
    flex: 1,
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    paddingHorizontal: 22,
    paddingVertical: 12,
    overflow: 'hidden',
    borderBottomColor: '#dbdbdb',
    borderBottomWidth: 1,
    borderStyle: 'solid',
  },
  name: {
    fontSize: 18,
    fontWeight: '500',
    flexBasis: '75%',
    flexGrow: 0,
    textAlign: 'left',
  },
  trash: {
    flex: 1,
    alignItems: 'center',
    flexBasis: '10%',
    flexGrow: 0,
    alignContent: 'center',
  },
});

export default GroupListRow;

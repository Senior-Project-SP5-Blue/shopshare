import React from 'react';
import {StyleSheet, Text, TouchableOpacity, View} from 'react-native';
import UserMinusIcon from 'react-native-heroicons/mini/UserMinusIcon';
import COLORS from '../constants/colors';

interface GroupUserRowProps {
  username: string;
}

const GroupUserRow: React.FC<GroupUserRowProps> = ({username}) => {
  return (
    <View style={styles.wrapper}>
      <Text style={styles.name}>{username}</Text>
      {/* <TouchableOpacity style={styles.trash}>
        <UserMinusIcon color={COLORS.primary} />
      </TouchableOpacity> */}
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

export default GroupUserRow;

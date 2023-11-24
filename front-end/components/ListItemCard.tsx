import React, {PropsWithChildren} from 'react';
import {StyleSheet, Text, View} from 'react-native';
import ListItemDto from '../models/listitem/ListItemDto';
import COLORS from '../constants/colors';

interface ListItemCardProps {
  item: ListItemDto;
}

const ListItemCard: React.FC<PropsWithChildren<ListItemCardProps>> = ({
  item,
}) => {
  return (
    <View style={styles.wrapper}>
      <Text style={styles.name}>{item.name}</Text>
      <Text style={styles.status}>{item.status}</Text>
      <Text style={styles.locked}>{item.locked ? 'O' : 'X'}</Text>
    </View>
  );
};

const styles = StyleSheet.create({
  wrapper: {
    flexDirection: 'row',
    justifyContent: 'space-around',
    borderColor: COLORS.black,
    height: '8%',
    // width: ""
    borderBlockColor: 'black',
    borderRadius: 8,
    marginBottom: 10,
  },
  name: {
    fontWeight: '500',
    flexBasis: '75%',
    flexGrow: 0,
    textAlign: 'left',
    // flexShrink: 0,
  },
  status: {
    fontStyle: 'italic',
    flexBasis: '15%',
    flexGrow: 0,
    textAlign: 'center',
    // flexShrink: 0,
  },
  locked: {
    textDecorationLine: 'underline',
    flexBasis: '10%',
    flexGrow: 0,
    textAlign: 'center',
    // flexShrink: 0,
  },
});
export default ListItemCard;

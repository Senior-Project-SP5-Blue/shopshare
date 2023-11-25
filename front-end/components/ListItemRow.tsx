import React, {PropsWithChildren, useState} from 'react';
import {StyleSheet, Text, TextInput, View} from 'react-native';
import ListItemDto from '../models/listitem/ListItemDto';
import COLORS from '../constants/colors';
import CheckBox from '@react-native-community/checkbox';
import ListItemStatus from '../models/listitem/ListItemStatus';
import {Svg, SvgFromUri, SvgUri, SvgXml} from 'react-native-svg';
import {useSelector} from 'react-redux';
import {selectCurrentUserId} from '../redux/slices/authSlice';

interface ListItemRowProps {
  item: ListItemDto;
  // setSelectedItem: (item: ListItemDto) => void;
}

const ListItemRow: React.FC<PropsWithChildren<ListItemRowProps>> = ({
  item,
  // selectedItem,
  // setSelectedItem,
}) => {
  const userId = useSelector(selectCurrentUserId);
  const [completed, setCompleted] = useState<boolean>(
    item.status === ListItemStatus.COMPLETED,
  );
  return (
    <View style={styles.wrapper}>
      <TextInput
        style={[
          styles.name,
          {textDecorationLine: completed ? 'line-through' : 'none'},
        ]}
        defaultValue={item.name}
        editable={!item.locked || userId === item.createdBy}
        selectTextOnFocus={!item.locked || userId === item.createdBy}
      />
      <CheckBox
        boxType="square"
        onCheckColor="#39B68D"
        value={completed}
        onValueChange={val => setCompleted(val)}
        style={styles.checkbox}
        disabled={item.locked && userId !== item.createdBy}
      />
      <Text style={styles.locked}>{item.locked ? 'O' : 'X'}</Text>
    </View>
  );
};

const styles = StyleSheet.create({
  wrapper: {
    flex: 1,
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    // height: '100%',
    paddingHorizontal: 22,
    paddingVertical: 10,
    borderBlockColor: 'black',
    // borderRadius: 8,
    marginBottom: 10,
    borderBottomColor: '#eee',
    borderBottomWidth: 1,
    borderStyle: 'solid',
    overflow: 'hidden',
  },
  name: {
    fontSize: 18,
    fontWeight: '500',
    flexBasis: '75%',
    flexGrow: 0,
    textAlign: 'left',
    // paddingLeft: 22,
    // flexShrink: 0,
  },
  checkbox: {
    alignSelf: 'center',
  },
  locked: {
    fontSize: 18,
    flexBasis: '10%',
    flexGrow: 0,
    textAlign: 'center',
    // flexShrink: 0,
  },
});
export default ListItemRow;

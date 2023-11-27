import CheckBox from '@react-native-community/checkbox';
import React, {PropsWithChildren, useState} from 'react';
import {StyleSheet, TextInput, TouchableOpacity, View} from 'react-native';
import LockClosedIcon from 'react-native-heroicons/mini/LockClosedIcon';
import LockOpenIcon from 'react-native-heroicons/mini/LockOpenIcon';
import {useSelector} from 'react-redux';
import COLORS from '../constants/colors';
import ListItemDto from '../models/listitem/ListItemDto';
import ListItemStatus from '../models/listitem/ListItemStatus';
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
  const [locked, setLocked] = useState<boolean>(item.locked);
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
      <TouchableOpacity
        onPress={() => setLocked(!locked)}
        style={styles.locked}>
        {locked ? (
          <LockClosedIcon color={COLORS.primary} />
        ) : (
          <LockOpenIcon color={COLORS.primary} />
        )}
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
    borderBottomColor: '#dbdbdb',
    borderBottomWidth: 1,
    borderStyle: 'solid',
    overflow: 'hidden',
  },
  name: {
    fontSize: 18,
    fontWeight: '500',
    flexBasis: '70%',
    flexGrow: 0,
    textAlign: 'left',
  },
  checkbox: {
    alignSelf: 'center',
  },
  locked: {
    flex: 1,
    alignItems: 'center',
    flexBasis: 10,
    flexGrow: 0,
    alignContent: 'center',
  },
});
export default ListItemRow;

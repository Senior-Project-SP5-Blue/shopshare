import CheckBox from '@react-native-community/checkbox';
import React, {PropsWithChildren, useCallback, useMemo, useState} from 'react';
import {StyleSheet, TextInput, TouchableOpacity, View} from 'react-native';
import LockClosedIcon from 'react-native-heroicons/mini/LockClosedIcon';
import LockOpenIcon from 'react-native-heroicons/mini/LockOpenIcon';
import {useSelector} from 'react-redux';
import COLORS from '../constants/colors';
import ListItemDto from '../models/listitem/ListItemDto';
import ListItemStatus from '../models/listitem/ListItemStatus';
import {selectCurrentUserId} from '../redux/slices/authSlice';
import EditListItemRequest from '../models/listitem/EditListItemRequest';
import Swipeable from 'react-native-gesture-handler/Swipeable';
import {TouchableHighlight} from 'react-native-gesture-handler';
import TrashIcon from 'react-native-heroicons/mini/TrashIcon';

interface ListItemRowProps {
  item: ListItemDto;
  onSaveItemChanges: (
    itemId: string,
    editedItem: EditListItemRequest,
  ) => Promise<any>;
  // setSelectedItem: (item: ListItemDto) => void;
}

const ListItemRow: React.FC<PropsWithChildren<ListItemRowProps>> = ({
  item,
  onSaveItemChanges,
}) => {
  const userId = useSelector(selectCurrentUserId);
  const [name, setName] = useState<string>(item.name);
  const [status, setStatus] = useState<ListItemStatus>(item.status);
  const [locked, setLocked] = useState<boolean>(item.locked);

  const completed = useMemo(
    () => status === ListItemStatus.COMPLETED,
    [status],
  );

  const handleToggleStatus = async (val: any) => {
    const newStatus = val ? ListItemStatus.COMPLETED : ListItemStatus.ACTIVE;
    await onSaveItemChanges(item.id, {name, status: newStatus, locked}).then(
      () => setStatus(newStatus),
    );
  };

  const renderRightActions = (x: any) => {
    return (
      <View
        style={{
          // flex: 1,
          height: '100%',
          alignContent: 'center',
          justifyContent: 'center',
          margin: 0,
          padding: 0,
          width: 90,
          // height: 90,
        }}>
        <TouchableHighlight style={{backgroundColor: 'red'}}>
          <TrashIcon color={COLORS.white} />
        </TouchableHighlight>
      </View>
    );
  };
  return (
    <Swipeable
      renderRightActions={renderRightActions}
      onSwipeableOpen={() => {}}>
      <View style={styles.wrapper}>
        <TextInput
          style={[
            styles.name,
            {
              color:
                item.locked && userId !== item.createdBy
                  ? COLORS.grey
                  : COLORS.black,
            },
            {textDecorationLine: completed ? 'line-through' : 'none'},
          ]}
          defaultValue={item.name}
          editable={!item.locked || userId === item.createdBy}
          selectTextOnFocus={!item.locked || userId === item.createdBy}
          onChangeText={newName => setName(newName)}
          onSubmitEditing={() =>
            onSaveItemChanges(item.id, {name, status, locked})
          }
        />
        <CheckBox
          boxType="square"
          onCheckColor="#39B68D"
          value={completed}
          onValueChange={handleToggleStatus}
          style={styles.checkbox}
          disabled={item.locked && userId !== item.createdBy}
          onTouchEnd={() => onSaveItemChanges(item.id, {name, status, locked})}
        />
        {
          // TO-DO: MAYBE IMPLEMENT LOCKING FEATURE
          /*<TouchableOpacity
          disabled={item.locked && userId !== item.createdBy}
          onPress={() => setLocked(!locked)}
          style={styles.locked}>
          {locked ? (
            <LockClosedIcon
              color={
                locked && userId !== item.createdBy
                  ? COLORS.grey
                  : COLORS.primary
              }
            />
          ) : (
            <LockOpenIcon
              color={
                item.locked && userId !== item.createdBy
                  ? COLORS.grey
                  : COLORS.primary
              }
            />
          )}
        </TouchableOpacity>*/
        }
      </View>
    </Swipeable>
  );
};

const styles = StyleSheet.create({
  wrapper: {
    flex: 1,
    backgroundColor: 'whitesmoke',
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

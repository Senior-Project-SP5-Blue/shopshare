import CheckBox from '@react-native-community/checkbox';
import React, {PropsWithChildren, useMemo, useState} from 'react';
import {StyleSheet, Text, TextInput, View} from 'react-native';
import {TouchableHighlight} from 'react-native-gesture-handler';
import Swipeable from 'react-native-gesture-handler/Swipeable';
import TrashIcon from 'react-native-heroicons/mini/TrashIcon';
import {useSelector} from 'react-redux';
import COLORS from '../constants/colors';
import EditListItemRequest from '../models/listitem/EditListItemRequest';
import ListItemDto from '../models/listitem/ListItemDto';
import ListItemStatus from '../models/listitem/ListItemStatus';
import {selectCurrentUserId} from '../redux/slices/authSlice';

interface ListItemRowProps {
  item: ListItemDto;
  onSaveItemChanges: (
    itemId: string,
    editedItem: EditListItemRequest,
  ) => Promise<any>;
  onDeleteItem: (itemId: string) => Promise<any>;
}

const ListItemRow: React.FC<PropsWithChildren<ListItemRowProps>> = ({
  item,
  onSaveItemChanges,
  onDeleteItem,
}) => {
  const [name, setName] = useState<string>(item.name);
  const [status] = useState<ListItemStatus>(item.status);

  const completed = useMemo(
    () => item.status === ListItemStatus.COMPLETED,
    [item.status],
  );

  const handleToggleStatus = async (val: any) => {
    const newStatus = val ? ListItemStatus.COMPLETED : ListItemStatus.ACTIVE;
    await onSaveItemChanges(item.id, {
      name,
      status: newStatus,
      locked: item.locked,
    });
  };

  const renderRightActions = () => {
    return (
      <View
        style={{
          flex: 1,
          backgroundColor: 'red',
          flexDirection: 'column',
          alignItems: 'flex-end',
          justifyContent: 'center',
          margin: 0,
          padding: 0,
          width: 90,
        }}>
        <TouchableHighlight
          style={{
            position: 'relative',
            right: 20,
            justifyContent: 'flex-start',
            backgroundColor: 'red',
            marginLeft: 20,
          }}>
          <View style={{alignItems: 'center'}}>
            <TrashIcon size={20} color={COLORS.white} />
            <Text style={{color: 'white'}}>Delete</Text>
          </View>
        </TouchableHighlight>
      </View>
    );
  };
  return (
    <Swipeable
      rightThreshold={100}
      renderRightActions={renderRightActions}
      onSwipeableWillOpen={() => onDeleteItem(item.id)}>
      <View style={styles.wrapper}>
        <TextInput
          style={[
            styles.name,
            {textDecorationLine: completed ? 'line-through' : 'none'},
          ]}
          defaultValue={item.name}
          onChangeText={newName => setName(newName)}
          onSubmitEditing={() =>
            onSaveItemChanges(item.id, {name, status, locked: item.locked})
          }
        />
        <CheckBox
          boxType="square"
          onCheckColor="#39B68D"
          value={completed}
          onValueChange={handleToggleStatus}
          style={styles.checkbox}
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

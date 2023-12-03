import {useNavigation} from '@react-navigation/native';
import React, {useCallback, useLayoutEffect, useMemo, useState} from 'react';
import {
  ActivityIndicator,
  FlatList,
  Keyboard,
  TouchableOpacity,
  TouchableWithoutFeedback,
} from 'react-native';
import PencilSquare from 'react-native-heroicons/mini/PencilSquareIcon';
import {useSelector} from 'react-redux';
import AddItemView from '../components/AddItemView';
import CreateButton from '../components/CreateButton';
import ListItemRow from '../components/ListItemRow';
import COLORS from '../constants/colors';
import EditListItemRequest from '../models/listitem/EditListItemRequest';
import {selectCurrentUserId} from '../redux/slices/authSlice';
import {
  useChangeItemMutation,
  useRemoveItemMutation,
} from '../redux/slices/listItemApiSlice';
import {useGetGroupShoppingListQuery} from '../redux/slices/shoppingListApiSlice';
import {ListScreenPropsType} from './types';

export type ListScreenNavigationProp = ListScreenPropsType['navigation'];

const ListScreen: React.FC<ListScreenPropsType> = props => {
  const [isAddMode, setIsAddMode] = useState<boolean>(false);
  const navigation = useNavigation<ListScreenNavigationProp>();
  const _userId = useSelector(selectCurrentUserId);
  // const _username = useSelector(selectCurrentUser)?.username;
  const {groupId, listId, color} = props.route.params;
  const {data: list, isLoading: isLoadingList} = useGetGroupShoppingListQuery(
    {
      userId: _userId!,
      groupId,
      listId,
    },
    {
      pollingInterval: 3000,
    },
  );
  const [_list, _setList] = useState(list);
  const [saveItemChange] = useChangeItemMutation();
  const [deleteListItem] = useRemoveItemMutation();

  const handleOnCreateButtonPress = useCallback(() => {
    setIsAddMode(() => !isAddMode);
  }, [isAddMode]);

  const renderEditButton = useCallback(
    () => (
      <TouchableOpacity
        onPress={() =>
          navigation.navigate('Edit List', {
            list: list!,
            groupId: groupId,
            color,
          })
        }>
        {<PencilSquare color={COLORS.primary1} />}
      </TouchableOpacity>
    ),
    [color, groupId, list, navigation],
  );

  const handleChangeItemSave = useCallback(
    async (itemId: string, editedItem: EditListItemRequest) => {
      saveItemChange({
        userId: _userId!,
        groupId,
        listId,
        itemId,
        body: editedItem,
      });
    },
    [_userId, groupId, listId, saveItemChange],
  );
  const handleDeleteItem = useCallback(
    async (itemId: string) => {
      deleteListItem({
        userId: _userId!,
        groupId,
        listId,
        itemId,
      });
    },
    [_userId, deleteListItem, groupId, listId],
  );

  const listHeaderComponent = useMemo(() => {
    return isAddMode ? (
      <AddItemView userId={_userId!} groupId={groupId} listId={listId} />
    ) : null;
  }, [_userId, groupId, isAddMode, listId]);

  useLayoutEffect(() => {
    navigation.setOptions({
      title: list?.name,
      headerRight: renderEditButton,
    });
  }, [list, navigation, renderEditButton]);

  return (
    <TouchableWithoutFeedback
      style={{flex: 1}}
      onPress={Keyboard.dismiss}
      accessible={false}>
      {isLoadingList ? (
        <ActivityIndicator size="large" color={COLORS.primary} />
      ) : (
        <>
          <FlatList
            // extraData={(() => {})()}
            ListHeaderComponent={listHeaderComponent}
            data={list?.items}
            renderItem={({item}) => (
              <ListItemRow
                onDeleteItem={handleDeleteItem}
                onSaveItemChanges={handleChangeItemSave}
                item={item}
              />
            )}
            keyExtractor={item => item.id}
            showsVerticalScrollIndicator={false}
          />
          <CreateButton
            addActive={isAddMode}
            onPress={handleOnCreateButtonPress}
          />
        </>
      )}
    </TouchableWithoutFeedback>
  );
};

export default ListScreen;

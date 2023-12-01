import {useNavigation} from '@react-navigation/native';
import {NativeStackScreenProps} from '@react-navigation/native-stack';
import React, {useCallback, useLayoutEffect, useMemo, useState} from 'react';
import {
  ActivityIndicator,
  Alert,
  FlatList,
  Keyboard,
  TouchableOpacity,
  TouchableWithoutFeedback,
  View,
} from 'react-native';
import {TextInput} from 'react-native-gesture-handler';
import PencilSquare from 'react-native-heroicons/mini/PencilSquareIcon';
import {useSelector} from 'react-redux';
import CreateButton from '../components/CreateButton';
import ListItemRow from '../components/ListItemRow';
import COLORS from '../constants/colors';
import EditListItemRequest from '../models/listitem/EditListItemRequest';
import {selectCurrentUserId} from '../redux/slices/authSlice';
import {
  useChangeItemMutation,
  useRemoveItemMutation,
} from '../redux/slices/listItemApiSlice';
import {
  useChangeShoppingListNameMutation,
  useGetGroupShoppingListQuery,
} from '../redux/slices/shoppingListApiSlice';
import {ListStackParamList} from './types';

type ListScreenProps = NativeStackScreenProps<ListStackParamList, 'List'>;

export type ListScreenNavigationProp = ListScreenProps['navigation'];

const ListScreen: React.FC<ListScreenProps> = props => {
  // const [editListModalVisible, setEditListModalVisible] =
  //   useState<boolean>(false);
  const [isAddMode, setIsAddMode] = useState<boolean>(false);
  const navigation = useNavigation<ListScreenNavigationProp>();
  const _userId = useSelector(selectCurrentUserId);
  const {groupId, listId} = props.route.params;
  const {
    data: list,
    isLoading: isLoadingList,
    isFetching,
    refetch,
  } = useGetGroupShoppingListQuery({
    userId: _userId!,
    groupId,
    listId,
  });
  const [saveListChanges] = useChangeShoppingListNameMutation();
  const [saveItemChange] = useChangeItemMutation();
  const [deleteListItem] = useRemoveItemMutation();

  const handleOnCreateButtonPress = useCallback(() => {
    Alert.alert('Not implemented yet baby. 😝😝');
  }, []);

  const handleChangeListSave = useCallback(
    async (newName: string) => {
      saveListChanges({
        userId: _userId!,
        groupId,
        listId,
        body: {
          name: newName,
        },
      });
    },
    [_userId, groupId, listId, saveListChanges],
  );

  const renderEditButton = useCallback(
    () => (
      <TouchableOpacity
        onPress={() =>
          navigation.navigate('Edit List', {
            list: list!,
            groupId: groupId,
          })
        }>
        {<PencilSquare color={COLORS.primary1} />}
      </TouchableOpacity>
    ),
    [handleChangeListSave, list, navigation],
  );

  const sortedItems = useMemo(() => {
    if (isLoadingList) {
      return [];
    }
    const listCopy = [...list!.items];
    listCopy.sort((a, b) => {
      if (new Date(a.createdOn) < new Date(b.createdOn)) return -1;
      else if (new Date(a.createdOn) > new Date(b.createdOn)) return 1;
      else {
        return 0;
      }
    });
    return listCopy;
  }, [isLoadingList, list]);

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

  // const closeModal = useCallback(() => {
  //   setEditListModalVisible(false);
  // }, []);

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
            onRefresh={refetch}
            refreshing={isFetching}
            data={sortedItems}
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
          {isAddMode && (
            <View>
              <TextInput placeholder="Add Item"></TextInput>
            </View>
          )}
          {/* <EditListModal
            list={list!}
            visible={editListModalVisible}
            closeModal={closeModal}
            onSave={handleChangeListSave}
          /> */}
          <CreateButton onPress={handleOnCreateButtonPress} />
        </>
      )}
    </TouchableWithoutFeedback>
  );
};

export default ListScreen;

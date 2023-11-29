import {useNavigation} from '@react-navigation/native';
import {NativeStackScreenProps} from '@react-navigation/native-stack';
import React, {useCallback, useEffect, useMemo, useState} from 'react';
import {
  ActivityIndicator,
  Alert,
  FlatList,
  Keyboard,
  Modal,
  Pressable,
  StyleSheet,
  Text,
  TextInput,
  TouchableOpacity,
  TouchableWithoutFeedback,
  View,
} from 'react-native';
import {useSelector} from 'react-redux';
import ListItemRow from '../components/ListItemRow';
import COLORS from '../constants/colors';
import ListItemDto from '../models/listitem/ListItemDto';
import {selectCurrentUserId} from '../redux/slices/authSlice';
import {useGetGroupShoppingListQuery} from '../redux/slices/shoppingListApiSlice';
import {ListStackParamList} from './types';
import PencilSquare from 'react-native-heroicons/mini/PencilSquareIcon';
import EditListModal from '../components/EditListModal';

type ListScreenProps = NativeStackScreenProps<ListStackParamList, 'List'>;

export type ListScreenNavigationProp = ListScreenProps['navigation'];

const ListScreen: React.FC<ListScreenProps> = props => {
  const [editItem, setEditItem] = useState<ListItemDto>();
  const [editListModalVisible, setEditListModalVisible] =
    useState<boolean>(false);
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

  const renderEditButton = useCallback(
    () => (
      <TouchableOpacity onPress={() => setEditListModalVisible(true)}>
        {<PencilSquare color={COLORS.primary1} />}
      </TouchableOpacity>
    ),
    [],
  );

  const onEditCancel = useCallback(() => {
    setEditListModalVisible(false);
  }, []);

  useEffect(() => {
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
          <EditListModal
            list={list!}
            visible={editListModalVisible}
            onSave={() => {}}
            onCancel={onEditCancel}
          />
          <FlatList
            onRefresh={refetch}
            refreshing={isFetching}
            ListHeaderComponent={
              <TextInput style={styles.name} placeholder="Add New Item" />
            }
            ListHeaderComponentStyle={{
              marginTop: 10,
              paddingHorizontal: 22,
            }}
            data={list!.items}
            renderItem={({item}) => <ListItemRow item={item} />}
            keyExtractor={item => item.id}
            showsVerticalScrollIndicator={false}
          />
        </>
      )}
    </TouchableWithoutFeedback>
  );
};

const styles = StyleSheet.create({
  listTitle: {
    color: COLORS.black,
    fontSize: 20,
    marginBottom: 20,
  },
  wrapper: {
    marginTop: 20,
    height: '8%',
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    paddingHorizontal: 22,
    paddingVertical: 10,
    borderBlockColor: 'black',
    marginBottom: 10,
    borderStyle: 'solid',
    overflow: 'hidden',
  },
  name: {
    fontSize: 18,
    fontWeight: '500',
    flexBasis: '90%',
    flexGrow: 0,
    textAlign: 'left',
  },
  locked: {
    fontSize: 18,
    flexBasis: '10%',
    flexGrow: 0,
    textAlign: 'center',
  },
});

export default ListScreen;

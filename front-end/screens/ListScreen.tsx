import React, {useEffect, useState} from 'react';
import {
  ActivityIndicator,
  FlatList,
  Keyboard,
  ScrollView,
  StyleSheet,
  Text,
  TextInput,
  TouchableWithoutFeedback,
  View,
} from 'react-native';
import {SafeAreaView} from 'react-native-safe-area-context';
import {useGetGroupShoppingListQuery} from '../redux/slices/shoppingListApiSlice';
import {NativeStackScreenProps} from '@react-navigation/native-stack';
import {ListStackParamList} from './types';
import {useSelector} from 'react-redux';
import {selectCurrentUserId} from '../redux/slices/authSlice';
import COLORS from '../constants/colors';
import ListItemRow from '../components/ListItemRow';
import {useNavigation} from '@react-navigation/native';
import ListItemDto from '../models/listitem/ListItemDto';

type ListScreenProps = NativeStackScreenProps<ListStackParamList, 'List'>;

export type ListScreenNavigationProp = ListScreenProps['navigation'];

const ListScreen: React.FC<ListScreenProps> = props => {
  const [editItem, setEditItem] = useState<ListItemDto>();
  const navigation = useNavigation<ListScreenNavigationProp>();
  const _userId = useSelector(selectCurrentUserId);
  const {groupId, listId} = props.route.params;
  const {data: list, isLoading: isLoadingList} = useGetGroupShoppingListQuery({
    userId: _userId!,
    groupId,
    listId,
  });

  useEffect(() => {
    navigation.setOptions({title: list?.name});
  }, [list]);

  return (
    <TouchableWithoutFeedback
      style={{flex: 1}}
      onPress={Keyboard.dismiss}
      accessible={false}>
      {isLoadingList ? (
        <ActivityIndicator size="large" color={COLORS.primary} />
      ) : (
        // <>
        // <Text>{list?.name}</Text>
        <FlatList
          style={{flex: 1}}
          data={list!.items}
          renderItem={({item}) => <ListItemRow item={item} />}
          keyExtractor={item => item.id}
          showsVerticalScrollIndicator={false}
        />
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
});

export default ListScreen;

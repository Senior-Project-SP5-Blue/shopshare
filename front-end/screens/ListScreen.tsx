import {useNavigation} from '@react-navigation/native';
import {NativeStackScreenProps} from '@react-navigation/native-stack';
import React, {useEffect, useState} from 'react';
import {
  ActivityIndicator,
  FlatList,
  Keyboard,
  StyleSheet,
  TextInput,
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
    navigation.setOptions({
      title: list?.name,
    });
  }, [list, navigation]);

  return (
    <TouchableWithoutFeedback
      style={{flex: 1}}
      onPress={Keyboard.dismiss}
      accessible={false}>
      {isLoadingList ? (
        <ActivityIndicator size="large" color={COLORS.primary} />
      ) : (
        <View>
          <View style={styles.wrapper}>
            <TextInput style={[styles.name]} placeholder="Add New Item" />
          </View>
          <FlatList
            data={list!.items}
            renderItem={({item}) => <ListItemRow item={item} />}
            keyExtractor={item => item.id}
            showsVerticalScrollIndicator={false}
          />
        </View>
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

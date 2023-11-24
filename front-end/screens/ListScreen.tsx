import React from 'react';
import {ActivityIndicator, StyleSheet, Text, View} from 'react-native';
import {SafeAreaView} from 'react-native-safe-area-context';
import {useGetGroupShoppingListQuery} from '../redux/slices/shoppingListApiSlice';
import {NativeStackScreenProps} from '@react-navigation/native-stack';
import {ListStackParamList} from './types';
import {useSelector} from 'react-redux';
import {selectCurrentUserId} from '../redux/slices/authSlice';
import COLORS from '../constants/colors';
import ListItemCard from '../components/ListItemCard';

type ListScreenProps = NativeStackScreenProps<ListStackParamList, 'List'>;

const ListScreen: React.FC<ListScreenProps> = props => {
  const _userId = useSelector(selectCurrentUserId);

  const {groupId, listId} = props.route.params;
  const {data: list, isLoading: isLoadingList} = useGetGroupShoppingListQuery({
    userId: _userId!,
    groupId,
    listId,
  });

  return (
    <SafeAreaView>
      {isLoadingList ? (
        <ActivityIndicator size="large" color={COLORS.primary} />
      ) : (
        <View>
          <Text style={styles.listTitle}>{list?.name}</Text>
          {list!.items.map(x => (
            <ListItemCard item={x} key={x.id} />
          ))}
        </View>
      )}
    </SafeAreaView>
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

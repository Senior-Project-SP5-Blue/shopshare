import React from 'react';
import {
  ActivityIndicator,
  FlatList,
  Image,
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
} from 'react-native';
import {SafeAreaView} from 'react-native-safe-area-context';
import COLORS from '../constants/colors';
import {NativeStackScreenProps} from '@react-navigation/native-stack';
import {useSelector} from 'react-redux';
import ListCard from '../components/ListCard';
import {selectCurrentUserId} from '../redux/slices/authSlice';
import {useGetShoppingListsQuery} from '../redux/slices/shoppingListApiSlice';
import {ListsStackParamList} from './types';
import {useNavigation} from '@react-navigation/native';

type ListsScreenPropsType = NativeStackScreenProps<
  ListsStackParamList,
  'Lists'
>;

export type ListsScreenNavigationProp = ListsScreenPropsType['navigation'];

const ListsScreen: React.FC<ListsScreenPropsType> = _props => {
  const navigation = useNavigation<ListsScreenNavigationProp>();
  const _userId = useSelector(selectCurrentUserId);

  const {data: lists, isLoading: isLoadingLists} = useGetShoppingListsQuery(
    {
      userId: _userId!,
    },
    {pollingInterval: 3000},
  );

  if (isLoadingLists) {
    return (
      <SafeAreaView style={styles.container}>
        <ActivityIndicator size="large" color={COLORS.primary} />
      </SafeAreaView>
    );
  }

  return (
    <SafeAreaView style={styles.container}>
      <View style={{flexDirection: 'row'}}>
        <View style={styles.divider} />
        <Text style={styles.title}>
          Your{' '}
          <Text style={{fontWeight: '400', color: COLORS.secondary}}>
            Lists
          </Text>
        </Text>
        <View style={styles.divider} />
      </View>
      <View style={{marginVertical: 48}}>
        <TouchableOpacity
          style={styles.addButton}
          onPress={() => navigation.navigate('Create List', {})}>
          <Image
            source={require('../assets/add.png')}
            style={styles.addImage}
          />
        </TouchableOpacity>
        <Text style={styles.addList}>Add List</Text>
      </View>
      <View style={{height: 275, justifyContent:"center"}}>
        <FlatList
          data={lists}
          extraData={[...lists!]}
          keyExtractor={item => item.id}
          horizontal={true}
          showsHorizontalScrollIndicator={false}
          renderItem={({item}) => <ListCard list={item} />}
        />
      </View>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: COLORS.white,
    alignItems: 'center',
    justifyContent: 'center',
  },
  divider: {
    backgroundColor: COLORS.secondary,
    height: 1,
    flex: 1,
    alignSelf: 'center',
  },
  title: {
    fontSize: 38,
    fontWeight: '800',
    color: COLORS.black,
    paddingHorizontal: 50,
  },
  addImage: {
    height: 30,
    resizeMode: 'contain',
    width: 30,
    justifyContent: 'center',
  },
  addButton: {
    borderWidth: 1,
    borderColor: COLORS.secondary,
    borderRadius: 4,
    padding: 15,
    alignItems: 'center',
    justifyContent: 'center',
  },
  addList: {
    color: COLORS.secondary,
    fontWeight: '600',
    fontSize: 16,
    marginTop: 8,
  },
});
export default ListsScreen;

import React, {useEffect, useState} from 'react';
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
//import {useSelector} from 'react-redux';
import {useSelector} from 'react-redux';
import {selectCurrentUserId} from '../redux/slices/authSlice';
import {useGetShoppingListsQuery} from '../redux/slices/shoppingListApiSlice';
import ListCard from '../components/ListCard';
import ListItemDto from '../models/listitem/ListItemDto';

interface ListScreenProps {
  navigation: any;
}

const ListsScreen: React.FC<ListScreenProps> = props => {
  const [selectedItem, setSelectedItem] = useState<ListItemDto>();
  const createList = () => props.navigation.navigate('CreateListScreen');
  const _userId = useSelector(selectCurrentUserId); //this is the signed in user

  const {data: lists, isLoading: isLoadingLists} = useGetShoppingListsQuery({
    userId: _userId!,
  }); //"lists" is all the users lists

  useEffect(() => {
    lists?.forEach(console.log);
  }, []);

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
        <TouchableOpacity style={styles.addButton} onPress={createList}>
          <Image
            source={require('../assets/add.png')}
            style={styles.addImage}
          />
        </TouchableOpacity>
        <Text style={styles.addList}>Add List</Text>
      </View>
      <View style={{height: 275, paddingLeft: 32}}>
        <FlatList
          data={lists}
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

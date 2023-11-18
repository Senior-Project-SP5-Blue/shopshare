import {
  Image,
  StyleSheet,
  Text,
  TouchableOpacity,
  FlatList,
} from 'react-native';
import {View} from 'react-native';
import React, {useEffect} from 'react';
import {SafeAreaView} from 'react-native-safe-area-context';
import COLORS from '../constants/colors';
import mockData from '../mockData';
//import {useSelector} from 'react-redux';
import {
  selectAccessToken,
  selectCurrentUser,
  signOut,
} from '../redux/slices/authSlice';
import {useSignOutMutation} from '../redux/slices/authApiSlice';
import {useAppDispatch} from '../redux/store';
import Button from '../components/Button';
import {useGetShoppingListsQuery} from '../redux/slices/shoppingListApiSlice';
import {list} from 'postcss';

const ListsScreen = () => {
  //const user = useSelector(selectCurrentUser);
  //const token = useSelector(selectAccessToken);
  // const [signout] = useSignOutMutation();
  // const dispatch = useAppDispatch();
  // const {data: lists, isSuccess} = useGetShoppingListsQuery({userId: user.id});
  // console.log('Current user');
  //console.log(user);
  console.log('token');
  //console.log(token);
  console.log('Successfully logged in');

  console.log('My lists:');

  // useEffect(() => {
  //   if (isSuccess) {
  //     lists.forEach(x => {
  //       console.log(x);
  //     });
  //   }
  // }, [isSuccess, lists]);
  // lists?.forEach((x, idx) => {

  // })

  // const handleLogOut = () => {
  //   signout()
  //     .unwrap()
  //     .then(_res => {
  //       dispatch(signOut());
  //     })
  //     .catch(err => {
  //       console.log('There was an error signing out.');
  //       console.log(err);
  //     });
    // .unwrap()
    // .then(_res => {
    //   dispa(signOut());
    // })
    // .catch(err => {
    //   console.log('There was an error signing out.');
    //   console.log(err);
    // });
  //};
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
        <TouchableOpacity style={styles.addButton}>
          <Image
            source={require('../assets/add.png')}
            style={styles.addImage}
          />
        </TouchableOpacity>
        <Text style={styles.addList}>Add List</Text>
      </View>
      <View style={{height: 275, paddingLeft: 32}}>
        <FlatList
          data={mockData}
          keyExtractor={item => item.name}
          horizontal={true}
          showsHorizontalScrollIndicator={false}
          renderItem={({item}) => (
            <View>
              <Text></Text>
            </View>
          )}
        />
      </View>
      <View>
        {/* <Button title="Log Out" 
        //onPress={handleLogOut}
         /> */}
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

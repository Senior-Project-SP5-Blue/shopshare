import React, {useState} from 'react';
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
import {useNavigation} from '@react-navigation/native';
import {NativeStackScreenProps} from '@react-navigation/native-stack';
import {useSelector} from 'react-redux';
import GroupCard from '../components/GroupCard';
import ListItemDto from '../models/listitem/ListItemDto';
import {selectCurrentUserId} from '../redux/slices/authSlice';
import {useGetGroupsQuery} from '../redux/slices/shopperGroupApiSlice';
import {GroupStackParamList} from './types';

interface ListScreenProps {
  navigation: any;
}

type GroupScreenProps = NativeStackScreenProps<GroupStackParamList, 'Group'>;

export type GroupScreenNavigationProp = GroupScreenProps['navigation'];

const GroupsScreen: React.FC<ListScreenProps> = props => {
  // const [selectedItem, setSelectedItem] = useState<ListItemDto>();
  // const navigation = useNavigation<GroupScreenNavigationProp>();
  const _userId = useSelector(selectCurrentUserId);

  const {data: groups, isLoading: isLoadingGroups} = useGetGroupsQuery({
    userId: _userId!,
  });

  if (isLoadingGroups) {
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
            Groups
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
        <Text style={styles.addList}>Add Groups</Text>
      </View>
      <View style={{height: 275, paddingLeft: 32}}>
        <FlatList
          style={{width: '100%'}}
          data={groups}
          keyExtractor={item => item.id}
          horizontal={true}
          showsHorizontalScrollIndicator={false}
          renderItem={({item}) => <GroupCard group={item} />}
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
    padding: 0,
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
export default GroupsScreen;

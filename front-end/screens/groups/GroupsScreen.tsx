import {useNavigation} from '@react-navigation/native';
import {NativeStackScreenProps} from '@react-navigation/native-stack';
import React, {useMemo} from 'react';
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
import {useSelector} from 'react-redux';
import GroupCard from '../../components/GroupCard';
import COLORS from '../../constants/colors';
import {selectCurrentUserId} from '../../redux/slices/authSlice';
import {useGetGroupsQuery} from '../../redux/slices/shopperGroupApiSlice';
import {GroupsStackParamList} from '../types';

type GroupsScreenPropsType = NativeStackScreenProps<
  GroupsStackParamList,
  'Groups'
>;

export type GroupsScreenNavigationProp = GroupsScreenPropsType['navigation'];

const GroupsScreen: React.FC<GroupsScreenPropsType> = _props => {
  const _userId = useSelector(selectCurrentUserId);
  const navigation = useNavigation<GroupsScreenNavigationProp>();

  const {data: groups, isLoading: isLoadingGroups} = useGetGroupsQuery(
    {
      userId: _userId!,
    },
    {
      pollingInterval: 3000,
    },
  );
  const renderEmptyListComponent = useMemo(() => {
    return <Text style={{fontSize: 20}}>No groups added</Text>;
  }, []);

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
      <View
        style={{
          marginVertical: 48,
          justifyContent: 'center',
          alignItems: 'center',
        }}>
        <TouchableOpacity
          onPress={() => {
            navigation.navigate('Create Group');
          }}
          style={styles.addButton}>
          <Image
            source={require('../../assets/add.png')}
            style={styles.addImage}
          />
        </TouchableOpacity>
        <Text style={styles.addGroup}>Add Groups</Text>
      </View>
      <View style={{height: 275, justifyContent: 'center'}}>
        <FlatList
          data={groups}
          keyExtractor={item => item.id}
          horizontal={true}
          showsHorizontalScrollIndicator={false}
          renderItem={({item}) => <GroupCard group={item} />}
          ListEmptyComponent={renderEmptyListComponent}
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
    alignItems: 'center',
    justifyContent: 'center',
  },
  addButton: {
    borderWidth: 1,
    borderColor: COLORS.secondary,
    borderRadius: 4,
    padding: 15,
    alignItems: 'center',
    justifyContent: 'center',
    width: 62,
  },
  addGroup: {
    color: COLORS.secondary,
    fontWeight: '600',
    fontSize: 16,
    marginTop: 8,
    alignItems: 'center',
    justifyContent: 'center',
  },
});
export default GroupsScreen;

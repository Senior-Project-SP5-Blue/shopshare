import {useNavigation} from '@react-navigation/native';
import React, {useCallback, useEffect, useLayoutEffect, useMemo} from 'react';
import {
  ActivityIndicator,
  Keyboard,
  SectionList,
  SectionListRenderItem,
  StyleSheet,
  Text,
  TouchableOpacity,
  TouchableWithoutFeedback,
  View,
} from 'react-native';
import PencilSquareIcon from 'react-native-heroicons/mini/PencilSquareIcon';
import UserPlusIcon from 'react-native-heroicons/mini/UserPlusIcon';
import {useSelector} from 'react-redux';
import GroupListRow from '../components/GroupListRow';
import GroupUserRow from '../components/GroupUserRow';
import COLORS from '../constants/colors';
import SlimList from '../models/shoppinglist/SlimList';
import {selectCurrentUserId} from '../redux/slices/authSlice';
import {useGetGroupQuery} from '../redux/slices/shopperGroupApiSlice';
import {GroupScreenPropsType} from './types';

export type GroupScreenNavigationProp = GroupScreenPropsType['navigation'];

const GroupScreen: React.FC<GroupScreenPropsType> = props => {
  const navigation = useNavigation<GroupScreenNavigationProp>();
  const _userId = useSelector(selectCurrentUserId);
  const {groupId, color} = props.route.params;
  const {data: group, isLoading: isLoadingGroup} = useGetGroupQuery(
    {
      userId: _userId!,
      groupId,
    },
    {
      pollingInterval: 3000,
    },
  );

  useEffect(() => {
    navigation.setOptions({
      title: group?.name,
    });
  }, [group, navigation]);

  type UserItem = string;
  type ListItem = SlimList;

  type Section = {
    title: string;
    data: ListItem[];
    renderItem: Function;
  };

  const renderUserItem: SectionListRenderItem<UserItem, Section> = ({item}) => {
    return <GroupUserRow username={item} />;
  };
  const renderListItem: SectionListRenderItem<ListItem, Section> = ({item}) => {
    return <GroupListRow id={item.id} name={item.name} />;
  };

  const renderGroupUsersHeader = () => {
    return (
      <View style={styles.sectionHeader}>
        <Text style={styles.sectionHeaderTitle}>Users</Text>
        <TouchableOpacity>
          <UserPlusIcon color={COLORS.primary1} />
        </TouchableOpacity>
      </View>
    );
  };

  const renderEditButton = useCallback(
    () => (
      <TouchableOpacity
        onPress={() =>
          navigation.navigate('Edit Group', {
            group: group!,
            color,
          })
        }>
        {<PencilSquareIcon color={COLORS.primary1} />}
      </TouchableOpacity>
    ),
    [color, group, navigation],
  );

  useLayoutEffect(() => {
    navigation.setOptions({
      title: group?.name,
      headerRight: renderEditButton,
    });
  }, [group?.name, navigation, renderEditButton]);

  const renderGroupListsHeader = useCallback(() => {
    return (
      <View style={styles.sectionHeader}>
        <Text style={styles.sectionHeaderTitle}>Lists</Text>
        {/* <TouchableOpacity>
          <PlusCircleIcon
            onPress={() =>
              navigation.navigate('ListsStack', {
                screen: 'Create List',
                params: {groupId: groupId},
              })
            }
            color={COLORS.primary1}
          />
        </TouchableOpacity> */}
      </View>
    );
  }, [groupId, navigation]);

  const DATA = useMemo(() => {
    return isLoadingGroup
      ? []
      : [
          {
            title: 'Members',
            data: group?.users || [],
            renderItem: renderUserItem,
            header: renderGroupUsersHeader,
          },
          {
            title: 'Lists',
            data: group?.lists || [],
            renderItem: renderListItem,
            header: renderGroupListsHeader,
          },
        ];
  }, [group?.lists, group?.users, isLoadingGroup, renderGroupListsHeader]);

  return (
    <TouchableWithoutFeedback
      style={{flex: 1}}
      onPress={Keyboard.dismiss}
      accessible={false}>
      {isLoadingGroup ? (
        <ActivityIndicator size="large" color={COLORS.primary} />
      ) : (
        <SectionList
          sections={DATA as any}
          keyExtractor={(item, idx) => `${item.id}` + `${idx}`}
          renderItem={() => <View />}
          renderSectionHeader={({section: {header}}) => header()}
        />
      )}
    </TouchableWithoutFeedback>
  );
};

const styles = StyleSheet.create({
  sectionHeader: {
    flex: 1,
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    paddingHorizontal: 22,
    paddingVertical: 10,
    borderBottomColor: '#dcdcdc',
    borderBottomWidth: 2,
    borderStyle: 'solid',
    marginTop: 24,
  },
  sectionHeaderTitle: {
    fontSize: 20,
    fontWeight: '600',
  },
});

export default GroupScreen;

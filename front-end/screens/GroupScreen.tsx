import {useNavigation} from '@react-navigation/native';
import {NativeStackScreenProps} from '@react-navigation/native-stack';
import React, {useEffect, useMemo, useState} from 'react';
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
import PlusCircleIcon from 'react-native-heroicons/mini/PlusCircleIcon';
import UserPlusIcon from 'react-native-heroicons/mini/UserPlusIcon';
import {useSelector} from 'react-redux';
import GroupListRow from '../components/GroupListRow';
import GroupUserRow from '../components/GroupUserRow';
import COLORS from '../constants/colors';
import ListItemDto from '../models/listitem/ListItemDto';
import SlimList from '../models/shoppinglist/SlimList';
import {selectCurrentUserId} from '../redux/slices/authSlice';
import {useGetGroupQuery} from '../redux/slices/shopperGroupApiSlice';
import {GroupStackParamList} from './types';

type GroupScreenProps = NativeStackScreenProps<GroupStackParamList, 'Group'>;

export type GroupScreenNavigationProp = GroupScreenProps['navigation'];

const GroupScreen: React.FC<GroupScreenProps> = props => {
  const [editItem, setEditItem] = useState<ListItemDto>();
  const navigation = useNavigation<GroupScreenNavigationProp>();
  const _userId = useSelector(selectCurrentUserId);
  const {groupId} = props.route.params;
  const {data: group, isLoading: isLoadingGroup} = useGetGroupQuery({
    userId: _userId!,
    groupId,
  });

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

  const renderGroupListsHeader = () => {
    return (
      <View style={styles.sectionHeader}>
        <Text style={styles.sectionHeaderTitle}>Lists</Text>
        <TouchableOpacity>
          <PlusCircleIcon color={COLORS.primary1} />
        </TouchableOpacity>
      </View>
    );
  };

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
  }, [group, isLoadingGroup]);

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

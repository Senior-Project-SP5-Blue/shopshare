import {NativeStackScreenProps} from '@react-navigation/native-stack';
import React, {useMemo} from 'react';
import {ActivityIndicator, SafeAreaView, Text, View} from 'react-native';
import {FlatList} from 'react-native-gesture-handler';
import {useSelector} from 'react-redux';
import InvitationView from '../../components/InvitationView';
import COLORS from '../../constants/colors';
import {selectCurrentUserId} from '../../redux/slices/authSlice';
import {useGetGroupInvitationsQuery} from '../../redux/slices/shopperGroupApiSlice';
import {AccountStackParamList} from '../types';

type InvitationsScreenPropsType = NativeStackScreenProps<
  AccountStackParamList,
  'Invitations'
>;

export type InvitationsScreenNavigationProp =
  InvitationsScreenPropsType['navigation'];

const InvitationsScreen: React.FC<InvitationsScreenPropsType> = _props => {
  const _userId = useSelector(selectCurrentUserId);
  const {data: invitations, isLoading: isLoadingInvitations} =
    useGetGroupInvitationsQuery({
      userId: _userId!,
    });

  const renderEmptyListComponent = useMemo(() => {
    return (
      <Text style={{fontSize: 20, paddingLeft: 22}}>
        You have no invitations
      </Text>
    );
  }, []);

  const renderSeperatorComponent = useMemo(() => {
    return <View style={{marginVertical: 8}} />;
  }, []);

  if (isLoadingInvitations) {
    return (
      <SafeAreaView
        style={{
          flex: 1,
          backgroundColor: COLORS.white,
          alignItems: 'center',
          justifyContent: 'center',
        }}>
        <ActivityIndicator size="large" color={COLORS.primary} />
      </SafeAreaView>
    );
  }

  return (
    <FlatList
      contentContainerStyle={{
        flex: 1,
        alignItems: 'flex-start',
        paddingTop: 12,
        paddingRight: 20,
        paddingBottom: 20,
      }}
      data={invitations}
      renderItem={({item}) => <InvitationView invitation={item} />}
      keyExtractor={item => `${item.groupId}-${_userId}`}
      ItemSeparatorComponent={() => renderSeperatorComponent}
      ListEmptyComponent={renderEmptyListComponent}
    />
  );
};
export default InvitationsScreen;

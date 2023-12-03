import {useNavigation} from '@react-navigation/native';
import {NativeStackScreenProps} from '@react-navigation/native-stack';
import React from 'react';
import {Text, TouchableHighlight, View} from 'react-native';
import COLORS from '../constants/colors';
import SlimShopperGroupDto from '../models/shoppergroup/SlimShopperGroupDto';
import {GroupsScreenNavigationProp} from '../screens/groups/GroupsScreen';
import {GroupsStackParamList} from '../screens/types';

interface GroupCardProps {
  group: SlimShopperGroupDto;
}

type GroupsScreenProps = NativeStackScreenProps<GroupsStackParamList, 'Groups'>;

export type GroupScreenNavigationProp = GroupsScreenProps['navigation'];

const GroupCard: React.FC<GroupCardProps> = ({group}) => {
  const {id, name, userCount, listCount, admin, color} = group;
  const navigation = useNavigation<GroupsScreenNavigationProp>();

  const handleOnGroupPress = () => {
    navigation.navigate('GroupStack', {
      screen: 'Group',
      params: {groupId: id, color: color || COLORS.grey},
    });
  };

  return (
    <TouchableHighlight
      onPress={handleOnGroupPress}
      underlayColor={COLORS.blue}
      style={{
        paddingVertical: 32,
        paddingHorizontal: 16,
        borderRadius: 6,
        marginHorizontal: 12,
        alignItems: 'center',
        width: 200,
        backgroundColor: color || COLORS.grey,
      }}>
      <View
        style={{
          alignItems: 'center',
          justifyContent: 'center',
        }}>
        <Text
          numberOfLines={1}
          style={{
            fontSize: 22,
            fontWeight: '800',
            paddingHorizontal: 16,
            borderRadius: 6,
            color: COLORS.white,
            marginBottom: 18,
          }}>
          {name}
        </Text>
        <Text
          numberOfLines={1}
          style={{
            fontSize: 18,
            fontWeight: '600',
            paddingHorizontal: 16,
            borderRadius: 6,
            color: COLORS.white,
            marginBottom: 18,
          }}>
          {`${userCount} Users`}
        </Text>
        <Text
          numberOfLines={1}
          style={{
            fontSize: 18,
            fontWeight: '600',
            paddingHorizontal: 16,
            borderRadius: 6,
            color: COLORS.white,
            marginBottom: 18,
          }}>
          {`${listCount} Lists`}
        </Text>
        <Text
          numberOfLines={1}
          style={{
            fontSize: 20,
            fontWeight: '400',
            paddingHorizontal: 16,
            borderRadius: 6,
            color: COLORS.white,
            marginBottom: 18,
            marginTop: 100,
          }}>
          {`Admin ${admin}`}
        </Text>
      </View>
    </TouchableHighlight>
  );
};
export default GroupCard;

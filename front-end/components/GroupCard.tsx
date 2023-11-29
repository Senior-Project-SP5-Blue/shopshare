import {useNavigation} from '@react-navigation/native';
import {NativeStackScreenProps} from '@react-navigation/native-stack';
import React from 'react';
import {Text, TouchableHighlight, View} from 'react-native';
import COLORS from '../constants/colors';
import SlimShopperGroupDto from '../models/shoppergroup/SlimShopperGroupDto';
import {GroupStackParamList} from '../screens/types';

interface GroupCardProps {
  group: SlimShopperGroupDto;
}

type GroupScreenProps = NativeStackScreenProps<GroupStackParamList, 'Group'>;

export type GroupScreenNavigationProp = GroupScreenProps['navigation'];

const GroupCard: React.FC<GroupCardProps> = ({group}) => {
  const {id, name, userCount, listCount, admin, color} = group;
  const navigation = useNavigation<GroupScreenNavigationProp>();

  const handleOnListPress = () => {
    navigation.navigate('Group', {groupId: id});
  };

  return (
    <TouchableHighlight
      onPress={handleOnListPress}
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
      <View>
        <Text
          numberOfLines={1}
          style={{
            fontSize: 16,
            fontWeight: '600',
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
            fontSize: 16,
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
            fontSize: 16,
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
            fontSize: 16,
            fontWeight: '600',
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

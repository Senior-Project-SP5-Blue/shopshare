import React from 'react';
import {
  StyleSheet,
  Text,
  TouchableHighlight,
  TouchableOpacity,
  View,
} from 'react-native';
import COLORS from '../constants/colors';
import SlimShoppingListDto from '../models/shoppinglist/SlimShoppingListDto';
import {useNavigation} from '@react-navigation/native';
import {ListScreenNavigationProp} from '../screens/ListScreen';

interface ListCardProps {
  list: SlimShoppingListDto & {color?: string};
}

const ListCard: React.FC<ListCardProps> = props => {
  const {
    list: {id, name, modifiedOn, completed, total, groupId, color},
  } = props;
  const navigation = useNavigation<ListScreenNavigationProp>();

  const handleOnListPress = () => {
    navigation.navigate('List', {groupId: groupId, listId: id});
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
          {`${completed}/${total} Completed`}
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
          {`Edited ${modifiedOn}`}
        </Text>
        <View>
          <View style={{alignItems: 'center'}}>
            <Text style={{}}></Text>
          </View>
        </View>
      </View>
    </TouchableHighlight>
  );
};
export default ListCard;

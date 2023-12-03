import {useNavigation} from '@react-navigation/native';
import React from 'react';
import {Text, TouchableHighlight, View} from 'react-native';
import COLORS from '../constants/colors';
import SlimShoppingListDto from '../models/shoppinglist/SlimShoppingListDto';
import {ListsScreenNavigationProp} from '../screens/ListsScreen';

interface ListCardProps {
  list: SlimShoppingListDto;
}

const ListCard: React.FC<ListCardProps> = props => {
  const {
    list: {id: listId, name, modifiedOn, completed, total, groupId, color},
  } = props;
  const navigation = useNavigation<ListsScreenNavigationProp>();

  const handleOnListPress = () => {
    navigation.navigate('ListStack' as any, {
      screen: 'List',
      params: {groupId, listId},
    });
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
      <View style={{            
            alignItems:'center',
            justifyContent:'center'}}>
        <Text
          numberOfLines={1}
          style={{
            fontSize: 18,
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
            fontSize: 16,
            fontWeight: '600',
            paddingHorizontal: 16,
            borderRadius: 6,
            color: COLORS.white,
            marginBottom: 18,
          }}>
            Completed Items:
        </Text>
        <View style={{
            alignItems:'center',
            justifyContent:'center',
            marginTop:3,
            marginBottom:50
            }}>
          <Text
          style={{
            fontSize: 16,
            fontWeight: '600',
            color: COLORS.white,
          }}
          >
          {`${completed}/${total}`}
          </Text>
        </View>
        <Text
          numberOfLines={1}
          style={{
            fontSize: 18,
            fontWeight: '600',
            paddingHorizontal: 16,
            borderRadius: 6,
            color: COLORS.white,
            marginBottom: 10,
            marginTop: 10,
          }}>
          Last Edited On
        </Text>
        <View>
          <Text 
          style={{
            fontSize: 16,
            fontWeight: '600',
            color: COLORS.white,
          }}>
          {`${modifiedOn}`}
          </Text>
        </View>
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

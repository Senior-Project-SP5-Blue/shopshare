import {NativeStackScreenProps} from '@react-navigation/native-stack';
import React, {useCallback, useState} from 'react';
import {
  KeyboardAvoidingView,
  Text,
  TextInput,
  TouchableOpacity,
  View,
} from 'react-native';
import {useSelector} from 'react-redux';
import COLORS, {BackGroundColors} from '../constants/colors';
import {selectCurrentUserId} from '../redux/slices/authSlice';
import {ShoppingListApiCreateShoppingListReq} from '../redux/types';
import {ListStackParamList} from './types';

type EditListScreenProps = NativeStackScreenProps<
  ListStackParamList,
  'Edit List'
>;

type EditListScreenNavigationProp = EditListScreenProps['navigation'];

const EditListScreen: React.FC<EditListScreenProps> = props => {
  const Shop = () => props.navigation.navigate('ShopScreen');
  const _userId = useSelector(selectCurrentUserId); //this is the signed in user
  const {
    list: {id: listId, groupId},
  } = props.route.params;
  const [newList, setNewList] = useState<ShoppingListApiCreateShoppingListReq>({
    userId: '',
    groupId: '',
    body: {
      name: '',
      color: '',
    },
  });

  const handleChangeListSave = useCallback(
    async (newName: string) => {
      saveListChanges({
        userId: _userId!,
        groupId,
        listId,
        body: {
          name: newName,
        },
      });
    },
    [_userId, groupId, listId, saveListChanges],
  );

  const [selectedColor, setSelectedColor] = useState<number>(0);
  const handleSelectColor = (idx: number) => {
    setNewList({
      ...newList,
      body: {
        ...newList.body,
        color: BackGroundColors[idx],
      },
    });
    setSelectedColor(idx);
  };

  function renderColors() {
    return BackGroundColors.map((color, idx) => {
      return (
        <TouchableOpacity
          key={color}
          style={{
            backgroundColor: color,
            width: idx === selectedColor ? 35 : 30,
            height: idx === selectedColor ? 35 : 30,
            borderRadius: 4,
          }}
          onPress={() => handleSelectColor(idx)}
        />
      );
    });
  }

  return (
    <KeyboardAvoidingView
      behavior="padding"
      style={{
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
      }}>
      <View style={{alignSelf: 'stretch', marginHorizontal: 32}}>
        <Text
          style={{
            fontSize: 28,
            fontWeight: '800',
            color: COLORS.black,
            alignSelf: 'center',
            marginBottom: 16,
          }}>
          Create Your New List
        </Text>
        <TextInput
          placeholder="New List Name"
          onChangeText={text =>
            setNewList({
              ...newList,
              body: {
                ...newList.body,
                name: text,
              },
            })
          }
          style={{
            borderWidth: 1,
            borderColor: COLORS.secondary,
            borderRadius: 6,
            height: 50,
            marginTop: 8,
            paddingHorizontal: 18,
            fontSize: 18,
          }}
        />
        <View
          style={{
            flexDirection: 'row',
            justifyContent: 'space-between',
            marginTop: 14,
            alignItems: 'flex-end',
          }}>
          {renderColors()}
        </View>
        <TouchableOpacity
          onPress={Shop}
          style={{
            marginTop: 24,
            height: 50,
            borderRadius: 6,
            alignItems: 'center',
            justifyContent: 'center',
            backgroundColor: COLORS.secondary,
          }}>
          <Text
            style={{
              color: COLORS.white,
              fontWeight: '600',
              fontSize: 18,
            }}>
            Continue
          </Text>
        </TouchableOpacity>
      </View>
    </KeyboardAvoidingView>
  );
};
export default EditListScreen;

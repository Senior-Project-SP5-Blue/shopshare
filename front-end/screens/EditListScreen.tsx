import {useNavigation} from '@react-navigation/native';
import {NativeStackScreenProps} from '@react-navigation/native-stack';
import React, {useState} from 'react';
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
import {useChangeShoppingListNameMutation} from '../redux/slices/shoppingListApiSlice';
import {ShoppingListApiCreateShoppingListReq} from '../redux/types';
import {ListStackParamList} from './types';

type EditListScreenProps = NativeStackScreenProps<
  ListStackParamList,
  'Edit List'
>;

export type EditListScreenNavigationProp = EditListScreenProps['navigation'];

const EditListScreen: React.FC<EditListScreenProps> = props => {
  const _userId = useSelector(selectCurrentUserId); //this is the signed in user
  const navigation = useNavigation<EditListScreenNavigationProp>();
  const {
    list: {id: listId, name},
    groupId,
  } = props.route.params;
  const [newListBody, setNewListBody] = useState<{
    name: string;
    color?: string;
  }>({
    name: name,
    color: undefined,
  });
  const [saveListChanges] = useChangeShoppingListNameMutation();

  const handleChangeListSave = async () => {
    console.log(newListBody);
    saveListChanges({
      userId: _userId!,
      groupId,
      listId,
      body: {
        name: newListBody.name,
        color: newListBody.color,
      },
    })
      .unwrap()
      .then(() => navigation.pop());
  };

  const [selectedColor, setSelectedColor] = useState<number>(0);
  const handleSelectColor = (idx: number) => {
    setNewListBody({
      ...newListBody,
      color: BackGroundColors[idx],
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
          Edit List
        </Text>
        <TextInput
          placeholder="Rename List"
          defaultValue={newListBody.name}
          onChangeText={text => setNewListBody({...newListBody, name: text})}
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
          onPress={handleChangeListSave}
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
            Save Changes
          </Text>
        </TouchableOpacity>
      </View>
    </KeyboardAvoidingView>
  );
};
export default EditListScreen;

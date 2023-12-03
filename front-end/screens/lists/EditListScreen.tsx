import {useNavigation} from '@react-navigation/native';
import React, {useState} from 'react';
import {
  KeyboardAvoidingView,
  Text,
  TextInput,
  TouchableOpacity,
  View,
} from 'react-native';
import Toast from 'react-native-toast-message';
import {useSelector} from 'react-redux';
import COLORS, {BackGroundColors} from '../../constants/colors';
import {selectCurrentUserId} from '../../redux/slices/authSlice';
import {
  useDeleteShoppingListMutation,
  useEditShoppingListMutation,
} from '../../redux/slices/shoppingListApiSlice';
import {EditListScreenPropsType} from '../types';

type EditListScreenNavigationProp = EditListScreenPropsType['navigation'];

const EditListScreen: React.FC<EditListScreenPropsType> = props => {
  const {
    list: {id: listId, name},
    groupId,
    color,
  } = props.route.params;
  const [selectedColor, setSelectedColor] = useState<number>(
    BackGroundColors.indexOf(color) === -1
      ? 0
      : BackGroundColors.indexOf(color),
  );
  const _userId = useSelector(selectCurrentUserId); //this is the signed in user
  const navigation = useNavigation<EditListScreenNavigationProp>();
  const [newListBody, setNewListBody] = useState<{
    name: string;
    color?: string;
  }>({
    name: name,
    color: color,
  });
  const [saveListChanges] = useEditShoppingListMutation();
  const [deleteList] = useDeleteShoppingListMutation();

  const handleChangeListSave = async () => {
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
      .then(() => {
        Toast.show({
          type: 'success',
          text1: 'Edited shopping list',
        });
        navigation.pop();
      });
  };

  const handleDeleteList = async () => {
    deleteList({
      userId: _userId!,
      groupId,
      listId,
    })
      .unwrap()
      .then(() => {
        Toast.show({
          type: 'success',
          text1: 'Deleted shopping list',
        });
        navigation.navigate('Lists');
      })
      .catch(_err => {
        Toast.show({
          type: 'error',
          text1: 'There was an error deleting list',
        });
      });
  };

  const handleSelectColor = (idx: number) => {
    setNewListBody({
      ...newListBody,
      color: BackGroundColors[idx],
    });
    setSelectedColor(idx);
  };

  function renderColors() {
    return BackGroundColors.map((x, idx) => {
      return (
        <TouchableOpacity
          key={x}
          style={{
            backgroundColor: x,
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
    <>
      <Toast />
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
          <TouchableOpacity
            onPress={handleDeleteList}
            style={{
              marginTop: 24,
              height: 50,
              borderRadius: 6,
              alignItems: 'center',
              justifyContent: 'center',
              backgroundColor: 'crimson',
            }}>
            <Text
              style={{
                color: COLORS.white,
                fontWeight: '600',
                fontSize: 18,
              }}>
              Delete List
            </Text>
          </TouchableOpacity>
        </View>
      </KeyboardAvoidingView>
    </>
  );
};
export default EditListScreen;

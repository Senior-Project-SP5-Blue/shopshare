import {StackActions, useNavigation} from '@react-navigation/native';
import React, {useMemo, useState} from 'react';
import {
  KeyboardAvoidingView,
  Text,
  TextInput,
  TouchableOpacity,
  View,
} from 'react-native';
import {SelectList} from 'react-native-dropdown-select-list';
import Toast from 'react-native-toast-message';
import {useSelector} from 'react-redux';
import COLORS, {BackGroundColors} from '../constants/colors';
import {selectCurrentUserId} from '../redux/slices/authSlice';
import {useGetGroupsQuery} from '../redux/slices/shopperGroupApiSlice';
import {useAddShoppingListMutation} from '../redux/slices/shoppingListApiSlice';
import {ShoppingListApiCreateShoppingListReq} from '../redux/types';
import {CreateListScreenPropsType} from './types';

export type CreateListScreenNavigationProp =
  CreateListScreenPropsType['navigation'];

const CreateListScreen: React.FC<CreateListScreenPropsType> = props => {
  const _userId = useSelector(selectCurrentUserId); //this is the signed in user
  // const [defaultSelectedGroup] = useState<>();
  const [selectedColor, setSelectedColor] = useState<number>(0);
  const [newListReq, setNewListReq] =
    useState<ShoppingListApiCreateShoppingListReq>({
      userId: _userId!,
      groupId: '',
      body: {
        name: '',
        color: BackGroundColors[selectedColor],
      },
    });
  const navigation = useNavigation<CreateListScreenNavigationProp>();
  const {data: groups} = useGetGroupsQuery({
    userId: _userId!,
  });
  const [createList] = useAddShoppingListMutation();

  const handleSelectColor = (idx: number) => {
    setNewListReq({
      ...newListReq,
      body: {
        ...newListReq.body,
        color: BackGroundColors[idx],
      },
    });
    setSelectedColor(idx);
  };

  const handleCreateList = async () => {
    createList(newListReq)
      .unwrap()
      .then(res => {
        navigation.dispatch(
          StackActions.replace('ListStack' as any, {
            screen: 'List',
            params: {groupId: newListReq.groupId, listId: res.id},
          }),
        );
        Toast.show({
          type: 'success',
          text1: 'Successfully added list!',
        });
      })
      .catch(_err => {
        Toast.show({
          type: 'error',
          text1: 'Error adding list',
        });
      });
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

  const groupChoices = useMemo(() => {
    if (!groups) {
      return [];
    }
    return groups.map(x => ({
      key: `${x.id}`,
      value: x.name,
    }));
  }, [groups]);

  const defaultSelectedGroup = groupChoices.find(
    x => x.key === props.route.params?.groupId,
  );

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
            setNewListReq({
              ...newListReq,
              body: {
                ...newListReq.body,
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
        <SelectList
          placeholder="Select Group"
          search={false}
          defaultOption={defaultSelectedGroup}
          boxStyles={{
            borderWidth: 1,
            borderColor: COLORS.secondary,
            borderRadius: 6,
            height: 50,
            marginTop: 8,
            paddingHorizontal: 18,
          }}
          inputStyles={{fontSize: 18}}
          data={groupChoices}
          setSelected={(val: string) =>
            setNewListReq({
              ...newListReq,
              groupId: val,
            })
          }
          save="key"
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
          onPress={handleCreateList}
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
            Save
          </Text>
        </TouchableOpacity>
      </View>
    </KeyboardAvoidingView>
  );
};
export default CreateListScreen;

import {StackActions, useNavigation} from '@react-navigation/native';
import {NativeStackScreenProps} from '@react-navigation/native-stack';
import React, {useMemo, useState} from 'react';
import {
  KeyboardAvoidingView,
  Text,
  TextInput,
  TouchableOpacity,
  View,
} from 'react-native';
import {SelectList} from 'react-native-dropdown-select-list';
import {useSelector} from 'react-redux';
import COLORS, {BackGroundColors} from '../constants/colors';
import {selectCurrentUserId} from '../redux/slices/authSlice';
import {useGetGroupsQuery} from '../redux/slices/shopperGroupApiSlice';
import {useAddShoppingListMutation} from '../redux/slices/shoppingListApiSlice';
import {ShoppingListApiCreateShoppingListReq} from '../redux/types';
import {ListsStackParamList} from './types';

interface CreateListModalProps {
  navigation: any;
}
type CreateEditListScreenProps = NativeStackScreenProps<
  ListsStackParamList,
  'Create List'
>;

type CreateEditListScreenNavigationProp =
  CreateEditListScreenProps['navigation'];

const CreateListScreen: React.FC<CreateListModalProps> = _props => {
  const _userId = useSelector(selectCurrentUserId); //this is the signed in user
  const [newListReq, setNewListReq] =
    useState<ShoppingListApiCreateShoppingListReq>({
      userId: _userId!,
      groupId: '',
      body: {
        name: '',
        color: '',
      },
    });
  const [selectedColor, setSelectedColor] = useState<number>(0);
  const navigation = useNavigation<CreateEditListScreenNavigationProp>();
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
      return [{}];
    }
    return groups.map(x => ({
      key: `${x.id}`,
      value: x.name,
    }));
  }, [groups]);

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
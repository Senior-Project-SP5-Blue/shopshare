import React, {useMemo, useState} from 'react';
import {
  View,
  Text,
  KeyboardAvoidingView,
  TouchableOpacity,
  TextInput,
  StyleSheet,
} from 'react-native';
import {SafeAreaView} from 'react-native-safe-area-context';
import COLORS, {BackGroundColors} from '../constants/colors';
import {SelectList} from 'react-native-dropdown-select-list';
import CreateEditShoppingListRequest from '../models/shoppinglist/CreateEditShoppingListRequest';
import {ShoppingListApiCreateShoppingListReq} from '../redux/types';
import {useGetGroupsQuery} from '../redux/slices/shopperGroupApiSlice';
import {useSelector} from 'react-redux';
import {selectCurrentUserId} from '../redux/slices/authSlice';

interface CreateGroupModalProps {
  navigation: any;
}

const CreateGroupScreen: React.FC<CreateGroupModalProps> = props => {
  const group = () => props.navigation.navigate('Groups');

  const _userId = useSelector(selectCurrentUserId); //this is the signed in user
  const {data: groups, isLoading: isLoadingGroups} = useGetGroupsQuery({
    userId: _userId!,
  });
  // const {data: groups} = useGetGroupsQuery();
  const [newList, setNewList] = useState<ShoppingListApiCreateShoppingListReq>({
    userId: '',
    groupId: '',
    body: {
      name: '',
      color: '',
    },
  });

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
      {/*<TouchableOpacity
        onPress={lists}
        style={{position: 'absolute', top: 64, left: 32}}>
        <Text
          style={{
            fontSize: 18,
            color: COLORS.black,
            fontWeight: 'bold',
          }}>
          Back
        </Text>
      </TouchableOpacity>*/}
      <View style={{alignSelf: 'stretch', marginHorizontal: 32}}>
        <Text
          style={{
            fontSize: 28,
            fontWeight: '800',
            color: COLORS.black,
            alignSelf: 'center',
            marginBottom: 16,
          }}>
          Create Your New Group
        </Text>
        <TextInput
          placeholder="New Group Name"
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
        {/* <SelectList
          placeholder="Select Group"
          search={false}
          searchPlaceholder="Select Group"
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
            setNewList({
              ...newList,
              groupId: val,
            })
          }
          save="key"
        /> */}
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
          onPress={group}
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
export default CreateGroupScreen;

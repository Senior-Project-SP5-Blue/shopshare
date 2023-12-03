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
import Toast from 'react-native-toast-message';
import {useSelector} from 'react-redux';
import COLORS, {BackGroundColors} from '../../constants/colors';
import {selectCurrentUserId} from '../../redux/slices/authSlice';
import {useCreateNewGroupMutation} from '../../redux/slices/shopperGroupApiSlice';
import {ShopperGroupApiCreateGroupReq} from '../../redux/types';
import {GroupsStackParamList} from '../types';

type CreateGroupScreenPropsType = NativeStackScreenProps<
  GroupsStackParamList,
  'Create Group'
>;

export type CreateGroupScreenNavigationProp =
  CreateGroupScreenPropsType['navigation'];

const CreateGroupScreen: React.FC<CreateGroupScreenPropsType> = props => {
  const _userId = useSelector(selectCurrentUserId);
  const navigation = useNavigation<CreateGroupScreenNavigationProp>();
  const [newGroupReq, setNewGroupReq] = useState<ShopperGroupApiCreateGroupReq>(
    {
      userId: _userId!,
      body: {
        name: '',
        color: BackGroundColors[0],
      },
    },
  );
  const [selectedColor, setSelectedColor] = useState<number>(0);
  const [createGroup] = useCreateNewGroupMutation();

  const handleCreateGroup = async () => {
    createGroup(newGroupReq)
      .unwrap()
      .then(() => {
        Toast.show({
          type: 'success',
          text1: 'Created group',
        });
        navigation.pop();
      })
      .catch(() => {
        Toast.show({
          type: 'error',
          text1: 'Error created group',
        });
      });
  };
  const handleSelectColor = (idx: number) => {
    setNewGroupReq({
      ...newGroupReq,
      body: {
        ...newGroupReq.body,
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
          Create Your New Group
        </Text>
        <TextInput
          placeholder="New Group Name"
          onChangeText={text =>
            setNewGroupReq({
              ...newGroupReq,
              body: {
                ...newGroupReq.body,
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
          onPress={handleCreateGroup}
          disabled={!newGroupReq.body.name.trim()}
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

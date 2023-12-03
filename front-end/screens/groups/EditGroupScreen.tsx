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
import CreateEditShopperGroupRequest from '../../models/shoppergroup/CreateEditShopperGroupRequest';
import {selectCurrentUser} from '../../redux/slices/authSlice';
import {
  useDeleteShopperGroupMutation,
  useEditGroupMutation,
} from '../../redux/slices/shopperGroupApiSlice';
import {EditGroupScreenPropsType} from '../types';

type EditGroupScreenNavigationProp = EditGroupScreenPropsType['navigation'];

const EditGroupScreen: React.FC<EditGroupScreenPropsType> = props => {
  const {group, color} = props.route.params;
  const [selectedColor, setSelectedColor] = useState<number>(
    BackGroundColors.indexOf(color) === -1
      ? 0
      : BackGroundColors.indexOf(color),
  );
  const _user = useSelector(selectCurrentUser);
  const [isAdmin] = useState<boolean>(group.admin === _user!.username);
  const navigation = useNavigation<EditGroupScreenNavigationProp>();
  const [newGroupBody, setNewGroupBody] =
    useState<CreateEditShopperGroupRequest>({name: group.name, color});
  const [saveGroupChanges] = useEditGroupMutation();
  const [deleteList] = useDeleteShopperGroupMutation();

  const handleChangeGroupSave = async () => {
    saveGroupChanges({
      userId: _user!.id,
      groupId: group.id,
      body: {
        name: newGroupBody.name,
        color: newGroupBody.color,
      },
    })
      .unwrap()
      .then(() => {
        Toast.show({
          type: 'success',
          text1: 'Edited group',
        });
        navigation.pop();
      })
      .catch(() => {
        Toast.show({
          type: 'error',
          text1: 'Invalid Permissions',
          text2: 'Must be group admin to modify group',
          onHide: () => navigation.pop(),
        });
      });
  };

  const handleDeleteGroup = async () => {
    deleteList({
      userId: _user!.id!,
      groupId: group.id,
    })
      .unwrap()
      .then(() => {
        Toast.show({
          type: 'success',
          text1: 'Deleted group',
        });
        navigation.navigate('Groups');
      })
      .catch(() => {
        Toast.show({
          type: 'error',
          text1: 'Invalid Permissions',
          text2: 'Must be group admin to delete group',
          onHide: () => navigation.pop(),
        });
      });
  };

  const handleSelectColor = (idx: number) => {
    setNewGroupBody({
      ...newGroupBody,
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
      <KeyboardAvoidingView
        behavior="padding"
        style={{
          flex: 1,
          justifyContent: 'center',
          alignItems: 'center',
        }}>
        <Toast />
        <View style={{alignSelf: 'stretch', marginHorizontal: 32}}>
          <Text
            style={{
              fontSize: 28,
              fontWeight: '800',
              color: COLORS.black,
              alignSelf: 'center',
              marginBottom: 16,
            }}>
            Edit Group
          </Text>
          <TextInput
            placeholder="Rename Group"
            defaultValue={newGroupBody.name}
            onChangeText={text =>
              setNewGroupBody({...newGroupBody, name: text})
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
            onPress={handleChangeGroupSave}
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
            onPress={handleDeleteGroup}
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
              {isAdmin ? 'Delete Group' : 'Leave Group'}
            </Text>
          </TouchableOpacity>
        </View>
      </KeyboardAvoidingView>
    </>
  );
};
export default EditGroupScreen;

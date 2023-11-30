import React, {useMemo, useState} from 'react';
import {
  Alert,
  KeyboardAvoidingView,
  Modal,
  Platform,
  Pressable,
  StyleSheet,
  Text,
  TextInput,
  TouchableOpacity,
  View,
} from 'react-native';
import ListItemDto from '../models/listitem/ListItemDto';
import {list} from 'postcss';
import ShoppingListDto from '../models/shoppinglist/ShoppingListDto';
import COLORS from '../constants/colors';

interface EditListModalProps {
  visible: boolean;
  list: ShoppingListDto;
  onSave: Function;
  closeModal: Function;
}

const EditListModal: React.FC<EditListModalProps> = ({
  visible,
  list,
  onSave,
  closeModal,
}) => {
  const [newName, setNewName] = useState<string>();
  const [selectedColor, setSelectedColor] = useState<number>(0);

  const handleSelectColor = (idx: number) => {
    setSelectedColor(idx);
  };
  const handleSave = () => {
    onSave(newName);
    closeModal();
  };
  const backgroundColors = useMemo(
    () => [
      '#5CD859',
      '#24A6D9',
      '#595BD9',
      '#8021D9',
      '#D159D8',
      '#D85963',
      '#D88559',
    ],
    [],
  );
  const renderColors = () => {
    return backgroundColors.map((color, idx) => {
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
  };
  return (
    <Modal
      animationType="slide"
      transparent={true}
      visible={visible}
      onRequestClose={() => closeModal}>
      <View style={styles.centeredView}>
        <View style={styles.modalView}>
          <KeyboardAvoidingView
            behavior={Platform.OS === 'ios' ? 'padding' : 'height'}
            style={{
              flex: 1,
              alignItems: 'center',
              flexBasis: '70%',
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
                onBlur={() => Alert.alert('Finished editing')}
                placeholder={list.name}
                onChangeText={name => setNewName(name)}
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
              <View style={styles.modalActions}>
                <TouchableOpacity
                  onPress={() => closeModal()}
                  style={{
                    marginTop: 24,
                    height: 50,
                    borderRadius: 6,
                    alignItems: 'center',
                    justifyContent: 'center',
                    backgroundColor: 'red',
                  }}>
                  <Text
                    style={{
                      color: COLORS.white,
                      fontWeight: '600',
                      fontSize: 18,
                    }}>
                    Cancel
                  </Text>
                </TouchableOpacity>
                <TouchableOpacity
                  onPress={handleSave}
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
            </View>
          </KeyboardAvoidingView>
        </View>
      </View>
    </Modal>
  );
};

//
const styles = StyleSheet.create({
  centeredView: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    marginTop: 22,
  },
  modalView: {
    paddingTop: '10%',
    margin: 20,
    width: '90%',
    backgroundColor: 'white',
    justifyContent: 'flex-start',
    borderRadius: 5,
    shadowColor: '#000',
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.25,
    shadowRadius: 4,
    elevation: 5,
  },
  modalActions: {
    flex: 1,
    flexDirection: 'row',
    justifyContent: 'space-evenly',
  },
  cancelButton: {
    flex: 1,
    color: 'red',
    width: '35%',
  },
  saveButton: {
    flex: 1,
    color: 'green',
    width: '35%',
  },
});

export default EditListModal;

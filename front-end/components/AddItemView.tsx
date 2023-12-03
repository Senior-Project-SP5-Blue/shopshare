import React, {useCallback, useMemo, useState} from 'react';
import {Platform, StyleSheet, TouchableOpacity, View} from 'react-native';
import {TextInput} from 'react-native-gesture-handler';
import PlusCircleIcon from 'react-native-heroicons/mini/PlusCircleIcon';
import COLORS from '../constants/colors';
import {useAddItemToListMutation} from '../redux/slices/listItemApiSlice';

interface AddItemViewProps {
  userId: string;
  groupId: string;
  listId: string;
}
const AddItemView: React.FC<AddItemViewProps> = ({userId, groupId, listId}) => {
  const [name, setName] = useState<string>();
  const [locked] = useState<boolean>(false);
  const [addItemToList] = useAddItemToListMutation();

  const addDisabled = useMemo(() => {
    return name ? false : true;
  }, [name]);

  const handleAddItemToList = useCallback(() => {
    if (!name) return;
    addItemToList({userId, groupId, listId, body: {name, locked}})
      .unwrap()
      .then(() => {
        setName(() => '');
      });
  }, [addItemToList, groupId, listId, locked, name, userId]);

  return (
    <View style={styles.wrapper}>
      <TextInput
        placeholder="New Item"
        onChangeText={text => setName(text)}
        value={name}
        onSubmitEditing={handleAddItemToList}
        style={[
          styles.name,
          {marginVertical: Platform.OS === 'android' ? 12 : 20},
        ]}
      />
      <TouchableOpacity
        disabled={addDisabled}
        onPress={handleAddItemToList}
        style={{
          width: '10%',
          borderRadius: 12,
          justifyContent: 'center',
          alignItems: 'center',
        }}>
        <PlusCircleIcon
          // style={{backgroundColor: addDisabled ? COLORS.grey : COLORS.primary1}}
          color={addDisabled ? COLORS.grey : COLORS.primary1}
        />
      </TouchableOpacity>
    </View>
  );
};

const styles = StyleSheet.create({
  wrapper: {
    flex: 1,
    backgroundColor: 'whitesmoke',
    flexDirection: 'row',
    justifyContent: 'space-between',
    paddingHorizontal: 20,
    borderWidth: 1,
    borderColor: COLORS.primary,
    borderStyle: 'solid',
    overflow: 'hidden',
  },
  name: {
    fontSize: 18,
    fontWeight: '700',
    flexBasis: '60%',
    flexGrow: 0,
    textAlign: 'left',
  },
});

export default AddItemView;

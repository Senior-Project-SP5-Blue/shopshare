import {NativeStackScreenProps} from '@react-navigation/native-stack';
import React, {useEffect, useLayoutEffect} from 'react';
import {SafeAreaView, Text, View} from 'react-native';
import {MultipleSelectList} from 'react-native-dropdown-select-list';
import COLORS from '../constants/colors';
import {ListStackParamList} from './types';

// export interface AddItemsToListScreenProps {
//   navigation: any;
//   listId: string;
// }

type AddItemsToListScreenProps = NativeStackScreenProps<
  ListStackParamList,
  'Add Items'
>;

export type AddItemsToListScreenNavigationProp =
  AddItemsToListScreenProps['navigation'];
const AddItemsToListScreen: React.FC<AddItemsToListScreenProps> = _props => {
  const [selected, setSelected] = React.useState([]);

  const data = [
    {key: '2', value: 'Apples'},
    {key: '3', value: 'Milk'},
    {key: '4', value: 'Rice', disabled: false},
    {key: '5', value: 'Yogurt'},
    {key: '6', value: 'Cheese'},
    {key: '7', value: 'Pasta'},
  ];

  useLayoutEffect(() => {
    console.log(selected);
  }, [selected]);
  return (
    <SafeAreaView>
      <View
        style={{
          paddingTop: 50,
          paddingHorizontal: 22,
          alignSelf: 'center',
        }}>
        <Text
          style={{
            fontSize: 30,
            fontWeight: '700',
            color: COLORS.secondary,
            justifyContent: 'center',
            alignItems: 'center',
            marginBottom: 20,
          }}>
          Add to Your List
        </Text>
      </View>
      <View>
        <MultipleSelectList
          setSelected={(val: any) => setSelected(val)}
          data={data}
          save="value"
          onSelect={() => selected}
          label="Categories"
        />
      </View>
    </SafeAreaView>
  );
};

export default AddItemsToListScreen;

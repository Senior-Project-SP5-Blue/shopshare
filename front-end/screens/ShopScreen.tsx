import React, {useState} from 'react';
import {
  View,
  ImageBackground,
  Text,
  TextInput,
  TouchableOpacity,
  Image,
} from 'react-native';
import {SafeAreaView} from 'react-native-safe-area-context';
import {StatusBar} from 'react-native';
import COLORS from '../constants/colors';
import Button from '../components/Button';
import {MultipleSelectList} from 'react-native-dropdown-select-list';

interface ShopScreenProps {
  navigation: any;
}

const ShopScreen = (props: ShopScreenProps) => {
  const create = () => props.navigation.navigate('CreateListsScreen');

  const [selected, setSelected] = React.useState([]);

  const data = [
    {key: '2', value: 'Apples'},
    {key: '3', value: 'Milk'},
    {key: '4', value: 'Rice', disabled: false},
    {key: '5', value: 'Yogurt'},
    {key: '6', value: 'Cheese'},
    {key: '7', value: 'Pasta'},
  ];

  return (
    <SafeAreaView>
      <View>
        <TouchableOpacity
          onPress={create}
          style={{paddingLeft: 10, marginBottom: 20}}>
          <Text
            style={{
              fontSize: 18,
              color: COLORS.black,
              fontWeight: 'bold',
            }}>
            Back
          </Text>
        </TouchableOpacity>
      </View>
      <View
        style={{
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
          Build your List
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
export default ShopScreen;

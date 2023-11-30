import React, {useEffect} from 'react';
import {Text, View} from 'react-native';
import {MultipleSelectList} from 'react-native-dropdown-select-list';
import {SafeAreaView} from 'react-native-safe-area-context';
import COLORS from '../constants/colors';

interface ShopScreenProps {
  navigation: any;
}

const ShopScreen = (props: ShopScreenProps) => {
  const [selected, setSelected] = React.useState([]);

  const data = [
    {key: '2', value: 'Apples'},
    {key: '3', value: 'Milk'},
    {key: '4', value: 'Rice', disabled: false},
    {key: '5', value: 'Yogurt'},
    {key: '6', value: 'Cheese'},
    {key: '7', value: 'Pasta'},
  ];

  useEffect(() => {
    console.log(selected);
  }, [selected]);
  return (
    <SafeAreaView>
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

import React from 'react';
import {
  View,
  Text,
  KeyboardAvoidingView,
  TouchableOpacity,
  TextInput,
  StyleSheet,
} from 'react-native';
import {SafeAreaView} from 'react-native-safe-area-context';
import COLORS from '../constants/colors';

interface CreateListModalProps {
  navigation: any;
}

const CreateListsScreen = (props: CreateListModalProps) => {
  const lists = () => props.navigation.navigate('Lists');
  const Shop = () => props.navigation.navigate('ShopScreen');
  //const name = ""

  const backgroundColors = [
    '#5CD859',
    '#24A6D9',
    '#595BD9',
    '#8021D9',
    '#D159D8',
    '#D85963',
    '#D88559',
  ];
  const bgColors = backgroundColors[0];
  function renderColors() {
    return backgroundColors.map(color => {
      return (
        <TouchableOpacity
          key={color}
          style={{
            backgroundColor: color,
            width: 30,
            height: 30,
            borderRadius: 4,
          }}
          onPress={() => ({color})}
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
      <TouchableOpacity
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
      </TouchableOpacity>
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
          onChangeText={text => ({name: text})}
          style={{
            borderWidth: 1,
            borderColor: COLORS.secondary,
            borderRadius: 6,
            height: 50,
            marginTop: 8,
            paddingHorizontal: 18,
            fontSize: 18,
          }}></TextInput>
        <View
          style={{
            flexDirection: 'row',
            justifyContent: 'space-between',
            marginTop: 14,
          }}>
          {renderColors()}
        </View>
        <TouchableOpacity
          onPress={Shop}
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
            Continue
          </Text>
        </TouchableOpacity>
      </View>
    </KeyboardAvoidingView>
  );
};
export default CreateListsScreen;

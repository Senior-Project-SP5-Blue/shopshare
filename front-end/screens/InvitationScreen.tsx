import {NativeStackScreenProps} from '@react-navigation/native-stack';
import React from 'react';
import {ScrollView, Text, TouchableOpacity, View} from 'react-native';
import COLORS from '../constants/colors';
import {AccountStackParamList} from './types';

type InvitationsScreenPropsType = NativeStackScreenProps<
  AccountStackParamList,
  'Requests'
>;

export type InvitationsScreenNavigationProp =
  InvitationsScreenPropsType['navigation'];

const InvitationsScreen: React.FC<InvitationsScreenPropsType> = _props => {
  return (
    <ScrollView
      contentContainerStyle={{
        flex: 1,
        alignItems: 'flex-start',
        paddingTop: 12,
        paddingRight: 20,
        paddingBottom: 20,
      }}>
      <View
        style={{flexDirection: 'row', alignItems: 'center', paddingLeft: 15}}>
        <Text style={{marginRight: 'auto', fontSize: 17, fontWeight: '500'}}>
          You are invited to Group 2
        </Text>
        <View style={{flexDirection: 'row', alignItems: 'center'}}>
          <TouchableOpacity
            style={{
              paddingVertical: 10,
              paddingHorizontal: 10,
              borderRadius: 5,
              marginLeft: 40,
              backgroundColor: COLORS.blue,
            }}>
            <Text
              style={{
                color: 'white',
                fontWeight: 'bold',
                fontSize: 16,
              }}>
              Accept
            </Text>
          </TouchableOpacity>
          <TouchableOpacity
            style={{
              paddingVertical: 10,
              paddingHorizontal: 10,
              borderRadius: 5,
              marginLeft: 15,
              backgroundColor: COLORS.red,
            }}>
            <Text
              style={{
                color: 'white',
                fontWeight: 'bold',
                fontSize: 16,
              }}>
              Decline
            </Text>
          </TouchableOpacity>
        </View>
      </View>
      <View
        style={{
          borderBottomWidth: 1,
          borderBottomColor: 'lightgray',
          marginVertical: 10,
        }}
      />
    </ScrollView>
  );
};
export default InvitationsScreen;

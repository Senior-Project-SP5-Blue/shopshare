import React from 'react';
import {Text, View} from 'react-native';
import {TouchableOpacity} from 'react-native-gesture-handler';
import COLORS from '../constants/colors';

interface InvitationViewProps {
  groupName: string;
}

const InvitationView: React.FC<InvitationViewProps> = () => {
  return (
    <View style={{flexDirection: 'row', alignItems: 'center', paddingLeft: 15}}>
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
  );
};

export default InvitationView;

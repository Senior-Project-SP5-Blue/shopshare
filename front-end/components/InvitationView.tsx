import React from 'react';
import {Text, View} from 'react-native';
import {TouchableOpacity} from 'react-native-gesture-handler';
import COLORS from '../constants/colors';
import InvitationDto from '../models/shoppergroup/InvitationDto';
import {
  useAcceptGroupInvitationMutation,
  useDeclineGroupInvitationMutation,
} from '../redux/slices/shopperGroupApiSlice';
import {useSelector} from 'react-redux';
import {selectCurrentUserId} from '../redux/slices/authSlice';
import Toast from 'react-native-toast-message';

interface InvitationViewProps {
  invitation: InvitationDto;
}

const InvitationView: React.FC<InvitationViewProps> = ({
  invitation: {groupId, groupName},
}) => {
  const _userId = useSelector(selectCurrentUserId);
  const [acceptInvitation] = useAcceptGroupInvitationMutation();
  const [declineInvitation] = useDeclineGroupInvitationMutation();
  const handleAcceptInvitation = async () => {
    acceptInvitation({
      groupId,
      userId: _userId!,
    })
      .unwrap()
      .then(() => {
        Toast.show({
          type: 'success',
          text1: 'Accepted Invitation',
          text2: `You are now a member of group ${groupName}`,
        });
      })
      .catch(() => {
        Toast.show({
          type: 'error',
          text1: 'Could not accept invitation',
        });
      });
  };
  const handleDeclineInvitation = async () => {
    declineInvitation({
      groupId,
      userId: _userId!,
    })
      .then(() => {
        Toast.show({
          type: 'success',
          text1: 'Declined Invitation',
        });
      })
      .catch(() => {
        Toast.show({
          type: 'error',
          text1: 'Could not decline invitation',
        });
      });
  };
  return (
    <View
      style={{
        flexDirection: 'row',
        alignItems: 'center',
        paddingLeft: 15,
      }}>
      <Text
        numberOfLines={1}
        style={{
          flexDirection: 'row',
          flexBasis: '55%',
          flexGrow: 0,
          marginRight: 'auto',
          fontSize: 17,
          fontWeight: '500',
        }}>
        {groupName}
      </Text>
      <View style={{flexDirection: 'row', alignItems: 'center'}}>
        <TouchableOpacity
          onPress={handleAcceptInvitation}
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
          onPress={handleDeclineInvitation}
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

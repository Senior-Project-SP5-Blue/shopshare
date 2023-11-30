import React, { useState } from 'react';
import {View, Text, Button, ScrollView, TextInput, Pressable, Modal, TouchableOpacity} from 'react-native';
import {SafeAreaView} from 'react-native-safe-area-context';
import {useAppDispatch} from '../redux/store';
import {useSignOutMutation} from '../redux/slices/authApiSlice';
import {clearAuthContext, selectCurrentUser} from '../redux/slices/authSlice';
import COLORS from '../constants/colors';
import SSTextInput from '../components/SSTextInput';
import SSPasswordInput from '../components/SSPasswordInput';
import { useSelector } from 'react-redux';

interface SettingsProps {
  navigation: any;
}

const AccountsScreen = (props:SettingsProps) => {
  const [isPasswordShown, setIsPasswordShown] = useState(false);
  const [modalVisible, setModalVisible] = useState(false);
  const yourFriends = () => props.navigation.navigate('YourFriends')
  const requests = () => props.navigation.navigate('Requests')

  const user = useSelector (selectCurrentUser)


  const dispatch = useAppDispatch();
  const [signOut] = useSignOutMutation();

  // An example of how to logout
  const handleSignOut = () => {
    signOut()
      .unwrap()
      .then(_res => {
        console.log('signing out...');
        dispatch(clearAuthContext());
      })
      .catch(err => {
        console.log('There was an error signing out.');
        console.log(err);
      });
  };

  return (
    <SafeAreaView 
      style ={{
      backgroundColor: COLORS.white,
      flex: 1,
      paddingLeft: 10,
      paddingRight: 10,
      marginTop:-30
      }}>
        <Modal transparent={true} visible={modalVisible} onRequestClose={() => setModalVisible(false)}>
        <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>
          <View style={{ backgroundColor: 'white', padding: 20, height:200, width: 350, borderWidth:2, borderColor:COLORS.secondary, borderRadius:10, justifyContent:"center", alignContent:"center", alignItems:"center"}}>
            <Text style={{fontSize:18, justifyContent:"center", alignContent:"center", alignItems:"center"}}>Enter their email address</Text>
            <Button title="Close" onPress={() => setModalVisible(false)} />
          </View>
        </View>
        </Modal>
        <View>
        <Text style={{fontSize: 22, fontWeight: 'bold', marginVertical: 0, color: COLORS.black}}>
          Manage Invitations
        </Text>
        </View>
        <View style={{marginTop:2, marginBottom: 2}}>
          <TouchableOpacity onPress={() => setModalVisible(true)}>
            <Text style={{fontSize:18, fontWeight:'700', color:COLORS.primary, marginTop: 10}}>Add Friends</Text>
          </TouchableOpacity>
        </View>
        <View>
        <TouchableOpacity onPress={yourFriends}>
            <Text style={{fontSize:18, fontWeight:'700', color:COLORS.primary, marginTop: 10}}>Your Friends</Text>
          </TouchableOpacity>
        </View>
        <View>
        <TouchableOpacity onPress={requests}>
            <Text style={{fontSize:18, fontWeight:'700', color:COLORS.primary, marginTop: 10}}>Friend Requests</Text>
          </TouchableOpacity>
        </View>
      <View >
        <Text style={{fontSize: 22, fontWeight: 'bold', marginTop: 30, color: COLORS.black}}>Edit Profile</Text>
      </View>
      {/* Username */}
      <SSTextInput
        label="Username"
        placeholder={user?.username}
        placeholderTextColor={COLORS.black}
      />
      {/* Email Address */}
      <SSTextInput
        label="Email Address"
        placeholder="user9first@email.com"
        placeholderTextColor={COLORS.black}
        keyboardType="email-address"
      />
      {/* Mobile Number */}
      <View
        style={{
          marginBottom: 12,
        }}>
        <Text
          style={{
            fontSize: 16,
            fontWeight: '400',
            marginVertical: 10,
          }}>
          Mobile Number
        </Text>

        <View
          style={{
            width: '100%',
            height: 60,
            borderColor: COLORS.black,
            borderWidth: 1,
            borderRadius: 8,
            alignItems: 'center',
            flexDirection: 'row',
            justifyContent: 'space-between',
            paddingLeft: 22,
          }}>
          <TextInput
            placeholder="+1"
            placeholderTextColor={COLORS.black}
            keyboardType="number-pad"
            style={{
              width: '12%',
              fontSize: 15,
              borderRightWidth: 1,
              height: '100%',
            }}
          />
          <TextInput
            placeholder="4703335555"
            placeholderTextColor={COLORS.black}
            keyboardType="numeric"
            style={{
              width: '80%',
            }}
            //onChangeText={number => setSignUpReq({...signUpReq, number})}
          />
        </View>
      </View>
      <SSPasswordInput
        label="Password"
        placeholder="user9pass"
        placeholderTextColor={COLORS.black}
        //secureTextEntry={isPasswordShown}
        autoCapitalize="none"
        onShowPasswordPress={setIsPasswordShown}
        // onChangeText={password => setSignUpReq({...signUpReq, password})}
      />
      <View>
      <View>
      <Button title="Log Out" onPress={handleSignOut} />
      </View> 
      </View>

    </SafeAreaView>
  );
};
export default AccountsScreen;
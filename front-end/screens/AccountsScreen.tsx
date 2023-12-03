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
  const requests = () => props.navigation.navigate('Requests')
  const password = () => props.navigation.navigate('Change Password')


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
            <Text style={{fontSize:18, justifyContent:"center", alignContent:"center", alignItems:"center"}}>Enter their username</Text>
            <TextInput 
            placeholder='Enter their username'
            style={{    
              width: '100%',
              height: 40,
              borderColor: COLORS.black,
              borderWidth: 1,
              borderRadius: 8,
              alignItems: 'center',
              justifyContent: 'center',
              marginTop:20, 
              marginBottom:15, 
              paddingLeft:10, 
              paddingRight:10 }}>
            </TextInput>
            <Button title="Send Request" onPress={() => setModalVisible(false)} />
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
            <Text style={{fontSize:18, fontWeight:'700', color:COLORS.primary, marginTop: 10}}>Invite People</Text>
          </TouchableOpacity>
        </View>
        <View>
        </View>
        <View>
        <TouchableOpacity onPress={requests}>
            <Text style={{fontSize:18, fontWeight:'700', color:COLORS.primary, marginTop: 10}}>Invites</Text>
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
        placeholder={user?.email}
        placeholderTextColor={COLORS.black}
        keyboardType="email-address"
      />
      <SSPasswordInput
        label="Password"
        placeholder="user9pass"
        placeholderTextColor={COLORS.black}
        //secureTextEntry={isPasswordShown}
        autoCapitalize="none"
        onShowPasswordPress={setIsPasswordShown}
        // onChangeText={password => setSignUpReq({...signUpReq, password})}
      />
      <View style={{paddingLeft: 5}}>
        <TouchableOpacity onPress={password}>
          <Text 
            style={{
            fontSize: 16,
            fontWeight: '600',
            color:COLORS.red
          }}>
            Change Password
            </Text>
        </TouchableOpacity>
      </View>
      <View style={{marginTop:90}}>
        <TouchableOpacity
        onPress={handleSignOut}
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
              fontSize: 20,
            }}>
            Log Out
            </Text>
        </TouchableOpacity>
      </View> 

    </SafeAreaView>
  );
};
export default AccountsScreen;

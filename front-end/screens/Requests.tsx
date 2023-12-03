import React from "react";
import {View, Text, Button, ScrollView, TextInput, Pressable, Modal, TouchableOpacity} from 'react-native';
import { SafeAreaView } from "react-native-safe-area-context";
import COLORS from "../constants/colors";

interface RequestsProps {
    navigation: any;
  }

const Requests = (props: RequestsProps) => {
    return(
        <SafeAreaView style={{marginTop:-40, flex: 1, justifyContent:'flex-end', alignItems:'flex-start', paddingRight:20, paddingBottom:20}}>
        <ScrollView>
          <View style={{flexDirection:'row', alignItems:'center', paddingLeft: 15}}>
            <Text style={{marginRight:'auto', fontSize:17, fontWeight:'500' }}>
              You are invited to Group 2
            </Text>
            <View style={{flexDirection:'row', alignItems:'center'}}>
            <TouchableOpacity style={{
                paddingVertical: 10,
                paddingHorizontal: 10,
                borderRadius: 5,
                marginLeft: 40, 
                backgroundColor: COLORS.blue
              }}>
              <Text style={{
                 color: 'white',
                 fontWeight: 'bold',
                 fontSize: 16,
              }}>
                Accept 
              </Text>
            </TouchableOpacity>
            <TouchableOpacity style={{
                paddingVertical: 10,
                paddingHorizontal: 10,
                borderRadius: 5,
                marginLeft: 15, 
                backgroundColor: COLORS.red
              }}>
              <Text style={{
                 color: 'white',
                 fontWeight: 'bold',
                 fontSize: 16,
              }}>
                Decline  
              </Text>
            </TouchableOpacity>
            </View>
          </View>
          <View style={{
            borderBottomWidth: 1,
            borderBottomColor: 'lightgray', 
            marginVertical: 10, 
          }}/>
        </ScrollView>
    </SafeAreaView>
    )
}
export default Requests;
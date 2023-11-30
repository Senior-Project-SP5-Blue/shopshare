import React from "react";
import {View, Text, Button, ScrollView, TextInput, Pressable, Modal, TouchableOpacity} from 'react-native';
import { SafeAreaView } from "react-native-safe-area-context";
import COLORS from "../constants/colors";

interface RequestsProps {
    navigation: any;
  }

const Requests = (props: RequestsProps) => {
    const account = () => props.navigation.navigate('Settings')
    return(
        <SafeAreaView>
        <View>
        <View>
        <Pressable onPress={account}>
          <Text
            style={{
              fontSize: 20,
              color: COLORS.primary,
              fontWeight: 'bold',
              marginLeft: 5,
            }}>
            Back
          </Text>
        </Pressable>
      </View>
            <Text>Your Requests</Text>
        </View>
    </SafeAreaView>
    )
}
export default Requests;
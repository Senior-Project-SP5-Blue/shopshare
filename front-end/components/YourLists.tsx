import React from "react";
import { StyleSheet, Text, View } from "react-native";
import COLORS from "../constants/colors";
import { list } from "postcss";

interface YourListsProps {
    list: { name: string, color: string }; 
    
  }
  const YourLists: React.FC<YourListsProps> = ({ list }) => {
    return (
        <View style={{
            paddingVertical: 32,
            paddingHorizontal: 16,
            borderRadius: 6,
            marginHorizontal: 12,
            alignItems: "center",
            width: 200,
            backgroundColor: list.color
        }}>
            <Text numberOfLines={1} style={{
                fontSize: 24,
                fontWeight: "600",
                paddingHorizontal: 16,
                borderRadius: 6,
                color: COLORS.white,
                marginBottom: 18
            }}>
                {list.name}
            </Text>
            <View>
                <View style={{alignItems: "center"}}>
                    <Text style={{

                    }}>

                    </Text>
                </View>
            </View>
        </View>
    )
}
    export default YourLists;

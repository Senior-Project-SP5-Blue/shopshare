import React from "react";
import { StyleSheet, Text, View } from "react-native";
import { SafeAreaView } from "react-native-safe-area-context";
import COLORS from "./constants/colors";
import { list } from "postcss";

const List = () =>{
    return (
        <SafeAreaView style ={[styles.container, {backgroundColor: list.color}]}>

        </SafeAreaView>
    )
}
const styles = StyleSheet.create ({
    container : {
        paddingVertical: 32, 
        paddingHorizontal: 16,
        borderRadius: 6,
        marginHorizontal: 12,
        alignItems: "center",
        width: 200
    }
})
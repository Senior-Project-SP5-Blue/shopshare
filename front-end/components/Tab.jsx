import React from "react";
import listsScreen from "../screens/listsScreen";
import AccountsScreen from "../screens/AccountsScreen";
import { createBottomTabNavigator } from "react-navigation-tabs";
import COLORS from "../constants/colors";

const Tab = createBottomTabNavigator();

const Tabs = () => {
    return (
        <Tab.Navigator
        tabBarOptions={{
            showLabel: true,
            style: {
                position: 'absolute',
                bottom: 25,
                left: 20,
                right: 20,
                backgroundColor: COLORS.secondary,
                borderRadius: 15,
                height: 90
            }
        }}
        >
            <Tab.Screen name="Lists" component={listsScreen}/>
            <Tab.Screen name="AccountsScreen" component={AccountsScreen}/>


        </Tab.Navigator>
    )
}
export default Tabs;
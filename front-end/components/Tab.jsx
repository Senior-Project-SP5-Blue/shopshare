import React from 'react';
import {createBottomTabNavigator} from 'react-navigation-tabs';
import COLORS from '../constants/colors';
import AccountsScreen from '../screens/AccountsScreen';
import listsScreen from '../screens/listsScreen';

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
          height: 90,
        },
      }}>
      <Tab.Screen
        name="Lists"
        component={listsScreen}
        options={{
          tabBarIcon: ({}) => (
            <HeroIcon name="academic-cap" color={COLORS.secondary} size={25} />
          ),
        }}
      />
      <Tab.Screen name="AccountsScreen" component={AccountsScreen} />
    </Tab.Navigator>
  );
};
export default Tabs;

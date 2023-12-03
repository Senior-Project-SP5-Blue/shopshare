import React from 'react';
import {Text} from 'react-native';

import AppNavigator from './navigation';
import {Provider} from 'react-redux';
import store from './redux/store';
import {PersistGate} from 'redux-persist/integration/react';
import {persistor} from './redux/store';
import {SafeAreaProvider} from 'react-native-safe-area-context';
import {GestureHandlerRootView} from 'react-native-gesture-handler';

const App = () => {
  return (
    <Provider store={store}>
      <PersistGate loading={<Text>Loading...</Text>} persistor={persistor}>
        <GestureHandlerRootView style={{flex: 1}}>
          <SafeAreaProvider>
            <AppNavigator />
          </SafeAreaProvider>
        </GestureHandlerRootView>
      </PersistGate>
    </Provider>
  );
};

export default App;

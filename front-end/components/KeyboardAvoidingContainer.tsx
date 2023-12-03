import React from 'react';
import {PropsWithChildren} from 'react';
import {
  KeyboardAvoidingView,
  Platform,
  ScrollView,
  ScrollViewProps,
  StyleProp,
  StyleSheet,
  ViewStyle,
} from 'react-native';
import {SafeAreaView, useSafeAreaInsets} from 'react-native-safe-area-context';
import COLORS from '../constants/colors';

interface KeyboardAvoidingContainerProps {
  style?: StyleProp<ViewStyle>;
  backgroundColor?: string;
  scrollEnabled?: boolean;
}

const KeyboardAvoidingContainer: React.FC<
  PropsWithChildren<KeyboardAvoidingContainerProps & ScrollViewProps>
> = ({children, backgroundColor, style, scrollEnabled}) => {
  const insets = useSafeAreaInsets();
  return (
    <SafeAreaView
      style={{
        flex: 1,
        backgroundColor: backgroundColor ?? undefined,
      }}>
      <KeyboardAvoidingView
        style={{flex: 1}}
        behavior={Platform.OS === 'ios' ? 'padding' : 'height'}
        keyboardVerticalOffset={0}>
        <ScrollView
          scrollEnabled={scrollEnabled ?? true}
          showsVerticalScrollIndicator={false}
          contentContainerStyle={[styles.contentContainer, style]}>
          {children}
        </ScrollView>
      </KeyboardAvoidingView>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  contentContainer: {
    paddingHorizontal: 22,
  },
});

export default KeyboardAvoidingContainer;

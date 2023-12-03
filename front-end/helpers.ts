import Toast from 'react-native-toast-message';

export const handleError = (err: any) => {
  if (err.originalStatus === 403) {
    Toast.show({
      type: 'error',
      text1: 'Invalid Permissions',
    });
  } else if (err.originalStatus === 404) {
    Toast.show({
      type: 'error',
      text1: 'Not Found',
    });
  } else if (err.originalStatus === 400) {
    Toast.show({
      type: 'error',
      text1: 'Invalid Operation',
    });
  } else {
    Toast.show({
      type: 'error',
      text1: 'Request could not be processed.',
    });
  }
};

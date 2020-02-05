import { NativeModules, Platform } from 'react-native';

const { RNSmsVerificationApi } = NativeModules;

const SmsVerificationApi = (Platform.OS == 'ios') ? null : {
  requestPhoneNumber: RNSmsVerificationApi.requestPhoneNumber
}

export default SmsVerificationApi;

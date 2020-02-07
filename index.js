import { DeviceEventEmitter, NativeModules, Platform } from 'react-native'

const { RNSmsVerificationApi } = NativeModules

const SmsVerificationApi = (Platform.OS == 'ios') ? {
  requestPhoneNumber: () => Promise.reject("iOS not supported"),
  startSmsRetriever: () => Promise.reject("iOS not supported"),
  addSmsListener: () => undefined,
  removeSmsListener: () => undefined
} : {
  requestPhoneNumber: RNSmsVerificationApi.requestPhoneNumber,
  startSmsRetriever: RNSmsVerificationApi.startSmsRetriever,
  addSmsListener: (callback) => DeviceEventEmitter.addListener(RNSmsVerificationApi.SMS_EVENT, callback),
  removeSmsListener: () => DeviceEventEmitter.removeAllListeners(RNSmsVerificationApi.SMS_EVENT)
}

export default SmsVerificationApi;

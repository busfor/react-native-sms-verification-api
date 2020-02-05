import { EmitterSubscription } from 'react-native'

declare const SmsVerificationApi: {
  requestPhoneNumber: () => Promise<string>
}

export default SmsVerificationApi

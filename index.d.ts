import { EmitterSubscription } from 'react-native'

export interface SmsListenerEvent {
  message?: string
  extras?: string
  status?: string
  timeout?: string
}

declare const SmsVerificationApi: {
  requestPhoneNumber: () => Promise<string>
  startSmsRetriever: () => Promise<boolean>
  addSmsListener: (callback: (event: SmsListenerEvent) => void) => EmitterSubscription
  removeSmsListener: () => void
}

export default SmsVerificationApi

import React, { useCallback } from 'react';
import { Text, TouchableHighlight, Alert, StyleSheet, View } from 'react-native';
import SmsVerificationApi from 'react-native-sms-verification-api';

export default App = () => {
  const requestPhoneNumber = useCallback(() => {
    SmsVerificationApi.requestPhoneNumber()
      .then((phone) => Alert.alert("SmsVerificationApi.requestPhoneNumber", phone))
      .catch((reason) => console.log(reason))
  }, [])

  const startSmsListener = useCallback(() => {
    SmsVerificationApi.startSmsRetriever()

    SmsVerificationApi.addSmsListener((event) => {
      Alert.alert("SmsVerificationApi.smsRetriever", event.message)
    })
  }, [])

  const stopSmsListener = useCallback(() => {
    SmsVerificationApi.removeSmsListener()
  })

  return (
    <View style={styles.container}>
      <TouchableHighlight underlayColor={"#ff1111"} style={styles.button} onPress={requestPhoneNumber}>
        <Text style={styles.text}>Request Phone Number</Text>
      </TouchableHighlight>

      <TouchableHighlight underlayColor={"#ff1111"} style={styles.button} onPress={startSmsListener}>
        <Text style={styles.text}>Start SMS listener</Text>
      </TouchableHighlight>

      <TouchableHighlight underlayColor={"#ff1111"} style={styles.button} onPress={stopSmsListener}>
        <Text style={styles.text}>Stop SMS listener</Text>
      </TouchableHighlight>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    flexDirection: "row",
  },

  button: {
    width: 120,
    height: 50,
    backgroundColor: "#ff5555",
  },

  text: {
    flex: 1,
    color: "#ffffff",
    textAlign: "center",
  },
})

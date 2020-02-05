package com.busfor.smsverification

enum class ErrorType(val type: String, val message: String) {
    SERVICES_UNAVAILABLE("GOOGLE_PLAY_SERVICE_UNAVAILABLE",
            "Google Play Services is not available."),
    UNSUPPORTED_VERSION("UNSUPPORTED_GOOGLE_PLAY_SERVICES_VERSION",
            "The device version of Google Play Services is not supported."),
    CONNECTION_SUSPENDED("CONNECTION_SUSPENDED",
            "Client is temporarily in a disconnected state."),
    CONNECTION_FAILED("CONNECTION_FAILED",
            "There was an error connecting the client to the service."),
    NULL_ACTIVITY("NULL_ACTIVITY","Activity is null."),
    ACTIVITY_RESULT_NOOK("ACTIVITY_RESULT_NOOK",
            "There was an error trying to get the phone number."),
    SEND_INTENT_ERROR("SEND_INTENT_ERROR_TYPE",
            "There was an error trying to send intent."),
    TASK_FAILURE("TASK_FAILURE_ERROR_TYPE", "Task failed."),
    EXTRAS_NULL(EXTRAS_KEY, "Extras is null."),
    STATUS_NULL(STATUS_KEY, "Status is null."),
    TIMEOUT(TIMEOUT_KEY, "Timeout error.")
}

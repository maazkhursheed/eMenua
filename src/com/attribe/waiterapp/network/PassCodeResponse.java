package com.attribe.waiterapp.network;

/**
 * Created by Sabih Ahmed on 6/1/2015.
 */
public class PassCodeResponse {
    public static String RESPONSE_PASSCODE_INVALID = "401";
    public static String RESPONSE_PASSCODE_SUCCESS = "200";

    String message,api_key,status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getApi_key() {
        return api_key;
    }

    public void setApi_key(String api_key) {
        this.api_key = api_key;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

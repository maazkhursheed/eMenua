package com.attribe.waiterapp.models;

/**
 * Created by Sabih Ahmed on 6/1/2015.
 */
public class DeviceRegister {

    private String device_id;
    private String device_name;
    private String passcode;

    public static String STATUS_DEVICE_NAME_EXISTS = "423";
    public static String STATUS_DEVICE_ID_EXISTS = "422";
    public static String STATUS_SUCCESS = "200";

    public DeviceRegister(String device_id, String device_name,String passCode) {
        this.device_id = device_id;
        this.device_name = device_name;
        this.passcode = passCode;
    }

    public class Response {


        public String message;
        public String status;
        public String key;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }
    }



}

package com.attribe.waiterapp.utils;

/**
 * Created by Sabih Ahmed on 08-Mar-16.
 */
public class CurrencyHelper {

    static String locale;

    public static String getCurrencyPrefix(){
        String currency="";

        switch (locale){

            case LocaleHelper.PAKISTAN:

                currency = CurrencyCode.PK;
                break;

            case LocaleHelper.UAE:

                currency = CurrencyCode.UAE;

        }
        return currency;
    }

    public static void setLocale(String locale) {
        CurrencyHelper.locale = locale;
    }

    public static class LocaleHelper {
        public static final String PAKISTAN = "PK";
        public static final String UAE = "AE";
    }


    public static class CurrencyCode {
        public static final String PK = "PKR";
        public static final String UAE = "AED";
    }
}

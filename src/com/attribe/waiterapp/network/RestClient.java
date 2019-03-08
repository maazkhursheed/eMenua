package com.attribe.waiterapp.network;

import com.attribe.waiterapp.utils.Constants;
import com.attribe.waiterapp.utils.DevicePreferences;
import com.squareup.okhttp.OkHttpClient;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.ConversionException;
import retrofit.converter.Converter;
import retrofit.converter.GsonConverter;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;

import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

/**
 * Created by Sabih Ahmed on 5/13/2015.
 */
public class RestClient {

    private static ApiInterface mApiInterface;

    private RestClient(){

    }

    static{
        setUpRestClient();
    }

    public static void setUpRestClient() {

        RequestInterceptor interceptor = new RequestInterceptor(){

            @Override
            public void intercept(RequestInterceptor.RequestFacade request) {

                request.addHeader("Accept","application/json");
                //request.addHeader("Accept","application/text");
                request.addHeader(Constants.PARAM_AUTHORIZATION,DevicePreferences.getInstance().getClientKey());

            }
        };

        OkHttpClient okHttpClient=new OkHttpClient();

        okHttpClient.setReadTimeout(Constants.TIMEOUT, TimeUnit.MILLISECONDS);
        okHttpClient.setConnectTimeout(Constants.TIMEOUT, TimeUnit.MILLISECONDS);




        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Constants.BASE_URL_PROD)
                .setRequestInterceptor(interceptor)
                .setLogLevel(RestAdapter.LogLevel.FULL)
               // .setConverter(new EmptyConverter())
                .setClient(new OkClient(okHttpClient))
                .build();

        mApiInterface = restAdapter.create(ApiInterface.class);



    }

    public static ApiInterface getAdapter(){

        return mApiInterface;
    }

    private static class EmptyConverter implements Converter {


        @Override
        public Object fromBody(TypedInput body, Type type) throws ConversionException {
            return body;
        }

        @Override
        public TypedOutput toBody(Object o) {
            return (TypedOutput) o;
        }
    }
}

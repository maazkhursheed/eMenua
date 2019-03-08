package com.attribe.waiterapp.interfaces;

/**
 * Created by Maaz on 4/27/2016.
 */
public interface OrderUploadResponse {

    void success(int orderID);
    void failed();

}

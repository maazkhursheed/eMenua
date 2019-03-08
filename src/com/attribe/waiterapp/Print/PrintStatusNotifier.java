package com.attribe.waiterapp.Print;

/**
 * Created by Sabih Ahmed on 04-Feb-16.
 */
public interface PrintStatusNotifier {

    void readyForPrint();
    void printerDicsonnected();
    void sentToPrintFailed();
    void printerConnectionError();
    void printerConnectionLost();

}

package com.attribe.waiterapp.Print;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import com.attribe.waiterapp.models.Item;
import com.attribe.waiterapp.models.Order;
import com.attribe.waiterapp.utils.DevicePreferences;
import com.attribe.waiterapp.utils.OrderContainer;
import com.attribe.waiterapp.utils.Utils;
import com.zj.wfsdk.WifiCommunication;

/**
 * Created by Sabih Ahmed on 03-Feb-16.
 */
public class PrintHelper {

    private static int printerConnectionFlag = 0;
    private static WifiCommunication printerCommunication;
    private Context mContext;




    public static void initializePrinter(final PrintStatusNotifier callBack) {

        final Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case WifiCommunication.WFPRINTER_CONNECTED:
                        printerConnectionFlag = 0;
                        callBack.readyForPrint();


                        break;
                    case WifiCommunication.WFPRINTER_DISCONNECTED:
                        callBack.printerDicsonnected();


                        break;
                    case WifiCommunication.SEND_FAILED:
                        callBack.sentToPrintFailed();

                        break;
                    case WifiCommunication.WFPRINTER_CONNECTEDERR:

                        callBack.printerConnectionError();
                        break;
                    case WifiCommunication.CONNECTION_LOST:
                        callBack.printerConnectionLost();

                        break;
                    default:
                        break;
                }
            }
        };

        printerCommunication = new WifiCommunication(mHandler);

    }

    public static void connectPrinter() {
        if(printerCommunication != null){
            printerCommunication.initSocket(PrinterConfig.PRINTER_IP,9100);
        }

    }


    public static void disConnectPrinter(){
        if(printerCommunication !=null){

            printerCommunication.close();
        }


    }




    public static void printOrder(Order order) {
        printerCommunication.sendMsg(prepareOrderForPrint(order),"gbk");

        byte[] paperCut = new byte[4];
        paperCut[1] = 0x1D;
        paperCut[2] = 0x56;
        paperCut[3] = 49;
        printerCommunication.sndByte(paperCut);
    }

    private static String prepareOrderForPrint(Order order) {

        Receipt receipt = new Receipt();

        receipt.printTableNumber(order.getTableNumber());
        receipt.printWaiterName(DevicePreferences.getInstance().getWaiterName());
        receipt.printSectionName(DevicePreferences.getInstance().getSectionName());
        receipt.printTimeStamp(Utils.getCurrentTime(order.getTimeStamp()));
        receipt.printTableCover(order.getTableCover());

        receipt.printTitle();
        for(Item orderedItems :OrderContainer.getInstance().getOrderableItems()){
            receipt.print(orderedItems.getName(),orderedItems.getDesiredQuantity());
        }

        receipt.getQTReceiptTotal();

        return receipt.getReceiptData();
    }


}

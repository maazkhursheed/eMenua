package com.attribe.waiterapp.Print;

import com.attribe.waiterapp.utils.DevicePreferences;

import java.util.Formatter;

/**
 * Created by Sabih Ahmed on 29-Jan-16.
 */
public class Receipt {

     private double total = 0;
     private Formatter f = new Formatter(System.out);
     private String printData = "";
     StringBuilder builder= new StringBuilder();
     private int maxLength = 30;
     private static String DASHES_TABLE_NUMBER="------------------------------------------------ \n";
     private static String FORMAT_WITH_LINE = "\t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t" +
                                              "%s %d \n" +
                                              "______________________________________________ \n";


     private static String FORMAT_WITH_DASHED_LINE = "\t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t" +
                                                        "%s %s \n" +
                                                        DASHES_TABLE_NUMBER;

    private static String Spaces="\t \t \t \t \t \t \t \t \t \t"+
                                 "%s %s \n";

    public void printTitle() {


        getQTReceiptHeader();

     }

    public void print(String name, int qty /**double price**/) {

        getQTReceiptFormat(name, qty);
    }

    public void getCustomerReceiptTotal() {

        printData = String.format("%-25s %5s %15.2f\n", "Tax", "", total * 0.06);
        builder.append(printData);
        printData = String.format("%-25s %5s %15s\n", "", "", "-----");
        builder.append(printData);
        printData = String.format("%-25s %5s %15.2f\n", "Total", "",
                total * 1.06);

        builder.append(printData);
        builder.append("\n\n\n\n\n");
  }

    public String getReceiptData(){

        return builder.toString();
    }

    private void getQTReceiptHeader(){

        printData = String.format("%-30s %10s \n", "Item", "Qty");
        builder.append(printData);
        printData = String.format("%-30s %10s \n", "----", "---");
        builder.append(printData);
    }


    public void printTableNumber(String tableNumber){

        if(tableNumber.isEmpty()){
            tableNumber = "N/A";
        }

        printData = String.format(Spaces, "Table #", tableNumber);
        builder.append(printData);

    }

    public void printTableCover(Integer tableCover){

        if(tableCover==0){
            printData = String.format(Spaces,"Table Cover :", "N/A");
        }

        else{
            printData = String.format(Spaces,"Table Cover :", tableCover);
        }

        builder.append(printData);
    }

    public void printWaiterName(String waiterName){
        printData = String.format(Spaces,"Waiter Name :", waiterName);
        builder.append(printData);
    }

    public void printSectionName(String sectionName){

        printData = String.format(Spaces,"Section :",sectionName);
        builder.append(printData);

    }

    public void printTimeStamp(String timeStamp) {

        printData = String.format(Spaces, "Order Time :", timeStamp);
        builder.append(printData);
    }

    private void getQTReceiptFormat(String name, int qty){

        if(name.length() >= maxLength){

            name = PrintUtils.addLinebreaks(name,maxLength);

            String[] split = name.split("\n");

            int firstIndex = split[0].length();

           // int qtySpacing =  (45 - firstIndex)+15+firstLineSize ;
            int qtySpacing =   (maxLength - firstIndex ) + 10;

            StringBuilder strBulder = new StringBuilder(name);

//            strBulder.insert(name.indexOf("\n"), String.format(" %15d \n", qty));
            strBulder.insert(name.indexOf("\n"), String.format(" %"+qtySpacing+"d \n", qty));



            printData = strBulder.toString().replaceFirst("\n", " ") +"\n";



           // printData = String.format("%-25s %15d \n", name, qty);
        }

        else{
            printData = String.format("%-"+maxLength+"s %10d \n", name, qty);
        }



        builder.append(printData);


    }

    public void getQTReceiptTotal(){
        builder.append("\n\n\n\n\n");
    }


    private void getCustomerReceiptHeader(){
        printData = String.format("%-25s %5s %15s \n", "Item", "Qty", "Price");
        builder.append(printData);
        printData = String.format("%-25s %5s %5s \n", "----", "---", "-----");
        builder.append(printData);
    }

    private void getCustomerReceiptFormat(String name, int qty, double price){
        printData = String.format("%-25s %5d %15.2f\n", name, qty, price);
        builder.append(printData);

        total += price;
    }



}

package com.attribe.waiterapp.models;

import java.io.Serializable;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Sabih Ahmed on 5/12/2015.
 */
public class Order implements Serializable{

    private String itemName;
    private String itemPrice;
    private String totalPrice;
    private int quantityValue, itemId, itemCategoryId;
    private Item item;
    private String tableNumber;
    private Integer tableCover;
    private long timeStamp;
    private CopyOnWriteArrayList<Item> itemsList ;
    private int ID;
    private Integer uploaded;


    public Order(String itemName, String itemPrice, int quantityValue, int itemId, int itemCategoryId, String totalPrice) {

       this.itemName = itemName ;
        this.itemPrice = itemPrice ;
        this.quantityValue = quantityValue ;
        this.itemId = itemId ;
        this.itemCategoryId = itemCategoryId ;
        totalPrice = this.totalPrice;

    }

    public Order (Item item){
        this.item = item;
    }

    public Order(Item item, int itemQuantity) {
        this.item=item;
        this.quantityValue=itemQuantity;
    }

    public Order(Item item, int itemQuantity, long timeStamp, Integer tableCover, String tableNumber) {
        this.item=item;
        this.quantityValue=itemQuantity;
        this.timeStamp = timeStamp;
        this.tableCover = tableCover;
        this.tableNumber = tableNumber;
    }

    public Order(CopyOnWriteArrayList<Item> itemsList, Integer tableCover, String tableNumber, long timeStamp) {
        this.itemsList = itemsList;
        this.tableCover = tableCover;
        this.tableNumber = tableNumber;
        this.timeStamp = timeStamp;
    }


    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public String getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
    }

    public Integer getTableCover() {
        return tableCover;
    }

    public void setTableCover(Integer tableCover) {
        this.tableCover = tableCover;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public CopyOnWriteArrayList<Item> getItemsList() {
        return itemsList;
    }

    public void setItemsList(CopyOnWriteArrayList<Item> itemsList) {
        this.itemsList = itemsList;
    }

    public int getQuantityValue() {
        return quantityValue;
    }

    public void setQuantityValue(int quantityValue) {
        this.quantityValue = quantityValue;
    }


    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {

        this.itemName = itemName;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }


    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getItemCategoryId() {
        return itemCategoryId;
    }

    public void setItemCategoryId(int itemCategoryId) {
        this.itemCategoryId = itemCategoryId;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return this.ID;
    }

    public Integer getUploaded() {
        return uploaded;
    }

    public void setUploaded(Integer uploaded) {
        this.uploaded = uploaded;
    }
}

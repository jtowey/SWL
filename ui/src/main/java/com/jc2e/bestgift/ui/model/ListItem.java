package com.jc2e.bestgift.ui.model;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseObject;

public class ListItem extends ParseObject {

    private String itemId;
    private String listId;
    private Integer itemNum;
    private String itemName;
    private String itemDesc;
    private Integer itemPrice;
    private String itemCatNum;
    private Integer itemQty;
    private String itemSize;
    private String itemColor;

//    public ListItem() {
//    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getListId() {
        Log.d("ListItem.class:", listId);
        return listId;
    }

    public void setListId(String listId) {

        this.listId = listId;
    }

    public Integer getItemNum() {
        return itemNum;
    }

    public void setItemNum(Integer itemNum) {
        this.itemNum = itemNum;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public Integer getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(Integer itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemCatNum() {
        return itemCatNum;
    }

    public void setItemCatNum(String itemCatNum) {
        this.itemCatNum = itemCatNum;
    }

    public Integer getItemQty() {
        return itemQty;
    }

    public void setItemQty(Integer itemQty) {
        this.itemQty = itemQty;
    }

    public String getItemSize() {
        return itemSize;
    }

    public void setItemSize(String itemSize) {
        this.itemSize = itemSize;
    }

    public String getItemColor() {
        return itemColor;
    }

    public void setItemColor(String itemColor) {
        this.itemColor = itemColor;
    }


}

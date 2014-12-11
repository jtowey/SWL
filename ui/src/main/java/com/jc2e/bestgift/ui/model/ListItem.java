package com.jc2e.bestgift.ui.model;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.jc2e.bestgift.ui.ParseConstants;
import com.jc2e.bestgift.ui.R;
import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName(ParseConstants.CLASS_LIST_ITEMS)
public class ListItem extends ParseObject {

    public ListItem() {}

    private String itemId;
    private Integer itemNum;
    private String itemName;
    private String itemDesc;
    private Integer itemPrice;
    private String itemCatNum;
    private Integer itemQty;
    private String itemSize;
    private String itemColor;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
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

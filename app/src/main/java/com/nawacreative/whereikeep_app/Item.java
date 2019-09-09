package com.nawacreative.whereikeep_app;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "item_table")
public class Item {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String itemName;
    private String category;
    private String quantity;
    private String storagelocation;
    private String notes;

    public Item(String itemName, String category, String quantity, String storagelocation, String notes) {
        this.itemName = itemName;
        this.category = category;
        this.quantity = quantity;
        this.storagelocation = storagelocation;
        this.notes = notes;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getItemName() {
        return itemName;
    }

    public String getCategory() {
        return category;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getStoragelocation() {
        return storagelocation;
    }

    public String getNotes() {
        return notes;
    }
}

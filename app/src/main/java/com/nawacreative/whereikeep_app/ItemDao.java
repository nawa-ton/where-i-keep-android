package com.nawacreative.whereikeep_app;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ItemDao {
    @Insert
    void insert(Item item);

    @Update
    void update(Item item);

    @Delete
    void delete(Item item);

    @Query("delete from item_table")
    void deleteAllItem();

    @Query("select * from item_table order by id desc")
    LiveData<List<Item>> getAllItems();

    @Query("select * from item_table order by id desc")
    List<Item> fetchAllItems();


}

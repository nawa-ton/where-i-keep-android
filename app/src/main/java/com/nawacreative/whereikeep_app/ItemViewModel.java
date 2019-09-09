package com.nawacreative.whereikeep_app;

import android.app.Application;
import android.app.ListActivity;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class ItemViewModel extends AndroidViewModel {

    private ItemRepo itemRepo;
    private LiveData<List<Item>> allItems;

    public ItemViewModel(@NonNull Application application) {
        super(application);
        itemRepo = new ItemRepo(application);
        allItems = itemRepo.getAllItems();
    }

    public void insert(Item item){
        itemRepo.insert(item);
    }

    public void update(Item item){
        itemRepo.update(item);
    }

    public void delete(Item item){
        itemRepo.delete(item);
    }

    public void deleteAllItems(){
        itemRepo.deleteAllItems();
    }

    public LiveData<List<Item>> getAllItems(){
        return allItems;
    }
}

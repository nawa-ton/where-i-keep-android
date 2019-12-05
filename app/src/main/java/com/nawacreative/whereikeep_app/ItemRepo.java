package com.nawacreative.whereikeep_app;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ItemRepo {
    private ItemDao itemDao;
    private LiveData<List<Item>> allItems;

    public ItemRepo(Application application){
        ItemDatabase itemDatabase = ItemDatabase.getInstance(application);
        itemDao = itemDatabase.itemDao();
        allItems = itemDao.getAllItems();
    }

    public void insert(Item item){
        new InsertItemAsyncTask(itemDao).execute(item);
    }

    public void update(Item item){
        new UpdateItemAsyncTask(itemDao).execute(item);
    }

    public void delete(Item item){
        new DeleteItemAsyncTask(itemDao).execute(item);
    }

    public void deleteAllItems(){
        new DeleteAllItemsAsyncTask(itemDao).execute();
    }

    public LiveData<List<Item>> getAllItems(){
        return allItems;
    }

    public List<Item> fetchAllItems() {
        return itemDao.fetchAllItems();
    }

    private static class InsertItemAsyncTask extends AsyncTask<Item, Void, Void>{
        private ItemDao itemDao;

        private InsertItemAsyncTask(ItemDao itemDao){
            this.itemDao = itemDao;
        }

        @Override
        protected Void doInBackground(Item... items) {
            itemDao.insert(items[0]);
            return null;
        }
    }

    private static class UpdateItemAsyncTask extends AsyncTask<Item, Void, Void>{
        private ItemDao itemDao;

        private UpdateItemAsyncTask(ItemDao itemDao){
            this.itemDao = itemDao;
        }

        @Override
        protected Void doInBackground(Item... items) {
            itemDao.update(items[0]);
            return null;
        }
    }

    private static class DeleteItemAsyncTask extends AsyncTask<Item, Void, Void>{
        private ItemDao itemDao;

        private DeleteItemAsyncTask(ItemDao itemDao){
            this.itemDao = itemDao;
        }

        @Override
        protected Void doInBackground(Item... items) {
            itemDao.delete(items[0]);
            return null;
        }
    }

    private static class DeleteAllItemsAsyncTask extends AsyncTask<Void, Void, Void>{
        private ItemDao itemDao;

        private DeleteAllItemsAsyncTask(ItemDao itemDao){
            this.itemDao = itemDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            itemDao.deleteAllItem();
            return null;
        }
    }

}

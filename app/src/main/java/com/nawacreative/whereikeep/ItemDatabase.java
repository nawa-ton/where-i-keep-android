package com.nawacreative.whereikeep;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

//put list of table in {}
@Database(entities = {Item.class}, version = 2)
public abstract class ItemDatabase extends RoomDatabase {
    private static ItemDatabase instance;

    public abstract ItemDao itemDao();

    public static synchronized ItemDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    ItemDatabase.class, "item_database").fallbackToDestructiveMigration().addCallback(roomCallback).build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            //new PopulateDatabaseAsyncTask(instance).execute();
        }
    };

    private static class PopulateDatabaseAsyncTask extends AsyncTask<Void, Void, Void>{
        private ItemDao itemDao;

        private PopulateDatabaseAsyncTask(ItemDatabase itemDatabase){
            itemDao =itemDatabase.itemDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            itemDao.insert(new Item("Title 1", "cate 1", "2", "location 1", "notes 1"));
            itemDao.insert(new Item("Title 2", "cate 1", "10", "location 2", "notes 2"));
            return null;
        }
    }
}

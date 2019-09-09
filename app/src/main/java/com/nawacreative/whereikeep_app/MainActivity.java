package com.nawacreative.whereikeep_app;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SearchEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int ADD_ITEM_REQUEST = 1;
    public static final int EDIT_ITEM_REQUEST = 2;

    private ItemViewModel itemViewModel;
    private ItemAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("WhereIkeep");

        FloatingActionButton addButton = findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddEditItemActivity.class);
                startActivityForResult(intent, ADD_ITEM_REQUEST);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        itemAdapter = new ItemAdapter();
        recyclerView.setAdapter(itemAdapter);

        itemViewModel = ViewModelProviders.of(this).get(ItemViewModel.class);
        itemViewModel.getAllItems().observe(this, new Observer<List<Item>>() {
            @Override
            public void onChanged(@Nullable List<Item> items) {
                itemAdapter.setItems(items);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                itemViewModel.delete(itemAdapter.getItemAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Item is deleted", Toast.LENGTH_LONG).show();
            }
        }).attachToRecyclerView(recyclerView);

        itemAdapter.setOnItemClickListener(new ItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Item item) {
                Intent intent = new Intent(MainActivity.this, AddEditItemActivity.class);
                intent.putExtra(AddEditItemActivity.EXTRA_ID, item.getId());
                intent.putExtra(AddEditItemActivity.EXTRA_NAME, item.getItemName());
                intent.putExtra(AddEditItemActivity.EXTRA_CATEGORY, item.getCategory());
                intent.putExtra(AddEditItemActivity.EXTRA_QTY, item.getQuantity());
                intent.putExtra(AddEditItemActivity.EXTRA_LOCATION, item.getStoragelocation());
                intent.putExtra(AddEditItemActivity.EXTRA_NOTES, item.getNotes());
                startActivityForResult(intent, EDIT_ITEM_REQUEST);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //everything is ok
        if(requestCode == ADD_ITEM_REQUEST && resultCode == RESULT_OK){
            String name = data.getStringExtra(AddEditItemActivity.EXTRA_NAME);
            String category = data.getStringExtra(AddEditItemActivity.EXTRA_CATEGORY);
            String qty = data.getStringExtra(AddEditItemActivity.EXTRA_QTY);
            String location = data.getStringExtra(AddEditItemActivity.EXTRA_LOCATION);
            String notes = data.getStringExtra(AddEditItemActivity.EXTRA_NOTES);

            Item item = new Item(name, category, qty, location, notes);
            itemViewModel.insert(item);

            Toast.makeText(this, name + " is added", Toast.LENGTH_LONG).show();
        }else if(requestCode == EDIT_ITEM_REQUEST && resultCode == RESULT_OK){
            int id = data.getIntExtra(AddEditItemActivity.EXTRA_ID, -1);
            if(id == -1){
                Toast.makeText(this, "Item CANNOT be updated.", Toast.LENGTH_LONG).show();
                return;
            }
            String name = data.getStringExtra(AddEditItemActivity.EXTRA_NAME);
            String category = data.getStringExtra(AddEditItemActivity.EXTRA_CATEGORY);
            String qty = data.getStringExtra(AddEditItemActivity.EXTRA_QTY);
            String location = data.getStringExtra(AddEditItemActivity.EXTRA_LOCATION);
            String notes = data.getStringExtra(AddEditItemActivity.EXTRA_NOTES);

            Item item = new Item(name, category, qty, location, notes);
            item.setId(id);

            itemViewModel.update(item);

            Toast.makeText(this, name + " is updated", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this, "Item is NOT added/updated", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                itemAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_all:
                itemViewModel.deleteAllItems();
                Toast.makeText(this, "All items are deleted.", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}

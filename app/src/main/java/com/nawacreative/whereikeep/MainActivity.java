package com.nawacreative.whereikeep;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class MainActivity extends AppCompatActivity implements DeleteAllDialog.DialogListener {
    public static final int ADD_ITEM_REQUEST = 1;
    public static final int EDIT_ITEM_REQUEST = 2;

    private ItemViewModel itemViewModel;
    private ItemAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("WhereIKeep");

        FloatingActionButton addButton = findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddEditItemActivity.class);
                startActivityForResult(intent, ADD_ITEM_REQUEST);
            }
        });

        final RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        itemViewModel = ViewModelProviders.of(this).get(ItemViewModel.class);

        itemAdapter = new ItemAdapter(itemViewModel);
        recyclerView.setAdapter(itemAdapter);


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
                int position = viewHolder.getAdapterPosition();
                final Item deletedItem = itemAdapter.getItemAt(position);
                itemViewModel.delete(itemAdapter.getItemAt(position));
                //Toast.makeText(MainActivity.this, "The item is deleted", Toast.LENGTH_LONG).show();
                Snackbar.make(recyclerView, deletedItem.getItemName(), Snackbar.LENGTH_LONG).setAction("Undo delete", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemViewModel.insert(deletedItem);
                    }
                }).show();
            }

            //background of delete swipe
            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                                    float dX, float dY, int actionState, boolean isCurrentlyActive) {
                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorAccent))
                        .addActionIcon(R.drawable.ic_delete_forever_white_24dp)
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
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
                openDeleteAllDialog();
                //itemViewModel.deleteAllItems();
                //Toast.makeText(this, "All items are deleted.", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onYesClick() {
        itemViewModel.deleteAllItems();
        Toast.makeText(this, "All items are deleted.", Toast.LENGTH_LONG).show();
    }

    private void openDeleteAllDialog(){
        DeleteAllDialog dialog = new DeleteAllDialog();
        dialog.show(getSupportFragmentManager(), "Delete all items dialog");
    }
}

package com.nawacreative.whereikeep_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddEditItemActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "com.nawacreative.whereikeep_app.EXTRA_ID";
    public static final String EXTRA_NAME = "com.nawacreative.whereikeep_app.EXTRA_NAME";
    public static final String EXTRA_CATEGORY = "com.nawacreative.whereikeep_app.EXTRA_CATEGORY";
    public static final String EXTRA_QTY = "com.nawacreative.whereikeep_app.EXTRA_QTY";
    public static final String EXTRA_LOCATION = "com.nawacreative.whereikeep_app.EXTRA_LOCATION";
    public static final String EXTRA_NOTES = "com.nawacreative.whereikeep_app.EXTRA_NOTES";

    private EditText editTextName;
    private EditText editTextCategory;
    private EditText editTextQuantity;
    private EditText editTextLocation;
    private EditText editTextNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        editTextName = findViewById(R.id.editTextItemName);
        editTextCategory = findViewById(R.id.editTextCategory);
        editTextQuantity = findViewById(R.id.editTextQty);
        editTextLocation = findViewById(R.id.editTextLocation);
        editTextNotes = findViewById(R.id.editTextNotes);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white);

        Intent intent = getIntent();
        if(intent.hasExtra(EXTRA_ID)){
            setTitle("Edit Item");
            editTextName.setText(intent.getStringExtra(EXTRA_NAME));
            editTextCategory.setText(intent.getStringExtra(EXTRA_CATEGORY));
            editTextQuantity.setText(intent.getStringExtra(EXTRA_QTY));
            editTextLocation.setText(intent.getStringExtra(EXTRA_LOCATION));
            editTextNotes.setText(intent.getStringExtra(EXTRA_NOTES));
        }else {
            setTitle("Add Item");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_item_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_item:
                saveItem();
                return true;
            default:
                    return super.onOptionsItemSelected(item);
        }
    }

    private void saveItem(){
        String name = editTextName.getText().toString().trim();
        String category = editTextCategory.getText().toString().trim();
        String quantity = editTextQuantity.getText().toString().trim();
        String location = editTextLocation.getText().toString().trim();
        String notes = editTextNotes.getText().toString().trim();
        String toastMsg;

        if(name.length() == 0){
            toastMsg = "Name cannot be blank. Item is not added";
            Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            return;
        }
        else if(category.length() == 0){
            toastMsg = "Category cannot be blank. Item is not added";
            Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            return;
        }
        else if(quantity.length() == 0){
            toastMsg = "Quantity cannot be blank. Item is not added";
            Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            return;
        }
        else if(location.length() == 0){
            toastMsg = "Location cannot be blank. Item is not added";
            Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_NAME, name);
        data.putExtra(EXTRA_CATEGORY, category);
        data.putExtra(EXTRA_QTY, quantity);
        data.putExtra(EXTRA_LOCATION, location);
        data.putExtra(EXTRA_NOTES, notes);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if(id != -1){
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();

    }
}

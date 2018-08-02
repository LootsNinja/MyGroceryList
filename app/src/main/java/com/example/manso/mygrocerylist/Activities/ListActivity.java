package com.example.manso.mygrocerylist.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.manso.mygrocerylist.Data.DatabaseHandler;
import com.example.manso.mygrocerylist.Model.Grocery;
import com.example.manso.mygrocerylist.R;
import com.example.manso.mygrocerylist.UI.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<Grocery> groceryList;
    private List<Grocery> listItems;
    private List<Grocery> updatedlistItems;
    private DatabaseHandler db;

    //floating add button
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText groceryItem;
    private EditText quantity;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                createPopDialog();
            }
        });

        db = new DatabaseHandler(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        groceryList = new ArrayList<>();
        listItems = new ArrayList<>();



        //Get items from database
        groceryList = db.getAllGroceries();

        for (Grocery c: groceryList){
            Grocery grocery = new Grocery();
            grocery.setName(c.getName());
            grocery.setQuantity("Quantity: " + c.getQuantity());
            grocery.setId(c.getId());
            grocery.setDateItemAdded("Added on: " + c.getDateItemAdded());

            listItems.add(grocery);

        }
        recyclerViewAdapter = new RecyclerViewAdapter(this, listItems);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();
    }


    //floating button save thingy
    private void createPopDialog(){

        dialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup, null);
        groceryItem = (EditText) view.findViewById(R.id.groceryItem);
        quantity = (EditText) view.findViewById(R.id.groceryQuantity);
        saveButton = (Button) view.findViewById(R.id.saveButton);



        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!groceryItem.getText().toString().isEmpty() && !quantity.getText().toString().isEmpty()){
                    SaveGroceryToDB(v);

                }        }
        });

    }

    private void SaveGroceryToDB(View v){
        Grocery grocery = new Grocery();

        String newGrocery = groceryItem.getText().toString();
        String newGroceryQuantity = quantity.getText().toString();

        grocery.setName(newGrocery);
        grocery.setQuantity(newGroceryQuantity);

        //Save to DB
        db.addGrocery(grocery);

        //refresh
        //Get items from database
        groceryList = db.getAllGroceries();

        updatedlistItems = new ArrayList<>();

        for (Grocery c: groceryList){
            Grocery groceryUpdated = new Grocery();
            groceryUpdated.setName(c.getName());
            groceryUpdated.setQuantity("Quantity: " + c.getQuantity());
            groceryUpdated.setId(c.getId());
            groceryUpdated.setDateItemAdded("Added on: " + c.getDateItemAdded());

            updatedlistItems.add(groceryUpdated);

        }
        recyclerViewAdapter = new RecyclerViewAdapter(this, updatedlistItems);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();


        Snackbar.make(v, "Item saved", Snackbar.LENGTH_LONG).show();

        //Log.d("Item Added ID:", String.valueOf(db.getGroceriesCount()));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                //Start a new activity
                //startActivity(new Intent(ListActivity.this, ListActivity.class));

            }
        },1200);
    }


}

package com.example.electricitybillapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class ListActivity extends AppCompatActivity {

    ListView listView;
    DatabaseHelper db;
    ArrayList<String> recordList = new ArrayList<>();
    ArrayList<Integer> ids = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        listView = findViewById(R.id.listView);
        db = new DatabaseHelper(this);

        // Initial load
        loadRecordsFromDatabase();

        // Handle click to view details
        listView.setOnItemClickListener((parent, view, position, id) -> {
            int recordId = ids.get(position);
            Intent intent = new Intent(ListActivity.this, DetailActivity.class);
            intent.putExtra("ID", recordId);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload after returning from DetailActivity
        loadRecordsFromDatabase();
    }

    private void loadRecordsFromDatabase() {
        recordList.clear();
        ids.clear();

        Cursor cursor = db.getAllData();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String month = cursor.getString(1);
                int kWh = cursor.getInt(2);
                double total = cursor.getDouble(3);
                double rebate = cursor.getDouble(4) * 100;
                double finalCost = cursor.getDouble(5);

                String item = month + " - Final Cost: RM " + String.format("%.2f", finalCost);

                recordList.add(item);
                ids.add(id);
            } while (cursor.moveToNext());

            if (adapter == null) {
                adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, recordList);
                listView.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }
        } else {
            Toast.makeText(this, "No Records", Toast.LENGTH_SHORT).show();
        }
        if (cursor != null) {
            cursor.close();
        }
    }
}

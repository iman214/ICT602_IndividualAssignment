package com.example.electricitybillapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    TextView textMonth, textUnit, textTotal, textRebate, textFinal;
    Button btnDelete;
    DatabaseHelper db;
    int id; // Declare at class level

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        textMonth = findViewById(R.id.textMonth);
        textUnit = findViewById(R.id.textUnit);
        textTotal = findViewById(R.id.textTotal);
        textRebate = findViewById(R.id.textRebate);
        textFinal = findViewById(R.id.textFinal);
        btnDelete = findViewById(R.id.btnDelete);
        db = new DatabaseHelper(this);

        id = getIntent().getIntExtra("ID", -1);
        if (id != -1) {
            showDetails(id);
        } else {
            Toast.makeText(this, "Invalid record.", Toast.LENGTH_SHORT).show();
            finish();
        }

        btnDelete.setOnClickListener(v -> showConfirmDialog(id));
    }

    private void showDetails(int id) {
        Cursor cursor = db.getAllData();

        while (cursor.moveToNext()) {
            if (cursor.getInt(0) == id) {
                textMonth.setText("Month: " + cursor.getString(1));
                textUnit.setText("Unit Used: " + cursor.getInt(2) + " kWh");
                textTotal.setText("Total Charges: RM " + String.format("%.2f", cursor.getDouble(3)));
                textRebate.setText("Rebate: " + (cursor.getDouble(4) * 100) + "%");
                textFinal.setText("Final Cost: RM " + String.format("%.2f", cursor.getDouble(5)));
                break;
            }
        }
        cursor.close();
    }

    private void showConfirmDialog(int id) {
        new AlertDialog.Builder(this)
                .setTitle("Confirm deletion")
                .setMessage("Are you sure you want to delete this record?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    boolean success = db.deleteData(id);
                    if (success) {
                        Toast.makeText(DetailActivity.this, "Record Deleted", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(DetailActivity.this, "Failed to delete.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }
}

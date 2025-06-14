package com.example.electricitybillapp;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.*;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Spinner spinnerMonth;
    EditText editUnit;
    RadioGroup radioGroupRebate;
    TextView textTotalCharge, textFinalCost;
    Button btnCalculate, btnReset, btnSave, btnAbout, btnViewList;
    double totalCharge = 0, finalCost = 0;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Link views
        spinnerMonth = findViewById(R.id.spinnerMonth);
        editUnit = findViewById(R.id.editUnit);
        radioGroupRebate = findViewById(R.id.radioGroupRebate);
        textTotalCharge = findViewById(R.id.textTotalCharge);
        textFinalCost = findViewById(R.id.textFinalCost);
        btnCalculate = findViewById(R.id.btnCalculate);
        btnReset = findViewById(R.id.btnReset);
        btnSave = findViewById(R.id.btnSave);
        btnAbout = findViewById(R.id.btnAbout);
        btnViewList = findViewById(R.id.btnViewList);
        db = new DatabaseHelper(this);

        // Set months to spinner
        String[] months = {"January", "February", "March", "April",
                "May", "June", "July", "August",
                "September", "October", "November", "December"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item_selected, months);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerMonth.setAdapter(adapter);

        // Handle Calculate button
        btnCalculate.setOnClickListener(v -> calculateCharges());

        // Handle Reset button
        btnReset.setOnClickListener(v -> {
            editUnit.setText("");
            spinnerMonth.setSelection(0);
            radioGroupRebate.check(R.id.radio0);
            textTotalCharge.setText("Total Charges: RM 0.00");
            textFinalCost.setText("Final Cost After Rebate: RM 0.00");

            Toast.makeText(MainActivity.this, "Reset successfully.", Toast.LENGTH_SHORT).show();
        });

        // Handle Save button
        btnSave.setOnClickListener(v -> {
            String kWhStr = editUnit.getText().toString();

            if (TextUtils.isEmpty(kWhStr)) {
                Toast.makeText(this, "Please enter kWh first.", Toast.LENGTH_SHORT).show();
                return;
            }
            int kWh = Integer.parseInt(kWhStr);
            int selectedId = radioGroupRebate.getCheckedRadioButtonId();

            double rebate = 0;
            if (selectedId == R.id.radio0) {
                rebate = 0;
            } else if (selectedId == R.id.radio1) {
                rebate = 0.01;
            } else if (selectedId == R.id.radio2) {
                rebate = 0.02;
            } else if (selectedId == R.id.radio3) {
                rebate = 0.03;
            } else if (selectedId == R.id.radio4) {
                rebate = 0.04;
            } else if (selectedId == R.id.radio5) {
                rebate = 0.05;
            } else {
                Toast.makeText(this, "Please select a rebate.", Toast.LENGTH_SHORT).show();
                return;
            }
            String month = spinnerMonth.getSelectedItem().toString();

            boolean inserted = db.insertData(month, kWh, totalCharge, rebate, finalCost);
            if (inserted) {
                Toast.makeText(this, "Save successful!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Save failed.", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle View List button
        btnViewList.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ListActivity.class);
            startActivity(intent);
        });

        // Handle About button
        btnAbout.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
        });

    }

    private void calculateCharges() {
        String kWhStr = editUnit.getText().toString();

        if (TextUtils.isEmpty(kWhStr)) {
            Toast.makeText(this, "Please enter kWh.", Toast.LENGTH_SHORT).show();
            return;
        }
        int kWh = Integer.parseInt(kWhStr);

        // Validate kWh (say we consider anything above 10,000 kWh to be illogical for a household)
        if (kWh <= 0 || kWh > 10000) {
            Toast.makeText(this, "Please enter a kWh less than or equal to 10,000.", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedId = radioGroupRebate.getCheckedRadioButtonId();

        double rebate = 0;
        if (selectedId == R.id.radio0) {
            rebate = 0;
        } else if (selectedId == R.id.radio1) {
            rebate = 0.01;
        } else if (selectedId == R.id.radio2) {
            rebate = 0.02;
        } else if (selectedId == R.id.radio3) {
            rebate = 0.03;
        } else if (selectedId == R.id.radio4) {
            rebate = 0.04;
        } else if (selectedId == R.id.radio5) {
            rebate = 0.05;
        } else {
            Toast.makeText(this, "Please select a rebate.", Toast.LENGTH_SHORT).show();
            return;
        }

        totalCharge = 0;
        if (kWh <= 200) {
            totalCharge = kWh * 0.218;
        } else if (kWh <= 300) {
            totalCharge = (200 * 0.218) + ((kWh - 200) * 0.334);
        } else if (kWh <= 600) {
            totalCharge = (200 * 0.218) + (100 * 0.334) + ((kWh - 300) * 0.516);
        } else {
            totalCharge = (200 * 0.218) + (100 * 0.334) + (300 * 0.516) + ((kWh - 600) * 0.546);
        }
        finalCost = totalCharge - (totalCharge * rebate);

        textTotalCharge.setText(String.format("Total Charges: RM %.2f", totalCharge));
        textFinalCost.setText(String.format("Final Cost After Rebate: RM %.2f", finalCost));
    }

}
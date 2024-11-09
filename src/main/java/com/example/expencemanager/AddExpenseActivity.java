package com.example.expencemanager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.expencemanager.databinding.ActivityAddExpenseBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.UUID;

public class AddExpenseActivity extends AppCompatActivity {

    ActivityAddExpenseBinding binding;
    private String type;
    private ExpenseModel expenseModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityAddExpenseBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getSupportActionBar();

        type=getIntent().getStringExtra("type");
        expenseModel=(ExpenseModel)getIntent().getSerializableExtra("model");

        if(expenseModel!=null) {
            type = expenseModel.getType();
            binding.amount.setText(String.valueOf(expenseModel.getAmount()));
            binding.note.setText(expenseModel.getNote());
            binding.category.setText(expenseModel.getCategory());
        }

        if(type != null && type.equals("Income")){
            binding.income.setChecked(true);
        }else{
            binding.expense.setChecked(true);
        }

        binding.income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type="Income";
            }
        });

        binding.expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type="Expense";
            }
        });

    }

    //for update menu and delete menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        if(expenseModel==null) {
            menuInflater.inflate(R.menu.add_menu, menu);
        }else{
            menuInflater.inflate(R.menu.update_menu,menu);
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.saveExpense) {
            if(type!=null) {
                createExpense();
            }else{
                updateExpense();
            }
            return true;
        }
        if(id==R.id.deleteExpense){
            deleteExpense();
        }
        return false;
    }
    //for delete menu to  delete expense
    private void deleteExpense(){
        FirebaseFirestore
                .getInstance()
                .collection("expenses")
                .document(expenseModel.getExpenseId())
                .delete();
        finish();
    }

//for create button  to create the data
    private void createExpense() {
        String expenseId = UUID.randomUUID().toString();
        String amount = binding.amount.getText().toString();
        String note = binding.note.getText().toString();
        String category = binding.category.getText().toString();

        boolean incomeChecked = binding.income.isChecked();

        if(incomeChecked){
            type="Income";
        }else{
            type="Expense";
        }

        if (amount.trim().isEmpty()) {
            binding.amount.setError("Empty");
            return;
        }
        ExpenseModel expenseModel = new ExpenseModel(expenseId,note,category,type, Long.parseLong(amount), Calendar.getInstance().getTimeInMillis(),
                FirebaseAuth.getInstance().getUid());
        FirebaseFirestore.getInstance().collection("expenses").document(expenseId).set(expenseModel);
        finish();
    }

    private void updateExpense() {
        String expenseId =expenseModel.getExpenseId();
        String amount = binding.amount.getText().toString();
        String note = binding.note.getText().toString();
        String category = binding.category.getText().toString();

        boolean incomeChecked = binding.income.isChecked();

        if(incomeChecked){
            type="Income";
        }else{
            type="Expense";
        }

        if (amount.trim().isEmpty()) {
            binding.amount.setError("Empty");
            return;
        }
        ExpenseModel model = new ExpenseModel(expenseId,note,category,type, Long.parseLong(amount),expenseModel.getTime(),
                FirebaseAuth.getInstance().getUid());
        FirebaseFirestore.getInstance().collection("expenses").document(expenseId).set(model);
        finish();
    }

}
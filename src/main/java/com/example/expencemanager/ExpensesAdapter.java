package com.example.expencemanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExpensesAdapter  extends RecyclerView.Adapter<ExpensesAdapter.MyViewHolder>{

    private Context context;
    private List<ExpenseModel> expenseModelList;
    private OnItemClick onItemClick;



    public ExpensesAdapter(Context context,OnItemClick onItemClick){
        this.context=context;
        this.onItemClick=onItemClick;
        expenseModelList=new ArrayList<>();
    }
    //data adding function for adapter
    public void add(ExpenseModel expenseModel){
        expenseModelList.add(expenseModel);
        notifyDataSetChanged();
    }
    //this function clear the data in the adapter
    public void clear(){
        expenseModelList.clear();
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       //for getting layout in recyclerView
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
         //fetch the data in the view
        //getting the data at the specific position
        ExpenseModel expenseModel=expenseModelList.get(position);
        holder.note.setText(expenseModel.getNote());
        holder.category.setText(expenseModel.getCategory());
        holder.amount.setText(String .valueOf(expenseModel.getAmount()));

        holder.date.setText(new Date(expenseModel.getTime()).toString());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.onClick(expenseModel);
            }
        });
    }

    @Override
    public int getItemCount() {
        //it show length of the item
        return expenseModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView note,category,amount,date;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            note=itemView.findViewById(R.id.note);
            category=itemView.findViewById(R.id.category);
            amount=itemView.findViewById(R.id.amount);
            date=itemView.findViewById(R.id.date);
        }
    }
}

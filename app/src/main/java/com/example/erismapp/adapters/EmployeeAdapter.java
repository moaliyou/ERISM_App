package com.example.erismapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.erismapp.R;
import com.example.erismapp.models.EmployeeModel;

import java.util.ArrayList;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder>{


    private Context mContext;
    private ArrayList<EmployeeModel> employeeArrayList;

    public EmployeeAdapter(Context context, ArrayList<EmployeeModel> employeeArrayList) {
        mContext = context;
        this.employeeArrayList = employeeArrayList;
    }

    @NonNull
    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(mContext)
                .inflate(R.layout.list_employee_layout, parent, false);
        return new EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return (employeeArrayList != null) ? employeeArrayList.size() : 0;
    }

    public class EmployeeViewHolder extends RecyclerView.ViewHolder {

        public EmployeeViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}

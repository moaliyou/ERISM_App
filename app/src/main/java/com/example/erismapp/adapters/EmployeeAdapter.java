package com.example.erismapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull EmployeeViewHolder holder, int position) {

        EmployeeModel employee = employeeArrayList.get(position);

        holder.tvEmployeeFullName.setText(employee.getFirstName() + " " + employee.getLastName());
        holder.tvJobTitle.setText(employee.getJobTitle());
        holder.tvEmployeeId.setText(employee.getEmployeeId());
        holder.tvDateOfBirth.setText(employee.getDateOfBirth());
        holder.tvSalary.setText(String.valueOf(employee.getSalary()));
        holder.tvHireDate.setText(employee.getHireDate());

    }

    @Override
    public int getItemCount() {
        return (employeeArrayList != null) ? employeeArrayList.size() : 0;
    }

    public class EmployeeViewHolder extends RecyclerView.ViewHolder {

        TextView tvEmployeeFullName, tvJobTitle, tvEmployeeId,
        tvDateOfBirth, tvHireDate, tvSalary;


        public EmployeeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEmployeeFullName = itemView.findViewById(R.id.tv_employee_full_name);
            tvJobTitle = itemView.findViewById(R.id.tv_job_title);
            tvJobTitle = itemView.findViewById(R.id.tv_job_title);
            tvEmployeeId = itemView.findViewById(R.id.tv_id_no);
            tvDateOfBirth = itemView.findViewById(R.id.tv_date_of_birth);
            tvHireDate = itemView.findViewById(R.id.tv_hire_date);
            tvSalary = itemView.findViewById(R.id.tv_salary);
        }
    }

}

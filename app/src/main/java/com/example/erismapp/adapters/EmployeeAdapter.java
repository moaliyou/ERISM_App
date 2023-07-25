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
import com.example.erismapp.interfaces.RecyclerViewInterface;
import com.example.erismapp.models.EmployeeModel;

import java.util.ArrayList;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder> {


    private final Context mContext;
    private ArrayList<EmployeeModel> employeeArrayList;
    private final RecyclerViewInterface mRecyclerViewInterface;

    public EmployeeAdapter(Context context, ArrayList<EmployeeModel> employeeArrayList,
                           RecyclerViewInterface recyclerViewInterface) {
        mContext = context;
        this.employeeArrayList = employeeArrayList;
        this.mRecyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(mContext)
                .inflate(R.layout.list_employee_layout, parent, false);
        return new EmployeeViewHolder(view, mRecyclerViewInterface);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(@NonNull EmployeeViewHolder holder, int position) {

        EmployeeModel employee = employeeArrayList.get(position);

        holder.tvEmployeeFullName.setText(employee.getFirstName() + " " + employee.getLastName());
        holder.tvJobTitle.setText(employee.getJobTitle());

        holder.tvEmployeeId.setText(
                mContext.getResources().getString(R.string.string_tv_id_no) + " " +
                        employee.getEmployeeId()
        );

        holder.tvDateOfBirth.setText(
                mContext.getResources().getString(R.string.string_tv_date_of_birth) + " " +
                        employee.getDateOfBirth()
        );

        holder.tvSalary.setText(
                String.format("Salary: $%,.1f", employee.getSalary())
        );

        holder.tvHireDate.setText(
                mContext.getResources().getString(R.string.string_tv_hire_date) + " " +
                        employee.getHireDate()
        );

    }

    @Override
    public int getItemCount() {
        return (employeeArrayList != null) ? employeeArrayList.size() : 0;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setFilteredList(ArrayList<EmployeeModel> filteredEmployeeList) {
        this.employeeArrayList = filteredEmployeeList;
        notifyDataSetChanged();
    }

    public static class EmployeeViewHolder extends RecyclerView.ViewHolder {

        TextView tvEmployeeFullName, tvJobTitle, tvEmployeeId,
                tvDateOfBirth, tvHireDate, tvSalary;


        public EmployeeViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            tvEmployeeFullName = itemView.findViewById(R.id.tv_employee_full_name);
            tvJobTitle = itemView.findViewById(R.id.tv_job_title);
            tvJobTitle = itemView.findViewById(R.id.tv_job_title);
            tvEmployeeId = itemView.findViewById(R.id.tv_id_no);
            tvDateOfBirth = itemView.findViewById(R.id.tv_date_of_birth);
            tvHireDate = itemView.findViewById(R.id.tv_hire_date);
            tvSalary = itemView.findViewById(R.id.tv_salary);

            itemView.setOnClickListener(view -> {

                int position = getAdapterPosition();

                if (isRecyclerViewNull(recyclerViewInterface, position))
                    recyclerViewInterface.onItemClick(position);

            });

            itemView.setOnLongClickListener(view -> {

                int position = getAdapterPosition();

                if (isRecyclerViewNull(recyclerViewInterface, position))
                    recyclerViewInterface.onItemLongClick(position);

                return true;

            });

        }

        private boolean isRecyclerViewNull(RecyclerViewInterface viewInterface, int position) {
            return (viewInterface != null && position != RecyclerView.NO_POSITION);
        }
    }

}

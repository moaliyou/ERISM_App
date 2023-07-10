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
import com.example.erismapp.models.RetirementPlanModel;

import java.util.ArrayList;

public class RetirementPlanAdapter extends
        RecyclerView.Adapter<RetirementPlanAdapter.RetirementPlanViewHolder> {


    private final Context mContext;
    private final RecyclerViewInterface mRecyclerViewInterface;
    private ArrayList<RetirementPlanModel> retirementPlanArrayList;

    public RetirementPlanAdapter(
            Context context,
            ArrayList<RetirementPlanModel> retirementPlanArrayList,
            RecyclerViewInterface recyclerViewInterface
    ) {
        mContext = context;
        this.retirementPlanArrayList = retirementPlanArrayList;
        this.mRecyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public RetirementPlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(mContext)
                .inflate(R.layout.list_retirement_plan_layout, parent, false);
        return new RetirementPlanViewHolder(view, mRecyclerViewInterface);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RetirementPlanViewHolder holder, int position) {

        RetirementPlanModel retirementPlanModel = retirementPlanArrayList.get(position);

        holder.tvPlanName.setText(retirementPlanModel.getPlanName());
        holder.tvPlanType.setText(retirementPlanModel.getPlanType());
        holder.tvEmployerContributionRate.setText(
                retirementPlanModel.getEmployerContributionRate() + "%"
        );
        holder.tvEmployeeContributionRate.setText(
                retirementPlanModel.getEmployeeContributionRate() + "%"
        );
        holder.tvVestingPeriod.setText(retirementPlanModel.getVestingPeriod() + " years");
        holder.tvMaxContributionLimit.setText("$" + retirementPlanModel.getMaxContributionLimit());
        holder.tvMinContributionLimit.setText("$" + retirementPlanModel.getMinContributionLimit());

    }

    @Override
    public int getItemCount() {
        return (retirementPlanArrayList != null) ? retirementPlanArrayList.size() : 0;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setFilteredList(ArrayList<RetirementPlanModel> filteredRetirementPlanList) {
        this.retirementPlanArrayList = filteredRetirementPlanList;
        notifyDataSetChanged();
    }

    public static class RetirementPlanViewHolder extends RecyclerView.ViewHolder {

        TextView tvPlanName, tvPlanType,
                tvEmployerContributionRate, tvEmployeeContributionRate, tvVestingPeriod,
                tvMaxContributionLimit, tvMinContributionLimit;

        public RetirementPlanViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            tvPlanName = itemView.findViewById(R.id.tv_plan_name);
            tvPlanType = itemView.findViewById(R.id.tv_plan_type);
            tvEmployerContributionRate = itemView.findViewById(R.id.tv_employer_contribution_rate);
            tvEmployeeContributionRate = itemView.findViewById(R.id.tv_employee_contribution_rate);
            tvVestingPeriod = itemView.findViewById(R.id.tv_vesting_period);
            tvMaxContributionLimit = itemView.findViewById(R.id.tv_max_contribution_limit);
            tvMinContributionLimit = itemView.findViewById(R.id.tv_min_contribution_limit);

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

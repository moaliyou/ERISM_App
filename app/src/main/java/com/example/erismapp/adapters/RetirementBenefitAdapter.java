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
import com.example.erismapp.models.RetirementBenefitModel;

import java.util.ArrayList;

public class RetirementBenefitAdapter extends RecyclerView.Adapter<RetirementBenefitAdapter.RetirementBenefitViewHolder> {


    private final Context mContext;
    private final RecyclerViewInterface mRecyclerViewInterface;
    private ArrayList<RetirementBenefitModel> retirementBenefitArrayList;

    public RetirementBenefitAdapter(
            Context context,
            ArrayList<RetirementBenefitModel> retirementBenefitArrayList,
            RecyclerViewInterface recyclerViewInterface
    ) {
        mContext = context;
        this.retirementBenefitArrayList = retirementBenefitArrayList;
        this.mRecyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public RetirementBenefitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(mContext)
                .inflate(R.layout.list_retirement_benefit_layout, parent, false);
        return new RetirementBenefitViewHolder(view, mRecyclerViewInterface);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RetirementBenefitViewHolder holder, int position) {

        RetirementBenefitModel retirementBenefitModel = retirementBenefitArrayList.get(position);

        holder.tvEmployeeName.setText(retirementBenefitModel.getEmployeeName());
        holder.tvBenefitType.setText(retirementBenefitModel.getBenefitType());
        holder.tvContributionAmount.setText("$" +
                retirementBenefitModel.getContributionAmount()
        );
        holder.tvContributionFrequency.setText(retirementBenefitModel.getContributionFrequency());

    }

    @Override
    public int getItemCount() {
        return (retirementBenefitArrayList != null) ? retirementBenefitArrayList.size() : 0;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setFilteredList(ArrayList<RetirementBenefitModel> filteredRetirementBenefitList) {
        this.retirementBenefitArrayList = filteredRetirementBenefitList;
        notifyDataSetChanged();
    }

    public static class RetirementBenefitViewHolder extends RecyclerView.ViewHolder {

        TextView tvEmployeeName, tvBenefitType,
                tvContributionAmount, tvContributionFrequency;

        public RetirementBenefitViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            tvEmployeeName = itemView.findViewById(R.id.tv_employee_name);
            tvBenefitType = itemView.findViewById(R.id.tv_benefit_type);
            tvContributionAmount = itemView.findViewById(R.id.tv_contribution_amount);
            tvContributionFrequency = itemView.findViewById(R.id.tv_contribution_frequency);

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

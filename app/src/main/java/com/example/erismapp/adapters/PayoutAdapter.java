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
import com.example.erismapp.models.PayoutModel;

import java.util.ArrayList;

public class PayoutAdapter extends
        RecyclerView.Adapter<PayoutAdapter.PayoutViewHolder> {


    private final Context mContext;
    private final RecyclerViewInterface mRecyclerViewInterface;
    private ArrayList<PayoutModel> payoutArrayList;

    public PayoutAdapter(
            Context context,
            ArrayList<PayoutModel> payoutArrayList,
            RecyclerViewInterface recyclerViewInterface
    ) {
        mContext = context;
        this.payoutArrayList = payoutArrayList;
        this.mRecyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public PayoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(mContext)
                .inflate(R.layout.list_payout_layout, parent, false);
        return new PayoutViewHolder(view, mRecyclerViewInterface);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(@NonNull PayoutViewHolder holder, int position) {

        PayoutModel payoutModel = payoutArrayList.get(position);

        holder.tvEmployeeName.setText(payoutModel.getEmployeeName());
        holder.tvBenefitType.setText(payoutModel.getBenefitType());
        holder.tvPayoutAmount.setText(
                String.format("$%,.1f", payoutModel.getPayoutAmount())
        );
        holder.tvPayoutDate.setText(payoutModel.getPayoutDate());

    }

    @Override
    public int getItemCount() {
        return (payoutArrayList != null) ? payoutArrayList.size() : 0;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setFilteredList(ArrayList<PayoutModel> filteredPayoutList) {
        this.payoutArrayList = filteredPayoutList;
        notifyDataSetChanged();
    }

    public static class PayoutViewHolder extends RecyclerView.ViewHolder {

        TextView tvEmployeeName, tvBenefitType,
                tvPayoutAmount, tvPayoutDate;

        public PayoutViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            tvEmployeeName = itemView.findViewById(R.id.tv_employee_name);
            tvBenefitType = itemView.findViewById(R.id.tv_benefit_type);
            tvPayoutAmount = itemView.findViewById(R.id.tv_payout_amount);
            tvPayoutDate = itemView.findViewById(R.id.tv_payout_date);

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

package com.example.erismapp.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.erismapp.R;
import com.example.erismapp.adapters.RetirementPlanAdapter;
import com.example.erismapp.interfaces.RecyclerViewInterface;
import com.example.erismapp.models.RetirementPlanModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class RetirementPlanFragment extends Fragment implements RecyclerViewInterface {

    private FloatingActionButton fabAddRetirementPlan;
    private View mainView;
    private RecyclerView retirementPlanRecyclerView;
    private RetirementPlanAdapter retirementPlanAdapter;
    private ArrayList<RetirementPlanModel> retirementPlanList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainView = inflater.inflate(R.layout.fragment_retirement_plan, container, false);
        return mainView;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void initViews() {
        fabAddRetirementPlan = mainView.findViewById(R.id.fab_retirement_benefit);
        retirementPlanRecyclerView = mainView.findViewById(R.id.rv_retirement_plan);

        retirementPlanRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        retirementPlanRecyclerView.setHasFixedSize(true);
        retirementPlanAdapter = new RetirementPlanAdapter(
                getContext(), retirementPlanList, this
        );
        retirementPlanRecyclerView.setAdapter(retirementPlanAdapter);
        retirementPlanAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onItemLongClick(int position) {

    }
}
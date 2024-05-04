package com.example.project;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DrugAdapter extends RecyclerView.Adapter<DrugAdapter.ViewHolder> {
    private ArrayList<Drug> drugs;

    public DrugAdapter() {
        this.drugs = new ArrayList<>();
    }

    public void setDrugs(ArrayList<Drug> drugs) {
        this.drugs = drugs;
        notifyDataSetChanged();
        logDrugs();
    }

    private void logDrugs() {
        String TAG = "DrugAdapter";
        Log.d(TAG, "Drug list size: " + drugs.size());
        for (Drug drug : drugs) {
            Log.d(TAG, "Product NDC: " + drug.getProduct_ndc());
            Log.d(TAG, "Generic Name: " + drug.getGeneric_name());
            Log.d(TAG, "Labeler Name: " + drug.getLabeler_name());
            Log.d(TAG, "Brand Name: " + drug.getBrand_name());
            Log.d(TAG, "Dosage Form: " + drug.getDosage_form());
            Log.d(TAG, "Product Type: " + drug.getProduct_type());
        }
    }

    @NonNull
    @Override
    public DrugAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drug_card_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Drug drug = drugs.get(position);

        holder.productNdc.setText(drug.getProduct_ndc());
        holder.genericName.setText(drug.getGeneric_name());
        holder.labelerName.setText(drug.getLabeler_name());
        holder.brandName.setText(drug.getBrand_name());
        holder.dosageForm.setText(drug.getDosage_form());
        holder.productType.setText(drug.getProduct_type());
    }

    @Override
    public int getItemCount() {
        return drugs.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView productNdc;
        TextView genericName;
        TextView labelerName;
        TextView brandName;
        TextView dosageForm;
        TextView productType;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productNdc = itemView.findViewById(R.id.ProductNdc);
            genericName = itemView.findViewById(R.id.GenericName);
            labelerName = itemView.findViewById(R.id.LabelerName);
            brandName = itemView.findViewById(R.id.BrandName);
            dosageForm = itemView.findViewById(R.id.DosageForm);
            productType = itemView.findViewById(R.id.ProductType);
        }
    }
}
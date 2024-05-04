package com.example.project;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DrugsFragment extends Fragment {
    private static final String BASE_URL = "https://drug-info-and-price-history.p.rapidapi.com/1/";
    private static final String API_KEY = "371ab6ae03msh41f8a0eeec0aa76p15e073jsn2c75ccdccb86";
    private static final String API_HOST = "drug-info-and-price-history.p.rapidapi.com";
    private final String TAG = "APIError";
    private DrugAdapter drugAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_reservations, container, false);

        EditText searchEditText = root.findViewById(R.id.searchEditText);
        RecyclerView recyclerView = root.findViewById(R.id.Records);
        Button search = root.findViewById(R.id.searchButton);

        drugAdapter = new DrugAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(drugAdapter);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = searchEditText.getText().toString().trim();
                Log.d(TAG, query);
                if (!query.isEmpty()) {
                    fetchDataFromApi(query);
                }
            }
        });

        /*searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String query = editable.toString().trim();
                Log.e(TAG, query);
                if (!query.isEmpty()) {
                    fetchDataFromApi(query);
                }
            }
        });*/

        return root;
    }

    private void fetchDataFromApi(String query) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        DrugAPI drugAPI = retrofit.create(DrugAPI.class);
        Call<ArrayList<Drug>> call = drugAPI.getDrugInfo(API_KEY, API_HOST, query);
        call.enqueue(new Callback<ArrayList<Drug>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Drug>> call, @NonNull Response<ArrayList<Drug>> response) {
                if (response.isSuccessful()) {
                    ArrayList<Drug> drugs = response.body();
                    if (drugs != null && !drugs.isEmpty()) {
                        drugAdapter.setDrugs(drugs);
                    } else {
                        Log.e(TAG, "No drugs found: " + response.message());
                        Toast.makeText(getActivity(), "No drugs found", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.e(TAG, "Failed to fetch drugs: " + response.message());
                    Toast.makeText(getActivity(), "Failed to fetch drugs", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Drug>> call, @NonNull Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(getActivity(), "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    if (call.isCanceled()) {
                        Toast.makeText(getActivity(), "Request canceled", Toast.LENGTH_LONG).show();
                    } else {
                        t.printStackTrace();
                        Log.e(TAG, "Error: " + t.getMessage());
                        Toast.makeText(getActivity(), "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                        if (t instanceof HttpException) {
                            HttpException httpException = (HttpException) t;
                            int statusCode = httpException.code();
                            Log.e(TAG, "HTTP error code: " + statusCode);
                            Toast.makeText(getActivity(), "HTTP error code: " + statusCode, Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });
    }
}
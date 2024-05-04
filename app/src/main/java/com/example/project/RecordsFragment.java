package com.example.project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class RecordsFragment extends Fragment {
    private RecordsAdapter recordsAdapter;
    private ArrayList<Record> recordsList;
    private Spinner sortSpinner;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_records, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.Records);
        recordsList = new ArrayList<>();
        recordsAdapter = new RecordsAdapter(requireContext(), recordsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(recordsAdapter);

        sortSpinner = view.findViewById(R.id.sortSpinner);
        setupSortSpinner();

        fetchRecordsFromFirebase();
    }


    private void fetchRecordsFromFirebase() {
        DatabaseReference recordsRef = FirebaseDatabase.getInstance().getReference("records");
        String userID = getCurrentUserID();

        recordsRef.orderByChild("userID").equalTo(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                recordsList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Record record = snapshot.getValue(Record.class);
                    if (record != null) {
                        recordsList.add(record);
                    }
                }
                recordsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }

    private String getCurrentUserID() {
        Bundle args = getArguments();
        if (args != null) {
            return args.getString("USERID");
        }
        return null;
    }

    private void sortRecords(String selectedOption) {
        if (recordsList.size() <= 1) {
            return;
        }

        switch (selectedOption) {
            case "Oldest to Newest":
                Collections.sort(recordsList, new Comparator<Record>() {
                    @Override
                    public int compare(Record record1, Record record2) {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        try {
                            Date date1 = sdf.parse(record1.getDate());
                            Date date2 = sdf.parse(record2.getDate());
                            assert date1 != null;
                            return date1.compareTo(date2);
                        } catch (ParseException e) {
                            e.printStackTrace();
                            return 0;
                        }
                    }
                });
                break;
            case "Newest to Oldest":
                Collections.sort(recordsList, new Comparator<Record>() {
                    @Override
                    public int compare(Record record1, Record record2) {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        try {
                            Date date1 = sdf.parse(record1.getDate());
                            Date date2 = sdf.parse(record2.getDate());
                            assert date2 != null;
                            return date2.compareTo(date1);
                        } catch (ParseException e) {
                            e.printStackTrace();
                            return 0;
                        }
                    }
                });
                break;
        }
        recordsAdapter.notifyDataSetChanged();
    }

    private void setupSortSpinner() {
        List<String> sortOptions = Arrays.asList("Oldest to Newest", "Newest to Oldest");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, sortOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(adapter);
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedOption = sortOptions.get(position);
                sortRecords(selectedOption);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}

package com.example.project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;

public class RecordsFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecordsAdapter recordsAdapter;
    private ArrayList<Record> recordsList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_records, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.Records);
        recordsList = new ArrayList<>();
        recordsAdapter = new RecordsAdapter(requireContext(), recordsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(recordsAdapter);

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
}

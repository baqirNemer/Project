package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RecordsAdapter extends RecyclerView.Adapter<RecordsAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<Record> records;

    public RecordsAdapter(Context context, ArrayList<Record> records) {
        this.context = context;
        this.records = records;
    }

    @NonNull
    @Override
    public RecordsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_card_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordsAdapter.ViewHolder holder, int position) {
        Record record = records.get(position);

        //holder.categoryName.setText(record.getCategoryID());
        holder.date.setText(record.getDate());
        //holder.doctorName.setText(record.getDoctorID());
        //holder.hospitalName.setText(record.getHospitalID());
        //holder.userName.setText(record.getUserID());
        fetchHospitalName(record.getHospitalID(), holder.hospitalName);
        fetchDoctorName(record.getDoctorID(), holder.doctorName);
        //fetchUserName(record.getUserID(), holder.userName);
        fetchCategoryName(record.getCategoryID(), holder.categoryName);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(context, RecordCardPage.class);
                intent.putExtra("RESULT", record.getResult());
                intent.putExtra("DESC", record.getDescription());
                intent.putExtra("CATEGORY", holder.categoryName.getText());
                intent.putExtra("DOCTOR", holder.doctorName.getText());
                intent.putExtra("DATE", holder.date.getText());
                intent.putExtra("HOSPITAL", holder.hospitalName.getText());
                //intent.putExtra("HID", record.getHospitalID());
                intent.putExtra("DID", record.getDoctorID());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;
        TextView date;
        TextView doctorName;
        TextView hospitalName;
        //TextView userName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.CategoryName);
            date = itemView.findViewById(R.id.Date);
            doctorName = itemView.findViewById(R.id.DoctorName);
            hospitalName = itemView.findViewById(R.id.HospitalName);
            //userName = itemView.findViewById(R.id.UserName);
        }
    }

    private void fetchHospitalName(String hospitalId, TextView hospitalNameTextView) {
        DatabaseReference hospitalsRef = FirebaseDatabase.getInstance().getReference("hospitals");
        hospitalsRef.child(hospitalId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Hospital hospital = dataSnapshot.getValue(Hospital.class);
                    if (hospital != null) {
                        String hospitalName = hospital.getName();
                        hospitalNameTextView.setText(hospitalName);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }

    private void fetchDoctorName(String doctorId, TextView doctorNameTextView) {
        DatabaseReference doctorsRef = FirebaseDatabase.getInstance().getReference("doctors");
        doctorsRef.child(doctorId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Doctor doctor = dataSnapshot.getValue(Doctor.class);
                    if (doctor != null) {
                        String doctorName = doctor.getFirstName() + " " + doctor.getLastName();
                        doctorNameTextView.setText(doctorName);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }

    /*private void fetchUserName(String userId, TextView userNameTextView) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
        usersRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        String userName = user.getFirstName() + " " + user.getLastName();
                        userNameTextView.setText(userName);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }*/

    private void fetchCategoryName(String categoryId, TextView categoryNameTextView) {
        DatabaseReference categoriesRef = FirebaseDatabase.getInstance().getReference("categories");
        categoriesRef.child(categoryId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Category category = dataSnapshot.getValue(Category.class);
                    if (category != null) {
                        String categoryName = category.getName();
                        categoryNameTextView.setText(categoryName);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }
}
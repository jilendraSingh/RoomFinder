package com.example.roomfinderapp.adapter_classes;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roomfinderapp.R;
import com.example.roomfinderapp.activity.MessageActivity;

import java.util.HashMap;
import java.util.List;

public class RoomMateListAdapter extends RecyclerView.Adapter<RoomMateListAdapter.MyViewHolder> {
    Context context;
    List<HashMap<String, String>> arraList;
    String purposeValue = "";

    public RoomMateListAdapter(Context context, List<HashMap<String, String>> arraList, String purposeValue) {
        this.arraList = arraList;
        this.context = context;
        this.purposeValue = purposeValue;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_mate_list_adapter_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        HashMap<String, String> hashMap = arraList.get(position);
        holder.roommateName.setText(hashMap.get("name"));
        holder.roommateAddress.setText(hashMap.get("address"));
        holder.roommateGender.setText(hashMap.get("gender"));
        holder.roommateBudget.setText(hashMap.get("budget"));
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView roommateName, roommateAddress, roommateGender, roommateBudget;
        Button sendMessageButton;

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            roommateName = itemView.findViewById(R.id.roommateName);
            roommateAddress = itemView.findViewById(R.id.roommateAddress);
            roommateGender = itemView.findViewById(R.id.roommateGender);
            roommateBudget = itemView.findViewById(R.id.roommateBudget);

            sendMessageButton = itemView.findViewById(R.id.sendMessageButton);
            sendMessageButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), MessageActivity.class);
            intent.putExtra("phone", arraList.get(getAdapterPosition()).get("phone"));
            view.getContext().startActivity(intent);
        }
    }

    @Override
    public int getItemCount() {
        return arraList.size();
    }
}

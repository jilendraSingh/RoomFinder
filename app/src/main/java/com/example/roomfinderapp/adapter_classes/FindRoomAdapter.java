package com.example.roomfinderapp.adapter_classes;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roomfinderapp.R;
import com.example.roomfinderapp.Utility;
import com.example.roomfinderapp.activity.RoomDetailActivity;

import java.util.HashMap;
import java.util.List;

public class FindRoomAdapter extends RecyclerView.Adapter<FindRoomAdapter.MyViewHolder> {

    Context context;
    List<HashMap<String, String>> arraList;
    List<HashMap<String, byte[]>> imgArrayList;
    String itemId;
    Bitmap bitmap;

    public FindRoomAdapter(Context context, List<HashMap<String, String>> arraList, List<HashMap<String, byte[]>> imgArrayList) {
        this.arraList = arraList;
        this.context = context;
        this.imgArrayList = imgArrayList;
    }


    @NonNull
    @Override
    public FindRoomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.find_room_adapter_row, parent, false);
        return new FindRoomAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FindRoomAdapter.MyViewHolder holder, int position) {
        HashMap<String, String> hashMap = arraList.get(position);
        holder.tv_FindRoomAddress.setText(hashMap.get("address"));
        holder.tv_FindRoomBudget.setText(hashMap.get("budget"));
        holder.tv_FindCityName.setText(hashMap.get("city"));
        HashMap<String, byte[]> imghashmap = imgArrayList.get(position);
        byte[] img_byteArray = imghashmap.get("roomimage");
        try {
            assert img_byteArray != null;
             bitmap = BitmapFactory.decodeByteArray(img_byteArray, 0, img_byteArray.length);
             bitmap= Utility.getRealImage(bitmap);
            holder.imageView.setImageBitmap(bitmap);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }


    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_FindRoomAddress, tv_FindRoomBudget, tv_FindCityName;
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_FindRoomAddress = itemView.findViewById(R.id.tv_FindRoomAddress);
            tv_FindRoomBudget = itemView.findViewById(R.id.tv_FindRoomBudget);
            tv_FindCityName = itemView.findViewById(R.id.tv_FindCityName);
            imageView = itemView.findViewById(R.id.imageView_findRoom);
            imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            itemId = arraList.get(getAdapterPosition()).get("id");
            Intent intent = new Intent(view.getContext(), RoomDetailActivity.class);
            intent.putExtra("budget", arraList.get(getAdapterPosition()).get("budget"));
            intent.putExtra("city", arraList.get(getAdapterPosition()).get("city"));
            intent.putExtra("phone", arraList.get(getAdapterPosition()).get("phone"));
            intent.putExtra("floor", arraList.get(getAdapterPosition()).get("floor"));
            intent.putExtra("roomtype", arraList.get(getAdapterPosition()).get("roomtype"));
            intent.putExtra("facility", arraList.get(getAdapterPosition()).get("facility"));
            intent.putExtra("address", arraList.get(getAdapterPosition()).get("address"));
            intent.putExtra("roomimage", imgArrayList.get(getAdapterPosition()).get("roomimage"));

            view.getContext().startActivity(intent);
        }
    }

    @Override
    public int getItemCount() {
        return arraList.size();
    }
}

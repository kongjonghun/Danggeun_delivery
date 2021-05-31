package org.techtown.dangguen.Adapter;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.techtown.dangguen.DetailActivity;
import org.techtown.dangguen.Model.Room;
import org.techtown.dangguen.R;

import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.ViewHolder> {
    private Context mContext;
    private List<Room> rooms;

    public DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");

    public RoomAdapter(Context mContext, List<Room> rooms){
        this.rooms = rooms;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.room_item, parent, false);
        return new RoomAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Room room = rooms.get(position);
        Log.d("!!!!",room.toString());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("#@!!", snapshot.child(room.getManager_id()).toString());
                holder.host_id.setText(snapshot.child(room.getManager_id()).child("username").getValue().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Glide.with(mContext).load(room.getRes_img()).into(holder.res_image);


        holder.res_name.setText(room.getRes_name());
//        holder.last_msg.setVisibility(View.GONE);

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra("res_img", room.getRes_img());
                intent.putExtra("min_del", room.getMin_del());
                intent.putExtra("max_del", room.getMax_del());
                intent.putExtra("prcie", room.getPrcie());
                intent.putExtra("res_name", room.getRes_name());
                intent.putExtra("manager_id", room.getManager_id());

                Log.d("*!",room.getManager_id().toString());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return rooms.size();

    }

    // 2개 Class 생성 필요 : Single Adapter Class + ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView res_name;
        public TextView host_id;
        ShapeableImageView res_image;
        LinearLayout item;
        TextView last_msg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            host_id = itemView.findViewById(R.id.host_id);
            res_name = itemView.findViewById(R.id.name);
            res_image = itemView.findViewById(R.id.imageView);
            item = itemView.findViewById(R.id.item);
            last_msg = itemView.findViewById(R.id.last_msg);

        }
    }

}

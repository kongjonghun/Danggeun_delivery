package org.techtown.dangguen.Adapter;
// adapter에서 item들을 관리, layout도 viewHoler에 넣어서 관리
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

import org.techtown.dangguen.EatDetailActivity;
import org.techtown.dangguen.R;
import org.techtown.dangguen.Model.Restaurant;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

//viewholder를 담고 있는 Adapter를 상속한다.
// recyclerview.adapter를 상속받음
public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder>{
    List<Restaurant> mRestaurant;
    private Context mContext;

    public RestaurantAdapter(Context mContext, List<Restaurant> mRestaurant){
        this.mContext = mContext;
        this.mRestaurant = mRestaurant;
    }


    @NonNull
    @Override  //viewholder를 생성 시점에 자동호출됨 (recyclerview.holder를 확장한 viewholder class 선언)
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //각 item을 위한 layout을 안에서 inflation한 후에
        View view = LayoutInflater.from(mContext).inflate(R.layout.restaurant_item, parent, false);

        // Viewholder
        return new RestaurantAdapter.ViewHolder(view);
    }

    //ViewHolder 재사용을 위함 스크롤하여 위에 없어지는 ViewHolder를 재사용하기 위함
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // 몇 번째 item을?
        Restaurant restaurant = mRestaurant.get(position);
        Glide.with(mContext).load(restaurant.getImg()).into(holder.imageView);

//        holder.setItem(item);
        holder.res_name.setText(restaurant.getName());
        holder.price.setText(restaurant.getPrice());
        holder.min_del.setText(restaurant.getMin_del());
        holder.max_del.setText(restaurant.getMax_del());
        holder.res_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, EatDetailActivity.class);
                intent.putExtra("res_name", restaurant.getName());
                intent.putExtra("res_img", restaurant.getImg());
                intent.putExtra("min_del", restaurant.getMin_del());
                intent.putExtra("max_del", restaurant.getMax_del());
                intent.putExtra("price", restaurant.getPrice());
                mContext.startActivity(intent);
            }
        });

    }

    @Override // 객체 몇개냐
    public int getItemCount() {
        return mRestaurant.size();
    }

    // ViewHolder : RecyclerView에 보여질 각 항목
    static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView res_name;
        public TextView price;
        public TextView min_del;
        public TextView max_del;
        ShapeableImageView imageView;
        LinearLayout res_item;
        //생성자 함수
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            res_name = itemView.findViewById(R.id.res_name);
            price = itemView.findViewById(R.id.price);
            min_del = itemView.findViewById(R.id.min_del);
            max_del = itemView.findViewById(R.id.max_del);
            imageView =itemView.findViewById(R.id.imageView);

            res_item = itemView.findViewById(R.id.res_item);

        }


    }

}


package org.techtown.dangguen;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailActivity extends AppCompatActivity {
    TextView res_name, price, min_del, max_del, username;
    CircleImageView profile_image;
    ShapeableImageView res_img;
    String  image, manager_id;
    Button btn_join, btn_back;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        username = findViewById(R.id.username);
        btn_join = findViewById(R.id.btn_join);
        btn_back = findViewById(R.id.btn_back);
        res_name = findViewById(R.id.res_name);
        profile_image = findViewById(R.id.profile_image);
        min_del = findViewById(R.id.min_del);
        max_del = findViewById(R.id.max_del);
        price = findViewById(R.id.price);
        res_img = findViewById(R.id.res_image);

//        Log.d("&&",getIntent().getStringExtra("user_img").toString());
//        image = getIntent().getStringExtra("user_img");





        manager_id = getIntent().getStringExtra("manager_id");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String uname = snapshot.child(manager_id).child("username").getValue(String.class);
                username.setText(uname);

                String uimg = snapshot.child(manager_id).child("imageURL").getValue(String.class);
                if(uimg.equals("default")){
                    profile_image.setImageResource(R.mipmap.ic_launcher);
                }else{
                    Glide.with(getApplicationContext()).load(uimg).into(profile_image);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Glide.with(getApplicationContext()).load(getIntent().getStringExtra("res_img")).into(res_img);
        res_name.setText(getIntent().getStringExtra("res_name"));
        min_del.setText(getIntent().getStringExtra("min_del"));
        max_del.setText(getIntent().getStringExtra("max_del"));
        price.setText(getIntent().getStringExtra("prcie"));

        if (user.getUid().equals(manager_id)) { //자기가 만든 방일 경우
            btn_join.setVisibility(View.INVISIBLE);
        }
        btn_back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, MessageActivity.class);
                intent.putExtra("username", getIntent().getStringExtra("username"));
                intent.putExtra("imageUri", getIntent().getStringExtra("imageUri"));
                intent.putExtra("userid", manager_id);

                startActivity(intent);
                finish();
            }
        });


//        profile_image.setImageResource(R.mipmap.ic_launcher);
//        if(image.equals("default")){
//            profile_image.setImageResource(R.mipmap.ic_launcher);
//        }else{
////            Glide.with(DetailActivity.this).load(image).into(profile_image);
//        }
//    }
}}
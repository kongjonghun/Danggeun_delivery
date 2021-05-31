package org.techtown.dangguen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.techtown.dangguen.Fragments.Frag_res1;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class EatDetailActivity extends AppCompatActivity {
    Button btn_back, btn_create;
    ImageView imageView;
    TextView  name, price, min_del, max_del;
    String res_name;
    String res_img;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eat_detail);


        imageView = findViewById(R.id.imageView);
        name = findViewById(R.id.name);

        btn_back = findViewById(R.id.btn_back);
        btn_create = findViewById(R.id.btn_create);
        price = findViewById(R.id.price);
        min_del = findViewById(R.id.min_del);
        max_del = findViewById(R.id.max_del);

        Glide.with(getApplicationContext()).load(getIntent().getStringExtra("res_img")).into(imageView);


        max_del.setText(getIntent().getStringExtra("max_del"));
        min_del.setText(getIntent().getStringExtra("min_del"));
        price.setText(getIntent().getStringExtra("price"));
        name.setText(getIntent().getStringExtra("res_name"));

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EatDetailActivity.this, MainActivity.class);

                reference = FirebaseDatabase.getInstance().getReference("Roomlist").push(); // Firebase "Users" 목록

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("res_name", getIntent().getStringExtra("res_name"));
                hashMap.put("res_img", getIntent().getStringExtra("res_img"));
                hashMap.put("manager_id", user.getUid());
                hashMap.put("min_del", getIntent().getStringExtra("min_del"));
                hashMap.put("max_del", getIntent().getStringExtra("max_del"));
                hashMap.put("price", getIntent().getStringExtra("price"));

                reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            finish();
                        }
                    }
                });
            }
        });

    }
}
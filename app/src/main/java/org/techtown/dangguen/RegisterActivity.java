package org.techtown.dangguen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity
{
    private EditText username,email,password,password2;
    private Button btn_register;

    CheckBox cb;
    //TextView gpsResult;
    boolean check = true;

    public double longitude;
    public double latitude;

    FirebaseAuth auth;
    DatabaseReference reference;

    public final LocationListener gpsLocationListener = new LocationListener()
    {
        @Override
        public void onLocationChanged(@NonNull Location location)
        {
            String provider = location.getProvider(); // 위치정보
            longitude = location.getLongitude(); // 위도
            latitude = location.getLatitude(); // 경도

            //gpsResult.setText("위도 : " + longitude + "\n" + "경도 : " + latitude + "\n"); // 테스트용

            check = true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        password2 = findViewById(R.id.password2);
        email = findViewById(R.id.email);
        btn_register = findViewById(R.id.btn_register);
        cb = findViewById(R.id.checkBox);
        //gpsResult = (TextView) findViewById(R.id.gps_info);

        // 버전 체크
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }

        // 위치 관리자 객체 참조
        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        cb.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    if(cb.isChecked()) // 체크박스가 체크되면,
                    {
                        if (check == true)
                        {
                            // GPS 제공자의 정보가 바뀌면 콜백하도록 리스너 등록하기
                            // 위치 정보 갱신하기
                            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, // 등록할 위치제공자
                                    100, // 통지사이의 최소 시간간격 (miliSecond)
                                    1,// 통지사이의 최소 변경거리 (m)
                                    gpsLocationListener);
                            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 등록할 위치제공자
                                    100, // 통지사이의 최소 시간간격 (miliSecond)
                                    1, // 통지사이의 최소 변경거리 (m)
                                    gpsLocationListener);

                            check = false;
                        }
                    }
                    else
                    {
                        lm.removeUpdates(gpsLocationListener); // 미수신할때는 반드시 자원해체를 해주어야 한다.
                    }
                } catch (SecurityException ex)
                {
                }
            }
        });

        auth = FirebaseAuth.getInstance();

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_username = username.getText().toString();
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
                String longitude_s = Double.toString(longitude);
                String latitude_s = Double.toString(latitude);

                if(TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                    Toast.makeText(RegisterActivity.this, "입력란을 모두 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else if(txt_password.length() < 6){
                    Toast.makeText(RegisterActivity.this, "비밀번호 6자 이상", Toast.LENGTH_SHORT).show();
                }else{
                    register(txt_username, txt_email, txt_password, longitude_s, latitude_s);
                }
            }
        });


    }
    private void register(String username, String email, String password, String longitude, String latitude){

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    assert firebaseUser != null;
                    String userid = firebaseUser.getUid();

                    reference = FirebaseDatabase.getInstance().getReference("Users").child(userid); // Firebase "Users" 목록

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("id", userid);
                    hashMap.put("username", username);
                    hashMap.put("imageURL", "default");
                    hashMap.put("x", longitude);
                    hashMap.put("y", latitude);


                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent); // change
                                finish();
                            }
                        }
                    });
                }else{
                    Toast.makeText(RegisterActivity.this, "You can't register woth this email or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}





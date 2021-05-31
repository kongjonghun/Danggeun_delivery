package org.techtown.dangguen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.techtown.dangguen.Model.User;

public class MyMapActivity extends AppCompatActivity implements OnMapReadyCallback
{
    private GoogleMap mMap; // 구글맵

    //FirebaseAuth firebaseAuth;
    //FirebaseUser firebaseUser;
    //DatabaseReference reference;

    //public double longitude;
    //public double latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_map);

        // 맵 생성
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // 버전 체크
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MyMapActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }

        // 위치 관리자 객체 참조
        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

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

        //firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //firebaseAuth = FirebaseAuth.getInstance();
        //reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        //reference.addValueEventListener(new ValueEventListener()
        //{
        //@Override
        //public void onDataChange(@NonNull DataSnapshot snapshot)
        //{
        //User user = snapshot.getValue(User.class);
        // gps.setText(user.getX() + "\n" + user.getY()); // 테스트용 내 위경도 가져오기
        //longitude = Double.parseDouble(user.getX()); // 형변환해서 double로 저장
        //latitude = Double.parseDouble(user.getY());
        //}

        //@Override
        //public void onCancelled(@NonNull DatabaseError error)
        //{

        //}
        //});
    }

    public final LocationListener gpsLocationListener = new LocationListener()
    {
        @Override
        public void onLocationChanged(@NonNull Location location)
        {
            double longitude = location.getLongitude(); // 위도
            double latitude = location.getLatitude(); // 경도

            LatLng curPoint = new LatLng(latitude, longitude); // double로 저장한거 가져와서 내 현 위치 객체 생성
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 17)); // 화면 줌
            showMyLocationMarker(curPoint); // showMyLocationMarker 메서드 호출 (마커 보여주는 것)
        }

        private void showMyLocationMarker(LatLng curPoint) // 마커 생성
        {
            MarkerOptions myLocationMarker = new MarkerOptions(); // 마커 옵션 주기
            myLocationMarker.position(curPoint);
            myLocationMarker.title("내 위치");
            myLocationMarker.snippet("GPS로 확인한 위치");
            BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.mylocation);
            Bitmap b=bitmapdraw.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b, 80, 80, false);
            myLocationMarker.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

            mMap.addMarker(myLocationMarker); // 마커 생성
        }
    };

    public void onMapReady(final GoogleMap googleMap) // 지도 생성
    {
        mMap = googleMap;

        // 지도의 초기위치를 서울로 이동
        setDefaultLocation();
    }

    private void setDefaultLocation()
    {
        //디폴트 위치, Seoul
        LatLng DEFAULT_LOCATION = new LatLng(37.56, 126.97);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 10);
        mMap.moveCamera(cameraUpdate);
    }
}
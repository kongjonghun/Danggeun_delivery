package org.techtown.dangguen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.dangguen.Fragments.Frag_Chat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class ChatMapActivity extends AppCompatActivity implements OnMapReadyCallback
{
    public GoogleMap mMap; // 구글맵

    private Button button;
    private TextView tv_geocoder;
    private Button button2;
    private Geocoder geocoder; // 수령장소
    private Geocoder geocoder3; // poi

    String host_id;

    //guest id 가져오기
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String geust_id = user.getUid();

    String location;
    String pickup_geocode;
    public static double longitude;
    public static double latitude;
    public static double poi_latitide;
    public static double poi_longitude;
    public static String poi_name;
    private RequestQueue mQ;


    List list = new ArrayList();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //host id 가져오기
        host_id = getIntent().getStringExtra("userid");

        // 수령장소 추천
        jsonParse();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_map);

        //Intent myIntent = new Intent(this, MessageActivity.class);

        tv_geocoder = findViewById(R.id.tv_geocoder); // 주소 텍스트
        button = findViewById(R.id.button); // 전송하기 버튼
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent();
                intent.putExtra("location", location);
                setResult(5678,intent);
                finish();
            }
        });

        button2 = findViewById(R.id.button2); // 뒤로가기 버튼
        button2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        // 맵 생성
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void jsonParse()
    {
        String URL = "http://3.34.105.174/db/db_cal_cent.php?host="+ host_id +"&geust="+geust_id;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++)
                    {
                        JSONObject object = array.getJSONObject(i);
                        String lat = object.getString("lat");
                        String lng = object.getString("long");

                        list.add(lat);
                        list.add(lng);

                        latitude = Double.parseDouble(lat);
                        longitude = Double.parseDouble(lng);

                        Log.d("JSON", String.valueOf(lat));
                        Log.d("JSON", String.valueOf(lng));
                    }
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }

                jsonParse2();
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.d("tag", "onErrorResponse: ");
            }
        });
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void jsonParse2()
    {
        String URL = "http://3.34.105.174/db/poi.php?host="+ host_id +"&geust="+geust_id;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {

                try
                {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++)
                    {
                        JSONObject object = array.getJSONObject(i);
                        String lat = object.getString("y");
                        String lng = object.getString("x");
                        String name = object.getString("name");

                        poi_name = name;
                        poi_latitide = Double.parseDouble(lat);
                        poi_longitude = Double.parseDouble(lng);

                        Log.d("poi_lat", String.valueOf(poi_latitide));
                        Log.d("poi_long", String.valueOf(poi_longitude));
                        Log.d("poi_name", String.valueOf(poi_name));
                    }
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
                setMap();
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.d("tag", "onErrorResponse: ");
            }
        });
        Volley.newRequestQueue(this).add(stringRequest);
    }


    public void onMapReady(final GoogleMap googleMap) // 지도 생성
    {
        this.mMap = googleMap;

        Log.d("ppp2", tv_geocoder.toString());
    }

    public void setMap()
    {
        LatLng Point = new LatLng(latitude, longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Point, 16)); // 화면 줌

        Log.d("JSONs", String.valueOf(latitude));
        Log.d("JSONs", String.valueOf(longitude));

        // marker 표시
        MarkerOptions marker = new MarkerOptions();
        marker.position(Point).title("수령장소").snippet("추천된 수령장소 입니다.");
        BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.mylocation);
        Bitmap b=bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, 80, 80, false);
        marker.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
        mMap.addMarker(marker);

        geocoder = new Geocoder(this);
        geocoder3 = new Geocoder(this);

        // 지오코딩
        List<Address> list = null;
        try
        {
            list = geocoder.getFromLocation(latitude, longitude, 10); // 얻어올 값의 개수
        } catch (IOException e)
        {
            e.printStackTrace();
            Log.e("test", "입출력 오류");
        }
        if (list != null)
        {
            if (list.size()==0)
            {
                tv_geocoder.setText("해당되는 주소 정보는 없습니다");
            }
            else {
                tv_geocoder.setText(list.get(0).getAddressLine(0));
            }
        }
        location = list.get(0).getAddressLine(0);

        // poi 마커
        poi_marker();
    }


    private void poi_marker()
    {
        LatLng poi_point = new LatLng(poi_latitide, poi_longitude);

        // marker 표시
        MarkerOptions marker = new MarkerOptions();
        marker.position(poi_point).title(poi_name).snippet("보다 안전한 곳에서 수령하세요.");
        BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.poipoi);
        Bitmap b=bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, 80, 80, false);
        marker.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
        mMap.addMarker(marker);

        // POI 정보창 클릭 리스너
        mMap.setOnInfoWindowClickListener(infoWindowClickListeners);
    }

    // POI 정보창 클릭 리스너
    GoogleMap.OnInfoWindowClickListener infoWindowClickListeners = new GoogleMap.OnInfoWindowClickListener()
    {
        @Override
        public void onInfoWindowClick(Marker marker)
        {
            // 지오코딩
            List<Address> list = null;
            try
            {
                list = geocoder3.getFromLocation(poi_latitide, poi_longitude, 10); // 얻어올 값의 개수
            } catch (IOException e)
            {
                e.printStackTrace();
                Log.e("test", "입출력 오류");
            }
            if (list != null)
            {
                if (list.size()==0)
                {
                    tv_geocoder.setText("해당되는 주소 정보는 없습니다");
                }
                else {
                    tv_geocoder.setText(list.get(0).getAddressLine(0));
                }
            }
            location = list.get(0).getAddressLine(0);
        }
    };
}
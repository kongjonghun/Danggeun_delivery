package org.techtown.dangguen.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.dangguen.DetailActivity;
import org.techtown.dangguen.EatDetailActivity;
import org.techtown.dangguen.FoodDetailActivity;
import org.techtown.dangguen.MainActivity;
import org.techtown.dangguen.MessageActivity;
import org.techtown.dangguen.Model.User;
import org.techtown.dangguen.MyMapActivity;
import org.techtown.dangguen.R;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;



public class Frag_FoodMap extends Fragment implements OnMapReadyCallback
{

    private static ArrayList names;
    private static ArrayList list_x;
    private static ArrayList list_y;
    private static ArrayList list_category;

    private View view;
    MapView mapView;
    public static double longitude;
    public static double latitude;


    private GoogleMap mMap; // 구글맵
    private Context context;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {

    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

    }

    private void jsonParse()
    {
        names = new ArrayList();
        list_x = new ArrayList();
        list_y = new ArrayList();
        list_category = new ArrayList();

        String URL = "http://3.34.105.174/db/res_position.php";
        Log.d("URLl", URL);
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
                        String name = object.getString("name");
                        String x = object.getString("x");
                        String y = object.getString("y");
                        String category = object.getString("category");

                        latitude = Double.parseDouble(y);
                        longitude = Double.parseDouble(x);

                        Log.d("double", String.valueOf(latitude));
                        Log.d("double", String.valueOf(longitude));

                        names.add(name);
                        list_x.add(longitude);
                        list_y.add(latitude);
                        list_category.add(category);
                    }
                    setMap();

                } catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag", "onErrorResponse: ");
            }
        });
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.activity_foodmap, container, false);

        mapView = (MapView) view.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        context = container.getContext();

        return view;
    }

    @Override
    public void onResume()
    {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
        mapView.onLowMemory();
    }


    public void setMap()
    {
        Log.d("res1", names.toString());
        Log.d("resx", list_x.toString());
        Log.d("resy", list_y.toString());
        Log.d("rescate", list_category.toString());

        for(int i = 0; i< names.size(); i++)
        {
            LatLng food = new LatLng((Double) list_y.get(i), (Double) list_x.get(i));
            LatLng startfood = new LatLng(37.58395034890324,127.05341736880399);

            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(startfood, 16);
            mMap.moveCamera(cameraUpdate);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(food);
            markerOptions.title((String) names.get(i));


            if (list_category.get(i).equals("americanfood"))
            {
                BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.americanfood);
                Bitmap b = bitmapdraw.getBitmap();
                Bitmap smallMarker = Bitmap.createScaledBitmap(b, 70, 70, false);
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
            }
            else if ((list_category.get(i).equals("japanesefood")))
            {
                BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.japanesefood);
                Bitmap b = bitmapdraw.getBitmap();
                Bitmap smallMarker = Bitmap.createScaledBitmap(b, 70, 70, false);
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
            }
            else if ((list_category.get(i).equals("koreanfood")))
            {
                BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.koreanfood);
                Bitmap b = bitmapdraw.getBitmap();
                Bitmap smallMarker = Bitmap.createScaledBitmap(b, 70, 70, false);
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
            }
            else if ((list_category.get(i).equals("fastfood")))
            {
                BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.fastfood);
                Bitmap b = bitmapdraw.getBitmap();
                Bitmap smallMarker = Bitmap.createScaledBitmap(b, 70, 70, false);
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
            }
            else if ((list_category.get(i).equals("chicken")))
            {
                BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.chicken);
                Bitmap b = bitmapdraw.getBitmap();
                Bitmap smallMarker = Bitmap.createScaledBitmap(b, 70, 70, false);
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
            }
            else if ((list_category.get(i).equals("drink")))
            {
                BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.beverage);
                Bitmap b = bitmapdraw.getBitmap();
                Bitmap smallMarker = Bitmap.createScaledBitmap(b, 70, 70, false);
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
            }
            else if ((list_category.get(i).equals("tai")))
            {
                BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.tai);
                Bitmap b = bitmapdraw.getBitmap();
                Bitmap smallMarker = Bitmap.createScaledBitmap(b, 70, 70, false);
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
            }
            else
            {

            }

            mMap.addMarker(markerOptions); // 마커 생성
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        MapsInitializer.initialize(this.getActivity());
        this.mMap = googleMap;
        // 지도의 초기위치를 서울로 이동
        setDefaultLocation();
        jsonParse();

        //정보창 클릭 리스너
        googleMap.setOnInfoWindowClickListener(infoWindowClickListener);

    }

    //정보창 클릭 리스너
    GoogleMap.OnInfoWindowClickListener infoWindowClickListener = new GoogleMap.OnInfoWindowClickListener()
    {
        @Override
        public void onInfoWindowClick(Marker marker)
        {
            String markerTitle = marker.getTitle();
            Intent intent = new Intent(getActivity(), FoodDetailActivity.class);
            startActivity(intent);
        }
    };

    private void setDefaultLocation()
    {
        //디폴트 위치
        LatLng DEFAULT_LOCATION = new LatLng(37.58, 127.05);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 15);
        mMap.moveCamera(cameraUpdate);
    }
}

